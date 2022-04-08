package com.itl.mes.andon.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.AndonLookPlateQueryDTO;
import com.itl.mes.andon.api.dto.RecordUpdateDTO;
import com.itl.mes.andon.api.service.AndonLookPlateService;
import com.itl.mes.andon.api.vo.AndonLookPlateVo;
import com.itl.mes.andon.api.vo.RecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/27
 */
@RestController
@RequestMapping("/lookPlate")
@Api(tags = "安灯看板")
public class AndonLookPlateController {

    @Autowired
    AndonLookPlateService andonLookPlateService;



    @PostMapping("/list")
    @ApiOperation(value = "查询安灯看板数据列表")
    public ResponseData<List<AndonLookPlateVo>> findList(@RequestBody AndonLookPlateQueryDTO andonLookPlateQueryDTO){

        return ResponseData.success(andonLookPlateService.getAndonLookPlate(andonLookPlateQueryDTO));

    }


    @GetMapping("/{pid}")
    @ApiOperation(value = "查询安灯看板异常数据信息")
    public ResponseData<RecordVo> findById(@PathVariable String pid){

        return ResponseData.success(andonLookPlateService.getRecordById(pid));

    }

    @PutMapping("/andonRepair")
    @ApiOperation(value = "安灯修复")
    public ResponseData andonRepair(@RequestBody RecordUpdateDTO recordUpdateDTO){
        andonLookPlateService.updateRecord(recordUpdateDTO);
        return ResponseData.success();
    }

}
