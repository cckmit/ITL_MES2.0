package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.label.LabelTypeQueryDTO;

import com.itl.iap.mes.api.entity.label.LabelTypeEntity;
import com.itl.iap.mes.provider.service.impl.LabelTypeServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/4 11:11
 */
@RestController
@RequestMapping("/sys/labelType")
public class LabelTypeController {


    @Autowired
    LabelTypeServiceImpl labelTypeService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData findList(@RequestBody LabelTypeQueryDTO labelTypeQueryDTO) {
        return ResponseData.success(labelTypeService.findList(labelTypeQueryDTO));
    }

    @PostMapping("/queryByState")
    @ApiOperation(value = "分页查询信息ByState", notes = "分页查询信息")
    public ResponseData findListByState(@RequestBody LabelTypeQueryDTO labelTypeQueryDTO) {
        return ResponseData.success(labelTypeService.findListByState(labelTypeQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody LabelTypeEntity labelTypeEntity) throws CommonException {
        labelTypeService.save(labelTypeEntity);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        labelTypeService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(labelTypeService.findById(id));
    }


}
