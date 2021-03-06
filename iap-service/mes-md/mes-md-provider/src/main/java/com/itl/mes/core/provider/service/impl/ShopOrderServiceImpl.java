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
 * ????????? ???????????????
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
     * ??????ShopOrderHandleBO?????????????????????
     *
     * @param shopOrderHandleBO ??????BOHandle
     * @return ShopOrder
     * @throws CommonException ?????????
     */
    @Override
    public ShopOrder getExistShopOrder(ShopOrderHandleBO shopOrderHandleBO) throws CommonException {
        ShopOrder shopOrder = super.getById(shopOrderHandleBO.getBo());
        if (shopOrder == null) {
            throw new CommonException("??????" + shopOrderHandleBO.getShopOrder() + "?????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return shopOrder;
    }

    /**
     * ??????ShopOrderHandleBO????????????
     *
     * @param shopOrderHandleBO ??????BOHandle
     * @return ??????
     */
    @Override
    public ShopOrder getShopOrder(ShopOrderHandleBO shopOrderHandleBO) {
        return super.getById(shopOrderHandleBO.getBo());
    }


    /**
     * ????????????????????????
     *
     * @param shopOrderHandleBO ??????BOHandle
     * @return ShopOrderFullVo
     * @throws CommonException ??????
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

        //??????????????????
        /*if (!StrUtil.isBlank(shopOrder.getCustomerOrderBo())) {

            CustomerOrderHandleBO customerOrderHandleBO = new CustomerOrderHandleBO(shopOrder.getCustomerOrderBo());
            shopOrderFullVo.setCustomerOrder(customerOrderHandleBO.getCustomerOrder());
        }*/

        //??????BOM
        if (!StrUtil.isBlank(shopOrder.getBomBo())) {

            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getBomBo());
            shopOrderFullVo.setBom(bomHandleBO.getBom());
            shopOrderFullVo.setBomVersion(bomHandleBO.getVersion());
        }
        //??????????????????
        if (!StrUtil.isBlank(shopOrder.getRouterBo())) {
            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            shopOrderFullVo.setRouter(routerHandleBO.getRouter());
            shopOrderFullVo.setRouterVersion(routerHandleBO.getVersion());
        }
        /*if (!StrUtil.isBlank(shopOrder.getRouterBo())) {

            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            //????????????????????????????????????????????????????????????????????????
            Router router= routerService.getRouterByRouter(routerHandleBO.getRouter());
            // ????????????????????????????????????????????????
            if(router ==null){
                // ?????????????????????????????????????????????????????????
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
        //????????????
        if (!StrUtil.isBlank(shopOrder.getProductLineBo())) {

            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrder.getProductLineBo());
            shopOrderFullVo.setProductLine(productLineHandleBO.getProductLine());
            shopOrderFullVo.setProductLineDesc(productLineMapper.selectOne(new QueryWrapper<ProductLine>().eq("PRODUCT_LINE",productLineHandleBO.getProductLine())).getProductLineDesc());
        }

        //??????????????????
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
                    //???????????????????????????
                    ShopOrder shopOrderEntity = shopOrderMapper.selectById(shopOrderDTO.getShopOrderBo());
                    if (ShopOrder.FINISHED_STATE.equals(shopOrderEntity.getStateBo()) || ShopOrder.CLOSE_STATE.equals(shopOrderEntity.getStateBo())){
                        try {
                            throw new CommonException("????????????:" + shopOrderDTO.getShopOrder() + "??????????????????????????????????????????????????????",CommonExceptionDefinition.VERIFY_EXCEPTION);
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
                            throw new CommonException("????????????:" + shopOrderDTO.getShopOrder() + "??????????????????????????????????????????",CommonExceptionDefinition.VERIFY_EXCEPTION);
                        } catch (CommonException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    OperationOrder operationOrder = new OperationOrder();
                    QueryWrapper<OperationOrder> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("shop_order",shopOrderDTO.getShopOrder()).orderByDesc("CREATE_DATE");
                    List<OperationOrder> operationOrders = operationOrderMapper.selectList(queryWrapper);
                    String number = "";
                    if (!CollectionUtils.isEmpty(operationOrders)){//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????ID????????????number + 1
                        for (String s : operationOrders.get(0).getOperationOrder().split(",")) {
                            number = s;
                        }
                        int numberLast = Integer.parseInt(number) + 1;
                        operationOrder.setBo("OO:" + shopOrderDTO.getShopOrder() + "," + numberLast);
                        operationOrder.setOperationOrder(shopOrderDTO.getShopOrder() + "," + numberLast);
                    }else {//?????????????????????number???1??????
                        operationOrder.setBo("OO:" + shopOrderDTO.getShopOrder() + ",1");
                        operationOrder.setOperationOrder(shopOrderDTO.getShopOrder() + ",1");
                    }

                    ShopOrderDTO shopOrder = new ShopOrderDTO();
                    List<String> bos = new ArrayList<>();
                    bos.add(shopOrderDTO.getShopOrderBo());
                    shopOrder.setBos(bos);
                    shopOrder.setSite(UserUtils.getSite());
                    List<ShopOrder> records = shopOrderMapper.getShopOrder(shopOrderDTO.getShopOrderBo());//?????????????????????????????????

                    operationOrder.setShopOrder(shopOrderDTO.getShopOrder());
//                    operationOrder.setOperationOrderState("");//??????

                    operationOrder.setOperationOrderQty(shopOrderDTO.getOperationOrderQty());

                    String router = records.get(0).getRouter();//??????????????????
                    String routerVersion = records.get(0).getVersion();//??????????????????
                    String routerBo = records.get(0).getRouterBo();//????????????BO
                    if (StringUtils.isBlank(records.get(0).getRouter()) || StringUtils.isBlank(records.get(0).getVersion())){
                        //????????????????????????????????????????????????????????????????????????????????????????????????
                        Item itemObj = itemService.getById(records.get(0).getItemBo());

                        QueryWrapper<Router> qw = new QueryWrapper<>();
                        qw.eq("router",itemObj.getRouterName());
                        qw.eq("is_current_version",1);//????????????
                        Router routerObj = routerMapper.selectOne(qw);

                        if (ObjectUtil.isEmpty(routerObj)){
                            try {
                                throw new CommonException("????????????:" + shopOrderDTO.getShopOrder() + "??????????????????????????????,??????????????????????????????????????????",CommonExceptionDefinition.VERIFY_EXCEPTION);
                            } catch (CommonException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        router = routerObj.getRouter();
                        routerVersion = routerObj.getVersion();
                        routerBo = routerObj.getBo();
                    }
                    operationOrder.setRoutre(router);
                    operationOrder.setVersion(routerVersion);//??????????????????
                    operationOrder.setItem(records.get(0).getItem());
                    operationOrder.setItemName(records.get(0).getItemName());
                    operationOrder.setIterVersion(records.get(0).getItemVersion());
                    operationOrder.setWorkShop(records.get(0).getWorkShop());
                    operationOrder.setIsUrgent(shopOrderDTO.getIsUrgent());

                    operationOrder.setCreateUser(userUtil.getUser().getUserName());
                    operationOrder.setCreateDate(new Date());

                    //????????????????????????
                    operationOrderMapper.insert(operationOrder);
                    ShopOrder shopOrderObj = new ShopOrder();
                    shopOrderObj.setReleaseQty(records.get(0).getReleaseQty().add(shopOrderDTO.getOperationOrderQty()));
                    shopOrderObj.setStateBo("STATE:dongyin,501");
                    shopOrderObj.setRouterBo(routerBo);
                    //??????????????????????????????
                    shopOrderMapper.update(shopOrderObj,new QueryWrapper<ShopOrder>().eq("bo",shopOrderDTO.getShopOrderBo()));
                    insertNoDispatch(operationOrder,router,routerVersion,shopOrderDTO.getShopOrderBo());
                }
        );
    }

    /**
     * ???????????????????????????????????????m_dispatch????????????????????????????????????
     */
    public void insertNoDispatch(OperationOrder operationOrder,String router,String routerVersion,String shopOrderBo){
        //???????????????????????????????????????????????????????????????
        String processInfo = routeStationService.queryRouterOperation(router,routerVersion);

        JSONObject jsonObj = JSON.parseObject(processInfo);
        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        JSONArray lineList = JSONArray.parseArray(jsonObj.getString("lineList"));

        List<String> operationList = Lists.newArrayList();
        if (nodeList.size() > 0){
            for (int i=0;i<nodeList.size();i++){
                JSONObject operationObj = JSON.parseObject(nodeList.get(i).toString());
                //????????????
                String operation = operationObj.getString("operation");
                if (StringUtils.isNotBlank(operation)){
                    operationList.add(operation);
                }
            }
        }
        String startId = sfcServiceImpl.getStartId(nodeList);
        String nextId = sfcServiceImpl.getNextId(lineList, startId);
        String theFirstOp = sfcServiceImpl.getOperationById(nodeList,nextId);//?????????

        if (!CollectionUtils.isEmpty(operationList)){
            //??????????????????
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
                if ("yes".equals(isSendToD)){//??????????????????????????????
                    dispatch.setIsNeedDispatch("0");
                }
                dispatch.setNotDoneQty(operationOrder.getOperationOrderQty());
                //????????????????????????
                if (operation.equals(theFirstOp)){
                    dispatch.setIsFirstOperation("1");
                }
                dispatch.setCanPrintQty(operationOrder.getOperationOrderQty());
                //??????????????????,????????????????????? - 1???2???3....
                if (!"yes".equals(isSendToD)){
                    dispatch.setDispatchCode(operationOrder.getOperationOrder() + "-" + number);
                    number++;
                }

                dispatchMapper.insert(dispatch);
            }
        }
    }
    /**
     * ????????????????????????
     *
     * @param shopOrderFullVo ??????shopOrderFullVo
     * @throws CommonException ??????
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveShopOrder(ShopOrderFullVo shopOrderFullVo) throws CommonException {

        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderFullVo.getShopOrder());
        ShopOrder shopOrder = getShopOrder(shopOrderHandleBO);
        if (shopOrder == null) { //??????

            insertShopOrder(shopOrderFullVo);
        } else { //??????

            updateShopOrder(shopOrder, shopOrderFullVo);
        }
        //?????????????????????
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
     * ??????
     *
     * @param shopOrderFullVo shopOrderFullVo
     * @throws CommonException ??????
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
        itemService.getExitsItemByItemHandleBO(itemHandleBO); //?????????????????????
        shopOrder.setItemBo(itemHandleBO.getBo());

        //??????????????????????????????
        if (!StrUtil.isBlank(shopOrderFullVo.getBom()) && !StrUtil.isBlank(shopOrderFullVo.getBomVersion())) {

            Bom bom = bomService.selectByBom(shopOrderFullVo.getBom(), shopOrderFullVo.getBomVersion());
            shopOrder.setBomBo(bom.getBo());

        }
        if (StringUtils.isNotBlank(shopOrderFullVo.getRouter()) && StringUtils.isNotBlank(shopOrderFullVo.getRouterVersion())){
            shopOrder.setRouterBo(new RouterHandleBO(UserUtils.getSite(), shopOrderFullVo.getRouter(),shopOrderFullVo.getRouterVersion()).getBo());
        }else {
            // ????????????????????????????????????????????????????????????????????????????????????????????????
            if(StringUtils.isNotBlank(shopOrderFullVo.getItem())){
                Item item=itemMapper.selectOne(new QueryWrapper<Item>().eq("item",shopOrderFullVo.getItem()).eq("VERSION",shopOrderFullVo.getItemVersion()));
                // ?????????????????????????????????????????????????????????????????????
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
        //????????????????????????????????????
        if (shopOrderFullVo.getPlanStartDate() != null && shopOrderFullVo.getPlanEndDate() != null) {

            if (shopOrderFullVo.getPlanStartDate().getTime() > shopOrderFullVo.getPlanEndDate().getTime()) {
                throw new CommonException("????????????????????????????????????????????????",CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        //??????????????????????????????
        /*if (!StrUtil.isBlank(shopOrderFullVo.getRouter()) && !StrUtil.isBlank(shopOrderFullVo.getRouterVersion())) {

            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getRouter(), shopOrderFullVo.getRouterVersion());
            routerService.getExistRouterByHandleBO(routerHandleBO);
            shopOrder.setRouterBo(routerHandleBO.getBo());

        }*/
        //????????????
        if (!StrUtil.isBlank(shopOrderFullVo.getProductLine())) {

            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getProductLine());
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrder.setProductLineBo(productLineHandleBO.getBo());
        }
        //??????????????????????????????
        /*if (!StrUtil.isBlank(shopOrderFullVo.getCustomerOrder())) {

            CustomerOrderHandleBO customerOrderHandleBO = new CustomerOrderHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getCustomerOrder());
            customerOrderService.getExistCustomerOrder(customerOrderHandleBO);
            shopOrder.setCustomerOrderBo(customerOrderHandleBO.getBo());

        }*/
        shopOrder.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(shopOrder); //??????????????????????????????
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //??????????????????
        super.save(shopOrder);

    }

    /**
     * ??????????????????
     *
     * @param shopOrder       ????????????????????????
     * @param shopOrderFullVo ???????????????
     */
    private void updateShopOrder(ShopOrder shopOrder, ShopOrderFullVo shopOrderFullVo) throws CommonException {
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(shopOrder.getBo());
        ShopOrder shopOrderEntity = new ShopOrder();
        shopOrderEntity.setBo(shopOrderHandleBO.getBo());
        shopOrderEntity.setOrderDesc(StrUtil.isBlank(shopOrderFullVo.getOrderDesc()) ? shopOrderFullVo.getShopOrder() : shopOrderFullVo.getOrderDesc());
        if ("502".equals(shopOrderFullVo.getState())){//?????????????????????????????????
            QueryWrapper<Sfc> sfcQw = new QueryWrapper<>();
            sfcQw.eq("shop_order_bo",shopOrderHandleBO.getBo());
            sfcQw.eq("state","?????????");
            List<Sfc> sfcList = sfcService.list(sfcQw);
            if (CollectionUtil.isNotEmpty(sfcList)){
                throw new CommonException("???SFC???????????????????????????",CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        shopOrderEntity.setStateBo(new StatusHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getState()).getBo());
        shopOrderEntity.setIsOverfulfill(shopOrderFullVo.getIsOverfulfill());
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (shopOrderFullVo.getOrderQty().compareTo(shopOrder.getReleaseQty()) == -1){
            throw new CommonException("????????????????????????????????????????????????",CommonExceptionDefinition.VERIFY_EXCEPTION);
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
        //????????????????????????????????????
        if (shopOrderEntity.getPlanStartDate() != null && shopOrderEntity.getPlanEndDate() != null) {

            if (shopOrderEntity.getPlanStartDate().getTime() > shopOrderEntity.getPlanEndDate().getTime()) {
                throw new CommonException("????????????????????????????????????????????????",CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getItem(), shopOrderFullVo.getItemVersion());
        itemService.getExitsItemByItemHandleBO(itemHandleBO); //?????????????????????
        shopOrderEntity.setItemBo(itemHandleBO.getBo());

        /*if (!StrUtil.isBlank(shopOrderFullVo.getCustomerOrder())) {
            //??????????????????????????????
            CustomerOrderHandleBO customerOrderHandleBO = new CustomerOrderHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getCustomerOrder());
            customerOrderService.getExistCustomerOrder(customerOrderHandleBO);
            shopOrderEntity.setCustomerOrderBo(customerOrderHandleBO.getBo());
        } else {
            shopOrderEntity.setCustomerOrderBo("");
        }

        if (!StrUtil.isBlank(shopOrderFullVo.getRouter()) && !StrUtil.isBlank(shopOrderFullVo.getRouterVersion())) {
            //??????????????????????????????
            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getRouter(), shopOrderFullVo.getRouterVersion());
            routerService.getExistRouterByHandleBO(routerHandleBO);
            shopOrderEntity.setRouterBo(routerHandleBO.getBo());
        } else {
            shopOrderEntity.setRouterBo("");
        }*/

        if (!StrUtil.isBlank(shopOrderFullVo.getBom()) && !StrUtil.isBlank(shopOrderFullVo.getBomVersion())) {
            //??????????????????????????????
            Bom bom = bomService.selectByBom(shopOrderFullVo.getBom(), shopOrderFullVo.getBomVersion());
            shopOrderEntity.setBomBo(bom.getBo());
        } else {
            shopOrderEntity.setBomBo("");
        }

        if (!StrUtil.isBlank(shopOrderFullVo.getProductLine())) {
            //????????????????????????
            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getProductLine());
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrderEntity.setProductLineBo(productLineHandleBO.getBo());
        } else {
            shopOrderEntity.setProductLineBo("");
        }

        //????????????????????????????????????
        ValidationUtil.ValidResult validResult =
                ValidationUtil.validateProperties(shopOrderEntity, "orderDesc", "stateBo", "isOverfulfill", "orderQty");
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        Integer integer = shopOrderMapper.updateShopOrder(shopOrderEntity, shopOrder.getModifyDate());//??????????????????
        if (integer == 0) {
            throw new CommonException("?????????????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

    }


    /**
     * ???????????????????????????
     *
     * @param shopOrder ??????????????????
     * @return boolean
     * @throws CommonException ??????
     */
    private boolean validateShopOrderIsUsed(ShopOrder shopOrder) throws CommonException {
        if (shopOrder == null) {
            return false;
        } else {
            //???????????????
            if (shopOrder.getSchedulQty() != null && shopOrder.getSchedulQty().doubleValue() > 0) return true;
            //???????????????
            if (shopOrder.getReleaseQty() != null && shopOrder.getReleaseQty().doubleValue() > 0) return true;
            //????????????
            //????????????
            //????????????
            //????????????
            return false;
        }

    }


    /**
     * ??????????????????
     *
     * @param shopOrderHandleBO ??????BOHandle
     * @param modifyDate        ????????????
     * @throws CommonException ??????
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteShopOrderByHandleBO(ShopOrderHandleBO shopOrderHandleBO, Date modifyDate) throws CommonException {

        ShopOrder shopOrder = getExistShopOrder(shopOrderHandleBO);
//        CommonUtil.compareDateSame(modifyDate, shopOrder.getModifyDate());
        boolean isUsed = validateShopOrderIsUsed(shopOrder);
        if (isUsed) {
            throw new CommonException("??????"+shopOrder+"????????????????????????",CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        QueryWrapper<ShopOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(ShopOrder.BO, shopOrder.getBo());

        //??????????????????
        Integer integer = shopOrderMapper.delete(wrapper);
        if (integer == 0) {
            throw new CommonException("???????????????????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //???????????????????????????
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
            throw new BusinessException("???????????????????????????????????????????????????????????????????????????");
        }
        if (router == null) {
            throw new BusinessException("???????????????????????????????????????????????????????????????????????????");
        }


        //?????????????????????
        if (existShopOrder.getReleaseQty() != null) {
            stayNumber = existShopOrder.getReleaseQty().add(stayNumber);
        }

        //????????????????????????
        if ("Y".equals(existShopOrder.getIsOverfulfill())) {
            //????????????
            if ( existShopOrder.getOverfulfillQty()!=null && existShopOrder.getOverfulfillQty().compareTo( BigDecimal.ZERO )==1 ) {
                //???????????? + ????????????
                int i = stayNumber.compareTo(existShopOrder.getOrderQty().add(existShopOrder.getOverfulfillQty()));
                if (i == 1) {
                    throw new BusinessException("??????"+existShopOrder.getShopOrder()+"????????????????????????????????????????????????????????????!");
                }
                Integer integer = shopOrderMapper.updateLimitQtyShopOrderReleaseQtyByBO(existShopOrder.getBo(), stayNumber, existShopOrder.getOverfulfillQty());
                if (integer < 1){
                    throw new BusinessException("??????"+existShopOrder.getShopOrder()+"????????????????????????????????????????????????????????????!");
                }
            } else {
                shopOrderMapper.updateOverfullQtyShopOrderReleaseQtyByBO( existShopOrder.getBo(), stayNumber );
            }
        } else {
            int i = stayNumber.compareTo(existShopOrder.getOrderQty());
            if (i == 1) {
                throw new BusinessException("??????"+existShopOrder.getShopOrder()+"???????????????????????????????????????!");
            }
            Integer integer = shopOrderMapper.updateShopOrderReleaseQtyByBO(existShopOrder.getBo(), stayNumber);
            if (integer < 1){
                throw new BusinessException("??????"+existShopOrder.getShopOrder()+"???????????????????????????????????????!");
            }
        }

        CodeRule codeRule;
        //??????itembo?????? ?????????BO
        Wrapper<ItemGroupMember> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq(ItemGroupMember.ITEM_BO, itemBo);
        List<ItemGroupMember> itemGroupMembers = itemGroupMemberService.selectList(entityWrapper);
        List<ItemGroup> itemGroupList = new ArrayList<>();
        ItemGroup itemGroup = new ItemGroup();
        if (!itemGroupMembers.isEmpty()) {
            for (ItemGroupMember itemGroupMember : itemGroupMembers) {
                //??????????????? ???????????????????????? ?????? BO
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
            throw new BusinessException("???????????????????????????????????????????????????????????????");
        }


        //?????????????????? ????????????
        BigDecimal lotSize = itemEntity.getLotSize();
        BigDecimal divide = stayNumber.divide(lotSize, 6, RoundingMode.HALF_UP);
        BigDecimal number = divide.setScale(0, BigDecimal.ROUND_UP);//????????????
        BigDecimal remainder = stayNumber.divideAndRemainder(lotSize)[1];//??????

        List<String> sfcList = codeRuleService.generatorNextNumberList(codeRule.getBo(), number.intValue());

        //????????????????????????decimal??????
        BigDecimal bigDecimal_1 = new BigDecimal(1);
        BigDecimal bigDecimal_0 = new BigDecimal(0);

        List<Sfc> sfcs = new ArrayList<>();
        List<SfcRouter> sfcRouterList = new ArrayList<>();
        List<SfcStep> sfcStepList = new ArrayList<>();
        List<SfcActive> sfcActiveList = new ArrayList<>();
        int i = checkForDuplicates(sfcList);
        if (i != -1) {
            throw new BusinessException("SFC??????:" + sfcList.get(i) + "????????????");
        }
        for (String sfcSerial : sfcList) {
            //??????sfc???????????????  ???????????????
            sfcService.verifyExist(sfcSerial);
            Sfc sfc = new Sfc();
            SfcHandleBO sfcHandleBO = new SfcHandleBO(site, sfcSerial);
            sfc.setBo(sfcHandleBO.getBo());
            sfc.setSfc(sfcSerial);
            sfc.setSite(site);
            sfc.setItemBo(existShopOrder.getItemBo());
            if (sfcList.get(sfcList.size() - 1).equals(sfcSerial)) {
                if (remainder.equals(BigDecimal.ZERO)) {
                    sfc.setQty(lotSize);//???????????????
                } else {
                    sfc.setQty(remainder);//??????
                }
            } else {
                sfc.setQty(lotSize);//???????????????
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

            //??????routerBO??????M_ROUTER_STEP,router_step_operation?????? ?????? stepId ,OPERATION_BO
            List<SfcGetRouterParamVo> routerParamVos = sfcMapper.getRouterParamVos(router.getBo());
            if (routerParamVos.size() == 0) {
                throw new BusinessException("????????????" + router.getRouter() + "???????????????????????????");
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
                //??????????????????????????????
                Wrapper<RouterStepOperation> entityWrapper1 = new EntityWrapper<>();
                entityWrapper1.eq(RouterStepOperation.ROUTER_STEP_BO, router.getEntryRouterStepBo());
                List<RouterStepOperation> routerStepOperations = routerStepOperationService.selectList(entityWrapper1);
                for (RouterStepOperation routerStepOperation : routerStepOperations) {
                    if (routerStepOperation.getOperationBo().equals(routerParamVo.getOperationBo())) {
                        sfcStep.setQtyInQueue(sfc.getQty()); //sfc ??????
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


    //??????Sfc?????????????????????
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public List<Sfc> shopOrderReleaseBySfcAndNumVos(String shopOrder, String planStartDate, String planEndDate, List<SfcAndNumVo> sfcAndNumVos) throws BusinessException {
        return shopOrderReleaseBySfcAndNumVos( shopOrder,planStartDate,planEndDate,null,sfcAndNumVos );
    }


    //??????Sfc?????????????????????
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public List<Sfc> shopOrderReleaseBySfcAndNumVos(String shopOrder, String planStartDate, String planEndDate,String routerBo,  List<SfcAndNumVo> sfcAndNumVos) throws BusinessException {
        String site = UserUtils.getSite();
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(site, shopOrder);
        ShopOrder existShopOrder = getExistShopOrder(shopOrderHandleBO);
        StatusHandleBO statusHandleBO = new StatusHandleBO( existShopOrder.getStateBo() );
        verifyShopOrderState(statusHandleBO,shopOrder);
        String itemBo = existShopOrder.getItemBo();

        //??????sfcAndNumVos ????????????
        if (sfcAndNumVos.size() == 0) {
            throw new BusinessException("sfc??????????????????!");
        }

        //??????sfcAndNumVos??? ????????????;
        BigDecimal total = BigDecimal.ZERO;
        //??????????????????????????????sfc??????
        List<String> list = new ArrayList<>();
        for (SfcAndNumVo sfcAndNumVo : sfcAndNumVos) {
            //??????sfc???????????????  ???????????????
            sfcService.verifyExist(sfcAndNumVo.getSfc());
            if (sfcAndNumVo.getNumber().compareTo(BigDecimal.ZERO) == 1){
                total = sfcAndNumVo.getNumber().add(total);
                list.add(sfcAndNumVo.getSfc());
            }else {
                throw new BusinessException("SFC??????:" + sfcAndNumVo.getSfc() + "??????????????????!");
            }

        }

        //?????????????????????
        if (existShopOrder.getReleaseQty() != null) {
            total = existShopOrder.getReleaseQty().add(total);
        }

        int index = checkForDuplicates(list);
        if (index != -1) {
            throw new BusinessException("SFC??????:" + list.get(index) + "????????????");
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
                throw new BusinessException("???????????????????????????????????????????????????????????????????????????");
            }
        }else {
            router = routerService.getExistRouterByHandleBO(new RouterHandleBO(routerBo));
        }


        //????????????????????????
        if ("Y".equals(existShopOrder.getIsOverfulfill())) {
            //????????????
            if ( existShopOrder.getOverfulfillQty()!=null && existShopOrder.getOverfulfillQty().compareTo( BigDecimal.ZERO )==1 ) {
                //???????????? + ????????????
                int i = total.compareTo(existShopOrder.getOrderQty().add(existShopOrder.getOverfulfillQty()));
                if (i == 1) {
                    throw new BusinessException("??????"+existShopOrder.getShopOrder()+"????????????????????????????????????????????????????????????!");
                }
                Integer integer = shopOrderMapper.updateLimitQtyShopOrderReleaseQtyByBO( existShopOrder.getBo(),
                        new BigDecimal(sfcAndNumVos.size() ), existShopOrder.getOverfulfillQty() );
                if( integer.intValue()<1 ){
                    throw new BusinessException("??????"+existShopOrder.getShopOrder()+"????????????????????????????????????????????????????????????!");
                }
            }else{
                shopOrderMapper.updateOverfullQtyShopOrderReleaseQtyByBO( existShopOrder.getBo(), new BigDecimal(sfcAndNumVos.size() ) );
            }
        } else {
            int i = total.compareTo(existShopOrder.getOrderQty());
            if (i == 1) {
                throw new BusinessException("??????"+existShopOrder.getShopOrder()+"???????????????????????????????????????!");
            }
            Integer integer = shopOrderMapper.updateShopOrderReleaseQtyByBO( existShopOrder.getBo(), new BigDecimal(sfcAndNumVos.size() ) );
            if( integer.intValue()<1 ){
                throw new BusinessException("??????"+existShopOrder.getShopOrder()+"???????????????????????????????????????!");
            }
        }


        //????????????????????????decimal??????
        BigDecimal bigDecimal_1 = new BigDecimal(1);
        BigDecimal bigDecimal_0 = new BigDecimal(0);

        List<Sfc> sfcs = new ArrayList<>();
        List<SfcRouter> sfcRouterList = new ArrayList<>();
        List<SfcStep> sfcStepList = new ArrayList<>();
        List<SfcActive> sfcActiveList = new ArrayList<>();

        for (SfcAndNumVo sfcAndNumVo : sfcAndNumVos) {
            String sfcCode = sfcAndNumVo.getSfc(); //sfc??????
            BigDecimal number = sfcAndNumVo.getNumber();//??????
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

            //??????routerBO??????M_ROUTER_STEP,router_step_operation?????? ?????? stepId ,OPERATION_BO
            List<SfcGetRouterParamVo> routerParamVos = sfcMapper.getRouterParamVos(router.getBo());
            if (routerParamVos.size() == 0) {
                throw new BusinessException("????????????" + router.getRouter() + "???????????????????????????");
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
                //??????????????????????????????
                Wrapper<RouterStepOperation> entityWrapper1 = new EntityWrapper<>();
                entityWrapper1.eq(RouterStepOperation.ROUTER_STEP_BO, router.getEntryRouterStepBo());
                List<RouterStepOperation> routerStepOperations = routerStepOperationService.selectList(entityWrapper1);
                for (RouterStepOperation routerStepOperation : routerStepOperations) {
                    if (routerStepOperation.getOperationBo().equals(routerParamVo.getOperationBo())) {
                        sfcStep.setQtyInQueue(sfc.getQty()); //sfc ??????
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
     * ????????????????????????
     *
     * @param bo ??????BO
     * @param completeQty ????????????
     * @return Integer
     */
    @Override
    public Integer updateShopOrderCompleteQtyByBO(String bo, BigDecimal completeQty){

        return shopOrderMapper.updateShopOrderCompleteQtyByBO( bo, completeQty );
    }

    /**
     * ??????????????????????????????
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
        if ("5422".equals(shopOrderDTO.getWorkShop()) || "5424".equals(shopOrderDTO.getWorkShop()) || "544".equals(shopOrderDTO.getWorkShop())){//???????????????????????????????????????????????????
            shopOrderIPage = shopOrderMapper.selectShopOrderReleaseByZz(shopOrderDTO.getPage(), shopOrderDTO);
        }else {
            shopOrderIPage = shopOrderMapper.selectShopOrderRelease(shopOrderDTO.getPage(), shopOrderDTO);
        }
        for (ShopOrder record : shopOrderIPage.getRecords()) {
            if (StringUtils.isBlank(record.getRouter())){
                //???????????????????????????????????????????????????
                Item itemObj = itemService.getById(record.getItemBo());
                if (StringUtils.isNotBlank(itemObj.getRouterName())){//routerName???????????????
                    QueryWrapper<Router> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("router",itemObj.getRouterName());
                    queryWrapper.eq("is_current_version",1);//????????????
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
     * ????????????????????????
     *
     * @param bo ??????BO
     * @param scrapTty ????????????
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
        //????????????????????????????????????????????????????????????????????????
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


        //??????BOM
        if (!StrUtil.isBlank(shopOrder.getBomBo())) {

            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getBomBo());
            shopOrderFullVo.setBom(bomHandleBO.getBom());
            shopOrderFullVo.setBomVersion(bomHandleBO.getVersion());
        }
        //??????????????????
        if (!StrUtil.isBlank(shopOrder.getRouterBo())) {

            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            shopOrderFullVo.setRouter(routerHandleBO.getRouter());
            shopOrderFullVo.setRouterVersion(routerHandleBO.getVersion());
        }
        //????????????
        if (!StrUtil.isBlank(shopOrder.getProductLineBo())) {

            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrder.getProductLineBo());
            shopOrderFullVo.setProductLine(productLineHandleBO.getProductLine());
        }

        //??????????????????
//        Item exitsItem = null;
//        try {
//            exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(shopOrder.getItemBo()));
//        } catch (CommonException e) {
//            throw new RuntimeException("??????????????????");
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
                //?????????
                String filter_ = key.replaceAll("filter_", "");
                Map<String, Object> map = new HashMap<>();
                map.put("key",filter_);
                map.put("value",params.get(key).toString());
                map.put("site",params.get("site").toString());
                n++;
                //????????????????????????
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
            throw new BusinessException( "??????"+sfc.getSfc()+"??????????????????????????????????????????"+targetShopOrder.getShopOrder()+"????????????" );
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
        updateShopOrderCompleteQtyByBO( targetShopOrderBo,sfc.getQty() ); //??????
        if( "Y".equals( targetShopOrder.getIsOverfulfill() ) ){ //??????
            if( targetShopOrder.getOverfulfillQty()!=null && targetShopOrder.getOverfulfillQty().compareTo( BigDecimal.ZERO )>0 ){
                //????????????
                Integer updateNum = shopOrderMapper
                        .updateLimitQtyShopOrderReleaseQtyByBO( targetShopOrderBo, sfc.getQty(),targetShopOrder.getOverfulfillQty() );
                if( updateNum<1 ){
                    throw new BusinessException( "??????"+targetShopOrder.getShopOrder()+"???????????????????????????????????????????????????????????????" );
                }
            }else{ //????????????
                shopOrderMapper.updateOverfullQtyShopOrderReleaseQtyByBO( targetShopOrderBo, sfc.getQty() );
            }
        }else{
            //?????????
            Integer updateNum = shopOrderMapper.updateShopOrderReleaseQtyByBO( targetShopOrderBo,sfc.getQty() );
            if( updateNum<1 ){
                throw new BusinessException( "??????"+targetShopOrder.getShopOrder()+"????????????????????????????????????" );
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
                throw new BusinessException( "??????"+shopOrder+"?????????,????????????" );
            }
            if( "503".equals( statusHandleBO.getState() ) ){
                throw new BusinessException( "??????"+shopOrder+"?????????,????????????" );
            }
            if( "504".equals( statusHandleBO.getState() ) ){
                throw new BusinessException( "??????"+shopOrder+"?????????,????????????" );
            }
            throw new BusinessException( "??????"+shopOrder+"????????????" );
        }
    }*/

    @Override
    public IPage<ShopOrderReportVo> getShopOrderReport(ShopOrderReportDTO shopOrderReportDTO) {
        if (ObjectUtil.isEmpty(shopOrderReportDTO.getPage())){
            shopOrderReportDTO.setPage(new Page(1,10));
        }
        IPage<ShopOrderReportVo> shopOrderReport = shopOrderMapper.getShopOrderReport(shopOrderReportDTO.getPage(), shopOrderReportDTO);
        for (ShopOrderReportVo shopOrderReportVo : shopOrderReport.getRecords()) {
            BigDecimal orderQty = shopOrderReportVo.getOrderQty();//????????????
            //????????????????????????????????????sfc????????????????????????sfc???????????? + ???????????????????????????
            BigDecimal waitQty = BigDecimal.ZERO;//???????????????
            QueryWrapper<Sfc> soQw = new QueryWrapper<>();
            soQw.eq("shop_order_bo",shopOrderReportVo.getShopOrderBo());
            soQw.eq("state","??????");
            List<Sfc> sfcListO = sfcService.list(soQw);
            for (Sfc sfc : sfcListO) {
                waitQty = waitQty.add(sfc.getSfcQty());
            }
            List<Sfc> sfcListT = sfcService.list(new QueryWrapper<Sfc>().eq("shop_order_bo", shopOrderReportVo.getShopOrderBo()));
            BigDecimal totalQty = BigDecimal.ZERO;//??????????????????????????????sfc????????????????????????
            for (Sfc sfc : sfcListT) {
                totalQty = totalQty.add(sfc.getSfcQty());
            }
            waitQty = waitQty.add(orderQty.subtract(totalQty));
            shopOrderReportVo.setWaitQty(waitQty);

            List<ShopOrderReportVo.MakingDetailsVo> makingDetailsList = shopOrderMapper.getMakingDetails(shopOrderReportVo.getShopOrderBo());//??????????????????
            shopOrderReportVo.setMakingDetails(makingDetailsList);
            BigDecimal makingTotalQty = BigDecimal.ZERO;//?????????????????????
            for (ShopOrderReportVo.MakingDetailsVo makingDetailsVo : makingDetailsList) {
                makingTotalQty = makingTotalQty.add(makingDetailsVo.getMakingQty());
            }
            shopOrderReportVo.setMakingTotalQty(makingTotalQty);
            QueryWrapper<Sfc> soQwT = new QueryWrapper<>();
            soQwT.eq("shop_order_bo",shopOrderReportVo.getShopOrderBo());
            soQwT.eq("state","?????????");
            List<Sfc> sfcListF = sfcService.list(soQwT);

            BigDecimal doneQty = BigDecimal.ZERO;//?????????
            for (Sfc sfc : sfcListF) {
                doneQty = doneQty.add(sfc.getSfcQty());
            }
            shopOrderReportVo.setDoneQty(doneQty);
            BigDecimal goToStockQty = BigDecimal.ZERO;//?????????
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
     * ???????????????????????????????????????
     * @return
     */
    private boolean isCanCancelOperationOrder(String operationOrder){
        List<Enter> enterList = enterMapper.selectList(new QueryWrapper<Enter>().eq("operation_order", operationOrder));
        if (CollectionUtil.isNotEmpty(enterList)){
            return false;//??????????????????????????????
        }
        return true;//?????????????????????????????????
    }

    @Override
    public void okCancelOperationOrder(List<String> operationOrderList) throws CommonException{
        boolean isException = false;//????????????????????????????????????
        String exceptionOpOrder = null;//??????????????????
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
            throw new CommonException("???????????????"+exceptionOpOrder+"?????????????????????????????????",504);
        }
    }

    /**
     * ????????????
     */
    private void cancelOperate(String operationOrder){
        QueryWrapper<OperationOrder> operationOrderQw = new QueryWrapper<OperationOrder>().eq("operation_order", operationOrder);
        OperationOrder operationOrderObj = operationOrderMapper.selectOne(operationOrderQw);//??????????????????
        String shopOrder = operationOrderObj.getShopOrder();//????????????
        BigDecimal operationOrderQty = operationOrderObj.getOperationOrderQty();//????????????????????????????????????
        operationOrderMapper.delete(operationOrderQw);//??????????????????
        QueryWrapper<ShopOrder> shopOrderQw = new QueryWrapper<ShopOrder>().eq("shop_order", shopOrder);
        BigDecimal releaseQty = shopOrderMapper.selectOne(shopOrderQw).getReleaseQty();
        //??????????????????????????????
        ShopOrder shopOrderObj = new ShopOrder();
        shopOrderObj.setReleaseQty(releaseQty.subtract(operationOrderQty));
        shopOrderMapper.update(shopOrderObj,shopOrderQw);

        //????????????????????????????????????
        dispatchMapper.delete(new QueryWrapper<Dispatch>().eq("operation_order",operationOrder));
        //??????sfc???????????????
        sfcMapper.delete(new QueryWrapper<Sfc>().eq("operation_order",operationOrder));
        //?????????????????????????????????
        stemDispatchMapper.delete(new QueryWrapper<StemDispatch>().eq("operation_order",operationOrder));
    }

    /**
     * ??????????????????
     */
    public void saveEndTime(ShopOrderFullVo shopOrderFullVo,ShopOrder shopOrder){
        if ("502".equals(shopOrderFullVo.getState()) || "503".equals(shopOrderFullVo.getState())){
            shopOrder.setEndTime(new Date());
        }
    }
}