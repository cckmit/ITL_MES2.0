package com.itl.mes.core.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.StrNotNull;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CodeRuleFullVo;
import com.itl.mes.core.api.vo.CreateSnVo;
import com.itl.mes.core.api.vo.OpeartionItemVo;
import com.itl.mes.core.api.vo.SfcVo;
import com.itl.mes.core.provider.mapper.ShopOrderMapper;
import com.itl.mes.core.provider.mapper.WorkShopMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author sky,
 * @date 2019/8/5
 * @time 15:36
 */
@Service
@Transactional
public class CreateSnServiceImpl implements CreateSnService {

    @Autowired
    private SnRuleService snRuleService;

    @Autowired
    private SnService snService;

    @Autowired
    private SfcService sfcService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private CodeRuleService codeRuleService;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private ItemGroupService itemGroupService;

    @Autowired
    private SnLogService snLogService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemGroupMemberService itemGroupMemberService;

    @Autowired
    private RouterProcessService routerProcessService;

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private WorkShopMapper workShopMapper;

    @Resource
    private UserUtil userUtil;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private DispatchService dispatchService;

    @Override
    public List<SnRule> querySnRuleByT() throws CommonException {
        QueryWrapper<SnRule> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(SnRule.RULE_TYPE, "T");
        List<SnRule> snRules = snRuleService.list(entityWrapper);
        if (snRules.size() == 0) {
            throw new CommonException("??????????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return snRules;
    }

    @Override
    public List<SnRule> querySnRuleByS() throws CommonException {
        QueryWrapper<SnRule> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(SnRule.RULE_TYPE, "S");
        List<SnRule> snRules = snRuleService.list(entityWrapper);
        if (snRules.size() == 0) {
            throw new CommonException("??????????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return snRules;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void createSn(String number, String codeRuleType, Boolean whether, String shopOrder) throws CommonException {
        String site = UserUtils.getSite();
        CodeRule codeRule = codeRuleService.getExistCodeRule(new CodeRuleHandleBO(site, codeRuleType));

        List<Sn> sns = new ArrayList<>();
        int num = Integer.parseInt(number);

        if (num > 100000) {
            throw new CommonException("??????????????????????????????10???!",CommonExceptionDefinition.SN_ENCODING_EXCEPTION);
        }

        //??????????????????
        ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(site, shopOrder));
        ItemHandleBO itemHandleBO = new ItemHandleBO(existShopOrder.getItemBo());

        HashMap<String, Object> params = new HashMap<>();
        CodeRuleFullVo fullVo = codeRuleService.getCodeRuleType(codeRuleType);
        fullVo.getCodeRuleItemVoList().forEach(ruleItem -> {
            if ("4".equals(ruleItem.getSectType())) {
                params.put(ruleItem.getSectParam(), itemHandleBO.getItem());
            }
        });
        //??????num?????????
        List<String> snsByRule = codeRuleService.generatorNextNumberList(codeRule.getBo(), num, params);

        //???????????????
        SnLog snLog = new SnLog();
        if (whether) {
            for (int i = 1; i < num + 1; i++) {
                //?????????sn???
                String sn = snsByRule.get(i - 1);
                Sn snEntity = new Sn();
                snEntity.setBo(new SnHandleBO(site, sn).getBo());
                snEntity.setSite(site);
                snEntity.setSn(sn);
                snEntity.setShopOrder(existShopOrder.getShopOrder());
                snEntity.setItemBo(existShopOrder.getItemBo());
                snEntity.setOriginalItemBo(existShopOrder.getItemBo());
                snEntity.setState("401");
                snEntity.setQty(new BigDecimal(1));
                snEntity.setComplementCodeState("Y");
                snEntity.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
                sns.add(snEntity);

                if (i == 1) {
                    snLog.setStartNumber(snEntity.getSn());
                }
                if (i == num) {
                    snLog.setEndNumber(snEntity.getSn());
                }
            }
        } else {
            for (int i = 1; i < num + 1; i++) {
                String sn = snsByRule.get(i - 1);
                //?????????sn???
                Sn snEntity = new Sn();
                snEntity.setBo(new SnHandleBO(site, sn).getBo());
                snEntity.setSite(site);
                snEntity.setSn(sn);
                snEntity.setShopOrder(existShopOrder.getShopOrder());
                snEntity.setItemBo(existShopOrder.getItemBo());
                snEntity.setOriginalItemBo(existShopOrder.getItemBo());
                snEntity.setState("401");
                snEntity.setQty(new BigDecimal(1));
                snEntity.setComplementCodeState("N");
                snEntity.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
                sns.add(snEntity);

                if (i == 1) {
                    snLog.setStartNumber(snEntity.getSn());
                }
                if (i == num) {
                    snLog.setEndNumber(snEntity.getSn());
                }
            }
        }
        try {
            snService.saveBatch(sns);

            snLog.setBo(UUID.randomUUID().toString());
            snLog.setSite(UserUtils.getSite());

            snLog.setItem(itemHandleBO.getItem());
            //?????????????????????
            List<String> assignedItemGroupList = itemGroupService.getAssignedItemGroupListBySiteAndItemBO(new ItemHandleBO(existShopOrder.getItemBo()));
            if (assignedItemGroupList.size() > 0) {
                snLog.setMaterialType(assignedItemGroupList.get(0));
            }
            snLog.setShopOrder(existShopOrder.getShopOrder());
            snLog.setCreateQuantity(num);
            snLog.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
            snLogService.save(snLog);
        } catch (Exception e) {
            throw new CommonException("?????????????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }


    @Override
    public IPage<Map<String, Object>> selectPageByShape(IPage<Map<String, Object>> page, Map<String, Object> params) {

        List<Map<String, Object>> list = snService.selectPageShopOrderByShape(page, params);
        page.setRecords(list);
        return page;
    }


    @Override
    public String selectTypeByShopOrder(String shopOrder) throws CommonException {
        //??????????????????????????????????????????????????????????????????
        ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(UserUtils.getSite(), shopOrder));
        Item exitesItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(existShopOrder.getItemBo()));
        QueryWrapper<ItemGroupMember> itemGroupMemberEntityWrapper = new QueryWrapper<>();
        itemGroupMemberEntityWrapper.eq(ItemGroupMember.ITEM_BO, exitesItem.getBo());
        List<ItemGroupMember> itemGroupMembers = itemGroupMemberService.list(itemGroupMemberEntityWrapper);
        String vals = null;
        if (itemGroupMembers.size() > 0) {
            ItemGroup itemGroup = itemGroupService.getItemGroupByItemGroupBO(new ItemGroupHandleBO(itemGroupMembers.get(0).getItemGroupBo()));
            //??????????????????????????????CODE
            vals = snService.getSelfDefiningData(UserUtils.getSite(), "ITEM_GROUP", "CODE", itemGroup.getBo());

        }
        //??????RULE_CODE ????????????????????????
        String ruleName = null;
        if (vals != null) {
            QueryWrapper<SnRule> snRuleEntityWrapper = new QueryWrapper<>();
            snRuleEntityWrapper.eq(SnRule.RULE_CODE, vals).eq(SnRule.RULE_TYPE, "T");
            List<SnRule> snRules = snRuleService.list(snRuleEntityWrapper);
            if (snRules.size() > 0) {
                ruleName = snRules.get(0).getRuleName();
            }
        }
        return ruleName;
    }

    @Override
    public void importExcel(MultipartFile file) throws CommonException {
        List<CreateSnVo> createSnVos = ExcelUtils.importExcel(file, 1, 1, CreateSnVo.class);
        if (createSnVos.size() > 0) {
            for (CreateSnVo createSnVo : createSnVos) {
                StrNotNull.validateNotNull(createSnVo.getNumber(),"????????????????????????" );
                StrNotNull.validateNotNull(createSnVo.getCodeRuleType(),"????????????????????????" );
                StrNotNull.validateNotNull(createSnVo.getShopOrder(),"?????????????????????" );
                //StrNotNull.validateNotNull(createSnVo.getWhether(),"????????????????????????" );
                //???????????? ????????? ???
                Boolean whether = false;
                if ("???".equals(createSnVo.getWhether())) {
                    whether = true;
                }
                createSn(createSnVo.getNumber(),createSnVo.getCodeRuleType(),whether,createSnVo.getShopOrder());
            }
        }
    }


    @Override
    public void exportExcel(String site, HttpServletResponse response) throws CommonException {
        List<CreateSnVo> createSnVos = new ArrayList<>();
        ExcelUtils.exportExcel(createSnVos, "??????????????????", "??????????????????", CreateSnVo.class, "??????????????????.xls", response);
    }
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public List<Sfc> createSnByRule(String number, String codeRuleType, String sfcQty, String operationOrder,String dispatchCode,int flag) throws CommonException {
        String site = UserUtils.getSite();
        CodeRule codeRule = codeRuleService.getExistCodeRule(new CodeRuleHandleBO(site, codeRuleType));

        List<Sfc> sfcs = new ArrayList<>();
        int num = Integer.parseInt(number);

        if (num > 100000) {
            throw new CommonException("??????????????????????????????10???!",CommonExceptionDefinition.SN_ENCODING_EXCEPTION);
        }
        if (flag == 0){//????????????????????????????????????????????????1???????????????
            //???????????????????????????????????? + ?????????????????????????????? ??????????????????????????????
            List<Sfc> sfcList = sfcService.list(new QueryWrapper<Sfc>().eq("dispatch_code", dispatchCode));
            BigDecimal printedQty = BigDecimal.ZERO;//???????????????
            for (Sfc sfc : sfcList) {
                printedQty = printedQty.add(sfc.getSfcQty());
            }
            QueryWrapper<Dispatch> dispatchQueryWrapper = new QueryWrapper<Dispatch>().eq("dispatch_code", dispatchCode);
            Dispatch dispatch = dispatchService.getOne(dispatchQueryWrapper);
            BigDecimal dispatchCodeTotalQty;//???????????????
            if (StringUtils.isNotBlank(dispatch.getDevice())){
                dispatchCodeTotalQty = dispatch.getDispatchQty();
            }else {
                dispatchCodeTotalQty = dispatch.getOperationOrderQty();
            }
            BigDecimal canPrintQty = dispatchCodeTotalQty.subtract(printedQty);//???????????????
            // ????????????????????????????????????---????????????*???????????????number*sfcQty???
            BigDecimal nowSfcQty=new BigDecimal(sfcQty).multiply(new BigDecimal(num));
            if ((printedQty.add(nowSfcQty)).compareTo(dispatchCodeTotalQty) == 1){
                throw new CommonException("????????????????????????????????????" + canPrintQty.toString() + ",???????????????",CommonExceptionDefinition.SN_ENCODING_EXCEPTION);
            }
            //????????????????????????????????????????????????
            dispatch.setCanPrintQty(canPrintQty.subtract(new BigDecimal(sfcQty)));
            dispatchService.updateById(dispatch);
        }

//        //??????????????????????????????????????????+?????????????????????????????????????????????????????????????????????
//        OpeartionItemVo opeartionItemVo=itemService.selectOpeartionOrderItem(operationOrder);
//        int operationOrderQty=Integer.parseInt(opeartionItemVo.getOpeartionOrderQty());//??????????????????
//        List<Sfc> sfcList=sfcService.selectSfc(operationOrder);
//        int qty=num*Integer.parseInt(sfcQty);//????????????????????????????????????+????????????????????????????????????
//        if(sfcList !=null && sfcList.size()>0){
//            for(Sfc sfc:sfcList){
//                qty+=sfc.getSfcQty().intValue();
//            }
//        }
//        if(qty>operationOrderQty){
//            throw new CommonException("????????????????????????????????????+?????????????????????????????????????????????????????????????????????",CommonExceptionDefinition.SN_ENCODING_EXCEPTION);
//        }

        //??????????????????
        String shopOrder = operationService.selectShopOrderByOperationOrder(operationOrder);
        ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(site, shopOrder));
        ItemHandleBO itemHandleBO = new ItemHandleBO(existShopOrder.getItemBo());

        HashMap<String, Object> params = new HashMap<>();
        CodeRuleFullVo fullVo = codeRuleService.getCodeRuleType(codeRuleType);
        fullVo.getCodeRuleItemVoList().forEach(ruleItem -> {
            if ("4".equals(ruleItem.getSectType())) {
                if(ruleItem.getSectParam().equals("{ITEM}")){
                    params.put(ruleItem.getSectParam(), itemHandleBO.getItem());
                }else if(ruleItem.getSectParam().equals("{OPERATIONO_ORDER}")){
                    // ??????operation_order????????????5???6?????????
                    String str=operationOrder.substring(4,6);
                    params.put(ruleItem.getSectParam(), str);
                }

            }
        });
        //??????num?????????
        List<String> sfcByRule = codeRuleService.generatorNextNumberList(codeRule.getBo(), num, params);
        //??????????????????
        Date date=new Date();
        for (int i = 1; i < num + 1; i++) {
            String sfc = sfcByRule.get(i - 1);
            //?????????sfc???
            Sfc sfcEntity=new Sfc();
            sfcEntity.setBo(new SfcHandleBo(site,sfc).getBo());
            sfcEntity.setSite(site);
            sfcEntity.setSfc(sfc);
            ShopOrder shopOrder1=shopOrderMapper.selectOne(new QueryWrapper<ShopOrder>().eq("SHOP_ORDER",existShopOrder.getShopOrder()));
            if(shopOrder1 !=null){
                sfcEntity.setShopOrderBo(shopOrder1.getBo());
            }
            WorkShop workShop=workShopMapper.selectOne(new QueryWrapper<WorkShop>().eq("WORK_SHOP",existShopOrder.getWorkShop()).eq("SITE",site));
            if(workShop != null) {
                sfcEntity.setWorkShopBo(workShop.getBo());
            }
            sfcEntity.setItemBo(existShopOrder.getItemBo());
            sfcEntity.setBomBo(existShopOrder.getBomBo());
            sfcEntity.setSfcRouterBo(existShopOrder.getRouterBo());
            sfcEntity.setSfcQty(new BigDecimal(sfcQty));
            if (flag == 1){
                sfcEntity.setState("?????????");
            }else {
                sfcEntity.setState("??????");
            }
            sfcEntity.setOperationOrder(operationOrder);
            if (flag ==1){
                List<String> routerOrderList = sfcServiceImpl.getRouterOrderList(existShopOrder.getRouterBo());
                String testOp = routerOrderList.get(routerOrderList.size() - 1);//????????????
                sfcEntity.setOperationBo(testOp);
                RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", existShopOrder.getRouterBo()));
                JSONObject jsonObject = JSONObject.parseObject(routerProcess.getProcessInfo());
                JSONArray nodeList = JSONArray.parseArray(jsonObject.getString("nodeList"));
                String idByOperation = sfcServiceImpl.getIdByOperation(nodeList, testOp);
                sfcEntity.setOpIds(idByOperation);
            }else {
                OpAndId nextOpAndId = sfcServiceImpl.getNextOpAndId(existShopOrder.getRouterBo(), "", true);//?????????????????????
                sfcEntity.setOperationBo(nextOpAndId.getOperationBo());
                sfcEntity.setOpIds(nextOpAndId.getId());
            }
            sfcEntity.setModifyDate(date);
            sfcEntity.setCreateDate(new Date());
            sfcEntity.setDispatchCode(dispatchCode);
            sfcs.add(sfcEntity);
        }
        try {
            sfcService.saveBatch(sfcs);
        } catch (Exception e) {
            throw new CommonException("?????????????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return sfcs;
    }

    @Override
    public IPage<Sfc> selectSfcByOperationOrder(SfcDto sfcDto) throws CommonException{
        IPage<Sfc> sfcList=sfcService.selectSfcPage(sfcDto);
        if(sfcList.getRecords() ==null && sfcList.getRecords().size()==0){
            throw new CommonException("?????????????????????????????????????????????",CommonExceptionDefinition.SN_ENCODING_EXCEPTION);
        }
        return sfcList;
    }
}
