package com.xxl.job.admin.service;


import com.xxl.job.admin.core.model.IapXxlJobInfo;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.Date;
import java.util.Map;

/**
 * xxl-job的Serivce层
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
public interface IapXxlJobService {

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
    public Map<String, Object> pageList(int start, int length, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author);

    /**
     * 添加任务（XxlJobInfo）
     *
     * @param jobInfo
     * @return
     */
    public ReturnT<String> add(IapXxlJobInfo jobInfo);

    /**
     * 更新任务（XxlJobInfo）
     *
     * @param jobInfo
     * @return
     */
    public ReturnT<String> update(IapXxlJobInfo jobInfo);

    /**
     * 移除任务（XxlJobInfo）
     * *
     *
     * @param id
     * @return
     */
    public ReturnT<String> remove(int id);

    /**
     * 启动任务（XxlJobInfo）
     *
     * @param id
     * @return
     */
    public ReturnT<String> start(int id);

    /**
     * 停止任务（XxlJobInfo）
     *
     * @param id
     * @return
     */
    public ReturnT<String> stop(int id);

    /**
     * dashboard info
     *
     * @return
     */
    public Map<String, Object> dashboardInfo();

    /**
     * chart info
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate);

}
