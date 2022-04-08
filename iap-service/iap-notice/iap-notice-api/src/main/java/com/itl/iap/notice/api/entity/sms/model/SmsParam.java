package com.itl.iap.notice.api.entity.sms.model;

import lombok.Data;

import java.util.List;

/**
 * 短信请求参数
 */
@Data
public class SmsParam {
    /**
     * 短信模板
     */
    private String templateCode;
    /**
     * 短信签名
     */
    private String signName;
    /**
     * 电话号码
     */
    private List<String> phoneNumbers;
    /**
     * 短信参数（json格式的数据）
     */
    private String params;

}
