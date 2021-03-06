package com.itl.iap.report.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.report.api.dto.ShopOrderTrackDto;
import com.itl.iap.report.api.entity.Sfc;
import com.itl.iap.report.api.entity.ShopOrderTrack;
import com.itl.iap.report.api.service.ShopOrderTrackService;

import com.itl.iap.report.api.vo.OperationVo;
import com.itl.iap.report.api.vo.ShopOrderTrackVo;
import com.itl.iap.report.provider.mapper.SfcReportMapper;
import com.itl.iap.report.provider.mapper.ShopOrderTrackMapper;
import com.itl.iap.report.provider.util.ExcelUtils;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.RouterProcessTable;
import com.itl.mes.core.api.entity.SfcStateTrack;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ConnectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShopOrderTrackServiceImpl extends ServiceImpl<ShopOrderTrackMapper, ShopOrderTrack>  implements ShopOrderTrackService {
    @Autowired
    private ShopOrderTrackMapper shopOrderTrackMapper;

    @Autowired
    private SfcReportMapper reportMapper;

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static Map<String,String> operationInfoMap=new HashMap<>();  //key->??????BO???value->????????????

    private static Map<String, List<RouterProcessTable>> routerProcessTableMap=new HashMap<>(); // key->????????????BO???value->??????????????????

    @Override
    public ShopOrderTrackVo selectByCondition(ShopOrderTrackDto shopOrderTrackDto) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        ShopOrderTrackVo shopOrderTrackVo=new ShopOrderTrackVo();
        shopOrderTrackVo.setMaxNum(0);
        Set<String> shopOrders=new HashSet<>();
        IPage<ShopOrderTrack> page= shopOrderTrackMapper.selectByCondition(shopOrderTrackDto.getPage(),shopOrderTrackDto);
        shopOrderTrackVo.setCurrent(page.getCurrent());
        shopOrderTrackVo.setSize(page.getSize());
        shopOrderTrackVo.setTotal(page.getTotal());
        shopOrderTrackVo.setPages(page.getPages());
        List<Map<String,Object>> shorpOrderTrackList=new ArrayList<>();
        if(page !=null && page.getRecords() !=null && page.getRecords().size()>0){
            // ???????????????????????????????????????????????????
            page.getRecords().forEach(shopOrderTrack -> {
                shopOrders.add(shopOrderTrack.getShopOrder());
                Map<String,Object> map=new LinkedHashMap<>();
                // ?????????????????????????????????????????????sfc??????
                map.put("sfcNum",shopOrderTrackMapper.selectCountSfcByShopOrder(shopOrderTrack.getShopOrder()));
                // ????????????????????????????????????????????????????????????
                if(shopOrderTrack.getPlanStartDate() !=null){
                    shopOrderTrack.setStartDate(format.format(shopOrderTrack.getPlanStartDate()));
                }
                if(shopOrderTrack.getPlanEndDate() !=null){
                    shopOrderTrack.setEndDate(format.format(shopOrderTrack.getPlanEndDate()));
                }
                // ???????????????????????????
                getAllBaseInfo(map,shopOrderTrack);
                // ??????sfc???????????????????????????????????????
                String processInfo=shopOrderTrackMapper.selectProcessInfoBySfc(shopOrderTrack.getSfc());
                // ??????????????????
                String[] operationBos=shopOrderTrack.getOperationBo().split("\\|");
                if(operationBos.length==1){
                    // 1.????????????????????????
                    if(processInfo !=null && processInfo !=""){
                        int num= 0;
                        try {
                            num = analysisProcessInfoByOne(map,processInfo,shopOrderTrack);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(num>shopOrderTrackVo.getMaxNum()){
                            shopOrderTrackVo.setMaxNum(num);
                        }
                    }
                }else {
                    // 2.????????????
                    if(processInfo !=null && processInfo !=""){
                        int num= 0;
                        try {
                            num = analysisProcessInfoByMore(map,processInfo,shopOrderTrack);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(num>shopOrderTrackVo.getMaxNum()){
                            shopOrderTrackVo.setMaxNum(num);
                        }
                    }
                }
                shorpOrderTrackList.add(map);
            });
        }
        // ??????????????????????????????????????????????????????????????????
        IPage<ShopOrderTrack> pageWithOutSfc=shopOrderTrackMapper.selectFirstOperationByCondifition(shopOrderTrackDto.getPage(),shopOrderTrackDto);
        // ???????????????
        if(pageWithOutSfc !=null && pageWithOutSfc.getRecords() !=null && pageWithOutSfc.getRecords().size()>0){
            shopOrderTrackVo.setTotal(shopOrderTrackVo.getTotal()+pageWithOutSfc.getRecords().size());
            String total=shopOrderTrackVo.getTotal()+"";
            int size=(int)shopOrderTrackVo.getSize();
            // ????????????
            shopOrderTrackVo.setPages(new Double(Math.ceil(Double.parseDouble(total)/size)).longValue());
        }
        if(pageWithOutSfc !=null && pageWithOutSfc.getRecords() !=null && pageWithOutSfc.getRecords().size()>0){
            pageWithOutSfc.getRecords().forEach(shopOrderTrack -> {
                shopOrders.add(shopOrderTrack.getShopOrder());
                Map<String,Object> map=new LinkedHashMap<>();
                map.put("sfcNum",1);
                // ????????????????????????????????????????????????????????????
                if(shopOrderTrack.getPlanStartDate() !=null){
                    shopOrderTrack.setStartDate(sdf.format(shopOrderTrack.getPlanStartDate()));
                }
                if(shopOrderTrack.getPlanEndDate() !=null){
                    shopOrderTrack.setEndDate(sdf.format(shopOrderTrack.getPlanEndDate()));
                }
                // ???????????????????????????
                getAllBaseInfo(map,shopOrderTrack);
                map.put("sfc",shopOrderTrack.getDispatchCode());
                OperationVo operationVo=new OperationVo();
                operationVo.setName(shopOrderTrack.getOperationName());
                operationVo.setState("??????????????????");
                operationVo.setDate(sdf.format(shopOrderTrack.getCreateDate()));
                map.put("operationVo1",operationVo);
                if(shopOrderTrackVo.getMaxNum()==0){
                    shopOrderTrackVo.setMaxNum(1);
                }
                shorpOrderTrackList.add(map);
            });
        }
        List<Map<String,Object>> shorpOrderTrackListSorted=new ArrayList<>();
        if(shorpOrderTrackList !=null && shorpOrderTrackList.size()>0){
            shorpOrderTrackListSorted= shorpOrderTrackList.stream().sorted((o1, o2) -> {
                Date date1= null;
                try {
                    date1 = format.parse(o1.get("startDate").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2= null;
                try {
                    date2 = format.parse(o2.get("startDate").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return  Long.compare(date2.getTime(),date1.getTime());
            }).collect(Collectors.toList());
        }
        shopOrderTrackVo.setShopOrderTrackList(shorpOrderTrackListSorted);
        shopOrderTrackVo.setShopOrders(shopOrders);
        return shopOrderTrackVo;
    }

    /**
     * ????????????????????????????????????
     * @param shopOrderTrackDto
     * @param response
     * @throws CommonException
     */
    @Override
    public void export(ShopOrderTrackDto shopOrderTrackDto, HttpServletResponse response) throws CommonException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        int maxNum=0;
        shopOrderTrackDto.setPage(new Page());
        shopOrderTrackDto.getPage().setSize(10);
        shopOrderTrackDto.getPage().setCurrent(1);
        List<ShopOrderTrack> page= shopOrderTrackMapper.selectListByCondition(shopOrderTrackDto);
        List<Map<String, Object>> list = new ArrayList<>();
        if(page !=null && page.size()>0){
            for(ShopOrderTrack shopOrderTrack:page){
                Map<String, Object> map = new HashMap();
                Map<String,Object> operationMap=new HashMap<>();
                //?????????????????????
                getAllTableBaseInfo(map,shopOrderTrack);
                // ??????sfc???????????????????????????????????????
                String processInfo=shopOrderTrackMapper.selectProcessInfoBySfc(shopOrderTrack.getSfc());
                // ??????????????????
                String[] operationBos=shopOrderTrack.getOperationBo().split("\\|");
                int num= 0;
                if(operationBos.length==1){
                    // 1.????????????????????????
                    if(processInfo !=null && processInfo !=""){
                        try {
                            num = analysisProcessInfoByOne(operationMap,processInfo,shopOrderTrack);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(num>maxNum){
                            maxNum=num;
                        }
                    }
                }else {
                    // 2.????????????
                    if(processInfo !=null && processInfo !=""){
                        try {
                            num = analysisProcessInfoByMore(operationMap,processInfo,shopOrderTrack);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(num>maxNum){
                            maxNum=num;
                        }
                    }
                }
                oderOperation(map,operationMap,num);
                list.add(map);
            }
        }
        /*??????????????????????????????excel?????????*/
        List<String> listHead=getTableHead();
        String[] title = new String[listHead.size()+maxNum];
        for(int i=0;i<listHead.size();i++){
            title[i]=listHead.get(i);
        }
        for(int j=1;j<=maxNum;j++){
            title[listHead.size()+j-1]="??????"+j;
        }
        Map<String/*?????????key?????????sheet??????????????????excel??????????????????sheet???*/, List<Map<String/*??????key????????????????????????*/, Object>>/*???list?????????sheet????????????*/> map = new HashMap<>();
        map.put("?????????????????????", list);


//        String[] title = {"id","??????","??????","?????????","????????????"};
//        List<Map<String, String>> list =new ArrayList();
//        /*??????????????????????????????????????????list???map???key????????????????????????????????????*/
//        for(int i = 0; i<10; i++){
//            HashMap<String, String> map = com.google.common.collect.Maps.newHashMap();
//            if(i > 5){
//                if(i<7){
//                    map.put("id","333");
//                    map.put("??????","mmmm");
//                }else {
//                    map.put("id","333");
//                    map.put("??????","aaaaa");
//                }
//            }else if (i >3){
//                map.put("id","222");
//                map.put("??????","????????????");
//            }else if (i>1 && i<3){
//                map.put("id","222");
//                map.put("??????","hhhhhhhh");
//            }else {
//                map.put("id","222");
//                map.put("??????","bbbb");
//            }
//            map.put("??????","sssssss");
//            map.put("?????????","vvvvv");
//            map.put("????????????","2017-02-27 11:20:26");
//            list.add(map);
//        }
//        Map<String/*?????????key?????????sheet??????????????????excel??????????????????sheet???*/, List<Map<String/*??????key????????????????????????*/, String>>/*???list?????????sheet????????????*/> map = new HashMap();
//        map.put("??????????????????", list);
     ExcelUtils.createExcel(title, map, new int[]{1},response,11,maxNum);

    }

    // ????????????????????????????????????map???
    public void getAllBaseInfo(Map<String,Object> map,ShopOrderTrack shopOrderTrack){
        map.put("shopOrder",shopOrderTrack.getShopOrder());
        map.put("orderQty",shopOrderTrack.getOrderQty());
        map.put("startDate",shopOrderTrack.getStartDate());
        map.put("endDate",shopOrderTrack.getEndDate());
        map.put("customerOrder",shopOrderTrack.getCustomerOrder());
        map.put("item",shopOrderTrack.getItem());
        map.put("itemName",shopOrderTrack.getItemName());
        map.put("itemDesc",shopOrderTrack.getItemDesc());
        map.put("sfc",shopOrderTrack.getSfc());
        map.put("state",shopOrderTrack.getState());
        map.put("sfcQty",shopOrderTrack.getSfcQty());
    }
    // ?????????????????? ??????????????????
    public int analysisProcessInfoByOne(Map<String,Object> map,String processInfo,ShopOrderTrack shopOrderTrack) throws ParseException {
        JSONObject processInfoObject= JSON.parseObject(processInfo);
        Map<String,JSONObject> nodeMap=new HashMap<>();
        int operationNum=1;//????????????
        if(processInfoObject.get("nodeList")!=null && processInfoObject.get("lineList") !=null){
            JSONArray nodeList=JSONArray.parseArray(processInfoObject.get("nodeList").toString());
            // nodeList ???map
            String startId=listToMap(nodeMap,nodeList);
            if(processInfoObject.get("lineList") !=null){
                JSONArray lineList=JSONArray.parseArray(processInfoObject.get("lineList").toString());
                Map<String,List<JSONObject>> lineMap=new HashMap<>();
                // lineList ???map
                for(Object obj:lineList){
                    JSONObject jsonObject=JSON.parseObject(obj.toString());
                    List<JSONObject> jsonObjectList=null;
                    if(lineMap.get(jsonObject.get("from").toString())==null){
                        // ?????????????????????
                        jsonObjectList=new ArrayList<>();
                    }else{
                        // ??????????????????
                        jsonObjectList=lineMap.get(jsonObject.get("from").toString());
                    }
                    jsonObjectList.add(jsonObject);
                    lineMap.put(jsonObject.get("from").toString(),jsonObjectList);
                }
                List<JSONObject> lineObjectList=lineMap.get(startId);
                int isFinshed=1; //0????????????1?????????
                OperationVo operationLastVo=new OperationVo();// ???????????????
                // ??????lineMap
                while(true){
                    // ?????????????????????
                    if(lineObjectList ==null){
                        break;
                    }
                    if(lineObjectList.size()==1){
                        // ????????????????????????
                        JSONObject operationObj=nodeMap.get(lineObjectList.get(0).get("to").toString());
                        if(operationObj !=null && operationObj.get("operation") !=null){
                            OperationVo operationVo=new OperationVo();
                            if(operationObj.get("operation").toString().equals(shopOrderTrack.getOperationBo())){
                                // ????????????
                                isFinshed=0;
                                if(shopOrderTrack.getState().equals("?????????") || shopOrderTrack.getState().equals("??????")){
                                    // ??????????????????????????????,???me_sfc_wip_log???sfc?????????????????????out???time
                                    String maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),shopOrderTrack.getOperationBo());
                                    if(maxTime !=null){
                                        operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                                    }
                                }else if(shopOrderTrack.getState().equals("??????")){
                                    // ???????????????sfc???????????????
                                    if(shopOrderTrack.getCreateDate() !=null){
                                        operationVo.setDate(sdf.format(shopOrderTrack.getCreateDate()));
                                    }
                                }else if(shopOrderTrack.getState().equals("?????????")){
                                    // ????????????????????????
                                    //???????????????
                                    Date inTime = shopOrderTrackMapper.getInTimeBySfcAndOperation(shopOrderTrack.getSfc(), shopOrderTrack.getOperationBo());
                                    if (ObjectUtil.isNotEmpty(inTime)){
                                        operationVo.setDate(sdf.format(inTime));
                                    }
                                }else{
                                    // ????????????????????????????????????????????????
                                    operationVo.setDate(operationLastVo.getDate());
                                }
                                operationVo.setName(operationObj.get("name").toString());
                                operationVo.setState(shopOrderTrack.getState());
                                map.put("operationVo"+operationNum,operationVo);
                                operationNum++;
                            }else {
                                // ???????????????
                                // ???????????????????????????????????????????????????????????????
                                if(isFinshed==1){
                                    String maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),operationObj.get("operation").toString());
                                    if(maxTime !=null){
                                        operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                                    }
                                    operationVo.setState("?????????");
                                }
                                operationVo.setName(operationObj.get("name").toString());
                                map.put("operationVo"+operationNum,operationVo);
                                operationNum++;
                            }
                            // ?????????????????????????????????????????????????????????
                            BeanUtil.copyProperties(operationVo, operationLastVo);
                        }
                    }else{
                        // ????????????
                        // ????????????????????????????????????????????????
                        boolean isExist=false;
                        if(lineObjectList !=null){
                            for(int i=0;i<lineObjectList.size();i++){
                                JSONObject operationObj=nodeMap.get(lineObjectList.get(i).get("to").toString());
                                JSONObject operationOtherObj=new JSONObject();
                                if(operationObj.get("operation") !=null){
                                    // ????????????????????????
                                    if(operationObj.get("operation").toString().equals(shopOrderTrack.getOperationBo())){
                                        isExist=true;
                                        isFinshed=0;
                                        // ???????????????????????????
                                        // 1.????????????????????????????????????me_sfc_wip_log???????????????????????????????????????????????????
                                        String maxTime=null;
                                        if(i==0){
                                            operationOtherObj=nodeMap.get(lineObjectList.get(1).get("to").toString());
                                            maxTime = shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(), shopOrderTrack.getSfc(),operationOtherObj.get("operation").toString());
                                        }else{
                                            operationOtherObj=nodeMap.get(lineObjectList.get(0).get("to").toString());
                                            maxTime = shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(), shopOrderTrack.getSfc(),operationOtherObj.get("operation").toString());
                                        }
                                        if(maxTime!=null){
                                            // ?????????????????????
                                            OperationVo operationVo=new OperationVo();
                                            operationVo.setName(operationOtherObj.get("name").toString());
                                            /*operationVo.setDate(maxTime.replaceAll("-", ""));*/
                                            operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                                            operationVo.setState("?????????");
                                            map.put("operationVo"+operationNum,operationVo);
                                            operationNum++;
                                        }else {
                                            // ?????????????????????
                                            OperationVo operationNowVo=new OperationVo();// ????????????
                                            operationNowVo.setName(operationObj.get("name").toString());
                                            if(shopOrderTrack.getState().equals("?????????") || shopOrderTrack.getState().equals("??????")){
                                                // ??????????????????????????????,???me_sfc_wip_log???sfc?????????????????????out???time
                                                maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),shopOrderTrack.getOperationBo());
                                                /*operationNowVo.setDate(maxTime.replaceAll("-",""));*/
                                                operationNowVo.setDate(sdf.format(sdf.parse(maxTime)));
                                            }else if(shopOrderTrack.getState().equals("??????")){
                                                // ???????????????sfc???????????????
                                                operationNowVo.setDate(sdf.format(shopOrderTrack.getCreateDate()));
                                            }else if(shopOrderTrack.getState().equals("?????????")){
                                                // ????????????????????????
                                                operationNowVo.setDate(sdf.format(shopOrderTrack.getInTime()));
                                            }else{
                                                // ????????????????????????????????????????????????
                                                operationNowVo.setDate(operationLastVo.getDate());
                                            }
                                            operationNowVo.setState(shopOrderTrack.getState());
                                            map.put("operationVo"+operationNum,operationNowVo);
                                            operationNum++;
                                        }
                                    }
                                }
                            }
                            if(!isExist){
                                // ????????????isFinshed
                                List<OperationVo> list=new ArrayList<>();
                                for(int i=0;i<lineObjectList.size();i++) {
                                    OperationVo operationVo = new OperationVo();
                                    JSONObject operationObj=nodeMap.get(lineObjectList.get(i).get("to").toString());
                                    if (isFinshed == 1) {
                                        // ??????????????????????????????,???me_sfc_wip_log???sfc?????????????????????out???time
                                        String maxTime = shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(), shopOrderTrack.getSfc(),operationObj.get("operation").toString());
                                        Date time=shopOrderTrackMapper.selectMaxAllTime(shopOrderTrack.getBo(), shopOrderTrack.getSfc(),operationObj.get("operation").toString());
                                        if(maxTime !=null){
                                            /*operationVo.setDate(maxTime.replaceAll("-", ""));*/
                                            operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                                        }
                                        operationVo.setState("?????????");
                                        operationVo.setTime(time);
                                    }
                                    operationVo.setName(operationObj.get("name").toString());
                                    operationVo.setTo(operationObj.get("id").toString());
                                    list.add(operationVo);
                                }
                                List<OperationVo> operationVoList=list.stream().sorted((o1,o2) -> {
                                    long time1=0;
                                    long time2=0;
                                    Date dt1= null;
                                    try {
                                        if(o1.getTime() !=null){
                                            dt1 = sdf.parse(o1.getDate());
                                            time1=dt1.getTime();
                                        }else {
                                            time1=0;
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date dt2= null;
                                    try {
                                        if(o2.getTime() !=null){
                                            dt2 = sdf.parse(o2.getDate());
                                            time2=dt2.getTime();
                                        }else {
                                            time2=0;
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return  Long.compare(time1,time2);
                                }).collect(Collectors.toList());

                                for(OperationVo operationVo:operationVoList){
                                    map.put("operationVo"+operationNum,operationVo);
                                    operationNum++;
                                }
                                // ?????????????????????????????????,????????????????????????
                                String first=operationVoList.get(0).getTo();
                                lineObjectList=lineMap.get(lineObjectList.get(lineObjectList.size()-1).get("to").toString());
                                //JSONObject operationObj=nodeMap.get(lineObjectList.get(0).get("to").toString());
                            }
                        }
                    }
                    // ???????????????
                    if(lineMap.get(lineObjectList.get(lineObjectList.size()-1).get("to").toString())==null){
                        break;
                    }
                    lineObjectList=lineMap.get(lineObjectList.get(lineObjectList.size()-1).get("to").toString());
                }
            }
        }
        return operationNum-1;
    }
    // ?????????????????????????????????
    public int analysisProcessInfoByMore(Map<String,Object> map,String processInfo,ShopOrderTrack shopOrderTrack) throws ParseException {
        JSONObject processInfoObject= JSON.parseObject(processInfo);
        Map<String,JSONObject> nodeMap=new HashMap<>();
        int operationNum=1;//????????????
        if(processInfoObject.get("nodeList")!=null) {
            JSONArray nodeList = JSONArray.parseArray(processInfoObject.get("nodeList").toString());
            // nodeList ???map
            String startId=listToMap(nodeMap,nodeList);
            if(processInfoObject.get("lineList") !=null){
                JSONArray lineList=JSONArray.parseArray(processInfoObject.get("lineList").toString());
                Map<String,JSONObject> lineMap=new HashMap<>();
                // lineList ???map
                for(Object obj:lineList){
                    JSONObject jsonObject=JSON.parseObject(obj.toString());
                    lineMap.put(jsonObject.get("from").toString(),jsonObject);
                }
                JSONObject lineObject=lineMap.get(startId);
                int isFinshed=1; //0????????????1?????????
                OperationVo operationLastVo=new OperationVo();// ???????????????
                String[] operationBos=shopOrderTrack.getOperationBo().split("\\|");

                while(true){
                    JSONObject operationObj=nodeMap.get(lineObject.get("to").toString());
                    for(int i=0;i<operationBos.length;i++){
                        // ?????????????????????????????????????????????
                        if(operationObj.get("operation").toString().equals(operationBos[i])){
                            isFinshed=0;
                            OperationVo operationVo=new OperationVo();
                            operationVo.setName(operationObj.get("name").toString());
                            operationVo.setDate(operationLastVo.getDate());
                            operationVo.setState("?????????");
                            map.put("operationVo"+operationNum,operationVo);
                            operationNum++;
                            // ?????????????????????????????????????????????
                            lineObject=lineMap.get(lineObject.get("to").toString());
                            operationObj=nodeMap.get(lineObject.get("to").toString());
                            OperationVo operationVo1=new OperationVo();
                            operationVo1.setName(operationObj.get("name").toString());
                            operationVo1.setDate(operationLastVo.getDate());
                            operationVo1.setState("?????????");
                            map.put("operationVo"+operationNum,operationVo1);
                            operationNum++;
                            lineObject=lineMap.get(lineObject.get("to").toString());
                        }
                    }
                    if(isFinshed==1){
                        String maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),operationObj.get("operation").toString());
                        OperationVo operationVo=new OperationVo();
                        operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                        operationVo.setState("?????????");
                        operationVo.setName(operationObj.get("name").toString());
                        map.put("operationVo"+operationNum,operationVo);
                        operationNum++;
                        // ?????????????????????????????????????????????????????????
                        BeanUtil.copyProperties(operationVo, operationLastVo);
                    }else {
                        operationObj=nodeMap.get(lineObject.get("to").toString());
                        OperationVo operationVo=new OperationVo();
                        operationVo.setName(operationObj.get("name").toString());
                        map.put("operationVo"+operationNum,operationVo);
                        operationNum++;
                    }
                    // ???????????????
                    if(lineMap.get(lineObject.get("to").toString())==null){
                        break;
                    }
                    lineObject=lineMap.get(lineObject.get("to").toString());
                }
            }
        }
        return operationNum-1;
    }
    public String listToMap(Map<String,JSONObject> nodeMap,JSONArray nodeList){
        String startId=null;
        if(nodeList !=null && nodeList.size()>0){
            for(Object obj:nodeList){
                JSONObject jsonObject=JSON.parseObject(obj.toString());
                if(jsonObject.get("name").toString().equals("????????????")){
                    startId=jsonObject.get("id").toString();
                }
                // key???id
                nodeMap.put(jsonObject.get("id").toString(),jsonObject);
            }
        }
        return startId;
    }

    // ?????????????????????
    public List<String> getTableHead(){
        List<String> list=new ArrayList<>();
        list.add("????????????");
        list.add("????????????");
        list.add("????????????");
        list.add("????????????");
        list.add("????????????");
        list.add("????????????");
        list.add("??????????????????");
        list.add("??????????????????");
        list.add("SFC??????");
        list.add("SFC??????");
        list.add("SFC??????");
        return list;
    }

    // ???????????????????????????????????????map???
    public void getAllTableBaseInfo(Map<String,Object> map,ShopOrderTrack shopOrderTrack){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        map.put("????????????",shopOrderTrack.getShopOrder());
        map.put("????????????",shopOrderTrack.getOrderQty()+"");
        map.put("??????????????????",simpleDateFormat.format(shopOrderTrack.getPlanStartDate()));
        map.put("??????????????????",simpleDateFormat.format(shopOrderTrack.getPlanEndDate()));
        map.put("????????????",shopOrderTrack.getCustomerOrder());
        map.put("????????????",shopOrderTrack.getItem());
        map.put("????????????",shopOrderTrack.getItemName());
        map.put("????????????",shopOrderTrack.getItemDesc());
        map.put("SFC??????",shopOrderTrack.getSfc());
        map.put("SFC??????",shopOrderTrack.getState());
        map.put("SFC??????",shopOrderTrack.getSfcQty()+"");
    }

    // ???operationMap?????????????????????????????????map???
    public void oderOperation(Map<String,Object> map,Map<String,Object> operationMap,int num){
        for(int i=1;i<=num;i++){
            OperationVo operationVo= (OperationVo) operationMap.get("operationVo"+i);
            String operationName=operationVo.getName()==null?"":operationVo.getName();
            String operationState=operationVo.getState()==null?"":operationVo.getState();
            String date=operationVo.getDate()==null?"":operationVo.getDate();
            String operationInfo=operationName+"\r\n"+operationState+"\r\n"+date;
            map.put("??????"+i,operationInfo);
        }
    }

    /**
     * ?????????????????????????????????????????????
     * @param shopOrderTrackDto
     * @param response
     * @throws CommonException
     */
    public void exportInfo(ShopOrderTrackDto shopOrderTrackDto, HttpServletResponse response) throws CommonException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int maxNum=0;
        shopOrderTrackDto.setPage(new Page());
        shopOrderTrackDto.getPage().setSize(10);
        shopOrderTrackDto.getPage().setCurrent(1);
        List<ShopOrderTrack> page= shopOrderTrackMapper.selectListByCondition(shopOrderTrackDto);
        List<Map<String, Object>> list = new ArrayList<>();
        if(page !=null && page.size()>0) {
            for (ShopOrderTrack shopOrderTrack : page) {
                Map<String, Object> map = new HashMap();
                //?????????????????????
                getAllTableBaseInfo(map, shopOrderTrack);
                Sfc sfc=reportMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc",shopOrderTrack.getSfc()));
                if(sfc !=null && sfc.getSfcRouterBo() !=null){
                    List<RouterProcessTable> routerProcessTableList=null;
                    if(routerProcessTableMap.get(sfc.getSfcRouterBo()) !=null){
                        routerProcessTableList=routerProcessTableMap.get(sfc.getSfcRouterBo());
                    }else{
                        routerProcessTableList=shopOrderTrackMapper.selectOperationListInfo(sfc.getSfcRouterBo());
                        if(routerProcessTableList!=null && routerProcessTableList.size()>0){
                            routerProcessTableMap.put(sfc.getSfcRouterBo(),routerProcessTableList);
                        }
                    }
                    if(routerProcessTableList!=null && routerProcessTableList.size()>0){
                        if(routerProcessTableList.size()>maxNum){
                            maxNum=routerProcessTableList.size();
                        }
                        int index=1;
//                        for(int i=0;i<routerProcessTableList.size();i++){
//                            if(routerProcessTableList.get(i).getParallelFlag()==0){
//                                // ?????????
//                                String operationInfo=null;
//                                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
//                                if(sfcStateTrack !=null){
//                                    String operationDate=null;
//                                    if(sfcStateTrack.getUpdateDate()!=null){
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                    }
//                                    operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                }else {
//                                    // ??????????????????????????? ????????? ????????????
//                                    //????????????
//                                    //??????sfc??????????????????????????????
//                                    String maxTime=null;
//                                    String state=null;
//                                    if("?????????".equals(sfc.getState())){
//                                        maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),routerProcessTableList.get(i).getOperationBo());
//                                        if(maxTime !=null){
//                                            try {
//                                                maxTime=sdf.format(sdf.parse(maxTime));
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                        state="?????????";
//
//                                    }
//                                   operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                }
//                                map.put("??????"+index,operationInfo);
//                                index++;
//                            }else if(routerProcessTableList.get(i).getParallelFlag()!=0){
//                                // ????????????,i+1??????????????????
//                                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
//                                SfcStateTrack lastSfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i+1).getOperationBo());
//                                i++;//????????????????????????????????????
//                                String operationInfo=null;
//                                if(sfcStateTrack==null && lastSfcStateTrack==null){
//                                    // ????????????????????????????????????????????????
//                                    String maxTime=null;
//                                    String state=null;
//                                    operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    map.put("??????"+index,operationInfo);
//                                    index++;
//                                    operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
//                                    map.put("??????"+index,operationInfo);
//                                    index++;
//                                }else if(sfcStateTrack==null && lastSfcStateTrack!=null ){
//                                    // ????????????????????????????????????
//                                    String maxTime=null;
//                                    String state=null;
//                                    String operationDate=null;
//                                    if(lastSfcStateTrack.getUpdateDate()!=null){
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                    }
//                                    operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                    map.put("??????"+index,operationInfo);
//                                    index++;
//                                    maxTime=null;
//                                    state=null;
//                                    operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    map.put("??????"+index,operationInfo);
//                                    index++;
//                                }else if(sfcStateTrack!=null && lastSfcStateTrack==null){
//                                    // ????????????????????????????????????
//                                    String maxTime=null;
//                                    String state=null;
//                                    String operationDate=null;
//                                    if(sfcStateTrack.getUpdateDate()!=null){
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                    }
//                                    operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                    map.put("??????"+index,operationInfo);
//                                    index++;
//                                    maxTime=null;
//                                    state=null;
//                                    operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
//                                    map.put("??????"+index,operationInfo);
//                                    index++;
//                                }else if(sfcStateTrack!=null && lastSfcStateTrack!=null){
//                                    if(sfcStateTrack.getUpdateDate().getTime()>lastSfcStateTrack.getUpdateDate().getTime()){
//                                        String operationDate=null;
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                        operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        map.put("??????"+index,operationInfo);
//                                        index++;
//                                        operationDate=null;
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                        operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        map.put("??????"+index,operationInfo);
//                                        index++;
//                                    }else {
//                                        String operationDate=null;
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                        operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        map.put("??????"+index,operationInfo);
//                                        index++;
//                                        operationDate=null;
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                        operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        map.put("??????"+index,operationInfo);
//                                        index++;
//                                    }
//                                }
//                            }
//                        }
                        getInfo(routerProcessTableList,sfc,shopOrderTrack,index,map,0,"");
                    }
                }
                list.add(map);
            }
        }

        /*??????????????????????????????excel?????????*/
        List<String> listHead=getTableHead();
        String[] title = new String[listHead.size()+maxNum];
        for(int i=0;i<listHead.size();i++){
            title[i]=listHead.get(i);
        }
        for(int j=1;j<=maxNum;j++){
            title[listHead.size()+j-1]="??????"+j;
        }
        Map<String/*?????????key?????????sheet??????????????????excel??????????????????sheet???*/, List<Map<String/*??????key????????????????????????*/, Object>>/*???list?????????sheet????????????*/> map = new HashMap<>();
        map.put("?????????????????????", list);
        ExcelUtils.createExcel(title, map, new int[]{1},response,11,maxNum);
    }


    public ShopOrderTrackVo selectInfoByCondition(ShopOrderTrackDto shopOrderTrackDto) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ShopOrderTrackVo shopOrderTrackVo=new ShopOrderTrackVo();
        shopOrderTrackVo.setMaxNum(0);
        Set<String> shopOrders=new HashSet<>();
        IPage<ShopOrderTrack> page= shopOrderTrackMapper.selectByCondition(shopOrderTrackDto.getPage(),shopOrderTrackDto);
        shopOrderTrackVo.setCurrent(page.getCurrent());
        shopOrderTrackVo.setSize(page.getSize());
        shopOrderTrackVo.setTotal(page.getTotal());
        shopOrderTrackVo.setPages(page.getPages());
        List<Map<String,Object>> shorpOrderTrackList=new ArrayList<>();
        if(page !=null && page.getRecords() !=null && page.getRecords().size()>0){
            // ???????????????????????????????????????????????????
            page.getRecords().forEach(shopOrderTrack -> {
                shopOrders.add(shopOrderTrack.getShopOrder());
                Map<String,Object> map=new LinkedHashMap<>();
                // ?????????????????????????????????????????????sfc??????
                map.put("sfcNum",shopOrderTrackMapper.selectCountSfcByShopOrder(shopOrderTrack.getShopOrder()));
                // ????????????????????????????????????????????????????????????
                if(shopOrderTrack.getPlanStartDate() !=null){
                    shopOrderTrack.setStartDate(format.format(shopOrderTrack.getPlanStartDate()));
                }
                if(shopOrderTrack.getPlanEndDate() !=null){
                    shopOrderTrack.setEndDate(format.format(shopOrderTrack.getPlanEndDate()));
                }
                // ???????????????????????????
                getAllBaseInfo(map,shopOrderTrack);
                //????????????????????????
                Sfc sfc=reportMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc",shopOrderTrack.getSfc()));
                if(sfc !=null && sfc.getSfcRouterBo() !=null){
                    List<RouterProcessTable> routerProcessTableList=null;
                    if(routerProcessTableMap.get(sfc.getSfcRouterBo()) !=null){
                        routerProcessTableList=routerProcessTableMap.get(sfc.getSfcRouterBo());
                    }else{
                        routerProcessTableList=shopOrderTrackMapper.selectOperationListInfo(sfc.getSfcRouterBo());
                        if(routerProcessTableList!=null && routerProcessTableList.size()>0){
                            routerProcessTableMap.put(sfc.getSfcRouterBo(),routerProcessTableList);
                        }
                    }
                    if(routerProcessTableList!=null && routerProcessTableList.size()>0){
                        if(routerProcessTableList.size()>shopOrderTrackVo.getMaxNum()){
                            shopOrderTrackVo.setMaxNum(routerProcessTableList.size());
                        }
                        int index=1;
//                        for(int i=0;i<routerProcessTableList.size();i++){
//                            if(routerProcessTableList.get(i).getParallelFlag()==0){
//                                // ?????????
//                                String operationInfo=null;
//                                OperationVo operationVo=new OperationVo();
//                                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
//                                if(sfcStateTrack !=null){
//                                    String operationDate=null;
//                                    if(sfcStateTrack.getUpdateDate()!=null){
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                    }
//                                    //operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                    operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                }else {
//                                    // ??????????????????????????? ????????? ????????????
//                                    //????????????
//                                    //??????sfc??????????????????????????????
//                                    String maxTime=null;
//                                    String state=null;
//                                    if("?????????".equals(sfc.getState())){
//                                        maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),routerProcessTableList.get(i).getOperationBo());
//                                        if(maxTime !=null){
//                                            try {
//                                                maxTime=sdf.format(sdf.parse(maxTime));
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                        state="?????????";
//                                    }
//                                   // operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                }
//                                map.put("operationVo"+index,operationVo);
//                                index++;
//                            }else if(routerProcessTableList.get(i).getParallelFlag()!=0){
//                                // ????????????,i+1??????????????????
//                                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
//                                SfcStateTrack lastSfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i+1).getOperationBo());
//                                i++;//????????????????????????????????????
//                                //String operationInfo=null;
//                                OperationVo operationVo=new OperationVo();
//                                if(sfcStateTrack==null && lastSfcStateTrack==null){
//                                    // ????????????????????????????????????????????????
//                                    String maxTime=null;
//                                    String state=null;
//                                    //operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    map.put("operationVo"+index,operationVo);
//                                    index++;
//                                    //operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
//                                    operationVo=generateOperationVoInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
//                                    map.put("operationVo"+index,operationVo);
//                                    index++;
//                                }else if(sfcStateTrack==null && lastSfcStateTrack!=null ){
//                                    // ????????????????????????????????????
//                                    String maxTime=null;
//                                    String state=null;
//                                    String operationDate=null;
//                                    if(lastSfcStateTrack.getUpdateDate()!=null){
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                    }
//                                    //operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                    operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                    map.put("operationVo"+index,operationVo);
//                                    index++;
//                                    maxTime=null;
//                                    state=null;
//                                    //operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    map.put("operationVo"+index,operationVo);
//                                    index++;
//                                }else if(sfcStateTrack!=null && lastSfcStateTrack==null){
//                                    // ????????????????????????????????????
//                                    String maxTime=null;
//                                    String state=null;
//                                    String operationDate=null;
//                                    if(sfcStateTrack.getUpdateDate()!=null){
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                    }
//                                    //operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                    operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                    map.put("operationVo"+index,operationVo);
//                                    index++;
//                                    maxTime=null;
//                                    state=null;
//                                    //operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
//                                    operationVo=generateOperationVoInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
//                                    map.put("operationVo"+index,operationVo);
//                                    index++;
//                                }else if(sfcStateTrack!=null && lastSfcStateTrack!=null){
//                                    if(sfcStateTrack.getUpdateDate().getTime()>lastSfcStateTrack.getUpdateDate().getTime()){
//                                        String operationDate=null;
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                        //operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        map.put("??????"+index,operationVo);
//                                        index++;
//                                        operationDate=null;
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                        //operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        map.put("??????"+index,operationVo);
//                                        index++;
//                                    }else {
//                                        String operationDate=null;
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                        //operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        map.put("??????"+index,operationVo);
//                                        index++;
//                                        operationDate=null;
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                        //operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        map.put("??????"+index,operationVo);
//                                        index++;
//                                    }
//                                }
//                            }
//                        }
                        getInfo(routerProcessTableList,sfc,shopOrderTrack,index,map,1,shopOrderTrackDto.getActionAcope());
                    }
                }
                shorpOrderTrackList.add(map);
            });

        }
        // ??????????????????????????????????????????????????????????????????
        IPage<ShopOrderTrack> pageWithOutSfc=shopOrderTrackMapper.selectFirstOperationByCondifition(shopOrderTrackDto.getPage(),shopOrderTrackDto);
        // ???????????????
        if(pageWithOutSfc !=null && pageWithOutSfc.getRecords() !=null && pageWithOutSfc.getRecords().size()>0){
            shopOrderTrackVo.setTotal(shopOrderTrackVo.getTotal()+pageWithOutSfc.getRecords().size());
            String total=shopOrderTrackVo.getTotal()+"";
            int size=(int)shopOrderTrackVo.getSize();
            // ????????????
            shopOrderTrackVo.setPages(new Double(Math.ceil(Double.parseDouble(total)/size)).longValue());

        }
        if(pageWithOutSfc !=null && pageWithOutSfc.getRecords() !=null && pageWithOutSfc.getRecords().size()>0){
            pageWithOutSfc.getRecords().forEach(shopOrderTrack -> {
                shopOrders.add(shopOrderTrack.getShopOrder());
                Map<String,Object> map=new LinkedHashMap<>();
                map.put("sfcNum",1);
                map.put("sfc",shopOrderTrack.getDispatchCode());
                // ???????????????????????????
                getAllBaseInfo(map,shopOrderTrack);
                map.put("sfc",shopOrderTrack.getDispatchCode());
                OperationVo operationVo=new OperationVo();
                operationVo.setName(shopOrderTrack.getOperationName());
                operationVo.setState("??????????????????");
                operationVo.setDate(sdf.format(shopOrderTrack.getCreateDate()));
                map.put("operationVo1",operationVo);
                if(shopOrderTrackVo.getMaxNum()==0){
                    shopOrderTrackVo.setMaxNum(1);
                }
                shorpOrderTrackList.add(map);
            });
        }
        List<Map<String,Object>> shorpOrderTrackListSorted=new ArrayList<>();
//        if(shorpOrderTrackList !=null && shorpOrderTrackList.size()>0){
//            shorpOrderTrackListSorted= shorpOrderTrackList.stream().sorted((o1, o2) -> {
//                Date date1= null;
//                try {
//                    date1 = format.parse(o1.get("startDate").toString());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Date date2= null;
//                try {
//                    date2 = format.parse(o2.get("startDate").toString());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                return  Long.compare(date2.getTime(),date1.getTime());
//            }).collect(Collectors.toList());
//        }
        shorpOrderTrackListSorted=shorpOrderTrackListSorted(shorpOrderTrackList);
        shopOrderTrackVo.setShopOrderTrackList(shorpOrderTrackListSorted);
        shopOrderTrackVo.setShopOrders(shopOrders);
        return shopOrderTrackVo;
    }

    /**
     * ??????????????????
     * @param operationBo
     * @param state
     * @param operationDate
     * @return
     */
    public String generateOperationInfo(String operationBo,String state,String operationDate){
        String operationName=null;
        if(operationInfoMap.get(operationBo) !=null){
            operationName=operationInfoMap.get(operationBo);
        }else{
            operationName=shopOrderTrackMapper.selectNameByOperationBo(operationBo);
            if(operationName !=null){
                operationInfoMap.put(operationBo,operationName);
            }
        }
        operationName=operationName==null?"":operationName;
        String operationState=state==null?"":state;
        String date=operationDate==null?"":operationDate;
        String operationInfo=operationName+"\r\n"+operationState+"\r\n"+date;
        return operationInfo;
    }

    /**
     *  ????????????????????????
     * @param operationBo
     * @param state
     * @param operationDate
     * @return
     */
    public OperationVo generateOperationVoInfo(String operationBo,String state,String operationDate,BigDecimal completeProportion){
        OperationVo operationVo=new OperationVo();
        String operationName=null;
        if(operationInfoMap.get(operationBo) !=null){
            operationName=operationInfoMap.get(operationBo);
        }else{
            operationName=shopOrderTrackMapper.selectNameByOperationBo(operationBo);
            if(operationName !=null){
                operationInfoMap.put(operationBo,operationName);
            }
        }
        operationVo.setName(operationName);
        operationVo.setState(state);
        operationVo.setDate(operationDate);
        operationVo.setCompleteProportion(completeProportion);
        return operationVo;
    }

    /**
     * ????????????
     * @param state
     * @return
     */
    public String checkState(String state){
        String operationState=null;
        if(state.equals("3")){
            operationState="?????????";
        }else if(state.equals("4")){
            operationState="?????????";
        }else{
            operationState="?????????";
        }
        return operationState;
    }

    /**
     *
     * @param routerProcessTableList
     * @param sfc
     * @param shopOrderTrack
     * @param index
     * @param map
     * @param type   0?????????1??????
     */
    public void getInfo(List<RouterProcessTable> routerProcessTableList,Sfc sfc,
                        ShopOrderTrack shopOrderTrack,int index,Map<String,Object> map,int type,String actionAcope){
        for(int i=0;i<routerProcessTableList.size();i++){
            if(routerProcessTableList.get(i).getParallelFlag()==0){
                // ?????????
                String operationInfo=null;
                OperationVo operationVo=new OperationVo();
                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
                BigDecimal complete_proportion = new BigDecimal("0.00");
                //???????????????????????????
                if(actionAcope.equals("1") && sfcStateTrack!=null){
                    List<HashMap<String, BigDecimal>> data=shopOrderTrackMapper.selectNumberProgress(sfcStateTrack.getSfc(),sfcStateTrack.getOperationBo());
                    if(!CollectionUtils.isEmpty(data)){
                        //?????????
                        BigDecimal sfc_qty = data.get(0).get("SFC_QTY");
                        //???????????????
                        BigDecimal complete_num = new BigDecimal("0.00");
                        for (HashMap<String, BigDecimal> datum : data) {
                            complete_num = complete_num.add(datum.get("DONE_QTY")).add(datum.get("SCRAP_QTY"));
                        }
                        //??????0????????????
                        if(complete_num.compareTo(BigDecimal.ZERO)>0){
                            complete_proportion=complete_num.divide(sfc_qty,2,BigDecimal.ROUND_HALF_UP);
                        }
                    }
                }
                if(sfcStateTrack !=null){
                    String operationDate=null;
                    if(sfcStateTrack.getUpdateDate()!=null){
                        operationDate=sdf.format(sfcStateTrack.getUpdateDate());
                    }
                    if(type==0){
                        operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
                    }else{
                        operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate,complete_proportion);
                    }
                }else {
                    // ??????????????????????????? ????????? ????????????
                    //????????????
                    //??????sfc??????????????????????????????
                    String maxTime=null;
                    String state=null;
                    //???????????????sfc
                    if("?????????".equals(sfc.getState())){
                        maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),routerProcessTableList.get(i).getOperationBo());
                        if(maxTime !=null){
                            try {
                                maxTime=sdf.format(sdf.parse(maxTime));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        state="?????????";
                    }
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
                    }else{
                        operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime,complete_proportion);
                    }
                }
                if(type==0){
                    map.put("??????"+index,operationInfo);
                }else{
                    map.put("operationVo"+index,operationVo);
                }
                index++;
            }else if(routerProcessTableList.get(i).getParallelFlag()!=0){
                // ????????????,i+1??????????????????
                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
                SfcStateTrack lastSfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i+1).getOperationBo());
                BigDecimal complete_proportion_one = new BigDecimal("0.00");
                BigDecimal complete_proportion_two = new BigDecimal("0.00");
                //???????????????????????????
                if(actionAcope.equals("1")) {
                    if(sfcStateTrack!=null){
                        List<HashMap<String, BigDecimal>> data = shopOrderTrackMapper.selectNumberProgress(sfcStateTrack.getSfc(),sfcStateTrack.getOperationBo());
                        if (!CollectionUtils.isEmpty(data)) {
                            //?????????
                            BigDecimal sfc_qty = data.get(0).get("SFC_QTY");
                            //???????????????
                            BigDecimal complete_num = new BigDecimal("0.00");
                            for (HashMap<String, BigDecimal> datum : data) {
                                complete_num = complete_num.add(datum.get("DONE_QTY")).add(datum.get("SCRAP_QTY"));
                            }
                            //??????0????????????
                            if (complete_num.compareTo(BigDecimal.ZERO) > 0) {
                                complete_proportion_one = complete_num.divide(sfc_qty, 2, BigDecimal.ROUND_HALF_UP);
                            }
                        }
                    }
                    if(lastSfcStateTrack!=null){
                        List<HashMap<String, BigDecimal>> data = shopOrderTrackMapper.selectNumberProgress(lastSfcStateTrack.getSfc(),lastSfcStateTrack.getOperationBo());
                        if (!CollectionUtils.isEmpty(data)) {
                            //?????????
                            BigDecimal sfc_qty = data.get(0).get("SFC_QTY");
                            //???????????????
                            BigDecimal complete_num = new BigDecimal("0.00");
                            for (HashMap<String, BigDecimal> datum : data) {
                                complete_num = complete_num.add(datum.get("DONE_QTY")).add(datum.get("SCRAP_QTY"));
                            }
                            //??????0????????????
                            if (complete_num.compareTo(BigDecimal.ZERO) > 0) {
                                complete_proportion_two = complete_num.divide(sfc_qty, 2, BigDecimal.ROUND_HALF_UP);
                            }
                        }
                    }
                }
                String operationInfo=null;
                OperationVo operationVo=new OperationVo();
                if(sfcStateTrack==null && lastSfcStateTrack==null){
                    // ????????????????????????????????????????????????
                    String maxTime=null;
                    String state=null;
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
                        map.put("??????"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime,complete_proportion_one);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
                        map.put("??????"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime,complete_proportion_one);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                }else if(sfcStateTrack==null && lastSfcStateTrack!=null ){
                    // ????????????????????????????????????
                    String maxTime=null;
                    String state=null;
                    String operationDate=null;
                    if(lastSfcStateTrack.getUpdateDate()!=null){
                        operationDate=sdf.format(lastSfcStateTrack.getUpdateDate());
                    }
                    if(type==0){
                        operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
                        map.put("??????"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate,complete_proportion_two);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                    maxTime=null;
                    state=null;
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
                        map.put("??????"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime,complete_proportion_two);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                }else if(sfcStateTrack!=null && lastSfcStateTrack==null){
                    // ????????????????????????????????????
                    String maxTime=null;
                    String state=null;
                    String operationDate=null;
                    if(sfcStateTrack.getUpdateDate()!=null){
                        operationDate=sdf.format(sfcStateTrack.getUpdateDate());
                    }
                    if(type==0){
                        operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
                        map.put("??????"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate,complete_proportion_one);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                    maxTime=null;
                    state=null;
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
                        map.put("??????"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime,complete_proportion_one);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                }else if(sfcStateTrack!=null && lastSfcStateTrack!=null){
                    if(sfcStateTrack.getUpdateDate().getTime()>lastSfcStateTrack.getUpdateDate().getTime()){
                        String operationDate=null;
                        operationDate=sdf.format(sfcStateTrack.getUpdateDate());
                        if(type==0){
                            operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
                            map.put("??????"+index,operationInfo);
                        }else{
                            operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate,complete_proportion_one);
                            map.put("operationVo"+index,operationVo);
                        }
                        index++;
                        operationDate=null;
                        operationDate=sdf.format(lastSfcStateTrack.getUpdateDate());
                        if(type==0){
                            operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
                            map.put("??????"+index,operationInfo);
                        }else{
                            operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate,complete_proportion_two);
                            map.put("operationVo"+index,operationVo);
                        }
                        index++;
                    }else {
                        String operationDate=null;
                        operationDate=sdf.format(lastSfcStateTrack.getUpdateDate());
                        if(type==0){
                            operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
                            map.put("??????"+index,operationInfo);
                        }else{
                            operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate,complete_proportion_two);
                            map.put("operationVo"+index,operationVo);
                        }
                        index++;
                        operationDate=null;
                        operationDate=sdf.format(sfcStateTrack.getUpdateDate());
                        if(type==0){
                            operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
                            map.put("??????"+index,operationInfo);
                        }else{
                            operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate,complete_proportion_one);
                            map.put("operationVo"+index,operationVo);
                        }
                        index++;
                    }
                }
                i++;//????????????????????????????????????
            }
        }
    }

    public List<Map<String,Object>>  shorpOrderTrackListSorted(List<Map<String,Object>>  shorpOrderTrackList){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String,Object>> shorpOrderTrackListSorted=new ArrayList<>();
        if(shorpOrderTrackList !=null && shorpOrderTrackList.size()>0){
            shorpOrderTrackListSorted= shorpOrderTrackList.stream().sorted((o1, o2) -> {
                Date date1= null;
                long longDate1=0;
                long longDate2=0;
                try {
                    if(o1.get("startDate")==null){
                        longDate1=0;
                    }else{
                        date1 = format.parse(o1.get("startDate").toString());
                        longDate1=date1.getTime();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2= null;
                try {
                    if(o2.get("startDate")==null){
                        longDate2=0;
                    }else{
                        date2 = format.parse(o2.get("startDate").toString());
                        longDate2=date2.getTime();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return  Long.compare(longDate2,longDate1);
            }).collect(Collectors.toList());
        }
        return shorpOrderTrackListSorted;
    }
}
