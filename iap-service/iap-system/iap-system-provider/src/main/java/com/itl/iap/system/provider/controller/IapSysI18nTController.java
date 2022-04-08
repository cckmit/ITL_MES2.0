package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysI18nTDto;
import com.itl.iap.system.api.entity.IapSysI18nT;
import com.itl.iap.system.api.service.IapSysI18nTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 国际化Controller
 *
 * @author 李骐光
 * @date 2020-06-29
 * @since jdk1.8
 */
@Api("System-国际化控制层")
@RestController
@RequestMapping("/iapSysI18nT")
public class IapSysI18nTController {

    @Autowired
    private IapSysI18nTService iapSysI18nService;

    @PostMapping("/queryAll")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ResponseData<IPage<IapSysI18nTDto>> queryAll(@RequestBody IapSysI18nTDto iapSysI18nDto) {
        return ResponseData.success(iapSysI18nService.queryAll(iapSysI18nDto));
    }
    @PostMapping("/queryById")
    @ApiOperation(value = "查询语言列表", notes = "查询语言列表")
    public ResponseData<List<IapSysI18nT>> queryById(@RequestBody IapSysI18nTDto iapSysI18nDto) {
        return ResponseData.success(iapSysI18nService.queryById(iapSysI18nDto));
    }
    @PostMapping("/add")
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseData<Boolean> add(@RequestBody IapSysI18nTDto iapSysI18nDto) {
        return ResponseData.success(iapSysI18nService.add(iapSysI18nDto));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseData<Boolean> update(@RequestBody IapSysI18nTDto iapSysI18nDto) {
        return ResponseData.success(iapSysI18nService.update(iapSysI18nDto));
    }

    @PostMapping("/updateKey")
    @ApiOperation(value = "修改键(i18n_key)",notes = "修改键(i18n_key)")
    public ResponseData<Boolean> updateKey(@RequestBody IapSysI18nTDto iapSysI18nTDto){
        return ResponseData.success(iapSysI18nService.updateKey(iapSysI18nTDto));
    }

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    public ResponseData<Boolean> deleteById(@RequestBody List<IapSysI18nTDto> sysI18nDtoList) {
        return ResponseData.success(iapSysI18nService.deleteBatch(sysI18nDtoList));
    }
}
