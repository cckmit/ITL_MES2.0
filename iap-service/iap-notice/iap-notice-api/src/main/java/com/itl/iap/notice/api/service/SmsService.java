package com.itl.iap.notice.api.service;

import java.util.Map;

public interface SmsService {
    String sendMessage(Map<String,String> msg);
}
