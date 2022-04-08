package com.itl.mes.core.provider.webService.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.provider.mapper.BomComponnetMapper;
import com.itl.mes.core.provider.mapper.ErpToMesShopOrderLogMapper;
import com.itl.mes.core.provider.webService.ShopOrderWebService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 工单维护webservice实现类
 */
@WebService(serviceName = "ERPToMESWoInfo", // 与接口中指定的name一致
        targetNamespace = "http://webservice.provider.core.mes.itl.com/", // 与接口中的命名空间一致
        endpointInterface = "com.itl.mes.core.provider.webService.ShopOrderWebService"// 接口地址
)
@Component
@Slf4j
public class ShopOrderWebServiceImpl implements ShopOrderWebService {

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BomComponnetService bomComponnetService;
    
    @Autowired
    private BomService bomService;

    @Autowired
    private BomComponnetMapper bomComponnetMapper;

    @Autowired
    private ErpToMesShopOrderLogMapper erpToMesShopOrderLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveShopOrderInfo(String info){
        log.info("打印工单请求字符串======>" + info);
        boolean isLegitimate;//判断json字符串是否合法
        try {
            JSONObject.parseObject(info);
            isLegitimate = true;
        }catch (Exception e){
            isLegitimate = false;
        }
        try{
            if (isLegitimate){
                JSONObject infoObj = JSON.parseObject(info);
                log.info("工单参数列表======>" +infoObj.toJSONString());
                String itemErp = infoObj.getString("ITEM");
                String shopOrder = infoObj.getString("SHOP_ORDER");//工单编号
                Item item = itemService.getOne(new QueryWrapper<Item>().eq("item", itemErp));
                if (ObjectUtil.isEmpty(item)){
                    log.info("物料为：<" + itemErp + ">的单号未维护");
                    return "物料为：<" + itemErp + ">的单号未维护";
                }
                ShopOrder shopOrderEntity = shopOrderService.getOne(new QueryWrapper<ShopOrder>().eq("shop_order", shopOrder));//查询工单号是否存在
                if (ObjectUtil.isEmpty(shopOrderEntity)){
                    insertShopOrder(infoObj,item.getBo());
                }else {
                    if ("STATE:dongyin,500".equals(shopOrderEntity.getStateBo()) || "STATE:dongyin,501".equals(shopOrderEntity.getStateBo())){
                        //校验修改的工单数量是否小于已下达的数量
                        String shopOrderBo = "SO:" + infoObj.getString("SITE") + "," +infoObj.getString("SHOP_ORDER");
                        BigDecimal releaseQty = shopOrderService.getById(shopOrderBo).getReleaseQty();//这条工单已下达数量
                        BigDecimal orderQty = new BigDecimal(infoObj.getString("ORDER_QTY"));//工单数量
                        if (orderQty.compareTo(releaseQty) == -1){//如果工单数量小于已下达数量
                            log.info("工单号为：<" + infoObj.getString("SHOP_ORDER") + ">的工单号工单数量小于该工单在MES已下达的数量");
                            return "工单号为：<" + infoObj.getString("SHOP_ORDER") + ">的工单号工单数量小于该工单在MES已下达的数量";
                        }
                    }
                    updateShopOrder(infoObj,item.getBo(),"",shopOrderEntity.getStateBo());
                }

                JSONArray itemList = JSONArray.parseArray(infoObj.getString("ITEM_LIST"));

                //校验物料集合在物料表中是否存在，理论上肯定存在，保险起见做个校验
                for (int i = 0; i < itemList.size(); i++) {
                    JSONObject itemJsonObj = JSON.parseObject(itemList.get(i).toString());
                    Item itemObj = itemService.getOne(new QueryWrapper<Item>().eq("item", itemJsonObj.getString("COMPONENT_BO")));
                    if (ObjectUtil.isEmpty(itemObj)){
                        log.info("<" + itemJsonObj.getString("COMPONENT_BO") + ">的物料号未维护");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
                        return "<" + itemJsonObj.getString("COMPONENT_BO") + ">的物料号未维护";
                    }
                }
                Bom one = bomService.getOne(new QueryWrapper<Bom>().eq("bom",shopOrder));
                Bom bom;
                if (ObjectUtil.isEmpty(one)){
                    bom = insertBom(infoObj.getString("SITE"),infoObj.getString("SHOP_ORDER"));
                    updateShopOrder(infoObj,item.getBo(),bom.getBo(),"1");
                }else {
                    bom = one;
                }
                delete(bom.getBo(),infoObj.getString("SITE"));//先删除m_bom_componnet表下该bomBo所有的数据
                //保存BOM组件表信息
                for (int i = 0; i < itemList.size(); i++) {
                    //然后逐条新增
                    JSONObject itemJsonObj = JSON.parseObject(itemList.get(i).toString());
                    BomComponnet bomComponnet = new BomComponnet();
                    bomComponnet.setBo("BC:"+infoObj.getString("SITE")+","+bom.getBom()+","+itemJsonObj.getString("COMPONENT_BO"));
                    bomComponnet.setBomBo(bom.getBo());

                    Item itemObj = itemService.getOne(new QueryWrapper<Item>().eq("item", itemJsonObj.getString("COMPONENT_BO")));
                    bomComponnet.setComponentBo(itemObj.getBo());//物料BO
                    bomComponnet.setSite(infoObj.getString("SITE"));
                    bomComponnet.setQty(new BigDecimal(itemJsonObj.getString("QTY")));
                    bomComponnet.setItemUnit(itemJsonObj.getString("ITEM_UNIT"));
                    bomComponnetService.save(bomComponnet);
                }
                ErpToMesShopOrderLog erpToMesShopOrderLog = new ErpToMesShopOrderLog();
                erpToMesShopOrderLog.setShopOrder(shopOrder);
                erpToMesShopOrderLog.setInfo(info);
                erpToMesShopOrderLog.setCreateTime(new Date());
                erpToMesShopOrderLogMapper.insert(erpToMesShopOrderLog);
            }else {
                log.info("字符串不合法，请检查格式");
                return "字符串不合法，请检查格式";
            }
        }catch (Exception e){
            log.info("异常信息<" + e.toString() + ">");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
            return "保存失败";
        }

        return "保存成功";
    }

    /**
     * 新增工单
     */
    private void insertShopOrder(JSONObject infoObj,String itemBo) throws ParseException {
        ShopOrder shopOrderObj = new ShopOrder();
        Date date = new Date();
        shopOrderObj.setBo("SO:" + infoObj.getString("SITE") + "," +infoObj.getString("SHOP_ORDER"));
        shopOrderObj.setSite(infoObj.getString("SITE"));
        shopOrderObj.setShopOrder(infoObj.getString("SHOP_ORDER"));
        shopOrderObj.setCustomerOrderBo(infoObj.getString("CUSTOMER_ORDERNO"));
        shopOrderObj.setOrderDesc(infoObj.getString("ORDER_DESC"));
        if (infoObj.getString("STATE_BO").equals("2")){//状态为2为ERP结案工单
            shopOrderObj.setIsErpCloseOrder("1");
            shopOrderObj.setStateBo("STATE:"+ infoObj.getString("SITE") + ",502");
            shopOrderObj.setEndTime(date);
        }else if (infoObj.getString("STATE_BO").equals("0")){
            shopOrderObj.setStateBo("STATE:"+ infoObj.getString("SITE") + ",500");
        }
        shopOrderObj.setShopType(infoObj.getString("SHOP_TYPE"));
        shopOrderObj.setWorkShop(infoObj.getString("WORK_SHOP"));
        shopOrderObj.setOrderQty(new BigDecimal(infoObj.getString("ORDER_QTY")));
        shopOrderObj.setPlanStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(infoObj.getString("PLAN_START_DATE")));
        shopOrderObj.setPlanEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(infoObj.getString("PLAN_END_DATE")));
        shopOrderObj.setCreateUser("ERP");
        shopOrderObj.setCreateDate(date);
        shopOrderObj.setShopOrderClass(infoObj.getString("SHOP_ORDER_CLASS"));
        shopOrderObj.setItemBo(itemBo);

//        Item item = itemService.getOne(new QueryWrapper<Item>().eq("item", infoObj.getString("ITEM")));
//        if (StringUtils.isNotBlank(item.getRouterBo())){
//            shopOrderObj.setRouterBo(item.getRouterBo());
//        }
        shopOrderService.save(shopOrderObj);
    }

    /**
     * 更新工单
     */
    private void updateShopOrder(JSONObject infoObj,String itemBo,String bomBo,String stateBo) throws ParseException {

        ShopOrder shopOrderObj = new ShopOrder();
        Date date = new Date();
        shopOrderObj.setBo("SO:" + infoObj.getString("SITE") + "," +infoObj.getString("SHOP_ORDER"));
        shopOrderObj.setSite(infoObj.getString("SITE"));
        shopOrderObj.setShopOrder(infoObj.getString("SHOP_ORDER"));
        shopOrderObj.setCustomerOrderBo(infoObj.getString("CUSTOMER_ORDERNO"));
        shopOrderObj.setOrderDesc(infoObj.getString("ORDER_DESC"));
        if (infoObj.getString("STATE_BO").equals("2")){//状态为2为ERP结案工单
            shopOrderObj.setIsErpCloseOrder("1");
            shopOrderObj.setStateBo("STATE:"+ infoObj.getString("SITE") + ",502");
            shopOrderObj.setEndTime(date);
//            shopOrderObj.setStateBo(infoObj.getString("STATE_BO"));
        }else if (infoObj.getString("STATE_BO").equals("0")){
            if (!"STATE:dongyin,500".equals(stateBo) && !"1".equals(stateBo)){
                shopOrderObj.setStateBo("STATE:"+ infoObj.getString("SITE") + ",501");
            }
        }
        shopOrderObj.setShopType(infoObj.getString("SHOP_TYPE"));
        shopOrderObj.setWorkShop(infoObj.getString("WORK_SHOP"));
        shopOrderObj.setOrderQty(new BigDecimal(infoObj.getString("ORDER_QTY")));
        shopOrderObj.setPlanStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(infoObj.getString("PLAN_START_DATE")));
        shopOrderObj.setPlanEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(infoObj.getString("PLAN_END_DATE")));
        shopOrderObj.setModifyDate(date);
        shopOrderObj.setModifyUser("ERP");
        if (StringUtils.isNotBlank(bomBo)){
            shopOrderObj.setBomBo(bomBo);
        }
        shopOrderObj.setShopOrderClass(infoObj.getString("SHOP_ORDER_CLASS"));
        shopOrderObj.setItemBo(itemBo);
        if (StringUtils.isNotBlank(infoObj.getString("COMPLETE_SET_QTY"))){
            shopOrderObj.setCompleteSetQty(new BigDecimal(infoObj.getString("COMPLETE_SET_QTY")));
        }
        shopOrderService.update(shopOrderObj, new QueryWrapper<ShopOrder>().eq("shop_order", infoObj.getString("SHOP_ORDER")));
    }

    /**
     * bom编码规则：BM:shopOrder
     * bo规则BOM:site:bom:version
     * 新增一条BOM表数据并返回BOM表的BO
     */
    private Bom insertBom(String site,String shopOrder){
        Bom bom = new Bom();

        bom.setBom(shopOrder);
        bom.setSite(site);
        bom.setVersion("1.0");
        bom.setIsCurrentVersion("Y");
        bom.setCreateUser("ERP");
        bom.setBomStandard("");//暂定
        bom.setBo("BOM:" + site + "," + bom.getBom() + "," + "1.0");

        bomService.save(bom);
        return bom;
    }

    public int delete(String bomBO,String site) {
        QueryWrapper<BomComponnet> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(BomComponnet.SITE, site);
        entityWrapper.eq(BomComponnet.BOM_BO,bomBO);
        Integer deleteInt = bomComponnetMapper.delete(entityWrapper);
        return deleteInt;
    }

}
