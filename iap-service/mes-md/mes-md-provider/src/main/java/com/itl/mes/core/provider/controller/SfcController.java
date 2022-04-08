package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.StrNotNull;
import com.itl.mes.core.api.dto.SfcSplitDto;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.service.SfcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sfc")
@Api(tags = "sfc拆分")
public class SfcController {
    private final Logger logger = LoggerFactory.getLogger(SfcController.class);

    @Autowired
    private SfcService sfcService;

    @GetMapping("/query")
    @ApiOperation(value = "查询sfc信息")
    @ApiImplicitParam(name = "sfc", value = "sfc编号")
    public ResponseData<Sfc> queryBysfc(String sfc) throws CommonException {
        StrNotNull.validateNotNull(sfc,"sfc编号不能为空" );
        return ResponseData.success(sfcService.selectBySfc(sfc));
    }

    @PostMapping("/sfcSplit")
    @ApiOperation(value = "拆分sfc")
    @ApiImplicitParam(name = "Sfc", value = "Sfc实体类")
    public ResponseData<Sfc> sfcSplit(@RequestBody  SfcSplitDto sfcSplitDto) throws CommonException {
        StrNotNull.validateNotNull(sfcSplitDto.getCodeRuleType(),"编码规则不能为空" );

        Sfc sfc = sfcService.getOne(new QueryWrapper<Sfc>().eq("sfc", sfcSplitDto.getSfc()));
//        if(sfcSplitDto.getNcQty()==null || sfcSplitDto.getNcQty().intValue()==0){
//            throw new CommonException("不良数量为0,无需拆分", CommonExceptionDefinition.VERIFY_EXCEPTION);
//        }
        if (sfc.getNcQty().compareTo(BigDecimal.ZERO) == 0){
            throw new CommonException("不良数量为0,无需拆分", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (sfc.getSfcQty().compareTo(sfc.getNcQty()) == 0){
            throw new CommonException("sfc数量与不良数量相同,无需拆分", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
//        if(sfcSplitDto.getSfcQty().compareTo(sfcSplitDto.getNcQty())==0){
//            throw new CommonException("sfc数量与不良数量相同,无需拆分", CommonExceptionDefinition.VERIFY_EXCEPTION);
//        }
        return ResponseData.success(sfcService.sfcSplit(sfcSplitDto));
    }


    @PostMapping("/wipData")
    @ApiOperation(value = "wip报表")
    public ResponseData<List<Map<String, Object>>> wipData(@RequestBody Map<String,Object> params) throws CommonException {
        return ResponseData.success(sfcService.wipData(params));
    }

    @PostMapping("/export")
    @ApiOperation(value = "WIP报表导出")
    public void exportWipData(@RequestBody Map<String,Object> params, HttpServletResponse response) {
        try {
            sfcService.exportWipData(params, response);
        } catch (CommonException | IOException e) {
            logger.error("wipData -=- {}", ExceptionUtils.getFullStackTrace(e));
        }
    }

    @PostMapping("/inSfcSplit")
    @ApiOperation("入库标签拆分")
    public ResponseData<Sfc> inSfcSplit(@RequestBody  SfcSplitDto sfcSplitDto) throws CommonException {
//        // 入库标签拆分数量是否为0
//        if(sfcSplitDto.getInSfcQty()==0){
//            throw new CommonException("标签拆分为0，无需拆分",30002);
//        }
        return ResponseData.success(sfcService.inSfcSplit(sfcSplitDto));
    }

    @GetMapping("/getInSplitInfo")
    @ApiOperation(value = "根据sfc查询入库标签拆分信息")
    public ResponseData<Sfc> getInSplitInfo(@RequestParam("sfc") String sfc) throws CommonException {
        return ResponseData.success(sfcService.getInSplitInfo(sfc));
    }
}
