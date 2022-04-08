/**
 * Copyright (C):  Evergrande Group
 * FileName: MyExecutionListener
 * Author:   huangjianming
 * Date:     2020-07-01 21:25
 * Description:
 */
package com.itl.iap.workflow.workflow.listener;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 黄建明
 * @date 2020-07-01
 * @since jdk1.8
 */

@Service
public class MyExecutionListener  implements TaskListener, ExecutionListener {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String eventName = execution.getEventName();
        System.out.println("监听测试111！！！！"+ eventName);
        listenerLogic(execution);
    }

    private void listenerLogic(DelegateExecution execution) {
        String eventName = execution.getEventName();
        String taskId = execution.getId();
        if("create".equals(eventName)){
            //上一个节点完成前调用
            System.out.println("create===========流程创建");
        }else if("assigment".equals(eventName)){
            //节点领取任务调用
            System.out.println("assigment===========流程部署");
            Map<String, Object> variables = new HashMap<>();
            variables.put("FormField_001", false);
            runtimeService.setVariables(execution.getId(),variables);
        }else if("complete".equals(eventName)){
            //节点完成之前任务调用
            System.out.println("complete===========流程完成");
        }else if("delete".equals(eventName)){
            //节点完成之后任务调用
            System.out.println("delete===========流程结束");
        }else if("update".equals(eventName)){
            System.out.println("start===========流程启动");
        }else if("timeout".equals(eventName)){
            System.out.println("end===========流程结束");
        }else {
            System.out.println("eventName========" + eventName);
        }
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        System.out.println("监听测试222！！！！");
        String taskId = delegateTask.getId();
        if("create".equals(eventName)){
            //上一个节点完成前调用
            System.out.println("create===========流程创建");
            Map<String, Object> variables = new HashMap<>();
            variables.put("FormField_001", false);
            runtimeService.setVariables(delegateTask.getExecution().getId(),variables);
        }else if("assigment".equals(eventName)){
            //节点领取任务调用
            System.out.println("assigment===========流程部署");
            Map<String, Object> variables = new HashMap<>();
            variables.put("FormField_001", false);
            runtimeService.setVariables(delegateTask.getExecution().getId(),variables);
        }else if("complete".equals(eventName)){
            //节点完成之前任务调用
            System.out.println("complete===========流程完成");
        }else if("delete".equals(eventName)){
            //节点完成之后任务调用
            System.out.println("delete===========流程结束");
        }else if("update".equals(eventName)){
            System.out.println("update===========流程启动");
        }else if("timeout".equals(eventName)){
            System.out.println("timeout===========流程结束");
        }else {
            System.out.println("eventName========" + eventName);
        }

    }
}