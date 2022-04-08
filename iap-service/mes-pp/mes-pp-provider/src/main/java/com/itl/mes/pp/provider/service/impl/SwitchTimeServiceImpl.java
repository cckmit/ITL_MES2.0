package com.itl.mes.pp.provider.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.pp.api.dto.SwitchTimeSaveDto;
import com.itl.mes.pp.api.entity.SwitchTimeEntity;
import com.itl.mes.pp.api.service.SwitchTimeService;
import com.itl.mes.pp.provider.mapper.SwitchTimeMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author yaoxiang
 * @date 2020/12/17
 * @since JDK1.8
 */
@Service
public class SwitchTimeServiceImpl extends ServiceImpl<SwitchTimeMapper, SwitchTimeEntity> implements SwitchTimeService {

    @Resource
    private SwitchTimeMapper switchTimeMapper;

    @Autowired
    private UserUtil userUtil;

    @Override
    public IPage<SwitchTimeEntity> queryPage(Map<String, Object> params) throws CommonException {
        params.put("isAsc", false);
        params.put("orderByField", SwitchTimeEntity.CREATE_DATE);
        QueryWrapper<SwitchTimeEntity> query = new QueryWrapper<>();
        if (!StrUtil.isBlank(params.getOrDefault("processCharacteristics", "").toString())) {
            query.like(SwitchTimeEntity.PROCESS_CHARACTERISTICS, params.get("processCharacteristics").toString());
        }
        if (!StrUtil.isBlank(params.getOrDefault("targetProcessCharacteristics", "").toString())) {
            query.like(SwitchTimeEntity.TARGET_PROCESS_CHARACTERISTICS, params.get("targetProcessCharacteristics").toString());
        }
        if (!StrUtil.isBlank(params.getOrDefault("switchType", "").toString())) {
            query.eq(SwitchTimeEntity.SWITCH_TYPE, params.get("switchType").toString());
        }
        query.eq("SITE", UserUtils.getSite());
        IPage<SwitchTimeEntity> page = page(new QueryPage<>(params), query);
        return page;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int delete(String[] bos) throws CommonException {
        int deleteInt = switchTimeMapper.deleteBatchIds(Arrays.asList(bos));
        return deleteInt;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveAndUpdate(SwitchTimeSaveDto switchTimeSaveDto) throws CommonException {
        if (switchTimeSaveDto.getProcessCharacteristics().equals(switchTimeSaveDto.getTargetProcessCharacteristics())) {
            throw new CommonException("切换前后工艺特性不能相同!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (StrUtil.isBlank(switchTimeSaveDto.getBo())) {
            SwitchTimeEntity entity = new SwitchTimeEntity();
            BeanUtil.copyProperties(switchTimeSaveDto, entity);
            Date newDate = new Date();
            entity.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), newDate);
            entity.setSite(UserUtils.getSite());
            switchTimeMapper.insert(entity);
            switchTimeSaveDto.setModifyDate(newDate);
        } else {
            Date frontModifyDate = switchTimeSaveDto.getModifyDate();
            SwitchTimeEntity entity_db = switchTimeMapper.selectById(switchTimeSaveDto.getBo());
            Date modifyDate = entity_db.getModifyDate();
            CommonUtil.compareDateSame(frontModifyDate, modifyDate);
            SwitchTimeEntity entity = new SwitchTimeEntity();
            BeanUtil.copyProperties(switchTimeSaveDto, entity);
            Date newDate = new Date();
            entity.setCreateUser(entity_db.getCreateUser());
            entity.setCreateDate(entity_db.getCreateDate());
            entity.setModifyUser(userUtil.getUser().getUserName());
            entity.setModifyDate(newDate);
            entity.setSite(UserUtils.getSite());
            int updateInt = switchTimeMapper.updateById(entity);
            if (updateInt == 0) {
                throw new CommonException("数据已修改,请重新查询再执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            switchTimeSaveDto.setModifyDate(newDate);
        }
    }

    @Override
    public void exportOperation(HttpServletResponse response) {
        Workbook workBook = null;
        try {
            List<SwitchTimeEntity> list = switchTimeMapper.selectList(null);
            ExportParams switchTimeExportParams = new ExportParams();
            switchTimeExportParams.setSheetName("生产切换时间");

            workBook = ExcelExportUtil.exportExcel(switchTimeExportParams, SwitchTimeEntity.class, list);

            String fileName = "生产切换时间表.xls";
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workBook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
