package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.service.CustomDataService;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commonCustomDatas")
@Api(tags = "自定义数据获取通用功能" )
public class CommonCustomDataController {

    private final Logger logger = LoggerFactory.getLogger(CommonCustomDataController.class);

    @Autowired
    private CustomDataService customDataService;

    @Autowired
    private CustomDataValService customDataValService;


    @GetMapping("/queryByDataType")
    @ApiOperation(value="通过类型查询自定义数据")
    public ResponseData<List<CustomDataVo>> getCustomDataByDataType(String customDataType ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( customDataType ) );
    }

    @GetMapping("/getWorkShopCustomData")
    @ApiOperation(value="获取车间自定义数据")
    public ResponseData<List<CustomDataVo>> getWorkShopCustomData( ) {
        return ResponseData.success(  customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.WORK_SHOP.getDataType() ) );
    }

    @GetMapping("/getProductLineCustomData")
    @ApiOperation(value="获取产线自定义数据")
    public ResponseData<List<CustomDataVo>> getProductLineCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.PRODUCT_LINE.getDataType() ) );
    }

    @GetMapping("/getLabelCustomData")
    @ApiOperation(value="获取标签自定义数据")
    public ResponseData<List<CustomDataVo>> getLabelCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.LABEL.getDataType() ) );
    }

    @GetMapping("/getItemGroupCustomData")
    @ApiOperation(value="获取物料组自定义数据")
    public ResponseData<List<CustomDataVo>> getItemGroupCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.ITEM_GROUP.getDataType() ) );
    }


    @GetMapping("/getItemCustomData")
    @ApiOperation(value="获取物料自定义数据")
    public ResponseData<List<CustomDataVo>> getItemCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.ITEM.getDataType() ) );
    }

    @GetMapping("/getCustomerCustomData")
    @ApiOperation(value="获取客户自定义数据")
    public ResponseData<List<CustomDataVo>> getCustomerCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.CUSTOMER.getDataType() ) );
    }


    @GetMapping("/getOperationCustomData")
    @ApiOperation(value="获取工序自定义数据")
    public ResponseData<List<CustomDataVo>> getOperationCustomData(){
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.OPERATION.getDataType() ) );
    }

    @GetMapping("/getStationCustomData")
    @ApiOperation(value="获取工位自定义数据")
    public ResponseData<List<CustomDataVo>> getStationCustomData(){
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.STATION.getDataType() ) );
    }




    @GetMapping("/getDcGroupCustomData")
    @ApiOperation(value="获取数据收集组自定义数据")
    public ResponseData<List<CustomDataVo>> getDcGroupCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.DC_GROUP.getDataType() ) );
    }



    @GetMapping("/getDeviceCustomData")
    @ApiOperation(value="获取设备自定义数据")
    public ResponseData<List<CustomDataVo>> getDeviceCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.DEVICE.getDataType() ) );
    }

    @GetMapping("/getNcCodeOrderCustomData")
    @ApiOperation(value="获取不合格代码自定义数据")
    public ResponseData<List<CustomDataVo>> getNcCodeOrderCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.NC_CODE.getDataType() ) );
    }

    @GetMapping("/getBomCustomData")
    @ApiOperation(value="获取物料清单自定义数据")
    public ResponseData<List<CustomDataVo>> getBomCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.BOM.getDataType() ) );
    }

    @GetMapping("/getVendorCustomData")
    @ApiOperation(value="获取供应商自定义数据")
    public ResponseData<List<CustomDataVo>> getVendorCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.VENDOR.getDataType() ) );
    }

    @GetMapping("/getInstructorCustomData")
    @ApiOperation(value="获取作业指导书自定义数据")
    public ResponseData<List<CustomDataVo>> getInstructorCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.INSTRUCTOR.getDataType() ) );
    }

    @GetMapping("/getTeamCustomData")
    @ApiOperation(value="获取班组自定义数据")
    public ResponseData<List<CustomDataVo>> getTeamCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.TEAM.getDataType() ) );
    }

    @GetMapping("/getShopOrderCustomData")
    @ApiOperation(value="获取工单自定义数据")
    public ResponseData<List<CustomDataVo>> getShopOrderCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.SHOP_ORDER.getDataType() ) );
    }

    @GetMapping("/getRouterCustomData")
    @ApiOperation(value="获取工艺路线自定义数据")
    public ResponseData<List<CustomDataVo>> getRouterCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.ROUTER.getDataType() ) );
    }

    @GetMapping("/getPackingCustomData")
    @ApiOperation(value="获取包装自定义数据")
    public ResponseData<List<CustomDataVo>> getPackingCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.PACKING.getDataType() ) );
    }

    @GetMapping("/getWarehouseCustomData")
    @ApiOperation(value="获取线边仓自定义数据")
    public ResponseData<List<CustomDataVo>> getWarehouseCustomData( ) {
        return ResponseData.success( customDataService.selectCustomDataVoListByDataType( CustomDataTypeEnum.WAREHOUSE.getDataType() ) );
    }

//    @GetMapping("/selectCustomDataVoListByDataType/{customDataType}")
//    @ApiOperation(value = "通过数据类型查询类型对应的自定义数据")
//    public List<CustomDataVo> selectCustomDataVoListByDataType(@PathVariable("customDataType") String customDataType) {
//        return customDataService.selectCustomDataVoListByDataType(customDataType);
//    }

    @GetMapping("/selectCustomDataVoListByDataType/{site}/{bo}/{dataType}")
    @ApiOperation(value = "根据 工厂、BO、数据类型查询自定义数据和自定义数据值")
    public List<CustomDataAndValVo> selectCustomDataAndValByBoAndDataType(
            @PathVariable("site") String site,
            @PathVariable("bo") String bo,
            @PathVariable("dataType") String dataType ){
        return customDataValService.selectCustomDataAndValByBoAndDataType(site, bo, dataType);
    }

    @PostMapping("/saveCustomDataVal")
    @ApiOperation(value = "保存自定义数据值")
    String saveCustomDataVal(@RequestBody CustomDataValRequest customDataValRequest) throws CommonException {
        customDataValService.saveCustomDataVal(customDataValRequest);
        return "success";
    }
}
