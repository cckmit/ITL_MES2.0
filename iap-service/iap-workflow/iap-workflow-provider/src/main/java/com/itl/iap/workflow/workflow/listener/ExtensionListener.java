package com.itl.iap.workflow.workflow.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.jobexecutor.NotifyAcquisitionRejectedJobsHandler;
import org.camunda.bpm.model.bpmn.impl.instance.UserTaskImpl;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;

import java.util.Collection;

/**
 * 专门为扩展属性加的监听器，用于扩展属性里面的 指定人，在这里存入全局变量，到审批的时候取出来
 *
 * @author 罗霄
 * @date 2020-09-15
 * @since jdk1.8
 */
public class ExtensionListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {



        final String appointType = "appointUser";
        UserTaskImpl startEvent = (UserTaskImpl) delegateTask.getBpmnModelElementInstance();
        ExtensionElements extensionElements = startEvent.getExtensionElements();
        if (extensionElements != null) {
            Collection<CamundaProperty> properties = extensionElements.getElementsQuery()
                    .filterByType(CamundaProperties.class)
                    .singleResult()
                    .getCamundaProperties();

            for (CamundaProperty property : properties) {
                if (appointType.equalsIgnoreCase(property.getCamundaName())) {
                    //放到缓存
                    delegateTask.setVariable("appointType",property.getCamundaValue());
                }
            }
        }
    }
}
