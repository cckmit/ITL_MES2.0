/**
 * Copyright (C):  Evergrande Group
 * FileName: TaskServiceImpl
 * Author:   huangjianming
 * Date:     2020-07-10 11:19
 * Description:
 */
package com.itl.iap.workflow.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.workflow.client.service.TaskFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Task任务fallback类
 *
 * @author 黄建明
 * @date 2020-07-10
 * @since jdk1.8
 */
@Slf4j
@Component
public class TaskFeignFallback implements TaskFeignClient {

    @Override
    public ResponseData<Boolean> approveTaskById(String taskId,
                                                 Map<String, Object> variables) {
        log.error("sorry,task feign fallback taskId:{},variables:{}", taskId, JSON.toJSONString(variables));
        return null;
    }

    @Override
    public void rejectTaskById(String taskId, String message, Map<String,
            Object> variables) {
        log.error("sorry,task feign fallback taskId:{},message:{},variables:{}",
                taskId, message, JSON.toJSONString(variables));
    }

}
