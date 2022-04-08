package com.itl.mes.andon.provider.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.notice.client.SmsClientService;
import com.itl.mes.andon.api.entity.AndonException;
import com.itl.mes.andon.api.entity.Grade;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.provider.mapper.ExceptionMapper;
import com.itl.mes.andon.provider.mapper.GradeMapper;
import com.itl.mes.andon.provider.mapper.GradePushMapper;
import com.itl.mes.andon.provider.service.impl.AndonExceptionServiceImpl;
import com.itl.mes.andon.provider.utils.MapUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("andonSendMsg")
@EnableScheduling
public class AndonSendMsgTask implements ITask{

    @Autowired
    private ExceptionMapper andonExceptionMapper;

    @Autowired
    private GradePushMapper gradePushMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    SmsClientService smsClientService;

    @Autowired
    UserService userService;


   @Autowired
   private RedisTemplate<String, Object>  redisTemplate;


    @Override
    public void run(String params) {
        AndonExceptionServiceImpl andonExceptionServiceImpl=new AndonExceptionServiceImpl();
        if(StringUtils.isNotBlank(params)){
            Map<String, Object> map = MapUtils.getStringToMap(params);
            Grade grade= gradeMapper.selectById(map.get("gradeId").toString());
            Object exceptionId=map.get("exceptionId");
            AndonException andonException=andonExceptionMapper.selectById(exceptionId.toString());
            String value=null;
            if(grade.getTimeoutType()==1){
                // 签到超时
                value="0";

            }else{
                // 处理超时
                value="1";
                //gradeInfo.put(andonException.getId()+"-"+grade.getTimeoutType(),grade);
                redisTemplate.opsForValue().set(andonException.getId()+"-"+grade.getTimeoutType(),grade);
            }
            // 如果当前andonException state 依然与value值相同，则进行报警升级
            if(value.equals(andonException.getState())){
                // 发送短信
                sendMsg(grade,grade.getAndonGrade(),andonException);
                // 创建新的定时任务
               andonExceptionServiceImpl.generateTask(grade.getWorkshopBo(),grade.getAndonType(),grade.getAndonGrade()+1,grade.getTimeoutType(),andonException.getId());


                //andonExceptionServiceImpl.generateTask(andonException.getWorkshopBo(),);
            }
        }
    }

    /**
     *
     * @param grade
     * @param andonGrade 安灯报警等级
     * @param andonException 安灯异常对象
     */
    public void sendMsg(Grade grade,int andonGrade,AndonException andonException){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String workShopName=andonException.getWorkshopName();
        String triggerUserName=andonException.getTriggerUserName();
        String device=andonException.getDevice();
        String deviceName=andonException.getDeviceName();
        String triggerTime=null;
        long diff=0;
        Date date=new Date();
        String minute=null;
        if(andonException.getTriggerTime() !=null){
            triggerTime=simpleDateFormat.format(andonException.getTriggerTime());
            //获得两个时间的毫秒时间差异
            diff = date.getTime() - andonException.getTriggerTime().getTime();
            minute=(diff/(60*1000))+"";
        }


        simpleDateFormat.format(andonException.getTriggerTime());
        String phone=null;
        if(StringUtils.isNotBlank(andonException.getTriggerUser())){
            phone=userService.queryByName(andonException.getTriggerUser()).getData().getUserMobile();
        }
        // 获取所有的推送人员
        List<GradePush> gradePushes = gradePushMapper.selectList(new QueryWrapper<GradePush>().eq("ANDON_GRADE_ID", grade.getId()));
        if(gradePushes !=null && gradePushes.size()>0){
            for(GradePush gradePush:gradePushes){
                Map<String,String> notice=new HashMap<>();
                notice.put("templateCode","andonWarning");
                notice.put("phone",gradePush.getUserMobile());
                notice.put("userName",gradePush.getRealName());
                notice.put("workshop_name",workShopName);
                notice.put("trigger_user_name",triggerUserName);
                notice.put("trigger_user_phone",phone);
                notice.put("device",device);
                notice.put("device_name",deviceName);
                notice.put("trigger_time",triggerTime);
                notice.put("minute",minute);
                notice.put("grade",andonGrade+"");
                notice.put("andon_detail",andonException.getAndonDetail());
                notice.put("andon_type_name",andonException.getAndonTypeName());
                notice.put("exception_code",andonException.getExceptionCode());
                smsClientService.sendMessage(notice);
            }

        }
    }
}
