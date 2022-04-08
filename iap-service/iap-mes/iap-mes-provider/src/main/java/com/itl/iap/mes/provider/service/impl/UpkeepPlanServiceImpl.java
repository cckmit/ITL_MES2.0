package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.mes.api.entity.CheckPlan;
import com.itl.iap.mes.api.entity.ScheduleJobEntity;
import com.itl.iap.mes.api.entity.UpkeepExecute;
import com.itl.iap.mes.api.entity.UpkeepPlan;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.CorrectiveMaintenanceMapper;
import com.itl.iap.mes.provider.mapper.JobMapper;
import com.itl.iap.mes.provider.mapper.UpkeepPlanMapper;
import com.itl.iap.mes.provider.utils.*;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class UpkeepPlanServiceImpl {

    @Autowired
    private UpkeepPlanMapper upkeepPlanMapper;

    @Autowired
    CorrectiveMaintenanceMapper correctiveMaintenanceMapper;

    @Autowired
    private UpkeepExecuteServiceImpl upkeepExecuteService;

    @Autowired
    private UserUtil userUtil;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private JobMapper jobMapper;

    @Transactional
    public void save(UpkeepPlan upkeepPlan, Integer type) {

        upkeepPlan.setRunState(type);
        if(upkeepPlan.getCycle().intValue()<=0){
            throw new CustomException(CommonCode.NUMBER_ERROR);
        }
        if(!upkeepPlan.getUpkeepPlanName().equals("2")){
            if(upkeepPlan.getStartTime() == null || upkeepPlan.getEndTime() == null){
                throw new CustomException(CommonCode.TIME_FAILE);
            }
            if(upkeepPlan.getStartTime().getTime()>upkeepPlan.getEndTime().getTime()){
                throw new CustomException(CommonCode.START_END);
            }
            if(upkeepPlan.getStartTime().getTime()<=new Date().getTime()){
                throw new CustomException(CommonCode.TIME_FAIL);
            }
        }

        if(StringUtils.isNotBlank(upkeepPlan.getId())){
            upkeepPlanMapper.updateById(upkeepPlan);
            String jobIds = upkeepPlan.getJobIds();
            if(StringUtils.isNotBlank(jobIds)){
                Arrays.asList(jobIds.split(",")).forEach(id->{
                    if(StringUtils.isNotBlank(id)){
                        Scheduler scheduler = null;
                        try {
                            scheduler = new StdSchedulerFactory().getScheduler();
                        } catch (SchedulerException e) {
                            throw new CustomException(CommonCode.SUCCESS);
                        }
                        ScheduleUtils.deleteScheduleJob(scheduler, Long.parseLong(id));
                        jobMapper.deleteById(Long.parseLong(id));
                    }
                });
            }

        }else{
            //该保养计划下设备类型是否重复
            if(upkeepPlan.getType() !=null && upkeepPlan.getType() !=""){
                List<UpkeepPlan> upkeepPlans=upkeepPlanMapper.selectList(new QueryWrapper<UpkeepPlan>().eq("upkeepPlanName",upkeepPlan.getUpkeepPlanName()));
                for(UpkeepPlan upkeepPlan1:upkeepPlans){
                    if(upkeepPlan.getType().equals(upkeepPlan1.getType())){
                        throw new CustomException(CommonCode.CHECK_REPEAT);
                    }
                }
            }
            upkeepPlan.setCreateTime(new Date());
            upkeepPlan.setCreateAccount(userUtil.getUser().getUserName());
            upkeepPlan.setCreateName(userUtil.getUser().getRealName());
            upkeepPlanMapper.insert(upkeepPlan);
        }
        String planId = upkeepPlan.getId();
        upkeepPlan.setId(planId);
        List<String> list = new ArrayList<>();
        String ids = "";
        String str="";
        if(!upkeepPlan.getUpkeepPlanName().equals("2")){
            if((upkeepPlan.getStartTime() !=null) && (upkeepPlan.getEndTime() !=null)){
                Date startTimeDate = upkeepPlan.getStartTime();
                str = sdf.format(startTimeDate);
                /*Integer cycle = upkeepPlan.getCycle();
                Integer ytd = upkeepPlan.getYtd();
                if(ytd.equals(Constant.YtdEnum.MONTH.getItem())){
                    //季度
                    TimeUtils.digui(list,startTime,sdf.format(upkeepPlan.getEndTime()),cycle,Constant.YtdEnum.MONTH.getItem());
                }else if(ytd.equals(Constant.YtdEnum.DAY.getItem())){
                    //周
                    TimeUtils.digui(list,startTime,sdf.format(upkeepPlan.getEndTime()),cycle,Constant.YtdEnum.DAY.getItem());
                }*/
            }else{
                throw new CustomException(CommonCode.TIME_FAILE);
            }
            //创建定时任务，返回id
            ids=ScheduleJob(ids,str,upkeepPlan,type);

        }
        if(upkeepPlan.getUpkeepPlanName().equals("2")){

            List<Map<String, Object>> device = correctiveMaintenanceMapper.getDevice(upkeepPlan.getCode());

            UpkeepExecute upkeepExecute = new UpkeepExecute();
            if(device != null && !device.isEmpty()){
                upkeepExecute.setProductionLine(device.get(0).get("productLineBo")==null?"":device.get(0).get("productLineBo").toString());
            }
            upkeepExecute.setUpkeepPlanName(upkeepPlan.getUpkeepPlanName());
            upkeepExecute.setCode(upkeepPlan.getCode());
            upkeepExecute.setName(upkeepPlan.getName());
            upkeepExecute.setType(upkeepPlan.getType());
            upkeepExecute.setDeviceTypeName(upkeepPlan.getDeviceTypeName());
            upkeepExecute.setDataCollectionId(upkeepPlan.getDataCollectionId());
            upkeepExecute.setUpkeepUserId(upkeepPlan.getUpkeepUserId());
            upkeepExecute.setUpkeepUserName(upkeepPlan.getUpkeepUserName());
            upkeepExecute.setDataCollectionName(upkeepPlan.getDataCollectionName());
            upkeepExecute.setPlanExecuteTime(new Date());
            upkeepExecute.setState(0);
            upkeepExecute.setUpkeepPlanId(upkeepPlan.getId());
            upkeepExecuteService.save(upkeepExecute);
        }
        upkeepPlan.setJobIds(ids);

        upkeepPlanMapper.updateById(upkeepPlan);
    }

    //根据创建时间生成定时任务,ids定时任务id，str指定的定时任务执行时间时间,upkeepPlan 计划实体，type 固定为Integer 1
    public String ScheduleJob(String ids,String str,UpkeepPlan upkeepPlan,Integer type){
        String corn  = "";
        String planId=upkeepPlan.getId();
        try {
            corn = CornUtils.getCron(sdf.parse(str));
        } catch (ParseException e) {
            throw new CustomException(CommonCode.PARSE_TIME_FAIL);
        }
        if(StringUtils.isNotBlank(corn)){
            ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
            Long jobId = SnowFlake.getId();
            scheduleJobEntity.setJobId(jobId);
            scheduleJobEntity.setCronExpression(corn);
            if(type.equals(Constant.ScheduleStatus.PAUSE.getItem()) && upkeepPlan.getState().equals(Constant.EnabledEnum.CLOSE.getItem())){
                scheduleJobEntity.setState(Constant.ScheduleStatus.NORMAL.getItem());
            }else{
                scheduleJobEntity.setState(Constant.ScheduleStatus.PAUSE.getItem());
            }
            scheduleJobEntity.setBeanName("upkeepPlan");
            scheduleJobEntity.setCreateTime(new Date());
            Map<String, Object> params = new HashMap<>();
            params.put("running",Constant.SYS_ADMIN);
            params.put("upkeepPlan",planId);
            scheduleJobEntity.setParams(MapUtils.getMapToString(params));

            if(type.equals(Constant.RepairEnum.NO.getItem())){
                Scheduler scheduler = null;
                try {
                    scheduler = new StdSchedulerFactory().getScheduler();
                } catch (SchedulerException e) {
                    throw new CustomException(CommonCode.SUCCESS);
                }
                CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, jobId);
                //如果不存在，则创建
                if(cronTrigger == null) {
                    ScheduleUtils.createScheduleJob(scheduler, scheduleJobEntity);
                }else {
                    ScheduleUtils.updateScheduleJob(scheduler, scheduleJobEntity);
                }
               ids+=","+jobId;
                jobMapper.insert(scheduleJobEntity);
            }
        }


        return ids;
    }

    public IPage<UpkeepPlan> findList(UpkeepPlan upkeepPlan, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum,pageSize);
        return upkeepPlanMapper.findList(page,upkeepPlan);
    }
    public IPage<UpkeepPlan> findListByState(UpkeepPlan upkeepPlan, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum,pageSize);
        return upkeepPlanMapper.findListByState(page,upkeepPlan);
    }

    public UpkeepPlan findById(String id) {
        UpkeepPlan upkeepPlan = upkeepPlanMapper.selectById(id);
        return upkeepPlan;
    }


    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id->{
            UpkeepPlan upkeepPlan = upkeepPlanMapper.selectById(id);
            String jobIds = upkeepPlan.getJobIds();
            List<String> stringList = Arrays.asList(jobIds.split(","));
//            stringList.removeIf(e->{
//                if(StringUtils.isBlank(e)){
//                    return true;
//                }
//                return false;
//            });
//
//            List<Long> collect = stringList.stream().map(e -> {
//                return Long.valueOf(e);
//            }).collect(Collectors.toList());


            Scheduler scheduler = null;
            try {
                scheduler = new StdSchedulerFactory().getScheduler();
            } catch (SchedulerException e) {
                throw new CustomException(CommonCode.SUCCESS);
            }
            for(String jobId : stringList){
                if(StringUtils.isNotBlank(jobId)){
                    ScheduleUtils.deleteScheduleJob(scheduler, Long.parseLong(jobId));
                    jobMapper.deleteById(Long.parseLong(jobId));
                }
            }
            upkeepPlanMapper.deleteById(id);
        });
    }


}
