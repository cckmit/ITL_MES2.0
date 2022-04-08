package com.itl.iap.mes.provider.job;


/*import com.itl.iap.attachment.client.service.UserService;*/
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.mes.api.entity.UpkeepExecute;
import com.itl.iap.mes.api.entity.UpkeepPlan;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.CorrectiveMaintenanceMapper;
import com.itl.iap.mes.provider.mapper.UpkeepPlanMapper;
import com.itl.iap.mes.provider.service.impl.UpkeepExecuteServiceImpl;
import com.itl.iap.mes.provider.service.impl.UpkeepPlanServiceImpl;
import com.itl.iap.mes.provider.utils.MapUtils;
import com.itl.iap.mes.provider.utils.TimeUtils;
import com.itl.iap.notice.client.SmsClientService;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("upkeepPlan")
@EnableScheduling
public class UpkeepPlanTask implements ITask {

	@Autowired
	private UpkeepExecuteServiceImpl upkeepExecuteService;

	@Autowired
	private UpkeepPlanMapper upkeepPlanMapper;

	@Autowired
	CorrectiveMaintenanceMapper correctiveMaintenanceMapper;

	@Autowired
	private UpkeepPlanServiceImpl upkeepPlanService;

	@Autowired
	private UserUtil userUtil;

	@Autowired
	SmsClientService smsClientService;


	@Autowired
	UserService userService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Value("${notice.upkeepCode}")
	private String upkeepCode;
	@Override
	public void run(String params)  {
		Date date=new Date();
		if(StringUtils.isNotBlank(params)){
			Map<String, Object> map = MapUtils.getStringToMap(params);
			Object upkeepPlan = map.get("upkeepPlan");
			if(upkeepPlan != null){
				UpkeepPlan plan = upkeepPlanMapper.selectById(upkeepPlan.toString());

				List<Map<String, Object>> device = correctiveMaintenanceMapper.getDevice(plan.getCode());

				UpkeepExecute upkeepExecute = new UpkeepExecute();
				if(device != null && !device.isEmpty()){
					upkeepExecute.setProductionLine(device.get(0).get("productLineBo")==null?"":device.get(0).get("productLineBo").toString());
				}
				upkeepExecute.setUpkeepPlanName(plan.getUpkeepPlanName());
				upkeepExecute.setCode(plan.getCode());
				upkeepExecute.setName(plan.getName());
				upkeepExecute.setType(plan.getType());
				upkeepExecute.setDeviceTypeName(plan.getDeviceTypeName());
				upkeepExecute.setDataCollectionId(plan.getDataCollectionId());
				upkeepExecute.setUpkeepUserId(plan.getUpkeepUserId());
				upkeepExecute.setUpkeepUserName(plan.getUpkeepUserName());
				upkeepExecute.setDataCollectionName(plan.getDataCollectionName());
				upkeepExecute.setPlanExecuteTime(new Date());
				upkeepExecute.setState(0);
				upkeepExecute.setUpkeepPlanId(plan.getId());
				upkeepExecuteService.save(upkeepExecute);
				String phone=userService.queryByName(plan.getUpkeepUserId()).getData().getUserMobile();
				//保养提醒  设备编码，
				Map<String,String> notice=new HashMap<>();
				notice.put("phone",phone);
				notice.put("templateCode",upkeepCode);
				setNotice(notice,plan);
				smsClientService.sendMessage(notice);

				//发完信息后，生成下次执行时间
				String startTime=sdf.format(date);//本次执行的时间
				Integer cycle = plan.getCycle();//间隔
				Integer ytd = plan.getYtd();//("0 季度 1 周 2 日")
				String nexTime=nextTime(startTime,cycle,ytd);
				if(plan.getEndTime() !=null){
					Date nextDate= null;
					try {
						nextDate = sdf.parse(nexTime);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					//下一次执行时间，小于等于结束时间
					if(nextDate.getTime()<=plan.getEndTime().getTime()){
						if(nexTime !=null && nexTime !=""){
							//创建定时任务，并返回ids
							String ids=upkeepPlanService.ScheduleJob(plan.getJobIds(),nexTime,plan,1);
							//更新定时任务id
							plan.setJobIds(ids);
							upkeepPlanMapper.updateById(plan);
						}
					}else{
						throw new CustomException(CommonCode.NEXTTIME_ERROR);
					}
				}else{
					throw new CustomException(CommonCode.TIME_FAILE);
				}
			}
		}
	}


	//生成定时任务下一次执行时间
	public String nextTime(String startTime, Integer cycle, Integer ytd){
		String nextTime="";
		if(ytd.equals(Constant.YtdEnum.MONTH.getItem())){
			//季度
			nextTime = TimeUtils.nextMonth(startTime,cycle,Constant.YtdEnum.MONTH.getItem());
		}else if(ytd.equals(Constant.YtdEnum.DAY.getItem())){
			//周
			nextTime = TimeUtils.nextMonth(startTime,cycle,Constant.YtdEnum.DAY.getItem());
		}
		return nextTime;
	}

	public void setNotice(Map<String,String> notice,UpkeepPlan plan){
		notice.put("upkeepPlanName",plan.getUpkeepPlanName());
		if(plan.getUpkeepPlanName().equals("0")){
			notice.put("upkeepPlanName","季度保养");
		}else if(plan.getUpkeepPlanName().equals("1")){
			notice.put("upkeepPlanName","周保养");
		}
		notice.put("code",plan.getCode());
		notice.put("name",plan.getName());
		notice.put("type",plan.getType());
		notice.put("dataCollectionId",plan.getDataCollectionId());
		notice.put("upkeepUserId",plan.getUpkeepUserId());
		notice.put("upkeepUserName",plan.getUpkeepUserName());
		notice.put("startTime", sdf.format(plan.getStartTime()));
		notice.put("endTime",sdf.format(plan.getEndTime()));
		notice.put("cycle",plan.getCycle()+"");
		if(plan.getYtd().intValue()==0){
			notice.put("ytd","季度保养");
		}else if(plan.getYtd().intValue()==1){
			notice.put("ytd","周保养");
		}
		notice.put("remark",plan.getRemark());
		notice.put("dataCollectionName",plan.getDataCollectionName());
		notice.put("createTime",plan.getCreateName());
		notice.put("deviceTypeName",plan.getDeviceTypeName());
		notice.put("createName",plan.getCreateName());
		notice.put("createAccount",plan.getCreateAccount());
		notice.put("repairId",plan.getRepairId());
	}


}
