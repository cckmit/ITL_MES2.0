package com.itl.mes.andon.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.AndonQueryDTO;
import com.itl.mes.andon.api.dto.AndonSaveDTO;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.andon.api.service.AndonService;
import com.itl.mes.andon.api.vo.AndonVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/andon")
@Api(tags = "安灯表")
public class AndonController {

    @Autowired
    AndonService andonService;


    @PutMapping("/save")
    @ApiOperation(value = "保存安灯数据")
    public ResponseData save(@RequestBody AndonSaveDTO andonSaveDTO){

        andonService.save(andonSaveDTO);
        return ResponseData.success(true);
    }


    @DeleteMapping("/delete")
    @ApiOperation(value = "删除安灯数据")
    public ResponseData delete(@RequestBody List<String> ids){
        andonService.deleteByIds(ids);
        return ResponseData.success(true);
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询安灯数据列表")
    public ResponseData<IPage<AndonVo>> findList(@RequestBody AndonQueryDTO andonQueryDTO){

        return ResponseData.success(andonService.findList(andonQueryDTO));

    }


    @GetMapping("/findById")
    @ApiOperation(value = "根据ID查询安灯数据")
    public ResponseData<AndonVo> findById(String id){

        return ResponseData.success(andonService.findById(id));

    }

    @GetMapping("/findByLine")
    @ApiOperation(value = "根据产线查询所有的异常")
    public List<Record> findByLine(@RequestParam("line") String line){

        return andonService.findByLine(line);

    }

    @PostMapping("/listForUse")
    @ApiOperation(value = "查询可用的安灯数据列表")
    public ResponseData<IPage<AndonVo>> findListForUse(@RequestBody AndonQueryDTO andonQueryDTO){

        return ResponseData.success(andonService.findListForUse(andonQueryDTO));

    }
}
