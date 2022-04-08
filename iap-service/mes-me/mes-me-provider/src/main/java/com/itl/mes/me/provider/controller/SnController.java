package com.itl.mes.me.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.me.api.dto.SnDto;
import com.itl.mes.me.api.entity.Sn;
import com.itl.mes.me.api.service.SnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;



/**
 * 条码信息表
 *
 * @author cuichonghe
 * @date 2020-12-25 12:36:32
 */
@RestController
@RequestMapping("me/sn")
@Api(tags = "条码信息表")
public class SnController {
    @Autowired
    private SnService snService;


    /**
     * 分页查询信息
     *
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询")
    public ResponseData<IPage<Sn>> page(@RequestBody SnDto snDto) {
        return ResponseData.success(snService.getAll(snDto));
    }

    /**
     * 信息
     */
    @GetMapping("/info/{bo}")
    @ApiOperation(value = "查询一条")
    public ResponseData<Sn> info(@PathVariable("bo") String bo){
		Sn sn = snService.getById(bo);
        sn.setItemBo(new ItemHandleBO(sn.getItemBo()).getItem());
        return ResponseData.success(sn);
    }

    /**
     * 保存/修改
     */
    @PostMapping("/saveOrUpdate")
    public ResponseData saveOrUpdate(@RequestBody Sn sn){
        snService.saveSn(sn);

        return ResponseData.success();
    }



    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public ResponseData delete(@RequestBody String[] bos){
        snService.removeByIds(Arrays.asList(bos));

        return ResponseData.success();
    }

}
