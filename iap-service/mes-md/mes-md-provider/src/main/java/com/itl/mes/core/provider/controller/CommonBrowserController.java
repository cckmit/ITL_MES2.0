package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.CommonBrowserService;
import com.itl.mes.core.api.service.OperationService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/commonBrowsers")
@Api(tags = "通用弹出框类")
public class CommonBrowserController {

    private final Logger logger = LoggerFactory.getLogger(CommonBrowserController.class);

    @Autowired
    private CommonBrowserService commonBrowserService;
    @Autowired
    private OperationService operationService;

    /**
     * 查询工厂数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageSite")
    @ApiOperation(value = "查询工厂数据弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonSiteRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "site", value = "工厂，可不填"),
            @DynamicParameter(name = "siteDesc", value = "描述，可不填"),
            @DynamicParameter(name = "siteType", value = "类型，可不填"),
            @DynamicParameter(name = "enableFlag", value = "是否启用，可不填"),
            @DynamicParameter(name = "SITE", value = "返回字段：工厂，不需要传入"),
            @DynamicParameter(name = "SITE_DESC", value = "返回字段：描述，不需要传入"),
            @DynamicParameter(name = "SITE_TYPE", value = "返回字段：类型，不需要传入"),
            @DynamicParameter(name = "ENABLE_FLAG", value = "返回字段：是否启用，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageSite(@RequestBody Map<String, Object> params) {
        params.put("isAsc", false);
        params.put("orderByField", Site.CREATE_DATE);
        return ResponseData.success(commonBrowserService.selectPageSite(new QueryPage<>(params), params));


    }

    /**
     * 根据状态查询工厂数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageSiteByState")
    @ApiOperation(value = "根据状态查询工厂数据弹出框ByState")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonSiteRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "site", value = "工厂，可不填"),
            @DynamicParameter(name = "siteDesc", value = "描述，可不填"),
            @DynamicParameter(name = "siteType", value = "类型，可不填"),
            @DynamicParameter(name = "enableFlag", value = "是否启用，可不填"),
            @DynamicParameter(name = "SITE", value = "返回字段：工厂，不需要传入"),
            @DynamicParameter(name = "SITE_DESC", value = "返回字段：描述，不需要传入"),
            @DynamicParameter(name = "SITE_TYPE", value = "返回字段：类型，不需要传入"),
            @DynamicParameter(name = "ENABLE_FLAG", value = "返回字段：是否启用，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageSiteByState(@RequestBody Map<String, Object> params) {
        params.put("isAsc", false);
        params.put("orderByField", Site.CREATE_DATE);
        return ResponseData.success(commonBrowserService.selectPageSiteByState(new QueryPage<>(params), params));
    }

    /**
     * 查询车间数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageWorkShop")
    @ApiOperation(value = "查询车间数据弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonWorkShopRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "workShop", value = "车间，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不填"),
            @DynamicParameter(name = "workShopDesc", value = "描述，可不填"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "WORK_SHOP", value = "返回字段：车间，不需要传入"),
            @DynamicParameter(name = "WORK_SHOP_DESC", value = "返回字段，车间描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageWorkShop(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageWorkShop(new QueryPage<>(params), params));

    } /**
     * 查询车间数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageWorkShopByState")
    @ApiOperation(value = "查询车间数据弹出框ByState")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonWorkShopRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "workShop", value = "车间，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不填"),
            @DynamicParameter(name = "workShopDesc", value = "描述，可不填"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "WORK_SHOP", value = "返回字段：车间，不需要传入"),
            @DynamicParameter(name = "WORK_SHOP_DESC", value = "返回字段，车间描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageWorkShopByState(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageWorkShopByState(new QueryPage<>(params), params));

    }


    /**
     * 查询产线数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageProductLine")
    @ApiOperation(value = "查询产线数据弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonProductLineRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "productLine", value = "产线，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不填"),
            @DynamicParameter(name = "productLineDesc", value = "描述，可不填"),
            @DynamicParameter(name = "workShop", value = "车间，可不填"),
            @DynamicParameter(name = "PRODUCT_LINE", value = "返回字段：产线，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "PRODUCT_LINE_DESC", value = "返回字段，描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageProductLine(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageProductLine(new QueryPage<>(params), params));

    } /**
     * 查询产线数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageProductLineByState")
    @ApiOperation(value = "查询产线数据弹出框ByState")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonProductLineRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "productLine", value = "产线，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不填"),
            @DynamicParameter(name = "productLineDesc", value = "描述，可不填"),
            @DynamicParameter(name = "workShop", value = "车间，可不填"),
            @DynamicParameter(name = "PRODUCT_LINE", value = "返回字段：产线，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "PRODUCT_LINE_DESC", value = "返回字段，描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageProductLineByState(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageProductLineByState(new QueryPage<>(params), params));

    }


    /**
     * 查询物料组数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageItemGroup")
    @ApiOperation(value = "查询物料组数据弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonItemGroupRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "itemGroup", value = "物料组编号，可不填"),
            @DynamicParameter(name = "groupName", value = "物料组名称，可不填"),
            @DynamicParameter(name = "groupDesc", value = "物料组描述，可不填"),
            @DynamicParameter(name = "ITEM_GROUP", value = "返回字段：物料组编号，不需要传入"),
            @DynamicParameter(name = "GROUP_NAME", value = "返回字段：物料组名称，不需要传入"),
            @DynamicParameter(name = "GROUP_DESC", value = "返回字段：物料组描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageItemGroup(@RequestBody Map<String, Object> params) {
        return ResponseData.success(commonBrowserService.selectPageItemGroup(new QueryPage<>(params), params));

    }

    /**
     * 查询物料清单数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageBom")
    @ApiOperation(value = "查询物料清单数据弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonBomRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "bom", value = "物料清单"),
            @DynamicParameter(name = "bomDesc", value = "物料清单描述"),
            @DynamicParameter(name = "version", value = "版本"),
            @DynamicParameter(name = "state", value = "版本"),
            @DynamicParameter(name = "BOM", value = "返回字段：物料清单，不需要传入"),
            @DynamicParameter(name = "BOM_DESC", value = "返回字段：物料清单描述，不需要传入"),
            @DynamicParameter(name = "VERSION", value = "返回字段：版本，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageBom(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageBom(new QueryPage<>(params), params));

    }

    /**
     * 模糊查询供应商弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageVendor")
    @ApiOperation(value = "模糊查询供应商弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonVendorRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "vend", value = "供应商"),
            @DynamicParameter(name = "vendName", value = "供应商名称"),
            @DynamicParameter(name = "SITE", value = "返回字段：工厂，不需要传入"),
            @DynamicParameter(name = "VEND", value = "返回字段：供应商，不需要传入"),
            @DynamicParameter(name = "VEND_NAME", value = "返回字段：供应商名称，不需要传入"),
            @DynamicParameter(name = "VEND_DESC", value = "返回字段：供应商描述，不需要传入"),
            @DynamicParameter(name = "CONTACT", value = "返回字段：联系人，不需要传入"),
            @DynamicParameter(name = "TEL", value = "返回字段：联系电话，不需要传入"),
            @DynamicParameter(name = "ADDRESS", value = "返回字段：地址，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageVendor(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageVendor(new QueryPage<>(params), params));
    }

    @PostMapping("/selectPageStation")
    @ApiOperation(value = "模糊查询工位弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonStationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "station", value = "工位"),
            @DynamicParameter(name = "stationDesc", value = "工位描述"),
            @DynamicParameter(name = "STATION", value = "返回字段：工位，不需要传入"),
            @DynamicParameter(name = "STATION_NAME", value = "返回字段：工位名称，不需要传入"),
            @DynamicParameter(name = "STATION_DESC", value = "返回字段：工位描述，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageStation(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageStation(new QueryPage<>(params), params));

    }  @PostMapping("/selectPageStationByState")
    @ApiOperation(value = "模糊查询工位弹出框ByState")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonStationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "station", value = "工位"),
            @DynamicParameter(name = "stationDesc", value = "工位描述"),
            @DynamicParameter(name = "STATION", value = "返回字段：工位，不需要传入"),
            @DynamicParameter(name = "STATION_NAME", value = "返回字段：工位名称，不需要传入"),
            @DynamicParameter(name = "STATION_DESC", value = "返回字段：工位描述，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageStationByState(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageStationByState(new QueryPage<>(params), params));

    }

    @PostMapping("/selectPageOperation")
    @ApiOperation(value = "模糊查询工序弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonStationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "operation", value = "工序"),
            @DynamicParameter(name = "isCurrentVersion", value = "是否当前版本Y/N可不填"),
            @DynamicParameter(name = "operationName", value = "工序名称"),
            @DynamicParameter(name = "operationDesc", value = "工序描述"),
            @DynamicParameter(name = "productLine", value = "所属产线"),
            @DynamicParameter(name = "operationType", value = "工序类型"),
            @DynamicParameter(name = "state", value = "状态"),
            @DynamicParameter(name = "OPERATION", value = "返回字段：工序，不需要传入"),
            @DynamicParameter(name = "OPERATION_NAME", value = "返回字段：工序名称，不需要传入"),
            @DynamicParameter(name = "VERSION", value = "返回字段：版本，不需要传入"),
            @DynamicParameter(name = "OPERATION_TYPE", value = "返回字段：工序类型，不需要传入"),
            @DynamicParameter(name = "OPERATION_DESC", value = "返回字段：工序描述，不需要传入"),
            @DynamicParameter(name = "PRODUCT_LINE", value = "返回字段：所属产线，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageOperation(@RequestBody Map<String, Object> params) {
        return ResponseData.success(commonBrowserService.selectPageOperation(new QueryPage<>(params), params));
    }

    @PostMapping("/selectPageNcCode")
    @ApiOperation(value = "模糊查询不合格代码弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonStationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "ncGroup", value = "不合格代码组，true/false，可不填"),
            @DynamicParameter(name = "ncCode", value = "不合格代码"),
            @DynamicParameter(name = "ncDesc", value = "不合格代码描述"),
            @DynamicParameter(name = "ncName", value = "不合格代码名称"),
            @DynamicParameter(name = "NC_CODE", value = "返回字段：不合格代码，不需要传入"),
            @DynamicParameter(name = "NC_DESC", value = "返回字段：不合格代码描述，不需要传入"),
            @DynamicParameter(name = "NC_NAME", value = "返回字段：不合格代码名称，不需传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageNcCode(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageNcCode(new QueryPage<>(params), params));

    }
    @PostMapping("/selectPageNcCodeByState")
    @ApiOperation(value = "模糊查询不合格代码弹出框ByState")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonStationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "ncGroup", value = "不合格代码组，true/false，可不填"),
            @DynamicParameter(name = "ncCode", value = "不合格代码"),
            @DynamicParameter(name = "ncDesc", value = "不合格代码描述"),
            @DynamicParameter(name = "NC_CODE", value = "返回字段：不合格代码，不需要传入"),
            @DynamicParameter(name = "NC_DESC", value = "返回字段：不合格代码描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageNcCodeByState(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageNcCodeByState(new QueryPage<>(params), params));

    }
    @GetMapping("/selectPageNcCodeByStateByNcGroup")
    @ApiOperation(value = "查询不合格代码弹出框ByStateByNcGroup")
    public ResponseData<List<Map<String, Object>>> selectPageNcCodeByStateByNcGroup(@RequestParam String ncGroup) {

        return ResponseData.success(commonBrowserService.selectPageNcCodeByStateByNcGroup(ncGroup));

    }
    @PostMapping("/selectPageNcGroup")
    @ApiOperation(value = "模糊查询不合格代码组弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonStationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "ncGroup", value = "工序"),
            @DynamicParameter(name = "ncGroupDesc", value = "工序描述"),
            @DynamicParameter(name = "NC_GROUP", value = "返回字段：不合格代码组，不需要传入"),
            @DynamicParameter(name = "NC_GROUP_DESC", value = "返回字段：不合格代码组描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageNcGroup(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageNcGroup(new QueryPage<>(params), params));

    }

    @PostMapping("/selectPageStationType")
    @ApiOperation(value = "模糊查询工位类型弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonStationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "stationType", value = "工位类型"),
            @DynamicParameter(name = "stationTypeDesc", value = "工位类型描述"),
            @DynamicParameter(name = "STATION_TYPE", value = "返回字段：工位类型，不需要传入"),
            @DynamicParameter(name = "STATION_TYPE_DESC", value = "返回字段：工位类型描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageStationType(@RequestBody Map<String, Object> params) {
        params.put("isAsc", false);
        params.put("orderByField", Station.CREATE_DATE);
        return ResponseData.success(commonBrowserService.selectPageStationType(new QueryPage<>(params), params));

    }


    /**
     * 模糊查询客户数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageCustomer")
    @ApiOperation(value = "模糊查询客户数据弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonCustomerRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "customer", value = "客户编号，可不填"),
            @DynamicParameter(name = "customerName", value = "客户名称，可不填"),
            @DynamicParameter(name = "customerDesc", value = "客户描述，可不填"),
            @DynamicParameter(name = "CUSTOMER", value = "返回字段：客户编号，不需要传入"),
            @DynamicParameter(name = "CUSTOMER_NAME", value = "返回字段：客户名称，不需要传入"),
            @DynamicParameter(name = "CUSTOMER_DESC", value = "返回字段，客户描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageCustomer(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageCustomer(new QueryPage<>(params), params));

    }


    @PostMapping("/selectPageItem")
    @ApiOperation(value = "模糊查询物料数据弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonItemRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认100，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "item", value = "物料编号，可不填"),
            @DynamicParameter(name = "itemName", value = "物料名称，可不填"),
            @DynamicParameter(name = "itemDesc", value = "物料描述，可不填"),
            @DynamicParameter(name = "drawingNo", value = "物料图号，可不填"),
            @DynamicParameter(name = "version", value = "版本，可不填"),
            @DynamicParameter(name = "itemType", value = "物料类别，可不填"),
            @DynamicParameter(name = "ITEM", value = "返回字段：物料编号，不需要传入"),
            @DynamicParameter(name = "VERSION", value = "返回字段：版本，不需要传入"),
            @DynamicParameter(name = "ITEM_NAME", value = "返回字段:物料名称，不需要传入"),
            @DynamicParameter(name = "ITEM_DESC", value = "返回字段，物料描述，不需要传入"),
            @DynamicParameter(name = "DRAWING_NO", value = "返回字段，物料图号，不需要传入"),
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageItem(@RequestBody Map<String, Object> params) {
        params.put("isAsc", false);
        params.put("orderByField", Item.CREATE_DATE);
        return ResponseData.success(commonBrowserService.selectPageItem(new QueryPage<>(params), params));
    }

    /**
     * 模糊查询客户订单数据弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageCustomerOrder")
    @ApiOperation(value = "模糊查询客户订单弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonCustomerOrderRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "customer", value = "客户编号，可不填"),
            @DynamicParameter(name = "customerOrder", value = "订单编号，可不填"),
            @DynamicParameter(name = "state", value = "客户描述，可不填"),
            @DynamicParameter(name = "CUSTOMER", value = "返回字段：客户编号，不需要传入"),
            @DynamicParameter(name = "CUSTOMER_ORDER", value = "返回字段：订单编号，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段，状态BO，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageCustomerOrder(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageCustomerOrder(new QueryPage<>(params), params));

    }

    /**
     * 模糊查询数据列表弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageDataList")
    @ApiOperation(value = "模糊查询数据列表弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonDataListRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "dataList", value = "列表编号，可不填"),
            @DynamicParameter(name = "listName", value = "列表名称，可不填"),
            @DynamicParameter(name = "DATA_LIST", value = "返回字段：列表编号，不需要传入"),
            @DynamicParameter(name = "LIST_NAME", value = "返回字段：列表名称，不需要传入"),
            @DynamicParameter(name = "LIST_DESC", value = "返回字段，列表描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageDataList(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageDataList(new QueryPage<>(params), params));

    }

    /**
     * 模糊查询数据收集组弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageDcGroup")
    @ApiOperation(value = "模糊查询数据收集组弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonDcGroupRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "dcGroup", value = "收集组编号，可不填"),
            @DynamicParameter(name = "dcName", value = "收集组名称，可不填"),
            @DynamicParameter(name = "state", value = "收集组状态，可不填"),
            @DynamicParameter(name = "DC_GROUP", value = "返回字段：收集组编号，不需要传入"),
            @DynamicParameter(name = "DC_NAME", value = "返回字段：收集组名称，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：收集组状态，不需要传入"),
            @DynamicParameter(name = "DC_DESC", value = "返回字段，收集组描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageDcGroup(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageDcGroup(new QueryPage<>(params), params));
    }

    @PostMapping("/selectPageInstructor")
    @ApiOperation(value = "模糊查询作业指导书出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "InstructorRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "instructor", value = "作业指导书编号，可不填"),
            @DynamicParameter(name = "instructorName", value = "作业指导书名称，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不填"),
            @DynamicParameter(name = "INSTRUCTOR", value = "返回字段：作业指导书编号，不需要传入"),
            @DynamicParameter(name = "VERSION", value = "返回字段：版本，不需要传入"),
            @DynamicParameter(name = "INSTRUCTOR_NAME", value = "返回字段：作业指导书名称，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "INSTRUCTOR_DESC", value = "返回字段，作业指导书描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageInstructor(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageInstructor(new QueryPage<>(params), params));
    }


    /**
     * 模糊查询设备弹出框
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPageDevice")
    @ApiOperation(value = "模糊查询设备弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonDeviceRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "device", value = "设备编号，可不填"),
            @DynamicParameter(name = "deviceName", value = "设备名称，可不填"),
            @DynamicParameter(name = "DEVICE", value = "返回字段：设备编号，不需要传入"),
            @DynamicParameter(name = "DEVICE_NAME", value = "返回字段：设备名称，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：设备状态，不需要传入"),
            @DynamicParameter(name = "DEVICE_MODEL", value = "返回字段，设备型号，不需要传入"),
            @DynamicParameter(name = "DEVICE_DESC", value = "返回字段，设备描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageDevice(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageDevice(new QueryPage<>(params), params));
    }

    @PostMapping("/selectPageShopOrder")
    @ApiOperation(value = "模糊查询工单弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonShopOrderRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "shopOrder", value = "工单，可不填"),
            @DynamicParameter(name = "item", value = "物料，可不填"),
            @DynamicParameter(name = "startDate", value = "创建开始时间，可不填"),
            @DynamicParameter(name = "endDate", value = "创建结束时间，可不填"),
            @DynamicParameter(name = "workShopDesc", value = "车间名称，可不填"),
            @DynamicParameter(name = "SHOP_ORDER", value = "返回字段：工单编号，不需要传入"),
            @DynamicParameter(name = "ORDER_DESC", value = "返回字段：工单描述，不需要传入"),
            @DynamicParameter(name = "ORDER_QTY", value = "返回字段：工单数量，不需要传入"),
            @DynamicParameter(name = "ITEM", value = "返回字段：物料，不需要传入"),
            @DynamicParameter(name = "ITEM_DESC", value = "返回字段：物料描述，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "PLAN_END_DATE", value = "返回字段，计划完成时间，不需要传入"),
            @DynamicParameter(name = "WORK_SHOP_DESC", value = "返回字段，车间名称，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageShopOrder(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageShopOrder(new QueryPage<>(params), params));

    }

    @PostMapping("/selectPageWarehouse")
    @ApiOperation(value = "模糊查询线边仓弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "WarehouseRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "warehouse", value = "线边仓编号，可不填"),
            @DynamicParameter(name = "warehouseName", value = "线边仓名称，可不填"),
            @DynamicParameter(name = "WAREHOUSE", value = "返回字段：线边仓编号，不需要传入"),
            @DynamicParameter(name = "WAREHOUSE_NAME", value = "返回字段：线边仓名称，不需要传入"),
            @DynamicParameter(name = "WAREHOUSE_TYPE", value = "返回字段：线边仓类型，不需要传入"),
            @DynamicParameter(name = "WAREHOUSE_DESC", value = "返回字段，线边仓描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageWarehouse(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageWarehouse(new QueryPage<>(params), params));

    }


    @PostMapping("/selectPageDeviceType")
    @ApiOperation(value = "模糊查询设备类型弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonDeviceTypeRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "deviceType", value = "设备类型编号，可不填"),
            @DynamicParameter(name = "deviceTypeName", value = "设备类型名称，可不填"),
            @DynamicParameter(name = "DEVICE_TYPE", value = "返回字段：设备类型编号，不需要传入"),
            @DynamicParameter(name = "DEVICE_TYPE_NAME", value = "返回字段：设备类型名称，不需要传入"),
            @DynamicParameter(name = "DEVICE_TYPE_DESC", value = "返回字段，设备类型描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageDeviceType(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageDeviceType(new QueryPage<>(params), params));

    }


    @PostMapping("/selectPageShift")
    @ApiOperation(value = "模糊查询班次弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "ShiftRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "shift", value = "班次，可不填"),
            @DynamicParameter(name = "shiftName", value = "班次名称，可不填"),
            @DynamicParameter(name = "SHIFT", value = "返回字段：班次，不需要传入"),
            @DynamicParameter(name = "SHIFT_NAME", value = "返回字段：班次名称，不需要传入"),
            @DynamicParameter(name = "WORK_TIME", value = "返回字段：班次工作时长，不需要传入"),
            @DynamicParameter(name = "IS_VALID", value = "返回字段：是否有效 Y：表示当前班次有效 N：表示当前班次无效，不需要传入"),
            @DynamicParameter(name = "SHIFT_DESC", value = "返回字段，班次描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageShift(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageShift(new QueryPage<>(params), params));

    }

    @PostMapping("/selectPageTeam")
    @ApiOperation(value = "模糊查询班组弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "TeamRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "team", value = "班组，可不填"),
            @DynamicParameter(name = "teamDesc", value = "班组描述，可不填"),
            @DynamicParameter(name = "TEAM", value = "返回字段：班组，不需要传入"),
            @DynamicParameter(name = "TEAM_DESC", value = "返回字段：班组描述，不需要传入"),
            @DynamicParameter(name = "LEADER", value = "返回字段：班组长，不需要传入"),
            @DynamicParameter(name = "PRODUCT_LINE", value = "返回字段：产线，不需要传入"),
            @DynamicParameter(name = "PRODUCT_LINE_DESC", value = "返回字段，产线描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageTeam(@RequestBody Map<String, Object> params) {


        return ResponseData.success(commonBrowserService.selectPageTeam(new QueryPage<>(params), params));

    }

    @PostMapping("/selectPageEmployee")
    @ApiOperation(value = "模糊查询员工信息弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "EmployeeRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "employeeCode", value = "员工编号，可不填"),
            @DynamicParameter(name = "name", value = "姓名，可不填"),
            @DynamicParameter(name = "EPMLOYEE_CODE", value = "返回字段：员工编号，不需要传入"),
            @DynamicParameter(name = "NAME", value = "返回字段：姓名，不需要传入"),
            @DynamicParameter(name = "ENABLED_FLAG", value = "返回字段：是否启用 Y已启用，N未启用，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageEmployee(@RequestBody Map<String, Object> params) {


        return ResponseData.success(commonBrowserService.selectPageEmployee(new QueryPage<>(params), params));

    }


    /*@PostMapping("/selectPageSN")
    @ApiOperation(value = "查询SN弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name="SNRequestModel",properties={
            @DynamicParameter( name="page", value = "页面，默认为1" ),
            @DynamicParameter( name="limit", value = "分页大小，默认20，可不填" ),
            @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
            @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
            @DynamicParameter( name="operation", value = "工序编号，可不填" ),
            @DynamicParameter( name="version", value = "工序版本，可不填" ),
            @DynamicParameter( name="SFC", value = "返回字段：SFC，不需要传入" ),
            @DynamicParameter( name="ITEM", value = "返回字段：物料，不需要传入" ),
            @DynamicParameter( name="VERSION", value = "返回字段：物料版本，不需要传入" ),
            @DynamicParameter( name="SHOP_ORDER", value = "返回字段：工单，不需要传入" ),
            @DynamicParameter( name="QTY", value = "返回字段：数量，不需要传入" ),
            @DynamicParameter( name="MODIFY_USER", value = "返回字段：修改人，不需要传入" )
    }))
    public ResponseData<IPage<Map<String,Object>>> selectPageSN(@RequestBody Map<String,Object> params){
        try {
            return ResponseData.success(commonBrowserService.selectPageSN(new QueryPage<>(params),params));
        } catch (BusinessException e) {
            return ResponseData.success(false, 10000, e.getMessage() );
        }

    }*/


    @PostMapping("/selectPageDcParameter")
    @ApiOperation(value = "模糊查询数据收集组参数表弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonDcParameterRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "paramName", value = "参数名，可不填"),
            @DynamicParameter(name = "DC_GROUP", value = "返回字段：数据收集组，不需要传入"),
            @DynamicParameter(name = "PARAM_NAME", value = "返回字段：参数名，不需要传入"),
            @DynamicParameter(name = "PARAM_DESC", value = "返回字段：参数说明，不需要传入"),
            @DynamicParameter(name = "PARAM_TYPE", value = "返回字段：类型，不需要传入"),
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageDcParameter(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageDcParameter(new QueryPage<>(params), params));

    }


    @PostMapping("/selectPageSfc")
    @ApiOperation(value = "模糊查询车间作业控制信息表弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonSfcRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "sfc", value = "SFC编号，可不填"),
            @DynamicParameter(name = "SFC", value = "返回字段：SFC编号，不需要传入"),
            @DynamicParameter(name = "ITEM_BO", value = "返回字段：物料，不需要传入"),
            @DynamicParameter(name = "QTY", value = "返回字段：数量，不需要传入"),
            @DynamicParameter(name = "BOM_BO", value = "返回字段：物料清单，不需要传入"),
            @DynamicParameter(name = "SHOP_ORDER_BO", value = "返回字段：工单，不需要传入"),
            @DynamicParameter(name = "PRODUCT_LINE_BO", value = "返回字段：产线，不需要传入"),
            @DynamicParameter(name = "MODIFY_USER", value = "返回字段：修改人，不需要传入"),
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageSfc(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageSfc(new QueryPage<>(params), params));

    }

    @PostMapping("/selectPageRouter")
    @ApiOperation(value = "模糊查询工艺路线弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonRouterRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "router", value = "工艺路线，可不填"),
            @DynamicParameter(name = "routerDesc", value = "工艺路线描述，可不填"),
            @DynamicParameter(name = "ROUTER", value = "返回字段：工艺路线，不需要传入"),
            @DynamicParameter(name = "ROUTER_DESC", value = "返回字段：工艺路线描述，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "VERSION", value = "返回字段：版本，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageRouter(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageRouter(new QueryPage<>(params), params));

    }

    @PostMapping("/selectPagePackData")
    @ApiOperation(value = "模糊查询包装数据弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonPackDataRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "packCode", value = "包装箱编码，可不填"),
            @DynamicParameter(name = "packName", value = "包装名称，可不填"),
            @DynamicParameter(name = "PACK_CODE", value = "返回字段：包装箱编码，不需要传入"),
            @DynamicParameter(name = "PACK_NAME", value = "返回字段：包装名称，不需要传入"),
            @DynamicParameter(name = "PACK_DESC", value = "返回字段：描述，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "SUM", value = "返回字段：合计数量，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPagePackData(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPagePackData(new QueryPage<>(params), params));

    }


    @PostMapping("/selectPagePacking")
    @ApiOperation(value = "模糊查询包装信息弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonPackingRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "packName", value = "包装，可不填"),
            @DynamicParameter(name = "packDesc", value = "包装描述，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不填"),
            @DynamicParameter(name = "packGrade", value = "状态，可不填"),
            @DynamicParameter(name = "PACK_NAME", value = "返回字段：包装名称，不需要传入"),
            @DynamicParameter(name = "PACK_DESC", value = "返回字段：描述，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "PACK_GRADE", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "MAX_QTY", value = "返回字段：最大数量，不需要传入"),
            @DynamicParameter(name = "MIN_QTY", value = "返回字段：最小数量，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPagePacking(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPagePacking(new QueryPage<>(params), params));

    }

    @PostMapping("/selectPageCarrierType")
    @ApiOperation(value = "模糊查询载具类型信息弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonCarrierTypeRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "carrierType", value = "载具类型，可不填"),
            @DynamicParameter(name = "description", value = "载具描述，可不填"),
            @DynamicParameter(name = "CARRIER_TYPE", value = "返回字段：载具类型，不需要传入"),
            @DynamicParameter(name = "DESCRIPTION", value = "返回字段：载具类型描述，不需要传入"),
            @DynamicParameter(name = "CAPACITY", value = "返回字段：容量，不需要传入"),
            @DynamicParameter(name = "ROW_SIZE", value = "返回字段：行数，不需要传入"),
            @DynamicParameter(name = "COLUMN_SIZE", value = "返回字段：列数，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageCarrierType(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageCarrierType(new QueryPage<>(params), params));

    }


    @PostMapping("/selectPageCarrier")
    @ApiOperation(value = "模糊查询载具信息弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonCarrierRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "carrier", value = "载具，可不填"),
            @DynamicParameter(name = "carrierType", value = "载具类型，可不填"),
            @DynamicParameter(name = "description", value = "载具描述，可不填"),
            @DynamicParameter(name = "CARRIER", value = "返回字段：载具，不需要传入"),
            @DynamicParameter(name = "CARRIER_TYPE", value = "返回字段：载具类型，不需要传入"),
            @DynamicParameter(name = "DESCRIPTION", value = "返回字段：载具描述，不需要传入"),
            @DynamicParameter(name = "CARRIER_TYPE_DESCRIPTION", value = "返回字段：载具类型描述，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageCarrier(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageCarrier(new QueryPage<>(params), params));

    }

    @PostMapping("/selectPageUser")
    @ApiOperation(value = "模糊查询用户信息弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonCarrierRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "userName", value = "用户名，可不填"),
            @DynamicParameter(name = "realName", value = "真实姓名，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不填"),
            @DynamicParameter(name = "USER_NAEM", value = "返回字段：用户名，不需要传入"),
            @DynamicParameter(name = "REAL_NAME", value = "返回字段：真实姓名，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageUser(@RequestBody Map<String, Object> params) {

        return ResponseData.success(commonBrowserService.selectPageUser(new QueryPage<>(params), params));

    }


    @PostMapping("/selectPageWorkstation")
    @ApiOperation(value = "模糊查询工步弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonWorkStationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "workStepCode", value = "工步编码"),
            @DynamicParameter(name = "workStepName", value = "工步名称"),
            @DynamicParameter(name = "workStepDesc", value = "工步描述"),
            @DynamicParameter(name = "working_Process", value = "工步所属工序"),
            @DynamicParameter(name = "BO", value = "返回字段：主键BO"),
            @DynamicParameter(name = "WORK_STEP_CODE", value = "返回字段：工步编码"),
            @DynamicParameter(name = "WORK_STEP_NAME", value = "返回字段：工步名称"),
            @DynamicParameter(name = "WORK_STEP_DESC", value = "返回字段：工步描述"),
            @DynamicParameter(name = "WORKING_PROCESS", value = "返回字段：工步所属工序"),
            @DynamicParameter(name = "EFFECTIVE", value = "返回字段：工步所属工序"),
            @DynamicParameter(name = "UPDATE_TIME", value = "返回字段：更新时间"),
            @DynamicParameter(name = "UPDATED_BY", value = "返回字段：更新人")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageWorkstation(@RequestBody Map<String, Object> params) {
        return ResponseData.success(commonBrowserService.selectPageWorkstation(new QueryPage<>(params), params));
    }

    @PostMapping("/selectPageRoutestation")
    @ApiOperation(value = "模糊查询工艺路线工步维护弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonRouteStationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "router", value = "工艺路线"),
            @DynamicParameter(name = "version", value = "版本"),
            @DynamicParameter(name = "workStepCode", value = "工步编码"),
            @DynamicParameter(name = "isCurrentVersion", value = "是否当前版本Y/N可不填"),
            @DynamicParameter(name = "workStepName", value = "工步名称"),
            @DynamicParameter(name = "workStepDesc", value = "工步描述"),
            @DynamicParameter(name = "workingProcess", value = "工序"),
            @DynamicParameter(name = "operationName", value = "工序名称"),
            @DynamicParameter(name = "BO", value = "返回字段：主键BO"),
            @DynamicParameter(name = "PROCESS_ROUTE", value = "返回字段：工艺路线，不需要传入"),
            @DynamicParameter(name = "ROUTE_VER", value = "返回字段：版本，不需要传入"),
            @DynamicParameter(name = "WORKING_PROCESS", value = "返回字段：工序，不需要传入"),
            @DynamicParameter(name = "WORKING_PROCESS_NAME", value = "返回字段：工序名称，不需要传入"),
            @DynamicParameter(name = "WORK_STEP_CODE", value = "返回字段：工步编码"),
            @DynamicParameter(name = "WORK_STEP_NAME", value = "返回字段：工步名称"),
            @DynamicParameter(name = "UPDATE_TIME", value = "返回字段：更新时间"),
            @DynamicParameter(name = "UPDATED_BY", value = "返回字段：更新人")
    }))
    public ResponseData<IPage<Map<String, Object>>> selectPageRoutestation(@RequestBody Map<String, Object> params) {
        return ResponseData.success(commonBrowserService.selectPageRoutestation(new QueryPage<>(params), params));
    }
}
