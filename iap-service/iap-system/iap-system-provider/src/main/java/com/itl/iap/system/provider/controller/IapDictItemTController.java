package com.itl.iap.system.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import com.itl.iap.system.api.dto.IapDictItemTDto;
import com.itl.iap.system.api.dto.IapDictTDto;
import com.itl.iap.system.api.entity.IapDictItemT;
import com.itl.iap.system.api.service.IapDictItemTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 字典表详情表Controller
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
@Api("System-数据字典详情表控制层")
@RestController
@RequestMapping("/iapDictItemT")
public class IapDictItemTController {
    @Autowired
    private IapDictItemTService iapDictItemService;

    @PostMapping("/queryDictCodeOrName")
    @ApiOperation(value = "通过字典编号和名称查询", notes = "通过字典编号和名称查询")
    public ResponseData<List<IapDictItemTDto>> queryDictCodeOrName(@RequestBody IapDictItemTDto iapDictItemDto) {
        return ResponseData.success(iapDictItemService.queryDictCodeOrName(iapDictItemDto));
    }

    @GetMapping("/queryDictCode")
    @ApiOperation(value = "通过字典编号和名称查询", notes = "通过字典编号和名称查询")
    public ResponseData<List<IapDictItemTDto>> queryDictCode(@RequestParam("dictCode") String dictCode) {
        return ResponseData.success(iapDictItemService.queryDictCode(dictCode));
    }

    @PostMapping("/insertIapDictItemT")
    @ApiOperation(value = "新增字典", notes = "新增字典")
    public ResponseData<Integer> insertIapDictItemT(@RequestBody IapDictItemTDto iapDictItemDto) {
        return ResponseData.success(iapDictItemService.insertIapDictItemT(iapDictItemDto));
    }

    @PostMapping("/updateIapDictItemT")
    @ApiOperation(value = "修改字典", notes = "修改字典")
    public ResponseData<Integer> updateIapDictItemT(@RequestBody  IapDictItemTDto iapDictItemDto) {
        return ResponseData.success(iapDictItemService.updateIapDictItemT(iapDictItemDto));
    }

    @PostMapping("/deleteByIds")
    @ApiOperation(value = "通过id批量删除数据", notes = "通过id批量删除数据")
    public ResponseData<Boolean> deleteByIds(@RequestBody List<String> ids) {
        return ResponseData.success(iapDictItemService.deleteByIds(ids));
    }

    @GetMapping("/getCode/{code}")
    @ApiOperation(value = "根据数据字典编码获取取值", notes = "根据数据字典编码获取取值")
    public ResponseData<List<IapDictItemT>> getCode(@PathVariable("code") String code) {
        return ResponseData.success(iapDictItemService.getCode(code));
    }
}
