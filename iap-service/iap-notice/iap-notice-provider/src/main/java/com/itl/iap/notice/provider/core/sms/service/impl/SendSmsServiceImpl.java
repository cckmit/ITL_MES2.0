package com.itl.iap.notice.provider.core.sms.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.notice.api.entity.MsgMailConfiguration;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.entity.email.model.SmsNotice;
import com.itl.iap.notice.provider.core.sms.service.ISendSmsService;
import com.itl.iap.notice.provider.core.sms.utils.SendSmsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 阿里云短信服务
 *
 * @author 曾慧任
 * @date 2020/06/29
 * @since jdk1.8
 */
@Service("sendSmsService")
public class SendSmsServiceImpl implements ISendSmsService {

    @Value("${defaultConnectTimeout}")
    private String connectTimeOut;
    @Value("${defaultReadTimeout}")
    private String readTimeOut;

    @Override
    public ResponseData sendSms(SmsNotice smsNotice, MsgPublicTemplate msgPublicTemplate, MsgMailConfiguration msgMailConfiguration) {

        SendSmsResponse sendSmsResponse = SendSmsUtil.sendSms(connectTimeOut, readTimeOut, msgMailConfiguration.getUsername(), msgMailConfiguration.getPassword(), smsNotice.getPhones(), msgPublicTemplate.getSign(), msgPublicTemplate.getMessageCode(), JSONObject.toJSONString(smsNotice.getParams()));
        if ("OK".equals(sendSmsResponse.getCode())) {
            //请求成功
            return ResponseData.success(sendSmsResponse.getMessage());
        } else {
            //修改成抛出异常，让方法捕捉
            return ResponseData.error(sendSmsResponse.getMessage());
        }
//        return new SmsResult( CommonCode.FAIL.code(), CommonCode.FAIL.success(), sendSmsResponse.getMessage());
    }
}
