package com.itl.iap.mes.provider.job;

import com.itl.iap.mes.api.entity.CheckExecute;
import com.itl.iap.mes.api.entity.CheckPlan;
import com.itl.iap.mes.provider.mapper.CheckExecuteMapper;
import com.itl.iap.mes.provider.mapper.CheckPlanMapper;
import com.itl.iap.mes.provider.mapper.CorrectiveMaintenanceMapper;
import com.itl.iap.mes.provider.service.impl.CheckExecuteServiceImpl;
import com.itl.iap.mes.provider.utils.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("checkPlan")
@EnableScheduling
public class CheckPlanTask implements ITask {

	@Autowired
	private CheckExecuteMapper checkExecuteMapper;

	@Autowired
	private CheckPlanMapper checkPlanMapper;

	@Autowired
	CorrectiveMaintenanceMapper correctiveMaintenanceMapper;

	@Override
	public void run(String params){
		if(StringUtils.isNotBlank(params)){
			Map<String, Object> map = MapUtils.getStringToMap(params);
			Object checkPlan = map.get("checkPlan");
			if(checkPlan != null){
				CheckPlan plan = checkPlanMapper.selectById(checkPlan.toString());
				List<Map<String, Object>> device = correctiveMaintenanceMapper.getDevice(plan.getCode());

				CheckExecute checkExecute = new CheckExecute();
				if(device != null && !device.isEmpty()){
					checkExecute.setProductionLine(device.get(0).get("productLineBo")==null?"":device.get(0).get("productLineBo").toString());
				}
				checkExecute.setCheckPlanName(plan.getCheckPlanName());
				checkExecute.setCode(plan.getCode());
				checkExecute.setName(plan.getName());
				checkExecute.setType(plan.getType());
				checkExecute.setDataCollectionId(plan.getDataCollectionId());
				//checkExecute.setCheckUserId(plan.getCheckUserId());
				//checkExecute.setCheckUserName(plan.getCheckUserName());
				checkExecute.setSiteId(plan.getSiteId());
				checkExecute.setDataCollectionName(plan.getDataCollectionName());
				checkExecute.setState(0);
				checkExecute.setPlanExecuteTime(new Date());
				checkExecuteMapper.insert(checkExecute);
			}
		}
	}
}
