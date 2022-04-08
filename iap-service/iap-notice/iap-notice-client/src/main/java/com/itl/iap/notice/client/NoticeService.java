package com.itl.iap.notice.client;

import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @auth liuchenghao
 * @date 2021/2/24
 */
@FeignClient(value = "iap-notice-provider")
public interface NoticeService {

    @GetMapping("/notice/sendMessage")
    @ApiOperation(value = "发送消息")
    ResponseData sendMessage(@RequestBody Map<String, Object> notice);
}
