package com.itl.iap.notice.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.notice.api.pojo.ResponseCode;
import com.itl.iap.notice.api.service.SmsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @ApiOperation(value = "发送消息", notes = "发送消息")
    @PostMapping(value = "/sendMessage")
    public ResponseData sendMessage(@RequestBody Map<String,String> notice) {
        smsService.sendMessage(notice);
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }
}
