package com.itl.mes.pp.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.pp.api.dto.scheduleplan.DateTypeQueryDTO;
import com.itl.mes.pp.api.dto.scheduleplan.DateTypeSaveDTO;
import com.itl.mes.pp.api.dto.scheduleplan.DateTypeUpdateDTO;
import com.itl.mes.pp.api.service.DateTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @User liuchenghao
 * @Date 2020/11/25 10:06
 */
@Api("日期类型维护控制层")
@RestController
@RequestMapping("/p/dateType")
public class DateTypeController {

    @Autowired
    DateTypeService dateTypeService;

    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody DateTypeSaveDTO dateTypeSaveDTO) {
        dateTypeService.batchSave(dateTypeSaveDTO);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "query", notes = "查询日期集合，根据年，月查询", httpMethod = "POST")
    @PostMapping(value = "/query")
    public ResponseData query(@RequestBody DateTypeQueryDTO dateTypeQueryDTO){
        return  ResponseData.success(dateTypeService.findList(dateTypeQueryDTO));
    }


    @ApiOperation(value = "update", notes = "修改", httpMethod = "PUT")
    @PutMapping(value = "/update")
    public ResponseData update(@RequestBody List<DateTypeUpdateDTO> dateTypeUpdateDTOs) {
        dateTypeService.update(dateTypeUpdateDTOs);
        return ResponseData.success(true);
    }

}
