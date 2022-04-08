package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.MouldDto;
import com.itl.mes.core.api.entity.Mould;
import com.itl.mes.core.api.service.MouldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mould")
@Api(tags = " 模具表" )
public class MouldController {
    @Autowired
    private MouldService mouldService;

    @PostMapping("/save")
    @ApiOperation(value="保存模具数据")
    public ResponseData save(@RequestBody Mould mould ) throws CommonException {
         mouldService.insert(mould);
        return ResponseData.success();
    }

    @GetMapping("/getByBo")
    @ApiOperation(value="根据模具bo查询单条信息")
    public ResponseData<Mould> getById(@RequestParam("bo") String bo ) throws CommonException {
        return ResponseData.success(mouldService.getById(bo));
    }

    @PostMapping("/returnMould")
    @ApiOperation(value="归还模具功能")
    public ResponseData<Mould> returnMould(@RequestBody Mould mould ) throws CommonException {
        return ResponseData.success(mouldService.returnMould(mould));
    }

    @PostMapping("/query")
    @ApiOperation(value="分页查询")
    public ResponseData<IPage<Mould>> query(@RequestBody MouldDto mouldDto ) throws CommonException {

        return ResponseData.success(mouldService.queryPage(mouldDto));
    }

    @DeleteMapping("/batchDelete")
    @ApiOperation("删除")
    public ResponseData batchDelete(@RequestBody List<String> bos) throws CommonException {
        mouldService.batchDelete(bos);
        return  ResponseData.success();
    }


}
