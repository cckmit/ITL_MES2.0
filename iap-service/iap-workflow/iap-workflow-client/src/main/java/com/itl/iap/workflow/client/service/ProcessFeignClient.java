/**
 * Copyright (C):  Evergrande Group
 * FileName: ProcessFeignClient
 * Author:   huangjianming
 * Date:     2020-07-10 16:16
 * Description:
 */
package com.itl.iap.workflow.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.workflow.api.dto.ProcessDefinitionDto;
import com.itl.iap.workflow.client.config.FallBackConfig;
import com.itl.iap.workflow.client.service.impl.ProcessFeignFallback;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;

/**
 * Process远程调用
 *
 * @author 黄建明
 * @date 2020-07-10
 * @since jdk1.8
 */
@FeignClient(value = "iap-workflow-provider", fallback = ProcessFeignFallback.class, configuration = FallBackConfig.class)
public interface ProcessFeignClient {

    @PostMapping("/process/create")
    @ApiOperation(value = "创建流程实例", notes = "创建流程实例")
    ResponseData<ProcessDefinitionDto> createProcessInstaceByKey(
            @NotNull @RequestBody ProcessDefinitionDto processDefinition);


    @PostMapping("/process/createAndApprove")
    @ApiOperation(value = "创建流程实例并默认审批通过第一个节点", notes = "创建流程实例并默认审批通过第一个节点")
    ResponseData<ProcessDefinitionDto> createAndApprove(
            @NotNull @RequestBody ProcessDefinitionDto processDefinition);

}
