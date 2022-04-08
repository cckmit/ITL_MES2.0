package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.job.admin.core.model.IapXxlJobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 表（xxl_job_info）的操作
 * 调度扩展信息表： 用于保存XXL-JOB调度任务的扩展信息，如任务分组、任务名、机器地址、执行器、执行入参和报警邮件等等
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Mapper
public interface IapXxlJobInfoDao extends BaseMapper<IapXxlJobInfo> {

    /**
     * 分页查询
     *
     * @param offset          偏移大小
     * @param pagesize        每页大小
     * @param jobGroup        执行器主键ID
     * @param triggerStatus   调度状态（0-停止，1-运行）
     * @param jobDesc         任务描述
     * @param executorHandler 行器任务handler
     * @param author          作者
     * @return List<XxlJobInfo>
     */
    public List<IapXxlJobInfo> pageList(@Param("offset") int offset,
                                        @Param("pagesize") int pagesize,
                                        @Param("jobGroup") int jobGroup,
                                        @Param("triggerStatus") int triggerStatus,
                                        @Param("jobDesc") String jobDesc,
                                        @Param("executorHandler") String executorHandler,
                                        @Param("author") String author);

    /**
     * 查询总页数
     *
     * @param offset          偏移量
     * @param pagesize        每页da
     * @param jobGroup        执行器主键ID
     * @param triggerStatus   调度状态（0-停止，1-运行）
     * @param jobDesc         任务描述
     * @param executorHandler 执行器任务handler
     * @param author          作者
     * @return int
     */
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("jobGroup") int jobGroup,
                             @Param("triggerStatus") int triggerStatus,
                             @Param("jobDesc") String jobDesc,
                             @Param("executorHandler") String executorHandler,
                             @Param("author") String author);

    /**
     * 保存 XxlJobInfo
     *
     * @param info XxlJobInfo
     * @return int
     */
    public int save(IapXxlJobInfo info);

    /**
     * 根据 XxlJobInfo的id 查询 XxlJobInfo
     *
     * @param id XxlJobInfo的id
     * @return XxlJobInfo
     */
    public IapXxlJobInfo loadById(@Param("id") int id);

    /**
     * 查询所有 XxlJobInfo
     *
     * @return List<XxlJobInfo>
     */
    public List<IapXxlJobInfo> loadByAll();

    /**
     * 更新 XxlJobInfo
     *
     * @param iapXxlJobInfo
     * @return int
     */
    public int update(IapXxlJobInfo iapXxlJobInfo);

    /**
     * 根据 xxlJobInfo的id删除
     *
     * @param id xxlJobInfo的id
     * @return　int
     */
    public int delete(@Param("id") long id);

    /**
     * 通过 jobGroup 查询 XxlJobInfo列表
     *
     * @param jobGroup 执行器主键ID
     * @return List<XxlJobInfo>
     */
    public List<IapXxlJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

    /**
     * 查询所有 XxlJobInfo 的总数
     *
     * @return int
     */
    public int findAllCount();

    /**
     * 查询调度状态为正在运行（trigger_status = 1）且下次调度时间(trigger_next_time)小于 maxNextTime 的 XxlJobInfo
     *
     * @param maxNextTime 最大的下次调度时间戳
     * @param pagesize    每页大小
     * @return List<XxlJobInfo>
     */
    public List<IapXxlJobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize);

    /**
     * 更新 XxlJobInfo
     *
     * @param iapXxlJobInfo
     * @return int
     */
    public int scheduleUpdate(IapXxlJobInfo iapXxlJobInfo);


}
