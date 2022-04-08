package com.itl.iap.workflow.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程定义dto
 *
 * @author 黄建明
 * @date 2020-06-24
 * @since jdk1.8
 */
@Data
public class ProcessDefinitionDto {
    /**
     * 流程定义ID
     */
    @NotNull(message = "流程定义ID不能为空")
    private String processDefinitionId;
    /**
     * 流程定义KEY
     */
    private String processDefinitionKey;
    /**
     * 创建人
     */
    @NotNull(message = "创建人不能为空")
    private String creator;
    /**
     * 创建后的流程实例ID
     */
    private String processInstanceId;
    /**
     * 唯一业务主键
     */
    private String businessKey;

    /**
     * 流程名称
     */
    @NotNull(message = "流程名称不能为空")
    private String processName;

    /**
     * 流程变量
     */
    private Map<String, Object> variables = new HashMap<>();

}
