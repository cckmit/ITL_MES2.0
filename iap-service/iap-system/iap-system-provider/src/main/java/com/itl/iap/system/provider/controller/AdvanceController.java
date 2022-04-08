package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.system.api.dto.AdvancedQueryDto;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.service.AdvanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 崔翀赫
 * @date 2021/3/5$
 * @since JDK1.8
 */
@Api(tags = "高级查询")
@RestController
@RequestMapping("/advance")
public class AdvanceController {


    @Autowired
    private AdvanceService advanceService;


    @PostMapping("/queryAdvance")
    @ApiOperation(value = "高级查询", notes = "高级查询")
    public ResponseData<IPage<Map>> queryAdvance(@RequestBody AdvancedQueryDto advancedQueryDtos) throws CommonException {
        return ResponseData.success(advanceService.queryAdvance(advancedQueryDtos));
    }

    @GetMapping("/getColumn")
    @ApiOperation(value = "getColumn", notes = "getColumn")
    public ResponseData<List<Map<String,String>>> getColumn(@RequestParam String pageId){
        return ResponseData.success(advanceService.getColumn(pageId));
    }


}
