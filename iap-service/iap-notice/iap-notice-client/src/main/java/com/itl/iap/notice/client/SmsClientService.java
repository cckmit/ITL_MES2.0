package com.itl.iap.notice.client;

import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "iap-notice-provider")
public interface SmsClientService {

    @GetMapping("/sms/sendMessage")
    @ApiOperation(value="发送短信")
    ResponseData sendMessage(@RequestBody Map<String,String> msg);

}
