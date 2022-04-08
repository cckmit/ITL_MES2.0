package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.job.admin.core.model.IapXxlJobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 表（xxl_job_log）的操作
 * 调度日志表： 用于保存XXL-JOB任务调度的历史信息，如调度结果、执行结果、调度入参、调度机器和执行器等等
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Mapper
public interface IapXxlJobLogDao extends BaseMapper<IapXxlJobLog> {

    /**
     * 分页查询 XxlJobLog（存在jobId不使用jobGroup，不存在使用jobGroup）
     *
     * @param offset           偏移量
     * @param pagesize         每页大小
     * @param jobGroup         执行器主键ID
     * @param jobId            任务，主键ID
     * @param triggerTimeStart trigger_time起始时间
     * @param triggerTimeEnd   trigger_time结束时间
     * @param logStatus        标识符
     * @return
     */
    public List<IapXxlJobLog> pageList(@Param("offset") int offset,
                                       @Param("pagesize") int pagesize,
                                       @Param("jobGroup") int jobGroup,
                                       @Param("jobId") int jobId,
                                       @Param("triggerTimeStart") Date triggerTimeStart,
                                       @Param("triggerTimeEnd") Date triggerTimeEnd,
                                       @Param("logStatus") int logStatus);

    /**
     * 符合条件的总数
     *
     * @param offset           偏移量
     * @param pagesize         每页大小
     * @param jobGroup         执行器主键ID
     * @param jobId            任务，主键ID
     * @param triggerTimeStart trigger_time起始时间
     * @param triggerTimeEnd   trigger_time结束时间
     * @param logStatus        标识符
     * @return
     */
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("jobGroup") int jobGroup,
                             @Param("jobId") int jobId,
                             @Param("triggerTimeStart") Date triggerTimeStart,
                             @Param("triggerTimeEnd") Date triggerTimeEnd,
                             @Param("logStatus") int logStatus);

    /**
     * 根据 XxlJobLog 的 ID 查询
     *
     * @param id XxlJobLog 的 ID
     * @return XxlJobLog
     */
    public IapXxlJobLog load(@Param("id") long id);

    /**
     * 保存 XxlJobLog
     *
     * @param iapXxlJobLog
     * @return
     */
    public long save(IapXxlJobLog iapXxlJobLog);

    /**
     * 更新 XxlJobLog 的 Trigger 信息
     *
     * @param iapXxlJobLog XxlJobLog
     * @return int
     */
    public int updateTriggerInfo(IapXxlJobLog iapXxlJobLog);

    /**
     * 更新 XxlJobLog 的 handele 信息
     *
     * @param iapXxlJobLog XxlJobLog
     * @return int
     */
    public int updateHandleInfo(IapXxlJobLog iapXxlJobLog);

    /**
     * 根据 jobId删除 XxlJobLog
     *
     * @param jobId 任务，主键ID
     * @return int
     */
    public int delete(@Param("jobId") int jobId);

    /**
     * 统计执行器机器数量
     *
     * @param from 起始时间
     * @param to   结束时间
     * @return
     */
    public Map<String, Object> findLogReport(@Param("from") Date from,
                                             @Param("to") Date to);

    /**
     * 查询需要清除的日志（ XxlJobLog）ID
     *
     * @param jobGroup        执行器主键ID
     * @param jobId           任务，主键ID
     * @param clearBeforeTime 清理（XX）之前的日志
     * @param clearBeforeNum  清理（xx）条之前的数据
     * @param pagesize        清理数量
     * @return
     */
    public List<Long> findClearLogIds(@Param("jobGroup") int jobGroup,
                                      @Param("jobId") int jobId,
                                      @Param("clearBeforeTime") Date clearBeforeTime,
                                      @Param("clearBeforeNum") int clearBeforeNum,
                                      @Param("pagesize") int pagesize);

    /**
     * 批量删除XxlJobLog
     *
     * @param logIds
     * @return
     */
    public int clearLog(@Param("logIds") List<Long> logIds);

    /**
     * 查找任务失败的XxlJobLog的ID列表
     *
     * @param pagesize
     * @return
     */
    public List<Long> findFailJobLogIds(@Param("pagesize") int pagesize);

    /**
     * 更新告警状态
     *
     * @param logId          XxlJobLog的ID
     * @param oldAlarmStatus 修改之前的状态
     * @param newAlarmStatus 修改之后的状态
     * @return
     */
    public int updateAlarmStatus(@Param("logId") long logId,
                                 @Param("oldAlarmStatus") int oldAlarmStatus,
                                 @Param("newAlarmStatus") int newAlarmStatus);


    public List<Long> findLostJobIds(@Param("losedTime") Date losedTime);

}
