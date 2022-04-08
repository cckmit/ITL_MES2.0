package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapDictTDto;
import com.itl.iap.system.api.service.IapDictTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据字典主表Controller
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
@Api("System-数据字典主表控制层")
@RestController
@RequestMapping("/iapDictT")
public class IapDictTController {

    @Resource
    private IapDictTService iapDictService;

    @PostMapping("/query")
    @ApiOperation(value = "数据字典分页查询", notes = "数据字典分页查询")
    public ResponseData<IPage<IapDictTDto>> query(@RequestBody IapDictTDto iapDictDto) {
        return ResponseData.success(iapDictService.query(iapDictDto));
    }
    @PostMapping("/queryByState")
    @ApiOperation(value = "数据字典分页查询ByState", notes = "数据字典分页查询ByState")
    public ResponseData<IPage<IapDictTDto>> queryByState(@RequestBody IapDictTDto iapDictDto) {
        return ResponseData.success(iapDictService.queryByState(iapDictDto));
    }

    @PostMapping("/queryDictCodeOrName")
    @ApiOperation(value = "通过字典编号和名称查询", notes = "通过字典编号和名称查询")
    public ResponseData<List<IapDictTDto>> queryDictCodeOrName(@RequestBody IapDictTDto iapDictDto) {
        return ResponseData.success(iapDictService.queryDictCodeOrName(iapDictDto));
    }

    @PostMapping("/insertIapDictT")
    @ApiOperation(value = "新建字典", notes = "新建字典")
    public ResponseData<IapDictTDto> insertIapDictT(@RequestBody IapDictTDto iapDictDto) throws Exception {
        return ResponseData.success(iapDictService.insertIapDictT(iapDictDto));
    }

    @PostMapping("/updateIapDictT")
    @ApiOperation(value = "修改字典", notes = "修改字典")
    public ResponseData<String> updateIapDictT(@RequestBody IapDictTDto iapDictDto) {
        return ResponseData.success(iapDictService.updateIapDictT(iapDictDto));
    }

    @PostMapping("/deleteByIds")
    @ApiOperation(value = "批量删除字典", notes = "批量删除字典")
    public ResponseData<Boolean> deleteById(@RequestBody List<String> ids) {
        return ResponseData.success(iapDictService.deleteByIds(ids));
    }

    @PostMapping("/uploadFile")
    @ApiOperation(value = "批量删除字典", notes = "批量删除字典")
    public ResponseData<Boolean> deleteById(@RequestBody MultipartFile file) {
        file.getName();
        file.getOriginalFilename();
        return ResponseData.success(true);
    }
}
