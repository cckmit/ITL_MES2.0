package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.utils.QueryPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonBrowserService {

    /**
     * 模糊查询车间数据
     * workShop
     * state
     * workShopDesc
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageWorkShop(IPage<Map<String, Object>> page, Map<String, Object> params);
    IPage<Map<String, Object>> selectPageWorkShopByState(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询产线数据
     * productLine
     * state
     * productLineDesc
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageProductLine(IPage<Map<String, Object>> page, Map<String, Object> params);
    IPage<Map<String, Object>> selectPageProductLineByState(IPage<Map<String, Object>> page, Map<String, Object> params);


    /**
     * 模糊查询工厂数据
     * site
     * siteDesc
     * siteType
     * enableFlag
     *
     * @param page
     * @param params
     * @return
     */

    IPage<Map<String, Object>> selectPageSite(IPage<Map<String, Object>> page, Map<String, Object> params);
    IPage<Map<String, Object>> selectPageSiteByState(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询物料清单数据
     * bom
     * bomDesc
     * version
     * state
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageBom(IPage<Map<String, Object>> page, @Param("params") Map<String, Object> params);




    /**
     * 模糊查询供应商数据
     * site
     * vend
     * vendName
     * vendDesc
     * contact
     * tel
     * address
     */
    IPage<Map<String, Object>> selectPageVendor(IPage<Map<String, Object>> page, @Param("params") Map<String, Object> params);

     /**
     * 模糊查询物料组数据
     * site
     * itemGroup
     * groupName
     * groupDesc
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageItemGroup(IPage<Map<String, Object>> page, Map<String, Object> params);


    /**
     * 模糊查询客户数据
     * site
     * customer
     * customerName
     * customerDesc
     */
    IPage<Map<String, Object>> selectPageCustomer(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询工位信息
     * station
     * stationName
     * stationDesc
     * state
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageStation(IPage<Map<String, Object>> page, Map<String, Object> params);
    IPage<Map<String, Object>> selectPageStationByState(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询工序信息
     * operation
     * operationDesc
     * productLine
     * state
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageOperation(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询不合格代码信息
     * ncCode
     *ncDesc
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageNcCode(IPage<Map<String, Object>> page, Map<String, Object> params);
    IPage<Map<String, Object>> selectPageNcCodeByState(IPage<Map<String, Object>> page, Map<String, Object> params);
    List<Map<String, Object>> selectPageNcCodeByStateByNcGroup(String params);

    /**
     * 模糊查询不合格代码组信息
     * ncGroup
     * ncGroupDesc
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageNcGroup(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询工位类型信息
     * stationType
     * stationTypeDesc
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageStationType(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询物料信息
     * item
     * itemDesc
     * version
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageItem(IPage<Map<String, Object>> page, Map<String, Object> params);


    /**
     * 模糊查询客户订单数据
     * customer
     * customerOrder
     * state
     */
    IPage<Map<String, Object>> selectPageCustomerOrder(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询数据列表信息
     * dataList
     * listName
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageDataList(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询数据列表信息
     * dcGroup
     * dcName
     * state
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageDcGroup(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询设备信息
     * device
     * deviceName
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageDevice(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询作业指导书信息
     * INSTRUCTOR
     * INSTRUCTOR_NAME
     * INSTRUCTOR_DESC
     * STATE
     * VERSION
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageInstructor(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询工单数据
     * shopOrder
     * item
     * startDate
     * endDate
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageShopOrder(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * *模糊查询线边仓弹出框
     * varehouse
     * varehouseName
     * varehouseDesc
     * varehouseType
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageWarehouse(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     *模糊查询设备类型弹出框
     * deviceType
     * deviceTypeName
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageDeviceType(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询班次弹出框
     * shift
     * shiftName
     * shiftDesc
     * workTime
     * isValid
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageShift(IPage<Map<String, Object>> page, Map<String, Object> params);


    /**
     * 模糊查询班组信息
     * group
     * groupDesc
     * leader
     * productLine
     * productLineDesc
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageTeam(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询员工信息
     * EPMLOYEE_CODE
     * NAME
     * ENABLED_FLAG
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageEmployee(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     *
     * @param page
     * @param params
     * @return
     */
    //IPage<Map<String, Object>> selectPageSN(IPage<Map<String, Object>> page, Map<String, Object> params) throws BusinessException;

    //模糊查询数据收集组参数表
    IPage<Map<String, Object>> selectPageDcParameter(IPage<Map<String, Object>> page, Map<String, Object> params);

    //模糊查询车间作业控制信息表弹出框
    IPage<Map<String, Object>> selectPageSfc(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询工艺路线弹出框
     * router
     * routerDesc
     */
    IPage<Map<String, Object>> selectPageRouter(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询包装数据弹出框
     * packCode
     */
    IPage<Map<String, Object>> selectPagePackData(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 模糊查询包装弹出框
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPagePacking(IPage<Map<String, Object>> page, @Param("params") Map<String, Object> params);
    /**
     * 模糊查询载具类型信息
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageCarrierType(IPage<Map<String, Object>> page, @Param("params")Map<String, Object> params);

    /**
     * 模糊查询载具信息
     * @param page
     * @param params
     * @return
     */
    IPage<Map<String, Object>> selectPageCarrier(IPage<Map<String, Object>> page, @Param("params")Map<String, Object> params);


    //模糊查询用户表
    IPage<Map<String, Object>> selectPageUser(IPage<Map<String, Object>> page, Map<String, Object> params);
    //模糊查询工步表
    IPage<Map<String, Object>>selectPageWorkstation(IPage<Map<String, Object>>page, Map<String, Object> params);
    //模糊查询工艺路线工步配置表
    IPage<Map<String, Object>> selectPageRoutestation(IPage<Map<String, Object>>page, Map<String, Object> params);
}
