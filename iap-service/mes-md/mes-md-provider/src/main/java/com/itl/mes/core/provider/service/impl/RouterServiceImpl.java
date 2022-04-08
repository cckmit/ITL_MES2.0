package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.serviceImpl.ImomServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.CodeUtils;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.service.RouterProcessService;
import com.itl.mes.core.api.service.RouterService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import com.itl.mes.core.api.vo.RouterVo;
import com.itl.mes.core.provider.mapper.OperationMapper;
import com.itl.mes.core.provider.mapper.RouterMapper;
import com.itl.mes.core.provider.mapper.RouterProcessMapper;
import com.itl.mes.core.provider.mapper.RouterProcessTableMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 工艺路线
 * </p>
 *
 * @author linjl
 * @since 2021-01-28
 */
@Service
@Transactional
public class RouterServiceImpl extends ImomServiceImpl<RouterMapper, Router> implements RouterService {

    @Autowired
    RouterProcessService routerProcessService;

    @Autowired
    ItemService itemService;

    @Autowired
    CustomDataValService customDataValService;

    @Autowired
    RouterMapper routerMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    RouterProcessMapper routerProcessMapper;

    @Autowired
    RouterProcessTableMapper routerProcessTableMapper;

    public static SyncRouterProcess syncRouterProcess = new SyncRouterProcess();

    public static List<SyncRouterProcess.NodeList> nodeLists = Lists.newArrayList();

    public static List<SyncRouterProcess.LineList> lineList = Lists.newArrayList();

    public static int top;

    public static int positionFlag; //工序位置

    public static int parallelFlag; //工序位置

    /**
     * 获取工艺路线信息
     * */
    public Router getRouter(String bo) throws CommonException {
        Router router = getById(bo);
        if (null == router)
            return null;

        Item item = itemService.getById(router.getItemBo());
        router.setItem(item);
        RouterProcess routerProcess = routerProcessService.getById(bo);
        router.setRouterProcess(routerProcess);

        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(router.getSite(),
                router.getBo(), CustomDataTypeEnum.ROUTER.getDataType());
        router.setCustomDataAndValVoList(customDataAndValVos);
        return router;
    }

    /**
     * 保存工艺路线
     * */
    @Transactional( rollbackFor = {Exception.class,RuntimeException.class} )
    public boolean saveRouter(Router router) throws Exception {

        //判断此条工艺路线是否是当前版本
        if(router.getIsCurrentVersion() == 1){
            //工艺路线为当前版本，更新表中所有为此工艺路线编号的版本为0
            routerMapper.updateByRouter(router.getRouter());
        }

        LambdaQueryWrapper<Router> query = new QueryWrapper<Router>().lambda()
                .and(i -> i.eq(Router::getRouter, router.getRouter())
                        .eq(Router::getSite, router.getSite())
                        .eq(Router::getVersion, router.getVersion()));

        Router queryRouter = routerMapper.selectOne(query);
        boolean isUpdate = false; //是否更新操作
        if (null == queryRouter) {
            router.setCreateDate(DateTime.now());
            router.setCreateUser(UserUtils.getUser());
            try{
                routerMapper.insert(router);
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }

        } else {
            if(queryRouter.getBo().equals(router.getBo())) {
                router.setModifyDate(DateTime.now());
                router.setModifyUser(UserUtils.getUser());
                // 更新前检测工序工单表中是否已经存在该条工艺路线
                int count=routerMapper.isExistRouter(router.getRouter(),router.getVersion());
                if(count>0){
                    //工序工单中存在工艺路线,工艺路线被引用不允许直接修改
                    throw new CommonException("该条工艺路线被引用不允许直接修改",30002);
                }
                routerMapper.updateById(router);
                isUpdate = true;
            }else {
                throw new Exception(String.format("工厂%s中已存在工艺路线%s", router.getSite(),router.getRouter()));
            }
        }

        RouterProcess routerProcess = router.getRouterProcess();
        if (null != routerProcess) {
            routerProcess.setSite(router.getSite());
            routerProcess.setRouterBo(router.getBo());
            if (isUpdate) {
                routerProcessService.updateById(routerProcess);
            } else {
                routerProcessService.save(routerProcess);
            }
            //根据router_bo，删除工艺路线表(m_router_process_table)中的数据
            routerProcessTableMapper.delete(new QueryWrapper<RouterProcessTable>().eq("router_bo",router.getBo()));
            // 解析工艺路线，并将所有数据存入
            if(routerProcess.getProcessInfo() !=null){
                String processInfo=routerProcess.getProcessInfo();
                JSONObject processInfoObject= JSON.parseObject(processInfo);
                if(processInfoObject.get("nodeList")!=null && processInfoObject.get("lineList") !=null){
                    JSONArray nodeList = JSONArray.parseArray(processInfoObject.get("nodeList").toString());
                    JSONArray lineList=JSONArray.parseArray(processInfoObject.get("lineList").toString());
                    if(nodeList.size()>0 && lineList.size()>0){
                        Map<String,JSONObject> nodeMap=new HashMap<>();
                        // nodeList 转map
                        String startId=listToMap(nodeMap,nodeList);
                        // 将数据存入表中，并返回工序BO集合
                        List<String> operationList=getOperationList(nodeMap,startId,lineList,router.getBo());
                    }
                }
            }
            // 将工艺路线中对应的bo全部设置为已引用
            List<String> operationBoList=getAllOperationBo(routerProcess);
            for(String operationBo:operationBoList){
                routerProcessMapper.updateIsUsed(operationBo);
            }
        }

        //保存自定义数据
        List<CustomDataValVo> customDataValVoList = router.getCustomDataValVoList();
        if (null != customDataValVoList) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(router.getBo());
            customDataValRequest.setSite(router.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.ROUTER.getDataType());
            customDataValRequest.setCustomDataValVoList(customDataValVoList);
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
        return true;
    }
    public List<String> getAllOperationBo(RouterProcess routerProcess){
        List<String> operationBoList=new ArrayList<>();
        // 解析routerProcess中的所有的工序BO
        String processInfo=routerProcess.getProcessInfo();
        JSONObject jsonObject= JSON.parseObject(processInfo);
        Object obj=jsonObject.get("nodeList");
        if(obj !=null){
            if(obj.toString() !=null && obj.toString() !=""){
                JSONArray nodeList=JSONArray.parseArray(obj.toString());
                for(Object node:nodeList){
                    JSONObject operation=JSON.parseObject(node.toString());
                    if(operation.get("operation") !=null && operation.get("operation") !=""){
                        String bo=operation.get("operation").toString();
                        operationBoList.add(bo);
                    }
                }
            }
        }
        return operationBoList;
    }
    /**
     * 删除工艺路线
     *
     * @param router 工艺路线
     * @throws CommonException 异常
     */
    @Override
    public void deleteRouter(Router router) throws CommonException {
        LambdaQueryWrapper<Router> query = new QueryWrapper<Router>().lambda()
            .eq(Router::getBo, router.getBo());
        Router routerEntity=routerMapper.selectById(router.getBo());
        // 删除工艺路线前校验当前工艺路线是否被引用
        int count=routerMapper.isExistRouter(routerEntity.getRouter(),routerEntity.getVersion());
        if(count>0){
            //工序工单中存在工艺路线,工艺路线被引用不允许直接删除
            throw new CommonException("该条工艺路线被引用不允许删除",30002);
        }
        //删除工艺路线
        Integer integer = routerMapper.delete(query);

        // 同步删除工艺路线流程信息
        routerProcessMapper.deleteById(router.getBo());

        // 同步删除工艺路线表中信息
        routerProcessTableMapper.delete(new QueryWrapper<RouterProcessTable>().eq("router_bo",router.getBo()));

        //删除自定义数据
        customDataValService.deleteCustomDataValByBoAndType(UserUtils.getSite(), router.getBo(), CustomDataTypeEnum.ROUTER);
    }

    @Override
    public Router getRouterByRouter(String router) throws CommonException {
        Router routerEntity=routerMapper.selectOne(new QueryWrapper<Router>().eq("ROUTER",router).eq("IS_CURRENT_VERSION",1));
        return routerEntity;
    }

    @Override
    public String importRouter(MultipartFile file) throws IOException, CommonException {
        //创建excel工作对象
        Workbook workbook = null;
        //要读取的文件名
        FileInputStream fis = (FileInputStream) file.getInputStream();
        //判断当前excel是哪一个版本的，如果不是excel，返回null
        String fileName = file.getOriginalFilename();
        String substring = fileName.substring(fileName.indexOf(".") + 1);
        if ("xls".equals(substring)) {
            //2003版
            workbook = new HSSFWorkbook(fis);
        } else if ("xlsx".equals(substring)) {
            //2007版
            workbook = new XSSFWorkbook(fis);
        } else {
            //未知内容,返回null
            return "非excel文件";
        }
        Sheet sheetAt = workbook.getSheetAt(0);

        if (sheetAt == null) {
            return null;
        }

        //创建一个集合用来返回所有的读取到excel中所有的结果
        Set<Integer> lists = new HashSet<>();
        //获取到excel最后一行的下标
        int lastRowNum = sheetAt.getLastRowNum();
        Row row = sheetAt.getRow(0);
        //循环遍历读取excel中的内容
        for (int i = 1; i <= lastRowNum; i++) {
            //获取到每一行
            row = sheetAt.getRow(i);
            List<Object> list = new ArrayList<>();
            // 查询数据库是否存在当前工艺路线编码
            //第一列为工艺路线编码
            String router=row.getCell(0).toString();
            //第二列为工艺路线名称
            String routerName=row.getCell(1).toString();
            Router routerEntity=routerMapper.selectOne(new QueryWrapper<Router>().eq("router",router));
            if(routerEntity==null){
                routerEntity=new Router();
                //为空则新建工艺路线数据,默认版本为1.0
                routerEntity.setBo("ROUTER:dongyin,"+router+",1.0");
                routerEntity.setRouter(router);
                routerEntity.setRouterName(routerName);
                routerEntity.setVersion("1.0");
                routerEntity.setSite("dongyin");
                routerEntity.setCreateDate(new Date());
                routerEntity.setCreateUser("外部导入"); //待定
                routerEntity.setRouterType("S");
                routerEntity.setIsCurrentVersion(1);
                routerEntity.setState("N");
                routerMapper.insert(routerEntity);
            }

            List<RouterProcessTable> routerProcessTables=routerProcessTableMapper.
                    selectList(new QueryWrapper<RouterProcessTable>()
                            .eq("router_bo",routerEntity.getBo()));
            if(routerProcessTables !=null && routerProcessTables.size()>0){
                //清空m_router_process_table中改router_bo的数据
                routerProcessTableMapper.delete(new QueryWrapper<RouterProcessTable>().eq("router_bo",routerEntity.getBo()));
            }
            //刷新数据
            nodeLists.clear();
            lineList.clear();
            syncRouterProcess.setLineList(null);
            syncRouterProcess.setNodeList(null);
            top=0;
            positionFlag=0;
            parallelFlag=0;
            //获得开始节点
            String fromId=createStart();
            for (int j = row.getFirstCellNum()+2; j < row.getLastCellNum() + 1; j++) {
                //判断每一个单元格的内容是否为空，如果为空，则默认当前无工序
                if(row.getCell(j)==null){
                    //跳过当前循环
                    continue;
                }
                //生成一个工序节点
                fromId=generateOp(routerEntity.getBo(),fromId,row.getCell(j).toString());
                if(fromId.equals("error")){
                    //当前行有错误的工序数据
                    lists.add(i);
                    break;//跳出当前循环
                }
            }
            // 生成一条工艺路线，转json格式
            syncRouterProcess.setNodeList(nodeLists);
            syncRouterProcess.setLineList(lineList);
            String routerProcess = JSONObject.toJSONString(syncRouterProcess);
            RouterProcess routerProcessEntity=routerProcessMapper.selectById(routerEntity.getBo());
            if(routerProcessEntity !=null){
                //删掉，重新导入
                routerProcessMapper.deleteById(routerProcessEntity.getRouterBo());
            }
            routerProcessEntity=new RouterProcess();
            routerProcessEntity.setRouterBo(routerEntity.getBo());
            routerProcessEntity.setSite("dongyin");
            routerProcessEntity.setProcessInfo(routerProcess);
            routerProcessMapper.insert(routerProcessEntity);
        }
        String rowInfo="第";
        if(lists !=null && lists.size()>0){
            for(Integer rows:lists){
                rowInfo+=(rows+1)+",";
            }
            rowInfo+="行工序数据错误，未生成工艺路线数据,请检查后重新导入!";
        }else{
            rowInfo="success";
        }
        return rowInfo;
    }

    /**
     * 获取工艺路线中的nodeList转Map集合
     * @param nodeMap
     * @param nodeList
     * @return
     */
    public String listToMap(Map<String,JSONObject> nodeMap, JSONArray nodeList){
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

    public List<String> getOperationList(Map<String,JSONObject> nodeMap,String startId,JSONArray lineList,String routerBo){
        List<String> operationList=new ArrayList<>();
        Map<String,List<JSONObject>> lineMap=new HashMap<>();
        // lineList 转map
        for(Object obj:lineList){
            JSONObject jsonObject=JSON.parseObject(obj.toString());
            List<JSONObject> jsonObjectList=null;
            if(lineMap.get(jsonObject.get("from").toString())==null){
                // 无数据，初始化,非并行工序
                jsonObjectList=new ArrayList<>();
                jsonObjectList.add(jsonObject);

            }else{
                // 有数据新增,并行工序
                jsonObjectList=lineMap.get(jsonObject.get("from").toString());
                jsonObjectList.add(jsonObject);
            }
            lineMap.put(jsonObject.get("from").toString(),jsonObjectList);
        }
        // 第一个节点
        List<JSONObject> lineObjects=lineMap.get(startId);
        int parallelFlag=1; // 并行标识
        int position=1; // 工序位置
        while(true){
            boolean isSave=true; // 是否需要存到表中
            if(lineObjects.size()==1){
                // 非并行工序
                JSONObject operationObj=nodeMap.get(lineObjects.get(0).get("to").toString());
                if(operationObj !=null && operationObj.get("operation") !=null){
                    String operationBo=operationObj.get("operation").toString();
                    if(operationList.size()>0){
                        for(String operationStr:operationList){
                            if(operationStr.equals(operationBo)){
                                isSave=false; // 集合中存在该工序，无需存储
                            }
                        }
                    }
                    if(isSave){
                        operationList.add(operationBo);
                        //生成routerProcessTable对象,存入表中
                        RouterProcessTable routerProcessTable=new RouterProcessTable();
                        routerProcessTable.setRouterBo(routerBo);
                        routerProcessTable.setOperationBo(operationBo);
                        routerProcessTable.setPositionFlag(position);
                        routerProcessTable.setParallelFlag(0);//0代表非并行工序
                        routerProcessTable.setCreateDate(new Date());
                        routerProcessTableMapper.insert(routerProcessTable);
                    }
                }
            }else if(lineObjects.size()==2){
                // 并行工序
                for(int i=0;i<2;i++){
                    JSONObject operationObj=nodeMap.get(lineObjects.get(i).get("to").toString());
                    if(operationObj !=null && operationObj.get("operation") !=null){
                        String operationBo=operationObj.get("operation").toString();
                        if(operationList.size()>0){
                            for(String operationStr:operationList){
                                if(operationStr.equals(operationBo)){
                                    isSave=false; // 集合中存在该工序，无需存储
                                }
                            }
                        }
                        if(isSave){
                            operationList.add(operationBo);
                            //生成routerProcessTable对象,存入表中
                            RouterProcessTable routerProcessTable=new RouterProcessTable();
                            routerProcessTable.setRouterBo(routerBo);
                            routerProcessTable.setOperationBo(operationBo);
                            routerProcessTable.setPositionFlag(position);
                            routerProcessTable.setParallelFlag(parallelFlag);//0代表非并行工序
                            routerProcessTable.setCreateDate(new Date());
                            routerProcessTableMapper.insert(routerProcessTable);
                        }
                    }
                }
                parallelFlag++;
            }
            if(isSave){
                position++; // 只有在存入表中，position才会增加
            }
            // 下一个工序
            if(lineMap.get(lineObjects.get(0).get("to").toString())==null){
                break;
            }
            lineObjects=lineMap.get(lineObjects.get(0).get("to").toString());
        }
        return operationList;
    }


    /**
     * 生成工艺路线数据
     * @param routerBo
     * @param fromId
     * @param operationObj
     * @return
     * @throws CommonException
     */
    public String generateOp(String routerBo,String fromId,String operationObj) throws CommonException {
        //查看当前单元格是否存在并行工序
        if(operationObj.contains("<>")){
            String originalId=fromId;
            String fromIds=""; //放置两个工序对应的id
            //存在并行工序
            String[] parallelOps=operationObj.split("<>");//并行工序只能是两个
            String operationOne=parallelOps[0];
            String operationTwo=parallelOps[1];
            //第一道工序
            Map<String,String> operationOneMap=splitOperationInfo(operationOne);
            //第二道工序
            Map<String,String> operationTwoMap=splitOperationInfo(operationTwo);
            if(operationOneMap.get("error") !=null || operationTwoMap.get("error") !=null){
                fromId="error"; //赋值错误标识
                return fromId;
            }
            //第一道线
            fromId=createSingleNode(fromId,operationOneMap.get("operation"),operationOneMap.get("operationName"),433-115);
            fromId=createSingleNode(fromId,operationTwoMap.get("operation"),operationTwoMap.get("operationName"),433-115);
            fromIds+=fromId;
            //第二道线,最初的fromId已被覆盖，使用originalId
            //设置高-94
            top-=94;
            fromId=createSingleNode(originalId,operationTwoMap.get("operation"),operationTwoMap.get("operationName"),433+115);
            fromId=createSingleNode(fromId,operationOneMap.get("operation"),operationOneMap.get("operationName"),433+115);
            fromIds=fromIds+","+fromId;

            //插入m_router_process_table表中
            //并行工序，并行标识+1
            parallelFlag++;
            insertRouterProcessTrable(routerBo,operationOneMap.get("operationBo"),positionFlag,parallelFlag);
            insertRouterProcessTrable(routerBo,operationTwoMap.get("operationBo"),positionFlag,parallelFlag);
            //顺序位置+1
            positionFlag++;
            return fromIds;
        }else{
            //单条工序
            Map<String,String> operationMap=splitOperationInfo(operationObj);
            if(operationMap.get("error") !=null){
                fromId = "error"; //赋值错误标识
                return fromId;
            }
            fromId=createSingleNode(fromId,operationMap.get("operation"),operationMap.get("operationName"),433);
            //插入m_router_process_table表中
            insertRouterProcessTrable(routerBo,operationMap.get("operationBo"),positionFlag,0);
            //顺序位置+1
            positionFlag++;
        }
        return fromId;
    }

    /**
     * 将工艺路线数据插入routerProcessTable表中
     * @param routerBo  工艺路线bo
     * @param operationBo 工序bo
     */
    public void insertRouterProcessTrable(String routerBo,String operationBo,int positionFlagPT,int parallelFlagPT){
        RouterProcessTable routerProcessTable=new RouterProcessTable();
        routerProcessTable.setRouterBo(routerBo);
        routerProcessTable.setOperationBo(operationBo);
        routerProcessTable.setPositionFlag(positionFlagPT);
        routerProcessTable.setParallelFlag(parallelFlagPT);
        routerProcessTable.setCreateDate(new Date());
        routerProcessTableMapper.insert(routerProcessTable);
    }

    /**
     * 拆分工序信息
     * @param operationObj
     * @return  将工序信息作为key-value返回
     * @throws CommonException
     */
    public Map<String,String> splitOperationInfo(String operationObj) throws CommonException {
        Map<String,String> operationInfoMap=new HashMap<>();
        //查询当前工序是否存在,不存在则创建
        String[] obj=operationObj.split("&");
        if(obj.length !=2){//认定为无效工序 缺少工序编码或名称或存在多个&
            operationInfoMap.put("error","error");
            return operationInfoMap;
        }
        String operation=obj[0];
        String operationName=obj[1];
        operationInfoMap.put("operation",operation);
        operationInfoMap.put("operationName",operationName);
        //是否存在工序，不存在则新建
        String operationBo=createOp(operation,operationName);
        operationInfoMap.put("operationBo",operationBo);
        return operationInfoMap;
    }
    /**
     * 新建节点
     * @param operation
     * @param operationName
     */
    public String createSingleNode(String fromId,String operation,String operationName,int left){
        SyncRouterProcess.NodeList nodeListObj = new SyncRouterProcess.NodeList();
        //node对象
        String nextId = CodeUtils.getRandomNickname(10);
        nodeListObj.setId(nextId);
        top = top + 47;
        nodeListObj.setName(operationName);
        nodeListObj.setType("OP:dongyin," + operation + ",1.0");
        nodeListObj.setIco("craftRouteList");
        nodeListObj.setOperation("OP:dongyin," + operation + ",1.0");

        nodeListObj.setLeft(left + "px");
        nodeListObj.setTop(top + "px");
        nodeLists.add(nodeListObj);

        // line对象
        //判断否上一节点是并行工序
        if(fromId.contains(",")){
            //并行工序
            String[] fromIds=fromId.split(",");
            for(int i=0;i<2;i++){
                SyncRouterProcess.LineList lineListObj = new SyncRouterProcess.LineList();
                lineListObj.setFrom(fromIds[i]);
                lineListObj.setTo(nextId);
                lineList.add(lineListObj);
            }
        }else{
            SyncRouterProcess.LineList lineListObj = new SyncRouterProcess.LineList();
            lineListObj.setFrom(fromId);
            lineListObj.setTo(nextId);
            lineList.add(lineListObj);
        }
        return nextId;
    }

    /**
     * 创建开始节点
     * @return nextId作为下一个节点的fromId
     */
    public String createStart(){
        SyncRouterProcess.NodeList nodeListObj = new SyncRouterProcess.NodeList();
        //node对象
        String nextId = CodeUtils.getRandomNickname(10);
        nodeListObj.setId(nextId);
        top = top + 47;
        nodeListObj.setName("开始流程");
        nodeListObj.setType("timer");
        nodeListObj.setIco("craftRouteStart");
        nodeListObj.setLeft("433px");
        nodeListObj.setTop(top + "px");
        //保存到nodeLists中
        nodeLists.add(nodeListObj);
        return nextId;
    }

    /**
     * 不存在创建工序
     * @param operation
     * @param operationName
     */
    public String createOp(String operation,String operationName){

        Operation operationEntity=operationMapper.selectOne(new QueryWrapper<Operation>().eq("operation",operation));
        if(operationEntity==null){
            operationEntity=new Operation();
            //生成工序BO
            String operationBo= "OP:dongyin," + operation + ",1.0";
            operationEntity.setBo(operationBo);
            operationEntity.setCreateUser("导入工序"); //待定
            operationEntity.setSite("dongyin");
            operationEntity.setOperation(operation);
            operationEntity.setVersion("1.0");
            operationEntity.setOperationName(operationName);
            operationEntity.setState("N");
            operationEntity.setCreateDate(new Date());
            operationEntity.setStationTypeBo("ST:dongyin,过站采集");
            //插入一个新建工序
            operationMapper.insert(operationEntity);
        }
        return operationEntity.getBo();
    }
}
