package com.itl.mes.core.provider.webService.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.itl.iap.common.util.CodeUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.provider.mapper.PlmRouterMapper;
import com.itl.mes.core.provider.mapper.RouterProcessTableMapper;
import com.itl.mes.core.provider.service.impl.RouterServiceImpl;
import com.itl.mes.core.provider.webService.SyncRouterWebService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.jws.WebService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebService(serviceName = "ERPToMESRouterInfo",
        targetNamespace = "http://webservice.provider.core.mes.itl.com/",
        endpointInterface = "com.itl.mes.core.provider.webService.SyncRouterWebService"
)
@Component
@Slf4j
public class SyncRouterWebServiceImpl implements SyncRouterWebService {

    @Autowired
    private RouterService routerService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private WorkStationService workStationService;

    @Autowired
    private StationService stationService;

    @Autowired
    private RouterProcessService routerProcessService;

    @Autowired
    private PlmRouterMapper plmRouterMapper;

    @Autowired
    private RouterProcessTableMapper routerProcessTableMapper;

    @Autowired
    private RouterServiceImpl routerServiceImpl;

    public static SyncRouterProcess syncRouterProcess = new SyncRouterProcess();

    public static List<SyncRouterProcess.NodeList> nodeList = Lists.newArrayList();

    public static List<SyncRouterProcess.LineList> lineList = Lists.newArrayList();

    public static int top;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized String syncRouterInfo(String info) {
        log.info("打印工艺路线请求字符串======>" + info);
        boolean isLegitimate;//判断json字符串是否合法
        try {
            JSONObject.parseObject(info);
            isLegitimate = true;
        }catch (Exception e){
            isLegitimate = false;
        }
        if (isLegitimate){
            JSONObject routerInfoObj = JSONObject.parseObject(info, Feature.OrderedField);
//            JSONObject routerInfoObj = JSON.parseObject(info);
            JsonParser parser = new JsonParser();
            JsonArray routerList = parser.parse(routerInfoObj.getString("routerList")).getAsJsonArray();//工艺路线集合
            for (int i = 0; i < routerList.size(); i++) {
                //清空静态变量
                nodeList.clear();
                lineList.clear();
                syncRouterProcess.setLineList(null);
                syncRouterProcess.setNodeList(null);
                top = 0;
                String routerInfo = JSON.parseObject(routerList.get(i).toString(),Feature.OrderedField).toString();//单条工艺路线
                try {
                    JSONObject infoObj = JSON.parseObject(routerInfo,Feature.OrderedField);
                    log.info("第（" + i + "）条工艺路线参数列表======>" +infoObj.toJSONString());
                    String item = infoObj.getString("item");
                    //验证物料是否在MES中存在
                    Item itemObj = itemService.getOne(new QueryWrapper<Item>().eq("item",item));
                    if (ObjectUtil.isEmpty(itemObj)){
                        log.info("物料为：<" + item + ">的单号未维护！");
                        return "物料为：<" + item + ">的单号未维护！";
                    }
                    //验证工艺路线编码在MES中是否存在，不存在版本为1.0，存在几条版本累加（第二条3.0、第三条3.0，以此类推），都是新增一条数据，不修改
                    String router = infoObj.getString("router");//工艺路线编码(PLM)
                    String routerMes = router + "," + item;//工艺路线编码（MES）
                    List<Router> routerSingleList = routerService.list(new QueryWrapper<Router>().eq("router", routerMes));
                    String routerVersion;
                    if (CollectionUtil.isNotEmpty(routerSingleList)){
                        routerVersion = Double.valueOf(routerSingleList.size() + 1).toString();
                    }else {
                        routerVersion = "1.0";
                    }
                    //把除了这个工艺路线的其他版本的全部变成非当前版本
                    routerService.update(new Router().setIsCurrentVersion(0),new QueryWrapper<Router>().eq("router",routerMes));

                    Router routerObj = new Router();
                    routerObj.setBo("ROUTER:dongyin," + routerMes + "," + routerVersion);
                    routerObj.setSite("dongyin");
                    routerObj.setRouter(routerMes);
                    routerObj.setRouterType("S");
                    routerObj.setRouterName(infoObj.getString("routerName"));
                    routerObj.setRouterDesc("");
                    routerObj.setState("N");
                    routerObj.setVersion(routerVersion);
                    routerObj.setItemBo(itemObj.getBo());
                    routerObj.setCreateUser("PLM");
                    routerObj.setCreateDate(new Date());
                    routerObj.setIsCurrentVersion(1);
                    routerService.save(routerObj);//新增工艺路线

                    itemObj.setRouterBo(routerObj.getBo());
                    itemObj.setRouterName(routerMes);
                    itemObj.setRouterVersion(routerVersion);
                    itemService.updateById(itemObj);//更新物料表的工艺路线bo

                    generateSurplusData(routerInfo);
                    String routerProcess = JSONArray.toJSON(syncRouterProcess).toString();
                    RouterProcess routerProcessObj = new RouterProcess();
                    routerProcessObj.setRouterBo(routerObj.getBo());
                    routerProcessObj.setSite("dongyin");
                    routerProcessObj.setProcessInfo(routerProcess);
                    routerProcessService.save(routerProcessObj);

                    if(routerProcess !=null){
                        JSONObject processInfoObject= JSON.parseObject(routerProcess);
                        if(processInfoObject.get("nodeList")!=null && processInfoObject.get("lineList") !=null){
                            JSONArray nodeList = JSONArray.parseArray(processInfoObject.get("nodeList").toString());
                            JSONArray lineList=JSONArray.parseArray(processInfoObject.get("lineList").toString());
                            if(nodeList.size()>0 && lineList.size()>0){
                                Map<String,JSONObject> nodeMap=new HashMap<>();
                                // nodeList 转map
                                String startId=routerServiceImpl.listToMap(nodeMap,nodeList);
                                // 将数据存入表中，并返回工序BO集合
                                List<String> operationList=routerServiceImpl.getOperationList(nodeMap,startId,lineList,routerObj.getBo());
                            }
                        }
                    }

                    PlmRouter plmRouter = new PlmRouter();
                    plmRouter.setId(UUID.uuid32());
                    plmRouter.setRouter(routerMes);
                    plmRouter.setRouterName(infoObj.getString("routerName"));
                    plmRouter.setRouterVersion(routerVersion);
                    plmRouter.setItem(itemObj.getItem());
                    plmRouter.setOperationList(infoObj.getString("operationList"));
                    plmRouter.setCreateDate(new Date());
//                    plmRouter.setPType(infoObj.getString("pType"));
                    plmRouter.setCreator(infoObj.getString("creator"));
//                    plmRouter.setSMemo(infoObj.getString("sMemo"));
//                    plmRouter.setMacht(infoObj.getString("macht"));
//                    plmRouter.setDeviceNo(infoObj.getString("deviceNo"));
//                    plmRouter.setDeviceName(infoObj.getString("deviceName"));
//                    plmRouter.setPartNo(infoObj.getString("partNo"));
//                    plmRouter.setPartName(infoObj.getString("partName"));
                    plmRouter.setPbompkgId(infoObj.getString("pbompkgId"));
                    plmRouterMapper.insert(plmRouter);
                }catch (Exception e){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚事务
                    e.getMessage();
                    log.info("异常信息===========>" + e.toString());
                    return "保存失败";
                }
            }
            return "保存成功";
        }else {
            log.info("字符串不合法,请检查格式");
            return "字符串不合法,请检查格式";
        }
    }

    /**
     * 生成工序、工艺路线、工步、设备等数据
     * @param
     */
    public void generateSurplusData(String info) throws Exception{
        JSONObject infoObj = JSON.parseObject(info,Feature.OrderedField);
        JsonParser parser = new JsonParser();
        JsonArray operationList = parser.parse(infoObj.getString("operationList")).getAsJsonArray();
//        JSONArray operationList = JSONArray.parseArray(infoObj.getString("operationList"));//工序集合
        for (int i = 0; i < operationList.size(); i++) {
            JSONObject operationObj = JSON.parseObject(operationList.get(i).toString(),Feature.OrderedField);
            String operation = operationObj.getString("operation");//工序编码
            //验证工序是否在MES中存在，不存在则新增这条工序
            Operation opObj = operationService.getOne(new QueryWrapper<Operation>().eq("operation",operation));
            if (ObjectUtil.isEmpty(opObj)){
                Operation op = new Operation();
                op.setBo("OP:dongyin," + operation + ",1.0");
                op.setSite("dongyin");
                op.setOperation(operation);
                op.setVersion("1.0");
                op.setOperationName(operationObj.getString("operationName"));
                op.setState("N");
                op.setCreateDate(new Date());
                op.setCreateUser("PLM");
                op.setStationTypeBo("ST:dongyin,过站采集");
                op.setDeptNo(operationObj.getString("deptNo"));
                operationService.save(op);
                opObj = op;
            }
            JsonArray workStepList = parser.parse(operationObj.getString("workStepList")).getAsJsonArray();
//            JSONArray workStepList = JSONArray.parseArray(operationObj.getString("workStepList"));//工步集合
            for (int j = 0; j < workStepList.size(); j++) {
                JSONObject workStepObj = JSON.parseObject(workStepList.get(j).toString(),Feature.OrderedField);
                String workStepCode = workStepObj.getString("workStepCode");//工步编码
                String workStepName = workStepObj.getString("workStepName");//工步名称
                String stationCode = "GW-" + workStepCode;//工位编码
                //判断工步、工位是否在MES系统中存在
                WorkStation workStationObj = workStationService.getOne(new QueryWrapper<WorkStation>().eq("work_step_code", workStepCode));
                if (ObjectUtil.isEmpty(workStationObj)){
                    WorkStation workStation = new WorkStation();
                    workStation.setBo("OPS:dongyin," + opObj.getBo() + "," + workStepCode);
                    workStation.setSite("dongyin");
                    workStation.setWorkStepCode(workStepCode);
                    workStation.setWorkStepName(workStepName);
                    workStation.setWorkingProcess(opObj.getBo());
                    workStation.setUpdatedBy("PLM");
                    workStation.setUpdateTime(new Date());
                    workStationService.save(workStation);
                    workStationObj = workStation;
                }
                Station stationObj = stationService.getOne(new QueryWrapper<Station>().eq("station", stationCode));
                if (ObjectUtil.isEmpty(stationObj)){
                    Station station = new Station();
                    station.setBo("STATION:dongyin," + stationCode);
                    station.setSite("dongyin");
                    station.setStation(stationCode);
                    station.setStationName(workStepName);
                    station.setStationDesc(workStepName);
                    station.setOperationBo(opObj.getBo());
                    station.setState("1");
                    station.setCreateDate(new Date());
                    station.setCreateUser("PLM");
                    station.setWorkstationBo(workStationObj.getBo());
                    stationService.save(station);
                    stationObj = station;
                }
            }
        }
        getRouterProcess(info);
    }

    /**
     * 生成工艺路线
     * @param info
     */
    public void getRouterProcess(String info){
        JSONObject infoObj = JSON.parseObject(info,Feature.OrderedField);
        JsonParser parser = new JsonParser();
        JsonArray operationList = parser.parse(infoObj.getString("operationList")).getAsJsonArray();

//        JSONArray operationList = JSONArray.parseArray(infoObj.getString("operationList"));//工序集合
        String id = CodeUtils.getRandomNickname(10);
        List<String> nextIds = Lists.newArrayList();
        if(operationList.size() < 2){//如果传入的工艺路线的工序为0个或1个
            for (int i = 0; i < operationList.size(); i++){
                JSONObject operationObj = JSON.parseObject(operationList.get(i).toString(),Feature.OrderedField);
                String operation = operationObj.getString("operation");//工序编码
                String operationName = operationObj.getString("operationName");//工序名称
                List<String> ids = saveOrdinaryOp(id, null, null, false);//新建开始流程
                saveOrdinaryOp(ids.get(0),operation,operationName,false);
            }
        }else {
            for (int i = 0; i < operationList.size(); i++) {
                JSONObject operationObj = JSON.parseObject(operationList.get(i).toString(),Feature.OrderedField);
                String operation = operationObj.getString("operation");//工序编码
                String operationName = operationObj.getString("operationName");//工序名称
                String currentOp = JSON.parseObject(operationList.get(i).toString()).getString("isParallelOp");
                if (i == 0) {
                    if (StringUtils.isNotBlank(currentOp)) {
                        List<String> ids = saveOrdinaryOp(id, null, null, true);//新建开始流程

                        List<String> nIds = saveParallelOp(operation, JSON.parseObject(operationList.get(i + 1).toString()).getString("operation"),
                                operationName, JSON.parseObject(operationList.get(i + 1).toString()).getString("operationName"), ids.get(0), ids.get(1));
                        nextIds.add(nIds.get(0));
                        i++;
                    } else {
                        String nextOp = JSON.parseObject(operationList.get(i + 1).toString()).getString("isParallelOp");
                        List<String> ids = saveOrdinaryOp(id, null, null, false);//新建开始流程
                        if (StringUtils.isNotBlank(nextOp)) {//判断下一条工序是否为并行工序
                            List<String> nIds = saveOrdinaryOp(ids.get(0), operation, operationName, true);
                            nextIds.add(nIds.get(0));
                            nextIds.add(nIds.get(1));
                        } else {
                            List<String> nIds = saveOrdinaryOp(ids.get(0), operation, operationName, false);
                            nextIds.add(nIds.get(0));
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(currentOp)) {//判断当前工序是否是并行工序
                        List<String> nIds = saveParallelOp(operation, JSON.parseObject(operationList.get(i + 1).toString()).getString("operation"),
                                operationName, JSON.parseObject(operationList.get(i + 1).toString()).getString("operationName"), nextIds.get(0), nextIds.get(1));
                        nextIds.clear();
                        nextIds.add(nIds.get(0));
                        i++;
                    } else {
                        String nextOp = null;
                        if (i < operationList.size() - 1) {//如果是最后一条,就不存在下一条工序是不是并行的问题
                            nextOp = JSON.parseObject(operationList.get(i + 1).toString()).getString("isParallelOp");
                        }
                        if (StringUtils.isNotBlank(nextOp)) {//判断下一条工序是否为并行工序
                            List<String> nIds = saveOrdinaryOp(nextIds.get(0), operation, operationName, true);
                            nextIds.clear();
                            nextIds.add(nIds.get(0));
                            nextIds.add(nIds.get(1));
                        } else {
                            List<String> nIds = saveOrdinaryOp(nextIds.get(0), operation, operationName, false);
                            nextIds.clear();
                            nextIds.add(nIds.get(0));
                        }
                    }
                }
            }
        }

    }
    /**
     * 处理并行工序的流程
     * @param opOneBo 并行工序一bo
     * @param opTwoBo 并行工序二bo
     * @param opOneName 并行工序一名称
     * @param opTwoName 并行工序二名词
     * @param opOneId 并行工序一的流程id
     * @param opTwoId 并行工序二的流程id
     * @return 返回下一个节点的流程id
     */
    public List<String> saveParallelOp(String opOneBo,String opTwoBo,String opOneName,String opTwoName,String opOneId,String opTwoId){
        //新建第一条并行工序工艺路线
        SyncRouterProcess.NodeList nodeListObj_o = new SyncRouterProcess.NodeList();
        //------------------------------第一条线---------------------------------
        nodeListObj_o.setId(opOneId);
        nodeListObj_o.setName(opOneName);
        nodeListObj_o.setType("OP:dongyin," + opOneBo + ",1.0");
        nodeListObj_o.setIco("craftRouteList");
        nodeListObj_o.setOperation("OP:dongyin," + opOneBo + ",1.0");
        nodeListObj_o.setLeft("243px");
        top = top + 47;
        nodeListObj_o.setTop(top + "px");
        nodeListObj_o.setState("success");

        String nextId_o = CodeUtils.getRandomNickname(10);
        SyncRouterProcess.LineList lineListObj_o = new SyncRouterProcess.LineList();
        lineListObj_o.setFrom(opOneId);
        lineListObj_o.setTo(nextId_o);
        lineList.add(lineListObj_o);

        SyncRouterProcess.NodeList nodeListObj_t = new SyncRouterProcess.NodeList();
        nodeListObj_t.setId(nextId_o);
        nodeListObj_t.setName(opTwoName);
        nodeListObj_t.setType("OP:dongyin," + opTwoBo + ",1.0");
        nodeListObj_t.setIco("craftRouteList");
        nodeListObj_t.setOperation("OP:dongyin," + opTwoBo + ",1.0");
        nodeListObj_t.setLeft("243px");
        top = top + 47;
        nodeListObj_t.setTop(top + "px");
        nodeListObj_t.setState("success");


        String intersectionId = CodeUtils.getRandomNickname(10);//交集id,注意，这个ID是此方法返回的id

        SyncRouterProcess.LineList lineListObj_t = new SyncRouterProcess.LineList();

        lineListObj_t.setFrom(nextId_o);
        lineListObj_t.setTo(intersectionId);
        lineList.add(lineListObj_t);

        //------------------------------第二条线---------------------------------
        SyncRouterProcess.NodeList nodeListObj_th = new SyncRouterProcess.NodeList();

        nodeListObj_th.setId(opTwoId);
        nodeListObj_th.setName(opTwoName);
        nodeListObj_th.setType("OP:dongyin," + opTwoBo + ",1.0");
        nodeListObj_th.setIco("craftRouteList");
        nodeListObj_th.setOperation("OP:dongyin," + opTwoBo + ",1.0");
        nodeListObj_th.setLeft("623px");
        top = top - 47;
        nodeListObj_th.setTop(top + "px");
        nodeListObj_th.setState("success");

        String nextId_t = CodeUtils.getRandomNickname(10);

        SyncRouterProcess.LineList lineListObj_th = new SyncRouterProcess.LineList();

        lineListObj_th.setFrom(opTwoId);
        lineListObj_th.setTo(nextId_t);
        lineList.add(lineListObj_th);

        SyncRouterProcess.NodeList nodeListObj_f = new SyncRouterProcess.NodeList();
        nodeListObj_f.setId(nextId_t);
        nodeListObj_f.setName(opOneName);
        nodeListObj_f.setType("OP:dongyin," + opOneBo + ",1.0");
        nodeListObj_f.setIco("craftRouteList");
        nodeListObj_f.setOperation("OP:dongyin," + opOneBo + ",1.0");
        nodeListObj_f.setLeft("623px");
        top = top + 47;
        nodeListObj_f.setTop(top + "px");
        nodeListObj_f.setState("success");


        nodeList.add(nodeListObj_o);
        nodeList.add(nodeListObj_th);
        nodeList.add(nodeListObj_t);
        nodeList.add(nodeListObj_f);

        SyncRouterProcess.LineList lineListObj_f = new SyncRouterProcess.LineList();

        lineListObj_f.setFrom(nextId_t);
        lineListObj_f.setTo(intersectionId);
        lineList.add(lineListObj_f);

        List<String> ids = Lists.newArrayList();
        ids.add(intersectionId);
        return ids;
    }

    /**
     * 处理正常流程的工序
     * id 流程id
     * operationBo 工序Bo
     * operationName 工序名称
     * isParallelOp 是否是并行节点
     * @return 返回下一个节点流程id（如果是并行节点，则有两条）
     */
    public List<String> saveOrdinaryOp(String id,String operationBo,String operationName,boolean isParallelOp){
        SyncRouterProcess.NodeList nodeListObj = new SyncRouterProcess.NodeList();

        String nextId = CodeUtils.getRandomNickname(10);
        nodeListObj.setId(id);
        if (StringUtils.isBlank(operationBo)){
            top = top + 47;
            nodeListObj.setName("开始流程");
            nodeListObj.setType("timer");
            nodeListObj.setIco("craftRouteStart");
            nodeListObj.setLeft("433px");
            nodeListObj.setTop(top + "px");
        }else {
            top = top + 47;
            nodeListObj.setName(operationName);
            nodeListObj.setType("OP:dongyin," + operationBo + ",1.0");
            nodeListObj.setIco("craftRouteList");
            nodeListObj.setOperation("OP:dongyin," + operationBo + ",1.0");
            nodeListObj.setLeft("433px");
            nodeListObj.setTop(top + "px");
        }
        nodeListObj.setState("success");
        nodeList.add(nodeListObj);

        SyncRouterProcess.LineList lineListObj = new SyncRouterProcess.LineList();
        lineListObj.setFrom(id);
        lineListObj.setTo(nextId);
        lineList.add(lineListObj);
        List<String> nextIds = Lists.newLinkedList();
        if (isParallelOp){
            String nextId_ = CodeUtils.getRandomNickname(10);
            SyncRouterProcess.LineList lineListObj_ = new SyncRouterProcess.LineList();
            lineListObj_.setFrom(id);
            lineListObj_.setTo(nextId_);
            lineList.add(lineListObj_);
            nextIds.add(nextId_);
        }
        syncRouterProcess.setNodeList(nodeList);
        syncRouterProcess.setLineList(lineList);
        nextIds.add(nextId);
        return nextIds;
    }

}
