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

    private static Map<String,String> operationInfoMap=new HashMap<>();  //key->工序BO，value->工序名称

    private static Map<String, List<RouterProcessTable>> routerProcessTableMap=new HashMap<>(); // key->工艺路线BO，value->工艺路线对象

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
            // 遍历集合，将个工单下的所有工序取出
            page.getRecords().forEach(shopOrderTrack -> {
                shopOrders.add(shopOrderTrack.getShopOrder());
                Map<String,Object> map=new LinkedHashMap<>();
                // 根据工单查询当前工单下的所有的sfc个数
                map.put("sfcNum",shopOrderTrackMapper.selectCountSfcByShopOrder(shopOrderTrack.getShopOrder()));
                // 将计划开始时间和计划完成时间转换数据格式
                if(shopOrderTrack.getPlanStartDate() !=null){
                    shopOrderTrack.setStartDate(format.format(shopOrderTrack.getPlanStartDate()));
                }
                if(shopOrderTrack.getPlanEndDate() !=null){
                    shopOrderTrack.setEndDate(format.format(shopOrderTrack.getPlanEndDate()));
                }
                // 获取所有的基础数据
                getAllBaseInfo(map,shopOrderTrack);
                // 通过sfc查询对应工艺路线所有的工序
                String processInfo=shopOrderTrackMapper.selectProcessInfoBySfc(shopOrderTrack.getSfc());
                // 解析工艺路线
                String[] operationBos=shopOrderTrack.getOperationBo().split("\\|");
                if(operationBos.length==1){
                    // 1.当前工序只有一个
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
                    // 2.多个工序
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
        // 首工序，没有批次条码的情况下，根据派工单查询
        IPage<ShopOrderTrack> pageWithOutSfc=shopOrderTrackMapper.selectFirstOperationByCondifition(shopOrderTrackDto.getPage(),shopOrderTrackDto);
        // 合并总数据
        if(pageWithOutSfc !=null && pageWithOutSfc.getRecords() !=null && pageWithOutSfc.getRecords().size()>0){
            shopOrderTrackVo.setTotal(shopOrderTrackVo.getTotal()+pageWithOutSfc.getRecords().size());
            String total=shopOrderTrackVo.getTotal()+"";
            int size=(int)shopOrderTrackVo.getSize();
            // 计算页数
            shopOrderTrackVo.setPages(new Double(Math.ceil(Double.parseDouble(total)/size)).longValue());
        }
        if(pageWithOutSfc !=null && pageWithOutSfc.getRecords() !=null && pageWithOutSfc.getRecords().size()>0){
            pageWithOutSfc.getRecords().forEach(shopOrderTrack -> {
                shopOrders.add(shopOrderTrack.getShopOrder());
                Map<String,Object> map=new LinkedHashMap<>();
                map.put("sfcNum",1);
                // 将计划开始时间和计划完成时间转换数据格式
                if(shopOrderTrack.getPlanStartDate() !=null){
                    shopOrderTrack.setStartDate(sdf.format(shopOrderTrack.getPlanStartDate()));
                }
                if(shopOrderTrack.getPlanEndDate() !=null){
                    shopOrderTrack.setEndDate(sdf.format(shopOrderTrack.getPlanEndDate()));
                }
                // 获取所有的基础数据
                getAllBaseInfo(map,shopOrderTrack);
                map.put("sfc",shopOrderTrack.getDispatchCode());
                OperationVo operationVo=new OperationVo();
                operationVo.setName(shopOrderTrack.getOperationName());
                operationVo.setState("首工序生产中");
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
     * 逻辑改变前的跟踪报表导出
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
                //获取基础表数据
                getAllTableBaseInfo(map,shopOrderTrack);
                // 通过sfc查询对应工艺路线所有的工序
                String processInfo=shopOrderTrackMapper.selectProcessInfoBySfc(shopOrderTrack.getSfc());
                // 解析工艺路线
                String[] operationBos=shopOrderTrack.getOperationBo().split("\\|");
                int num= 0;
                if(operationBos.length==1){
                    // 1.当前工序只有一个
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
                    // 2.多个工序
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
        /*此处标题的数组则对应excel的标题*/
        List<String> listHead=getTableHead();
        String[] title = new String[listHead.size()+maxNum];
        for(int i=0;i<listHead.size();i++){
            title[i]=listHead.get(i);
        }
        for(int j=1;j<=maxNum;j++){
            title[listHead.size()+j-1]="工序"+j;
        }
        Map<String/*此处的key为每个sheet的名称，一个excel中可能有多个sheet页*/, List<Map<String/*此处key对应每一列的标题*/, Object>>/*该list为每个sheet页的数据*/> map = new HashMap<>();
        map.put("派工单跟踪报表", list);


//        String[] title = {"id","标题","描述","负责人","开始时间"};
//        List<Map<String, String>> list =new ArrayList();
//        /*这边是制造一些数据，注意每个list中map的key要和标题数组中的元素一致*/
//        for(int i = 0; i<10; i++){
//            HashMap<String, String> map = com.google.common.collect.Maps.newHashMap();
//            if(i > 5){
//                if(i<7){
//                    map.put("id","333");
//                    map.put("标题","mmmm");
//                }else {
//                    map.put("id","333");
//                    map.put("标题","aaaaa");
//                }
//            }else if (i >3){
//                map.put("id","222");
//                map.put("标题","哈哈哈哈");
//            }else if (i>1 && i<3){
//                map.put("id","222");
//                map.put("标题","hhhhhhhh");
//            }else {
//                map.put("id","222");
//                map.put("标题","bbbb");
//            }
//            map.put("描述","sssssss");
//            map.put("负责人","vvvvv");
//            map.put("开始时间","2017-02-27 11:20:26");
//            list.add(map);
//        }
//        Map<String/*此处的key为每个sheet的名称，一个excel中可能有多个sheet页*/, List<Map<String/*此处key对应每一列的标题*/, String>>/*该list为每个sheet页的数据*/> map = new HashMap();
//        map.put("测试合并数据", list);
     ExcelUtils.createExcel(title, map, new int[]{1},response,11,maxNum);

    }

    // 将所有的基础数据信息放入map中
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
    // 解析工艺路线 只有一个工序
    public int analysisProcessInfoByOne(Map<String,Object> map,String processInfo,ShopOrderTrack shopOrderTrack) throws ParseException {
        JSONObject processInfoObject= JSON.parseObject(processInfo);
        Map<String,JSONObject> nodeMap=new HashMap<>();
        int operationNum=1;//工序序号
        if(processInfoObject.get("nodeList")!=null && processInfoObject.get("lineList") !=null){
            JSONArray nodeList=JSONArray.parseArray(processInfoObject.get("nodeList").toString());
            // nodeList 转map
            String startId=listToMap(nodeMap,nodeList);
            if(processInfoObject.get("lineList") !=null){
                JSONArray lineList=JSONArray.parseArray(processInfoObject.get("lineList").toString());
                Map<String,List<JSONObject>> lineMap=new HashMap<>();
                // lineList 转map
                for(Object obj:lineList){
                    JSONObject jsonObject=JSON.parseObject(obj.toString());
                    List<JSONObject> jsonObjectList=null;
                    if(lineMap.get(jsonObject.get("from").toString())==null){
                        // 不存在并行工序
                        jsonObjectList=new ArrayList<>();
                    }else{
                        // 存在并行工序
                        jsonObjectList=lineMap.get(jsonObject.get("from").toString());
                    }
                    jsonObjectList.add(jsonObject);
                    lineMap.put(jsonObject.get("from").toString(),jsonObjectList);
                }
                List<JSONObject> lineObjectList=lineMap.get(startId);
                int isFinshed=1; //0未完成，1已完成
                OperationVo operationLastVo=new OperationVo();// 前一个工序
                // 遍历lineMap
                while(true){
                    // 如果是单个工序
                    if(lineObjectList ==null){
                        break;
                    }
                    if(lineObjectList.size()==1){
                        // 验证是否当前工序
                        JSONObject operationObj=nodeMap.get(lineObjectList.get(0).get("to").toString());
                        if(operationObj !=null && operationObj.get("operation") !=null){
                            OperationVo operationVo=new OperationVo();
                            if(operationObj.get("operation").toString().equals(shopOrderTrack.getOperationBo())){
                                // 当前工序
                                isFinshed=0;
                                if(shopOrderTrack.getState().equals("已完成") || shopOrderTrack.getState().equals("暂停")){
                                    // 如果状态是已完成状态,从me_sfc_wip_log取sfc在该工序最后的out—time
                                    String maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),shopOrderTrack.getOperationBo());
                                    if(maxTime !=null){
                                        operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                                    }
                                }else if(shopOrderTrack.getState().equals("新建")){
                                    // 新建状态，sfc的创建时间
                                    if(shopOrderTrack.getCreateDate() !=null){
                                        operationVo.setDate(sdf.format(shopOrderTrack.getCreateDate()));
                                    }
                                }else if(shopOrderTrack.getState().equals("生产中")){
                                    // 生产中，进站时间
                                    //取进站时间
                                    Date inTime = shopOrderTrackMapper.getInTimeBySfcAndOperation(shopOrderTrack.getSfc(), shopOrderTrack.getOperationBo());
                                    if (ObjectUtil.isNotEmpty(inTime)){
                                        operationVo.setDate(sdf.format(inTime));
                                    }
                                }else{
                                    // 排队中，维修中去上一个工序的时间
                                    operationVo.setDate(operationLastVo.getDate());
                                }
                                operationVo.setName(operationObj.get("name").toString());
                                operationVo.setState(shopOrderTrack.getState());
                                map.put("operationVo"+operationNum,operationVo);
                                operationNum++;
                            }else {
                                // 非当前工序
                                // 验证是当前工序前的工序还是当前工序后的工序
                                if(isFinshed==1){
                                    String maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),operationObj.get("operation").toString());
                                    if(maxTime !=null){
                                        operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                                    }
                                    operationVo.setState("已完成");
                                }
                                operationVo.setName(operationObj.get("name").toString());
                                map.put("operationVo"+operationNum,operationVo);
                                operationNum++;
                            }
                            // 保留当前工序数据作为下个工序的参照数据
                            BeanUtil.copyProperties(operationVo, operationLastVo);
                        }
                    }else{
                        // 并行工序
                        // 第一步检查集合中是否存在当前工序
                        boolean isExist=false;
                        if(lineObjectList !=null){
                            for(int i=0;i<lineObjectList.size();i++){
                                JSONObject operationObj=nodeMap.get(lineObjectList.get(i).get("to").toString());
                                JSONObject operationOtherObj=new JSONObject();
                                if(operationObj.get("operation") !=null){
                                    // 验证是否当前工序
                                    if(operationObj.get("operation").toString().equals(shopOrderTrack.getOperationBo())){
                                        isExist=true;
                                        isFinshed=0;
                                        // 集合中存在当前工序
                                        // 1.验证集合中工序的顺序，向me_sfc_wip_log中查询该集合中的另一个工序是否存在
                                        String maxTime=null;
                                        if(i==0){
                                            operationOtherObj=nodeMap.get(lineObjectList.get(1).get("to").toString());
                                            maxTime = shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(), shopOrderTrack.getSfc(),operationOtherObj.get("operation").toString());
                                        }else{
                                            operationOtherObj=nodeMap.get(lineObjectList.get(0).get("to").toString());
                                            maxTime = shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(), shopOrderTrack.getSfc(),operationOtherObj.get("operation").toString());
                                        }
                                        if(maxTime!=null){
                                            // 存在，则已完成
                                            OperationVo operationVo=new OperationVo();
                                            operationVo.setName(operationOtherObj.get("name").toString());
                                            /*operationVo.setDate(maxTime.replaceAll("-", ""));*/
                                            operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                                            operationVo.setState("已完成");
                                            map.put("operationVo"+operationNum,operationVo);
                                            operationNum++;
                                        }else {
                                            // 不存在，未开始
                                            OperationVo operationNowVo=new OperationVo();// 当前工序
                                            operationNowVo.setName(operationObj.get("name").toString());
                                            if(shopOrderTrack.getState().equals("已完成") || shopOrderTrack.getState().equals("暂停")){
                                                // 如果状态是已完成状态,从me_sfc_wip_log取sfc在该工序最后的out—time
                                                maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),shopOrderTrack.getOperationBo());
                                                /*operationNowVo.setDate(maxTime.replaceAll("-",""));*/
                                                operationNowVo.setDate(sdf.format(sdf.parse(maxTime)));
                                            }else if(shopOrderTrack.getState().equals("新建")){
                                                // 新建状态，sfc的创建时间
                                                operationNowVo.setDate(sdf.format(shopOrderTrack.getCreateDate()));
                                            }else if(shopOrderTrack.getState().equals("生产中")){
                                                // 生产中，进站时间
                                                operationNowVo.setDate(sdf.format(shopOrderTrack.getInTime()));
                                            }else{
                                                // 排队中，维修中去上一个工序的时间
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
                                // 检查状态isFinshed
                                List<OperationVo> list=new ArrayList<>();
                                for(int i=0;i<lineObjectList.size();i++) {
                                    OperationVo operationVo = new OperationVo();
                                    JSONObject operationObj=nodeMap.get(lineObjectList.get(i).get("to").toString());
                                    if (isFinshed == 1) {
                                        // 如果状态是已完成状态,从me_sfc_wip_log取sfc在该工序最后的out—time
                                        String maxTime = shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(), shopOrderTrack.getSfc(),operationObj.get("operation").toString());
                                        Date time=shopOrderTrackMapper.selectMaxAllTime(shopOrderTrack.getBo(), shopOrderTrack.getSfc(),operationObj.get("operation").toString());
                                        if(maxTime !=null){
                                            /*operationVo.setDate(maxTime.replaceAll("-", ""));*/
                                            operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                                        }
                                        operationVo.setState("已完成");
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
                                // 找到这并行工序后的工序,只有两个并行节点
                                String first=operationVoList.get(0).getTo();
                                lineObjectList=lineMap.get(lineObjectList.get(lineObjectList.size()-1).get("to").toString());
                                //JSONObject operationObj=nodeMap.get(lineObjectList.get(0).get("to").toString());
                            }
                        }
                    }
                    // 下一个工序
                    if(lineMap.get(lineObjectList.get(lineObjectList.size()-1).get("to").toString())==null){
                        break;
                    }
                    lineObjectList=lineMap.get(lineObjectList.get(lineObjectList.size()-1).get("to").toString());
                }
            }
        }
        return operationNum-1;
    }
    // 解析工艺路线，多个工序
    public int analysisProcessInfoByMore(Map<String,Object> map,String processInfo,ShopOrderTrack shopOrderTrack) throws ParseException {
        JSONObject processInfoObject= JSON.parseObject(processInfo);
        Map<String,JSONObject> nodeMap=new HashMap<>();
        int operationNum=1;//工序序号
        if(processInfoObject.get("nodeList")!=null) {
            JSONArray nodeList = JSONArray.parseArray(processInfoObject.get("nodeList").toString());
            // nodeList 转map
            String startId=listToMap(nodeMap,nodeList);
            if(processInfoObject.get("lineList") !=null){
                JSONArray lineList=JSONArray.parseArray(processInfoObject.get("lineList").toString());
                Map<String,JSONObject> lineMap=new HashMap<>();
                // lineList 转map
                for(Object obj:lineList){
                    JSONObject jsonObject=JSON.parseObject(obj.toString());
                    lineMap.put(jsonObject.get("from").toString(),jsonObject);
                }
                JSONObject lineObject=lineMap.get(startId);
                int isFinshed=1; //0未完成，1已完成
                OperationVo operationLastVo=new OperationVo();// 前一个工序
                String[] operationBos=shopOrderTrack.getOperationBo().split("\\|");

                while(true){
                    JSONObject operationObj=nodeMap.get(lineObject.get("to").toString());
                    for(int i=0;i<operationBos.length;i++){
                        // 如果当前工序为数组中的某个工序
                        if(operationObj.get("operation").toString().equals(operationBos[i])){
                            isFinshed=0;
                            OperationVo operationVo=new OperationVo();
                            operationVo.setName(operationObj.get("name").toString());
                            operationVo.setDate(operationLastVo.getDate());
                            operationVo.setState("排队中");
                            map.put("operationVo"+operationNum,operationVo);
                            operationNum++;
                            // 当前工序的下一个工序也为排队中
                            lineObject=lineMap.get(lineObject.get("to").toString());
                            operationObj=nodeMap.get(lineObject.get("to").toString());
                            OperationVo operationVo1=new OperationVo();
                            operationVo1.setName(operationObj.get("name").toString());
                            operationVo1.setDate(operationLastVo.getDate());
                            operationVo1.setState("排队中");
                            map.put("operationVo"+operationNum,operationVo1);
                            operationNum++;
                            lineObject=lineMap.get(lineObject.get("to").toString());
                        }
                    }
                    if(isFinshed==1){
                        String maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),operationObj.get("operation").toString());
                        OperationVo operationVo=new OperationVo();
                        operationVo.setDate(sdf.format(sdf.parse(maxTime)));
                        operationVo.setState("已完成");
                        operationVo.setName(operationObj.get("name").toString());
                        map.put("operationVo"+operationNum,operationVo);
                        operationNum++;
                        // 保留当前工序数据作为下个工序的参照数据
                        BeanUtil.copyProperties(operationVo, operationLastVo);
                    }else {
                        operationObj=nodeMap.get(lineObject.get("to").toString());
                        OperationVo operationVo=new OperationVo();
                        operationVo.setName(operationObj.get("name").toString());
                        map.put("operationVo"+operationNum,operationVo);
                        operationNum++;
                    }
                    // 下一个工序
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
                if(jsonObject.get("name").toString().equals("开始流程")){
                    startId=jsonObject.get("id").toString();
                }
                // key为id
                nodeMap.put(jsonObject.get("id").toString(),jsonObject);
            }
        }
        return startId;
    }

    // 初始化表头数据
    public List<String> getTableHead(){
        List<String> list=new ArrayList<>();
        list.add("订购单号");
        list.add("工单编码");
        list.add("物料规格");
        list.add("物料编码");
        list.add("物料名称");
        list.add("工单数量");
        list.add("计划开始时间");
        list.add("计划完成时间");
        list.add("SFC数量");
        list.add("SFC状态");
        list.add("SFC条码");
        return list;
    }

    // 将所有的基础数据信息放入表map中
    public void getAllTableBaseInfo(Map<String,Object> map,ShopOrderTrack shopOrderTrack){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        map.put("工单编码",shopOrderTrack.getShopOrder());
        map.put("工单数量",shopOrderTrack.getOrderQty()+"");
        map.put("计划开始时间",simpleDateFormat.format(shopOrderTrack.getPlanStartDate()));
        map.put("计划完成时间",simpleDateFormat.format(shopOrderTrack.getPlanEndDate()));
        map.put("订购单号",shopOrderTrack.getCustomerOrder());
        map.put("物料编码",shopOrderTrack.getItem());
        map.put("物料名称",shopOrderTrack.getItemName());
        map.put("物料规格",shopOrderTrack.getItemDesc());
        map.put("SFC条码",shopOrderTrack.getSfc());
        map.put("SFC状态",shopOrderTrack.getState());
        map.put("SFC数量",shopOrderTrack.getSfcQty()+"");
    }

    // 将operationMap中的工序数据排序存入表map中
    public void oderOperation(Map<String,Object> map,Map<String,Object> operationMap,int num){
        for(int i=1;i<=num;i++){
            OperationVo operationVo= (OperationVo) operationMap.get("operationVo"+i);
            String operationName=operationVo.getName()==null?"":operationVo.getName();
            String operationState=operationVo.getState()==null?"":operationVo.getState();
            String date=operationVo.getDate()==null?"":operationVo.getDate();
            String operationInfo=operationName+"\r\n"+operationState+"\r\n"+date;
            map.put("工序"+i,operationInfo);
        }
    }

    /**
     * 逻辑改变后的派工单跟踪报表导出
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
                //获取基础表数据
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
//                                // 非并行
//                                String operationInfo=null;
//                                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
//                                if(sfcStateTrack !=null){
//                                    String operationDate=null;
//                                    if(sfcStateTrack.getUpdateDate()!=null){
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                    }
//                                    operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                }else {
//                                    // 该工序还未进行生产 或者是 检验工序
//                                    //检验工序
//                                    //根据sfc状态判断是否已经完成
//                                    String maxTime=null;
//                                    String state=null;
//                                    if("已完成".equals(sfc.getState())){
//                                        maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),routerProcessTableList.get(i).getOperationBo());
//                                        if(maxTime !=null){
//                                            try {
//                                                maxTime=sdf.format(sdf.parse(maxTime));
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                        state="已完成";
//
//                                    }
//                                   operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                }
//                                map.put("工序"+index,operationInfo);
//                                index++;
//                            }else if(routerProcessTableList.get(i).getParallelFlag()!=0){
//                                // 并行工序,i+1为其并行工序
//                                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
//                                SfcStateTrack lastSfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i+1).getOperationBo());
//                                i++;//移到当前工序的后一位工序
//                                String operationInfo=null;
//                                if(sfcStateTrack==null && lastSfcStateTrack==null){
//                                    // 两个工序都为空，说明都未开始生产
//                                    String maxTime=null;
//                                    String state=null;
//                                    operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    map.put("工序"+index,operationInfo);
//                                    index++;
//                                    operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
//                                    map.put("工序"+index,operationInfo);
//                                    index++;
//                                }else if(sfcStateTrack==null && lastSfcStateTrack!=null ){
//                                    // 一个已经生产，一个未生产
//                                    String maxTime=null;
//                                    String state=null;
//                                    String operationDate=null;
//                                    if(lastSfcStateTrack.getUpdateDate()!=null){
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                    }
//                                    operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                    map.put("工序"+index,operationInfo);
//                                    index++;
//                                    maxTime=null;
//                                    state=null;
//                                    operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    map.put("工序"+index,operationInfo);
//                                    index++;
//                                }else if(sfcStateTrack!=null && lastSfcStateTrack==null){
//                                    // 一个已经生产，一个未生产
//                                    String maxTime=null;
//                                    String state=null;
//                                    String operationDate=null;
//                                    if(sfcStateTrack.getUpdateDate()!=null){
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                    }
//                                    operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                    map.put("工序"+index,operationInfo);
//                                    index++;
//                                    maxTime=null;
//                                    state=null;
//                                    operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
//                                    map.put("工序"+index,operationInfo);
//                                    index++;
//                                }else if(sfcStateTrack!=null && lastSfcStateTrack!=null){
//                                    if(sfcStateTrack.getUpdateDate().getTime()>lastSfcStateTrack.getUpdateDate().getTime()){
//                                        String operationDate=null;
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                        operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        map.put("工序"+index,operationInfo);
//                                        index++;
//                                        operationDate=null;
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                        operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        map.put("工序"+index,operationInfo);
//                                        index++;
//                                    }else {
//                                        String operationDate=null;
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                        operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        map.put("工序"+index,operationInfo);
//                                        index++;
//                                        operationDate=null;
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                        operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        map.put("工序"+index,operationInfo);
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

        /*此处标题的数组则对应excel的标题*/
        List<String> listHead=getTableHead();
        String[] title = new String[listHead.size()+maxNum];
        for(int i=0;i<listHead.size();i++){
            title[i]=listHead.get(i);
        }
        for(int j=1;j<=maxNum;j++){
            title[listHead.size()+j-1]="工序"+j;
        }
        Map<String/*此处的key为每个sheet的名称，一个excel中可能有多个sheet页*/, List<Map<String/*此处key对应每一列的标题*/, Object>>/*该list为每个sheet页的数据*/> map = new HashMap<>();
        map.put("派工单跟踪报表", list);
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
            // 遍历集合，将个工单下的所有工序取出
            page.getRecords().forEach(shopOrderTrack -> {
                shopOrders.add(shopOrderTrack.getShopOrder());
                Map<String,Object> map=new LinkedHashMap<>();
                // 根据工单查询当前工单下的所有的sfc个数
                map.put("sfcNum",shopOrderTrackMapper.selectCountSfcByShopOrder(shopOrderTrack.getShopOrder()));
                // 将计划开始时间和计划完成时间转换数据格式
                if(shopOrderTrack.getPlanStartDate() !=null){
                    shopOrderTrack.setStartDate(format.format(shopOrderTrack.getPlanStartDate()));
                }
                if(shopOrderTrack.getPlanEndDate() !=null){
                    shopOrderTrack.setEndDate(format.format(shopOrderTrack.getPlanEndDate()));
                }
                // 获取所有的基础数据
                getAllBaseInfo(map,shopOrderTrack);
                //获取工艺路线信息
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
//                                // 非并行
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
//                                    // 该工序还未进行生产 或者是 检验工序
//                                    //检验工序
//                                    //根据sfc状态判断是否已经完成
//                                    String maxTime=null;
//                                    String state=null;
//                                    if("已完成".equals(sfc.getState())){
//                                        maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),routerProcessTableList.get(i).getOperationBo());
//                                        if(maxTime !=null){
//                                            try {
//                                                maxTime=sdf.format(sdf.parse(maxTime));
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                        state="已完成";
//                                    }
//                                   // operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                    operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
//                                }
//                                map.put("operationVo"+index,operationVo);
//                                index++;
//                            }else if(routerProcessTableList.get(i).getParallelFlag()!=0){
//                                // 并行工序,i+1为其并行工序
//                                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
//                                SfcStateTrack lastSfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i+1).getOperationBo());
//                                i++;//移到当前工序的后一位工序
//                                //String operationInfo=null;
//                                OperationVo operationVo=new OperationVo();
//                                if(sfcStateTrack==null && lastSfcStateTrack==null){
//                                    // 两个工序都为空，说明都未开始生产
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
//                                    // 一个已经生产，一个未生产
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
//                                    // 一个已经生产，一个未生产
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
//                                        map.put("工序"+index,operationVo);
//                                        index++;
//                                        operationDate=null;
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                        //operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        map.put("工序"+index,operationVo);
//                                        index++;
//                                    }else {
//                                        String operationDate=null;
//                                        operationDate=simpleDateFormat.format(lastSfcStateTrack.getUpdateDate());
//                                        //operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
//                                        map.put("工序"+index,operationVo);
//                                        index++;
//                                        operationDate=null;
//                                        operationDate=simpleDateFormat.format(sfcStateTrack.getUpdateDate());
//                                        //operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
//                                        map.put("工序"+index,operationVo);
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
        // 首工序，没有批次条码的情况下，根据派工单查询
        IPage<ShopOrderTrack> pageWithOutSfc=shopOrderTrackMapper.selectFirstOperationByCondifition(shopOrderTrackDto.getPage(),shopOrderTrackDto);
        // 合并总数据
        if(pageWithOutSfc !=null && pageWithOutSfc.getRecords() !=null && pageWithOutSfc.getRecords().size()>0){
            shopOrderTrackVo.setTotal(shopOrderTrackVo.getTotal()+pageWithOutSfc.getRecords().size());
            String total=shopOrderTrackVo.getTotal()+"";
            int size=(int)shopOrderTrackVo.getSize();
            // 计算页数
            shopOrderTrackVo.setPages(new Double(Math.ceil(Double.parseDouble(total)/size)).longValue());

        }
        if(pageWithOutSfc !=null && pageWithOutSfc.getRecords() !=null && pageWithOutSfc.getRecords().size()>0){
            pageWithOutSfc.getRecords().forEach(shopOrderTrack -> {
                shopOrders.add(shopOrderTrack.getShopOrder());
                Map<String,Object> map=new LinkedHashMap<>();
                map.put("sfcNum",1);
                map.put("sfc",shopOrderTrack.getDispatchCode());
                // 获取所有的基础数据
                getAllBaseInfo(map,shopOrderTrack);
                map.put("sfc",shopOrderTrack.getDispatchCode());
                OperationVo operationVo=new OperationVo();
                operationVo.setName(shopOrderTrack.getOperationName());
                operationVo.setState("首工序生产中");
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
     * 生成工序信息
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
     *  生成工序对象信息
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
     * 检测状态
     * @param state
     * @return
     */
    public String checkState(String state){
        String operationState=null;
        if(state.equals("3")){
            operationState="已完成";
        }else if(state.equals("4")){
            operationState="维修中";
        }else{
            operationState="生产中";
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
     * @param type   0导出，1报表
     */
    public void getInfo(List<RouterProcessTable> routerProcessTableList,Sfc sfc,
                        ShopOrderTrack shopOrderTrack,int index,Map<String,Object> map,int type,String actionAcope){
        for(int i=0;i<routerProcessTableList.size();i++){
            if(routerProcessTableList.get(i).getParallelFlag()==0){
                // 非并行
                String operationInfo=null;
                OperationVo operationVo=new OperationVo();
                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
                BigDecimal complete_proportion = new BigDecimal("0.00");
                //获取生产中数据比例
                if(actionAcope.equals("1") && sfcStateTrack!=null){
                    List<HashMap<String, BigDecimal>> data=shopOrderTrackMapper.selectNumberProgress(sfcStateTrack.getSfc(),sfcStateTrack.getOperationBo());
                    if(!CollectionUtils.isEmpty(data)){
                        //被除数
                        BigDecimal sfc_qty = data.get(0).get("SFC_QTY");
                        //循环取除数
                        BigDecimal complete_num = new BigDecimal("0.00");
                        for (HashMap<String, BigDecimal> datum : data) {
                            complete_num = complete_num.add(datum.get("DONE_QTY")).add(datum.get("SCRAP_QTY"));
                        }
                        //大于0才做处理
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
                    // 该工序还未进行生产 或者是 检验工序
                    //检验工序
                    //根据sfc状态判断是否已经完成
                    String maxTime=null;
                    String state=null;
                    //处理已完成sfc
                    if("已完成".equals(sfc.getState())){
                        maxTime=shopOrderTrackMapper.selectMaxTime(shopOrderTrack.getBo(),shopOrderTrack.getSfc(),routerProcessTableList.get(i).getOperationBo());
                        if(maxTime !=null){
                            try {
                                maxTime=sdf.format(sdf.parse(maxTime));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        state="已完成";
                    }
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
                    }else{
                        operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime,complete_proportion);
                    }
                }
                if(type==0){
                    map.put("工序"+index,operationInfo);
                }else{
                    map.put("operationVo"+index,operationVo);
                }
                index++;
            }else if(routerProcessTableList.get(i).getParallelFlag()!=0){
                // 并行工序,i+1为其并行工序
                SfcStateTrack sfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i).getOperationBo());
                SfcStateTrack lastSfcStateTrack=shopOrderTrackMapper.selectOperationStateInfo(sfc.getSfc(), routerProcessTableList.get(i+1).getOperationBo());
                BigDecimal complete_proportion_one = new BigDecimal("0.00");
                BigDecimal complete_proportion_two = new BigDecimal("0.00");
                //获取生产中数据比例
                if(actionAcope.equals("1")) {
                    if(sfcStateTrack!=null){
                        List<HashMap<String, BigDecimal>> data = shopOrderTrackMapper.selectNumberProgress(sfcStateTrack.getSfc(),sfcStateTrack.getOperationBo());
                        if (!CollectionUtils.isEmpty(data)) {
                            //被除数
                            BigDecimal sfc_qty = data.get(0).get("SFC_QTY");
                            //循环取除数
                            BigDecimal complete_num = new BigDecimal("0.00");
                            for (HashMap<String, BigDecimal> datum : data) {
                                complete_num = complete_num.add(datum.get("DONE_QTY")).add(datum.get("SCRAP_QTY"));
                            }
                            //大于0才做处理
                            if (complete_num.compareTo(BigDecimal.ZERO) > 0) {
                                complete_proportion_one = complete_num.divide(sfc_qty, 2, BigDecimal.ROUND_HALF_UP);
                            }
                        }
                    }
                    if(lastSfcStateTrack!=null){
                        List<HashMap<String, BigDecimal>> data = shopOrderTrackMapper.selectNumberProgress(lastSfcStateTrack.getSfc(),lastSfcStateTrack.getOperationBo());
                        if (!CollectionUtils.isEmpty(data)) {
                            //被除数
                            BigDecimal sfc_qty = data.get(0).get("SFC_QTY");
                            //循环取除数
                            BigDecimal complete_num = new BigDecimal("0.00");
                            for (HashMap<String, BigDecimal> datum : data) {
                                complete_num = complete_num.add(datum.get("DONE_QTY")).add(datum.get("SCRAP_QTY"));
                            }
                            //大于0才做处理
                            if (complete_num.compareTo(BigDecimal.ZERO) > 0) {
                                complete_proportion_two = complete_num.divide(sfc_qty, 2, BigDecimal.ROUND_HALF_UP);
                            }
                        }
                    }
                }
                String operationInfo=null;
                OperationVo operationVo=new OperationVo();
                if(sfcStateTrack==null && lastSfcStateTrack==null){
                    // 两个工序都为空，说明都未开始生产
                    String maxTime=null;
                    String state=null;
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
                        map.put("工序"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime,complete_proportion_one);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
                        map.put("工序"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime,complete_proportion_one);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                }else if(sfcStateTrack==null && lastSfcStateTrack!=null ){
                    // 一个已经生产，一个未生产
                    String maxTime=null;
                    String state=null;
                    String operationDate=null;
                    if(lastSfcStateTrack.getUpdateDate()!=null){
                        operationDate=sdf.format(lastSfcStateTrack.getUpdateDate());
                    }
                    if(type==0){
                        operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
                        map.put("工序"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate,complete_proportion_two);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                    maxTime=null;
                    state=null;
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime);
                        map.put("工序"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(routerProcessTableList.get(i).getOperationBo(),state,maxTime,complete_proportion_two);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                }else if(sfcStateTrack!=null && lastSfcStateTrack==null){
                    // 一个已经生产，一个未生产
                    String maxTime=null;
                    String state=null;
                    String operationDate=null;
                    if(sfcStateTrack.getUpdateDate()!=null){
                        operationDate=sdf.format(sfcStateTrack.getUpdateDate());
                    }
                    if(type==0){
                        operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
                        map.put("工序"+index,operationInfo);
                    }else{
                        operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate,complete_proportion_one);
                        map.put("operationVo"+index,operationVo);
                    }
                    index++;
                    maxTime=null;
                    state=null;
                    if(type==0){
                        operationInfo=generateOperationInfo(routerProcessTableList.get(i+1).getOperationBo(),state,maxTime);
                        map.put("工序"+index,operationInfo);
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
                            map.put("工序"+index,operationInfo);
                        }else{
                            operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate,complete_proportion_one);
                            map.put("operationVo"+index,operationVo);
                        }
                        index++;
                        operationDate=null;
                        operationDate=sdf.format(lastSfcStateTrack.getUpdateDate());
                        if(type==0){
                            operationInfo=generateOperationInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate);
                            map.put("工序"+index,operationInfo);
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
                            map.put("工序"+index,operationInfo);
                        }else{
                            operationVo=generateOperationVoInfo(lastSfcStateTrack.getOperationBo(),checkState(lastSfcStateTrack.getState()),operationDate,complete_proportion_two);
                            map.put("operationVo"+index,operationVo);
                        }
                        index++;
                        operationDate=null;
                        operationDate=sdf.format(sfcStateTrack.getUpdateDate());
                        if(type==0){
                            operationInfo=generateOperationInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate);
                            map.put("工序"+index,operationInfo);
                        }else{
                            operationVo=generateOperationVoInfo(sfcStateTrack.getOperationBo(),checkState(sfcStateTrack.getState()),operationDate,complete_proportion_one);
                            map.put("operationVo"+index,operationVo);
                        }
                        index++;
                    }
                }
                i++;//移到当前工序的后一位工序
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
