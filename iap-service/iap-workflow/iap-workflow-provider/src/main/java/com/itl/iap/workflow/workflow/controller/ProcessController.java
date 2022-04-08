/**
 * Copyright (C):  Evergrande Group
 * FileName: ProcessController
 * Author:   huangjianming
 * Date:     2020-06-24 15:36
 * Description:
 */
package com.itl.iap.workflow.workflow.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.workflow.api.dto.ProcessDefinitionDto;
import com.itl.iap.workflow.workflow.service.ProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程控制器
 *
 * @author 黄建明
 * @date 2020-06-24
 * @since jdk1.8
 */
@Api("流程控制器")
@RestController
@RequestMapping("/process")
@Log4j2
public class ProcessController {

    @Resource
    private ProcessService processService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserUtil userUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private FormService formService;


    @PostMapping("/query")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ResponseData queryRecord(@RequestBody String code) {
        log.info("IapSysRoleAuthTDto queryRecord...");
        return ResponseData.success(processService.query(code));
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建流程实例", notes = "创建流程实例")
    public ResponseData<ProcessDefinitionDto> createProcessInstaceByKey(@NotNull @RequestBody ProcessDefinitionDto processDefinition) {
        log.info("---->>>create process instace start");
        try {
            /*if (ObjectUtils.isEmpty(processDefinition.getVariables())) {
                throw new NullPointerException("流程变量不能为空");
            }*/
            ResponseData<IapSysUserTDto> iapSysUserDtoResponseData = userService.queryByName(userUtil.getUser().getUserName());
            IapSysUserTDto user = iapSysUserDtoResponseData.getData();
            //1.获取定义信息
            ProcessDefinition processDefinitionIdResult = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinition.getProcessDefinitionId())
                    .singleResult();
            if (ObjectUtils.isEmpty(processDefinitionIdResult)) {
                throw new IllegalArgumentException("错误的processDefinitionId");
            }
            //设置流程变量
            processDefinition.getVariables().put("creator", user.getUserName());
            processDefinition.getVariables().put("processName", processDefinition.getProcessName());
            processDefinition.getVariables().put("createTime", new Date());
            processDefinition.getVariables().put("businessKey",processDefinition.getBusinessKey());
            processDefinition.getVariables().put("toDelete", Boolean.FALSE);
//            processDefinition.getVariables().put("formKey",getProcessDefinitionFormData(processDefinitionIdResult.getId()));
            ProcessInstance processInstance = runtimeService.createProcessInstanceById(processDefinitionIdResult.getId())
                    .businessKey(processDefinition.getBusinessKey())
                    //.processDefinitionTenantId(processDefinition.getTenantId())    //设置发起人
                    .setVariables(processDefinition.getVariables())    //设置流程变量
                    .execute();     //开始执行
            //业务主键ID
            processDefinition.setProcessInstanceId(processInstance.getId());
            processDefinition.setBusinessKey(processInstance.getBusinessKey());
            return ResponseData.success(processDefinition);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error("500", e.getMessage());
        }
    }

    @PostMapping("/createAndApprove")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "创建流程实例", notes = "创建流程实例")
    public ResponseData<ProcessDefinitionDto> createAndApprove(@NotNull @RequestBody ProcessDefinitionDto processDefinition) {
        log.info("---->>>create process instace start");
        try {
            //1.获取定义信息
            ProcessDefinition processDefinitionIdResult = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinition.getProcessDefinitionId())
                    .singleResult();
            if (ObjectUtils.isEmpty(processDefinitionIdResult)) {
                throw new IllegalArgumentException("错误的processDefinitionId");
            }
            //设置流程变量
            processDefinition.getVariables().put("creator", processDefinition.getCreator());
            processDefinition.getVariables().put("processName", processDefinition.getProcessName());
            processDefinition.getVariables().put("createTime", new Date());
            processDefinition.getVariables().put("businessKey",processDefinition.getBusinessKey());
            processDefinition.getVariables().put("toDelete", Boolean.FALSE);
            ProcessInstance processInstance = runtimeService.createProcessInstanceById(processDefinitionIdResult.getId())
                    .businessKey(processDefinition.getBusinessKey())
                    .setVariables(processDefinition.getVariables())    //设置流程变量
                    .execute();     //开始执行
            //业务主键ID
            processDefinition.setProcessInstanceId(processInstance.getId());
            processDefinition.setBusinessKey(processInstance.getBusinessKey());
            //执行完毕后执行第一个节点审批操作
            Task task = taskService.createTaskQuery().processInstanceId(processDefinition.getProcessInstanceId()).singleResult();
            //设置当前处理人完成任务领取
            if(StringUtils.isEmpty(task.getAssignee())){
                taskService.claim(task.getId(), userUtil.getUser().getUserName());
            }
            Map variables = new HashMap<String,String>();
            //操作类型
            variables.put("action","自动审批通过");
            variables.put(task.getId()+"_opType","approve");
            variables.put("nowApprove","approve");
            String formKey = (String)taskService.getVariable(task.getId(),"formKey");
            if(formKey == null || "".equals(formKey)){
                variables.put("formKey", getTaskFormData(task.getId()).getFormKey());
            }
            //每个节点保存formKey
            variables.put(task.getTaskDefinitionKey() + "_formKey", getTaskFormData(task.getId()).getFormKey());
            //执行通过
            taskService.complete(task.getId(), variables);
            taskService.createComment(task.getId(), task.getProcessInstanceId(), "自动审批通过");

            return ResponseData.success(processDefinition);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error("500", e.getMessage());
        }
    }

    @PostMapping("/batchCreateAndApprove")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "批量创建流程实例并自动审批", notes = "批量创建流程实例并自动审批")
    public ResponseData<String> batchCreateAndApprove(
            @NotNull @RequestBody List<ProcessDefinitionDto> processDefinitionList) {
        log.info("---->>>create process instaceList start");
        try {
            for (ProcessDefinitionDto processDefinition : processDefinitionList) {
                //1.获取定义信息
                ProcessDefinition processDefinitionIdResult = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(processDefinition.getProcessDefinitionId())
                        .singleResult();
                if (ObjectUtils.isEmpty(processDefinitionIdResult)) {
                    throw new IllegalArgumentException("错误的processDefinitionId");
                }
                //设置流程变量
                processDefinition.getVariables().put("creator", processDefinition.getCreator());
                processDefinition.getVariables().put("processName", processDefinition.getProcessName());
                processDefinition.getVariables().put("createTime", new Date());
                processDefinition.getVariables().put("businessKey",processDefinition.getBusinessKey());
                processDefinition.getVariables().put("toDelete", Boolean.FALSE);
                ProcessInstance processInstance = runtimeService.createProcessInstanceById(processDefinitionIdResult.getId())
                        .businessKey(processDefinition.getBusinessKey())
                        .setVariables(processDefinition.getVariables())    //设置流程变量
                        .execute();     //开始执行
                //业务主键ID
                processDefinition.setProcessInstanceId(processInstance.getId());
                processDefinition.setBusinessKey(processInstance.getBusinessKey());
                //执行完毕后执行第一个节点审批操作
                Task task = taskService.createTaskQuery().processInstanceId(processDefinition.getProcessInstanceId()).singleResult();
                //设置当前处理人完成任务领取
                if(StringUtils.isEmpty(task.getAssignee())){
                    taskService.claim(task.getId(), userUtil.getUser().getUserName());
                }
                Map variables = new HashMap<String,String>();
                //操作类型
                variables.put("action","自动审批通过");
                variables.put(task.getId()+"_opType","approve");
                variables.put("nowApprove","approve");
                //判断是否第一个节点页面
                String formKey = (String)taskService.getVariable(task.getId(),"formKey");
                if(formKey == null || "".equals(formKey)){
                    variables.put("formKey", getTaskFormData(task.getId()).getFormKey());
                }
                //每个节点保存formKey
                variables.put(task.getTaskDefinitionKey() + "_formKey", getTaskFormData(task.getId()).getFormKey());

                //执行通过
                taskService.complete(task.getId(), variables);
                taskService.createComment(task.getId(), task.getProcessInstanceId(), "自动审批通过");
            }
            return ResponseData.success("Success");
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseData.error("500", e.getMessage());
        }
    }


    @GetMapping("/testQuery")
    @ApiOperation(value = "测试查询", notes = "测试查询")
    public ResponseData queryRecord() {
        return ResponseData.success(processService.query("49b2d0ba36f14210ad7a39a45e333111"));
    }


    /**
     * 获取表单方法
     * @param taskId
     * @return
     */
    private TaskFormData getTaskFormData(String taskId) {
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        return taskFormData;
    }

    @DeleteMapping("/remove")
    @ApiOperation(value = "创建流程实例", notes = "创建流程实例")
    public ResponseData<String> removeInstance() {
        log.info("---->>>remove process instace start");
        try {
            runtimeService.deleteProcessInstance("","");
            return ResponseData.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error(e.getMessage());
        }
    }
}
