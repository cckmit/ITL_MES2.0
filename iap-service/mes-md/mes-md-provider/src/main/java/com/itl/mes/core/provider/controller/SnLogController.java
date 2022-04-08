package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mes.core.api.entity.SnLog;
import com.itl.mes.core.api.service.SnLogService;
import com.itl.mes.core.api.vo.SnLogVo;
import io.swagger.annotations.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author space
 * @since 2019-09-25
 */
@RestController
@RequestMapping("/monopy/snLogs")
@Api(tags = " SN日志表")
public class SnLogController {
    private final Logger logger = LoggerFactory.getLogger(SnLogController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public SnLogService snLogService;

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过主键查询数据")
    public ResponseData<SnLog> getSnLogById(@PathVariable String id) {
        return ResponseData.success(snLogService.getById(id));
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询SN日志")
    @ApiOperationSupport(params = @DynamicParameters(name = "SnLogRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "item", value = "物料，可不填"),
            @DynamicParameter(name = "startNumber", value = "开始号段，可不填"),
            @DynamicParameter(name = "endNumber", value = "结束号段，可不填"),
            @DynamicParameter(name = "startCreateDate", value = "开始日期，可不填"),
            @DynamicParameter(name = "endCreateDate", value = "结束日期，可不填")
    }))
    public ResponseData<IPage<SnLogVo>> snRule(@RequestBody Map<String, Object> params) throws CommonException {
        IPage<SnLogVo> snLogVoPage = snLogService.selectPageByDate(new QueryPage<>(params), params);
        return ResponseData.success(snLogVoPage);
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public ResponseData<String> exportMouldGroup(String site, HttpServletResponse response, String item, String startNumber, String endNumber, String startCreateDate, String endCreateDate ) throws CommonException {
        snLogService.exportMouldGroup(site, response,item,startNumber,endNumber,startCreateDate,endCreateDate);
        return ResponseData.success("success");
    }


    @PostMapping("/delete")
    @ApiOperation(value = "删除SN日志数据")
    public ResponseData<String> deleteSnLog(@RequestBody List<SnLogVo> snLogs) throws CommonException {
        snLogService.deleteSnLog(snLogs);
        return ResponseData.success("success");
    }

}