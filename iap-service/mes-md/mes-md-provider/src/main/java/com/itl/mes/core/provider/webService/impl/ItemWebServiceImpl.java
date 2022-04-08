package com.itl.mes.core.provider.webService.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.provider.webService.ItemWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author pwy
 * @date 2021/3/10
 */
@WebService(serviceName = "ERPToMESMaterialInfo",
        targetNamespace = "http://webservice.provider.core.mes.itl.com/",
        endpointInterface = "com.itl.mes.core.provider.webService.ItemWebService")
@Component
@Slf4j
public class ItemWebServiceImpl implements ItemWebService {

    @Autowired
    public ItemService itemService;

    @Override
    public String saveItemInfo(String info) {
        log.info("打印物料请求字符串======>" + info);
        boolean isLegitimate;//判断json字符串是否合法
        try {
            JSONObject.parseObject(info);
            isLegitimate = true;
        }catch (JSONException e){
            isLegitimate = false;
        }
        try {
            if (isLegitimate){
                JSONObject infoObj = JSON.parseObject(info);
                log.info("物料参数列表======>" + infoObj);
                String itemNo = infoObj.getString("ITEM");//物料编码
                QueryWrapper<Item> queryWrapper = new QueryWrapper<Item>().eq("item", itemNo);
                Item one = itemService.getOne(queryWrapper);

                Item item = new Item();
                item.setBo("ITEM:" + infoObj.getString("SITE") + "," + itemNo + "," + infoObj.getString("VERSION"));
                item.setSite(infoObj.getString("SITE"));

                item.setItemName(infoObj.getString("ITEM_NAME"));
                item.setVersion(infoObj.getString("VERSION"));
                item.setIsCurrentVersion("Y");
                item.setItemType(infoObj.getString("ITEM_TYPE"));
                item.setItem(itemNo);
                item.setItemUnit(infoObj.getString("ITEM_UNIT"));
                item.setLotSize(new BigDecimal(infoObj.getString("LOT_SIZE")));
                if (infoObj.getString("ITEM_STATE_BO").equals("0")){
                    item.setItemStateBo("STATE:dongyin,N");
                }
                item.setItemDesc(infoObj.getString("ITEM_DESC"));
                item.setDrawingNo(infoObj.getString("DRAWING_NO"));

                item.setCreateUser("ERP");
                item.setModifyUser("ERP");
                if (ObjectUtils.isEmpty(one)){
                    item.setCreateDate(new Date());
                    itemService.save(item);
                }else {
                    item.setModifyDate(new Date());
                    itemService.update(item,queryWrapper);
                }
                log.info("保存成功：保存的物料单号为:<" + itemNo + ">");
            }else {
                log.info("字符串不合法,请检查格式");
                return "字符串不合法，请检查格式";
            }
        }catch (Exception e){
            log.info("异常信息=====>" + e.toString());
            return "保存失败！";
        }
        return "保存成功";
    }
}
