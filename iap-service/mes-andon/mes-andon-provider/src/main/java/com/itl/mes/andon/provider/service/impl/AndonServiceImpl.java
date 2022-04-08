package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.bo.AndonHandleBo;
import com.itl.mes.andon.api.dto.AndonQueryDTO;
import com.itl.mes.andon.api.dto.AndonSaveDTO;
import com.itl.mes.andon.api.entity.Andon;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.andon.api.service.AndonService;
import com.itl.mes.andon.api.vo.AndonVo;
import com.itl.mes.andon.provider.common.CommonCode;
import com.itl.mes.andon.provider.config.Constant;
import com.itl.mes.andon.provider.exception.CustomException;
import com.itl.mes.andon.provider.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class AndonServiceImpl extends ServiceImpl<AndonMapper, Andon> implements AndonService {

    @Autowired
    AndonMapper andonMapper;

    @Autowired
    RecordMapper recordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AndonSaveDTO andonSaveDTO) {

        Andon andon = new Andon();
        BeanUtil.copyProperties(andonSaveDTO,andon);
        /**
         * 目前需求安灯类型只有物料和设备，故只设置这几种取值
         */
        if (StrUtil.isNotBlank(andonSaveDTO.getAndonTypeTag())){
            switch (andonSaveDTO.getAndonTypeTag()){
                case Constant.andonTypeTagItem:
                    andon.setResourceType(Constant.andonResourceType.ITEM.getValue());
                    if(StrUtil.isNotBlank(andonSaveDTO.getRelatedObjectBo ())){
                        andon.setItemBo(andonSaveDTO.getRelatedObjectBo ());
                    }else {
                        andon.setItemBo("");
                    }
                    break;
                case Constant.andonTypeTagDevice:
                    andon.setResourceType(Constant.andonResourceType.DEVICE.getValue());
                    if(StrUtil.isNotBlank(andonSaveDTO.getRelatedObjectBo())){
                        andon.setDeviceBo(andonSaveDTO.getRelatedObjectBo());
                    }else {
                        andon.setDeviceBo("");
                    }
                    break;
                default:andon.setResourceType(Constant.andonResourceType.OTHER.getValue()); break;
            }
        }

        if(StrUtil.isNotBlank(andonSaveDTO.getBo())){
            Andon checkAndon = andonMapper.selectById(andonSaveDTO.getBo());
            switch (checkAndon.getResourceType()){
                case Constant.andonTypeTagItem:
                    andon.setItemBo(null);
                    break;
                case Constant.andonTypeTagDevice:
                    andon.setDeviceBo(null);
                    break;
                default: break;
            }
            andon.setModifyDate(new Date());
            andon.setModifyUser(UserUtils.getCurrentUser().getUserName());
            andonMapper.updateById(andon);

        }else {
            //添加唯一性校验提示
            QueryWrapper<Andon> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("andon",andonSaveDTO.getAndon());
            Integer count = andonMapper.selectCount(queryWrapper);
            if(count>0){
                throw new CustomException(CommonCode.CODE_REPEAT);
            }
            andon.setBo(new AndonHandleBo(UserUtils.getSite(), andonSaveDTO.getAndon()).getBo());
            andon.setSite(UserUtils.getSite());
            andon.setCreateDate(new Date());
            andon.setCreateUser(UserUtils.getCurrentUser().getUserName());
            andonMapper.insert(andon);
        }

    }

    @Override
    public IPage<AndonVo> findList(AndonQueryDTO andonQueryDTO) {
        if(ObjectUtil.isEmpty(andonQueryDTO.getPage())){
            andonQueryDTO.setPage(new Page(0, 10));
        }
        return andonMapper.findList(andonQueryDTO.getPage(),andonQueryDTO);
    }

    @Override
    public AndonVo findById(String id) {

        return andonMapper.findAndonById(id);
    }

    @Override
    public void deleteByIds(List<String> ids) {

        ids.forEach(id ->{
            UpdateWrapper<Record> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("andon_bo",id);
            recordMapper.delete(updateWrapper);
            andonMapper.deleteById(id);
        });

    }

    @Override
    public List<Record> findByLine(String line) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PRODUCT_LINE_BO",line);
        queryWrapper.eq("STATE",1);
        List<Record> records = recordMapper.selectList(queryWrapper);
        return records;
    }

    @Override
    public IPage<AndonVo> findListForUse(AndonQueryDTO andonQueryDTO) {
        if(ObjectUtil.isEmpty(andonQueryDTO.getPage())){
            andonQueryDTO.setPage(new Page(0, 10));
        }
        andonQueryDTO.setIsEnable(true);
        return andonMapper.findList(andonQueryDTO.getPage(),andonQueryDTO);
    }
}
