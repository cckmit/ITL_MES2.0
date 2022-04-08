package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.MeSfcPackDto;
import com.itl.mes.me.api.service.MeSfcPackingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yaoxiang
 * @date 2021/1/28
 * @since JDK1.8
 */
@RestController
@Api(tags = "包装")
public class MeSfcPackingController {

    @Autowired
    private MeSfcPackingService meSfcPackingService;

    @PostMapping("/pack")
    @ApiOperation("包装")
    public ResponseData pack(@RequestBody MeSfcPackDto meSfcPackDto) throws CommonException {
        meSfcPackingService.pack(meSfcPackDto);
        return ResponseData.success();
    }

    @GetMapping("/generateNo/{item}/{itemVersion}")
    @ApiOperation("生成箱号")
    public ResponseData generateNo(@PathVariable("item") String item, @PathVariable("itemVersion") String itemVersion) {
        return ResponseData.success(meSfcPackingService.generateNo(item, itemVersion));
    }
}
