package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.ShopOrderDTO;
import com.itl.mes.core.api.dto.ShopOrderReportDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderReportVo;
import com.itl.mes.core.provider.mapper.*;

import com.itl.mes.core.provider.util.KeyWordUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 工单表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Service
@Transactional
public class ShopOrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrder> implements ShopOrderService {


    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BomService bomService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private CustomDataValMapper customDataValMapper;

    @Autowired
    private ItemGroupService itemGroupService;

    @Resource
    private UserUtil userUtil;

    @Autowired
    private OperationOrderMapper operationOrderMapper;

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private RouteStationService routeStationService;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private RouterService routerService;

    @Autowired
    private WorkShopMapper workShopMapper;

    @Autowired
    private ProductLineMapper productLineMapper;

    @Autowired
    private SfcService sfcService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private RouterMapper routerMapper;

    @Autowired
    private EnterMapper enterMapper;

    @Autowired
    private SfcMapper sfcMapper;

    @Autowired
    private StemDispatchMapper stemDispatchMapper;
    @Override
    public List<ShopOrder> selectList() {
        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<ShopOrder>();
        //getEntityWrapper(entityWrapper, shopOrder);
        return super.list(entityWrapper);
    }

    /**
     * 通过ShopOrderHandleBO查询存在的工单
     *
     * @param shopOrderHandleBO 工单BOHandle
     * @return ShopOrder
     * @throws CommonException 扔异常
     */
    @Override
    public ShopOrder getExistShopOrder(ShopOrderHandleBO shopOrderHandleBO) throws CommonException {
        ShopOrder shopOrder = super.getById(shopOrderHandleBO.getBo());
        if (shopOrder == null) {
            throw new CommonException("工单" + shopOrderHandleBO.getShopOrder() + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return shopOrder;
    }

    /**
     * 通过ShopOrderHandleBO查询工单
     *
     * @param shopOrderHandleBO 工单BOHandle
     * @return 异常
     */
    @Override
    public ShopOrder getShopOrder(ShopOrderHandleBO shopOrderHandleBO) {
        return super.getById(shopOrderHandleBO.getBo());
    }


    /**
     * 查询工单相关数据
     *
     * @param shopOrderHandleBO 工单BOHandle
     * @return ShopOrderFullVo
     * @throws CommonException 异常
     */
    @Override
    public ShopOrderFullVo getShopFullOrder(ShopOrderHandleBO shopOrderHandleBO) throws CommonException {

        ShopOrder shopOrder = getExistShopOrder(shopOrderHandleBO);
        ShopOrderFullVo shopOrderFullVo = new ShopOrderFullVo();
        BeanUtils.copyProperties(shopOrder, shopOrderFullVo);
        if (StringUtils.isNotBlank(shopOrder.getWorkShop())){
            WorkShop workShop=workShopMapper.selectOne(new QueryWrapper<WorkShop>().eq("work_shop",shopOrder.getWorkShop()));
            if(workShop !=null) {
                shopOrderFullVo.setWorkShopName(workShop.getWorkShopDesc());
            }
        }
        ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrder.getItemBo());
        shopOrderFullVo.setItem(itemHandleBO.getItem());
        shopOrderFullVo.setItemVersion(itemHandleBO.getVersion());
        shopOrderFullVo.setState(new StatusHandleBO(shopOrder.getStateBo()).getState());

        //组装客户订单
        /*if (!StrUtil.isBlank(shopOrder.getCustomerOrderBo())) {

            CustomerOrderHandleBO customerOrderHandleBO = new CustomerOrderHandleBO(shopOrder.getCustomerOrderBo());
            shopOrderFullVo.setCustomerOrder(customerOrderHandleBO.getCustomerOrder());
        }*/

        //组装BOM
        if (!StrUtil.isBlank(shopOrder.getBomBo())) {

            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getBomBo());
            shopOrderFullVo.setBom(bomHandleBO.getBom());
            shopOrderFullVo.setBomVersion(bomHandleBO.getVersion());
        }
        //组装工艺路线
        if (!StrUtil.isBlank(shopOrder.getRouterBo())) {
            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            shopOrderFullVo.setRouter(routerHandleBO.getRouter());
            shopOrderFullVo.setRouterVersion(routerHandleBO.getVersion());
        }
        /*if (!StrUtil.isBlank(shopOrder.getRouterBo())) {

            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            //根据工艺路线的编号，查询表中为当前版本的工艺路线
            Router router= routerService.getRouterByRouter(routerHandleBO.getRouter());
            // 如果工单中的工艺路线不为当前版本
            if(router ==null){
                // 查询根据关联物料查询当前版本的工艺路线
               Item item=itemMapper.selectOne(new QueryWrapper<Item>().eq("item",shopOrderFullVo.getItem()));
               if(item !=null){
                   if(item.getRouterName()!=null && item.getRouterName()!=""){
                       Router routerItem=routerService.getRouterByRouter(item.getRouterName());
                       if(routerItem !=null){
                           shopOrderFullVo.setRouter(routerItem.getRouter());
                           shopOrderFullVo.setRouterVersion(routerItem.getVersion());
                       }
                   }

               }
            }else {
                shopOrderFullVo.setRouter(router.getRouter());
                shopOrderFullVo.setRouterVersion(router.getVersion());
            }
            *//*shopOrderFullVo.setRouter(routerHandleBO.getRouter());
            shopOrderFullVo.setRouterVersion(routerHandleBO.getVersion());*//*
        }else {
            Item item=itemMapper.selectOne(new QueryWrapper<Item>().eq("item",shopOrderFullVo.getItem()));
            if(item !=null){
                if(item.getRouterName()!=null && item.getRouterName()!=""){
                    Router routerItem=routerService.getRouterByRouter(item.getRouterName());
                    if(routerItem !=null){
                        shopOrderFullVo.setRouter(routerItem.getRouter());
                        shopOrderFullVo.setRouterVersion(routerItem.getVersion());
                    }
                }

            }
        }*/
        //组装产线
        if (!StrUtil.isBlank(shopOrder.getProductLineBo())) {

            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrder.getProductLineBo());
            shopOrderFullVo.setProductLine(productLineHandleBO.getProductLine());
            shopOrderFullVo.setProductLineDesc(productLineMapper.selectOne(new QueryWrapper<ProductLine>().eq("PRODUCT_LINE",productLineHandleBO.getProductLine())).getProductLineDesc());
        }

        //获取物料描述
        Item exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(shopOrder.getItemBo()));
        shopOrderFullVo.setItemDesc(exitsItem.getItemDesc());
        shopOrderFullVo.setItemName(exitsItem.getItemName());
        shopOrderFullVo.setDrawingNo(exitsItem.getDrawingNo());
        shopOrderFullVo.setCustomDataAndValVoList(customDataValMapper.selectCustomDataAndValByBoAndDataTypeFast(shopOrderHandleBO.getSite(),
                shopOrderHandleBO.getBo(), CustomDataTypeEnum.SHOP_ORDER.getDataType()));
        return shopOrderFullVo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void orderReleaseBatch(List<ShopOrderDTO> shopOrderDTOs){
        shopOrderDTOs.forEach(
                shopOrderDTO -> {
                    //校验工单可下达数量
                    ShopOrder shopOrderEntity = shopOrderMapper.selectById(shopOrderDTO.getShopOrderBo());
                    if (ShopOrder.FINISHED_STATE.equals(shopOrderEntity.getStateBo()) || ShopOrder.CLOSE_STATE.equals(shopOrderEntity.getStateBo())){
                        try {
                            throw new CommonException("工单号为:" + shopOrderDTO.getShopOrder() + "的工单状态为已关闭或已完成，不能下达",CommonExceptionDefinition.VERIFY_EXCEPTION);
                        } catch (CommonException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    BigDecimal releasable;
                    if ("5422".equals(shopOrderEntity.getWorkShop()) || "5424".equals(shopOrderEntity.getWorkShop()) /*|| "544".equals(shopOrderEntity.getWorkShop())*/){
                        releasable = shopOrderEntity.getCompleteSetQty().subtract(shopOrderEntity.getReleaseQty());
                    }else {
                        releasable = shopOrderEntity.getOrderQty().subtract(shopOrderEntity.getReleaseQty());
                    }
                    if (shopOrderDTO.getOperationOrderQty().compareTo(releasable) == 1){
                        try {
                            throw new CommonException("工单号为:" + shopOrderDTO.getShopOrder() + "的工单下达数量大于可下达数量",CommonExceptionDefinition.VERIFY_EXCEPTION);
                        } catch (CommonException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    OperationOrder operationOrder = new OperationOrder();
                    QueryWrapper<OperationOrder> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("shop_order",shopOrderDTO.getShopOrder()).orderByDesc("CREATE_DATE");
                    List<OperationOrder> operationOrders = operationOrderMapper.selectList(queryWrapper);
                    String number = "";
                    if (!CollectionUtils.isEmpty(operationOrders)){//如果对应工单号在表中存在数据，则取出最后一条工序单号的尾数，新建的工序工单的ID尾号就为number + 1
                        for (String s : operationOrders.get(0).getOperationOrder().split(",")) {
                            number = s;
                        }
                        int numberLast = Integer.parseInt(number) + 1;
                        operationOrder.setBo("OO:" + shopOrderDTO.getShopOrder() + "," + numberLast);
                        operationOrder.setOperationOrder(shopOrderDTO.getShopOrder() + "," + numberLast);
                    }else {//如果不存在，则number从1开始
                        operationOrder.setBo("OO:" + shopOrderDTO.getShopOrder() + ",1");
                        operationOrder.setOperationOrder(shopOrderDTO.getShopOrder() + ",1");
                    }

                    ShopOrderDTO shopOrder = new ShopOrderDTO();
                    List<String> bos = new ArrayList<>();
                    bos.add(shopOrderDTO.getShopOrderBo());
                    shopOrder.setBos(bos);
                    shopOrder.setSite(UserUtils.getSite());
                    List<ShopOrder> records = shopOrderMapper.getShopOrder(shopOrderDTO.getShopOrderBo());//根据工单号查询相应数据

                    operationOrder.setShopOrder(shopOrderDTO.getShopOrder());
//                    operationOrder.setOperationOrderState("");//暂定

                    operationOrder.setOperationOrderQty(shopOrderDTO.getOperationOrderQty());

                    String router = records.get(0).getRouter();//工艺路线编码
                    String routerVersion = records.get(0).getVersion();//工艺路线版本
                    String routerBo = records.get(0).getRouterBo();//工艺路线BO
                    if (StringUtils.isBlank(records.get(0).getRouter()) || StringUtils.isBlank(records.get(0).getVersion())){
                        //如果工单上没有工艺路线，则去物料表上找，物料上也没有，则返回异常
                        Item itemObj = itemService.getById(records.get(0).getItemBo());

                        QueryWrapper<Router> qw = new QueryWrapper<>();
                        qw.eq("router",itemObj.getRouterName());
                        qw.eq("is_current_version",1);//当前版本
                        Router routerObj = routerMapper.selectOne(qw);

                        if (ObjectUtil.isEmpty(routerObj)){
                            try {
                                throw new CommonException("工单号为:" + shopOrderDTO.getShopOrder() + "的工单工艺路线未维护,或工艺路线没有设置最新版本！",CommonExceptionDefinition.VERIFY_EXCEPTION);
                            } catch (CommonException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        router = routerObj.getRouter();
                        routerVersion = routerObj.getVersion();
                        routerBo = routerObj.getBo();
                    }
                    operationOrder.setRoutre(router);
                    operationOrder.setVersion(routerVersion);//工艺路线版本
                    operationOrder.setItem(records.get(0).getItem());
                    operationOrder.setItemName(records.get(0).getItemName());
                    operationOrder.setIterVersion(records.get(0).getItemVersion());
                    operationOrder.setWorkShop(records.get(0).getWorkShop());
                    operationOrder.setIsUrgent(shopOrderDTO.getIsUrgent());

                    operationOrder.setCreateUser(userUtil.getUser().getUserName());
                    operationOrder.setCreateDate(new Date());

                    //保存工序工单信息
                    operationOrderMapper.insert(operationOrder);
                    ShopOrder shopOrderObj = new ShopOrder();
                    shopOrderObj.setReleaseQty(records.get(0).getReleaseQty().add(shopOrderDTO.getOperationOrderQty()));
                    shopOrderObj.setStateBo("STATE:dongyin,501");
                    shopOrderObj.setRouterBo(routerBo);
                    //更新工单表已下达数量
                    shopOrderMapper.update(shopOrderObj,new QueryWrapper<ShopOrder>().eq("bo",shopOrderDTO.getShopOrderBo()));
                    insertNoDispatch(operationOrder,router,routerVersion,shopOrderDTO.getShopOrderBo());
                }
        );
    }

    /**
     * 新增不需要派工的工序工单到m_dispatch表中（一个工序一条数据）
     */
    public void insertNoDispatch(OperationOrder operationOrder,String router,String routerVersion,String shopOrderBo){
        //查询该工单对应的工艺路线对应的所有工序集合
        String processInfo = routeStationService.queryRouterOperation(router,routerVersion);

        JSONObject jsonObj = JSON.parseObject(processInfo);
        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        JSONArray lineList = JSONArray.parseArray(jsonObj.getString("lineList"));

        List<String> operationList = Lists.newArrayList();
        if (nodeList.size() > 0){
            for (int i=0;i<nodeList.size();i++){
                JSONObject operationObj = JSON.parseObject(nodeList.get(i).toString());
                //获取工序
                String operation = operationObj.getString("operation");
                if (StringUtils.isNotBlank(operation)){
                    operationList.add(operation);
                }
            }
        }
        String startId = sfcServiceImpl.getStartId(nodeList);
        String nextId = sfcServiceImpl.getNextId(lineList, startId);
        String theFirstOp = sfcServiceImpl.getOperationById(nodeList,nextId);//首工序

        if (!CollectionUtils.isEmpty(operationList)){
            //并行工序去重
            List<String> collect = operationList.stream().distinct().collect(Collectors.toList());
            int number = 1;
            for (String operation : collect) {
                String isSendToD = operationMapper.isSendToDeviceByOperationBo(operation);
                Dispatch dispatch = new Dispatch();
                dispatch.setId(UUID.uuid32());
                dispatch.setOperationOrder(operationOrder.getOperationOrder());
                dispatch.setOperationBo(operation);
                dispatch.setOperationOrderQty(operationOrder.getOperationOrderQty());
                dispatch.setItem(operationOrder.getItem());
                dispatch.setItemBo("ITEM:dongyin," + operationOrder.getItem() + ",1.0");
                dispatch.setCreateDate(new Date());
                dispatch.setCreateUser(userUtil.getUser().getUserName());
                dispatch.setWaitIn(operationOrder.getOperationOrderQty());
                dispatch.setShopOrderBo(shopOrderBo);
                if ("yes".equals(isSendToD)){//需要派工到设备的工序
                    dispatch.setIsNeedDispatch("0");
                }
                dispatch.setNotDoneQty(operationOrder.getOperationOrderQty());
                //判断是否是首工序
                if (operation.equals(theFirstOp)){
                    dispatch.setIsFirstOperation("1");
                }
                dispatch.setCanPrintQty(operationOrder.getOperationOrderQty());
                //生成派工单号,规则：工序工单 - 1、2、3....
                if (!"yes".equals(isSendToD)){
                    dispatch.setDispatchCode(operationOrder.getOperationOrder() + "-" + number);
                    number++;
                }

                dispatchMapper.insert(dispatch);
            }
        }
    }
    /**
     * 保存工单相关数据
     *
     * @param shopOrderFullVo 工单shopOrderFullVo
     * @throws CommonException 异常
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveShopOrder(ShopOrderFullVo shopOrderFullVo) throws CommonException {

        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderFullVo.getShopOrder());
        ShopOrder shopOrder = getShopOrder(shopOrderHandleBO);
        if (shopOrder == null) { //新增

            insertShopOrder(shopOrderFullVo);
        } else { //更新

            updateShopOrder(shopOrder, shopOrderFullVo);
        }
        //保存自定义数据
        if (shopOrderFullVo.getCustomDataValVoList() != null) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(shopOrderHandleBO.getBo());
            customDataValRequest.setSite(shopOrderHandleBO.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.SHOP_ORDER.getDataType());
            customDataValRequest.setCustomDataValVoList(shopOrderFullVo.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
    }


    /**
     * 新增
     *
     * @param shopOrderFullVo shopOrderFullVo
     * @throws CommonException 异常
     */
    private void insertShopOrder(ShopOrderFullVo shopOrderFullVo) throws CommonException {

        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderFullVo.getShopOrder());
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setBo(shopOrderHandleBO.getBo());
        shopOrder.setSite(shopOrderHandleBO.getSite());
        shopOrder.setShopOrder(shopOrderFullVo.getShopOrder());
        shopOrder.setOrderDesc(StrUtil.isBlank(shopOrderFullVo.getOrderDesc()) ? shopOrderFullVo.getShopOrder() : shopOrderFullVo.getOrderDesc());
        shopOrder.setStateBo(new StatusHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getState()).getBo());
        shopOrder.setIsOverfulfill(shopOrderFullVo.getIsOverfulfill());
        shopOrder.setOrderQty(shopOrderFullVo.getOrderQty());

        saveEndTime(shopOrderFullVo,shopOrder);

        shopOrder.setOverfulfillQty(shopOrderFullVo.getOverfulfillQty());
        shopOrder.setOrderDeliveryTime(shopOrderFullVo.getOrderDeliveryTime());
        shopOrder.setNegotiationTime(shopOrderFullVo.getNegotiationTime());
        shopOrder.setFixedTime(shopOrderFullVo.getFixedTime());
        shopOrder.setWorkShop(shopOrderFullVo.getWorkShop());
        ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getItem(), shopOrderFullVo.getItemVersion());
        itemService.getExitsItemByItemHandleBO(itemHandleBO); //获取存在的物料
        shopOrder.setItemBo(itemHandleBO.getBo());

        //验证物料清单是否存在
        if (!StrUtil.isBlank(shopOrderFullVo.getBom()) && !StrUtil.isBlank(shopOrderFullVo.getBomVersion())) {

            Bom bom = bomService.selectByBom(shopOrderFullVo.getBom(), shopOrderFullVo.getBomVersion());
            shopOrder.setBomBo(bom.getBo());

        }
        if (StringUtils.isNotBlank(shopOrderFullVo.getRouter()) && StringUtils.isNotBlank(shopOrderFullVo.getRouterVersion())){
            shopOrder.setRouterBo(new RouterHandleBO(UserUtils.getSite(), shopOrderFullVo.getRouter(),shopOrderFullVo.getRouterVersion()).getBo());
        }else {
            // 没有选择工艺路线，将物料关联的工艺路线的最新版本数据保存到工单表
            if(StringUtils.isNotBlank(shopOrderFullVo.getItem())){
                Item item=itemMapper.selectOne(new QueryWrapper<Item>().eq("item",shopOrderFullVo.getItem()).eq("VERSION",shopOrderFullVo.getItemVersion()));
                // 通过物料中的工艺路线编码去找最近版本的工艺路线
                if(item !=null){
                  Router router=routerMapper.selectById(item.getRouterBo());
                  if(router !=null){
                      shopOrder.setRouterBo(router.getBo());
                  }
                }
            }
        }
        if (shopOrderFullVo.getPlanStartDate() != null) {
            shopOrder.setPlanStartDate(shopOrderFullVo.getPlanStartDate());
        }
        if (shopOrderFullVo.getPlanEndDate() != null) {
            shopOrder.setPlanEndDate(shopOrderFullVo.getPlanEndDate());
        }
        //开始时间不能大于完成时间
        if (shopOrderFullVo.getPlanStartDate() != null && shopOrderFullVo.getPlanEndDate() != null) {

            if (shopOrderFullVo.getPlanStartDate().getTime() > shopOrderFullVo.getPlanEndDate().getTime()) {
                throw new CommonException("计划开始时间不能大于计划完成时间",CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        //验证工艺路线是否存在
        /*if (!StrUtil.isBlank(shopOrderFullVo.getRouter()) && !StrUtil.isBlank(shopOrderFullVo.getRouterVersion())) {

            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getRouter(), shopOrderFullVo.getRouterVersion());
            routerService.getExistRouterByHandleBO(routerHandleBO);
            shopOrder.setRouterBo(routerHandleBO.getBo());

        }*/
        //验证产线
        if (!StrUtil.isBlank(shopOrderFullVo.getProductLine())) {

            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getProductLine());
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrder.setProductLineBo(productLineHandleBO.getBo());
        }
        //验证客户订单是否存在
        /*if (!StrUtil.isBlank(shopOrderFullVo.getCustomerOrder())) {

            CustomerOrderHandleBO customerOrderHandleBO = new CustomerOrderHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getCustomerOrder());
            customerOrderService.getExistCustomerOrder(customerOrderHandleBO);
            shopOrder.setCustomerOrderBo(customerOrderHandleBO.getBo());

        }*/
        shopOrder.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(shopOrder); //验证工单数据是否合规
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //保存工单数据
        super.save(shopOrder);

    }

    /**
     * 更新工单数据
     *
     * @param shopOrder       已存在的工单数据
     * @param shopOrderFullVo 接受的数据
     */
    private void updateShopOrder(ShopOrder shopOrder, ShopOrderFullVo shopOrderFullVo) throws CommonException {
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(shopOrder.getBo());
        ShopOrder shopOrderEntity = new ShopOrder();
        shopOrderEntity.setBo(shopOrderHandleBO.getBo());
        shopOrderEntity.setOrderDesc(StrUtil.isBlank(shopOrderFullVo.getOrderDesc()) ? shopOrderFullVo.getShopOrder() : shopOrderFullVo.getOrderDesc());
        if ("502".equals(shopOrderFullVo.getState())){//校验是否有生产中的工单
            QueryWrapper<Sfc> sfcQw = new QueryWrapper<>();
            sfcQw.eq("shop_order_bo",shopOrderHandleBO.getBo());
            sfcQw.eq("state","生产中");
            List<Sfc> sfcList = sfcService.list(sfcQw);
            if (CollectionUtil.isNotEmpty(sfcList)){
                throw new CommonException("有SFC在生产中，不能关闭",CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        shopOrderEntity.setStateBo(new StatusHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getState()).getBo());
        shopOrderEntity.setIsOverfulfill(shopOrderFullVo.getIsOverfulfill());
        //校验工单修改数量是否在合理范围内（校验当前修改数量不能小于已经下达的数量）
        if (shopOrderFullVo.getOrderQty().compareTo(shopOrder.getReleaseQty()) == -1){
            throw new CommonException("修改工单数量不能小于已下达的数量",CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        shopOrderEntity.setOrderQty(shopOrderFullVo.getOrderQty());
        shopOrderEntity.setOverfulfillQty(shopOrderFullVo.getOverfulfillQty());
        shopOrderEntity.setRouterBo(new RouterHandleBO(UserUtils.getSite(), shopOrderFullVo.getRouter(),shopOrderFullVo.getRouterVersion()).getBo());
        shopOrderEntity.setNegotiationTime(shopOrderFullVo.getNegotiationTime());
        shopOrderEntity.setFixedTime(shopOrderFullVo.getFixedTime());
        shopOrderEntity.setOrderDeliveryTime(shopOrderFullVo.getOrderDeliveryTime());
        shopOrderEntity.setWorkShop(shopOrderFullVo.getWorkShop());
        shopOrderEntity.setModifyUser(userUtil.getUser().getUserName());
        shopOrderEntity.setModifyDate(new Date());

        saveEndTime(shopOrderFullVo,shopOrderEntity);

        if (StringUtils.isNotBlank(shopOrderFullVo.getRouter()) && StringUtils.isNotBlank(shopOrderFullVo.getRouterVersion())){
            shopOrderEntity.setRouterBo(new RouterHandleBO(UserUtils.getSite(), shopOrderFullVo.getRouter(),shopOrderFullVo.getRouterVersion()).getBo());
        }
        if (shopOrderFullVo.getPlanStartDate() != null) {
            shopOrderEntity.setPlanStartDate(shopOrderFullVo.getPlanStartDate());
        } else {
            shopOrderEntity.setPlanStartDate(null);
        }
        if (shopOrderFullVo.getPlanEndDate() != null) {
            shopOrderEntity.setPlanEndDate(shopOrderFullVo.getPlanEndDate());
        } else {
            shopOrderEntity.setPlanEndDate(null);
        }
        //开始时间不能大于完成时间
        if (shopOrderEntity.getPlanStartDate() != null && shopOrderEntity.getPlanEndDate() != null) {

            if (shopOrderEntity.getPlanStartDate().getTime() > shopOrderEntity.getPlanEndDate().getTime()) {
                throw new CommonException("计划开始时间不能大于计划完成时间",CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getItem(), shopOrderFullVo.getItemVersion());
        itemService.getExitsItemByItemHandleBO(itemHandleBO); //获取存在的物料
        shopOrderEntity.setItemBo(itemHandleBO.getBo());

        /*if (!StrUtil.isBlank(shopOrderFullVo.getCustomerOrder())) {
            //验证客户订单是否存在
            CustomerOrderHandleBO customerOrderHandleBO = new CustomerOrderHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getCustomerOrder());
            customerOrderService.getExistCustomerOrder(customerOrderHandleBO);
            shopOrderEntity.setCustomerOrderBo(customerOrderHandleBO.getBo());
        } else {
            shopOrderEntity.setCustomerOrderBo("");
        }

        if (!StrUtil.isBlank(shopOrderFullVo.getRouter()) && !StrUtil.isBlank(shopOrderFullVo.getRouterVersion())) {
            //验证工艺路线是否存在
            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getRouter(), shopOrderFullVo.getRouterVersion());
            routerService.getExistRouterByHandleBO(routerHandleBO);
            shopOrderEntity.setRouterBo(routerHandleBO.getBo());
        } else {
            shopOrderEntity.setRouterBo("");
        }*/

        if (!StrUtil.isBlank(shopOrderFullVo.getBom()) && !StrUtil.isBlank(shopOrderFullVo.getBomVersion())) {
            //验证物料清单是否存在
            Bom bom = bomService.selectByBom(shopOrderFullVo.getBom(), shopOrderFullVo.getBomVersion());
            shopOrderEntity.setBomBo(bom.getBo());
        } else {
            shopOrderEntity.setBomBo("");
        }

        if (!StrUtil.isBlank(shopOrderFullVo.getProductLine())) {
            //验证产线是否存在
            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getProductLine());
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrderEntity.setProductLineBo(productLineHandleBO.getBo());
        } else {
            shopOrderEntity.setProductLineBo("");
        }

        //验证工单指定属性是否合规
        ValidationUtil.ValidResult validResult =
                ValidationUtil.validateProperties(shopOrderEntity, "orderDesc", "stateBo", "isOverfulfill", "orderQty");
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        Integer integer = shopOrderMapper.updateShopOrder(shopOrderEntity, shopOrder.getModifyDate());//更新工单数据
        if (integer == 0) {
            throw new CommonException("数据已修改，请查询后再执行操作", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

    }


    /**
     * 验证工单是否被使用
     *
     * @param shopOrder 工单实类数据
     * @return boolean
     * @throws CommonException 异常
     */
    private boolean validateShopOrderIsUsed(ShopOrder shopOrder) throws CommonException {
        if (shopOrder == null) {
            return false;
        } else {
            //工单已排产
            if (shopOrder.getSchedulQty() != null && shopOrder.getSchedulQty().doubleValue() > 0) return true;
            //工单已下达
            if (shopOrder.getReleaseQty() != null && shopOrder.getReleaseQty().doubleValue() > 0) return true;
            //其它验证
            //其它验证
            //其它验证
            //其它验证
            return false;
        }

    }


    /**
     * 删除工单数据
     *
     * @param shopOrderHandleBO 工单BOHandle
     * @param modifyDate        修改时间
     * @throws CommonException 异常
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteShopOrderByHandleBO(ShopOrderHandleBO shopOrderHandleBO, Date modifyDate) throws CommonException {

        ShopOrder shopOrder = getExistShopOrder(shopOrderHandleBO);
//        CommonUtil.compareDateSame(modifyDate, shopOrder.getModifyDate());
        boolean isUsed = validateShopOrderIsUsed(shopOrder);
        if (isUsed) {
            throw new CommonException("工单"+shopOrder+"已生产，不能删除",CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        QueryWrapper<ShopOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(ShopOrder.BO, shopOrder.getBo());

        //删除工单数据
        Integer integer = shopOrderMapper.delete(wrapper);
        if (integer == 0) {
            throw new CommonException("数据已修改，请查询后再执行删除操作", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //删除工单自定义数据
        customDataValService.deleteCustomDataValByBoAndType(shopOrderHandleBO.getSite(), shopOrderHandleBO.getBo(), CustomDataTypeEnum.SHOP_ORDER);


    }


    @Override
    public List<ShopOrder> getShopOrderByBomBO(String bomBO) {
        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(ShopOrder.SITE, UserUtils.getSite());
        entityWrapper.eq(ShopOrder.BOM_BO, bomBO);
        List<ShopOrder> shopOrders = shopOrderMapper.selectList(entityWrapper);
        return shopOrders;
    }

    /*@Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public List<Sfc> shopOrderRelease(String shopOrder, BigDecimal stayNumber, String planStartDate, String planEndDate) throws BusinessException {
        String site = UserUtils.getSite();
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(site, shopOrder);
        ShopOrder existShopOrder = getExistShopOrder(shopOrderHandleBO);
        StatusHandleBO statusHandleBO = new StatusHandleBO( existShopOrder.getStateBo() );
        verifyShopOrderState(statusHandleBO,shopOrder);
        String itemBo = existShopOrder.getItemBo();
        CodeRuleHandleBO codeRuleHandleBO = new CodeRuleHandleBO(site, "SN", itemBo);
        ItemHandleBO itemHandleBO = new ItemHandleBO(itemBo);

        Router router;
        Item itemEntity = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(itemBo));
        if (!StrUtil.isBlank(existShopOrder.getRouterBo())) {
            router = routerService.selectById(existShopOrder.getRouterBo());
        } else if (!StrUtil.isBlank(itemEntity.getRouterBo())) {
            router = routerService.selectById(itemEntity.getRouterBo());
        } else {
            throw new BusinessException("物料与工单都没有维护工艺路线，请先维护相关工艺路线");
        }
        if (router == null) {
            throw new BusinessException("物料与工单都没有维护工艺路线，请先维护相关工艺路线");
        }


        //加上已下达数量
        if (existShopOrder.getReleaseQty() != null) {
            stayNumber = existShopOrder.getReleaseQty().add(stayNumber);
        }

        //校验数量是否合格
        if ("Y".equals(existShopOrder.getIsOverfulfill())) {
            //允许超产
            if ( existShopOrder.getOverfulfillQty()!=null && existShopOrder.getOverfulfillQty().compareTo( BigDecimal.ZERO )==1 ) {
                //订单数量 + 超产数量
                int i = stayNumber.compareTo(existShopOrder.getOrderQty().add(existShopOrder.getOverfulfillQty()));
                if (i == 1) {
                    throw new BusinessException("工单"+existShopOrder.getShopOrder()+"待下达数量不能超过订单数量与超产数量之和!");
                }
                Integer integer = shopOrderMapper.updateLimitQtyShopOrderReleaseQtyByBO(existShopOrder.getBo(), stayNumber, existShopOrder.getOverfulfillQty());
                if (integer < 1){
                    throw new BusinessException("工单"+existShopOrder.getShopOrder()+"待下达数量不能超过订单数量与超产数量之和!");
                }
            } else {
                shopOrderMapper.updateOverfullQtyShopOrderReleaseQtyByBO( existShopOrder.getBo(), stayNumber );
            }
        } else {
            int i = stayNumber.compareTo(existShopOrder.getOrderQty());
            if (i == 1) {
                throw new BusinessException("工单"+existShopOrder.getShopOrder()+"待下达数量不能超过订单数量!");
            }
            Integer integer = shopOrderMapper.updateShopOrderReleaseQtyByBO(existShopOrder.getBo(), stayNumber);
            if (integer < 1){
                throw new BusinessException("工单"+existShopOrder.getShopOrder()+"待下达数量不能超过订单数量!");
            }
        }

        CodeRule codeRule;
        //根据itembo查询 物料组BO
        Wrapper<ItemGroupMember> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(ItemGroupMember.ITEM_BO, itemBo);
        List<ItemGroupMember> itemGroupMembers = itemGroupMemberService.selectList(entityWrapper);
        List<ItemGroup> itemGroupList = new ArrayList<>();
        ItemGroup itemGroup = new ItemGroup();
        if (!itemGroupMembers.isEmpty()) {
            for (ItemGroupMember itemGroupMember : itemGroupMembers) {
                //查询物料组 根据最早创建时间 选择 BO
                ItemGroup itemGroupEntity = itemGroupService.getItemGroupByItemGroupBO(new ItemGroupHandleBO(itemGroupMember.getItemGroupBo()));
                itemGroupList.add(itemGroupEntity);
            }
            itemGroupList.sort(new Comparator<ItemGroup>() {
                @Override
                public int compare(ItemGroup o1, ItemGroup o2) {

                    return (int) (o1.getCreateDate().getTime() - o2.getCreateDate().getTime());
                }
            });
            itemGroup = itemGroupList.get(0);
        }

        if (codeRuleService.selectById(codeRuleHandleBO.getBo()) != null) {
            codeRule = codeRuleService.selectById(codeRuleHandleBO.getBo());
        } else if (codeRuleService.selectById(new CodeRuleHandleBO(site, "SN", new ItemHandleBO(site, itemHandleBO.getItem(), "*").getBo())) != null) {
            codeRule = codeRuleService.selectById(new CodeRuleHandleBO(site, "SN", new ItemHandleBO(site, itemHandleBO.getItem(), "*").getBo()));
        } else if (codeRuleService.selectById(new CodeRuleHandleBO(site, "SN", itemGroup.getBo())) != null) {
            codeRule = codeRuleService.selectById(new CodeRuleHandleBO(site, "SN", itemGroup.getBo()));
        } else if (codeRuleService.selectById(new CodeRuleHandleBO(site, "SN", new ItemHandleBO(site, "*", "*").getBo())) != null) {
            codeRule = codeRuleService.selectById(new CodeRuleHandleBO(site, "SN", new ItemHandleBO(site, "*", "*").getBo()));
        } else {
            throw new BusinessException("没有为此物料维护编码规则，请先维护编码规则");
        }


        //获取物料批次 向上取整
        BigDecimal lotSize = itemEntity.getLotSize();
        BigDecimal divide = stayNumber.divide(lotSize, 6, RoundingMode.HALF_UP);
        BigDecimal number = divide.setScale(0, BigDecimal.ROUND_UP);//向上取整
        BigDecimal remainder = stayNumber.divideAndRemainder(lotSize)[1];//取余

        List<String> sfcList = codeRuleService.generatorNextNumberList(codeRule.getBo(), number.intValue());

        //创建表的时候用的decimal类型
        BigDecimal bigDecimal_1 = new BigDecimal(1);
        BigDecimal bigDecimal_0 = new BigDecimal(0);

        List<Sfc> sfcs = new ArrayList<>();
        List<SfcRouter> sfcRouterList = new ArrayList<>();
        List<SfcStep> sfcStepList = new ArrayList<>();
        List<SfcActive> sfcActiveList = new ArrayList<>();
        int i = checkForDuplicates(sfcList);
        if (i != -1) {
            throw new BusinessException("SFC编号:" + sfcList.get(i) + "不能重复");
        }
        for (String sfcSerial : sfcList) {
            //校验sfc是否已存在  存在则报错
            sfcService.verifyExist(sfcSerial);
            Sfc sfc = new Sfc();
            SfcHandleBO sfcHandleBO = new SfcHandleBO(site, sfcSerial);
            sfc.setBo(sfcHandleBO.getBo());
            sfc.setSfc(sfcSerial);
            sfc.setSite(site);
            sfc.setItemBo(existShopOrder.getItemBo());
            if (sfcList.get(sfcList.size() - 1).equals(sfcSerial)) {
                if (remainder.equals(BigDecimal.ZERO)) {
                    sfc.setQty(lotSize);//物料批量数
                } else {
                    sfc.setQty(remainder);//余数
                }
            } else {
                sfc.setQty(lotSize);//物料批量数
            }
            sfc.setShopOrderBo(shopOrderHandleBO.getBo());
            if (!StrUtil.isBlank(existShopOrder.getProductLineBo())) {
                sfc.setProductLineBo(existShopOrder.getProductLineBo());
            }
            sfc.setState("401");
            if (!StrUtil.isBlank(existShopOrder.getBomBo())) {
                sfc.setBomBo(existShopOrder.getBomBo());
            } else {
                sfc.setBomBo(itemEntity.getBomBo());
            }
            sfc.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
            sfcs.add(sfc);

            SfcRouter sfcRouter = new SfcRouter();
            SfcRouterHandleBO sfcRouterHandleBO = new SfcRouterHandleBO(site, router.getRouter(), router.getVersion(), sfcSerial, "10");
            sfcRouter.setBo(sfcRouterHandleBO.getBo());
            sfcRouter.setSite(site);
            sfcRouter.setSfcBo(sfc.getBo());
            sfcRouter.setRouterBo(router.getBo());
            sfcRouter.setSerial("10");
            sfcRouter.setInUse("Y");
            sfcRouter.setLastOperationStep(router.getEntryRouterStepBo());
            sfcRouter.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
            sfcRouterList.add(sfcRouter);

            //根据routerBO查找M_ROUTER_STEP,router_step_operation数据 获取 stepId ,OPERATION_BO
            List<SfcGetRouterParamVo> routerParamVos = sfcMapper.getRouterParamVos(router.getBo());
            if (routerParamVos.size() == 0) {
                throw new BusinessException("工艺路线" + router.getRouter() + "未维护相关工艺步骤");
            }

            for (SfcGetRouterParamVo routerParamVo : routerParamVos) {
                SfcStep sfcStep = new SfcStep();
                SfcStepHandleBO sfcStepHandleBO = new SfcStepHandleBO(site, router.getRouter(), router.getVersion(), sfcSerial, routerParamVo.getStepId(), "10");
                sfcStep.setBo(sfcStepHandleBO.getBo());
                sfcStep.setOperationBo(routerParamVo.getOperationBo());
                sfcStep.setSite(site);
                sfcStep.setSfcRouterBo(sfcRouter.getBo());
                sfcStep.setStepId(routerParamVo.getStepId());
                sfcStep.setIsDone("0");
                //获取第一步步骤的工序
                Wrapper<RouterStepOperation> entityWrapper1 = new EntityWrapper<>();
                entityWrapper1.eq(RouterStepOperation.ROUTER_STEP_BO, router.getEntryRouterStepBo());
                List<RouterStepOperation> routerStepOperations = routerStepOperationService.selectList(entityWrapper1);
                for (RouterStepOperation routerStepOperation : routerStepOperations) {
                    if (routerStepOperation.getOperationBo().equals(routerParamVo.getOperationBo())) {
                        sfcStep.setQtyInQueue(sfc.getQty()); //sfc 数量
                    } else {
                        sfcStep.setQtyInQueue(bigDecimal_0);
                    }
                }
                sfcStep.setQueueNum(bigDecimal_1);
                sfcStep.setLoopNum(bigDecimal_1);
                sfcStep.setQtyInWork(bigDecimal_0);
                sfcStep.setQtyCompleted(bigDecimal_0);
                sfcStep.setActiveNum(bigDecimal_0);
                sfcStep.setThroughNum(bigDecimal_0);
                sfcStepList.add(sfcStep);

                SfcActive sfcActive = new SfcActive();
                sfcActive.setPid(idWorker.nextId() + "");
                sfcActive.setSite(site);
                sfcActive.setSfc(sfcSerial);
                sfcActive.setActiveNo("SFCREL");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                sfcActive.setActiveDate(df.format(new Date()));
                sfcActive.setItemBo(existShopOrder.getItemBo());
                sfcActive.setBomBo(existShopOrder.getBomBo());
                sfcActive.setShopOrderBo(shopOrderHandleBO.getBo());
                if (!StrUtil.isBlank(existShopOrder.getProductLineBo())) {
                    sfcActive.setProductLineBo(existShopOrder.getProductLineBo());
                } else {
                    sfcActive.setProductLineBo(existShopOrder.getProductLineBo());
                }
                sfcActive.setUserNo(userUtil.getUser().getUserName());
                sfcActive.setSfcQty(bigDecimal_1);
                sfcActive.setSfcRouterBo(itemEntity.getRouterBo());
                sfcActive.setSfcStepId(routerParamVo.getStepId());
                sfcActive.setOperationBo(routerParamVo.getOperationBo());
                sfcActive.setScrapQty(bigDecimal_0);
                sfcActive.setIsRework("0");
                sfcActiveList.add(sfcActive);
            }

        }
        sfcMapper.insertBatch(sfcs);
        sfcRouterService.insertBatch(sfcRouterList);
        sfcStepService.insertBatch(sfcStepList);
        sfcActiveService.insertBatch(sfcActiveList);
        return sfcs;
    }


    //根据Sfc和数量工单下达
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public List<Sfc> shopOrderReleaseBySfcAndNumVos(String shopOrder, String planStartDate, String planEndDate, List<SfcAndNumVo> sfcAndNumVos) throws BusinessException {
        return shopOrderReleaseBySfcAndNumVos( shopOrder,planStartDate,planEndDate,null,sfcAndNumVos );
    }


    //根据Sfc和数量工单下达
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public List<Sfc> shopOrderReleaseBySfcAndNumVos(String shopOrder, String planStartDate, String planEndDate,String routerBo,  List<SfcAndNumVo> sfcAndNumVos) throws BusinessException {
        String site = UserUtils.getSite();
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(site, shopOrder);
        ShopOrder existShopOrder = getExistShopOrder(shopOrderHandleBO);
        StatusHandleBO statusHandleBO = new StatusHandleBO( existShopOrder.getStateBo() );
        verifyShopOrderState(statusHandleBO,shopOrder);
        String itemBo = existShopOrder.getItemBo();

        //判断sfcAndNumVos 是否有值
        if (sfcAndNumVos.size() == 0) {
            throw new BusinessException("sfc编号不能为空!");
        }

        //获取sfcAndNumVos中 数量之和;
        BigDecimal total = BigDecimal.ZERO;
        //用来判断是否有重复的sfc数据
        List<String> list = new ArrayList<>();
        for (SfcAndNumVo sfcAndNumVo : sfcAndNumVos) {
            //校验sfc是否已存在  存在则报错
            sfcService.verifyExist(sfcAndNumVo.getSfc());
            if (sfcAndNumVo.getNumber().compareTo(BigDecimal.ZERO) == 1){
                total = sfcAndNumVo.getNumber().add(total);
                list.add(sfcAndNumVo.getSfc());
            }else {
                throw new BusinessException("SFC编号:" + sfcAndNumVo.getSfc() + "数量要大于零!");
            }

        }

        //加上已下达数量
        if (existShopOrder.getReleaseQty() != null) {
            total = existShopOrder.getReleaseQty().add(total);
        }

        int index = checkForDuplicates(list);
        if (index != -1) {
            throw new BusinessException("SFC编号:" + list.get(index) + "不能重复");
        }

        //
        Router router;
        Item itemEntity = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(itemBo));
        if (StrUtil.isBlank(routerBo)) {
            if (!StrUtil.isBlank(existShopOrder.getRouterBo())) {
                router = routerService.getExistRouterByHandleBO(new RouterHandleBO(existShopOrder.getRouterBo()));
            } else if (!StrUtil.isBlank(itemEntity.getRouterBo())) {
                router = routerService.getExistRouterByHandleBO(new RouterHandleBO(itemEntity.getRouterBo()));
            } else {
                throw new BusinessException("物料与工单都没有维护工艺路线，请先维护相关工艺路线");
            }
        }else {
            router = routerService.getExistRouterByHandleBO(new RouterHandleBO(routerBo));
        }


        //校验数量是否合格
        if ("Y".equals(existShopOrder.getIsOverfulfill())) {
            //允许超产
            if ( existShopOrder.getOverfulfillQty()!=null && existShopOrder.getOverfulfillQty().compareTo( BigDecimal.ZERO )==1 ) {
                //订单数量 + 超产数量
                int i = total.compareTo(existShopOrder.getOrderQty().add(existShopOrder.getOverfulfillQty()));
                if (i == 1) {
                    throw new BusinessException("工单"+existShopOrder.getShopOrder()+"待下达数量不能超过订单数量与超产数量之和!");
                }
                Integer integer = shopOrderMapper.updateLimitQtyShopOrderReleaseQtyByBO( existShopOrder.getBo(),
                        new BigDecimal(sfcAndNumVos.size() ), existShopOrder.getOverfulfillQty() );
                if( integer.intValue()<1 ){
                    throw new BusinessException("工单"+existShopOrder.getShopOrder()+"待下达数量不能超过订单数量与超产数量之和!");
                }
            }else{
                shopOrderMapper.updateOverfullQtyShopOrderReleaseQtyByBO( existShopOrder.getBo(), new BigDecimal(sfcAndNumVos.size() ) );
            }
        } else {
            int i = total.compareTo(existShopOrder.getOrderQty());
            if (i == 1) {
                throw new BusinessException("工单"+existShopOrder.getShopOrder()+"待下达数量不能超过订单数量!");
            }
            Integer integer = shopOrderMapper.updateShopOrderReleaseQtyByBO( existShopOrder.getBo(), new BigDecimal(sfcAndNumVos.size() ) );
            if( integer.intValue()<1 ){
                throw new BusinessException("工单"+existShopOrder.getShopOrder()+"待下达数量不能超过订单数量!");
            }
        }


        //创建表的时候用的decimal类型
        BigDecimal bigDecimal_1 = new BigDecimal(1);
        BigDecimal bigDecimal_0 = new BigDecimal(0);

        List<Sfc> sfcs = new ArrayList<>();
        List<SfcRouter> sfcRouterList = new ArrayList<>();
        List<SfcStep> sfcStepList = new ArrayList<>();
        List<SfcActive> sfcActiveList = new ArrayList<>();

        for (SfcAndNumVo sfcAndNumVo : sfcAndNumVos) {
            String sfcCode = sfcAndNumVo.getSfc(); //sfc编号
            BigDecimal number = sfcAndNumVo.getNumber();//数量
            Sfc sfc = new Sfc();
            SfcHandleBO sfcHandleBO = new SfcHandleBO(site, sfcCode);
            sfc.setBo(sfcHandleBO.getBo());
            sfc.setSfc(sfcCode);
            sfc.setSite(site);
            sfc.setItemBo(existShopOrder.getItemBo());
            sfc.setQty(number);
            sfc.setShopOrderBo(existShopOrder.getBo());
            if (!StrUtil.isBlank(existShopOrder.getProductLineBo())) {
                sfc.setProductLineBo(existShopOrder.getProductLineBo());
            }
            sfc.setState("401");
            if (!StrUtil.isBlank(existShopOrder.getBomBo())) {
                sfc.setBomBo(existShopOrder.getBomBo());
            } else {
                sfc.setBomBo(itemEntity.getBomBo());
            }
            sfc.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
            sfcs.add(sfc);

            SfcRouter sfcRouter = new SfcRouter();
            SfcRouterHandleBO sfcRouterHandleBO = new SfcRouterHandleBO(site, router.getRouter(), router.getVersion(), sfcCode, "10");
            sfcRouter.setBo(sfcRouterHandleBO.getBo());
            sfcRouter.setSite(site);
            sfcRouter.setSfcBo(sfc.getBo());
            sfcRouter.setRouterBo(router.getBo());
            sfcRouter.setSerial("10");
            sfcRouter.setInUse("Y");
            sfcRouter.setLastOperationStep(router.getEntryRouterStepBo());
            sfcRouter.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
            sfcRouterList.add(sfcRouter);

            //根据routerBO查找M_ROUTER_STEP,router_step_operation数据 获取 stepId ,OPERATION_BO
            List<SfcGetRouterParamVo> routerParamVos = sfcMapper.getRouterParamVos(router.getBo());
            if (routerParamVos.size() == 0) {
                throw new BusinessException("工艺路线" + router.getRouter() + "未维护相关工艺步骤");
            }

            for (SfcGetRouterParamVo routerParamVo : routerParamVos) {
                SfcStep sfcStep = new SfcStep();
                SfcStepHandleBO sfcStepHandleBO = new SfcStepHandleBO(site, router.getRouter(), router.getVersion(), sfcCode, routerParamVo.getStepId(), "10");
                sfcStep.setBo(sfcStepHandleBO.getBo());
                sfcStep.setOperationBo(routerParamVo.getOperationBo());
                sfcStep.setSite(site);
                sfcStep.setSfcRouterBo(sfcRouter.getBo());
                sfcStep.setStepId(routerParamVo.getStepId());
                sfcStep.setIsDone("0");
                //获取第一步步骤的工序
                Wrapper<RouterStepOperation> entityWrapper1 = new EntityWrapper<>();
                entityWrapper1.eq(RouterStepOperation.ROUTER_STEP_BO, router.getEntryRouterStepBo());
                List<RouterStepOperation> routerStepOperations = routerStepOperationService.selectList(entityWrapper1);
                for (RouterStepOperation routerStepOperation : routerStepOperations) {
                    if (routerStepOperation.getOperationBo().equals(routerParamVo.getOperationBo())) {
                        sfcStep.setQtyInQueue(sfc.getQty()); //sfc 数量
                    } else {
                        sfcStep.setQtyInQueue(bigDecimal_0);
                    }
                }
                sfcStep.setQueueNum(bigDecimal_1);
                sfcStep.setLoopNum(bigDecimal_1);
                sfcStep.setQtyInWork(bigDecimal_0);
                sfcStep.setQtyCompleted(bigDecimal_0);
                sfcStep.setActiveNum(bigDecimal_0);
                sfcStep.setThroughNum(bigDecimal_0);
                sfcStepList.add(sfcStep);

                SfcActive sfcActive = new SfcActive();
                sfcActive.setPid(idWorker.nextId() + "");
                sfcActive.setSite(site);
                sfcActive.setSfc(sfcCode);
                sfcActive.setActiveNo("SFCREL");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                sfcActive.setActiveDate(df.format(new Date()));
                sfcActive.setItemBo(existShopOrder.getItemBo());
                sfcActive.setBomBo(existShopOrder.getBomBo());
                sfcActive.setShopOrderBo(shopOrderHandleBO.getBo());
                if (!StrUtil.isBlank(existShopOrder.getProductLineBo())) {
                    sfcActive.setProductLineBo(existShopOrder.getProductLineBo());
                } else {
                    sfcActive.setProductLineBo(existShopOrder.getProductLineBo());
                }
                sfcActive.setUserNo(userUtil.getUser().getUserName());
                sfcActive.setSfcQty(bigDecimal_1);
                sfcActive.setSfcRouterBo(itemEntity.getRouterBo());
                sfcActive.setSfcStepId(routerParamVo.getStepId());
                sfcActive.setOperationBo(routerParamVo.getOperationBo());
                sfcActive.setScrapQty(bigDecimal_0);
                sfcActive.setIsRework("0");
                sfcActiveList.add(sfcActive);
            }

        }
        sfcMapper.insertBatch(sfcs);
        sfcRouterService.insertBatch(sfcRouterList);
        sfcStepService.insertBatch(sfcStepList);
        sfcActiveService.insertBatch(sfcActiveList);
        return sfcs;
    }*/

    /**
     * 更新工单完成数量
     *
     * @param bo 工单BO
     * @param completeQty 完成数量
     * @return Integer
     */
    @Override
    public Integer updateShopOrderCompleteQtyByBO(String bo, BigDecimal completeQty){

        return shopOrderMapper.updateShopOrderCompleteQtyByBO( bo, completeQty );
    }

    /**
     * 查询所有未下达的工单
     * @param
     * @return
     */
    @Override
    public IPage<ShopOrder> queryShopOrderRelease(ShopOrderDTO shopOrderDTO) {
        List<String> itemDescKeyWordList= KeyWordUtil.encapsulationItemDescKeyWord(shopOrderDTO.getKeyWord());
        shopOrderDTO.setItemDescKeyWordList(itemDescKeyWordList);
        if (ObjectUtil.isEmpty(shopOrderDTO.getPage())){
            shopOrderDTO.setPage(new Page(1,10));
        }
        shopOrderDTO.setSite(UserUtils.getSite());
        if (StringUtils.isNotBlank(shopOrderDTO.getPlanStartDate())){
            shopOrderDTO.setPlanStartDate(shopOrderDTO.getPlanStartDate().substring(0,10));
        }
        if (StringUtils.isNotBlank(shopOrderDTO.getPlanEndDate())){
            shopOrderDTO.setPlanEndDate(shopOrderDTO.getPlanEndDate().substring(0,10));
        }
        IPage<ShopOrder> shopOrderIPage;
        if ("5422".equals(shopOrderDTO.getWorkShop()) || "5424".equals(shopOrderDTO.getWorkShop()) || "544".equals(shopOrderDTO.getWorkShop())){//如果是转轴车间和管壳车间、线圈车间
            shopOrderIPage = shopOrderMapper.selectShopOrderReleaseByZz(shopOrderDTO.getPage(), shopOrderDTO);
        }else {
            shopOrderIPage = shopOrderMapper.selectShopOrderRelease(shopOrderDTO.getPage(), shopOrderDTO);
        }
        for (ShopOrder record : shopOrderIPage.getRecords()) {
            if (StringUtils.isBlank(record.getRouter())){
                //查找物料上的工艺编码对应的最新版本
                Item itemObj = itemService.getById(record.getItemBo());
                if (StringUtils.isNotBlank(itemObj.getRouterName())){//routerName为工艺编码
                    QueryWrapper<Router> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("router",itemObj.getRouterName());
                    queryWrapper.eq("is_current_version",1);//当前版本
                    Router router = routerMapper.selectOne(queryWrapper);
                    if (ObjectUtil.isNotEmpty(router)){
                        record.setRouter(router.getRouter());
                        record.setRouterName(router.getRouterName());
                        record.setVersion(router.getVersion());
                    }
                }
            }
        }
        return shopOrderIPage;
    }

    /**
     * 更新工单报废数量
     *
     * @param bo 工单BO
     * @param scrapTty 报废数量
     * @return Integer
     */
    @Override
    public Integer updateShopOrderScrapQtyByBO(String bo, BigDecimal scrapTty){
        return shopOrderMapper.updateShopOrderCompleteQtyByBO( bo, scrapTty );
    }

    @Override
    @Transactional
    public Object getAllOrder(Map<String,Object> params, Integer pageNum, Integer pageSize){
        Page page = new Page(pageNum,pageSize);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String site = UserUtils.getSite();
        //   String site = "1040";
        params.put("site",site);
        //先判断是否为自定义数据的字段并对自定义数据的查询
        List<String> boList = judgeCustomData(params);
        params.put("stateBo",Arrays.asList("STATE:"+site+",500","STATE:"+site+",501"));
        if(boList != null && !boList.isEmpty()) params.put("boList", boList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(params.get("timeParam") != null && StringUtils.isNotBlank(params.get("timeParam").toString())) {

            try {
                params.put("startTime",sdf.parse(format.format(new Date())+" 00:00:00"));
                params.put("endTime", sdf.parse(afterNDay(Integer.valueOf(params.get("timeParam").toString()))+" 24:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        List<Map<String, Object>> bindings = shopOrderMapper.getBindingBySite(site);
        List<Map<String, Object>> noFirstBinding = bindings.stream().filter(e -> Integer.valueOf(e.get("sort").toString()) != 1).collect(Collectors.toList());
        List<Map<String, Object>> firstBinding = bindings.stream().filter(e -> Integer.valueOf(e.get("sort").toString()) == 1).collect(Collectors.toList());
        if(noFirstBinding != null && !noFirstBinding.isEmpty()){
            List<String> bo = noFirstBinding.stream().map(e -> e.get("bo").toString()).collect(Collectors.toList());
            if(bo != null && !bo.isEmpty()){
                params.put("bindings",bo);
            }
        }

        List<String> orderList = shopOrderMapper.getScheduleShopOrder();
        if(orderList != null && !orderList.isEmpty()){
            if(params.get("bindings")!=null){
                List<String> bo = (List<String>)params.get("bindings");
                bo.addAll(orderList);
            }else{
                params.put("bindings",orderList);
            }

        }


        IPage list = shopOrderMapper.getList(page, params);
        List<ShopOrder> shopOrderList = list.getRecords();

        List<Map> shopOrderFullVos = new ArrayList<>();

        shopOrderList.forEach(shopOrder -> {
            Map result = createShopOrderFullVo(shopOrder, site);
            shopOrderFullVos.add(result);

            if(firstBinding != null && !firstBinding.isEmpty()){
                String  no = "";
                for(Map<String, Object> stringObjectMap : firstBinding){
                    if(shopOrder.getShopOrder().equals(stringObjectMap.get("bo").toString())){
                        no = stringObjectMap.get("no").toString();
                    }
                }
                if(StringUtils.isNotBlank(no)){
                    Map last = shopOrderFullVos.get(shopOrderFullVos.size()-1);
                    String finalNo = no;
                    List<Map<String, Object>> bind = noFirstBinding.stream().filter(e -> finalNo.equals(e.get("no").toString())).collect(Collectors.toList());
                    bind.stream().sorted(Comparator.comparing(e->e.get("sort").toString()));
                    last.put("margin",bind.size());
                    last.put("no",no);
                    bind.forEach(map->{
                        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<>();
                        entityWrapper.eq("SHOP_ORDER",map.get("bo").toString());
                        ShopOrder order = shopOrderMapper.selectOne(entityWrapper);
                        Map shopOrderFullVo = createShopOrderFullVo(order, site);
                        shopOrderFullVo.put("no",finalNo);
                        shopOrderFullVos.add(shopOrderFullVo);
                    });
                }
            }
        });
        list.setRecords(shopOrderFullVos);
        return list;
    }

    @Override
    public void updateShopOrderFullVo(ShopOrderFullVo shopOrderFullVo) {
        ShopOrder shopOrder = new ShopOrder();
        BeanUtils.copyProperties(shopOrderFullVo,shopOrder);
        shopOrderMapper.updateById(shopOrder);
    }

    @Override
    public void updateEmergenc(List<Map<String, Object>> shopOrderList) {
        shopOrderList.forEach(shopOrder -> {
            Map<String, Object> params = new HashMap<>();
            params.put("shopOrder", shopOrder.get("shopOrder").toString());
            params.put("emergencyState", shopOrder.get("emergencyState").toString());
            params.put("emergencyBz", shopOrder.get("emergencyBz").toString());
            shopOrderMapper.updateEmergenc(params);
        });
    }

    @Override
    public void updateFixedTime(String shopOrder, String fixedTime) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("SHOP_ORDER",shopOrder);
        ShopOrder order = shopOrderMapper.selectOne(entityWrapper);
        try {
            order.setFixedTime(sd.parse(fixedTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        shopOrderMapper.updateById(order);
    }


    public Map createShopOrderFullVo(ShopOrder shopOrder, String site){
        ShopOrderFullVo shopOrderFullVo = new ShopOrderFullVo();
        BeanUtils.copyProperties(shopOrder, shopOrderFullVo);

        if(shopOrder.getItemBo() != null){
            ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrder.getItemBo());
            shopOrderFullVo.setItem(itemHandleBO.getItem());
            shopOrderFullVo.setItemVersion(itemHandleBO.getVersion());
            shopOrderFullVo.setState(new StatusHandleBO(shopOrder.getStateBo()).getState());
        }


        //组装BOM
        if (!StrUtil.isBlank(shopOrder.getBomBo())) {

            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getBomBo());
            shopOrderFullVo.setBom(bomHandleBO.getBom());
            shopOrderFullVo.setBomVersion(bomHandleBO.getVersion());
        }
        //组装工艺路线
        if (!StrUtil.isBlank(shopOrder.getRouterBo())) {

            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            shopOrderFullVo.setRouter(routerHandleBO.getRouter());
            shopOrderFullVo.setRouterVersion(routerHandleBO.getVersion());
        }
        //组装产线
        if (!StrUtil.isBlank(shopOrder.getProductLineBo())) {

            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrder.getProductLineBo());
            shopOrderFullVo.setProductLine(productLineHandleBO.getProductLine());
        }

        //获取物料描述
//        Item exitsItem = null;
//        try {
//            exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(shopOrder.getItemBo()));
//        } catch (CommonException e) {
//            throw new RuntimeException("获取物料失败");
//        }
//        shopOrderFullVo.setItemDesc(exitsItem.getItemDesc());

        //ZHUAN MAP
        Map result = JSONObject.parseObject(JSONObject.toJSONString(shopOrderFullVo), Map.class);

        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectOnlyCustomData(site, shopOrder.getBo(), CustomDataTypeEnum.SHOP_ORDER.getDataType());

//        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(site, shopOrder.getBo(), CustomDataTypeEnum.SHOP_ORDER.getDataType());
        customDataAndValVos.forEach(vo ->{
            result.put(vo.getCdField(),vo.getVals());
        });
        String planStartDateStr = result.get("planStartDate")==null?"":result.get("planStartDate").toString();
        if(StringUtils.isNotBlank(planStartDateStr)) result.put("planStartDate",stringToDate(planStartDateStr));
        String planEndDateStr = result.get("planEndDate")==null?"":result.get("planEndDate").toString();
        if(StringUtils.isNotBlank(planEndDateStr)) result.put("planEndDate",stringToDate(planEndDateStr));
        String negotiationTimeStr = result.get("negotiationTime")==null?"":result.get("negotiationTime").toString();
        if(StringUtils.isNotBlank(negotiationTimeStr)) result.put("negotiationTime",stringToDate(negotiationTimeStr));
        String fixedTimeStr = result.get("fixedTime")==null?"":result.get("fixedTime").toString();
        if(StringUtils.isNotBlank(fixedTimeStr)) result.put("fixedTime",stringToDate(fixedTimeStr));
        String orderDeliveryTimeStr = result.get("orderDeliveryTime")==null?"":result.get("orderDeliveryTime").toString();
        if(StringUtils.isNotBlank(orderDeliveryTimeStr)) result.put("orderDeliveryTime",stringToDate(orderDeliveryTimeStr));
        return result;
    }

    public String stringToDate(String lo){
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }
    public  String afterNDay(int n){
        Calendar c=Calendar.getInstance();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        c.setTime(new Date());
        c.add(Calendar.DATE,n);
        Date d2=c.getTime();
        String s=df.format(d2);
        return s;
    }

    private List<String> judgeCustomData(Map<String, Object> params) {
        List<String> boList = null;
        List<String> newList = new ArrayList<>();
        int n = 0;
        Set<String> keys = params.keySet();
        for(String key : keys){
            if(key.startsWith("filter") && params.get(key) != null && StringUtils.isNotBlank(params.get(key).toString())){
                //有查询
                String filter_ = key.replaceAll("filter_", "");
                Map<String, Object> map = new HashMap<>();
                map.put("key",filter_);
                map.put("value",params.get(key).toString());
                map.put("site",params.get("site").toString());
                n++;
                //根据参数进行查询
                boList = shopOrderMapper.getIdsByVals(map);
                boList.forEach(str->{
                    newList.add(str);
                });

            }
        }

        if(n>1){
            List<String> list = new ArrayList<>();
            for(String str : newList){
                long count = newList.stream().filter(e -> str.equals(e)).count();
                if(n == (int)count){
                    list.add(str);
                }
            }
            if(list.isEmpty()){
                list.add("noEmpty");
            }
            return list;
        }else{
            if(boList!= null && boList.isEmpty()){
                boList.add("noEmpty");
            }
            return boList;

        }
    }


    /*@Override
    public void changeShopOrderCompleteQty( String targetShopOrderBo , String sfcBo ) throws BusinessException{

        ShopOrder targetShopOrder = getExistShopOrder( new ShopOrderHandleBO( targetShopOrderBo ) );
        Sfc sfc = sfcService.getExistSfcByHandleBO( new SfcHandleBO( sfcBo ) );
        if( !SfcStateEnum.DONE.getState().equals( sfc.getState() )){
            throw new BusinessException( "条码"+sfc.getSfc()+"不是完成状态，不能修改为工单"+targetShopOrder.getShopOrder()+"的完成数" );
        }
        if( targetShopOrder.getBo().equals( sfc.getShopOrderBo() ) ){
            return;
        }
        ShopOrder sourceShopOrder = null;
        if( !StrUtil.isBlank( sfc.getShopOrderBo() ) ){
            sourceShopOrder = getExistShopOrder( new ShopOrderHandleBO( sfc.getShopOrderBo() ) );
            updateShopOrderCompleteQtyByBO( sourceShopOrder.getBo(), sfc.getQty().subtract( sfc.getQty().add( sfc.getQty() ) ) );
            shopOrderMapper.updateOverfullQtyShopOrderReleaseQtyByBO( sourceShopOrder.getBo(), sfc.getQty().subtract( sfc.getQty().add( sfc.getQty() ) ) );
        }
        updateShopOrderCompleteQtyByBO( targetShopOrderBo,sfc.getQty() ); //更新
        if( "Y".equals( targetShopOrder.getIsOverfulfill() ) ){ //超产
            if( targetShopOrder.getOverfulfillQty()!=null && targetShopOrder.getOverfulfillQty().compareTo( BigDecimal.ZERO )>0 ){
                //有限超产
                Integer updateNum = shopOrderMapper
                        .updateLimitQtyShopOrderReleaseQtyByBO( targetShopOrderBo, sfc.getQty(),targetShopOrder.getOverfulfillQty() );
                if( updateNum<1 ){
                    throw new BusinessException( "工单"+targetShopOrder.getShopOrder()+"下达数量不能超计划数和超产数之和，不能更改" );
                }
            }else{ //无限超产
                shopOrderMapper.updateOverfullQtyShopOrderReleaseQtyByBO( targetShopOrderBo, sfc.getQty() );
            }
        }else{
            //不超产
            Integer updateNum = shopOrderMapper.updateShopOrderReleaseQtyByBO( targetShopOrderBo,sfc.getQty() );
            if( updateNum<1 ){
                throw new BusinessException( "工单"+targetShopOrder.getShopOrder()+"可下达数量不足，不能更改" );
            }
        }

        Sfc sfcEntity = new Sfc();
        sfcEntity.setBo( sfc.getBo() );
        sfcEntity.setItemBo( targetShopOrder.getItemBo() );
        sfcEntity.setShopOrderBo( targetShopOrder.getBo() );
        sfcService.updateById( sfcEntity );

    }

    private void verifyShopOrderState(StatusHandleBO statusHandleBO,String shopOrder) throws BusinessException {
        if ( !"501".equals( statusHandleBO.getState() ) ){
            if( "502".equals( statusHandleBO.getState() ) ){
                throw new BusinessException( "工单"+shopOrder+"已关闭,不可下达" );
            }
            if( "503".equals( statusHandleBO.getState() ) ){
                throw new BusinessException( "工单"+shopOrder+"已完成,不可下达" );
            }
            if( "504".equals( statusHandleBO.getState() ) ){
                throw new BusinessException( "工单"+shopOrder+"已保留,不可下达" );
            }
            throw new BusinessException( "工单"+shopOrder+"不可下达" );
        }
    }*/

    @Override
    public IPage<ShopOrderReportVo> getShopOrderReport(ShopOrderReportDTO shopOrderReportDTO) {
        if (ObjectUtil.isEmpty(shopOrderReportDTO.getPage())){
            shopOrderReportDTO.setPage(new Page(1,10));
        }
        IPage<ShopOrderReportVo> shopOrderReport = shopOrderMapper.getShopOrderReport(shopOrderReportDTO.getPage(), shopOrderReportDTO);
        for (ShopOrderReportVo shopOrderReportVo : shopOrderReport.getRecords()) {
            BigDecimal orderQty = shopOrderReportVo.getOrderQty();//工单总数
            //查询待生产数量（该工单在sfc表中状态为新建的sfc数量之和 + 未生成条码的数量）
            BigDecimal waitQty = BigDecimal.ZERO;//待生产数量
            QueryWrapper<Sfc> soQw = new QueryWrapper<>();
            soQw.eq("shop_order_bo",shopOrderReportVo.getShopOrderBo());
            soQw.eq("state","新建");
            List<Sfc> sfcListO = sfcService.list(soQw);
            for (Sfc sfc : sfcListO) {
                waitQty = waitQty.add(sfc.getSfcQty());
            }
            List<Sfc> sfcListT = sfcService.list(new QueryWrapper<Sfc>().eq("shop_order_bo", shopOrderReportVo.getShopOrderBo()));
            BigDecimal totalQty = BigDecimal.ZERO;//总数（只要该工单生成sfc就把数累加起来）
            for (Sfc sfc : sfcListT) {
                totalQty = totalQty.add(sfc.getSfcQty());
            }
            waitQty = waitQty.add(orderQty.subtract(totalQty));
            shopOrderReportVo.setWaitQty(waitQty);

            List<ShopOrderReportVo.MakingDetailsVo> makingDetailsList = shopOrderMapper.getMakingDetails(shopOrderReportVo.getShopOrderBo());//查询在制详情
            shopOrderReportVo.setMakingDetails(makingDetailsList);
            BigDecimal makingTotalQty = BigDecimal.ZERO;//该工单在制总数
            for (ShopOrderReportVo.MakingDetailsVo makingDetailsVo : makingDetailsList) {
                makingTotalQty = makingTotalQty.add(makingDetailsVo.getMakingQty());
            }
            shopOrderReportVo.setMakingTotalQty(makingTotalQty);
            QueryWrapper<Sfc> soQwT = new QueryWrapper<>();
            soQwT.eq("shop_order_bo",shopOrderReportVo.getShopOrderBo());
            soQwT.eq("state","已完成");
            List<Sfc> sfcListF = sfcService.list(soQwT);

            BigDecimal doneQty = BigDecimal.ZERO;//完成数
            for (Sfc sfc : sfcListF) {
                doneQty = doneQty.add(sfc.getSfcQty());
            }
            shopOrderReportVo.setDoneQty(doneQty);
            BigDecimal goToStockQty = BigDecimal.ZERO;//入库数
            List<Stock> stockList = stockService.list(new QueryWrapper<Stock>().eq("shop_order_bo", shopOrderReportVo.getShopOrderBo()));
            for (Stock stock : stockList) {
                goToStockQty = goToStockQty.add(new BigDecimal(stock.getOkQty()));
            }
            shopOrderReportVo.setGoToStockQty(goToStockQty);
        }
        return shopOrderReport;
    }

    @Override
    public IPage<OperationOrder> getCanCancelOperationOrder(ShopOrderDTO shopOrderDTO) {
        IPage<OperationOrder> operationOrderList = operationOrderMapper.selectPage(shopOrderDTO.getPage(), new QueryWrapper<OperationOrder>().eq("shop_order", shopOrderDTO.getShopOrder()));
        for (int i = 0; i < operationOrderList.getRecords().size(); i++) {
            boolean flag = isCanCancelOperationOrder(operationOrderList.getRecords().get(i).getOperationOrder());
            if (!flag){
                operationOrderList.getRecords().remove(i);
                i--;
                continue;
            }
        }
        return operationOrderList;
    }

    /**
     * 判断该工序工单是否可以撤回
     * @return
     */
    private boolean isCanCancelOperationOrder(String operationOrder){
        List<Enter> enterList = enterMapper.selectList(new QueryWrapper<Enter>().eq("operation_order", operationOrder));
        if (CollectionUtil.isNotEmpty(enterList)){
            return false;//有进站记录，不能撤回
        }
        return true;//没有进站记录，可以撤回
    }

    @Override
    public void okCancelOperationOrder(List<String> operationOrderList) throws CommonException{
        boolean isException = false;//是否有不能撤回的工序工单
        String exceptionOpOrder = null;//异常工序工单
        for (String operationOrder : operationOrderList) {
            boolean flag = isCanCancelOperationOrder(operationOrder);
            if (!flag){
                isException = true;
                exceptionOpOrder = operationOrder;
                break;
            }
            cancelOperate(operationOrder);
        }
        if (isException){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CommonException("工序工单为"+exceptionOpOrder+"已经开始生产，不能撤回",504);
        }
    }

    /**
     * 撤回操作
     */
    private void cancelOperate(String operationOrder){
        QueryWrapper<OperationOrder> operationOrderQw = new QueryWrapper<OperationOrder>().eq("operation_order", operationOrder);
        OperationOrder operationOrderObj = operationOrderMapper.selectOne(operationOrderQw);//工单下达数量
        String shopOrder = operationOrderObj.getShopOrder();//对应工单
        BigDecimal operationOrderQty = operationOrderObj.getOperationOrderQty();//工序工单数量（下达数量）
        operationOrderMapper.delete(operationOrderQw);//删除工序工单
        QueryWrapper<ShopOrder> shopOrderQw = new QueryWrapper<ShopOrder>().eq("shop_order", shopOrder);
        BigDecimal releaseQty = shopOrderMapper.selectOne(shopOrderQw).getReleaseQty();
        //减少相应工单下达数量
        ShopOrder shopOrderObj = new ShopOrder();
        shopOrderObj.setReleaseQty(releaseQty.subtract(operationOrderQty));
        shopOrderMapper.update(shopOrderObj,shopOrderQw);

        //清除派工表所有相关的数据
        dispatchMapper.delete(new QueryWrapper<Dispatch>().eq("operation_order",operationOrder));
        //删除sfc表相关数据
        sfcMapper.delete(new QueryWrapper<Sfc>().eq("operation_order",operationOrder));
        //删除线圈派工表中的数据
        stemDispatchMapper.delete(new QueryWrapper<StemDispatch>().eq("operation_order",operationOrder));
    }

    /**
     * 处理完工时间
     */
    public void saveEndTime(ShopOrderFullVo shopOrderFullVo,ShopOrder shopOrder){
        if ("502".equals(shopOrderFullVo.getState()) || "503".equals(shopOrderFullVo.getState())){
            shopOrder.setEndTime(new Date());
        }
    }
}