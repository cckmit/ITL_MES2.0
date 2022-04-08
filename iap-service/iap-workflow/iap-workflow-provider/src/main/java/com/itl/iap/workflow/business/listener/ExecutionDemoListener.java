/**
 * Copyright (C):  Evergrande Group
 * FileName: ExecutionDemoListener
 * Author:   huangjianming
 * Date:     2020-07-09 17:23
 * Description:
 */
package com.itl.iap.workflow.business.listener;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 黄建明
 * @date 2020-07-09
 * @since jdk1.8
 */
@Log4j2
@Service
public class ExecutionDemoListener implements ExecutionListener {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        log.info("执行 ExecutionDemoListener 案例");
        //1、可引用业务的方法
        //service.updateByObj(obj);

        //2、可修改流程变量(用于改变流程的流转条件)
        Map<String, Object> variables = new HashMap<>();
        variables.put("FormField_001", false);
        runtimeService.setVariables(delegateExecution.getId(),variables);
    }
}
