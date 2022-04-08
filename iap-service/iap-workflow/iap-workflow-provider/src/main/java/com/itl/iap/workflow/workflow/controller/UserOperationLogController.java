package com.itl.iap.workflow.workflow.controller;


import com.google.gson.Gson;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.workflow.api.entity.BusinessCommentEntity;
import com.itl.iap.workflow.api.entity.BusinessHistoricTaskInstanceEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.TaskQueryImpl;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程控制器
 *
 * @author 汤俊
 * @date 2020-07-07
 * @since jdk1.8
 */
@Slf4j
@Api("用户操作")
@RestController
@RequestMapping("/userop")
public class UserOperationLogController {

    @Autowired
    private HistoryService historyService;

    @Autowired
    TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 获取用户历史意见
     * https://blog.csdn.net/jisu30miao1225/article/details/80705529
     *
     * @param processInstanceId
     */
    @ApiOperation("获取流程实例的审批意见")
    @GetMapping("/logs/{processInstanceId}")
    public ResponseData<List<BusinessCommentEntity>> getUserOpList(@PathVariable("processInstanceId") String processInstanceId) {
        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);
        List<BusinessCommentEntity> businessCommentEntities = new ArrayList<>();
        Gson gson = new Gson();
        for (Comment processInstanceComment : processInstanceComments) {
            String commentStr = gson.toJson(processInstanceComment);
            BusinessCommentEntity bce = gson.fromJson(commentStr, BusinessCommentEntity.class);
//            Task task = taskService.createTaskQuery().taskId(processInstanceComment.getTaskId()).singleResult();
            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(processInstanceComment.getTaskId()).singleResult();


            //Map<String, Object> variables = runtimeService.getVariables(taskInstance.getExecutionId());
            //if(variables.containsKey(processInstanceComment.getTaskId()+"_opType")){
            //    bce.setOpType(String.valueOf(variables.get(processInstanceComment.getTaskId()+"_opType")));
            //}
            List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(taskInstance.getProcessInstanceId()).executionIdIn(taskInstance.getExecutionId())
                    .variableName(processInstanceComment.getTaskId()+"_opType").list();

            if(list != null && list.size() > 0) {
                bce.setOpType((String)list.get(0).getValue());
            }
            //获取节点名称
            bce.setTaskName(taskInstance.getName());
            //获取节点处理人
            bce.setAssign(taskInstance.getAssignee());
            businessCommentEntities.add(bce);
        }
        return ResponseData.success(businessCommentEntities);
    }

}
