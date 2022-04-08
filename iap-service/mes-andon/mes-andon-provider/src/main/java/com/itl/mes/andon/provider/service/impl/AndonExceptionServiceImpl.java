package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.PassWordUtil;
import com.itl.iap.common.util.UUID;
import com.itl.iap.notice.client.NoticeService;
import com.itl.iap.notice.client.SmsClientService;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.andon.api.dto.AndonExceptionDTO;
import com.itl.mes.andon.api.entity.*;
import com.itl.mes.andon.api.service.AndonExceptionService;
import com.itl.mes.andon.api.vo.AndonExceptionVo;
import com.itl.mes.andon.provider.common.CommonCode;
import com.itl.mes.andon.provider.config.ScheduleConstant;
import com.itl.mes.andon.provider.exception.CustomException;
import com.itl.mes.andon.provider.job.AndonSendMsgTask;
import com.itl.mes.andon.provider.mapper.*;
import com.itl.mes.andon.provider.utils.*;
import com.itl.mes.core.api.entity.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AndonExceptionServiceImpl extends ServiceImpl<ExceptionMapper,AndonException> implements AndonExceptionService {

    @Autowired
    private ExceptionMapper exceptionMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    NoticeService noticeService;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    SmsClientService smsClientService;

    @Autowired
    UserService userService;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private  HttpServletRequest request;

    @Autowired
    private  RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private GradePushMapper gradePushMapper;

//    @Autowired
//    public CronSchedulerJob scheduleJobs;


    @Value("${notice.andonCode}")
    private String andonCode;

    private SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /*根据id查询*/
    @Override
    public AndonException getAndonExceptionById(String id) {
        AndonException andonException = exceptionMapper.selectById(id);
        //根据触发用户账号获取触发用户姓名和电话号码
        if(StringUtils.isNotBlank(andonException.getTriggerUser())){
            String phone=userService.queryByName(andonException.getTriggerUser()).getData().getUserMobile();
            andonException.setTriggerUserPhone(phone);
        }
        /*if (andonException.getState().equals("0")){
            andonException.setCheckUserName(null);
        }*/
        return andonException;
    }

    public String getAndonCode(){
        String str = exceptionMapper.selectMaxCode();
        int qnum = 0;
        if (StrUtil.isNotBlank(str)){
            qnum = Integer.parseInt(str.substring(str.length()- 4,str.length()));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String format = String.format("%04d", qnum+1);
        String checkCode = "";
        checkCode = "Andon"+sdf.format(new Date())+format;
        return checkCode;
    }

    /*分页查询*/
    @Override
    public IPage<AndonException> getAndonException(AndonExceptionDTO andonExceptionDTO) {
        if(ObjectUtil.isEmpty(andonExceptionDTO.getPage())){
            andonExceptionDTO.setPage(new Page(0, 10));
        }
        QueryWrapper<AndonException> queryWrapper = new QueryWrapper<>();
        if (andonExceptionDTO.getStartTime() != null){
            queryWrapper.gt("trigger_time",andonExceptionDTO.getStartTime());
        }
        if (andonExceptionDTO.getEndTime() != null){
            queryWrapper.lt("trigger_time",andonExceptionDTO.getEndTime());
        }
        if (StrUtil.isNotEmpty(andonExceptionDTO.getWorkShopName())){
            queryWrapper.like("workshop_name",andonExceptionDTO.getWorkShopName());
        }
        if (StrUtil.isNotEmpty(andonExceptionDTO.getTriggerUser())){
            queryWrapper.like("trigger_user",andonExceptionDTO.getTriggerUser());
        }
        if (StrUtil.isNotEmpty(andonExceptionDTO.getTriggerUserName())){
            queryWrapper.like("trigger_user_name",andonExceptionDTO.getTriggerUserName());
        }
        if (StrUtil.isNotEmpty(andonExceptionDTO.getAndonTypeName())){
            queryWrapper.like("andon_type_name",andonExceptionDTO.getAndonTypeName());
        }
        if (StrUtil.isNotEmpty(andonExceptionDTO.getDeviceName())){
            queryWrapper.like("device_name",andonExceptionDTO.getDeviceName());
        }
        if (StrUtil.isNotEmpty(andonExceptionDTO.getDevice())){
            queryWrapper.eq("device",andonExceptionDTO.getDevice());
        }
        if (StrUtil.isNotEmpty(andonExceptionDTO.getState())){
            queryWrapper.eq("state",andonExceptionDTO.getState());
            andonExceptionDTO.setStates(null);
        }
        if (andonExceptionDTO.getStates() != null){
            queryWrapper.in("state",andonExceptionDTO.getStates());
        }
        if (StrUtil.isNotEmpty(andonExceptionDTO.getDeviceBo())){
            queryWrapper.eq("device_bo",andonExceptionDTO.getDeviceBo());
        }
        queryWrapper.orderByDesc("trigger_time");
        IPage<AndonException> page=exceptionMapper.selectPage(andonExceptionDTO.getPage(),queryWrapper);
        // 将持续时间转换为秒,计算方式：当前时间-触发时间
        if(page !=null && page.getRecords() !=null){
            Date date=new Date();
            for(AndonException andonException:page.getRecords()){
                if(andonException.getTriggerTime() !=null && !andonException.getState().equals("2")){
                    long diff;
                    //获得两个时间的毫秒时间差异
                    diff = date.getTime() - andonException.getTriggerTime().getTime();
                    // 毫秒转秒
                    andonException.setContinueTime(diff/1000);
                }
                // 根据andon类型BO查询安灯类型数据
                Type type=typeMapper.selectById(andonException.getAndonTypeBo());
                if(type !=null){
                    andonException.setIsCheck(type.getIsCheck());
                }
            }
        }
        return page;
    }

    /*新建或更新*/
    @Override
    public AndonException saveInUpdate(AndonExceptionVo andonExceptionVO) throws CommonException {
        // 检验当前触发设备是否存在未签到或未解除的异常数据
        List<AndonException> andonExceptionList=exceptionMapper.selectList(new QueryWrapper<AndonException>().eq("device_bo",andonExceptionVO.getDeviceBo()).ne("state","2"));
        if(andonExceptionList !=null && andonExceptionList.size()>0){
            // 该设备存在未签到或未解除的数据，不能触发
            throw new CommonException("该设备存在未签到或未解除的数据，请先解决后再触发",30002);
        }
        AndonException andonException = new AndonException();
        BeanUtils.copyProperties(andonExceptionVO,andonException);
        String checkCode = getAndonCode();
        String ids = "";
        String str="";
        if (StrUtil.isEmpty(andonExceptionVO.getExceptionCode())){
            andonException.setId(UUID.uuid32());
            andonException.setState("0");
            andonException.setExceptionCode(checkCode);
            andonException.setTriggerUser(userUtil.getUser().getUserName());
            andonException.setTriggerUserName(userUtil.getUser().getRealName());
            andonException.setTriggerTime(new Date());
            exceptionMapper.insert(andonException);

            // 触发人信息
            UserInfo userInfo=getUserInfo();
            //发送信息
            if (StrUtil.isNotEmpty(andonException.getCheckUser())){
                //JSONArray nodeList = JSONArray.parseArray(andonException.getCheckUser());
               String[] checkUserArray =andonException.getCheckUser().split(",");
                if (checkUserArray!=null && checkUserArray.length > 0){
                    Map<String,String> notice=new HashMap<>();
                    notice.put("templateCode","andonDeviceException");
                    notice.put("exception_code",andonException.getExceptionCode());
                    notice.put("workshop_name",andonException.getWorkshopName());
                    notice.put("device", andonExceptionVO.getDevice());
                    notice.put("device_name",andonException.getDeviceName());
                    notice.put("andon_type_name",andonException.getAndonTypeName());
                    notice.put("andon_detail",andonException.getAndonDetail());
                    notice.put("fault_code",andonException.getFaultCode());
                    notice.put("trigger_user_name",andonException.getTriggerUserName());
                    String triggerUserPhone=userService.queryByName(andonException.getTriggerUser()).getData().getUserMobile();
                    notice.put("trigger_user_phone",triggerUserPhone);
                    notice.put("station_name",userInfo.getStationName());
                    notice.put("productLine_name",userInfo.getProductLineName());
                    notice.put("device_ip",andonExceptionVO.getIp());
                    if(andonException.getTriggerTime() !=null){
                        notice.put("trigger_time", sdff.format(andonException.getTriggerTime()));
                    }
                    notice.put("check_user_name",andonException.getCheckUserName());
                    if(andonException.getCheckTime()  !=null){
                        notice.put("check_time", sdff.format(andonException.getCheckTime()));
                    }
                    if(andonException.getResponseTime() !=null){
                        notice.put("response_time", sdff.format(andonException.getResponseTime()));
                    }
                    notice.put("relieve_user_name",andonException.getRelieveUserName());
                    if(andonException.getRelieveTime() !=null){
                        notice.put("relieve_time", sdff.format(andonException.getRelieveTime()));
                    }
                    notice.put("exception_desc",andonException.getExceptionDesc());
                    notice.put("process_method",andonException.getProcessMethod());
                    if(andonException.getState().equals("0")){
                        notice.put("state","未签到");
                    }else if(andonException.getState().equals("1")){
                        notice.put("state","处理中");
                    }else{
                        notice.put("state","已解除");
                    }
                    for (int i=0;i<checkUserArray.length;i++){
                        String operationObj = checkUserArray[i].toString();
                        //获取签到人账号
                       /*String checkUser = operationObj.getString("checkUser");*/
                        String checkUser =operationObj;
                        String phone=userService.queryByName(checkUser).getData().getUserMobile();
                        notice.put("phone",phone);// 处理人电话
//                        notice.put("templateCode",andonCode);
                        smsClientService.sendMessage(notice);
                    }
//                    Type type=typeMapper.selectById(andonExceptionVO.getAndonTypeBo());
//                    if(type !=null && ("设备异常".equals(type.getAndonTypeName())||"设备电工类异常".equals(type.getAndonTypeName())||"设备气动类故障".equals(type.getAndonTypeName())||"设备液压/水液类故障".equals(type.getAndonTypeName())||"设备机械类故障".equals(type.getAndonTypeName()))){
//                        // 获取机修组的关联用户
//                        List<IapSysUserT> iapSysUserTList=exceptionMapper.selectRoleUserList("3cb977566efa49b197e0358eb399d42c");
//                        //根据用户账号发送数据
//                        for(IapSysUserT iapSysUserT:iapSysUserTList){
//                            String phone=userService.queryByName(iapSysUserT.getUserName()).getData().getUserMobile();
//                            notice.put("phone",phone);// 处理人电话
//                            smsClientService.sendMessage(notice);
//                        }
//
//                    }
                }
            }

            //签到定时任务
            Type type=typeMapper.selectById(andonExceptionVO.getAndonTypeBo());
            generateTask(andonExceptionVO.getWorkshopBo(),type.getAndonType(),1,1,andonException.getId());
//            try {
//                schedule(andonException.getExceptionCode());
//            } catch (SchedulerException e) {
//                e.printStackTrace();
//            }
        }else {
            exceptionMapper.update(andonException,new QueryWrapper<AndonException>().eq("exception_code",andonExceptionVO.getExceptionCode()));
        }
        exceptionMapper.update(andonException,new QueryWrapper<AndonException>().eq("exception_code",andonExceptionVO.getExceptionCode()));
        return exceptionMapper.selectOne(new QueryWrapper<AndonException>().eq("exception_code",checkCode));
    }

//    @Scheduled(cron="0/59 0/30 * * * ?")
//    public void schedule(String exceptionCode) throws SchedulerException {
//        scheduleJobs.scheduleJobs(exceptionCode);
//        System.out.println(">>>>>>>>>>>>>>>定时任务开始执行<<<<<<<<<<<<<");
//    }

    /*删除*/
    @Override
    public int delete(List<Integer> Ids) {
        return exceptionMapper.deleteBatchIds(Ids);
    }

    /*签到*/
    @Override
    public void signin(AndonExceptionVo exceptionVO) {
        QueryWrapper<AndonException> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exception_code", exceptionVO.getExceptionCode());
        AndonException andonException = new AndonException();
        BeanUtils.copyProperties(exceptionVO,andonException);
        //判断是否需要验证
        if(StringUtils.isNotBlank(exceptionVO.getIsCheck())  && "0".equals(exceptionVO.getIsCheck())){
            // 无需验证，直接拿当前登录用户作为签到人
            andonException.setCheckUser(userUtil.getUser().getUserName());
            andonException.setCheckUserName(userUtil.getUser().getRealName());
        }else{
            // 需要验证，前端传的用户账号和用户人名信息对应不一致，后端重新获取人名
            String realName=userService.queryByName(exceptionVO.getCheckUser()).getData().getRealName();
            andonException.setCheckUserName(realName);
        }
        Date date = new Date();
        long diff;
        //获得两个时间的毫秒时间差异
        diff = date.getTime() - andonException.getTriggerTime().getTime();
        // 毫秒转秒
        long responseTime = diff/1000;
        /*if (StrUtil.isEmpty(exceptionVO.getCheckUser())){
            andonException.setCheckUser(userUtil.getUser().getUserName());
            andonException.setCheckUserName(userUtil.getUser().getRealName());
        }*/
        andonException.setCheckTime(date);
        andonException.setResponseTime(responseTime);
        andonException.setState("1");
        exceptionMapper.update(andonException,queryWrapper);

        // 解除超时定时任务
        Type type=typeMapper.selectById(exceptionVO.getAndonTypeBo());
        generateTask(exceptionVO.getWorkshopBo(),type.getAndonType(),1,2,exceptionVO.getId());

    }

    /*解除*/
    @Override
    public void relieve(AndonExceptionVo exceptionVO) {
        QueryWrapper<AndonException> queryWrapper = new QueryWrapper<AndonException>();
        queryWrapper.eq("exception_code", exceptionVO.getExceptionCode());
        AndonException andonException = new AndonException();
        BeanUtils.copyProperties(exceptionVO,andonException);
        Date date = new Date();
        long diff;
        //获得两个时间的毫秒时间差异
        diff = date.getTime() - andonException.getTriggerTime().getTime();
        long continueTime = diff/1000;
        if (StrUtil.isEmpty(exceptionVO.getRelieveUser())){
            andonException.setRelieveUser(userUtil.getUser().getUserName());
            andonException.setRelieveUserName(userUtil.getUser().getRealName());
        }
        andonException.setRelieveTime(date);
        andonException.setContinueTime(continueTime);
        andonException.setState("2");
        exceptionMapper.update(andonException,queryWrapper);

        //查看是否存在签到超时的数据
        //Grade grade=AndonSendMsgTask.gradeInfo.get(andonException.getId()+"-"+1);
        Grade grade= (Grade) redisTemplate.opsForValue().get(andonException.getId()+"-"+2);
        if(grade !=null){
            sendMsg(grade,andonException);
        }
    }

    @Override
    public String userVerify(Map<String, Object> params) throws CommonException {
        String userName = params.getOrDefault("userName", "").toString();
        String pwd = params.getOrDefault("pwd", "").toString();
        String id = params.getOrDefault("id", "").toString();
        Integer type = Integer.valueOf(params.getOrDefault("type", "").toString());
        Map<String,String> param = new HashMap();
        param.put("userName", userName);
        param.put("pwd", PassWordUtil.encrypt(pwd,userName));
        String uName = exceptionMapper.userVerify(param);
        AndonException andonException = exceptionMapper.selectById(id);
        if (StrUtil.isEmpty(uName)){
            return uName;
        }
        if (type == 0){ //添加签到人
            if (!(andonException.getCheckUser().indexOf(userName)!=-1)){
                // 当前账号不存在于指定处理人中,判断是否设备故障
                Type typeEntity=typeMapper.selectById(andonException.getAndonTypeBo());
                boolean isExist=true;//不存在为true
                if(typeEntity !=null && ("设备异常".equals(typeEntity.getAndonTypeName())||"设备电工类异常".equals(typeEntity.getAndonTypeName())||"设备气动类故障".equals(typeEntity.getAndonTypeName())||"设备液压/水液类故障".equals(typeEntity.getAndonTypeName())||"设备机械类故障".equals(typeEntity.getAndonTypeName()))) {
                    // 获取机修组的关联用户
                    //List<IapSysUserT> iapSysUserTList = exceptionMapper.selectRoleUserList("261225991ee54165a72c3718b4ea3279"); //测试
                    List<IapSysUserT> iapSysUserTList = exceptionMapper.selectRoleUserList("3cb977566efa49b197e0358eb399d42c");  //正式
                    for(IapSysUserT iapSysUserT:iapSysUserTList) {
                        if (userName.equals(iapSysUserT.getUserName())) {
                            isExist = false;
                        }
                    }
                }
                if(isExist){
                    throw new CommonException("请登录通知人账号或机修组账号", 25000);
                }
            }
        }
        if (type == 1){ //添加解除人
            if (!(andonException.getCheckUser().indexOf(userName)!=-1)){
                throw new CommonException("请登录签到人账号", 25000);
            }
        }
        return uName;
    }

    @Override
    public AndonException saveOrUpdateRepair(AndonExceptionVo andonExceptionVo) {
        AndonException andonException = new AndonException();
        BeanUtils.copyProperties(andonExceptionVo,andonException);
        String checkCode = getAndonCode();
        String ids = "";
        String str="";
        if (StrUtil.isEmpty(andonExceptionVo.getExceptionCode())){
            andonException.setId(UUID.uuid32());
            andonException.setState("0");
            andonException.setExceptionCode(checkCode);
            andonException.setTriggerUser(userUtil.getUser().getUserName());
            andonException.setTriggerUserName(userUtil.getUser().getRealName());
            andonException.setTriggerTime(new Date());
            exceptionMapper.insert(andonException);
        }else {
            exceptionMapper.update(andonException,new QueryWrapper<AndonException>().eq("exception_code",andonExceptionVo.getExceptionCode()));
        }
        return exceptionMapper.selectOne(new QueryWrapper<AndonException>().eq("exception_code",checkCode));
    }

    /*修理任务列表*/
    @Override
    public IPage<AndonException> repairList(AndonExceptionDTO andonExceptionDTO) {
        IPage<AndonException> page = exceptionMapper.repairList(andonExceptionDTO.getPage(),andonExceptionDTO.getDevice(),null);
        return page;
    }

    /*获取我的修理任务*/
    @Override
    public IPage<AndonException> myRepairTask(AndonExceptionDTO andonExceptionDTO) {
        String userName = userUtil.getUser().getUserName();
        IPage<AndonException> page = exceptionMapper.repairList(andonExceptionDTO.getPage(),andonExceptionDTO.getDevice(),userName);
        return page;
    }

    /*接单设备修理任务*/
    @Override
    public void getRepairTask(AndonExceptionVo exceptionVO) {
        String userName = userUtil.getUser().getUserName();
        String realName = userUtil.getUser().getRealName();
        String exceptionCode = exceptionVO.getExceptionCode();
        AndonException andonException = new AndonException();
        andonException.setCheckUser(userName);
        andonException.setCheckUserName(realName);
        andonException.setState("1");
        QueryWrapper<AndonException> queryWrapper = new QueryWrapper<AndonException>();
        queryWrapper.eq("exception_code",exceptionCode);
        exceptionMapper.update(andonException,queryWrapper);
    }
    /**
     * 生成定时任务
     */
    public  void generateTask(String workShopBo,String andonType,int andonGrade,int timeOutType,String exceptionId){
        if(gradeMapper == null){
            gradeMapper= (GradeMapper) SpringContextUtils.getBean("gradeMapper");
        }
        if(jobMapper == null){
            jobMapper= (JobMapper) SpringContextUtils.getBean("jobMapper");
        }
        // 根据参数信息查询是否存在该等级的报警设置
        Grade grade=gradeMapper.selectOne(new QueryWrapper<Grade>()
                .eq("WORKSHOP_BO",workShopBo).eq("ANDON_TYPE",andonType).
                        eq("ANDON_GRADE",andonGrade).eq("TIMEOUT_TYPE",timeOutType));
        // 如果存在该级别的报警信息，创建新的定时任务
        if(grade !=null){
            Calendar nowTime = Calendar.getInstance();
            int overTime=grade.getOverTime();
            nowTime.add(Calendar.MINUTE,overTime);
            String corn= CornUtils.getCron(nowTime.getTime());

            ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
            Long jobId = SnowFlake.getId();
            scheduleJobEntity.setJobId(jobId);
            scheduleJobEntity.setCronExpression(corn);
            scheduleJobEntity.setState(ScheduleConstant.ScheduleStatus.NORMAL.getItem());
            scheduleJobEntity.setBeanName("andonSendMsg");
            scheduleJobEntity.setCreateTime(new Date());
            Map<String, Object> params = new HashMap<>();
            params.put("running",ScheduleConstant.SYS_ADMIN);
            params.put("gradeId",grade.getId());
            params.put("exceptionId",exceptionId);
            scheduleJobEntity.setParams(MapUtils.getMapToString(params));
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
            jobMapper.insert(scheduleJobEntity);
        }
    }
    /**
     * 获取用户信息（工号、姓名、工位）
     * @return UserInfo
     */
    public  UserInfo getUserInfo() throws CommonException {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userUtil.getUser().getRealName());
        userInfo.setUserBo(userUtil.getUser().getUserName());
        String onlyStation = "";
        Cookie[] cookies = request.getCookies();//从cookie中获取工位
        if (null != cookies){
            for (Cookie cookie : cookies) {
                if ("onlyStation".equals(cookie.getName())){
                    onlyStation = cookie.getValue();
                }
            }
        }
        //redis中工位的值
        String station = "";
        try {
            station = redisTemplate.opsForValue().get("station-" + userUtil.getUser().getUserName() + ":" + onlyStation).toString();
        }catch (NullPointerException e){
            e.getMessage();
        }
        if(StringUtils.isNotBlank(station)){
            userInfo.setStation(station);
            Station stationEntity=exceptionMapper.selectStationDescByStation(station);
            userInfo.setStationName(stationEntity.getStationDesc());
            userInfo.setProductLineName(stationEntity.getProductLineName());
        }
        return userInfo;
    }
    public void sendMsg(Grade grade,AndonException andonException){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String triggerTime=null;
        String relieveTime=null;
        if(andonException.getTriggerTime() !=null){
            triggerTime=simpleDateFormat.format(andonException.getTriggerTime());
        }else{
            triggerTime="";
        }
        if(andonException.getRelieveTime() !=null){
            relieveTime=simpleDateFormat.format(andonException.getRelieveTime());
        }else {
            relieveTime="";
        }
        // 获取所有的推送人员
        List<GradePush> gradePushes = gradePushMapper.selectList(new QueryWrapper<GradePush>().eq("ANDON_GRADE_ID", grade.getId()));
        if(gradePushes !=null && gradePushes.size()>0){
            for(GradePush gradePush:gradePushes){
                Map<String,String> notice=new HashMap<>();
                notice.put("templateCode","andonRelieve");
                notice.put("phone",gradePush.getUserMobile());
                notice.put("exception_code",andonException.getExceptionCode());
                notice.put("device",andonException.getDevice());
                notice.put("device_name",andonException.getDeviceName());
                notice.put("trigger_time",triggerTime);
                notice.put("relieve_time",relieveTime);
                notice.put("relieve_user",andonException.getCheckUserName());
                smsClientService.sendMessage(notice);
            }

        }
    }
}
