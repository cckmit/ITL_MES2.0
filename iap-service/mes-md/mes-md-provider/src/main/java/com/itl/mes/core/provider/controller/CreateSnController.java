package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.StrNotNull;
import com.itl.mes.core.api.dto.CreateSfcDto;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.entity.OperationOrder;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.entity.SnRule;
import com.itl.mes.core.api.service.CreateSnService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.vo.OpeartionItemVo;
import com.itl.mes.core.api.vo.SfcVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sky,
 * @date 2019/8/5
 * @time 15:34
 */
@RestController
@RequestMapping("/monopy/createSn")
@Api(tags = "条码生成")
public class CreateSnController {
    private final Logger logger = LoggerFactory.getLogger(CreateSnController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CreateSnService createSnService;

    @Autowired
    public ItemService itemService;

    @Autowired
    public OperationService operationService;

    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询工单")
    @ApiOperationSupport(params = @DynamicParameters(name = "WorkShopRequestModel", properties = {
            @DynamicParameter( name="page", value = "页面，默认为1" ),
            @DynamicParameter( name="limit", value = "分页大小，默认20，可不填" ),
            @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
            @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
            @DynamicParameter( name="shopOrder", value = "工单，可不填" ),
            @DynamicParameter( name="item", value = "物料，可不填" ),
            @DynamicParameter( name="itemDesc", value = "物料描述，可不填" ),
            @DynamicParameter( name="SHOP_ORDER", value = "返回字段：工单号，不需要传入" ),
            @DynamicParameter( name="ORDER_DESC", value = "返回字段：工单描述，不需要传入" ),
            @DynamicParameter( name="STATE", value = "返回字段：工单状态，不需要传入" ),
            @DynamicParameter( name="ORDER_QTY", value = "返回字段：订单数量，不需要传入" ),
            @DynamicParameter( name="ITEM", value = "返回字段：物料，不需要传入" ),
            @DynamicParameter( name="ITEM_DESC", value = "返回字段：物料描述，不需要传入" ),
            @DynamicParameter( name="PLAN_END_DATE", value = "返回字段：计划完成时间，不需要传入" )
    }))
    public ResponseData<IPage<Map<String,Object>>> snRule(@RequestBody Map<String, Object> params) {

        return ResponseData.success( createSnService.selectPageByShape(new QueryPage<>(params), params ) );

    }


    @GetMapping("/querySnRuleByT")
    @ApiOperation(value = "查询物料下拉框")
    public ResponseData<List<SnRule>> querySnRuleByT() throws CommonException {
        return ResponseData.success(createSnService.querySnRuleByT());
    }


    @GetMapping("/querySnRuleByS")
    @ApiOperation(value = "查询工厂下拉框")
    public ResponseData<List<SnRule>> querySnRuleByS() throws CommonException {
        return ResponseData.success(createSnService.querySnRuleByS());
    }


//    @GetMapping("/serialCode")
//    @ApiOperation(value = "流水号和条码样式")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "number", value = "计划数量", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "whether", value = "是否补码", dataType = "Boolean", paramType = "query"),
//            @ApiImplicitParam(name = "workTypeName", value = "工厂类型名称", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "itemTypeName", value = "物料类型名称", dataType = "String", paramType = "query")
//    })
//    public ResponseData<Map<String,String>> serialCode(String number, Boolean whether, String itemTypeName,String workTypeName) throws CommonException {
//        Map<String,String> map;
//        StrNotNull.validateNotNull(number,"计划数量不能为空" );
//        StrNotNull.validateNotNull(itemTypeName,"物料类型名称不能为空" );
//        StrNotNull.validateNotNull(workTypeName,"工厂类型名称不能为空" );
//        map = createSnService.serialCode(number, whether,itemTypeName,workTypeName);
//        return ResponseData.success(map);
//    }
    @PostMapping("/createSn")
    @ApiOperation(value = "生成条码")
    public ResponseData<String> createSn(@RequestBody CreateSfcDto createSfcDto) throws CommonException {
        StrNotNull.validateNotNull(createSfcDto.getNumber(),"标签数量不能为空" );
        StrNotNull.validateNotNull(createSfcDto.getCodeRuleType(),"编码规则类型不能为空" );
        createSnService.createSnByRule(createSfcDto.getNumber(),createSfcDto.getCodeRuleType(),createSfcDto.getSfcQty(),createSfcDto.getOperationOrder(),createSfcDto.getDispatchCode(),0);
        return ResponseData.success("success");
    }

    @GetMapping("/queryType")
    @ApiOperation(value = "根据工单查询类型")
    @ApiImplicitParam(name = "shopOrder", value = "工单", dataType = "String", paramType = "query")
    public ResponseData<String> selectTypeByShopOrder(String shopOrder) throws CommonException {
        if (StrUtil.isBlank(shopOrder)) {
            throw new CommonException("计划数量不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return ResponseData.success(createSnService.selectTypeByShopOrder(shopOrder));
    }


    @PostMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("FileName", file.getOriginalFilename());
        //POIUtils.checkFile(file);
        createSnService.importExcel(file);
        return ResponseData.success("success");
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public ResponseData<String> exportExcel(String site, HttpServletResponse response) throws CommonException {
        createSnService.exportExcel(site, response);
        return ResponseData.success("success");
    }

    /**
     *
     * @param sfcDto
     * @return
     * @throws CommonException
     */
    @PostMapping("/querySfc")
    @ApiOperation(value = "根据工序工单号查询条码信息")
    public ResponseData<IPage<Sfc>> selectSfcByoperationOrder(@RequestBody SfcDto sfcDto) throws CommonException {
        if (StrUtil.isBlank(sfcDto.getOperationOrder())) {
            throw new CommonException("工序工单号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return ResponseData.success(createSnService.selectSfcByOperationOrder(sfcDto));
    }

    /**
     *
     * @param operationOrder
     * @return
     * @throws CommonException
     */
    @GetMapping("/queryInfo")
    @ApiOperation(value = "根据工序工单号查询物料信息")
    @ApiImplicitParam(name = "operationOrder", value = "工序工单", dataType = "String", paramType = "query")
    public ResponseData<OpeartionItemVo> selectOpeartionItemInfo(String operationOrder) throws CommonException {
        return ResponseData.success(itemService.selectOpeartionOrderItem(operationOrder));
    }

    /**
     *
     * @param sfcDto
     * @return
     * @throws CommonException
     */
    @PostMapping("/getShopOperation")
    @ApiOperation(value = "获取所有工序工单号下拉")
    public ResponseData<IPage<OperationOrder>> getAllShopOperation(@RequestBody  SfcDto sfcDto) throws CommonException{
        return ResponseData.success(operationService.selectAllOrderOperation(sfcDto));
    }

}
