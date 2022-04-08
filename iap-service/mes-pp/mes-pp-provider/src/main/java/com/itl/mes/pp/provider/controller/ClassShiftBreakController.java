package com.itl.mes.pp.provider.controller;


import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.pp.api.dto.scheduleplan.ClassShiftBreakQueryDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ClassShiftBreakEntity;
import com.itl.mes.pp.api.service.ClassShiftBreakService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/p/classShiftBreak")
public class ClassShiftBreakController {

    @Autowired
    ClassShiftBreakService classShiftBreakService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData findList(@RequestBody ClassShiftBreakQueryDTO classShiftBreakQueryDTO) {
        return ResponseData.success(classShiftBreakService.findList(classShiftBreakQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody ClassShiftBreakEntity classShiftBreakEntity) {
        classShiftBreakService.save(classShiftBreakEntity);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        classShiftBreakService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(classShiftBreakService.findById(id));
    }
    

}
