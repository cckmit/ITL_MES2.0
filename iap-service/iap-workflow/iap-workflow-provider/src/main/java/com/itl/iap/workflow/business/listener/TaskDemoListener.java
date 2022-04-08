/**
 * Copyright (C):  Evergrande Group
 * FileName: TaskDemoListener
 * Author:   huangjianming
 * Date:     2020-07-07 18:44
 * Description:
 */
package com.itl.iap.workflow.business.listener;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.model.bpmn.impl.instance.UserTaskImpl;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * task监听器案例
 *
 * @author 黄建明
 * @date 2020-07-07
 * @since jdk1.8
 */
@Log4j2
@Service
public class TaskDemoListener implements TaskListener {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("执行 TaskDemoListener 案例");
        //1、可引用业务的方法
        //service.updateByObj(obj);

        //2、可修改流程变量(用于改变流程的流转条件)
        Map<String, Object> variables = new HashMap<>();
        variables.put("FormField_001", false);
        runtimeService.setVariables(delegateTask.getExecutionId(), variables);

        //3、可变更受让人(参数1：受让人，参数2：受让人类型（固定值）)
        //delegateTask.addUserIdentityLink("admin","assignee");
        delegateTask.setAssignee("admin");
        //4、可变更候选人(Assignee不设置空，候选人查不到)
        delegateTask.setAssignee(null);
        String[] users = {"admin", "demo"};
        delegateTask.addCandidateUsers(Arrays.asList(users));

        //5、查询拓展字段
        UserTaskImpl startEvent = (UserTaskImpl) delegateTask.getBpmnModelElementInstance();
        ExtensionElements extensionElements = startEvent.getExtensionElements();
        if (extensionElements != null) {
            System.out.println("BaseListener2！！！！");
            Collection<CamundaProperty> properties = extensionElements.getElementsQuery()
                    .filterByType(CamundaProperties.class)
                    .singleResult()
                    .getCamundaProperties();
            for (CamundaProperty property : properties) {
                String name = property.getCamundaName();
                String value = property.getCamundaValue();
                System.out.println("name = " + name + ",value = " + value);
            }
        }
    }
}
