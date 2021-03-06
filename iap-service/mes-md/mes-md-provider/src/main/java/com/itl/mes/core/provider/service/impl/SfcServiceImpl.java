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
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.iap.notice.client.NoticeService;
import com.itl.mes.core.api.bo.CodeRuleHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.SfcHandleBo;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.*;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CodeRuleFullVo;
import com.itl.mes.core.api.vo.ReworkVo;
import com.itl.mes.core.provider.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SfcServiceImpl extends ServiceImpl<SfcMapper, Sfc> implements SfcService {

    @Autowired
    private SfcMapper sfcMapper;

    @Autowired
    private CodeRuleService codeRuleService;

    @Autowired
    private FeedInService feedInService;

    @Autowired
    private SfcNcLogMapper sfcNcLogMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StationService stationService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SfcWiplogMapper sfcWiplogMapper;

    @Autowired
    private SfcRepairMapper sfcRepairMapper;

    @Autowired
    private RouterProcessService routerProcessService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private ReportWorkMapper reportWorkMapper;

    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private SkipStationMapper skipStationMapper;

    @Autowired
    private SfcDeviceMapper sfcDeviceMapper;

    @Autowired
    private QualityCheckListMapper qualityCheckListMapper;

    @Autowired
    private MyDeviceMapper myDeviceMapper;

    @Autowired
    private EnterMapper enterMapper;

    @Autowired
    private RouterProcessMapper routerProcessMapper;

    @Autowired
    private OperationOrderService operationOrderService;

    @Autowired
    private ReportWorkService reportWorkService;

    @Autowired
    private WorkShopMapper workShopMapper;

    @Autowired
    private ProductionExecuteServiceImpl productionExecuteServiceImpl;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SfcStateTrackMapper sfcStateTrackMapper;

    @Autowired
    private RouterProcessTableMapper routerProcessTableMapper;

    @Autowired
    private SfcSkipBeforeOperationMapper sfcSkipBeforeOperationMapper;

    @Override
    public IPage<Sfc> selectSfcPage(SfcDto sfcDto) throws CommonException {
        if((sfcDto.getScDate() !=null && sfcDto.getScDate() !="")&&(sfcDto.getJsDate() !=null && sfcDto.getJsDate() !="")){
            return sfcMapper.selectSfcPage(sfcDto.getPage(),sfcDto.getOperationOrder(),sfcDto.getScDate(),sfcDto.getJsDate(),sfcDto.getDispatchCode());
        }else if(sfcDto.getScDate() !=null && sfcDto.getScDate() !=""){
            sfcDto.setScDate("%"+sfcDto.getScDate()+"%");
        }

        return sfcMapper.selectSfcPage(sfcDto.getPage(),sfcDto.getOperationOrder(),sfcDto.getScDate(),sfcDto.getJsDate(),sfcDto.getDispatchCode());
    }

    @Override
    public List<Sfc> selectSfc(String operationOrder) throws CommonException {
        return sfcMapper.selectSfc(operationOrder);
    }

    @Override
    public Sfc selectBySfc(String sfc) throws CommonException {
        Sfc sfcEntity=sfcMapper.selectBySfc(sfc);
        if(sfcEntity == null){
            throw new CommonException("????????????sfc??????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        sfcEntity.setModifyDate(null);
        QueryWrapper<SfcNcLog> ncLogQueryWrapper = new QueryWrapper<>();
        ncLogQueryWrapper.eq("sfc",sfc);
        ncLogQueryWrapper.eq("IS_REPAIRED","0");
        ncLogQueryWrapper.orderByDesc("RECORD_TIME");
        List<SfcNcLog> sfcNcLogs = sfcNcLogMapper.selectList(ncLogQueryWrapper);
        if (CollectionUtil.isNotEmpty(sfcNcLogs)){
            sfcEntity.setRepairOpIds(sfcNcLogs.get(0).getOpIds());
        }

        return sfcEntity;
    }

    @Override
    public Sfc sfcSplit(SfcSplitDto sfcSplitDto) throws CommonException {


        String site = UserUtils.getSite();
        Sfc sfc=new Sfc();
        BeanUtils.copyProperties(sfcSplitDto,sfc);
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfcSplitDto.getSfc()));
        sfc.setOpIds(sfcObj.getOpIds());
        //??????Sfc?????????????????????me_sfc_nc_log???????????????sfc????????????????????????????????????????????????????????????
        /*List<SfcNcLog> sfcNcLogs=sfcNcLogMapper.selectList(new QueryWrapper<SfcNcLog>().eq("sfc",sfc.getSfc()));
        if(sfcNcLogs !=null && sfcNcLogs.size()>0){
            int logQty=0;
            for(SfcNcLog sfcNcLog:sfcNcLogs){
                logQty+=sfcNcLog.getNcQty().intValue();
            }
            if(sfc.getNcQty().intValue() != logQty){
                throw new CommonException("????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }*/


        BigDecimal ncQty=sfc.getNcQty();
        Sfc sfcSplit=new Sfc();
        //?????????sfc???????????????
        BeanUtils.copyProperties(sfc,sfcSplit);
        String codeRuleType=sfcSplitDto.getCodeRuleType();
        CodeRule codeRule = codeRuleService.getExistCodeRule(new CodeRuleHandleBO(site,codeRuleType));
        HashMap<String, Object> params = new HashMap<>();

        String operationOrder=sfcSplitDto.getOperationOrder();
        //??????????????????
        String shopOrder = operationService.selectShopOrderByOperationOrder(operationOrder);
        ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(site, shopOrder));
        ItemHandleBO itemHandleBO = new ItemHandleBO(existShopOrder.getItemBo());
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
        //????????????sfc
        List<String> sfcsByRule = codeRuleService.generatorNextNumberList(codeRule.getBo(), 1, params);
        sfcSplit.setSfc(sfcsByRule.get(0));
        sfcSplit.setBo(new SfcHandleBo(site,sfcsByRule.get(0)).getBo());//??????BO

        sfcSplit.setStationBo(null);
        sfcSplit.setDeviceBo(null);
        sfcSplit.setUserBo(null);
        sfcSplit.setShitBo(null);
        sfcSplit.setTeamBo(null);
        //??????sfc????????????????????????sfc?????????
        sfcSplit.setSfcQty(ncQty);
        sfcSplit.setInputQty(BigDecimal.ZERO);
        sfcSplit.setStopQty(BigDecimal.ZERO);
        sfcSplit.setDoneQty(BigDecimal.ZERO);
        sfcSplit.setScrapQty(BigDecimal.ZERO);
        sfcSplit.setModifyUser(userUtil.getUser().getRealName());
        sfcSplit.setModifyDate(new Date());
        sfcSplit.setCreateDate(new Date());
        //?????????sfc_bo
        sfcSplit.setParentSfcBo(sfc.getParentSfcBo());
        //???????????????sfc????????????
        sfcMapper.insert(sfcSplit);

        //?????????sfc sfcQty?????????????????????????????????????????????
        BigDecimal sfcQty=sfc.getSfcQty().subtract(sfc.getNcQty());
        sfc.setSfcQty(sfcQty);
        sfc.setNcQty(BigDecimal.ZERO);
        sfc.setState("?????????");
//        OpAndId nextOpAndId = getNextOpAndId(sfcObj.getSfcRouterBo(), sfcSplitDto.getRepairOpIds(), false);
//        if (StringUtils.isBlank(nextOpAndId.getOperationBo())){
//            sfc.setState("?????????");
//        }else {
//            sfc.setState("?????????");
//        }
        //?????????sfc??????
        sfcMapper.updateById(sfc);

        //???Sfc?????????????????????me_sfc_nc_log??????sfc?????????????????????sfc
        sfcNcLogMapper.updateBySfc(sfcSplit.getSfc(),sfc.getSfc());


        return sfcSplit;
    }

    /**
     * ????????????????????????????????????????????????
     * @return
     */
    public boolean checkIsFirstOperation(String routerBo,String currrentOperationBo) throws CommonException {
//        ShopOrder shopOrder = shopOrderService.getById(shopOrderBo);
//        String routerBo = shopOrder.getRouterBo();
        String processInfo = routerProcessService.getById(routerBo).getProcessInfo();

        JSONObject jsonObj = JSON.parseObject(processInfo);

        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        JSONArray lineList = JSONArray.parseArray(jsonObj.getString("lineList"));

        String startId = getStartId(nodeList);
        String nextId = getNextId(lineList, startId);
        String operationBo = getOperationById(nodeList,nextId);//?????????

//        UserInfo userInfo = getUserInfo();
        if (currrentOperationBo.equals(operationBo)){
            return true;
        }
        return false;
    }

    /**
     * ????????????????????????????????????
     * @param operationBo
     * @return
     */
    public boolean isCanDisToDeviceOp(String operationBo){
        OperationOrderDTO operationOrderDTO = new OperationOrderDTO();
        operationOrderDTO.setOperationBo(operationBo);
        //?????????????????????????????????????????????
        List<Operation> operations = operationMapper.queryCanDispatchOperation(new Page(0, 10), operationOrderDTO).getRecords();
        if (CollectionUtil.isNotEmpty(operations)){
            return true;
        }
        return false;
    }

    /**
     * ????????????????????????????????????
     * @param operationBo
     * @return
     */
    public boolean isCanAutomaticReportWork(String operationBo){
        String canAutomaticReportWork = operationMapper.isCanAutomaticReportWork(operationBo);
        if (StringUtils.isNotBlank(canAutomaticReportWork)){
            return true;
        }
        return false;
    }
    /**
     * ?????????????????????????????????????????????????????????
     * @return UserInfo
     */
    public UserInfo getUserInfo() throws CommonException {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userUtil.getUser().getId());
        userInfo.setUserName(userUtil.getUser().getRealName());
        userInfo.setUserBo(userUtil.getUser().getUserName());
        userInfo.setProductLineBo(userUtil.getUser().getProductLineBo());
        String onlyStation = "";
        Cookie[] cookies = request.getCookies();//???cookie???????????????
        if (null != cookies){
            for (Cookie cookie : cookies) {
                if ("onlyStation".equals(cookie.getName())){
                    onlyStation = cookie.getValue();
                }
            }
        }
        //redis???????????????
        String station = "";
        try {
            station = redisTemplate.opsForValue().get("station-" + userUtil.getUser().getUserName() + ":" + onlyStation).toString();
        }catch (NullPointerException e){
            e.getMessage();
        }
        log.info("????????????:" + station);
        Station stationObj = stationService.getOne(new QueryWrapper<Station>().eq("station", station));
        Operation op = operationService.getOne(new QueryWrapper<Operation>().eq("bo", stationObj.getOperationBo()));
        if(op==null){
            throw new CommonException("????????????????????????????????????????????????",504);
        }
        userInfo.setOperationBo(op.getBo());
        userInfo.setOperation(op.getOperation());
        userInfo.setOperationName(op.getOperationName());
        userInfo.setStationBo(stationObj.getBo());
        userInfo.setStation(stationObj.getStation());
        userInfo.setStationName(stationObj.getStationDesc());
        userInfo.setOperationType(op.getStationTypeBo().equals("ST:dongyin,????????????") ? 1 : 0);
        userInfo.setWorkStationBo(stationObj.getWorkstationBo());
        //??????????????????
        String workShopBo = sfcMapper.selectWorkShopBoByUserId(userUtil.getUser().getUserName());
        if (StringUtils.isNotBlank(workShopBo)){
            WorkShop workShop = workShopMapper.selectById(workShopBo);
            userInfo.setWorkShopBo(workShop.getBo());
            userInfo.setWorkShop(workShop.getWorkShop());
            userInfo.setWorkShopDesc(workShop.getWorkShopDesc());
        }
        boolean isAllStepReportWork = sfcServiceImpl.isCanAutomaticReportWork(userInfo.getOperationBo());
        if (isAllStepReportWork)userInfo.setIsAllStepReportWork("1");
        else userInfo.setIsAllStepReportWork("0");

        log.info("userInfo:{" + userInfo.toString() + "}");
        return userInfo;
    }

    @Override
    public void stopOperate(OutStationDto outStationDto) throws CommonException{
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", outStationDto.getSfc()));
        UserInfo userInfo = getUserInfo();//??????????????????
        if (!sfcObj.getOperationBo().equals(userInfo.getOperationBo())){
            throw new CommonException("SFC????????????????????????????????????????????????!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (!sfcObj.getState().equals("?????????")){
            throw new CommonException("???????????????sfc????????????!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        boolean canDisToDeviceOp = isCanDisToDeviceOp(userInfo.getOperationBo());
        BigDecimal stopQtyTotal = BigDecimal.ZERO;
        for (OutStationDevice outStationDevice : outStationDto.getOutStationDevices()) {
            SfcWiplog sfcWiplog = new SfcWiplog();
            BeanUtils.copyProperties(sfcObj,sfcWiplog);
            sfcWiplog.setBo(UUID.uuid32());
            sfcWiplog.setOutTime(new Date());
            sfcWiplog.setDoneQty(outStationDevice.getOutStationQty());
            sfcWiplog.setState("??????");

            sfcWiplogMapper.insert(sfcWiplog);
            stopQtyTotal = stopQtyTotal.add(outStationDevice.getOutStationQty());
            if (canDisToDeviceOp){
                String device = deviceService.getOne(new QueryWrapper<Device>().eq("bo",outStationDevice.getDeviceBo())).getDevice();
                QueryWrapper<Dispatch> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("operation_order",sfcObj.getOperationOrder());
                queryWrapper.eq("operation_bo",sfcObj.getOperationBo());
                queryWrapper.eq("device",device);
                queryWrapper.eq("IS_NEED_DISPATCH","1");
                queryWrapper.orderByDesc("create_date");
                List<Dispatch> dispatches = dispatchService.list(queryWrapper);
                BigDecimal subQty = outStationDevice.getOutStationQty();
                Dispatch dispatchObj = new Dispatch();
                if (CollectionUtil.isNotEmpty(dispatches)){
                    for (Dispatch dispatch : dispatches) {
                        dispatchObj.setId(dispatch.getId());
                        if (dispatch.getWaitIn().compareTo(subQty) == -1){//??????????????????????????????wait_in?????????????????????wait_in????????????0,???????????????????????????????????????????????????
                            dispatchObj.setWaitIn(BigDecimal.ZERO);
                            dispatchService.updateById(dispatchObj);
                            subQty = subQty.subtract(dispatch.getWaitIn());
                            continue;
                        }
                        dispatchObj.setWaitIn(dispatch.getWaitIn().subtract(subQty));
                        dispatchService.updateById(dispatchObj);
                        break;//????????????
                    }
                }
            }
            //???????????????
            ReportWork reportWork = new ReportWork();
            BeanUtils.copyProperties(sfcObj,reportWork);
            String workStepBo = stationService.getOne(new QueryWrapper<Station>().eq("station", userInfo.getStation())).getWorkstationBo();
            reportWork.setWorkStepCodeBo(workStepBo);
            reportWork.setQty(outStationDevice.getOutStationQty());
            reportWork.setMeSfcWipLogBo(sfcWiplog.getBo());
            reportWork.setState("0");
            reportWork.setTime(new Date());
            reportWork.setBo(UUID.uuid32());
            reportWork.setCreateTime(new Date());

//            //?????????????????????????????????
//            OkReportWorkDto okReportWorkDto=new OkReportWorkDto();
//            okReportWorkDto.setSfc(sfcObj.getSfc());
//            okReportWorkDto.setWorkStepCodeBo(workStepBo);
//            if(!reportWorkService.isConfig(okReportWorkDto)){
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//????????????
//                throw new CommonException("?????????????????????,????????????",30002);
//            }
            reportWorkMapper.insert(reportWork);

            //???????????????device_sfc???????????????
            SfcDevice sfcDevice = sfcDeviceMapper.selectById(outStationDevice.getDeviceBo());
            if (sfcDevice.getShopOrderBo().equals(sfcObj.getShopOrderBo())){//???????????????????????????
                SfcDevice sfcDeviceEntity = new SfcDevice();
                sfcDeviceEntity.setDeviceBo(outStationDevice.getDeviceBo());
                sfcDeviceEntity.setSfc("");
                sfcDeviceEntity.setShopOrderBo("");
                sfcDeviceMapper.updateById(sfcDeviceEntity);
            }
        }
        if (!canDisToDeviceOp){//????????????????????????????????????????????????????????????wait_in??????
            QueryWrapper<Dispatch> wrapper = new QueryWrapper<>();
            wrapper.eq("operation_order",sfcObj.getOperationOrder());
            wrapper.eq("operation_bo",sfcObj.getOperationBo());
            wrapper.eq("IS_NEED_DISPATCH","1");
            List<Dispatch> dispatchList = dispatchService.list(wrapper);
            Dispatch dispatch = new Dispatch();
            if (CollectionUtil.isNotEmpty(dispatchList)){
                dispatch.setId(dispatchList.get(0).getId());
                dispatch.setWaitIn(dispatchList.get(0).getWaitIn().subtract(stopQtyTotal));
                dispatchService.updateById(dispatch);
            }
        }
        Sfc sfc = new Sfc();
        sfc.setBo(sfcObj.getBo());
        sfc.setState("??????");
        sfc.setInTime(new Date());
        sfc.setStopQty(sfcObj.getStopQty().add(stopQtyTotal));
        sfc.setInputQty(BigDecimal.ZERO);

        sfcMapper.updateById(sfc);

        QueryWrapper<Dispatch> dispatchQueryWrapper = new QueryWrapper<>();
        dispatchQueryWrapper.eq("operation_bo",sfcObj.getOperationBo());
        dispatchQueryWrapper.eq("operation_order",sfcObj.getOperationOrder());
        dispatchQueryWrapper.eq("is_need_dispatch","0");
        Dispatch dispatchObj = dispatchService.getOne(dispatchQueryWrapper);
        if (ObjectUtil.isNotEmpty(dispatchObj)){
            Dispatch dispatch = new Dispatch();
            dispatch.setId(dispatchObj.getId());
            dispatch.setNotDoneQty(dispatchObj.getNotDoneQty().subtract(stopQtyTotal));
            dispatchService.updateById(dispatch);
        }
    }

    @Override
    public List<ReworkVo> selectReworkList(String sfc) throws CommonException {
        String operationBo = getUserInfo().getOperationBo();//????????????
        String stationBo = getUserInfo().getStationBo();//????????????
        List<ReworkVo> reworkVoPartOne = sfcMapper.selectReworkPartOne(sfc,operationBo,stationBo);
        return reworkVoPartOne;
    }

    @Override
    public void okRework(List<ReworkDto> reworkDto) throws CommonException {
        String sfc = "";
        String operationBo = "";//?????????????????????
        String opIds = "";//???????????????????????????ID
        for (ReworkDto dto : reworkDto) {
            sfc = dto.getSfc();
            operationBo = dto.getOperationBo();
            opIds = dto.getOpIds();
            break;
        }
        UserInfo userInfo = getUserInfo();
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfc));
        BigDecimal doneQty = sfcObj.getDoneQty();//??????ok?????????
        BigDecimal scrapQty = sfcObj.getScrapQty();//???????????????
        for (ReworkDto dto : reworkDto) {
            SfcRepair sfcRepair = new SfcRepair();
            BeanUtils.copyProperties(sfcObj,sfcRepair);
            sfcRepair.setBo(UUID.uuid32());
            BeanUtils.copyProperties(dto,sfcRepair);
            sfcRepair.setRepairTime(new Date());
            sfcRepairMapper.insert(sfcRepair);
            doneQty = doneQty.add(dto.getRepairQty());
            scrapQty = scrapQty.add(dto.getScrapQty());

            //????????????
            ReportWork reportWork = new ReportWork();
            BeanUtils.copyProperties(sfcObj,reportWork);
            String workStepBo = stationService.getOne(new QueryWrapper<Station>().eq("station", userInfo.getStation())).getWorkstationBo();
            reportWork.setWorkStepCodeBo(workStepBo);
            reportWork.setQty(dto.getRepairQty());
            reportWork.setMeSfcWipLogBo(dto.getWipLogBo());
            reportWork.setState("0");
            reportWork.setTime(new Date());
            reportWork.setBo(UUID.uuid32());
            reportWork.setCreateTime(new Date());
            reportWork.setOperationBo(operationBo);
            reportWork.setUserBo(userInfo.getUserBo());
            reportWorkMapper.insert(reportWork);
        }
        SfcWiplog sfcWiplog = new SfcWiplog();
        BeanUtils.copyProperties(sfcObj,sfcWiplog);
        BigDecimal thisOkDoneQty = doneQty.subtract(sfcObj.getDoneQty());//?????????????????????
        BigDecimal thisOkScrapQty = scrapQty.subtract(sfcObj.getScrapQty());//????????????????????????

        sfcWiplog.setBo(UUID.uuid32());
        sfcWiplog.setOutTime(new Date());
        sfcWiplog.setInputQty(thisOkDoneQty.add(thisOkScrapQty));
        sfcWiplog.setDoneQty(thisOkDoneQty);
        sfcWiplog.setNcQty(BigDecimal.ZERO);
        sfcWiplog.setScrapQty(thisOkScrapQty);
        sfcWiplog.setState("??????");
        sfcWiplog.setInTime(new Date());
        sfcWiplogMapper.insert(sfcWiplog);

        Sfc sfcUpdate = new Sfc();
        sfcUpdate.setBo(sfcObj.getBo());
        sfcUpdate.setNcQty(BigDecimal.ZERO);
        sfcUpdate.setDoneQty(doneQty);
        sfcUpdate.setScrapQty(scrapQty);
        sfcUpdate.setModifyDate(new Date());
        if (sfcObj.getSfcQty().compareTo(scrapQty) == 0){
            sfcUpdate.setState("?????????");
        }else {
            OpAndId nextOpAndId = getNextOpAndId(sfcObj.getSfcRouterBo(),opIds, false);
            if (StringUtils.isBlank(nextOpAndId.getOperationBo())){//??????????????????
                sfcUpdate.setState("?????????");
            }else {
                sfcUpdate.setState("?????????");
            }

        }
        sfcMapper.updateById(sfcUpdate);

        //???me_sfc_nc_log????????????????????????????????????1???
        SfcNcLog sfcNcLog = new SfcNcLog();
        sfcNcLog.setIsRepaired("1");
        QueryWrapper<SfcNcLog> sfcNcLogQueryWrapper = new QueryWrapper<>();
        sfcNcLogQueryWrapper.eq("sfc",sfc);
        sfcNcLogQueryWrapper.eq("operation_bo",operationBo);
        sfcNcLogMapper.update(sfcNcLog,sfcNcLogQueryWrapper);

        if (sfcUpdate.getState().equals("?????????")){
            //??????????????????????????????sfc?????????sfc??????????????? == ???????????????????????????????????????????????????503???
            QueryWrapper<Sfc> sfcQw = new QueryWrapper<>();
            sfcQw.eq("state","?????????");
            sfcQw.eq("shop_order_bo",sfcObj.getShopOrderBo());
            List<Sfc> sfcList = sfcMapper.selectList(sfcQw);
            BigDecimal sfcTotalQty = BigDecimal.ZERO;//???????????????
            for (Sfc s : sfcList) {
                sfcTotalQty = sfcTotalQty.add(s.getSfcQty());
            }
            BigDecimal orderQty = shopOrderService.getOne(new QueryWrapper<ShopOrder>().eq("bo",sfcObj.getShopOrderBo())).getOrderQty();//????????????
            if (sfcTotalQty.compareTo(orderQty) == 0){
                ShopOrder shopOrder = new ShopOrder();
                shopOrder.setBo(sfcObj.getShopOrderBo());
                shopOrder.setStateBo("STATE:dongyin,503");
                shopOrderService.updateById(shopOrder);
            }

            //??????????????????????????????
            QueryWrapper<Sfc> sfcQueryWrapper = new QueryWrapper<>();
            sfcQueryWrapper.eq("state","?????????");
            sfcQueryWrapper.eq("operation_order",sfcObj.getOperationOrder());
            List<Sfc> sfcObjList = sfcMapper.selectList(sfcQueryWrapper);
            BigDecimal sfcOpOrderTotalQty = BigDecimal.ZERO;//???????????????????????????
            for (Sfc s : sfcObjList) {
                sfcOpOrderTotalQty = sfcOpOrderTotalQty.add(s.getSfcQty());
            }
            BigDecimal operationOrderQty = operationOrderService.getOne(new QueryWrapper<OperationOrder>().eq("operation_order", sfcObj.getOperationOrder())).getOperationOrderQty();//??????????????????

            if (sfcOpOrderTotalQty.compareTo(operationOrderQty) == 0){
                OperationOrder operationOrder = new OperationOrder();
                operationOrder.setOperationOrderState("2");//??????????????????
                operationOrderService.update(operationOrder,new QueryWrapper<OperationOrder>().eq("operation_order", sfcObj.getOperationOrder()));
            }
        }
    }

    /*WIP??????*/
    @Override
    public List<Map<String, Object>> wipData(Map<String,Object> params){
        List<Map<String,Object>> wipLog = new ArrayList<>();
        List<Map<String,Object>> wipData = sfcMapper.selectOperationOrder(params);
        for (Map<String, Object> wipDatum : wipData) {
            List<Sfc> sfcEntityList = new ArrayList<>();
            List<Map<String,Object>> sfcMapList = new ArrayList<>();
            int doingQty = 0;
            int doingSum = 0;
            int doneSum = 0;
            int scrapSum = 0;
            String operationOrder = (String) wipDatum.get("operationOrder");
            BigDecimal orderQ = (BigDecimal) wipDatum.get("orderQty");
            int orderQty = orderQ.intValue();
            String operationBo = (String) params.get("operationBo");
            Map<String,Object> param = new HashMap<>();
            param.put("operationOrder",operationOrder);
            param.put("operationBo",operationBo);
//            param.put("operationName",(String) params.get("operationName"));
            if (StrUtil.isNotEmpty(operationOrder)){
                String operationNameParam = (String) params.get("operationName");
                List<String> operationNameList = Collections.EMPTY_LIST;
                if (StringUtils.isNotBlank(operationNameParam)){
                    operationNameList = new ArrayList<>();
                    for (String s : operationNameParam.split(",")) {
                        operationNameList.add(s);
                    }
                }
                param.put("operationNameList",operationNameList);
                sfcEntityList = sfcMapper.getSfcByOperationOrder(param);
                if (sfcEntityList.size()!=0){
                    for (Sfc sfc : sfcEntityList) {
                        Map<String,Object> sfcMap = new HashMap<>();
                        String sn = sfc.getSfc();
                        int sfcQty = sfc.getSfcQty().intValue();
                        int doneQty = 0;
                        int scrapQty = sfc.getScrapQty().intValue();
                        String operationName = sfc.getOperationName();
                        if (sfc.getState().equals("?????????")){
                            doneQty = sfcQty-scrapQty;
                            doingQty = sfcQty-doneQty-scrapQty;
                        }
                        else {
                            doneQty = sfc.getDoneQty().intValue();
                            doingQty = sfcQty-scrapQty-doneQty;
                        }
                        scrapSum += scrapQty;
                        doingSum += doingQty;
                        doneSum += doneQty;
                        sfc.setDoingQty(doingQty);
                        sfcMap.put("sfc",sn);
                        sfcMap.put("state",sfc.getState());
                        sfcMap.put("sfcQty",sfcQty);
                        sfcMap.put("doneQty",doneQty);
                        sfcMap.put("doingQty",doingQty);
                        sfcMap.put("operationName",operationName);
                        if (doingQty!=0){
                            sfcMapList.add(sfcMap);
                        }
                    }
                }
            }
            wipDatum.put("scrapSum",scrapSum);
            wipDatum.put("doneSum",doneSum);
            wipDatum.put("doingSum",doingSum);
            wipDatum.put("sfcMapList",sfcMapList);
            if (doingSum!=0){
                wipLog.add(wipDatum);
            }
        }
        return wipLog;
    }

    @Override
    public void exportWipData(Map<String, Object> params, HttpServletResponse response) throws CommonException, IOException {
        List<Map<String, Object>> wipData = wipData(params);
        String fileName = "WIP???????????????";
        //??????Excel???????????????
        HSSFWorkbook workbook = new HSSFWorkbook();
        //??????Excel???????????????
        HSSFSheet sheet = workbook.createSheet();
        int titleNum = 13;
        sheet.setColumnWidth(0, 5500);
        sheet.setColumnWidth(1, 1500);
        sheet.setColumnWidth(2, 4500);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 11000);
        sheet.setColumnWidth(6, 2500);
        sheet.setColumnWidth(7, 2500);
        sheet.setColumnWidth(8, 2000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 3000);
        sheet.setColumnWidth(11, 3000);

        // ????????????????????????
        HSSFFont columnHeadFont = workbook.createFont();
        columnHeadFont.setFontName("??????");
        columnHeadFont.setFontHeightInPoints((short) 10);
        columnHeadFont.setBold(true);
        // ???????????????
        HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
        columnHeadStyle.setFont(columnHeadFont);
        // ????????????
        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);
        // ????????????
        columnHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        columnHeadStyle.setLocked(true);
        columnHeadStyle.setWrapText(true);
        // ?????????????????????????????????
        HSSFFont font = workbook.createFont();
        font.setFontName("??????");
        font.setFontHeightInPoints((short) 10);
        //??????Excel??????????????????
        HSSFRow row0 = sheet.createRow(0);
        // ????????????
        row0.setHeight((short) 750);
        HSSFCell cell = row0.createCell(0);
        //?????????????????????
        cell.setCellValue(new HSSFRichTextString("????????????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(1);
        cell.setCellValue(new HSSFRichTextString("??????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(2);
        cell.setCellValue(new HSSFRichTextString("??????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(3);
        cell.setCellValue(new HSSFRichTextString("??????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(4);
        cell.setCellValue(new HSSFRichTextString("????????????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(5);
        cell.setCellValue(new HSSFRichTextString("????????????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(6);
        cell.setCellValue(new HSSFRichTextString("????????????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(7);
        cell.setCellValue(new HSSFRichTextString("????????????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(8);
        cell.setCellValue(new HSSFRichTextString("?????????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(9);
        cell.setCellValue(new HSSFRichTextString("SFC"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(10);
        cell.setCellValue(new HSSFRichTextString("??????"));
        cell.setCellStyle(columnHeadStyle);
        cell = row0.createCell(11);
        cell.setCellValue(new HSSFRichTextString("???????????????"));
        cell.setCellStyle(columnHeadStyle);
        int rowSize = 1;
        for (int i = 0; i < wipData.size(); i++) {
            Map<String, Object> wipDatum = wipData.get(i);
            List<Map<String , Object>> sfcMapList = (List<Map<String, Object>>) wipDatum.get("sfcMapList");
            int listSize = sfcMapList.size();
            if (listSize>1){
                for (int j = 0; j <= 8; j++) {
                    CellRangeAddress cas = new CellRangeAddress(rowSize,rowSize+listSize-1,j,j);
                    sheet.addMergedRegion(cas);
                }
            }
            for (int i1 = 0; i1 < sfcMapList.size(); i1++) {
                Map<String, Object> sfcMap = sfcMapList.get(i1);
                HSSFRow row = sheet.createRow(rowSize+i1);
                cell = row.createCell(0);
                cell.setCellValue(new HSSFRichTextString(wipDatum.getOrDefault("operationOrder","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(1);
                cell.setCellValue(new HSSFRichTextString(wipDatum.getOrDefault("orderQty","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(2);
                cell.setCellValue(new HSSFRichTextString(wipDatum.getOrDefault("shopOrder","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(3);
                cell.setCellValue(new HSSFRichTextString(wipDatum.getOrDefault("item","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(4);
                cell.setCellValue(new HSSFRichTextString(wipDatum.getOrDefault("itemName","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(5);
                cell.setCellValue(new HSSFRichTextString(wipDatum.getOrDefault("itemDesc","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(6);
                cell.setCellValue(new HSSFRichTextString(wipDatum.getOrDefault("doneSum","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(7);
                cell.setCellValue(new HSSFRichTextString(wipDatum.getOrDefault("scrapSum","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(8);
                cell.setCellValue(new HSSFRichTextString(wipDatum.getOrDefault("doingSum","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(9);
                cell.setCellValue(new HSSFRichTextString(sfcMap.getOrDefault("sfc","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(10);
                cell.setCellValue(new HSSFRichTextString(sfcMap.getOrDefault("operationName","").toString()));
                cell.setCellStyle(columnHeadStyle);
                cell = row.createCell(11);
                cell.setCellValue(new HSSFRichTextString(sfcMap.getOrDefault("doingQty","").toString()));
                cell.setCellStyle(columnHeadStyle);
            }


            rowSize += listSize;
        }
        // ???????????????
        response.setContentType("application/x-download");
        response.setCharacterEncoding("utf-8");
        OutputStream os = response.getOutputStream();
        // ???????????????
        response.reset();
        // ?????????????????????
        response.setHeader("Content-disposition",
                "attachment; filename=" + new String("WIP".getBytes("gbk"), "iso8859-1") + ".xls");
        // ??????????????????
        response.setContentType("application/msexcel");

        workbook.write(os);
        os.close();
        return;
    }
    /**
     * ??????????????????Id??????????????????????????????ID
     * @param currentId
     * @return
     */
    public String getPreviousIdByCurrentId(String currentId,JSONArray lineList){
        String from = null;
        for (int i = 0; i < lineList.size(); i++) {
            String to = JSON.parseObject(lineList.get(i).toString()).getString("to");
            if (to.equals(currentId)){
                from = JSON.parseObject(lineList.get(i).toString()).getString("from");
                break;
            }
        }
        return from;
    }
    public void getNext(String to,JSONArray lineList,List<String> theNextOpIds){
        boolean result =false;
        if (lineList.size() > 0){
            for (int i = 0; i < lineList.size(); i++) {
                String from = JSON.parseObject(lineList.get(i).toString()).getString("from");
                if (from.equals(to)){
                    theNextOpIds.add(from);
                    to = JSON.parseObject(lineList.get(i).toString()).getString("to");
                    result=true;
                }
            }
            if(result){
                getNext(to,lineList,theNextOpIds);
            }
        }
    }
    /**
     * ????????????????????????bo?????????id(???????????????????????????????????????????????? | ????????????)
     * @param routerBo ????????????bo
     * @param opId  ?????????????????????id
     * @param isStartProcess ?????????????????????
     * @return
     */
    public OpAndId getNextOpAndId(String routerBo,String opId,boolean isStartProcess){
        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", routerBo));
        String processInfo = routerProcess.getProcessInfo();
        JSONObject jsonObj = JSON.parseObject(processInfo);

        if (isStartProcess){//?????????????????????
            JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
            if (nodeList.size() > 0){
                for (int i = 0; i < nodeList.size(); i++) {
                    String ico = JSON.parseObject(nodeList.get(i).toString()).getString("ico");
                    if (ico.equals("craftRouteStart")){
                        String id = JSON.parseObject(nodeList.get(i).toString()).getString("id");
                        opId = id;
                        break;
                    }
                }
            }
        }
        List<String> theNextOpIds = new ArrayList<>();//????????????????????????id????????????????????????
        JSONArray lineList = JSONArray.parseArray(jsonObj.getString("lineList"));
        if (lineList.size() > 0){
            for (int i = 0; i < lineList.size(); i++) {
                String from = JSON.parseObject(lineList.get(i).toString()).getString("from");
                if (from.equals(opId)){
                    String to = JSON.parseObject(lineList.get(i).toString()).getString("to");
                    theNextOpIds.add(to);
                }
            }
        }
        String operationBos = "";
        String ids = "";

        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        if (nodeList.size() > 0){
            for (int i = 0; i < nodeList.size(); i++) {
                String id = JSON.parseObject(nodeList.get(i).toString()).getString("id");
                for (int j = 0; j < theNextOpIds.size(); j++) {
                    String operationId = theNextOpIds.get(j);
                    if (id.equals(operationId)){
                        if (StringUtils.isBlank(operationBos)){
                            operationBos = JSON.parseObject(nodeList.get(i).toString()).getString("operation");
                            ids = operationId;
                            continue;
                        }
                        operationBos = operationBos + "|" + JSON.parseObject(nodeList.get(i).toString()).getString("operation");
                        ids = ids + "|" + operationId;
                    }
                }
            }
        }
        OpAndId opAndId = new OpAndId();
        opAndId.setOperationBo(operationBos);
        opAndId.setId(ids);
        return opAndId;
    }

    //    @Override
//    public List<Operation> getOperationByRouterBo(String routerBo,String sfc) {
//        //?????????sfc????????????????????????
//        QueryWrapper<SfcWiplog> wipLogQw = new QueryWrapper<>();
//        wipLogQw.eq("sfc",sfc);
//        wipLogQw.eq("state","??????");
//        List<SfcWiplog> sfcWiplogs = sfcWiplogMapper.selectList(wipLogQw);
//        List<String> notSkipOpList = Lists.newArrayList();//??????????????????????????????
//        if (CollectionUtil.isNotEmpty(sfcWiplogs)){
//            for (SfcWiplog sfcWiplog : sfcWiplogs) {
//                notSkipOpList.add(sfcWiplog.getOperationBo());
//            }
//        }
////        notSkipOpList.add(sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc",sfc)).getOperationBo());//sfc??????????????????????????????????????????
//        List<String> notSkipOpDList = notSkipOpList.stream().distinct().collect(Collectors.toList());//??????
//
//        List<String> orderOpList = getRouterOrderList(routerBo);
//        int k = -1;//??????????????????????????????????????????????????????????????????????????????
//        for (int i = 0; i < orderOpList.size(); i++) {
//            for (String notSkipOpD : notSkipOpDList) {
//                if (orderOpList.get(i).equals(notSkipOpD)){
//                    if (i > k){
//                        k = i;
//                    }
//                }
//            }
//        }
//        //?????????k??? + 1 ????????????????????????????????????????????????
//        List<String> canSkipOpList = Lists.newArrayList();
//        for (int i = k + 1; i < orderOpList.size(); i++) {
//            canSkipOpList.add(orderOpList.get(i));
//        }
//
//        List<String> collect = canSkipOpList.stream().distinct().collect(Collectors.toList());
//        List<Operation> operations = Lists.newArrayList();
//        collect.forEach(
//                x -> {
//                    Operation bo = operationService.getOne(new QueryWrapper<Operation>().eq("bo", x));
//                    operations.add(bo);
//                }
//        );
//        return operations;
//    }
    @Override
    public List<Operation> getOperationByRouterBo(String routerBo,String sfc) throws CommonException{
        List<String> orderOpList = getRouterOrderList(routerBo);//??????????????????????????????
        //????????????sfc??????????????????BO??????
        List<String> doneOpList = Lists.newArrayList();//???sfc????????????????????????
        List<SfcStateTrack> doneList = sfcStateTrackMapper.selectList(new QueryWrapper<SfcStateTrack>().eq("sfc", sfc).eq("state", "3"));
        for (SfcStateTrack sfcStateTrack : doneList) {
            doneOpList.add(sfcStateTrack.getOperationBo());
        }
        List<String> collect = orderOpList.stream().filter(x -> !doneOpList.contains(x)).collect(Collectors.toList());//???????????????????????????
        if (CollectionUtil.isEmpty(collect)){
            throw new CommonException("???????????????????????????",504);
        }
        List<Operation> operations = Lists.newArrayList();
        collect.forEach(
                x -> {
                    Operation bo = operationService.getOne(new QueryWrapper<Operation>().eq("bo", x));
                    operations.add(bo);
                }
        );
        return operations;
    }
    public JSONArray getObjects(String routerBo) {
        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", routerBo));
        String processInfo = routerProcess.getProcessInfo();
        JSONObject jsonObj = JSON.parseObject(processInfo);
        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        return nodeList;
    }

    @Override
    public void skipStation(SkipStationDTO skipStationDTO) throws CommonException{
        //?????????sfc????????????????????????????????????
        // ???1????????????sfc????????????????????????????????????????????????track?????????????????????3????????????
        // ???2????????????????????????????????????
        QueryWrapper<SfcStateTrack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sfc",skipStationDTO.getSfc());
        queryWrapper.ne("state","3");
        List<SfcStateTrack> sfcStateTracks = sfcStateTrackMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(sfcStateTracks)){
            throw new CommonException("???sfc?????????????????????????????????????????????????????????",504);
        }
        SkipStation skipStation = new SkipStation();
        BeanUtils.copyProperties(skipStationDTO,skipStation);
        skipStation.setId(UUID.uuid32());
        skipStation.setCreateDate(new Date());
        skipStation.setUserBo(userUtil.getUser().getUserName());
        skipStationMapper.insert(skipStation);
        String routerBo;
        if (StringUtils.isNotBlank(skipStationDTO.getNewRouterBo())){
            routerBo = skipStationDTO.getNewRouterBo();
        }else {
            routerBo = skipStationDTO.getOldRouterBo();
        }
        JSONArray nodeList = getObjects(routerBo);
        String opId = null;
        for (int i = 0; i < nodeList.size(); i++) {
            String operation = JSON.parseObject(nodeList.get(i).toString()).getString("operation");
            if (StringUtils.isNotBlank(operation)){
                if (operation.equals(skipStationDTO.getOperationBo())){
                    opId = JSON.parseObject(nodeList.get(i).toString()).getString("id");
                    break;
                }
            }
        }
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", skipStationDTO.getSfc()));
//        //?????????????????????????????????????????????????????????????????????????????????????????????
        RouterProcessTable routerProcessTable = routerProcessTableMapper.selectOne(new QueryWrapper<RouterProcessTable>().
                eq("router_bo", routerBo).
                eq("operation_bo",skipStationDTO.getOperationBo()));

        if (routerProcessTable.getParallelFlag() != 0){//???????????????
            int positionFlag = routerProcessTable.getPositionFlag();
            opId = getRealOpIds(routerBo, skipStationDTO.getSfc(), skipStationDTO.getOperationBo(), positionFlag);
        }
        Sfc sfc = new Sfc();
        sfc.setSfcRouterBo(routerBo);
        sfc.setOperationBo(skipStationDTO.getOperationBo());
        sfc.setOpIds(opId);
        sfc.setIsSkipStationSfc("1");
        sfc.setSkipOperationBo(skipStationDTO.getOperationBo());
        sfc.setSkipBeforeScrapQty(sfcObj.getScrapQty());
        sfcMapper.update(sfc,new QueryWrapper<Sfc>().eq("sfc",skipStationDTO.getSfc()));

        sfcSkipBeforeOperationMapper.delete(new QueryWrapper<SfcSkipBeforeOperation>().eq("sfc",skipStationDTO.getSfc()));
        //???sfc???????????????????????????
        List<SfcStateTrack> sfcStateTracksList = sfcStateTrackMapper.selectList(new QueryWrapper<SfcStateTrack>().eq("sfc", skipStationDTO.getSfc()));
        SfcSkipBeforeOperation sfcSkipBeforeOperation;
        Date date = new Date();
        for (SfcStateTrack sfcStateTrack : sfcStateTracksList) {
            sfcSkipBeforeOperation = new SfcSkipBeforeOperation();
            sfcSkipBeforeOperation.setSfc(skipStationDTO.getSfc());
            sfcSkipBeforeOperation.setOperationBo(sfcStateTrack.getOperationBo());
            sfcSkipBeforeOperation.setCreateDate(date);
            sfcSkipBeforeOperationMapper.insert(sfcSkipBeforeOperation);
        }
    }

    /**
     * ?????????????????????????????????ID
     * @param routerBo
     * @param sfc
     * @param operationBo
     * @return
     */
    private String getRealOpIds(String routerBo,String sfc,String operationBo,int positionFlag){
        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", routerBo));
        String processInfo = routerProcess.getProcessInfo();
        JSONObject jsonObj = JSON.parseObject(processInfo);
        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        JSONArray lineList = JSONArray.parseArray(jsonObj.getString("lineList"));

        /*???????????????
        ??????????????????????????????A
                       B1  C1
                       C2  B2
                        D
        ??????????????????????????????????????????
        ?????????????????????B1???C1
        ????????????????????????????????????????????????
        ??????????????????C2???B1
        */
        List<RouterProcessTable> routerProcessTableList = routerProcessTableMapper.selectList(new QueryWrapper<RouterProcessTable>().
                eq("router_bo", routerBo).
                eq("position_flag", positionFlag));
        String parallelOpA = routerProcessTableList.get(0).getOperationBo();
        String parallelOpB = routerProcessTableList.get(1).getOperationBo();

        List<SfcStateTrack> sfcStateTracks = sfcStateTrackMapper.selectList(new QueryWrapper<SfcStateTrack>().
                eq("sfc", sfc).
                in("operation_bo", parallelOpA, parallelOpB));
        List<String> parallelIdByOperation = getParallelIdByOperation(nodeList, operationBo);
        String opId;
        if (sfcStateTracks.size() == 0){//??????????????????
            //?????????????????????ID?????????ID?????????????????????ID??????????????????????????????????????????????????????????????????B1,?????????B2????????????????????????B1
            opId = getRealOpIdA(parallelIdByOperation, nodeList, lineList, routerBo);
        }else {//size == 1 ?????????????????????
            //?????????????????????ID?????????ID?????????????????????ID??????????????????????????????????????????????????????????????????B2,?????????B1????????????????????????B2
            opId = getRealOpIdB(parallelIdByOperation, nodeList, lineList, routerBo);
        }
        return opId;
    }

    private String getRealOpIdB(List<String> parallelIdByOperation,JSONArray nodeList,JSONArray lineList,String routerBo){
        String idA = parallelIdByOperation.get(0);
        String idB = parallelIdByOperation.get(1);

        String nextIdA = getNextId(lineList, idA);
        String nextIdB = getNextId(lineList, idB);

        String operationA = getOperationById(nodeList, nextIdA);
        List<RouterProcessTable> routerProcessTableList1 = routerProcessTableMapper.selectList(new QueryWrapper<RouterProcessTable>().
                eq("router_bo", routerBo).eq("operation_bo", operationA));

        if (routerProcessTableList1.get(0).getParallelFlag() == 0){
            return idA;
        }
        String operationB = getOperationById(nodeList, nextIdB);
        List<RouterProcessTable> routerProcessTableList2 = routerProcessTableMapper.selectList(new QueryWrapper<RouterProcessTable>().
                eq("router_bo", routerBo).eq("operation_bo", operationB));
        if (routerProcessTableList2.get(0).getParallelFlag() == 0){
            return idB;
        }
        return null;
    }
    private String getRealOpIdA(List<String> parallelIdByOperation,JSONArray nodeList,JSONArray lineList,String routerBo){
        String idA = parallelIdByOperation.get(0);
        String idB = parallelIdByOperation.get(1);

        String previousIdA = getPreviousIdByCurrentId(idA,lineList);
        String previousIdB = getPreviousIdByCurrentId(idB,lineList);

        String operationA = getOperationById(nodeList, previousIdA);
        List<RouterProcessTable> routerProcessTableList1 = routerProcessTableMapper.selectList(new QueryWrapper<RouterProcessTable>().
                eq("router_bo", routerBo).eq("operation_bo", operationA));
        if (routerProcessTableList1.get(0).getParallelFlag() == 0){
            return idA;
        }
        String operationB = getOperationById(nodeList, previousIdB);
        List<RouterProcessTable> routerProcessTableList2 = routerProcessTableMapper.selectList(new QueryWrapper<RouterProcessTable>().
                eq("router_bo", routerBo).eq("operation_bo", operationB));
        if (routerProcessTableList2.get(0).getParallelFlag() == 0){
            return idB;
        }
        return null;
    }
    //????????????code
    @Value("${testOp.code}")
    private String code;
    //????????????
    @Value("${testOp.typeName}")
    private String typeName;
    /**
     * ??????????????????
     * @param sfcSplitDto
     * @return
     * @throws CommonException
     */
    @Override
    public Sfc inSfcSplit(SfcSplitDto sfcSplitDto) throws CommonException {
        // ??????sfc???????????????????????????BO
        // ???sfc
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfcSplitDto.getSfc()));
        if(sfcObj==null){
            throw new CommonException("???????????????????????????????????????",30002);
        }
        if(sfcObj.getSfcRouterBo()==null || sfcObj.getSfcRouterBo()==""){
            throw new CommonException("????????????????????????????????????",30002);
        }
        if ("?????????".equals(sfcObj.getState()) || "?????????".equals(sfcObj.getState())){
            throw new CommonException("???????????????????????????????????????????????????",30002);
        }
        List<String> routerNodeList=getRouterOrderList(sfcObj.getSfcRouterBo());
        Sfc sfcSplit=new Sfc();
        if(routerNodeList !=null && routerNodeList.size()>0){
            String lastOperationBO=routerNodeList.get(routerNodeList.size()-1);//??????????????????????????????
            // ??????me_sfc_wip_log???????????????????????????????????????????????????
//            List<SfcWiplog> sfcWiplogs=sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().eq("SFC",sfcSplitDto.getSfc()).eq("OPERATION_BO",lastOperationBO));
//            if(sfcWiplogs ==null || sfcWiplogs.size()==0){
//                throw new CommonException("???????????????????????????????????????????????????????????????????????????",30002);
//            }
            /*?????????????????????????????????sfc??????????????????????????????sfc??????????????????????????????????????????????????????sfc?????????????????????????????????
            sfcWiplogs????????????????????????
            */
            BigDecimal canSplitQty = getCanSplitQtyBySfc(sfcSplitDto.getSfc());//???????????????
            //????????????????????????????????????0
            if (new BigDecimal(sfcSplitDto.getInSfcQty()).compareTo(BigDecimal.ZERO) != 1){
                throw new CommonException("????????????????????????0",30002);
            }
            //?????????????????????????????????????????????
            if (new BigDecimal(sfcSplitDto.getInSfcQty()).compareTo(canSplitQty) == 1){
                throw new CommonException("???????????????????????????????????????",30002);
            }
            //????????????????????????
//            int doneQtySum=calculationDoneQty(sfcWiplogs);
            //???sfc??????????????????????????????sfc??????????????????????????????????????????????????????sfc
//            List<Sfc> sfcList=sfcMapper.selectList(new QueryWrapper<Sfc>().eq("PARENT_SFC_BO",sfcObj.getBo()).eq("OPERATION_BO",lastOperationBO).eq("STATE","?????????"));
//            int sfcQtySum=calculationSfcQty(sfcSplitDto.getInSfcQty(),sfcList);
//            if(sfcQtySum>doneQtySum){
//                throw new CommonException("???????????????????????????????????????????????????????????????????????????",30002);
//            }

            String site = UserUtils.getSite();
            Sfc sfc=new Sfc();
            BeanUtils.copyProperties(sfcSplitDto,sfc);
            sfc.setOpIds(sfcObj.getOpIds());
            //BigDecimal ncQty=sfc.getNcQty();
            //?????????sfc???????????????
            BeanUtils.copyProperties(sfc,sfcSplit);
            String codeRuleType=sfcSplitDto.getCodeRuleType();
            CodeRule codeRule = codeRuleService.getExistCodeRule(new CodeRuleHandleBO(site,codeRuleType));
            HashMap<String, Object> params = new HashMap<>();

            String operationOrder=sfcSplitDto.getOperationOrder();
            //??????????????????
            String shopOrder = operationService.selectShopOrderByOperationOrder(operationOrder);
            ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(site, shopOrder));
            ItemHandleBO itemHandleBO = new ItemHandleBO(existShopOrder.getItemBo());
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
            //????????????sfc
            List<String> sfcsByRule = codeRuleService.generatorNextNumberList(codeRule.getBo(), 1, params);
            sfcSplit.setSfc(sfcsByRule.get(0));
            sfcSplit.setBo(new SfcHandleBo(site,sfcsByRule.get(0)).getBo());//??????BO
            sfcSplit.setStationBo(null);
            sfcSplit.setDeviceBo(null);
            sfcSplit.setUserBo(null);
            sfcSplit.setShitBo(null);
            sfcSplit.setTeamBo(null);
            //??????????????????????????????sfc?????????
            sfcSplit.setSfcQty(new BigDecimal(sfcSplitDto.getInSfcQty()));
            sfcSplit.setInputQty(BigDecimal.ZERO);
            sfcSplit.setStopQty(BigDecimal.ZERO);
            sfcSplit.setDoneQty(BigDecimal.ZERO);
            sfcSplit.setScrapQty(BigDecimal.ZERO);
            sfcSplit.setModifyUser(userUtil.getUser().getRealName());
            sfcSplit.setModifyDate(new Date());
            sfcSplit.setCreateDate(new Date());
            // ??????????????????BO
            sfcSplit.setOperationBo(lastOperationBO);
            // ??????????????????
            sfcSplit.setState("?????????");
            //?????????sfc_bo
            sfcSplit.setParentSfcBo(sfcObj.getBo());
            if (StringUtils.isBlank(sfcObj.getParentSfcBo())){//??????????????????????????????????????????????????????
                sfcSplit.setInitParentSfcBo(sfcObj.getBo());
            }else {
                sfcSplit.setInitParentSfcBo(sfcObj.getInitParentSfcBo());
            }
            //???????????????sfc????????????
            sfcMapper.insert(sfcSplit);
            /*// ?????????sfc???????????????
            sfcObj.setDoneQty(new BigDecimal(sfcObj.getDoneQty().intValue()-sfcSplitDto.getInSfcQty()));
            // ?????????sfc
            sfcMapper.updateById(sfcObj);*/
            Sfc sfcParent = new Sfc();
            sfcParent.setSfcQty(sfcObj.getSfcQty().subtract(new BigDecimal(sfcSplitDto.getInSfcQty())));
            sfcParent.setBo(sfcObj.getBo());
            sfcMapper.updateById(sfcParent);//?????????sfc??????
            //???????????????
            sendStationMsg(lastOperationBO,sfcObj.getWorkShopBo(),sfcSplit.getSfc());
        }
        return sfcSplit;
    }

    /**
     * ???????????????
     * @param operationBo
     * @param workShopBo
     * @param sfc
     */
    public void sendStationMsg(String operationBo,String workShopBo,String sfc){
        Map<String, Object> notice;
        Map<String, Object> paramsMap;
        String operationName = operationMapper.selectById(operationBo).getOperationName();
        //?????????????????????????????????
        List<String> userList = sfcMapper.listAllSendMsgUser(workShopMapper.selectById(workShopBo).getWorkShopDesc(), typeName);
        for (String user : userList) {
            notice = new HashMap<>();
            notice.put("code",code);
            paramsMap = new HashMap<>();
            paramsMap.put("sfc",sfc);
            paramsMap.put("operation_name",operationName);
            notice.put("params",paramsMap);
            notice.put("userId",user);
            noticeService.sendMessage(notice);//????????????
        }
    }
    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     * @param routerBo ????????????bo
     * @return
     */
    public List<String> getRouterOrderList(String routerBo){
        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", routerBo));
        String processInfo = routerProcess.getProcessInfo();
        JSONObject jsonObj = JSON.parseObject(processInfo);

        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        JSONArray lineList = JSONArray.parseArray(jsonObj.getString("lineList"));

        List<String> orderList = Lists.newArrayList();//??????????????????
        //?????????????????????????????????id
        String startId = getStartId(nodeList);
        //????????????id??????????????????????????????id
        String nextId = getNextId(lineList, startId);
        //??????id??????????????????
        String operationBo = getOperationById(nodeList, nextId);//???????????????
        orderList.add(operationBo);

        while (StringUtils.isNotBlank(operationBo)){
            nextId = getNextId(lineList, nextId);
            operationBo = getOperationById(nodeList, nextId);
            if (StringUtils.isNotBlank(operationBo)){
                orderList.add(operationBo);
            }
        }
        return orderList;
    }

    /**
     * ????????????id????????????
     * @param nodeList
     * @param nextId
     * @return
     */
    public String getOperationById(JSONArray nodeList, String nextId) {
        String operation = null;
        if (nodeList.size() > 0){
            for (int i = 0; i < nodeList.size(); i++) {
                String id = JSON.parseObject(nodeList.get(i).toString()).getString("id");
                if (id.equals(nextId)){
                    operation = JSON.parseObject(nodeList.get(i).toString()).getString("operation");
                }
            }
        }
        return operation;
    }

    /**
     * ??????????????????????????????ID
     * @param nodeList
     * @param operationBo
     * @return
     */
    public String getIdByOperation(JSONArray nodeList,String operationBo){
        String id = null;
        if (nodeList.size() > 0){
            for (int i = 0; i < nodeList.size(); i++) {
                String operation = JSON.parseObject(nodeList.get(i).toString()).getString("operation");
                if (operationBo.equals(operation)){
                    id = JSON.parseObject(nodeList.get(i).toString()).getString("id");
                }
            }
        }
        return id;
    }

    /**
     * ??????????????????????????????ID??????????????????
     * @return
     */
    public List<String> getParallelIdByOperation(JSONArray nodeList,String operationBo){
        List<String> opIds = Lists.newArrayList();
        if (nodeList.size() > 0){
            for (int i = 0; i < nodeList.size(); i++) {
                String operation = JSON.parseObject(nodeList.get(i).toString()).getString("operation");
                if (operationBo.equals(operation)){
                    opIds.add(JSON.parseObject(nodeList.get(i).toString()).getString("id"));
                }
            }
        }
        return opIds;
    }
    /**
     * ??????????????????ID?????????????????????ID
     * @param lineList
     * @param startId
     * @return
     */
    public String getNextId(JSONArray lineList, String startId) {
        String to = null;
        if (lineList.size() > 0){
            for (int i = 0; i < lineList.size(); i++) {
                String from = JSON.parseObject(lineList.get(i).toString()).getString("from");
                if (from.equals(startId)){
                    to = JSON.parseObject(lineList.get(i).toString()).getString("to");
                    break;
                }
            }
        }
        return to;
    }

    /**
     * ?????????????????????ID
     * @param nodeList
     * @return
     */
    public String getStartId(JSONArray nodeList) {
        String id = null;
        if (nodeList.size() > 0){
            for (int i = 0; i < nodeList.size(); i++) {
                String ico = JSON.parseObject(nodeList.get(i).toString()).getString("ico");
                if ("craftRouteStart".equals(ico)){
                    id = JSON.parseObject(nodeList.get(i).toString()).getString("id");
                    break;
                }
            }
        }
        return id;
    }

    /**
     * ??????????????????
     * @param sfcWiplogs
     * @return
     */
    public int calculationDoneQty(List<SfcWiplog> sfcWiplogs){
        int doneQtySum=0;
        for(SfcWiplog sfcWiplog:sfcWiplogs){
            if(sfcWiplog.getDoneQty() !=null){
                doneQtySum+=sfcWiplog.getDoneQty().intValue();
            }
        }
        return doneQtySum;
    }

    /**
     * ????????????????????????????????????????????????
     * @param inSfcQty
     * @param sfcList
     * @return
     */
    public int calculationSfcQty(int inSfcQty,List<Sfc> sfcList){
        int sfcQtySum=inSfcQty;
        if(sfcList !=null && sfcList.size()>0){
            for(Sfc sfc:sfcList){
                if(sfc.getSfcQty() !=null){
                    sfcQtySum+=sfc.getSfcQty().intValue();
                }
            }
        }
        return sfcQtySum;
    }

    @Override
    public void okReworkNew(List<ReworkDto> reworkDto) throws CommonException {
        String sfc = reworkDto.get(0).getSfc();
        String operationBo = reworkDto.get(0).getOperationBo();//?????????????????????
        UserInfo userInfo = getUserInfo();
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfc));
        if (Sfc.WORK_SHOP_BO_XQ.equals(sfcObj.getWorkShopBo())){//??????????????????
            reworkNewByXQ(reworkDto,sfc,operationBo,sfcObj,userInfo);
        }else {//??????????????????
            BigDecimal doneQty = BigDecimal.ZERO;//??????ok?????????
            BigDecimal scrapQty = BigDecimal.ZERO;//???????????????
            for (ReworkDto dto : reworkDto) {
                SfcRepair sfcRepair = new SfcRepair();
                BeanUtils.copyProperties(sfcObj,sfcRepair);
                sfcRepair.setBo(UUID.uuid32());
                BeanUtils.copyProperties(dto,sfcRepair);
                sfcRepair.setRepairTime(new Date());
                if (StringUtils.isNotBlank(dto.getDutyUser()))sfcRepair.setDutyUser(dto.getDutyUser());
                else sfcRepair.setDutyUser(dto.getUserBo());

                sfcRepairMapper.insert(sfcRepair);
                doneQty = doneQty.add(dto.getRepairQty());
                scrapQty = scrapQty.add(dto.getScrapQty());

                //????????????
                ReportWork reportWork = new ReportWork();
                BeanUtils.copyProperties(sfcObj,reportWork);
                String workStepBo = stationService.getOne(new QueryWrapper<Station>().eq("station", userInfo.getStation())).getWorkstationBo();
                reportWork.setWorkStepCodeBo(workStepBo);
                reportWork.setQty(dto.getRepairQty());
                reportWork.setMeSfcWipLogBo(dto.getWipLogBo());
                reportWork.setState("0");
                reportWork.setTime(new Date());
                reportWork.setBo(UUID.uuid32());
                reportWork.setCreateTime(new Date());
                reportWork.setOperationBo(operationBo);
                reportWork.setUserBo(userInfo.getUserBo());
                reportWorkMapper.insert(reportWork);
            }
            //???wip_log????????????????????????
            insertWipLog(operationBo, userInfo, sfcObj, doneQty, scrapQty);

            //???me_sfc_nc_log????????????????????????????????????1???
            SfcNcLog sfcNcLog = new SfcNcLog();
            sfcNcLog.setIsRepaired("1");
            QueryWrapper<SfcNcLog> sfcNcLogQueryWrapper = new QueryWrapper<>();
            sfcNcLogQueryWrapper.eq("sfc",sfc);
            sfcNcLogQueryWrapper.eq("operation_bo",operationBo);
            sfcNcLogMapper.update(sfcNcLog,sfcNcLogQueryWrapper);

            //?????????sfc?????????????????????????????????
            List<String> routerOrderList = getRouterOrderList(sfcObj.getSfcRouterBo());
            String testOp = routerOrderList.get(routerOrderList.size() - 1);//????????????
            BigDecimal testOpOutQtyAndScrapTotalQty = productionExecuteServiceImpl.getTestOpOutQtyAndScrapTotalQty(sfc,testOp);//??????SFC??????????????????
            //???????????????
            if (scrapQty.compareTo(BigDecimal.ZERO) != 0){
                productionExecuteServiceImpl.updateScrapQtyBySfc(sfcObj.getScrapQty(),scrapQty,sfcObj.getSfc());
            }
            checkSfcDoneByOperation(sfc,routerOrderList);
            if (sfcObj.getSfcQty().compareTo(testOpOutQtyAndScrapTotalQty) == 0){
                Sfc sfcEntity = new Sfc();
                sfcEntity.setState(Sfc.F_STATE);
                sfcEntity.setOperationBo(testOp);
                sfcMapper.update(sfcEntity,new QueryWrapper<Sfc>().eq("sfc",sfc));
                productionExecuteServiceImpl.updateTrackBySfc(sfc);
            }
        }
    }

    /**
     * ??????sfc???????????????????????????
     */
    private void checkSfcDoneByOperation(String sfc,List<String> routerOrderList) throws CommonException {
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfc));
        QueryWrapper<RouterProcessTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("router_bo",sfcObj.getSfcRouterBo());
        queryWrapper.orderByAsc("position_flag");
        List<RouterProcessTable> operationList = routerProcessTableMapper.selectList(queryWrapper);//??????????????????
        BigDecimal splitQty = productionExecuteServiceImpl.getSplitQty(sfcObj.getBo());//???sfc???????????????
        BigDecimal totalQty;
        BigDecimal qty = sfcObj.getSfcQty().add(splitQty);
        for (int i = 0; i < operationList.size(); i++) {
            String operationBo = operationList.get(i).getOperationBo();
            BigDecimal currentOpOutQty = productionExecuteServiceImpl.getCurrentOpOutQty(sfc,operationBo);
            if (i == 0){//?????????
                totalQty = currentOpOutQty.add(sfcObj.getSkipBeforeScrapQty());
            }else {
                //???????????????????????????????????????????????????
                BigDecimal currentOpPreviousScrapQty = productionExecuteServiceImpl.getCurrentOpPreviousScrapQty(sfcObj,operationBo);
                totalQty = currentOpOutQty.add(currentOpPreviousScrapQty);
            }
            if (totalQty.compareTo(qty) == 0){
                if (!isNcBySfcAndOp(sfc,operationBo)){//????????????????????????????????????????????????
                    productionExecuteServiceImpl.updateTrackInfo(sfc, operationBo,"3");//??????????????????
                    productionExecuteServiceImpl.updateOperationBySfc(sfcObj,operationBo);
                    //???????????????????????????????????????
                    String lastOperationBo = routerOrderList.get(routerOrderList.size() - 2);//????????????????????????
                    if (lastOperationBo.equals(operationBo)){
                        //???????????????
                        sendStationMsg(routerOrderList.get(routerOrderList.size() - 1),sfcObj.getWorkShopBo(),sfcObj.getSfc());
                    }
                }
            }
        }
    }

    /**
     * ?????????sfc?????????????????????????????????
     * @param sfc
     * @param operationBo
     * @return
     */
    private boolean isNcBySfcAndOp(String sfc,String operationBo){
        List<SfcNcLog> sfcNcLogs = sfcNcLogMapper.selectList(new QueryWrapper<SfcNcLog>().
                eq("sfc", sfc).
                eq("operation_bo", operationBo).
                eq("is_repaired", "0"));
        if (CollectionUtil.isNotEmpty(sfcNcLogs)){
            return true;
        }
        return false;
    }
    private void insertWipLog(String operationBo, UserInfo userInfo, Sfc sfcObj, BigDecimal doneQty, BigDecimal scrapQty) {
        SfcWiplog sfcWiplog = new SfcWiplog();
        BeanUtils.copyProperties(sfcObj,sfcWiplog);
        sfcWiplog.setBo(UUID.uuid32());
        sfcWiplog.setInTime(new Date());
        sfcWiplog.setOutTime(new Date());
        sfcWiplog.setInputQty(BigDecimal.ZERO);
        sfcWiplog.setDoneQty(doneQty);
        sfcWiplog.setNcQty(BigDecimal.ZERO);
        sfcWiplog.setScrapQty(scrapQty);
        sfcWiplog.setState("??????");
        sfcWiplog.setUserBo(userInfo.getUserBo());
        sfcWiplog.setOperationBo(operationBo);
        sfcWiplog.setStationBo(userInfo.getStationBo());
        sfcWiplogMapper.insert(sfcWiplog);
    }

    /**
     * ??????????????????
     */
    private void reworkNewByXQ(List<ReworkDto> reworkDto,String sfc,String operationBo,Sfc sfcObj,UserInfo userInfo){
        BigDecimal doneQty = BigDecimal.ZERO;//??????ok?????????
        BigDecimal scrapQty = BigDecimal.ZERO;//???????????????
        for (ReworkDto dto : reworkDto) {
            SfcRepair sfcRepair = new SfcRepair();
            BeanUtils.copyProperties(sfcObj,sfcRepair);
            sfcRepair.setBo(UUID.uuid32());
            BeanUtils.copyProperties(dto,sfcRepair);
            sfcRepair.setRepairTime(new Date());
            sfcRepairMapper.insert(sfcRepair);
            doneQty = doneQty.add(dto.getRepairQty());
            scrapQty = scrapQty.add(dto.getScrapQty());
        }
        insertWipLog(operationBo, userInfo, sfcObj, doneQty, scrapQty);
        //???me_sfc_nc_log????????????????????????????????????1???
        SfcNcLog sfcNcLog = new SfcNcLog();
        sfcNcLog.setIsRepaired("1");
        QueryWrapper<SfcNcLog> sfcNcLogQueryWrapper = new QueryWrapper<>();
        sfcNcLogQueryWrapper.eq("sfc",sfc);
        sfcNcLogQueryWrapper.eq("operation_bo",operationBo);
        sfcNcLogMapper.update(sfcNcLog,sfcNcLogQueryWrapper);

        //??????Sfc?????????????????????????????????????????????????????????
        Sfc sfcEntity = new Sfc();
        sfcEntity.setState("?????????");
        sfcEntity.setNcQty(BigDecimal.ZERO);
        sfcEntity.setScrapQty(sfcObj.getScrapQty().add(scrapQty));
        sfcMapper.update(sfcEntity,new QueryWrapper<Sfc>().eq("sfc",sfc));
    }
    @Override
    public Sfc getInSplitInfo(String sfc) throws CommonException{

        BigDecimal canSplitQty = getCanSplitQtyBySfc(sfc);//?????????????????????
        if (canSplitQty.compareTo(BigDecimal.ZERO) == 0){
            throw new CommonException("???sfc????????????????????????",504);
        }
        Sfc sfcObj = sfcMapper.selectBySfc(sfc);
        if (Sfc.F_STATE.equals(sfcObj.getState())){
            throw new CommonException("???sfc?????????",504);
        }
        sfcObj.setDoneQty(canSplitQty);
        return sfcObj;
    }

    /**
     * ??????sfc????????????????????????
     * @param sfc
     * @return
     */
    private BigDecimal getCanSplitQtyBySfc(String sfc) throws CommonException {
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc",sfc));
        if (ObjectUtil.isEmpty(sfcObj)){
            throw new CommonException("???????????????sfc????????????",504);
        }

        BigDecimal doneQty = BigDecimal.ZERO;//?????????sfc?????????????????????????????????
        BigDecimal splitQty = BigDecimal.ZERO;//????????????????????????
        BigDecimal testDoneQty = BigDecimal.ZERO;//????????????????????????
        BigDecimal testScrapQty = BigDecimal.ZERO;//????????????????????????
        BigDecimal canSplitQty;//???????????????

        if (StringUtils.isBlank(sfcObj.getParentSfcBo())){//?????????????????????
            List<String> orderOpList = getRouterOrderList(sfcObj.getSfcRouterBo());//????????????????????????
            String theLastPassOperation = orderOpList.stream().skip(orderOpList.size() - 2).findFirst().orElse("no last element");//??????????????????????????????
            List<SfcWiplog> sfcWiplogs = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().eq("sfc", sfc).eq("operation_bo", theLastPassOperation));

            for (SfcWiplog sfcWiplog : sfcWiplogs) {
                doneQty = doneQty.add(sfcWiplog.getDoneQty());
            }
            //???sfc?????????????????????
            List<Sfc> sfcSplitList = sfcMapper.selectList(new QueryWrapper<Sfc>().eq("init_parent_sfc_bo", sfcObj.getBo()));


            for (Sfc sfcSplit : sfcSplitList) {
                splitQty = splitQty.add(sfcSplit.getSfcQty());
            }
            String testOp = orderOpList.get(orderOpList.size()-1);
            List<SfcWiplog> testWipLogs = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().eq("sfc", sfc).eq("operation_bo", testOp));
            for (SfcWiplog testWipLog : testWipLogs) {
                testDoneQty = testDoneQty.add(testWipLog.getDoneQty());
                testScrapQty = testScrapQty.add(testWipLog.getScrapQty());
            }
            canSplitQty = doneQty.subtract(splitQty).subtract(testDoneQty).subtract(testScrapQty);
        }else {
            canSplitQty = sfcObj.getSfcQty();
        }
        return canSplitQty;
    }
}
