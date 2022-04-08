package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mes.core.api.service.SnService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author space
 * @since 2019-07-25
 */
@RestController
@RequestMapping("/monopy/sns")
@Api(tags = " 条码信息表")
public class SnController {

    private final Logger logger = LoggerFactory.getLogger(SnController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public SnService snService;


    @PostMapping("/selectPageSN")
    @ApiOperation(value = "查询SN弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "SNRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "sn", value = "产品条码，可不填"),
            @DynamicParameter(name = "SN", value = "返回字段：SFC，不需要传入"),
            @DynamicParameter(name = "SITE", value = "返回字段：物料，不需要传入"),
            @DynamicParameter(name = "QTY", value = "返回字段：数量，不需要传入")
    }))
    public ResponseData<IPage<Map>> selectPageSN(@RequestBody Map<String, Object> params) {
        return ResponseData.success(snService.selectPageSN(new QueryPage<>(params), params));
    }


    /*@GetMapping("/query")
    @ApiOperation(value = "查询产品生产数据")
    public RestResult<SnDataVo> saveMouldGroup( String sn) {
        SnDataVo snDataVo = null;
        try {
            StrNotNull.validateNotNull( sn,"产品条码不能为空!" );
            snDataVo = snService.selectSnAndUserPassLog(sn.trim());
        } catch (BusinessException e) {
            logger.error("selectSnAndUserPassLog -=- {}", ExceptionUtils.getFullStackTrace(e));
            return new RestResult<SnDataVo>(false, e.getCode(), e.getMessage());
        }
        return new RestResult<SnDataVo>(snDataVo);
    }*/

}