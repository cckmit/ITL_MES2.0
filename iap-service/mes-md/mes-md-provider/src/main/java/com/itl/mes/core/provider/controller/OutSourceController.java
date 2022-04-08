package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.OutSourceDto;
import com.itl.mes.core.api.entity.OutSource;
import com.itl.mes.core.api.service.OutSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/outSource")
@Api(tags = "委外单" )
public class OutSourceController {
    @Autowired
    private OutSourceService outSourceService;

    @PostMapping("/save")
    @ApiOperation("保存和更新")
    public ResponseData save(@RequestBody OutSource outSource){
        outSourceService.saveOrUpdateOutSource(outSource);
        return ResponseData.success();
    }

    @PostMapping("/query")
    @ApiOperation("分页查询")
    public ResponseData<IPage<OutSource>> query(@RequestBody OutSourceDto outSourceDto){
        return ResponseData.success(outSourceService.queryPages(outSourceDto));
    }

    @GetMapping("/queryByBo")
    @ApiOperation("查询单条数据")
    public ResponseData<OutSource> queryByBo(@RequestParam("bo") String bo){
        return ResponseData.success(outSourceService.queryByBo(bo));
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除")
    public ResponseData delete(@RequestParam("bo") String bo){
        outSourceService.deleteByBo(bo);
        return ResponseData.success();
    }

    @GetMapping("/createCode")
    @ApiOperation("创建委外单号")
    public ResponseData<String> createCode(){
        return ResponseData.success(outSourceService.createCode());
    }

    @GetMapping("/selectByExceptionCode")
    @ApiOperation("根据异常单编号查询数据")
    public ResponseData<List<String>> selectByExceptionCode(@RequestParam("exceptionCode") String exceptionCode) throws CommonException {
        return ResponseData.success(outSourceService.selectExeceptionCode(exceptionCode));
    }

    @GetMapping("/selectDeviceInfoByCode")
    @ApiOperation("/根据异常单编码查询设备信息")
    public ResponseData<Map<String, String>> selectDeviceInfoByCode(@RequestParam("exceptionCode") String exceptionCode) throws CommonException {
        return ResponseData.success(outSourceService.selectInfoByCode(exceptionCode));
    }
    @GetMapping("/selectInfoByDevice")
    @ApiOperation("/根据设备编码查询设备信息")
    public ResponseData<Map<String, String>> selectInfoByCode(@RequestParam("device") String device) throws CommonException {
        return ResponseData.success(outSourceService.selectInfoByDevice(device));
    }
}
