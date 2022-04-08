package com.itl.iap.workflow.api.dto;

import com.google.common.collect.Lists;
import lombok.Data;
import org.camunda.bpm.engine.history.HistoricTaskInstance;

import java.util.Date;
import java.util.List;

/**
 * 查询工作流待办已办的entity
 *
 * @author 李虎
 * @date 2020/7/1
 * @since jdk1.8
 */
@Data
public class DoneDto extends Page {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 完成状态：true已完成、false未完成(默认)
     */
    private Boolean finished = false;
    /**
     * 启动结束时间（必须是北京时间且格式yyyy-MM-dd HH:mm:ss）
     */
    private Date startedBefore;
    /**
     * 启动开始时间（必须是北京时间且格式yyyy-MM-dd HH:mm:ss）
     */
    private Date startedAfter;
    /**
     * 完成结束时间（必须是北京时间且格式yyyy-MM-dd HH:mm:ss）
     */
    private Date finishedBefore;
    /**
     * 完成开始时间（必须是北京时间且格式yyyy-MM-dd HH:mm:ss）
     */
    private Date finishedAfter;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 业务编码
     */
    private String businessKey;
    /**
     * 列表数据
     */
    List<HistoricTaskInstance> list = Lists.newArrayList();

}
