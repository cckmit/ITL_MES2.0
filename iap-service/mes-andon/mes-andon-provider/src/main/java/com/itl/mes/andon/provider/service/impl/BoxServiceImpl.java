package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.bo.BoxHandlerBO;
import com.itl.mes.andon.api.entity.Box;
import com.itl.mes.andon.api.service.BoxService;
import com.itl.mes.andon.api.vo.BoxForShowVo;
import com.itl.mes.andon.api.vo.BoxQueryVo;
import com.itl.mes.andon.api.vo.BoxVo;
import com.itl.mes.andon.provider.mapper.BoxMapper;
import com.itl.mes.core.api.bo.DeviceHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service("boxService")
@Transactional
public class BoxServiceImpl extends ServiceImpl<BoxMapper, Box> implements BoxService {
    @Resource
    private BoxMapper boxMapper;

    @Resource
    private UserUtil userUtil;

    private String[] types = {"CJ", "CX", "GW", "SB"};

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveByBoxVo(BoxVo boxVo) throws CommonException {
        String site = UserUtils.getSite();

        Box box = new Box();
        BoxHandlerBO boxHandlerBO = new BoxHandlerBO(site, boxVo.getBox());
        String bo = boxHandlerBO.getBo();

        box.setBo(bo).setBox(boxVo.getBox()).setBoxName(boxVo.getBoxName())
                .setBoxDesc(boxVo.getBoxDesc()).setSite(site);
        Date newDate = new Date();
        if (!StrUtil.isBlank(boxVo.getWorkShop()) && !StrUtil.isBlank(boxVo.getProductLine())
                && !StrUtil.isBlank(boxVo.getStation()) && !StrUtil.isBlank(boxVo.getDevice())
        ) {
            throw new CommonException("只能选择一种资源类型", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        if (!StrUtil.isBlank(boxVo.getWorkShop())) {
            String workShopBo = new WorkShopHandleBO(site, boxVo.getWorkShop()).getBo();
            box.setWorkShopBo(workShopBo).setResourceType(boxMapper.getResourceType(types[0]));

            box.setProductLineBo("").setStationBo("").setDeviceBo("");

        } else if (!StrUtil.isBlank(boxVo.getProductLine())) {
            String productLineBO = new ProductLineHandleBO(site, boxVo.getProductLine()).getBo();
            box.setProductLineBo(productLineBO).setResourceType(boxMapper.getResourceType(types[1]));

            box.setWorkShopBo("").setStationBo("").setDeviceBo("");

        } else if (!StrUtil.isBlank(boxVo.getStation())) {
            String stationBo = new StationHandleBO(site, boxVo.getStation()).getBo();
            box.setStationBo(stationBo).setResourceType(boxMapper.getResourceType(types[2]));

            box.setProductLineBo("").setWorkShopBo("").setDeviceBo("");

        } else if (!StrUtil.isBlank(boxVo.getDevice())) {
            String deviceBo = new DeviceHandleBO(site, boxVo.getDevice()).getBo();
            box.setDeviceBo(deviceBo).setResourceType(boxMapper.getResourceType(types[3]));

            box.setProductLineBo("").setStationBo("").setWorkShopBo("");

        } else {
            throw new CommonException("未选择资源类型", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        Box box_db = boxMapper.selectById(bo);
        if (box_db == null) {
            box.setState("1");
            box.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), newDate);
            boxMapper.insert(box);
            boxVo.setModifyDate(newDate);
        } else {
            box.setState(boxVo.getState());
            Date frontModifyDate = boxVo.getModifyDate();
            Date modifyDate = box_db.getModifyDate();
            if (frontModifyDate == null) {
                throw new CommonException("该编号灯箱已存在!", CommonExceptionDefinition.TIME_VERIFY_EXCEPTION);
            }
            else {
                if (frontModifyDate.getTime() != modifyDate.getTime()) {
                    throw new CommonException("前台传送的时间和从数据库查询的时间不相等!", CommonExceptionDefinition.TIME_VERIFY_EXCEPTION);
                }
            }
            box.setCreateUser(box_db.getCreateUser());
            box.setCreateDate(box_db.getCreateDate());
            box.setModifyUser(userUtil.getUser().getUserName());
            box.setModifyDate(newDate);
            Integer updateInt = boxMapper.updateById(box);
            if (updateInt == 0) {
                throw new CommonException("数据已修改，请重新查询再执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            boxVo.setModifyDate(newDate);
        }
    }

    @Override
    public BoxVo getBoxVoByBox(String box) throws CommonException {
        Box box_db = selectByBox(box);
        BoxVo boxVo = new BoxVo();
        BeanUtils.copyProperties(box_db, boxVo);
        if (box_db.getWorkShopBo() != null && !box_db.getWorkShopBo().equals("")) {
            WorkShopHandleBO workShop = new WorkShopHandleBO(box_db.getWorkShopBo());
            if (workShop != null) {
                boxVo.setWorkShop(workShop.getWorkShop());
            }
        }
        if (box_db.getProductLineBo() != null && !box_db.getProductLineBo().equals("")) {
            ProductLineHandleBO productLine = new ProductLineHandleBO(box_db.getProductLineBo());
            if (productLine != null) {
                boxVo.setProductLine(productLine.getProductLine());
            }
        }
        if (box_db.getStationBo() != null && !box_db.getStationBo().equals("")) {
            StationHandleBO station = new StationHandleBO(box_db.getStationBo());
            if (station != null) {
                boxVo.setStation(station.getStation());
            }
        }
        if (box_db.getDeviceBo() != null && !box_db.getDeviceBo().equals("")) {
            DeviceHandleBO device = new DeviceHandleBO(box_db.getDeviceBo());
            if (device != null) {
                boxVo.setDevice(device.getDevice());
            }
        }
        return boxVo;
    }

    @Override
    public Box selectByBox(String box) throws CommonException {
        QueryWrapper<Box> query = new QueryWrapper<>();
        query.eq(Box.BOX, box);
        List<Box> boxes = boxMapper.selectList(query);
        if (boxes.isEmpty()) {
            throw new CommonException("灯箱:" + box + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        } else {
            return boxes.get(0);
        }
    }

    @Override
    public List<Box> selectByBoxVo(BoxVo boxVo) throws CommonException {
        QueryWrapper<Box> query = new QueryWrapper<>();
        if (boxVo.getBox() != null) {
            query.like(Box.BOX, boxVo.getBox());
        }
        if (boxVo.getBoxName() != null) {
            query.like(Box.BOX_NAME, boxVo.getBoxName());
        }
        if (boxVo.getBoxDesc() != null) {
            query.like(Box.BOX_DESC, boxVo.getBoxDesc());
        }
        List<Box> boxes = boxMapper.selectList(query);
        return boxes;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int delete(String[] boxes) throws CommonException {
        List<String> bos = new ArrayList<>();
        String site = UserUtils.getSite();
        Arrays.asList(boxes).forEach(box -> bos.add(new BoxHandlerBO(site, box).getBo()));
        int deleteInt = 0;
        try {
            deleteInt = boxMapper.deleteBatchIds(bos);
        } catch (Exception e) {
            throw new CommonException("本次修改的数据中有关联项,请先去删除关联项!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return deleteInt;
    }

    @Override
    public IPage<BoxForShowVo> queryPage(BoxQueryVo boxQueryVo) throws CommonException {
        if (ObjectUtil.isEmpty(boxQueryVo.getPage())) {
            boxQueryVo.setPage(new Page(0, 10));
        }
        boxQueryVo.getPage().setDesc("ab.CREATE_DATE");
        boxQueryVo.setSite(UserUtils.getSite());
        return boxMapper.findList(boxQueryVo.getPage(), boxQueryVo);
    }

    @Override
    public List<Box> selectBox(String box, String boxName, String boxDesc) throws CommonException {
        QueryWrapper<Box> query = new QueryWrapper<>();
        if (box != null) {
            query.like(Box.BOX, box);
        }
        if (boxName != null) {
            query.like(Box.BOX_NAME, boxName);
        }
        if (boxDesc != null) {
            query.like(Box.BOX_DESC, boxDesc);
        }
        List<Box> boxes = boxMapper.selectList(query);
        return boxes;
    }

    @Override
    public IPage<BoxForShowVo> queryPageForUse(BoxQueryVo boxQueryVo) throws CommonException {
        if (ObjectUtil.isEmpty(boxQueryVo.getPage())) {
            boxQueryVo.setPage(new Page(0, 10));
        }
        boxQueryVo.getPage().setDesc("ab.CREATE_DATE");
        boxQueryVo.setSite(UserUtils.getSite());
        boxQueryVo.setState("1");
        return boxMapper.findList(boxQueryVo.getPage(), boxQueryVo);
    }
}
