package com.itl.iap.report.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.report.api.dto.AndonDto;
import com.itl.iap.report.api.service.AndonReportService;
import com.itl.iap.report.api.service.ProductionKanbanService;
import com.itl.iap.report.provider.mapper.ProductionKanbanMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productionKanban")
@Api(tags = "生产看板" )
public class ProductionKanbanController {
    @Autowired
    private ProductionKanbanService productionKanbanService;
    @Resource
    private ProductionKanbanMapper productionKanbanMapper;

    @GetMapping("/DeviceState")
    @ApiOperation("查询产线下的设备状态")
    public ResponseData<List<Map<String,Object>>> selectDeviceState(@RequestParam("name")String name){
        return ResponseData.success(productionKanbanService.selectDeviceState(name));
    }

    @GetMapping("/Synthesize")
    @ApiOperation("查询产线下的综合信息")
    public ResponseData<Map<String,Object>> selectSynthesize(@RequestParam("name")String name){
        return ResponseData.success(productionKanbanService.selectSynthesize(name));
    }

    @GetMapping("/AndonObject")
    @ApiOperation("查询产线下的安灯信息-当日")
    public ResponseData<List<Map<String,Object>>> selectAndonObject(@RequestParam("name")String name){
        return ResponseData.success(productionKanbanMapper.selectAndonObject(name));
    }

    @PostMapping ("/PersonQty")
    @ApiOperation("查询产线下的人员产能-当日")
    public ResponseData<IPage<Map<String,Object>>> selectPersonQty(Page page, @RequestParam("name")String name){
        return ResponseData.success(productionKanbanService.selectPersonQty(page,name));
    }
}
