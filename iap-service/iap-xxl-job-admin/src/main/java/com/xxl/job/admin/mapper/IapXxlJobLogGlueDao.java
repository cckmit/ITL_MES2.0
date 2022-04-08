package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.job.admin.core.model.IapXxlJobLogGlue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表（xxl_job_logglue）的操作
 * 任务GLUE日志：用于保存GLUE更新历史，用于支持GLUE的版本回溯功能
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Mapper
public interface IapXxlJobLogGlueDao extends BaseMapper<IapXxlJobLogGlue> {

    /**
     * 保存
     *
     * @param iapXxlJobLogGlue
     * @return int
     */
    public int save(IapXxlJobLogGlue iapXxlJobLogGlue);

    /**
     * 根据 jobId 查找 XxlJobLogGlue列表
     *
     * @param jobId 任务，主键ID
     * @return List<XxlJobLogGlue>
     */
    public List<IapXxlJobLogGlue> findByJobId(@Param("jobId") int jobId);

    /**
     * 清除limit次之前的备份记录
     *
     * @param jobId 任务，主键ID
     * @param limit 数量（30）
     * @return
     */
    public int removeOld(@Param("jobId") int jobId, @Param("limit") int limit);

    /**
     * 根据 jobId 删除 xxl_job_logglue表记录
     *
     * @param jobId 任务，主键ID
     * @return int
     */
    public int deleteByJobId(@Param("jobId") int jobId);

}
