package com.itl.mes.core.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.ReportWorkService;
import com.itl.mes.core.api.service.SfcService;
import com.itl.mes.core.api.service.ShopOrderService;
import com.itl.mes.core.api.service.StockService;
import com.itl.mes.core.provider.mapper.QualityCheckListMapper;
import com.itl.mes.core.provider.mapper.StockMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    static String  hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f" };

    @Value("${stock.apId}")
    private String apId;

    @Value("${stock.secretKey}")
    private String secretKey;

    @Value("${stock.ecName}")
    private String ecName;

    @Value("${stock.sign}")
    private String sign;

    @Value("${stock.url}")
    private String url;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private QualityCheckListMapper qualityCheckListMapper;

    @Autowired
    private SfcService sfcService;

    @Autowired
    private ReportWorkService reportWorkService;

    @Autowired
    private ShopOrderService shopOrderService;

    @Override
    public String applyStock(Sfc sfcEntity, String wareHouse,int flag) {
        String warehouseEntryNo = sfcEntity.getSfc();
        String shopOrder = new ShopOrderHandleBO(sfcEntity.getShopOrderBo()).getShopOrder();
        QueryWrapper<QualityCheckList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("SFC",sfcEntity.getSfc());
        queryWrapper.eq("CHECK_TYPE","2");
        queryWrapper.eq("CHECK_RESULT","合格");
        QualityCheckList qualityCheckList = qualityCheckListMapper.selectOne(queryWrapper);
        if (qualityCheckList!=null){
            sfcEntity.setCheckUser(qualityCheckList.getCheckUser());
            sfcEntity.setCheckUserName(qualityCheckList.getCheckUserName());
        }
        int okQty = sfcEntity.getDoneQty().intValue();
        //int okQty = 10;
        int scrapQty = sfcEntity.getScrapQty().intValue();
        Stock stock = new Stock();
        stock.setBo(UUID.uuid32());
        stock.setSite("dongyin");
        stock.setSfc(sfcEntity.getSfc());
        stock.setOkQty(okQty);
        stock.setScrap(scrapQty);
        stock.setWarehouse(wareHouse);
        stock.setShopOrderBo(sfcEntity.getShopOrderBo());
        stock.setItemBo(sfcEntity.getItemBo());
        stock.setCheckUser(sfcEntity.getCheckUser());
        stock.setCheckUserName(sfcEntity.getCheckUserName());
        stock.setApplyUser(userUtil.getUser().getUserName());
        stock.setApplyUser(userUtil.getUser().getRealName());
        stock.setDifQty(sfcEntity.getDifQty());
        stock.setSfcTotalQty(sfcEntity.getSfcQty());
        stock.setCreateDate(new Date());

        Map<String,Object> params = new HashMap<>();
        params.put("warehouse_entry_no",warehouseEntryNo);
        params.put("shop_order",shopOrder);
        params.put("ok_qty",okQty);
        params.put("scrap_qty",scrapQty);
        params.put("warehouse",wareHouse);
        params.put("apply_user",userUtil.getUser().getUserName());
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        //对方的wsdl地址
        Client client = dcf.createClient(url);
        String json = null;
        try {
            String  str="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
                    str+="<request type=\"sync\" key=\"76CFB4D975B57C166A5F93C79E19D933\">";
                    str+="<host prod=\"EDI\" ver=\"1.0\" ip=\"192.168.6.69\" lang=\"zh_CN\" timezone=\"8\" timestamp=\"20170309085642517\" acct=\"tiptop\"/>";
                    str+="<service prod=\"T100\" name=\"insSfea\" srvver=\"1.0\" id=\"00000\"/>";
                    str+="<datakey type=\"FOM\">";
                    str+="<key name=\"EntId\">1</key>";
                    str+="<key name=\"CompanyId\">DY</key>";
                    str+="</datakey>";
                    str+="<payload>";
                    str+="<param key=\"data\" type=\"XML\"><![CDATA[";
                    str+="<Request>";
                    str+="<RequestContent>";
                    str+="<Parameter/>";
                    str+="<Document>";
                    str+="<RecordSet id=\"1\">";
                    str+="<Master name=\"sfea_t\" node_id=\"1\">";
                    str+="<Record>";
                    str+="<Field name=\"sfeadocno\" value="+"\""+warehouseEntryNo+"\""+"/>";
                    str+="<Field name=\"sfea002\" value="+"\""+userUtil.getUser().getUserName()+"\""+"/>";
                    str+="<Detail name=\"sfeb_t\" >";
                    str+="<Record>";
                    str+="<Field name=\"sfeb001\" value="+"\""+shopOrder+"\""+"/>";
                    str+="<Field name=\"sfeb008\" value="+"\""+okQty+"\""+"/>";
                    str+="<Field name=\"sfeb013\" value="+"\""+wareHouse+"\""+"/>";
                    str+="</Record>";
                    str+="</Detail>";
                    str+="</Record>";
                    str+="</Master>";
                    str+="</RecordSet>";
                    str+="</Document>";
                    str+="</RequestContent>";
                    str+="</Request>]]>";
                    str+="</param>";
                    str+="</payload>";
                    str+="</request>";
            System.out.println(str);
            Object[] objects1= client.invoke("invokeSrv",str); //参数1，参数2，参数3......按顺序放就看可以
            json = JSONObject.toJSONString(objects1[0]);
            System.out.println("返回数据:" + json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //String result = getResult(json);
        if (json.indexOf("SUCCESS！")!=-1){
            if (flag == 1){//如果为EPR直接入库不在MES做任何处理
                stockMapper.insert(stock);
                Sfc sfc = new Sfc();
                sfc.setDoneQty(sfcEntity.getDoneQty());
                sfcService.update(sfc,new QueryWrapper<Sfc>().eq("sfc",sfcEntity.getSfc()));//更新sfc中的良品数量

                ReportWork reportWork = new ReportWork();
                reportWork.setDifQty(sfcEntity.getDifQty());
                reportWorkService.update(reportWork,new QueryWrapper<ReportWork>().eq("sfc",sfcEntity.getSfc()));//更新报工表中的差异数

                List<Stock> stockList = stockMapper.selectList(new QueryWrapper<Stock>().eq("shop_order_bo", sfcEntity.getShopOrderBo()));
                BigDecimal totalQty = BigDecimal.ZERO;
                for (Stock stockObj : stockList) {
                    totalQty = totalQty.add(stockObj.getSfcTotalQty());
                }
                BigDecimal orderQty = shopOrderService.getOne(new QueryWrapper<ShopOrder>().eq("bo",sfcEntity.getShopOrderBo())).getOrderQty();//工单总数
                if (totalQty.compareTo(orderQty) == 0){
                    ShopOrder shopOrderObj = new ShopOrder();
                    shopOrderObj.setBo(sfcEntity.getShopOrderBo());
                    shopOrderObj.setStateBo("STATE:dongyin,502");
                    shopOrderService.updateById(shopOrderObj);
                }
            }
        }else {
            stock.setSuccessFlag(Stock.IN_STOCK_FAILED);
            stock.setFailedReason(json);
            stockMapper.insert(stock);
        }

        return json;
    }

    /*
    *  返回值转xml并获取返回结果
    */
    public String getResult(String sxml){
        String s = sxml.replaceAll("\n"," ");
        System.out.println(s);
        StringBuilder sXML = new StringBuilder();
        String nodePath = "description";
        sXML.append(sxml);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document document = null;
        InputStream is = new ByteArrayInputStream(sXML.toString().getBytes(StandardCharsets.UTF_8));
        try {
            document = dbf.newDocumentBuilder().parse(is);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath path = xpfactory.newXPath();
        String servInitrBrch = "";
        try {
            servInitrBrch = path.evaluate(nodePath, document);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return servInitrBrch;
    }
}
