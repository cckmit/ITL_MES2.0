package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.core.model.IapXxlJobGroup;
import com.xxl.job.admin.core.model.IapXxlJobInfo;
import com.xxl.job.admin.core.cron.CronExpression;
import com.xxl.job.admin.core.model.IapXxlJobLogReport;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.core.thread.JobScheduleHelper;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.core.util.SnowFlake;
import com.xxl.job.admin.mapper.*;
import com.xxl.job.admin.service.IapXxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * xxl-job的Serivce层实现类
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Service
public class IapIapXxlJobServiceImpl implements IapXxlJobService {
    private static Logger logger = LoggerFactory.getLogger(IapIapXxlJobServiceImpl.class);

    @Resource
    private IapXxlJobGroupDao iapXxlJobGroupDao;
    @Resource
    private IapXxlJobInfoDao iapXxlJobInfoDao;
    @Resource
    public IapXxlJobLogDao iapXxlJobLogDao;
    @Resource
    private IapXxlJobLogGlueDao iapXxlJobLogGlueDao;
    @Resource
    private IapXxlJobLogReportDao iapXxlJobLogReportDao;

    /**
     * 分页查询调度扩展信息表
     *
     * @param start           偏移量
     * @param length          每页大小
     * @param jobGroup        执行器主键ID
     * @param jobDesc         任务描述
     * @param executorHandler 执行器任务handler
     * @param author          作者
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {

        // page list
        List<IapXxlJobInfo> list = iapXxlJobInfoDao.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
        int listCount = iapXxlJobInfoDao.pageListCount(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        // 总记录数
        maps.put("recordsTotal", listCount);
        // 过滤后的总记录数
        maps.put("recordsFiltered", listCount);
        // 分页列表
        maps.put("data", list);
        return maps;
    }

    /**
     * 添加任务（XxlJobInfo）
     *
     * @param jobInfo
     * @return
     */
    @Override
    public ReturnT<String> add(IapXxlJobInfo jobInfo) {
        // valid
        IapXxlJobGroup group = iapXxlJobGroupDao.load(jobInfo.getJobGroup());
        if (group == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_choose") + I18nUtil.getString("jobinfo_field_jobgroup")));
        }
        if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid"));
        }
        if (jobInfo.getJobDesc() == null || jobInfo.getJobDesc().trim().length() == 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_jobdesc")));
        }
        if (jobInfo.getAuthor() == null || jobInfo.getAuthor().trim().length() == 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_author")));
        }
        if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_gluetype") + I18nUtil.getString("system_unvalid")));
        }
        if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType()) && (jobInfo.getExecutorHandler() == null || jobInfo.getExecutorHandler().trim().length() == 0)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input") + "JobHandler"));
        }

        // fix "\r" in shell
        if (GlueTypeEnum.GLUE_SHELL == GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource() != null) {
            jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
        }

        // ChildJobId valid
        if (jobInfo.getChildJobId() != null && jobInfo.getChildJobId().trim().length() > 0) {
            String[] childJobIds = jobInfo.getChildJobId().split(",");
            for (String childJobIdItem : childJobIds) {
                if (childJobIdItem != null && childJobIdItem.trim().length() > 0 && isNumeric(childJobIdItem)) {
                    IapXxlJobInfo childJobInfo = iapXxlJobInfoDao.loadById(Integer.parseInt(childJobIdItem));
                    if (childJobInfo == null) {
                        return new ReturnT<String>(ReturnT.FAIL_CODE,
                                MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_not_found")), childJobIdItem));
                    }
                } else {
                    return new ReturnT<String>(ReturnT.FAIL_CODE,
                            MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_unvalid")), childJobIdItem));
                }
            }

            // join , avoid "xxx,,"
            String temp = "";
            for (String item : childJobIds) {
                temp += item + ",";
            }
            temp = temp.substring(0, temp.length() - 1);

            jobInfo.setChildJobId(temp);
        }

        // add in db
        jobInfo.setAddTime(new Date());
        jobInfo.setUpdateTime(new Date());
        jobInfo.setGlueUpdatetime(new Date());
//        Long id = SnowFlake.getId();
//        System.out.print(id);
//        jobInfo.setId(id.intValue());
        iapXxlJobInfoDao.save(jobInfo);
        if (jobInfo.getId() < 1) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_add") + I18nUtil.getString("system_fail")));
        }

        return new ReturnT<String>(String.valueOf(jobInfo.getId()));
    }

    private boolean isNumeric(String str) {
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 更新任务（XxlJobInfo）
     *
     * @param jobInfo
     * @return
     */
    @Override
    public ReturnT<String> update(IapXxlJobInfo jobInfo) {

        // valid
        if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid"));
        }
        if (jobInfo.getJobDesc() == null || jobInfo.getJobDesc().trim().length() == 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_jobdesc")));
        }
        if (jobInfo.getAuthor() == null || jobInfo.getAuthor().trim().length() == 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_field_author")));
        }
        if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy") + I18nUtil.getString("system_unvalid")));
        }
        if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy") + I18nUtil.getString("system_unvalid")));
        }

        // ChildJobId valid
        if (jobInfo.getChildJobId() != null && jobInfo.getChildJobId().trim().length() > 0) {
            String[] childJobIds = jobInfo.getChildJobId().split(",");
            for (String childJobIdItem : childJobIds) {
                if (childJobIdItem != null && childJobIdItem.trim().length() > 0 && isNumeric(childJobIdItem)) {
                    IapXxlJobInfo childJobInfo = iapXxlJobInfoDao.loadById(Integer.parseInt(childJobIdItem));
                    if (childJobInfo == null) {
                        return new ReturnT<String>(ReturnT.FAIL_CODE,
                                MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_not_found")), childJobIdItem));
                    }
                } else {
                    return new ReturnT<String>(ReturnT.FAIL_CODE,
                            MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId") + "({0})" + I18nUtil.getString("system_unvalid")), childJobIdItem));
                }
            }

            // join , avoid "xxx,,"
            String temp = "";
            for (String item : childJobIds) {
                temp += item + ",";
            }
            temp = temp.substring(0, temp.length() - 1);

            jobInfo.setChildJobId(temp);
        }

        // group valid
        IapXxlJobGroup jobGroup = iapXxlJobGroupDao.load(jobInfo.getJobGroup());
        if (jobGroup == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_jobgroup") + I18nUtil.getString("system_unvalid")));
        }

        // stage job info
        IapXxlJobInfo existsJobInfo = iapXxlJobInfoDao.loadById(jobInfo.getId());
        if (existsJobInfo == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_id") + I18nUtil.getString("system_not_found")));
        }

        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = existsJobInfo.getTriggerNextTime();
        if (existsJobInfo.getTriggerStatus() == 1 && !jobInfo.getJobCron().equals(existsJobInfo.getJobCron())) {
            try {
                Date nextValidTime = new CronExpression(jobInfo.getJobCron()).getNextValidTimeAfter(new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
                if (nextValidTime == null) {
                    return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_never_fire"));
                }
                nextTriggerTime = nextValidTime.getTime();
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
                return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid") + " | " + e.getMessage());
            }
        }

        existsJobInfo.setJobGroup(jobInfo.getJobGroup());
        existsJobInfo.setJobCron(jobInfo.getJobCron());
        existsJobInfo.setJobDesc(jobInfo.getJobDesc());
        existsJobInfo.setAuthor(jobInfo.getAuthor());
        existsJobInfo.setAlarmEmail(jobInfo.getAlarmEmail());
        existsJobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
        existsJobInfo.setExecutorHandler(jobInfo.getExecutorHandler());
        existsJobInfo.setExecutorParam(jobInfo.getExecutorParam());
        existsJobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        existsJobInfo.setExecutorTimeout(jobInfo.getExecutorTimeout());
        existsJobInfo.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
        existsJobInfo.setChildJobId(jobInfo.getChildJobId());
        existsJobInfo.setTriggerNextTime(nextTriggerTime);

        existsJobInfo.setUpdateTime(new Date());
        iapXxlJobInfoDao.update(existsJobInfo);


        return ReturnT.SUCCESS;
    }

    /**
     * 移除任务（XxlJobInfo）
     * *
     *
     * @param id
     * @return
     */
    @Override
    public ReturnT<String> remove(int id) {
        IapXxlJobInfo iapXxlJobInfo = iapXxlJobInfoDao.loadById(id);
        if (iapXxlJobInfo == null) {
            return ReturnT.SUCCESS;
        }

        iapXxlJobInfoDao.delete(id);
        iapXxlJobLogDao.delete(id);
        iapXxlJobLogGlueDao.deleteByJobId(id);
        return ReturnT.SUCCESS;
    }

    /**
     * 启动任务（XxlJobInfo）
     *
     * @param id
     * @return
     */
    @Override
    public ReturnT<String> start(int id) {
        IapXxlJobInfo iapXxlJobInfo = iapXxlJobInfoDao.loadById(id);

        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = 0;
        try {
            Date nextValidTime = new CronExpression(iapXxlJobInfo.getJobCron()).getNextValidTimeAfter(new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
            if (nextValidTime == null) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_never_fire"));
            }
            nextTriggerTime = nextValidTime.getTime();
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid") + " | " + e.getMessage());
        }

        iapXxlJobInfo.setTriggerStatus(1);
        iapXxlJobInfo.setTriggerLastTime(0);
        iapXxlJobInfo.setTriggerNextTime(nextTriggerTime);

        iapXxlJobInfo.setUpdateTime(new Date());
        iapXxlJobInfoDao.update(iapXxlJobInfo);
        return ReturnT.SUCCESS;
    }

    /**
     * 停止任务（XxlJobInfo）
     *
     * @param id
     * @return
     */
    @Override
    public ReturnT<String> stop(int id) {
        IapXxlJobInfo iapXxlJobInfo = iapXxlJobInfoDao.loadById(id);

        iapXxlJobInfo.setTriggerStatus(0);
        iapXxlJobInfo.setTriggerLastTime(0);
        iapXxlJobInfo.setTriggerNextTime(0);

        iapXxlJobInfo.setUpdateTime(new Date());
        iapXxlJobInfoDao.update(iapXxlJobInfo);
        return ReturnT.SUCCESS;
    }

    /**
     * dashboard info
     *
     * @return
     */
    @Override
    public Map<String, Object> dashboardInfo() {

        int jobInfoCount = iapXxlJobInfoDao.findAllCount();
        int jobLogCount = 0;
        int jobLogSuccessCount = 0;
        IapXxlJobLogReport iapXxlJobLogReport = iapXxlJobLogReportDao.queryLogReportTotal();
        if (iapXxlJobLogReport != null) {
            jobLogCount = iapXxlJobLogReport.getRunningCount() + iapXxlJobLogReport.getSucCount() + iapXxlJobLogReport.getFailCount();
            jobLogSuccessCount = iapXxlJobLogReport.getSucCount();
        }

        // executor count
        Set<String> executorAddressSet = new HashSet<String>();
        List<IapXxlJobGroup> groupList = iapXxlJobGroupDao.findAll();

        if (groupList != null && !groupList.isEmpty()) {
            for (IapXxlJobGroup group : groupList) {
                if (group.getRegistryList() != null && !group.getRegistryList().isEmpty()) {
                    executorAddressSet.addAll(group.getRegistryList());
                }
            }
        }

        int executorCount = executorAddressSet.size();

        Map<String, Object> dashboardMap = new HashMap<String, Object>();
        dashboardMap.put("jobInfoCount", jobInfoCount);
        dashboardMap.put("jobLogCount", jobLogCount);
        dashboardMap.put("jobLogSuccessCount", jobLogSuccessCount);
        dashboardMap.put("executorCount", executorCount);
        return dashboardMap;
    }

    /**
     * chart info
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {

        // process
        List<String> triggerDayList = new ArrayList<String>();
        List<Integer> triggerDayCountRunningList = new ArrayList<Integer>();
        List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
        List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
        int triggerCountRunningTotal = 0;
        int triggerCountSucTotal = 0;
        int triggerCountFailTotal = 0;

        List<IapXxlJobLogReport> logReportList = iapXxlJobLogReportDao.queryLogReport(startDate, endDate);

        if (logReportList != null && logReportList.size() > 0) {
            for (IapXxlJobLogReport item : logReportList) {
                String day = DateUtil.formatDate(item.getTriggerDay());
                int triggerDayCountRunning = item.getRunningCount();
                int triggerDayCountSuc = item.getSucCount();
                int triggerDayCountFail = item.getFailCount();

                triggerDayList.add(day);
                triggerDayCountRunningList.add(triggerDayCountRunning);
                triggerDayCountSucList.add(triggerDayCountSuc);
                triggerDayCountFailList.add(triggerDayCountFail);

                triggerCountRunningTotal += triggerDayCountRunning;
                triggerCountSucTotal += triggerDayCountSuc;
                triggerCountFailTotal += triggerDayCountFail;
            }
        } else {
            for (int i = -6; i <= 0; i++) {
                triggerDayList.add(DateUtil.formatDate(DateUtil.addDays(new Date(), i)));
                triggerDayCountRunningList.add(0);
                triggerDayCountSucList.add(0);
                triggerDayCountFailList.add(0);
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("triggerDayList", triggerDayList);
        result.put("triggerDayCountRunningList", triggerDayCountRunningList);
        result.put("triggerDayCountSucList", triggerDayCountSucList);
        result.put("triggerDayCountFailList", triggerDayCountFailList);

        result.put("triggerCountRunningTotal", triggerCountRunningTotal);
        result.put("triggerCountSucTotal", triggerCountSucTotal);
        result.put("triggerCountFailTotal", triggerCountFailTotal);


        int jobInfoCount = iapXxlJobInfoDao.findAllCount();
        int jobLogCount = 0;
        int jobLogSuccessCount = 0;
        IapXxlJobLogReport iapXxlJobLogReport = iapXxlJobLogReportDao.queryLogReportTotal();
        if (iapXxlJobLogReport != null) {
            jobLogCount = iapXxlJobLogReport.getRunningCount() + iapXxlJobLogReport.getSucCount() + iapXxlJobLogReport.getFailCount();
            jobLogSuccessCount = iapXxlJobLogReport.getSucCount();
        }

        // executor count
//        Set<String> executorAddressSet = new HashSet<String>();
        List<IapXxlJobGroup> groupList = iapXxlJobGroupDao.findAll();

       /* if (groupList != null && !groupList.isEmpty()) {
            for (XxlJobGroup group : groupList) {
                if (group.getRegistryList() != null && !group.getRegistryList().isEmpty()) {
                    executorAddressSet.addAll(group.getRegistryList());
                }
            }
        }

        int executorCount = executorAddressSet.size();*/

        result.put("jobInfoCount", jobInfoCount);
        result.put("jobLogCount", jobLogCount);
        result.put("jobLogSuccessCount", jobLogSuccessCount);
        result.put("executorCount", groupList.stream().distinct().collect(Collectors.toList()).size());

        return new ReturnT<Map<String, Object>>(result);
    }

}
