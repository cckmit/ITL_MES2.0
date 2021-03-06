package com.itl.mes.core.provider.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.entity.IapSysRoleT;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.dto.OkStemDispatchDTO;
import com.itl.mes.core.api.dto.StemDispatchDTO;
import com.itl.mes.core.api.dto.StemReportWorkDto;
import com.itl.mes.core.api.dto.WorkStationDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.ManualInlayVo;
import com.itl.mes.core.api.vo.StemDispatchListVo;
import com.itl.mes.core.api.vo.StemDispatchVo;
import com.itl.mes.core.api.vo.StepNcVo;
import com.itl.mes.core.provider.mapper.DyYaoXianMapper;
import com.itl.mes.core.provider.mapper.OperationOrderMapper;
import com.itl.mes.core.provider.mapper.StemDispatchMapper;
import com.itl.mes.core.provider.util.KeyWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class StemDispatchServiceImpl extends ServiceImpl<StemDispatchMapper, StemDispatch> implements StemDispatchService {

    @Autowired
    private OperationOrderServiceImpl operationOrderService;

    @Autowired
    private OperationOrderMapper operationOrderMapper;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private StemDispatchService stemDispatchService;

    @Resource
    private UserUtil userUtil;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private StemDispatchMapper stemDispatchMapper;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private ReportWorkService reportWorkService;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private WorkStationService workStationService;

    @Autowired
    private DyYaoXianMapper dyYaoXianMapper;

    @Autowired
    private DispatchService dispatchService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public IPage<OperationOrder> queryNeedDispatchOrders(StemDispatchDTO stemDispatchDTO) {
        List<String> itemDescKeyWordList= KeyWordUtil.encapsulationItemDescKeyWord(stemDispatchDTO.getKeyWord());
        stemDispatchDTO.setItemDescKeyWordList(itemDescKeyWordList);
        IPage<OperationOrder> operationOrderIPage = stemDispatchMapper.queryNeedDispatchOrders(stemDispatchDTO.getPage(), stemDispatchDTO);
        return operationOrderIPage;
    }

    @Override
    public List<StemDispatchVo> assignment(StemDispatchDTO stemDispatchDTO) throws CommonException{
        BigDecimal qty = BigDecimal.ZERO;
        BigDecimal totalQty = BigDecimal.ZERO;
        List<OperationOrderAndQty> operationOrderAndQtys = new ArrayList<>();
        //????????????????????????????????????????????????
        for (OperationOrderAndQty operationOrderAndQty : stemDispatchDTO.getOperationOrderAndQtys()) {
            QueryWrapper<StemDispatch> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("work_step_code",stemDispatchDTO.getWorkStepCode());
            queryWrapper.eq("operation_order",operationOrderAndQty.getOperationOrder());
            BigDecimal operationOrderQty = operationOrderMapper.selectOne(new QueryWrapper<OperationOrder>().eq("operation_order", operationOrderAndQty.getOperationOrder())).getOperationOrderQty();
            List<StemDispatch> stemDispatchList = stemDispatchMapper.selectList(queryWrapper);
            BigDecimal dispatchQty = BigDecimal.ZERO;
            for (StemDispatch stemDispatch : stemDispatchList) {
                dispatchQty = dispatchQty.add(stemDispatch.getDispatchQty());
            }
            BigDecimal surplusQty = operationOrderQty.subtract(dispatchQty);//????????????

            if (surplusQty.compareTo(BigDecimal.ZERO) == 0){
                throw new CommonException(operationOrderAndQty.getOperationOrder() + "????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            if (StringUtils.isNotBlank(stemDispatchDTO.getAgainDispatchTotalQty())){//??????????????????????????????
                BigDecimal againDispatchTotalQty = new BigDecimal(stemDispatchDTO.getAgainDispatchTotalQty()).subtract(qty);
                if (qty.compareTo(new BigDecimal(stemDispatchDTO.getAgainDispatchTotalQty())) == 0){//????????????????????????????????????????????????????????????
                    stemDispatchList.remove(operationOrderAndQty);
                    continue;
                }
                if (againDispatchTotalQty.compareTo(surplusQty) != -1){
                    operationOrderAndQty.setQty(surplusQty);
                    qty = qty.add(surplusQty);
                    totalQty = totalQty.add(surplusQty);
                }else {
                    operationOrderAndQty.setQty(againDispatchTotalQty);
                    qty = qty.add(againDispatchTotalQty);
                    totalQty = totalQty.add(againDispatchTotalQty);
                }
            }else {
                operationOrderAndQty.setQty(surplusQty);
                totalQty = totalQty.add(surplusQty);
            }

            OperationOrderAndQty operationOrderAndQtyEntity = new OperationOrderAndQty();
            operationOrderAndQtyEntity.setOperationOrder(operationOrderAndQty.getOperationOrder());
            operationOrderAndQtyEntity.setQty(operationOrderAndQty.getQty());
            operationOrderAndQtys.add(operationOrderAndQtyEntity);
        }
        List<IapSysUserT> iapSysUserTS = stemDispatchMapper.selectUserT(stemDispatchDTO);
        List<StemDispatchVo> stemDispatchList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(iapSysUserTS)){
            BigDecimal ratioTotal= new BigDecimal("0.00");//????????????
            BigDecimal qtyTotal = new BigDecimal("0.00");//????????????(??????)

            for (IapSysUserT iapSysUserT : iapSysUserTS) {
                ratioTotal = ratioTotal.add(iapSysUserT.getDispatchRatio());
            }
            for (OperationOrderAndQty operationOrderAndQty : stemDispatchDTO.getOperationOrderAndQtys()) {
                qtyTotal = qtyTotal.add(operationOrderAndQty.getQty());
            }
            BigDecimal base = qtyTotal.divide(ratioTotal,2,BigDecimal.ROUND_HALF_UP);//?????????????????????????????????????????????????????????????????????=?????? * ??????????????????(??????????????????)
            for (IapSysUserT iapSysUserT : iapSysUserTS) {
                StemDispatchVo stemDispatchVo = new StemDispatchVo();
                stemDispatchVo.setUserName(iapSysUserT.getRealName());
                stemDispatchVo.setUserId(iapSysUserT.getUserName());
                BigDecimal assignmentQty = base.multiply(iapSysUserT.getDispatchRatio()).setScale(2,BigDecimal.ROUND_HALF_UP);//????????????
                stemDispatchVo.setAssignmentQty(assignmentQty);
                BigDecimal dispatchQty = assignmentQty.setScale(0, BigDecimal.ROUND_HALF_UP);//?????????????????????????????????????????????????????????????????????
                stemDispatchVo.setDispatchQty(dispatchQty);
                stemDispatchVo.setDispatchRatio(iapSysUserT.getDispatchRatio());
                stemDispatchVo.setTotalQty(totalQty);
                stemDispatchVo.setOperationOrderAndQtyList(operationOrderAndQtys);
                stemDispatchList.add(stemDispatchVo);
            }
        }
        return stemDispatchList;
    }
    @Override
    public void okAssignment(OkStemDispatchDTO okStemDispatchDTO) {
        int j = 0;
        String uuid = "";
        String stepDispatchCode = getStepDispatchCode();//????????????
        orderAndQtyFor:for (int i = 0; i < okStemDispatchDTO.getOperationOrderAndQtyList().size(); i++) {
            Set<String> userIds=new HashSet<>();
            String operationOrder = okStemDispatchDTO.getOperationOrderAndQtyList().get(i).getOperationOrder();//???????????????
            BigDecimal qty = okStemDispatchDTO.getOperationOrderAndQtyList().get(i).getQty();//??????????????????????????????
            BigDecimal qtyTotal = new BigDecimal("0.00");//???????????????
            if (StringUtils.isNotBlank(uuid)){
                StemDispatch one = stemDispatchService.getOne(new QueryWrapper<StemDispatch>().eq("id",uuid));
                qtyTotal = qtyTotal.add(one.getDispatchQty());
            }

            dispatchFor:for (; j < okStemDispatchDTO.getStemDispatchVo().size(); j++) {
                StemDispatch stemDispatch = new StemDispatch();
                stemDispatch.setId(UUID.uuid32());
                stemDispatch.setStepDispatchCode(stepDispatchCode);
                stemDispatch.setWorkStepCode(okStemDispatchDTO.getWorkStationDTO().getWorkStepCode());
                stemDispatch.setWorkStepName(okStemDispatchDTO.getWorkStationDTO().getWorkStepName());
                stemDispatch.setUserName(okStemDispatchDTO.getStemDispatchVo().get(j).getUserName());
                stemDispatch.setUserId(okStemDispatchDTO.getStemDispatchVo().get(j).getUserId());
                String insideNo = stemDispatchMapper.selectUserTByUserId(okStemDispatchDTO.getStemDispatchVo().get(j).getUserId());
                stemDispatch.setInsideNo(insideNo);
                String shopOrder = operationOrderMapper.selectOne(new QueryWrapper<OperationOrder>().eq("operation_order", operationOrder)).getShopOrder();

                stemDispatch.setOperationOrder(operationOrder);
                stemDispatch.setShopOrderBo("SO:dongyin," + shopOrder);

                stemDispatch.setItem(okStemDispatchDTO.getStemDispatchVo().get(j).getItem());
                stemDispatch.setItemName(okStemDispatchDTO.getStemDispatchVo().get(j).getItemName());
                stemDispatch.setItemDesc(okStemDispatchDTO.getStemDispatchVo().get(j).getItemDesc());
                stemDispatch.setDispatchQty(okStemDispatchDTO.getStemDispatchVo().get(j).getDispatchQty());
                stemDispatch.setCreateUser(userUtil.getUser().getUserName());
                stemDispatch.setCreateDate(new Date());
                stemDispatch.setRoleId(okStemDispatchDTO.getWorkStationDTO().getRoleId());
                qtyTotal = qtyTotal.add(okStemDispatchDTO.getStemDispatchVo().get(j).getDispatchQty());
                if (qtyTotal.compareTo(qty) == 1){//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    BigDecimal surplus = qty.subtract(qtyTotal.subtract(okStemDispatchDTO.getStemDispatchVo().get(j).getDispatchQty()));//??????????????????????????????
                    stemDispatch.setDispatchQty(surplus);
                    userIds.add(stemDispatch.getUserId());
                    stemDispatchService.save(stemDispatch);

                    int num = i + 1;

                    BigDecimal nextQty = qtyTotal.subtract(qty);//?????????????????????????????????
                    BigDecimal nextTotal = okStemDispatchDTO.getOperationOrderAndQtyList().get(num).getQty();//?????????????????????????????????


                    while (nextQty.compareTo(nextTotal) == 1){//???????????????????????????????????????????????????????????????????????????
                        StemDispatch stemDispatchs = new StemDispatch();
                        stemDispatchs.setId(UUID.uuid32());
                        stemDispatchs.setStepDispatchCode(stepDispatchCode);
                        stemDispatchs.setWorkStepCode(okStemDispatchDTO.getWorkStationDTO().getWorkStepCode());
                        stemDispatchs.setWorkStepName(okStemDispatchDTO.getWorkStationDTO().getWorkStepName());
                        stemDispatchs.setUserName(okStemDispatchDTO.getStemDispatchVo().get(j).getUserName());
                        stemDispatchs.setUserId(okStemDispatchDTO.getStemDispatchVo().get(j).getUserId());
                        stemDispatchs.setItem(okStemDispatchDTO.getStemDispatchVo().get(j).getItem());
                        stemDispatchs.setItemName(okStemDispatchDTO.getStemDispatchVo().get(j).getItemName());
                        stemDispatchs.setItemDesc(okStemDispatchDTO.getStemDispatchVo().get(j).getItemDesc());
                        stemDispatchs.setCreateUser(userUtil.getUser().getUserName());
                        stemDispatchs.setCreateDate(new Date());

                        String shopOrderO = operationOrderMapper.selectOne(new QueryWrapper<OperationOrder>().eq("operation_order", okStemDispatchDTO.getOperationOrderAndQtyList().get(num).getOperationOrder())).getShopOrder();
                        stemDispatchs.setShopOrderBo("SO:dongyin," + shopOrderO);

                        stemDispatchs.setOperationOrder(okStemDispatchDTO.getOperationOrderAndQtyList().get(num).getOperationOrder());
                        stemDispatchs.setDispatchQty(nextTotal);//???????????????????????????????????????????????????
                        stemDispatchs.setRoleId(okStemDispatchDTO.getWorkStationDTO().getRoleId());
                        userIds.add(stemDispatchs.getUserId());
                        stemDispatchService.save(stemDispatchs);
                        nextQty = nextQty.subtract(nextTotal);
                        nextTotal = okStemDispatchDTO.getOperationOrderAndQtyList().get(num + 1).getQty();
                        i++;
                        num++;
                    }
                    StemDispatch stemDispatch_ = new StemDispatch();
                    stemDispatch_.setId(UUID.uuid32());
                    stemDispatch_.setStepDispatchCode(stepDispatchCode);
                    stemDispatch_.setWorkStepCode(okStemDispatchDTO.getWorkStationDTO().getWorkStepCode());
                    stemDispatch_.setWorkStepName(okStemDispatchDTO.getWorkStationDTO().getWorkStepName());
                    stemDispatch_.setUserName(okStemDispatchDTO.getStemDispatchVo().get(j).getUserName());
                    stemDispatch_.setUserId(okStemDispatchDTO.getStemDispatchVo().get(j).getUserId());

                    String shopOrderT = operationOrderMapper.selectOne(new QueryWrapper<OperationOrder>().eq("operation_order", okStemDispatchDTO.getOperationOrderAndQtyList().get(num).getOperationOrder())).getShopOrder();

                    stemDispatch_.setOperationOrder(okStemDispatchDTO.getOperationOrderAndQtyList().get(num).getOperationOrder());
                    stemDispatch_.setShopOrderBo("SO:dongyin," + shopOrderT);

                    stemDispatch_.setItem(okStemDispatchDTO.getStemDispatchVo().get(j).getItem());
                    stemDispatch_.setItemName(okStemDispatchDTO.getStemDispatchVo().get(j).getItemName());
                    stemDispatch_.setItemDesc(okStemDispatchDTO.getStemDispatchVo().get(j).getItemDesc());
                    stemDispatch_.setDispatchQty(nextQty);//?????????????????????????????????
                    stemDispatch_.setCreateUser(userUtil.getUser().getUserName());
                    stemDispatch_.setCreateDate(new Date());
                    stemDispatch_.setRoleId(okStemDispatchDTO.getWorkStationDTO().getRoleId());
                    if (nextQty.compareTo(nextTotal) == 0){
                        i++;
                        userIds.add(stemDispatch_.getUserId());
                        stemDispatchService.save(stemDispatch_);//?????????????????????????????????????????????
                        j++;
                        uuid = "";
                        break dispatchFor;
                    }
                    userIds.add(stemDispatch_.getUserId());
                    stemDispatchService.save(stemDispatch_);//?????????????????????????????????????????????
                    j++;
                    uuid = stemDispatch_.getId();
                    break dispatchFor;
                }else if (qtyTotal.compareTo(qty) == 0){//????????????????????????????????????????????????????????????????????????????????????????????????
                    userIds.add(stemDispatch.getUserId());
                    stemDispatchService.save(stemDispatch);
                    j++;
                    uuid = "";
                    break dispatchFor;
                }
                uuid = "";
                userIds.add(stemDispatch.getUserId());
                stemDispatchService.save(stemDispatch);
            }

            // ?????????????????????????????????????????????????????????0????????????
            if(CollectionUtil.isNotEmpty(okStemDispatchDTO.getStemDispatchVo())){
                okAssignmentWithOutInList(stepDispatchCode,userIds,okStemDispatchDTO.getWorkStationDTO(),operationOrder,okStemDispatchDTO.getStemDispatchVo().get(0));
            }
            if (j==okStemDispatchDTO.getStemDispatchVo().size()){
                break orderAndQtyFor;
            }

        }
        checkIsFinishedByOperationOrder(okStemDispatchDTO.getOperationOrderAndQtyList(),okStemDispatchDTO.getWorkStationDTO().getWorkStepCode());
    }

    /**
     * ??????????????????????????????????????????
     * @param stepCode ????????????
     */
    public void checkIsFinishedByOperationOrder(List<OperationOrderAndQty> operationOrderAndQtyList,String stepCode){
        String operationBo = workStationService.getOne(new QueryWrapper<WorkStation>().eq("work_step_code", stepCode)).getWorkingProcess();
        List<String> needDispatchStep = stemDispatchMapper.listAllNeedDispatchOperationOrder(operationBo);
        List<String> unNeedDispatchStep = stemDispatchMapper.listUnNeedDispatchStep();
        List<String> stepBoList;//????????????????????????????????????
        if (CollectionUtil.isNotEmpty(needDispatchStep)){
            if(CollectionUtil.isNotEmpty(unNeedDispatchStep)){
                stepBoList = needDispatchStep.stream().filter(a ->
                        !unNeedDispatchStep.stream().collect(Collectors.toList()).contains(a)
                ).collect(Collectors.toList());
            }else {
                stepBoList = needDispatchStep;
            }
        }else {
            stepBoList = needDispatchStep;
        }
        for (OperationOrderAndQty operationOrderAndQty : operationOrderAndQtyList) {
            boolean flag = true;
            String operationOrder = operationOrderAndQty.getOperationOrder();
            //?????????????????? ???????????????????????????????????????????????????????????????????????????????????????
            BigDecimal operationOrderQty = operationOrderMapper.selectOne(new QueryWrapper<OperationOrder>().eq("operation_order",operationOrder)).getOperationOrderQty();
            for (String stepBo : stepBoList) {
                String step = workStationService.getById(stepBo).getWorkStepCode();
                String qty = stemDispatchMapper.sumQtyByOperationOrderAndStep(operationOrder, step);
                if (new BigDecimal(qty).compareTo(operationOrderQty) != 0){
                    flag = false;
                    break;
                }
            }
            if (flag){//??????????????????
                QueryWrapper<Dispatch> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("operation_order",operationOrder);
                queryWrapper.eq("operation_bo",operationBo);
                queryWrapper.eq("is_need_dispatch","0");

                Dispatch dispatch = new Dispatch();
                dispatch.setIsDispatchFinished(BigDecimal.ONE.toString());
                dispatchService.update(dispatch,queryWrapper);
            }
        }
    }

    /**
     * ??????????????????
     * @return
     */
    public String getStepDispatchCode(){
        String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replaceAll("-", "");
        String cT = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String startTime = cT + " 00:00:00";
        String endTime = cT + " 23:59:59";
        QueryWrapper<StemDispatch> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("create_date",startTime);
        queryWrapper.le("create_date",endTime);
        queryWrapper.orderByDesc("create_date");
        List<StemDispatch> list = stemDispatchService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            return "DIS" + currentTime + "-1";
        }
        String lastCode = "";
        for (String code : list.get(0).getStepDispatchCode().split("-")) {
            lastCode = code;
        }
        int newCode = Integer.parseInt(lastCode) + 1;
        return "DIS" + currentTime + "-" + newCode;
    }

    @Override
    public void exportStemDispatch(StemDispatchDTO stemDispatchDTO) throws CommonException, IOException {
        if (StringUtils.isNotBlank(stemDispatchDTO.getWorkStepCode()) && "XQ-04-01".equals(stemDispatchDTO.getWorkStepCode())){
            exportStemDispatchYx(stemDispatchDTO);
        }else {
            List<StemDispatchListVo> stemDispatchList = stemDispatchMapper.queryStemDispatchList(new Page(0, Integer.MAX_VALUE), stemDispatchDTO).getRecords();
            // ?????????????????????????????????excel???sheet?????????????????????
            ExportParams ReportWorkExport = new ExportParams();
            // ??????sheet?????????
            ReportWorkExport.setSheetName("???????????????");
            // ??????sheet1?????????map
            Map<String, Object> exportMap = new HashMap<>();
            // title????????????ExportParams????????????????????????ExportParams????????????sheetName
            exportMap.put("title", ReportWorkExport);
            // ?????????????????????????????????
            if("XQ-04-02".equals(stemDispatchDTO.getWorkStepCode())){
                List<ManualInlayVo> manualInlayVos = stemDispatchMapper.queryManualInlay(stemDispatchDTO);
                exportMap.put("entity", ManualInlayVo.class);
                // sheet?????????????????????
                exportMap.put("data", manualInlayVos);

            }else{
                exportMap.put("entity", StemDispatchListVo.class);
                // sheet?????????????????????
                exportMap.put("data", stemDispatchList);
            }
            // ???sheet1???sheet2???sheet3?????????map????????????
            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(exportMap);
            // ????????????
            ExcelUtils.exportExcel(sheetsList,"????????????",response);
        }
    }

    @Override
    public List<StemDispatchListVo> selectStemReportWorkList(List<StemDispatchDTO> stemDispatchDTOS){
        List<StemDispatchListVo> stemDispatchListVos = Lists.newArrayList();
        for (int i = 0; i < stemDispatchDTOS.size(); i++) {
            StemDispatchListVo stemDispatchVo = stemDispatchMapper.selectStemReportWork(stemDispatchDTOS.get(i).getId());
            QueryWrapper<ReportWork> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("operation_order",stemDispatchDTOS.get(i).getOperationOrder());
            queryWrapper.eq("user_bo",stemDispatchDTOS.get(i).getUserId());
            queryWrapper.eq("step_dispatch_code",stemDispatchDTOS.get(i).getStepDispatchCode());
            List<ReportWork> reportWorkList = reportWorkService.list(queryWrapper);
            BigDecimal reportWorkQty = BigDecimal.ZERO;//????????????
            if (!CollectionUtils.isEmpty(reportWorkList)){
                for (ReportWork reportWork : reportWorkList) {
                    reportWorkQty = reportWorkQty.add(reportWork.getQty());
                }
            }
            BigDecimal canReportWorkQty = stemDispatchVo.getDispatchQty().subtract(reportWorkQty);//???????????????
            if (canReportWorkQty.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }
            stemDispatchVo.setCanReportWorkQty(canReportWorkQty);
            String roleId = stemDispatchMapper.getRoleByUserId(stemDispatchDTOS.get(i).getUserId()).get(0);
            stemDispatchVo.setRoleId(roleId);
            stemDispatchListVos.add(stemDispatchVo);
        }
        return stemDispatchListVos;
    }

    @Override
    public void okStemReportWork(List<StemReportWorkDto> stemReportWorkDtoList) throws CommonException {
        for (StemReportWorkDto stemReportWorkDto : stemReportWorkDtoList) {
            ReportWork reportWork = new ReportWork();
            BeanUtils.copyProperties(stemReportWorkDto,reportWork);
            reportWork.setBo(UUID.uuid32());
            reportWork.setTime(new Date());
            reportWork.setState("0");
            reportWork.setCreateTime(new Date());
            reportWork.setOtherUser(stemReportWorkDto.getOtherUser());
            if (stemReportWorkDto.getQty().compareTo(BigDecimal.ZERO) != 0){
                List<ReportWork> reportWorkList = reportWorkService.list(new QueryWrapper<ReportWork>().
                        eq("step_dispatch_code", stemReportWorkDto.getStepDispatchCode()).
                        eq("user_bo", stemReportWorkDto.getUserBo()).
                        eq("operation_order",stemReportWorkDto.getOperationOrder()));
                BigDecimal qty = BigDecimal.ZERO;//????????????
                for (ReportWork work : reportWorkList) {
                    qty = qty.add(work.getQty());
                }
                QueryWrapper<StemDispatch> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("step_dispatch_code",stemReportWorkDto.getStepDispatchCode());
                queryWrapper.eq("user_id",stemReportWorkDto.getUserBo());
                queryWrapper.eq("operation_order",stemReportWorkDto.getOperationOrder());
                StemDispatch stemDispatch = stemDispatchMapper.selectOne(queryWrapper);
                BigDecimal add = qty.add(stemReportWorkDto.getQty());
                if (add.compareTo(stemDispatch.getDispatchQty()) == 0){
                    stemDispatch.setWorkReportFlag("1");
                    stemDispatchMapper.updateById(stemDispatch);
                }else if (add.compareTo(stemDispatch.getDispatchQty()) == 1){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new CommonException("????????????",504);
                }
                reportWorkService.save(reportWork);
            }
        }
    }

    @Override
    public StepNcVo getAllInfoByShopOrderBo(String shopOrderBo) throws CommonException{
        StepNcVo.Item item = stemDispatchMapper.getItemInfoByShopOrder(shopOrderBo);
        String routerBo = shopOrderService.getOne(new QueryWrapper<ShopOrder>().eq("bo", shopOrderBo)).getRouterBo();//????????????bo
        if (StringUtils.isBlank(routerBo)){
            throw new CommonException( "?????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        JSONArray nodeList = sfcServiceImpl.getObjects(routerBo);
        List<String> operationList = Lists.newArrayList();
        if (nodeList.size() > 0){
            for (int i = 0; i < nodeList.size(); i++) {
                String operation = JSON.parseObject(nodeList.get(i).toString()).getString("operation");
                if (StringUtils.isNotBlank(operation)){
                    operationList.add(operation);
                }
            }
        }
        List<String> orderOpList = operationList.stream().distinct().collect(Collectors.toList());//???????????????????????????
        List<StepNcVo.WorkStep> workStepList = Lists.newArrayList();//????????????

        for (String op : orderOpList) {
            List<WorkStation> workStationList = workStationService.list(new QueryWrapper<WorkStation>().eq("working_process",op));
            for (WorkStation workStation : workStationList) {
                StepNcVo.WorkStep workStep = new StepNcVo.WorkStep();
                workStep.setWorkStepBo(workStation.getBo());
                workStep.setWorkStepCode(workStation.getWorkStepCode());
                workStep.setWorkStepName(workStation.getWorkStepName());
                workStepList.add(workStep);
            }
        }

        List<StemDispatch> stemDispatchList = stemDispatchMapper.selectList(new QueryWrapper<StemDispatch>().eq("shop_order_bo", shopOrderBo));
        List<StepNcVo.PersonLiable> personLiableList = Lists.newArrayList();//???????????????

        for (StemDispatch stemDispatch : stemDispatchList) {
            StepNcVo.PersonLiable personLiable = new StepNcVo.PersonLiable();
            personLiable.setUserId(stemDispatch.getUserId());
            personLiable.setUserName(stemDispatch.getUserName());
            personLiableList.add(personLiable);
        }
        List<StepNcVo.PersonLiable> personLiableListD = personLiableList.stream().distinct().collect(Collectors.toList());
        personLiableListD.forEach(personLiable -> {
            personLiable.setInsideNo(stemDispatchMapper.getInsideNoByUserName(personLiable.getUserId()));
        });
        StepNcVo stepNcVo = new StepNcVo();
        stepNcVo.setItem(item);
        stepNcVo.setWorkStepList(workStepList);
        stepNcVo.setPersonLiableList(personLiableListD);
        return stepNcVo;
    }

    /**
     * ?????????????????????
     * @param stemDispatchDTO
     */
    public void exportStemDispatchYx(StemDispatchDTO stemDispatchDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        String title = "???????????????" + ".xls";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(title, "UTF-8"));
        OutputStream out = response.getOutputStream();
        HSSFWorkbook workbook = new HSSFWorkbook();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,+1);
        try {
            Map<String, Object> yaoXianParametersMap = new HashMap<>();//????????????????????????map????????????????????????(????????????????????????)?????????????????????
            List<DyYaoXian> dyYaoXianList = dyYaoXianMapper.selectList(null);
            for (DyYaoXian dyYaoXian : dyYaoXianList) {
                yaoXianParametersMap.put(dyYaoXian.getItem(),dyYaoXian);
            }
            List<IapSysRoleT> roleList = stemDispatchMapper.getYxGroupId();
            for (IapSysRoleT iapSysRoleT : roleList) {
                //===================================????????????start===================================
                Map<String, Object> qtyByItemAndUserMap = new HashMap<>();//?????????+??????????????????????????????hashmap??????
                if (StringUtils.isBlank(stemDispatchDTO.getReleaseDateStart()) || StringUtils.isBlank(stemDispatchDTO.getReleaseDateEnd())){//??????????????????????????????????????????????????????
                    String currentDate = simpleDateFormat.format(new Date());
                    stemDispatchDTO.setReleaseDateStart(currentDate + " 00:00:00");
                    stemDispatchDTO.setReleaseDateEnd(currentDate + " 23:59:59");
                }
                stemDispatchDTO.setRoleId(iapSysRoleT.getId());
                List<StemDispatch> stemDispatchList = stemDispatchMapper.getSumQtyByYxUserAndItem(stemDispatchDTO);

                for (StemDispatch stemDispatch : stemDispatchList) {
                    //key???item-userId(??????????????????),value???????????????
                    qtyByItemAndUserMap.put(stemDispatch.getItem()+"-"+stemDispatch.getUserId(),stemDispatch);
                }
                //===================================????????????end===================================

                //===================================????????????start===================================
                HSSFSheet sheet = workbook.createSheet(iapSysRoleT.getRoleName());
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                HSSFFont font = workbook.createFont();
                font.setBold(true);
                cellStyle.setFont(font);
                HSSFRow row_0 = sheet.createRow(0);
                row_0.createCell(0).setCellValue("?????????"+ simpleDateFormat.format(calendar.getTime()));
                for (Cell cell : row_0) {
                    cell.setCellStyle(cellStyle);
                }
                //????????????
                HSSFRow row0 = sheet.createRow(2);
                row0.createCell(0).setCellValue("??????");
                row0.createCell(1).setCellValue("??????");
                row0.createCell(2).setCellValue("??????");
                row0.createCell(3).setCellValue("");
                row0.createCell(4).setCellValue("??????");
                row0.createCell(5).setCellValue("??????");
                row0.createCell(6).setCellValue("");
                row0.createCell(7).setCellValue("");
                row0.createCell(8).setCellValue("");
                row0.createCell(9).setCellValue("");
                row0.createCell(10).setCellValue("?????????");
                row0.createCell(11).setCellValue("????????????");

                for (Cell cell : row0) {
                    cell.setCellStyle(cellStyle);
                }
                sheet.addMergedRegion(new CellRangeAddress(2,2,5,9));//???????????????????????????
                sheet.addMergedRegion(new CellRangeAddress(0,1,0,11));
                sheet.setColumnWidth(0,3000);
                sheet.setColumnWidth(1,7000);
                //????????????
                List<String> userList = stemDispatchMapper.selectUserByCondition(stemDispatchDTO);
                List<String> itemList = stemDispatchMapper.selectItemByCondition(stemDispatchDTO);
                int index = 2;
                for (String userId : userList) {
                    String insideNo = stemDispatchMapper.selectUserTByUserId(userId);
                    for (String item : itemList) {
                        StemDispatch stemDispatch = (StemDispatch) qtyByItemAndUserMap.get(item+"-"+userId);
                        if (ObjectUtil.isEmpty(stemDispatch))stemDispatch = new StemDispatch();//stemDispatch??????????????????
                        DyYaoXian dyYaoXian = (DyYaoXian)yaoXianParametersMap.get(item);//dyYaoXian?????????????????????
                        index++;
                        HSSFRow row = sheet.createRow(index);
                        String itemDesc = stemDispatch.getItemDesc();
                        String itemCode = stemDispatch.getItem();
                        if (StringUtils.isNotBlank(itemDesc)){
                            if (itemDesc.length() > 20)itemDesc = itemDesc.substring(0,20);
                        }
                        row.createCell(0).setCellValue(itemCode);
                        row.createCell(1).setCellValue(itemDesc);
                        String qty = "";
                        if (ObjectUtil.isNotEmpty(stemDispatch.getDispatchQty())){
                            qty = stemDispatch.getDispatchQty().toString();
                        }
                        row.createCell(2).setCellValue(qty);
                        row.createCell(3).setCellValue("???");
                        row.createCell(4).setCellValue(dyYaoXian.getMainDiameter());
                        row.createCell(5).setCellValue(dyYaoXian.getMainCircle1());
                        row.createCell(6).setCellValue(dyYaoXian.getMainCircle2());
                        row.createCell(7).setCellValue(dyYaoXian.getMainCircle3());
                        row.createCell(8).setCellValue(dyYaoXian.getMainCircle4());
                        row.createCell(9).setCellValue(dyYaoXian.getMainCircle5());
                        row.createCell(10).setCellValue(stemDispatch.getUserName());
                        row.createCell(11).setCellValue(insideNo);

                        for (Cell cell : row) {
                            cell.setCellStyle(cellStyle);
                        }
                        index++;
                        HSSFRow row_ = sheet.createRow(index);
                        row_.createCell(0).setCellValue("");
                        row_.createCell(1).setCellValue("");
                        row_.createCell(2).setCellValue("");
                        row_.createCell(3).setCellValue("???");
                        row_.createCell(4).setCellValue(dyYaoXian.getAuxiliaryDiameter());
                        row_.createCell(5).setCellValue(dyYaoXian.getAuxiliaryCircle1());
                        row_.createCell(6).setCellValue(dyYaoXian.getAuxiliaryCircle2());
                        row_.createCell(7).setCellValue(dyYaoXian.getAuxiliaryCircle3());
                        row_.createCell(8).setCellValue(dyYaoXian.getAuxiliaryCircle4());
                        row_.createCell(9).setCellValue(dyYaoXian.getAuxiliaryCircle5());
                        row_.createCell(10).setCellValue("");
                        row_.createCell(11).setCellValue("");

                        for (Cell cell : row_) {
                            cell.setCellStyle(cellStyle);
                        }
                    }
                    index++;
                    sheet.createRow(index);//???????????????
                }
            }
            workbook.write(out);
            //===================================????????????end===================================
        } catch (Exception e){
           log.error("???????????????"+e.toString());
        }finally {
            try {
                if (out != null) {
                    out.flush();
                    response.flushBuffer();
                    out.close();
                }
            } finally {
                workbook.close();
            }
        }
    }

    /**
     * ?????????????????????
     * @param dispatchOrders
     */
    @Override
    @Transactional(rollbackFor = {CommonException.class,Exception.class})
    public void cancelDispatchOrder(List<String> dispatchOrders) throws CommonException{
        for (String dispatchOrder : dispatchOrders) {
            List<ReportWork> reportWorks = reportWorkService.list(new QueryWrapper<ReportWork>().eq("step_dispatch_code", dispatchOrder));
            if (CollectionUtil.isNotEmpty(reportWorks)){
                throw new CommonException("????????????"+dispatchOrder+"??????????????????????????????????????????",504);
            }
            List<String> operationOrders = stemDispatchMapper.getOperationOrderByDispatchCode(dispatchOrder);
            String workStepCode = stemDispatchMapper.selectList(new QueryWrapper<StemDispatch>().eq("step_dispatch_code", dispatchOrder)).get(0).getWorkStepCode();
            String operationBo = workStationService.getOne(new QueryWrapper<WorkStation>().eq("work_step_code", workStepCode)).getWorkingProcess();

            for (String operationOrder : operationOrders) {
                Dispatch dispatch = new Dispatch();
                dispatch.setIsDispatchFinished(BigDecimal.ZERO.toString());

                QueryWrapper<Dispatch> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("operation_order",operationOrder);
                queryWrapper.eq("operation_bo",operationBo);
                dispatchService.update(dispatch,queryWrapper);
            }
            stemDispatchMapper.delete(new QueryWrapper<StemDispatch>().eq("step_dispatch_code",dispatchOrder));
        }
    }

    /**
     * ?????????????????????????????????0??????????????????
     * @param stepDispatchCode ????????????
     * @param userIds ????????????Id ??????
     * @param workStationDTO
     * @param operationOrder ???????????????
     * @param stemDispatchVo
     */
    public void okAssignmentWithOutInList(String stepDispatchCode,Set<String> userIds,WorkStationDTO workStationDTO,String operationOrder,StemDispatchVo stemDispatchVo){
        //??????????????????????????????????????????????????????
        List<IapSysUserT> iapSysUserTList=stemDispatchMapper.selectUserWithOutInList(userIds,workStationDTO.getRoleId());
        iapSysUserTList.forEach(iapSysUserT->{
            StemDispatch stemDispatch = new StemDispatch();
            stemDispatch.setId(UUID.uuid32());
            stemDispatch.setStepDispatchCode(stepDispatchCode);
            stemDispatch.setWorkStepCode(workStationDTO.getWorkStepCode());
            stemDispatch.setWorkStepName(workStationDTO.getWorkStepName());
            stemDispatch.setUserName(iapSysUserT.getRealName());
            stemDispatch.setUserId(iapSysUserT.getUserName());
            String insideNo = stemDispatchMapper.selectUserTByUserId(iapSysUserT.getUserName());
            stemDispatch.setInsideNo(insideNo);
            String shopOrder = operationOrderMapper.selectOne(new QueryWrapper<OperationOrder>().eq("operation_order", operationOrder)).getShopOrder();

            stemDispatch.setOperationOrder(operationOrder);
            stemDispatch.setShopOrderBo("SO:dongyin," + shopOrder);

            stemDispatch.setItem(stemDispatchVo.getItem());
            stemDispatch.setItemName(stemDispatchVo.getItemName());
            stemDispatch.setItemDesc(stemDispatchVo.getItemDesc());
            //????????????????????? 0
            stemDispatch.setDispatchQty(BigDecimal.ZERO);
            stemDispatch.setCreateUser(userUtil.getUser().getUserName());
            stemDispatch.setCreateDate(new Date());
            stemDispatch.setRoleId(workStationDTO.getRoleId());
            stemDispatchService.save(stemDispatch);
        });

    }
}
