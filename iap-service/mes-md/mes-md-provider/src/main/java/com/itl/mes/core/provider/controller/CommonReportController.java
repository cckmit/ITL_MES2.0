package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.StrNotNull;
import com.itl.mes.core.api.service.CommonReportService;
import com.itl.mes.core.api.service.LogicService;
import com.itl.mes.core.api.vo.QueryLogicVo;
import io.swagger.annotations.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monopy/commonReport")
@Api(tags = "报表通用入口")
public class CommonReportController {

    private final Logger logger = LoggerFactory.getLogger(CommonReportController.class);

    @Autowired
    private CommonReportService commonReportService;

    @Autowired
    public LogicService logicService;


    /*@GetMapping("/exportSelectAllCallDragData")
    @ApiOperation(value = "拉环看板数据")
    @ApiOperationSupport(params = @DynamicParameters(name = "AllCallDragDataRequestModel", properties = {
            @DynamicParameter(name = "CALL_DATE", value = "返回字段：呼叫时间"),
            @DynamicParameter(name = "CARRIER", value = "返回字段：坯车号"),
            @DynamicParameter(name = "PRODUCT_LINE", value = "返回字段：产线"),
            @DynamicParameter(name = "ITEM", value = "返回字段：物料编码"),
            @DynamicParameter(name = "ITEM_DESC", value = "返回字段：物料描述"),
            @DynamicParameter(name = "SQTY", value = "返回字段：数量"),
            @DynamicParameter(name = "CREATE_USER", value = "返回字段：注修工")
    }))
    public RestResult<List<Map<String,Object>>> selectAllCallDragData( String site,String sectionCodes ){

        return new RestResult<List<Map<String,Object>>>( commonReportService.selectAllCallDragData( site,sectionCodes ) );
    }*/

    @PostMapping("/exportQuery")
    @ApiOperation(value="查询数据")
    public ResponseData< List<Map<String,Object>> > query(@RequestBody QueryLogicVo queryLogicVo ) throws CommonException {

        if( queryLogicVo==null ){
            throw new CommonException( "查询参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        StrNotNull.validateNotNull( queryLogicVo.getLogicNo(), "逻辑编号不能为空" );
        String sql = "";
        if( StrUtil.isBlank( queryLogicVo.getVersion() ) ){
            sql = logicService.getCurrentLogicNoContent( queryLogicVo.getLogicNo() );
        }else{
            sql = logicService.getLogicNoAndVersionContent( queryLogicVo.getLogicNo(), queryLogicVo.getVersion() );
        }
        Map<String,Object> paramsMap = new HashMap<>();
        if( queryLogicVo.getParams()!=null ){
            paramsMap.putAll( queryLogicVo.getParams() );
        }
        return ResponseData.success( commonReportService.selectManualSql( sql, paramsMap ) );
    }

}
