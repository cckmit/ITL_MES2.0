package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.DeviceCheckDto;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.vo.CheckPlanVo;
import com.itl.iap.mes.api.vo.DeviceCheckVo;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.*;
import com.itl.iap.mes.provider.utils.*;
import com.itl.mes.core.api.entity.Device;
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
public class CheckPlanServiceImpl{

    @Autowired
    private CheckPlanMapper checkPlanMapper;



    @Autowired
    private DeviceCheckMapper deviceCheckMapper;

    @Autowired
    private DeviceCheckItemMapper deviceCheckItemMapper;

    @Autowired
    private DataCollectionItemMapper dataCollectionItemMapper;

    @Autowired
    private UserUtil userUtil;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private JobMapper jobMapper;

    @Transactional
    public void save(CheckPlan checkPlan,Integer type) {


        checkPlan.setSiteId(UserUtils.getSite());
        checkPlan.setCreatePerson(userUtil.getUser().getRealName());
        checkPlan.setRunState(type);

        if(checkPlan.getStartTime() !=null){
            if(checkPlan.getStartTime().getTime()<=new Date().getTime()){
                throw new CustomException(CommonCode.TIME_FAIL);
            }
        }
        if(StringUtils.isNotBlank(checkPlan.getId())){
            checkPlanMapper.updateById(checkPlan);
            String jobIds = checkPlan.getJobIds();
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
            //校验该点检计划下设备类型是否重复
            if(checkPlan.getType() !=null && checkPlan.getType()  !=""){
                List<CheckPlan> checkPlans=checkPlanMapper.selectList(new QueryWrapper<CheckPlan>().eq("checkPlanName",checkPlan.getCheckPlanName()));
                for(CheckPlan checkPlan1:checkPlans){
                    if(checkPlan.getType().equals(checkPlan1.getType())){
                        throw new CustomException(CommonCode.DEVICE_REPEAT);
                    }
                }
            }
            checkPlan.setCreateTime(new Date());
            checkPlanMapper.insert(checkPlan);
        }

        String planId = checkPlan.getId();
        checkPlan.setId(planId);
        String ids = "";
        if(checkPlan.getStartTime() !=null){
            Date startTimeDate = checkPlan.getStartTime();
            String startTime = sdf.format(startTimeDate);
            //Integer cycle = checkPlan.getCycle();
            Integer ytd = checkPlan.getYtd();
            List<String> list = new ArrayList<>();
        /*if(ytd.equals(Constant.YtdEnum.MONTH.getItem())){
            TimeUtils.digui(list,startTime,sdf.format(checkPlan.getEndTime()),cycle,Constant.YtdEnum.MONTH.getItem());
        }else if(ytd.equals(Constant.YtdEnum.DAY.getItem())){
            TimeUtils.digui(list,startTime,sdf.format(checkPlan.getEndTime()),cycle,Constant.YtdEnum.DAY.getItem());
        }else{
            TimeUtils.digui(list,startTime,sdf.format(checkPlan.getEndTime()),cycle,Constant.YtdEnum.HOUR.getItem());
        }*/


            for(String str: list){
                String corn  = "";
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
                    if(type.equals(Constant.ScheduleStatus.PAUSE.getItem()) && checkPlan.getState().equals(Constant.EnabledEnum.CLOSE.getItem())){
                        scheduleJobEntity.setState(Constant.ScheduleStatus.NORMAL.getItem());
                    }else{
                        scheduleJobEntity.setState(Constant.ScheduleStatus.PAUSE.getItem());
                    }
                    scheduleJobEntity.setBeanName("checkPlan");
                    scheduleJobEntity.setCreateTime(new Date());
                    Map<String, Object> params = new HashMap<>();
                    params.put("running",Constant.SYS_ADMIN);
                    params.put("checkPlan",planId);
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
            }
        }

        checkPlan.setJobIds(ids);

        checkPlanMapper.updateById(checkPlan);
    }


    public IPage<CheckPlan> findList(CheckPlan checkPlan, Integer pageNum, Integer pageSize) {
        checkPlan.setSiteId(UserUtils.getSite());
        Page page = new Page(pageNum,pageSize);
        return checkPlanMapper.findList(page,checkPlan);
    }
  public IPage<CheckPlan> findListByState(CheckPlan checkPlan, Integer pageNum, Integer pageSize) {
        checkPlan.setSiteId(UserUtils.getSite());
        Page page = new Page(pageNum,pageSize);
        return checkPlanMapper.findListByState(page,checkPlan);
    }

    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id->{
            CheckPlan checkPlan = checkPlanMapper.selectById(id);
            String jobIds = checkPlan.getJobIds();
            if(StringUtils.isNotBlank(jobIds)){
                List<String> stringList = Arrays.asList(jobIds.split(","));
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
            }

            checkPlanMapper.deleteById(id);
        });
    }


    public CheckPlan findById(String id) {
        CheckPlan checkPlan = checkPlanMapper.selectById(id);
        return checkPlan;
    }

    public CheckPlanVo getInfoById(){
        CheckPlanVo checkPlanVo=new CheckPlanVo();
        //点检单流水号
        String deviceCheckId =createCode();
        checkPlanVo.setDeviceCheckCode(deviceCheckId);
        //点检时间
        checkPlanVo.setCheckTime(new Date());
        checkPlanVo.setCheckPerson(userUtil.getUser().getRealName());
        return checkPlanVo;

    }

    public void saveCheckOrder(DeviceCheckDto deviceCheckDto){
        String id= UUID.randomUUID().toString();
       // DeviceCheck deviceCheck=deviceCheckDto.getDeviceCheck();
        CheckPlan checkPlan=deviceCheckDto.getCheckPlan();
        DeviceCheck deviceCheck=new DeviceCheck();
        deviceCheck.setDeviceCheckCode(deviceCheckDto.getDeviceCheckCode());
        deviceCheck.setCheckPlanId(deviceCheckDto.getCheckPlan().getId());
        deviceCheck.setCheckPlanName(deviceCheckDto.getCheckPlan().getCheckPlanName());
        deviceCheck.setDeviceName(deviceCheckDto.getCheckPlan().getName());
        deviceCheck.setDataCollectionName(deviceCheckDto.getCheckPlan().getDataCollectionName());
        deviceCheck.setDataCollectionId(deviceCheckDto.getCheckPlan().getDataCollectionId());
        deviceCheck.setDeviceType(deviceCheckDto.getCheckPlan().getType());
        deviceCheck.setCheckTime(new Date());
        deviceCheck.setCheckUserName(userUtil.getUser().getUserName());
        deviceCheck.setCheckRealName(userUtil.getUser().getRealName());
        deviceCheck.setId(id);
        deviceCheck.setState(checkPlan.getState());
        deviceCheck.setDeviceCode(checkPlan.getCode());
        if(checkPlan.getDevice() !=null && checkPlan.getDevice() !=""){
            deviceCheck.setDeviceCode(checkPlan.getDevice());
        }
        DeviceCheck deviceCheck1=deviceCheckMapper.selectOne(new QueryWrapper<DeviceCheck>().eq("device_check_code",deviceCheck.getDeviceCheckCode()));
        if(deviceCheck1 ==null){
            int row = 0;
            try {
                row = deviceCheckMapper.insert(deviceCheck);
            }catch (Exception e){
                e.getMessage();
            }finally {
                if (row != 1){//如果保存失败，说明在同一时间有两条数据插入，尝试重新插入
                    deviceCheck.setCheckTime(new Date());
                    deviceCheckMapper.insert(deviceCheck);
                }
            }
        }else{
            deviceCheckMapper.update(deviceCheck,new QueryWrapper<DeviceCheck>().eq("device_check_code",deviceCheck.getDeviceCheckCode()));
        }

        //保存点检单明细表信息
        //List<DeviceCheckItem> deviceCheckItems=deviceCheckDto.getDeviceCheckItems();
        List<DataCollectionItem> dataCollectionItems=deviceCheckDto.getDataCollectionItems();
        for(DataCollectionItem dataCollectionItem:dataCollectionItems){
            String checkItemId=UUID.randomUUID().toString();
            DeviceCheckItem deviceCheckItem=new DeviceCheckItem();
            deviceCheckItem.setId(checkItemId);
            deviceCheckItem.setItemName(dataCollectionItem.getItemName());
            deviceCheckItem.setRemark(dataCollectionItem.getRemark());
            deviceCheckItem.setOperation(dataCollectionItem.getOperation());
            deviceCheckItem.setComments(dataCollectionItem.getComments());
            deviceCheckItem.setDevicedCheckCode(deviceCheck.getDeviceCheckCode());
            deviceCheckItemMapper.insert(deviceCheckItem);
        }

    }
    public DeviceCheckVo selectById(String deviceCheckCode){
        //DeviceCheck deviceCheck=deviceCheckMapper.selectOne(new QueryWrapper<DeviceCheck>().eq("device_check_code",deviceCheckCode));
        DeviceCheck deviceCheck=deviceCheckMapper.selectByDeviceCheckCode(deviceCheckCode);
        List<DeviceCheckItem> deviceCheckItems=deviceCheckItemMapper.selectList(new QueryWrapper<DeviceCheckItem>().eq("device_check_code",deviceCheckCode));
        DeviceCheckVo deviceCheckVo=new DeviceCheckVo();
        deviceCheckVo.setDeviceCheck(deviceCheck);
        deviceCheckVo.setDeviceCheckItems(deviceCheckItems);
        return deviceCheckVo;
    }

    public IPage<DeviceCheck> selectDeviceCheck(Page page,DeviceCheckDto deviceCheckDto){

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ksTime=null;
        String jsTime=null;
        if(deviceCheckDto.getStartDate() !=null && deviceCheckDto.getEndDate()==null){
            ksTime=simpleDateFormat.format(deviceCheckDto.getStartDate());
        }else if(deviceCheckDto.getStartDate() !=null && deviceCheckDto.getEndDate() !=null){
            ksTime=sdf.format(deviceCheckDto.getStartDate());
            jsTime=sdf.format(deviceCheckDto.getEndDate());
        }
        String deviceName=deviceCheckDto.getDeviceName();
        String checkRealName=deviceCheckDto.getCheckRealName();
        String deviceCode=deviceCheckDto.getDeviceCode();
        return  deviceCheckMapper.selectDeviceCheckPage(page,deviceName,checkRealName,ksTime,jsTime,deviceCode);

    }
    @Transactional
    public void deletById(List<String> deviceCheckCodes) {
        deviceCheckCodes.forEach(deviceCheckCode ->{
            deviceCheckMapper.delete(new QueryWrapper<DeviceCheck>().eq("device_check_code",deviceCheckCode));
            deviceCheckItemMapper.delete(new QueryWrapper<DeviceCheckItem>().eq("device_check_code",deviceCheckCode));
        });
    }

    /**
     * 生成点检流水单编号
     * @return
     */
    public String createCode(){
        String code=deviceCheckMapper.selectMaxCode();
        String resultCode=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String pfix= sdf.format(new Date());
        if(code !=null && code.contains(pfix)){
            String end=code.substring(15,19);
            int endNum= Integer.parseInt(end);
            String format = String.format("%04d", endNum+1);
            resultCode= "DYMEDJ-"+pfix+format;
        }else{
            resultCode="DYMEDJ-"+pfix+"0001";
        }

        return  resultCode;
    }

    /**
     *
     * @param deviceType
     * @return
     */
    public CheckPlanVo queryByDeviceType(String deviceType) throws CommonException {
        CheckPlanVo checkPlanVo=new CheckPlanVo();
        CheckPlan checkPlan=new CheckPlan();
        if(deviceType.contains(",")){
            //根据逗号分割设备类型
            String[] deviceTypes=deviceType.split(",");
            //根据设备类型查询该类型最近创建的数据收集组
            for(int i=0;i<deviceTypes.length;i++){
                checkPlan=checkPlanMapper.selectByDeviceType(deviceTypes[i]);
                if(checkPlan !=null && StrUtil.isNotEmpty(checkPlan.getId())){
                    break;
                }
            }
        }else{
            checkPlan=checkPlanMapper.selectByDeviceType(deviceType);
        }
        if(checkPlan == null){
            throw new CustomException(CommonCode.CHECK_CREATE);
        }
        if(checkPlan !=null && checkPlan.getState() == 0){
            throw new CustomException(CommonCode.CHECK_STATE);
        }

        //根据设备收集组id查询对应的点检项
        List<DataCollectionItem>  dataCollectionItems=dataCollectionItemMapper.selectList(new QueryWrapper<DataCollectionItem>().eq("dataCollectionId",checkPlan.getDataCollectionId()));
        checkPlanVo.setCheckPlan(checkPlan);
        checkPlanVo.setDataCollectionItems(dataCollectionItems);
        return checkPlanVo;
    }

}
