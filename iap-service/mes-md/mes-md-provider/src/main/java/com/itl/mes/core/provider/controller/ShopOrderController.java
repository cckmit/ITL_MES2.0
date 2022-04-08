package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.ShopOrderDTO;
import com.itl.mes.core.api.dto.ShopOrderReportDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.DeviceService;
import com.itl.mes.core.api.service.RouterService;
import com.itl.mes.core.api.service.ShopOrderService;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderReportVo;
import com.itl.mes.core.provider.mapper.OperationMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author space
 * @since 2019-06-17
 */
@RestController
@RequestMapping("/shopOrders")
@Api(tags = "工单维护功能" )
public class ShopOrderController {
    private final Logger logger = LoggerFactory.getLogger(ShopOrderController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private RouterService routerService;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private DeviceService deviceService;

    @Resource
    private UserUtil userUtil;
    /**
    * 根据shopOrder查询
    *
    * @param shopOrder 工单
    * @return RestResult<ShopOrderFullVo>
    */
    @GetMapping("/query")
    @ApiOperation(value="通过工单查询工单相关数据")
    @ApiImplicitParam(name="shopOrder",value="工单",dataType="string", paramType = "query")
    public ResponseData<ShopOrderFullVo> getShopOrder(String shopOrder ) throws CommonException {
        return ResponseData.success(shopOrderService.getShopFullOrder( new ShopOrderHandleBO( UserUtils.getSite(), shopOrder ) ));
    }

    @PutMapping("/update")
    @ApiOperation(value="修改工单")
    @ApiImplicitParam(name="updateShopOrderFullVo",value="修改工单",dataType="string", paramType = "query")
    public ResponseData<ShopOrderFullVo> updateShopOrderFullVo(@RequestBody ShopOrderFullVo shopOrderFullVo) throws CommonException {
        shopOrderService.updateShopOrderFullVo( shopOrderFullVo);
        return ResponseData.success();
    }

    @PutMapping("/updateEmergenc")
    @ApiOperation(value="修改工单紧急状态")
    @ApiImplicitParam(name="updateEmergenc",value="修改工单紧急状态",dataType="string", paramType = "query")
    public ResponseData<ShopOrderFullVo> updateEmergenc(@RequestBody List<Map<String, Object>> shopOrderList) throws CommonException {
        shopOrderService.updateEmergenc(shopOrderList);
        return ResponseData.success();
    }


    @PutMapping("/updateFixedTime")
    @ApiOperation(value="修改工单固定交期")
    @ApiImplicitParam(name="updateFixedTime",value="查询单条",dataType="string", paramType = "query")
    public ResponseData updateFixedTime(@RequestParam("shopOrder") String shopOrder, @RequestParam("fixedTime") String fixedTime) throws CommonException {
        shopOrderService.updateFixedTime(shopOrder, fixedTime);
        return ResponseData.success();
    }

    /**
     * 查询所有工单
     *
     * @return RestResult<ShopOrderFullVo>
     */
    @PostMapping("/queryAllOrder")
    @ApiOperation(value="排程查询工单相关数据")
    @ApiImplicitParam(name="queryAllOrder",value="工单",dataType="string", paramType = "query")
    public ResponseData queryAllOrder(@RequestBody Map<String, Object> params, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) throws CommonException {
        return ResponseData.success(shopOrderService.getAllOrder(params, page, pageSize));
    }

    /**
     * 保存工单数据
     *
     * @param shopOrderFullVo shopOrderFullVo
     * @return RestResult<ShopOrderFullVo>
     */
    @PostMapping("/save")
    @ApiOperation(value="保存工单数据")
    public ResponseData<ShopOrderFullVo> saveShopOrder( @RequestBody ShopOrderFullVo shopOrderFullVo ) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean( shopOrderFullVo ); //简单验证数据是否合规
        if( validResult.hasErrors() ){
            throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        shopOrderService.saveShopOrder( shopOrderFullVo );
        shopOrderFullVo = shopOrderService.getShopFullOrder( new ShopOrderHandleBO( UserUtils.getSite(),shopOrderFullVo.getShopOrder() ) );
        return ResponseData.success(shopOrderFullVo);
    }


    /**
     * 删除工单数据
     *
     * @param shopOrder 工单
     * @param modifyDate 修改时间
     * @return RestResult<String>
     */
    @GetMapping("/delete")
    @ApiOperation(value="删除工单查数据")
    @ApiImplicitParam(name="shopOrder",value="工单",dataType="string", paramType = "query")
    public ResponseData<String> deleteShopOrder( String shopOrder,String modifyDate ) throws CommonException {
        if( StrUtil.isBlank( shopOrder ) ){
            throw new CommonException( "工单不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
//        if( StrUtil.isBlank( modifyDate ) ){
//            throw new CommonException("修改时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
//        }
        shopOrderService.deleteShopOrderByHandleBO( new ShopOrderHandleBO( UserUtils.getSite(),shopOrder ),
                DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS ) );
        return ResponseData.success( "success" );
    }



    /*@PostMapping("/release")
    @ApiOperation(value="工单下达车间作业控制服务")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "shopOrder", value = "工单编号", dataType = "string", paramType = "query" ),
            @ApiImplicitParam( name = "stayNumber", value = "待下达数量", dataType = "int", paramType = "query" ),
            @ApiImplicitParam( name = "planStartDate", value = "计划开始时间", dataType = "string", paramType = "query" ),
            @ApiImplicitParam( name = "planEndDate", value = "计划结束时间", dataType = "string", paramType = "query" )
    })
    public RestResult<List<Sfc>> shopOrderRelease(String shopOrder, BigDecimal stayNumber, String planStartDate, String  planEndDate,String routerBo, @RequestBody List<SfcAndNumVo> sfcAndNumVos)  {
        List<Sfc> sfcs;
        try {
            if( StrUtil.isBlank( shopOrder ) ){
                throw new BusinessException( "工单编号不能为空" );
            }

            if (sfcAndNumVos.size() > 0){
                sfcs = shopOrderService.shopOrderReleaseBySfcAndNumVos( shopOrder,planStartDate,planEndDate,routerBo,sfcAndNumVos);
            }else {
                sfcs = shopOrderService.shopOrderRelease( shopOrder,stayNumber,planStartDate,planEndDate);
            }

        } catch (BusinessException e) {
            logger.error("shopOrderRelease -=- {}", ExceptionUtils.getFullStackTrace( e ) );
            return new RestResult<List<Sfc>>( false, e.getCode(), e.getMessage() );
        }
        return new RestResult<>( sfcs );
    }*/

    /**
     * 查询所有可以下达的工单
     */
    @PostMapping("/queryShopOrderRelease")
    @ApiOperation(value="查询所有可以下达的工单")
    public ResponseData<IPage<ShopOrder>> shopOrderRelease(@RequestBody ShopOrderDTO shopOrderDTO){
        return ResponseData.success(shopOrderService.queryShopOrderRelease(shopOrderDTO));
    }

    /**
     * 修改工单表工艺路线
     * @param router 工艺路线
     * @param version 版本
     * @param bo 工单bo
     */
    @GetMapping("/updateShopOrderRouter")
    @ApiOperation(value="修改工单表工艺路线")
    public ResponseData updateShopOrderRouter(@RequestParam("router") String router,
                                              @RequestParam("version") String version,
                                              @RequestParam("bo") String bo){
        Router routerObj = routerService.getOne(new QueryWrapper<Router>().eq("router", router).eq("version", version));
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setBo(bo);
        shopOrder.setRouterBo(routerObj.getBo());
        shopOrder.setModifyUser(userUtil.getUser().getUserName());
        shopOrder.setModifyDate(new Date());
        return ResponseData.success(shopOrderService.updateById(shopOrder));
    }
    /**
     * 工单下达、批量下达
     */
    @PostMapping("/orderReleaseBatch")
    @ApiOperation(value="工单下达、批量下达")
    public ResponseData orderReleaseBatch(@RequestBody List<ShopOrderDTO> shopOrderDTOs){
        shopOrderService.orderReleaseBatch(shopOrderDTOs);
        return ResponseData.success();
    }

    @ApiOperation(value="工单查询报表")
    @PostMapping("/getShopOrderReport")
    public ResponseData<IPage<ShopOrderReportVo>> getShopOrderReport(@RequestBody ShopOrderReportDTO shopOrderReportDTO){
        return ResponseData.success(shopOrderService.getShopOrderReport(shopOrderReportDTO));
    }
//
//    @ApiOperation(value="根据工单查询在制详情")
//    @GetMapping("/getMakingDetails")
//    public ResponseData<IPage<ShopOrderReportVo>> getMakingDetails(@RequestParam("shopOrderBo") String shopOrderBo){
//
//    }
    @GetMapping("/getUser")
    public ResponseData<String> getUser(){
        return ResponseData.success(userUtil.getUser().getUserName());
    }

    @PostMapping("/getCanCancelOperationOrder")
    @ApiOperation(value = "根据工单查询该工单下所有可以撤回的工序工单")
    public ResponseData<IPage<OperationOrder>> getCanCancelOperationOrder(@RequestBody ShopOrderDTO shopOrderDTO){
        return ResponseData.success(shopOrderService.getCanCancelOperationOrder(shopOrderDTO));
    }

    @ApiOperation(value = "确认撤回工序工单")
    @PostMapping("/okCancelOperationOrder")
    public ResponseData okCancelOperationOrder(@RequestBody List<String> operationOrderList) throws CommonException {
        shopOrderService.okCancelOperationOrder(operationOrderList);
        return ResponseData.success("撤回成功");
    }
}