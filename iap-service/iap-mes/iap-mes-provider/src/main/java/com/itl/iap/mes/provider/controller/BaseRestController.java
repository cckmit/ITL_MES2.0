package com.itl.iap.mes.provider.controller;

import com.itl.iap.mes.provider.common.BaseResponse;
import com.itl.iap.mes.provider.common.PaginationResponse;
import com.itl.iap.mes.provider.common.ResponseDTOBuilder;
import org.springframework.context.MessageSource;

import javax.annotation.Resource;

/**
 * @author 胡广
 * @version 1.0
 * @name BaseRestController
 * @description
 * @date 2019-07-22
 */
public class BaseRestController {

    @Resource(name = "messageSource")
    protected MessageSource messageSource;

    protected <T extends BaseResponse> ResponseDTOBuilder<T > getBuilder(Class<T> clazz) {
        return ResponseDTOBuilder.with(clazz).messageSource(messageSource);
    }

    protected ResponseDTOBuilder<BaseResponse> getBuilder() {
        return ResponseDTOBuilder.with(BaseResponse.class).messageSource(messageSource);
    }

    protected ResponseDTOBuilder<PaginationResponse> getPageBuilder() {
        return ResponseDTOBuilder.with(PaginationResponse.class).messageSource(messageSource);
    }


}
