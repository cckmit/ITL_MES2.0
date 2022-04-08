package com.itl.iap.report.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.report.api.dto.DeviceResumeDto;
import com.itl.iap.report.api.service.DeviceResumeService;
import com.itl.iap.report.api.vo.DeviceResumeVo;
import com.itl.iap.report.provider.mapper.DeviceResumeMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/deviceResume")
@Api(tags = "设备履历" )
public class DeviceResumeController {
    @Autowired
    private DeviceResumeService resumeService;

    @PostMapping("/query")
    @ApiOperation("设备履历查询")
    public ResponseData<IPage<DeviceResumeVo>> query(@RequestBody DeviceResumeDto deviceResumeDto) throws CommonException {
        return ResponseData.success(resumeService.selectDeviceInfo(deviceResumeDto));
    }

    @PostMapping("queryByCondition")
    @ApiOperation("设备信息查询")
    public ResponseData<IPage<Map<String,String>>> queryByCondition(@RequestBody DeviceResumeDto deviceResumeDto) throws CommonException {
        return ResponseData.success(resumeService.selectByCondition(deviceResumeDto));
    }
}
