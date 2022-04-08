package com.itl.iap.mes.provider.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.CorrectiveMaintenance;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.provider.annotation.ParseState;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.CorrectiveMaintenanceMapper;
import com.itl.iap.mes.provider.mapper.MesFilesMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class CorrectiveMaintenanceServiceImpl {

    @Autowired
    private CorrectiveMaintenanceMapper correctiveMaintenanceMapper;


    @ParseState(Constant.RepairEnum.class)
    public Object findList(CorrectiveMaintenance correctiveMaintenance, Integer pageNum, Integer pageSize) {
        correctiveMaintenance.setSiteId(UserUtils.getSite());
        Page page = new Page(pageNum,pageSize);
        return correctiveMaintenanceMapper.findList(page,correctiveMaintenance);
    }

    @Autowired
    private MesFilesMapper mesFilesMapper;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    public void save(CorrectiveMaintenance correctiveMaintenance) throws CommonException{

        if(StringUtils.isNotBlank(correctiveMaintenance.getCode())){
            List<Map<String, Object>> deviceList = correctiveMaintenanceMapper.getDevice(correctiveMaintenance.getCode());
            if(!deviceList.isEmpty()){
                correctiveMaintenance.setProductionLine(deviceList.get(0).get("productLineBo") == null ? "" : deviceList.get(0).get("productLineBo").toString());
                correctiveMaintenance.setStation(deviceList.get(0).get("stationBo") == null ? "" : deviceList.get(0).get("stationBo").toString());
            }
        }
        if (correctiveMaintenance.getRepairStartTime() != null && correctiveMaintenance.getRepairEndTime() !=null){
            if (correctiveMaintenance.getRepairStartTime().getTime() > correctiveMaintenance.getRepairEndTime().getTime()){
                throw new CommonException("维修开始时间不能大于维修完成时间", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        correctiveMaintenance.setSiteId(UserUtils.getSite());
        if(correctiveMaintenance.getId() != null){
            correctiveMaintenanceMapper.updateById(correctiveMaintenance);
        }else{
            QueryWrapper<CorrectiveMaintenance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code",correctiveMaintenance.getCode());
            Integer count = correctiveMaintenanceMapper.selectCount(queryWrapper);
//            if(count > 0){
//                throw new CustomException(CommonCode.CODE_REPEAT);
//            }
            correctiveMaintenance.setCreateTime(new Date());
            correctiveMaintenanceMapper.insert(correctiveMaintenance);
        }
        saveFile(correctiveMaintenance);
    }
    @Transactional(rollbackFor = Exception.class)
    public void saveFile(CorrectiveMaintenance correctiveMaintenance) {
        if(correctiveMaintenance.getMesFilesList()!=null && !correctiveMaintenance.getMesFilesList().isEmpty()){
            LambdaQueryWrapper<MesFiles> query = new QueryWrapper<MesFiles>().lambda().eq(MesFiles::getGroupId, correctiveMaintenance.getId());
            correctiveMaintenance.getMesFilesList().forEach(file -> {
                query.ne(MesFiles::getId, file.getId());
            });
            List<MesFiles> mesFiles = mesFilesMapper.selectList(query);
            mesFiles.forEach(mes-> mesFilesMapper.deleteById(mes.getId()));
            correctiveMaintenance.getMesFilesList().forEach(file -> {
                file.setGroupId(correctiveMaintenance.getId());
                mesFilesMapper.updateById(file);
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            correctiveMaintenanceMapper.deleteById(id);
        });
    }

    public CorrectiveMaintenance getById(String id) {
        CorrectiveMaintenance correctiveMaintenance = correctiveMaintenanceMapper.selectById(id);

        QueryWrapper<MesFiles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("groupId",id);
        List<MesFiles> select = mesFilesMapper.selectList(queryWrapper);
        correctiveMaintenance.setMesFilesList(select);
        return correctiveMaintenance;
    }

}
