/**
 * Copyright (C):  Evergrande Group
 * FileName: TaskDemoListener2
 * Author:   huangjianming
 * Date:     2020-09-03 17:22
 * Description:
 */
package com.itl.iap.workflow.business.listener;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapEmployeeTDto;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * task监听器案例
 *
 * @author 黄建明
 * @date 2020-09-03
 * @since jdk1.8
 */
@Log4j2
@Service
public class TaskDemoListener2  implements TaskListener {

    @Resource
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("调用开户审批通过任务监听器开始，任务id：" + delegateTask.getId());
        ProcessInstance instance = (ProcessInstance) runtimeService.createProcessInstanceQuery()
                .processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
        if (instance != null) {
            String nowOperation = (String) delegateTask.getVariable("nowOperation");
            String businessKey = instance.getBusinessKey();
            IapEmployeeTDto employeeDto = new IapEmployeeTDto();
            /*employeeTDto.setWorkflowCode(businessKey);

            employeeTDto.setUserName(nowOperation);
            ResponseData responseData = employeeService.updateByWorkflowCode(employeeTDto);
            if (responseData != null && "200".equals(responseData.getCode())) {
                log.info("调用开户审批通过任务监听器执行审批成功，任务id：" + delegateTask.getId());
            } else {
                if (responseData != null) {
                    throw new RuntimeException(responseData.getMsg());
                } else {
                    throw new RuntimeException("通过业务编号批量修改流程状态失败");
                }
            }*/
            //以上是调用案例
        }
        log.info("调用开户审批通过任务监听器结束，任务id：" + delegateTask.getId());
    }
}