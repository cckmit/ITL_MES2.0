package com.itl.mes.andon.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.RecordSaveDTO;
import com.itl.mes.andon.api.service.AndonTriggerService;
import com.itl.mes.andon.api.vo.RecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @auth liuchenghao
 * @date 2020/12/23
 */
@RestController
@RequestMapping("/trigger")
@Api(tags = "安灯触发")
public class AndonTriggerController {

    @Autowired
    AndonTriggerService andonTriggerService;

    @PostMapping("/andonList")
    @ApiOperation(value = "查询当前用户绑定的安灯数据")
    public ResponseData  findAndonList(){

        return ResponseData.success(andonTriggerService.findAndonList());
    }

    @PostMapping("/andonTrigger")
    @ApiOperation(value = "安灯触发")
    public ResponseData andonTrigger(@RequestBody RecordSaveDTO recordSaveDTO){
        andonTriggerService.saveRecord(recordSaveDTO);
        return ResponseData.success();
    }


    @PostMapping("/uploadPicture")
    @ApiOperation(value = "上传图片")
    public ResponseData<String> uploadPicture(@RequestParam("file") MultipartFile[] files) {

        return ResponseData.success(andonTriggerService.upload(files));
    }

    @GetMapping("/getRecord")
    @ApiOperation(value = "查询安灯异常信息")
    public ResponseData<RecordVo> getRecord(String andonBo){

        return ResponseData.success(andonTriggerService.getRecord(andonBo));

    }
}
