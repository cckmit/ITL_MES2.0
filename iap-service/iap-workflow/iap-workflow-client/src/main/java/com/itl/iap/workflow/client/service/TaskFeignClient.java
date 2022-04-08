package com.itl.iap.workflow.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.workflow.client.config.FallBackConfig;
import com.itl.iap.workflow.client.service.impl.TaskFeignFallback;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Task任务远程调用
 *
 * @author 黄建明
 * @date 2020-07-10
 * @since jdk1.8
 */
@FeignClient(value = "iap-workflow-provider", fallback = TaskFeignFallback.class, configuration = FallBackConfig.class)
public interface TaskFeignClient {

    @ApiOperation("流程审批，同意")
    @PostMapping("/task/approveTaskById/{taskId}")
    ResponseData<Boolean> approveTaskById(@PathVariable("taskId") String taskId,
                                          @RequestBody Map<String, Object> variables);

    @ApiOperation("流程驳回")
    @GetMapping("/task/rejectTaskById/{taskId}/{message}")
    public void rejectTaskById(@PathVariable("taskId") String taskId,
                               @PathVariable("message") String message,
                               @RequestBody Map<String, Object> variables);


}