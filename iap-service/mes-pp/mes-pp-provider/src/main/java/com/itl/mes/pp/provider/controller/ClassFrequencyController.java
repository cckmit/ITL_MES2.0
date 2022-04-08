package com.itl.mes.pp.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.pp.api.dto.scheduleplan.ClassFrequencyQueryDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ClassFrequencyEntity;
import com.itl.mes.pp.api.service.ClassFrequencyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/p/classFrequency")
public class ClassFrequencyController {
    
    @Autowired
    ClassFrequencyService classFrequencyService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData findList(@RequestBody ClassFrequencyQueryDTO classFrequencyQueryDTO) {
        return ResponseData.success(classFrequencyService.findList(classFrequencyQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody ClassFrequencyEntity classFrequencyEntity) {
        classFrequencyService.save(classFrequencyEntity);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        classFrequencyService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(classFrequencyService.findById(id));
    }
    
}
