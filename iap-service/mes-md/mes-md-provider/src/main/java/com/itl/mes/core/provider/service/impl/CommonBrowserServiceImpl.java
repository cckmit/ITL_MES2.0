package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.NcGroupHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.StationTypeHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.service.CommonBrowserService;
import com.itl.mes.core.provider.mapper.CommonBrowserMapper;
import com.itl.mes.core.provider.util.KeyWordUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 弹出框公用类
 */
@Service
public class CommonBrowserServiceImpl implements CommonBrowserService {

    @Autowired
    private CommonBrowserMapper commonBrowserMapper;

    @Resource
    private UserUtil userUtil;

    //@Autowired
    //private SfcWipLogService sfcWipLogService;

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
    @Override
    public IPage<Map<String, Object>> selectPageWorkShop(IPage<Map<String, Object>> page, @Param("params") Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageWorkShop(page, params);
        page.setRecords(list);
        return page;
    }

    @Override
    public IPage<Map<String, Object>> selectPageWorkShopByState(IPage<Map<String, Object>> page, @Param("params") Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageWorkShopByState(page, params);
        page.setRecords(list);
        return page;
    }

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
    @Override
    public IPage<Map<String, Object>> selectPageProductLine(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        if (StringUtils.isNotBlank((String) params.getOrDefault("workShop", ""))) {
            params.put("workShop", new WorkShopHandleBO(UserUtils.getSite(), params.get("workShop").toString()).getBo());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageProductLine(page, params);
        list.forEach(x -> {
            if (StringUtils.isNotBlank((String) x.getOrDefault("WORK_SHOP_BO", ""))) {
                x.put("WORK_SHOP_BO", new WorkShopHandleBO(x.get("WORK_SHOP_BO").toString()).getWorkShop());
            }
        });
        page.setRecords(list);
        return page;
    }

    @Override
    public IPage<Map<String, Object>> selectPageProductLineByState(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        if (StringUtils.isNotBlank((String) params.getOrDefault("workShop", ""))) {
            params.put("workShop", new WorkShopHandleBO(UserUtils.getSite(), params.get("workShop").toString()).getBo());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageProductLineByState(page, params);
        list.forEach(x -> {
            if (StringUtils.isNotBlank((String) x.getOrDefault("WORK_SHOP_BO", ""))) {
                x.put("WORK_SHOP_BO", new WorkShopHandleBO(x.get("WORK_SHOP_BO").toString()).getWorkShop());
            }
        });
        page.setRecords(list);
        return page;
    }

    /**
     * 模糊查询车间数据
     * site
     * siteDesc
     * siteType
     * enableFlag
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageSite(IPage<Map<String, Object>> page, Map<String, Object> params) {
        List<Map<String, Object>> list = commonBrowserMapper.selectPageSite(page, params);
        page.setRecords(list);
        return page;
    }

    @Override
    public IPage<Map<String, Object>> selectPageSiteByState(IPage<Map<String, Object>> page, Map<String, Object> params) {
        List<Map<String, Object>> list = commonBrowserMapper.selectPageSiteByState(page, params);
        page.setRecords(list);
        return page;
    }

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
    @Override
    public IPage<Map<String, Object>> selectPageBom(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> maps = commonBrowserMapper.selectPageBom(page, params);
        page.setRecords(maps);
        return page;
    }

    /**
     * 查询供应商数据
     * * site
     * vend
     * vendName
     * vendDesc
     * contact
     * tel
     * address
     *
     * @param page
     * @param params
     * @return
     */

    public IPage<Map<String, Object>> selectPageVendor(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> maps = commonBrowserMapper.selectPageVendor(page, params);
        page.setRecords(maps);
        return page;
    }


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
    public IPage<Map<String, Object>> selectPageItemGroup(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageItemGroup(page, params);
        page.setRecords(list);
        return page;
    }


    /**
     * 模糊查询客户数据
     * site
     * customer
     * customerName
     * customerDesc
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageCustomer(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageCustomer(page, params);
        page.setRecords(list);
        return page;
    }

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
    @Override
    public IPage<Map<String, Object>> selectPageStation(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageStation(page, params);
        page.setRecords(list);
        return page;
    }

    @Override
    public IPage<Map<String, Object>> selectPageStationByState(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageStationByState(page, params);
        page.setRecords(list);
        return page;
    }

    /**
     * 模糊查询工序信息
     * operation
     * operationDesc
     * productLine
     * state
     * isCurrentVersion
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageOperation(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageOperation(page, params);
        list.forEach(x -> x.put("STATION_TYPE", new StationTypeHandleBO((String) x.get("STATION_TYPE_BO")).getStationType()));
        page.setRecords(list);
        return page;
    }

    /**
     * ncCode
     * ncDesc
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageNcCode(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageNcCode(page, params);
        List<Map<String, Object>> tmpList = new ArrayList<>();
        Set<String> keysSet = new HashSet<String>();
        for (Map<String, Object> map : list) {
            String keys = (String) map.get("NC_CODE");
            int beforeSize = keysSet.size();
            keysSet.add(keys);
            int afterSize = keysSet.size();
            if (afterSize == beforeSize + 1) {
                tmpList.add(map);
            }
        }
        page.setRecords(tmpList);
        return page;
    }

    @Override
    public IPage<Map<String, Object>> selectPageNcCodeByState(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageNcCodeByState(page, params);
        List<Map<String, Object>> tmpList = new ArrayList<>();
        Set<String> keysSet = new HashSet<String>();
        for (Map<String, Object> map : list) {
            String keys = (String) map.get("NC_CODE");
            String bo = (String) map.get("BO");
            int beforeSize = keysSet.size();
            keysSet.add(keys);
            int afterSize = keysSet.size();
            if (afterSize == beforeSize + 1) {
                tmpList.add(map);
            }
        }
        page.setRecords(tmpList);
        return page;
    }

    @Override
    public List<Map<String, Object>> selectPageNcCodeByStateByNcGroup(String params) {
        String bo = null;
        if (StrUtil.isNotEmpty(params)) {
            bo = new NcGroupHandleBO(UserUtils.getSite(), params).getBo();
        }
        List<Map<String, Object>> maps = commonBrowserMapper.selectPageNcCodeByStateByNcGroup(bo, UserUtils.getSite());
        return maps;
    }

    /**
     * ncGroup
     * ncGroupDesc
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageNcGroup(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageNcGroup(page, params);
        page.setRecords(mapList);
        return page;
    }

    /**
     * 模糊查询工位类型信息
     * stationType
     * stationTypeDesc
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageStationType(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageStationType(page, params);
        page.setRecords(mapList);
        return page;
    }

    /**
     * 模糊查询物料信息
     * item
     * itemDesc
     * version
     * 模糊查询客户订单数据
     * customer
     * customerOrder
     * state
     */
    @Override
    public IPage<Map<String, Object>> selectPageCustomerOrder(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageCustomerOrder(page, params);
        page.setRecords(list);
        return page;
    }

    /**
     * 模糊查询数据列表信息
     * dataList
     * listName
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageDataList(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageDataList(page, params);
        page.setRecords(list);
        return page;
    }

    /**
     * 模糊查询数据列表信息
     * dcGroup
     * dcName
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageItem(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageItem(page, params);
        page.setRecords(mapList);
        return page;
    }

    /**
     * 模糊查询数据收集组出框
     *
     * @param page
     * @param params
     * @return
     */
    public IPage<Map<String, Object>> selectPageDcGroup(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageDcGroup(page, params);
        page.setRecords(list);
        return page;
    }

    /**
     * 模糊查询设备信息
     * device
     * deviceName
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageDevice(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageDevice(page, params);
        page.setRecords(list);
        return page;
    }

    /**
     * 模糊查询作业指导书信息
     * INSTRUCTOR
     * INSTRUCTOR_NAME
     * INSTRUCTOR_DESC
     * STATE
     * VERSION
     *
     * @param page
     * @param params
     * @return
     */
    public IPage<Map<String, Object>> selectPageInstructor(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageInstructor(page, params);
        page.setRecords(mapList);
        return page;
    }

    /**
     * 模糊查询工单数据
     * shopOrder 工单
     * item 物料
     * startDate 创建时间
     * endDate 创建时间
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageShopOrder(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        if(params.get("itemDesc") !=null && StringUtils.isNotBlank(params.get("itemDesc").toString())){
            List<String> itemDescKeyWordList= KeyWordUtil.encapsulationItemDescKeyWord(params.get("itemDesc").toString());
            params.put("itemDescKeyWordList",itemDescKeyWordList);
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageShopOrder(page, params);
        page.setRecords(list);
        return page;
    }

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
    public IPage<Map<String, Object>> selectPageWarehouse(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageWarehouse(page, params);
        page.setRecords(mapList);
        return page;
    }


    /**
     * 模糊查询设备类型弹出框
     * deviceType
     * deviceTypeName
     *
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageDeviceType(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageDeviceType(page, params);
        page.setRecords(mapList);
        return page;
    }

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
    @Override
    public IPage<Map<String, Object>> selectPageShift(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageShift(page, params);
        page.setRecords(mapList);
        return page;
    }

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
    @Override
    public IPage<Map<String, Object>> selectPageTeam(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageTeam(page, params);
        page.setRecords(mapList);
        return page;
    }

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
    @Override
    public IPage<Map<String, Object>> selectPageEmployee(IPage<Map<String, Object>> page, Map<String, Object> params) {
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageEmployee(page, params);
        page.setRecords(mapList);
        return page;
    }

    /**
     * 查询SN相关信息
     *
     * @param page
     * @param params
     * @return
     */
    /*@Override
    public IPage<Map<String, Object>> selectPageSN(IPage<Map<String, Object>> page, Map<String, Object> params) throws BusinessException {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());

                String bo = sfcWipLogService.getCurrentVerifyOperationBO(params.get("operation").toString());
                params.put("operationBo",bo);


        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageSN(page, params);
        page.setRecords(mapList);
        return page;
    }*/

    //查询数据收集参数表
    @Override
    public IPage<Map<String, Object>> selectPageDcParameter(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageDcParameters(page, params);
        page.setRecords(mapList);
        return page;
    }

    //模糊查询车间作业控制信息表弹出框
    @Override
    public IPage<Map<String, Object>> selectPageSfc(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageSfc(page, params);
        for (Map<String, Object> map : mapList) {
            if (map.containsKey("ITEM_BO")) {
                map.put("ITEM_BO", new ItemHandleBO(map.get("ITEM_BO").toString()).getItem());
            }
            //if (map.containsKey("SHOP_ORDER_BO")) {
            //    map.put("SHOP_ORDER_BO", new ShopOrderHandleBO(map.get("SHOP_ORDER_BO").toString()).getShopOrder());
            //}
            if (map.containsKey("PRODUCT_LINE_BO")) {
                map.put("PRODUCT_LINE_BO", new ProductLineHandleBO(map.get("PRODUCT_LINE_BO").toString()).getProductLine());
            }
        }
        page.setRecords(mapList);
        return page;
    }

    /**
     * 模糊查询工艺路线弹出框
     * router
     * routerDesc
     */
    @Override
    public IPage<Map<String, Object>> selectPageRouter(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageRouter(page, params);
        page.setRecords(mapList);
        return page;
    }

    /**
     * 模糊查询包装数据弹出框
     * packCode
     */
    @Override
    public IPage<Map<String, Object>> selectPagePackData(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPagePackData(page, params);
        page.setRecords(mapList);
        return page;
    }

    /**
     * 模糊查询包装弹出框
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPagePacking(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPagePacking(page, params);
        if (mapList.size() > 0) {
            for (Map<String, Object> stringObjectMap : mapList) {
                switch (stringObjectMap.getOrDefault("STATE", "").toString()) {
                    case "H":
                        stringObjectMap.put("STATE_NAME", "保留");
                        break;
                    case "C":
                        stringObjectMap.put("STATE_NAME", "冻结");
                        break;
                    case "R":
                        stringObjectMap.put("STATE_NAME", "可下达");
                        break;
                    case "N":
                        stringObjectMap.put("STATE_NAME", "新建");
                        break;
                    case "S":
                        stringObjectMap.put("STATE_NAME", "作废");
                        break;
                    default:
                        stringObjectMap.put("STATE_NAME", "");
                        break;
                }
            }
        }
        page.setRecords(mapList);
        return page;
    }

    @Override
    public IPage<Map<String, Object>> selectPageCarrierType(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageCarrierType(page, params);
        page.setRecords(mapList);
        return page;
    }


    /**
     * 模糊查询载具信息
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> selectPageCarrier(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageCarrier(page, params);
        page.setRecords(mapList);
        return page;
    }

    @Override
    public IPage<Map<String, Object>> selectPageUser(IPage<Map<String, Object>> page, Map<String, Object> params) {
        List<Map<String, Object>> mapList = commonBrowserMapper.selectPageUser(page, params);
        page.setRecords(mapList);
        return page;
    }

    @Override
    public IPage<Map<String, Object>> selectPageWorkstation(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageWorkstation(page, params);
        list.forEach(x -> x.put("STATION_TYPE", new StationTypeHandleBO((String) x.get("STATION_TYPE_BO")).getStationType()));
        page.setRecords(list);
        return page;
    }

    @Override
    public IPage<Map<String, Object>> selectPageRoutestation(IPage<Map<String, Object>> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map<String, Object>> list = commonBrowserMapper.selectPageRoutestation(page, params);
        list.forEach(x -> x.put("STATION_TYPE", new StationTypeHandleBO((String) x.get("STATION_TYPE_BO")).getStationType()));
        page.setRecords(list);
        return page;
    }

}
