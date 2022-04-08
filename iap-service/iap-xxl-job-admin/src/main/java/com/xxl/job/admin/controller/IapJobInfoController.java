package com.xxl.job.admin.controller;

import com.xxl.job.admin.core.cron.CronExpression;
import com.xxl.job.admin.core.exception.XxlJobException;
import com.xxl.job.admin.core.model.IapXxlJobGroup;
import com.xxl.job.admin.core.model.IapXxlJobInfo;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.core.thread.JobTriggerPoolHelper;
import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.mapper.IapXxlJobGroupDao;
import com.xxl.job.admin.mapper.IapXxlJobInfoDao;
import com.xxl.job.admin.service.IapXxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * 该类主要负责任务的新增、修改、删除、查询下次调度时间、启动、停止、调度一次。涉及xxl_job_info表。
 * 注意：
 * xxl_job_info表trigger_status字段0-停止，1-运行；
 * 计算下次调度时间需要加5s，避开预读周期；
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Controller
@RequestMapping("/jobinfo")
public class IapJobInfoController {

    @Resource
    private IapXxlJobGroupDao iapXxlJobGroupDao;
    @Resource
    private IapXxlJobService iapXxlJobService;
    @Resource
    public IapXxlJobInfoDao iapXxlJobInfoDao;

    @RequestMapping
    public String index(HttpServletRequest request, Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

        // 枚举-字典
        // 路由策略-列表
        model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());
        // Glue类型-字典
        model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());
        // 阻塞处理策略-字典
        model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());

        // 执行器列表
        List<IapXxlJobGroup> jobGroupListAll = iapXxlJobGroupDao.findAll();

        // filter group
        List<IapXxlJobGroup> jobGroupList = filterJobGroupByRole(request, jobGroupListAll);
        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new XxlJobException(I18nUtil.getString("jobgroup_empty"));
        }

        model.addAttribute("JobGroupList", jobGroupList);
        model.addAttribute("jobGroup", jobGroup);

        return "jobinfo/jobinfo.index";
    }

    public static List<IapXxlJobGroup> filterJobGroupByRole(HttpServletRequest request, List<IapXxlJobGroup> jobGroupListAll) {
        return jobGroupListAll;
		/*List<XxlJobGroup> jobGroupList = new ArrayList<>();
		if (jobGroupList_all!=null && jobGroupList_all.size()>0) {
			XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
			if (loginUser.getRole() == 1) {
				jobGroupList = jobGroupList_all;
			} else {
				List<String> groupIdStrs = new ArrayList<>();
				if (loginUser.getPermission()!=null && loginUser.getPermission().trim().length()>0) {
					groupIdStrs = Arrays.asList(loginUser.getPermission().trim().split(","));
				}
				for (XxlJobGroup groupItem:jobGroupList_all) {
					if (groupIdStrs.contains(String.valueOf(groupItem.getId()))) {
						jobGroupList.add(groupItem);
					}
				}
			}
		}
		return jobGroupList;*/
    }

    public static void validPermission(HttpServletRequest request, int jobGroup) {
        /*XxlJobUser loginUser = (XxlJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
        if (!loginUser.validPermission(jobGroup)) {
            throw new RuntimeException(I18nUtil.getString("system_permission_limit") + "[username=" + loginUser.getUsername() + "]");
        }*/
    }

    /**
     * 任务管理页面查询接口
     *
     * @param start
     * @param length
     * @param jobGroup
     * @param triggerStatus
     * @param jobDesc
     * @param executorHandler
     * @param author
     * @return
     */
    @RequestMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {

        return iapXxlJobService.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
    }

    @RequestMapping("queryDict")
    @ResponseBody
    public Map<String, Object> getDictionary(@RequestParam(required = false, defaultValue = "-1") int jobGroup) {
        Map<String, Object> map = new HashMap();
        // 枚举-字典
        ExecutorRouteStrategyEnum[] values = ExecutorRouteStrategyEnum.values();
        List<Map<String, String>> strategyList = new ArrayList();
        for (ExecutorRouteStrategyEnum strategyEnum : values) {
            Map<String, String> routeMap = new HashMap();
            routeMap.put("title", strategyEnum.getTitle());
            routeMap.put("name", strategyEnum.name());
            strategyList.add(routeMap);
        }

        List<Map<String, Object>> glueList = new ArrayList();
        GlueTypeEnum[] glueTypes = GlueTypeEnum.values();
        for (GlueTypeEnum glueTypeEnum : glueTypes) {
            Map<String, Object> glueTypeMap = new HashMap();
            glueTypeMap.put("title", glueTypeEnum.getDesc());
            glueTypeMap.put("name", glueTypeEnum.name());
            glueTypeMap.put("bool", glueTypeEnum.isScript());
            glueList.add(glueTypeMap);
        }
        List<Map<String, Object>> blockList = new ArrayList();
        ExecutorBlockStrategyEnum[] blocks = ExecutorBlockStrategyEnum.values();
        for (ExecutorBlockStrategyEnum blockStrategy : blocks) {
            Map<String, Object> blockMap = new HashMap();
            blockMap.put("title", blockStrategy.getTitle());
            blockMap.put("name", blockStrategy.name());
            blockList.add(blockMap);
        }
        // 路由策略-列表
        map.put("ExecutorRouteStrategyEnum", strategyList);
        // Glue类型-字典
        map.put("GlueTypeEnum", glueList);
        // 阻塞处理策略-字典
        map.put("ExecutorBlockStrategyEnum", blockList);
        // 执行器列表
        map.put("JobGroupList", iapXxlJobGroupDao.findAll());
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (IapXxlJobInfo iapXxlJobInfo : iapXxlJobInfoDao.loadByAll()) {
            Map<String, Object> maps = new HashMap();
            maps.put("id", iapXxlJobInfo.getId());
            maps.put("name", iapXxlJobInfo.getJobDesc());
            mapList.add(maps);
        }
        // 所有执行器列表
        map.put("JobInfoList", mapList);
        map.put("jobGroup", jobGroup);
        return map;
    }

    /**
     * 添加XxlJobInfo
     *
     * @param jobInfo
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ReturnT<String> add(@RequestBody IapXxlJobInfo jobInfo) {
        return iapXxlJobService.add(jobInfo);
    }

    /**
     * 更新任务（XxlJobInfo）
     *
     * @param jobInfo
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ReturnT<String> update(IapXxlJobInfo jobInfo) {
        return iapXxlJobService.update(jobInfo);
    }

    /**
     * 删除任务（XxlJobInfo）
     *
     * @param id
     * @return
     */
    @RequestMapping("/remove")
    @ResponseBody
    public ReturnT<String> remove(int id) {
        return iapXxlJobService.remove(id);
    }

    /**
     * 停止任务（XxlJobInfo）
     *
     * @param id
     * @return
     */
    @RequestMapping("/stop")
    @ResponseBody
    public ReturnT<String> pause(int id) {
        return iapXxlJobService.stop(id);
    }

    /**
     * 启动任务（XxlJobInfo）
     *
     * @param id
     * @return
     */
    @RequestMapping("/start")
    @ResponseBody
    public ReturnT<String> start(int id) {
        return iapXxlJobService.start(id);
    }

    /**
     * 执行任务
     *
     * @param id
     * @param executorParam
     * @param addressList
     * @return
     */
    @RequestMapping("/trigger")
    @ResponseBody
    //@PermissionLimit(limit = false)
    public ReturnT<String> triggerJob(int id, String executorParam, String addressList) {
        // force cover job param
        if (executorParam == null) {
            executorParam = "";
        }

        JobTriggerPoolHelper.trigger(id, TriggerTypeEnum.MANUAL, -1, null, executorParam, addressList);
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/nextTriggerTime")
    @ResponseBody
    public ReturnT<List<String>> nextTriggerTime(String cron) {
        List<String> result = new ArrayList<>();
        try {
            CronExpression cronExpression = new CronExpression(cron);
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = cronExpression.getNextValidTimeAfter(lastTime);
                if (lastTime != null) {
                    result.add(DateUtil.formatDateTime(lastTime));
                } else {
                    break;
                }
            }
        } catch (ParseException e) {
            return new ReturnT<List<String>>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid"));
        }
        return new ReturnT<List<String>>(result);
    }

}
