package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.job.admin.core.model.IapXxlJobLogReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 表（xxl_job_log_report）的操作
 * 调度日志报表：用户存储XXL-JOB任务调度日志的报表，调度中心报表功能页面会用到；
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Mapper
public interface IapXxlJobLogReportDao extends BaseMapper<IapXxlJobLogReport> {

    /**
     * 保存
     *
     * @param iapXxlJobLogReport
     * @return int
     */
    public int save(IapXxlJobLogReport iapXxlJobLogReport);

    /**
     * 更新
     *
     * @param iapXxlJobLogReport
     * @return int
     */
    public int update(IapXxlJobLogReport iapXxlJobLogReport);

    /**
     * 根据调度时间查询
     *
     * @param triggerDayFrom 调度时间-起始时间
     * @param triggerDayTo   调度时间-结束时间
     * @return List<XxlJobLogReport>
     */
    public List<IapXxlJobLogReport> queryLogReport(@Param("triggerDayFrom") Date triggerDayFrom,
                                                   @Param("triggerDayTo") Date triggerDayTo);

    /**
     * 统计数量
     *
     * @return XxlJobLogReport
     */
    public IapXxlJobLogReport queryLogReportTotal();

}
