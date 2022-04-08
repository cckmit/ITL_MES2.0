/**
 * Copyright (C):  Evergrande Group
 * FileName: ProcessFeignFallback
 * Author:   huangjianming
 * Date:     2020-07-10 16:18
 * Description:
 */
package com.itl.iap.workflow.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.workflow.api.dto.ProcessDefinitionDto;
import com.itl.iap.workflow.client.service.ProcessFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Process远程调用熔断服务
 *
 * @author 黄建明
 * @date 2020-07-10
 * @since jdk1.8
 */
@Slf4j
@Component
public class ProcessFeignFallback implements ProcessFeignClient {

    @Override
    public ResponseData<ProcessDefinitionDto> createProcessInstaceByKey(@NotNull ProcessDefinitionDto processDefinition) {
        log.error("sorry,process createProcessInstaceByKey feign fallback processDefinition:{}", JSON.toJSON(processDefinition));
        return null;
    }

    @Override
    public ResponseData<ProcessDefinitionDto> createAndApprove(@NotNull ProcessDefinitionDto processDefinition) {
        log.error("sorry,process createAndApprove feign fallback processDefinition:{}", JSON.toJSON(processDefinition));
        return null;
    }
}
