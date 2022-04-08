package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.PlmDto;
import com.itl.mes.core.api.entity.WorkStation;
import com.itl.mes.core.api.service.PlmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plm")
@Api(tags = "plm集成" )
public class PlmController {

    @Autowired
    private PlmService plmService;

    @GetMapping("/setErrMsg")
    @ApiOperation("设置鉴权码")
    public ResponseData setErrMsg(@RequestParam("errMsg") String errMsg) {
        plmService.setErrMsg(errMsg);
        return  ResponseData.success();
    }

    @GetMapping("/getErrMsg")
    @ApiOperation("获取鉴权码")
    public ResponseData<String> getErrMsg() {
        return ResponseData.success(plmService.getErrMsg());
    }

    @PostMapping("/getWorkStep")
    @ApiOperation("获取工步信息")
    public ResponseData<String> getWorkStep(@RequestBody PlmDto plmDto) throws CommonException {
        return ResponseData.success(plmService.getWorkStepNo(plmDto));
    }

    @GetMapping("/getAllWorkStep")
    @ApiOperation("获取所有工步")
    public ResponseData<List<WorkStation>> getAllWorkStep(@RequestParam("item") String item,@RequestParam("version") String version) throws CommonException {
        return ResponseData.success( plmService.getAllWorkStation(item,version));
    }
}
