package com.itl.iap.mes.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupAttachQueryDTO;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupAttachResDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolGroupAttachEntity;
import com.itl.iap.mes.api.service.ToolGroupAttachService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/9 10:13
 */
@RestController
@RequestMapping("/m/toolGroupAttach")
public class ToolGroupAttachController {


    @Autowired
    ToolGroupAttachService toolGroupAttachService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData<IPage<ToolGroupAttachResDTO>> findList(@RequestBody ToolGroupAttachQueryDTO toolGroupAttachQueryDTO) {
        return ResponseData.success(toolGroupAttachService.findList(toolGroupAttachQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody ToolGroupAttachEntity toolGroupAttachEntity) {
        toolGroupAttachService.save(toolGroupAttachEntity);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        toolGroupAttachService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData<ToolGroupAttachResDTO> getById(@PathVariable String id) {
        return ResponseData.success(toolGroupAttachService.findById(id));
    }





}
