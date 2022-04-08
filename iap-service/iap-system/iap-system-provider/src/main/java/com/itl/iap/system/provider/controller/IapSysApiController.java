package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.system.api.dto.IapSysApiTDto;
import com.itl.iap.system.api.entity.IapSysApiT;
import com.itl.iap.system.api.service.IapSysApiTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.List;


/**
 * api控制器
 *
 * @author 马家伦
 * @date 2020-06-19
 * @since jdk1.8
 */
@Api("System-接口管理控制层")
@RestController
@RequestMapping("/iapSysApiT")
public class IapSysApiController {

    @Resource
    private IapSysApiTService iapSysApiService;

    @PostMapping("/delete")
    @ApiOperation(value = "通过微服务名称删除接口",notes = "通过微服务名称删除接口")
    public ResponseData<Integer> delete(@RequestBody IapSysApiTDto iapSysApiDto){
        return ResponseData.success(iapSysApiService.delete(iapSysApiDto));
    }

    @PostMapping("/query")
    @ApiOperation(value = "查询接口信息",notes = "查询接口信息")
    public ResponseData<IPage<IapSysApiTDto>> query(@RequestBody IapSysApiTDto iapSysApiDto) throws UnknownHostException {
        return ResponseData.success(iapSysApiService.queryList(iapSysApiDto));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据传入条件更新接口信息",notes = "根据传入条件更新接口信息")
    public ResponseData<Boolean> update(@RequestBody IapSysApiTDto iapSysApiDto){
        return ResponseData.success(iapSysApiService.updateById(DtoUtils.convertObj(iapSysApiDto, IapSysApiT.class)));
    }

    @PostMapping("/getById")
    @ApiOperation(value = "通过id查询接口详细",notes = "通过id查询接口详细")
    public ResponseData<IapSysApiTDto> getById(@RequestBody IapSysApiTDto iapSysApiDto) {
        return ResponseData.success(iapSysApiService.selectById(iapSysApiDto));
    }

    @PostMapping("/deleteByIds")
    @ApiOperation(value = "通过id列表批量删除接口",notes = "通过id列表批量删除接口")
    public ResponseData<Integer> deleteByIds(@RequestBody List<IapSysApiTDto> iapSysApiDtoList){
        return ResponseData.success(iapSysApiService.deleteByIds(iapSysApiDtoList));
    }

}
