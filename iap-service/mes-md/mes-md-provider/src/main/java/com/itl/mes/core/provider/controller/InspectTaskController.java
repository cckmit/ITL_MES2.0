package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.StrNotNull;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.entity.InspectTask;
import com.itl.mes.core.api.service.InspectTaskService;
import com.itl.mes.core.api.vo.InspectTaskVo;
import com.itl.mes.core.api.vo.InspectTypeVo;
import io.swagger.annotations.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author space
 * @since 2019-08-30
 */
@RestController
@RequestMapping("/monopy/inspectTasks")
@Api(tags = " 检验任务")
public class InspectTaskController {
    private final Logger logger = LoggerFactory.getLogger(InspectTaskController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public InspectTaskService inspectTaskService;

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过主键查询数据")
    public ResponseData<InspectTask> getInspectTaskById(@PathVariable String id) {
        return ResponseData.success(inspectTaskService.getById(id));
    }


    @PostMapping("/inspectTaskPage")
    @ApiOperation(value = "检验任务分页查询")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonInspectTaskRequestModel", properties = {
            @DynamicParameter(name = "inspectType", value = "检验类型"),
            @DynamicParameter(name = "workShop", value = "车间"),
            @DynamicParameter(name = "productLine", value = "产线"),
            @DynamicParameter(name = "item", value = "物料"),
            @DynamicParameter(name = "shopOrder", value = "工单"),
            @DynamicParameter(name = "operation", value = "工序"),
            @DynamicParameter(name = "sn", value = "条码"),
            @DynamicParameter(name = "state", value = "状态"),
            @DynamicParameter(name = "startCreateDate", value = "开始日期"),
            @DynamicParameter(name = "endCreateDate", value = "结束日期"),
            @DynamicParameter(name = "mobile", value = "手机端调用使用"),
            @DynamicParameter(name = "page", value = "当前页,默认1"),
            @DynamicParameter(name = "limit", value = "页面大小，默认20")
    }))
    public ResponseData<IPage<InspectTask>> inspectTaskPage(@RequestBody Map<String, Object> params) {
        IPage<InspectTask> inspectTaskPage = inspectTaskService.selectinspectTaskPage(new QueryPage<>(params), params);
        return ResponseData.success(inspectTaskPage);
    }


    @GetMapping("/select")
    @ApiOperation(value = "根据检验类型查询")
    public ResponseData<InspectTypeVo> getInspectType(String inspectType) throws CommonException {
        StrNotNull.validateNotNull(inspectType, "检验类型不能为空");
        return ResponseData.success(inspectTaskService.getInspectType(inspectType.trim()));
    }


    @PostMapping("/create")
    @ApiOperation(value = "创建")
    public ResponseData<String> createInspectTask(@RequestBody InspectTaskVo inspectTaskVo) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(inspectTaskVo); //验证数据是否合规
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        inspectTaskService.createInspectTask(inspectTaskVo);
        return ResponseData.success("success");
    }


    @PostMapping("/delete")
    @ApiOperation(value = "根据检验任务编号删除")
    public ResponseData<String> deleteInspectTask(@RequestBody List<InspectTaskVo> inspectTaskVos) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(inspectTaskVos); //验证数据是否合规
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        inspectTaskService.deleteInspectTask(inspectTaskVos);
        return ResponseData.success("success");
    }


    @PostMapping("/FqcCreate")
    @ApiOperation(value = "FQC自动创建")
    public ResponseData<String> createInspectTaskByFqc(@RequestBody InspectTaskVo inspectTaskVo) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(inspectTaskVo); //验证数据是否合规
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        inspectTaskService.createInspectTaskByFqc(inspectTaskVo);
        return ResponseData.success("success");
    }


    @PostMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("FileName", file.getOriginalFilename());
        inspectTaskService.importExcel(file);
        return ResponseData.success("success");
    }


    @PostMapping("/close")
    @ApiOperation(value = "根据检验任务编号强制关闭")
    public ResponseData<String> closeInspectTask(@RequestBody List<InspectTaskVo> inspectTaskVos) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(inspectTaskVos); //验证数据是否合规
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        inspectTaskService.closeInspectTask(inspectTaskVos);
        return ResponseData.success("success");
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportInspectTask(String site, HttpServletResponse response, String inspectType
            , String workShop, String productLine, String item, String shopOrder, String operation
            , String sn, String state, String startCreateDate, String endCreateDate) throws CommonException {
        Map<String,Object> params = new HashMap<>();
        params.put("inspectType", inspectType);
        params.put("workShop", workShop);
        params.put("productLine", productLine);
        params.put("item", item);
        params.put("shopOrder", shopOrder);
        params.put("operation", operation);
        params.put("sn", sn);
        params.put("state", state);
        params.put("startCreateDate", startCreateDate);
        params.put("endCreateDate", endCreateDate);
        inspectTaskService.exportInspectTask(site, response,params);
    }

}