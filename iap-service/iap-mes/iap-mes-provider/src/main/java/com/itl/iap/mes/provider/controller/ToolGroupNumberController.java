package com.itl.iap.mes.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupNumberQueryDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolNumberEntity;
import com.itl.iap.mes.api.service.ToolGroupNumberService;
import com.itl.iap.mes.api.vo.ToolGroupNumberVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/6 17:41
 */
@RestController
@RequestMapping("/m/toolGroupNumber")
public class ToolGroupNumberController {
    
    @Autowired
    ToolGroupNumberService toolGroupNumberService;


    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData<IPage<ToolGroupNumberVo>> findList(@RequestBody ToolGroupNumberQueryDTO toolGroupNumberQueryDTO) {
        return ResponseData.success(toolGroupNumberService.findList(toolGroupNumberQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody ToolNumberEntity toolNumberEntity) {
        toolGroupNumberService.save(toolNumberEntity);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        toolGroupNumberService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(toolGroupNumberService.findById(id));
    }



}
