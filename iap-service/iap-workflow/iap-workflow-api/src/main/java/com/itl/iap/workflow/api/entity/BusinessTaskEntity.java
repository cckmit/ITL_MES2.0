package com.itl.iap.workflow.api.entity;

import lombok.Data;
import org.camunda.bpm.engine.task.Task;

import java.util.Date;
import java.util.Map;

/**
 * @author 汤俊
 * @date 2020-07-09
 * @since jdk1.8
 */
@Data
public class BusinessTaskEntity {

    public BusinessTaskEntity(Task task) {
        this.id = task.getId();
//        this.revision = task.get
        this.assignee = task.getAssignee();
        this.name = task.getName();
        this.priority = task.getPriority();
        this.createTime = task.getCreateTime();
//        this.suspensionState = task.isSuspended();
        this.tenantId = task.getTenantId();
//        this.isIdentityLinksInitialized = task.
        this.executionId = task.getExecutionId();
        this.processInstanceId = task.getProcessInstanceId();
        this.processDefinitionId = task.getProcessDefinitionId();
        this.taskDefinitionKey = task.getTaskDefinitionKey();
//        this.isDeleted = task.del
//        this.isFormKeyInitialized = task.isForm
//        this.formKey = task.getFormKey();
        this.isSuspended = task.isSuspended();
        this.description = task.getDescription();
        this.owner = task.getOwner();
        this.parentTaskId = task.getParentTaskId();
    }

    private String id;
    private int revision;
    private String assignee;
    private String name;
    private int priority;
    private Date createTime;
    private int suspensionState;
    private String tenantId;
    private boolean isIdentityLinksInitialized;
    private String executionId;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private boolean isDeleted;
    private boolean isFormKeyInitialized;
    private String formKey;
    private boolean isSuspended;
    private String description;
    private String owner;
    private String parentTaskId;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;

}
