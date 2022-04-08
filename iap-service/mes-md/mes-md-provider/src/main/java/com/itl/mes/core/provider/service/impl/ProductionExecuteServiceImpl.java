package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.UUID;
import com.itl.iap.notice.client.NoticeService;
import com.itl.mes.core.api.dto.OutStationDto;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.SfcVo;
import com.itl.mes.core.provider.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class, CommonException.class})
public class ProductionExecuteServiceImpl implements ProductionExecuteService {

    @Autowired
    private SfcMapper sfcMapper;

    @Autowired
    private FeedInService feedInService;

    @Autowired
    private SfcNcLogMapper sfcNcLogMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private StationService stationService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SfcWiplogMapper sfcWiplogMapper;

    @Autowired
    private SfcRepairMapper sfcRepairMapper;

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
    private MyDeviceMapper myDeviceMapper;

    @Autowired
    private EnterMapper enterMapper;

    @Autowired
    private RouterProcessMapper routerProcessMapper;

    @Autowired
    private OperationOrderService operationOrderService;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private QualityCheckListMapper qualityCheckListMapper;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private WorkShopMapper workShopMapper;

    @Autowired
    private RouterProcessService routerProcessService;

    @Autowired
    private SfcStateTrackMapper sfcStateTrackMapper;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SfcService sfcService;

    @Autowired
    private RouterProcessTableMapper routerProcessTableMapper;

    @Autowired
    private SfcSkipBeforeOperationMapper sfcSkipBeforeOperationMapper;
    /**
     * 根据sfc条码带出相应进站信息
     * @param sfc
     * @return
     */
    @Override
    public SfcVo getSfcInfoBySfcIn(String sfc) throws CommonException{
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfc));
        if (ObjectUtil.isEmpty(sfcObj)){
            throw new CommonException("该sfc不存在",504);
        }
        SfcVo sfcVo = new SfcVo();
        BeanUtils.copyProperties(sfcObj,sfcVo);
        //计算进站数量，inputQty = sfc_qty - scrap_qty - nc_qty - done_qty
        BigDecimal inputQty = sfcObj.getSfcQty().subtract(sfcObj.getScrapQty()).subtract(sfcObj.getNcQty()).subtract(sfcObj.getDoneQty());
        sfcVo.setInputQty(inputQty);
        Item item = itemService.getOne(new QueryWrapper<Item>().eq("bo", sfcObj.getItemBo()));
        sfcVo.setItem(item.getItem());
        sfcVo.setItemName(item.getItemName());
        sfcVo.setItemDesc(item.getItemDesc());
        ShopOrder shopOrder = shopOrderService.getOne(new QueryWrapper<ShopOrder>().eq("bo", sfcObj.getShopOrderBo()));
        sfcVo.setShopOrder(shopOrder.getShopOrder());
        return sfcVo;
    }

    /**
     * 正常SFC进站
     * @param sfcDto
     * @throws CommonException
     */
    @Override
    public void enterStation(SfcDto sfcDto) throws CommonException {
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfcDto.getSfc()));
        UserInfo userInfo = sfcServiceImpl.getUserInfo();//获取人员信息

        String stateBo = shopOrderService.getOne(new QueryWrapper<ShopOrder>().eq("bo", sfcObj.getShopOrderBo())).getStateBo();
        //可出站数
        BigDecimal canOutStationQty = sfcObj.getSfcQty().subtract(sfcObj.getDoneQty()).subtract(sfcObj.getNcQty()).subtract(sfcObj.getScrapQty());
        if (!sfcObj.getOperationBo().contains(userInfo.getOperationBo())){
            if (canOutStationQty.compareTo(BigDecimal.ZERO) == 1){//如果sfc进站数还大于0，说明还没出站完
                String operationName = operationMapper.selectOne(new QueryWrapper<Operation>().eq("bo", sfcObj.getOperationBo())).getOperationName();
                throw new CommonException("该批次在" + operationName + "工序还在生产中,还剩" + canOutStationQty + "个没出站完成，请联系班组长", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else {
                throw new CommonException("该批次不在当前工序，不能进站!", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        if (sfcObj.getState().equals("维修中") || sfcObj.getState().equals("已完成")){
            throw new CommonException("SFC只有在新建、排队中、暂停、生产中状态才能进站!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if ("STATE:dongyin,502".equals(stateBo)){
            throw new CommonException("该sfc的工单已关闭，无法进站！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }


        Sfc sfc = new Sfc();
        sfc.setStationBo(userInfo.getStationBo());
        sfc.setUserBo(userInfo.getUserBo());
        sfc.setShitBo(null);//暂定
        sfc.setTeamBo(null);//暂定
        if (!sfcObj.getState().equals("新建")){
            sfc.setState("生产中");
        }
        sfc.setInputQty(BigDecimal.ZERO);
        if (sfcObj.getState().equals("排队中")){
            sfc.setDoneQty(BigDecimal.ZERO);
        }
        Date inTime=new Date();
        sfc.setInTime(inTime);

        String currentOp = "";//当前工序
        //解析工序bo（如果是并行节点可能存在多条，需要拿到那条工序的流程id）
        if (sfcObj.getOperationBo().contains("|")){//说明存在多条
            String[] opSplit = sfcObj.getOperationBo().split("\\|");
            String[] idSplit = sfcObj.getOpIds().split("\\|");
            String currentId = "";//当前工序流程ID
            for (int i = 0; i < opSplit.length; i++) {
                if (opSplit[i].equals(userInfo.getOperationBo())){
                    currentOp = opSplit[i];
                    for (int j = 0; j < idSplit.length; j++) {
                        currentId = idSplit[i];
                        break;
                    }
                    break;
                }
            }
            sfc.setOperationBo(currentOp);
            sfc.setOpIds(currentId);
        }else {
            sfc.setOperationBo(userInfo.getOperationBo());
            sfc.setOpIds(sfcObj.getOpIds());
        }
        //处理设备任务设备对应表
        for (String deviceBo : sfcDto.getDeviceList()) {
            SfcDevice sfcDeviceEntity = sfcDeviceMapper.selectById(deviceBo);

            SfcDevice sfcDevice = new SfcDevice();
            sfcDevice.setDeviceBo(deviceBo);
            sfcDevice.setSfc(sfcObj.getSfc());
            sfcDevice.setShopOrderBo(sfcObj.getShopOrderBo());
            sfcDevice.setCreateDate(new Date());
            sfcDevice.setOperationOrder(sfcObj.getOperationOrder());
            sfcDevice.setUserBo(userInfo.getUserBo());
            sfcDevice.setDispatchCode(sfcObj.getDispatchCode());

            if (ObjectUtil.isEmpty(sfcDeviceEntity)){
                sfcDeviceMapper.insert(sfcDevice);
            }else {
                if (StringUtils.isNotBlank(sfcDeviceEntity.getSfc())){//如果当前设备上挂有SFC
                    String device = deviceBo.substring(deviceBo.substring(0, deviceBo.indexOf(",")).length() + 1);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
                    throw new CommonException("设备编号为" + device + "的设备上有" + sfcDeviceEntity.getSfc() + "的SFC在生产", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                sfcDeviceMapper.updateById(sfcDevice);
            }
        }
        sfcMapper.update(sfc,new QueryWrapper<Sfc>().eq("sfc",sfcDto.getSfc()));
        // 将数据插入进站表与校验工单是否已经首检
        for(String str:sfcDto.getDeviceList()){
            String operationBo=null;
            if (sfcObj.getOperationBo().contains("|")){//说明存在多条
                operationBo=currentOp;
            }else {
                operationBo=userInfo.getOperationBo();
            }
            Device device=deviceService.getDeviceById(str);
            // 将进站数据插入进站表
            Enter enter=new Enter();
            enter.setBo(UUID.uuid32());
            enter.setDeviceBo(str);
            enter.setSfc(sfcObj.getSfc());
            enter.setInTime(inTime);
            enter.setOperationBo(operationBo);
            enter.setState("0");
            enter.setShopOrderBo(sfcObj.getShopOrderBo());
            enter.setOperationOrder(sfcObj.getOperationOrder());
            enter.setUserBo(userInfo.getUserBo());
            enterMapper.insert(enter);

            //验证工单是否已首检
            if(device !=null && device.getDevice() !=null){
                String shopOrder = shopOrderService.getById(sfcObj.getShopOrderBo()).getShopOrder();
                List<QualityCheckList> qualityCheckLists=qualityCheckListMapper.selectQualityInfo(device.getDevice(),operationBo,shopOrder);
                if(qualityCheckLists == null || qualityCheckLists.size()==0){
                    // 不存在已首检的当前工单,重置首检状态为未首检
                    myDeviceMapper.updateFirstInsState(userInfo.getUserId(),userInfo.getStationBo(),device.getBo());
                }
            }

        }
        //把工序工单状态更新为生产中
        OperationOrder operationOrder = new OperationOrder();
        operationOrder.setOperationOrderState("1");
        operationOrderService.update(operationOrder,new QueryWrapper<OperationOrder>().eq("operation_order",sfcObj.getOperationOrder()));
    }

    /**
     * 首工序进站
     * @param sfcDto
     * @throws CommonException
     */
    @Override
    public synchronized void enterStationByFirstOperation(SfcDto sfcDto) throws CommonException {
        UserInfo userInfo = sfcServiceImpl.getUserInfo();
        String shopOrderBo = "SO:dongyin," + sfcDto.getShopOrder();
        ShopOrder shopOrder = shopOrderService.getById(shopOrderBo);
        //验证该工单的首工序是否是当前工序
        boolean first = sfcServiceImpl.checkIsFirstOperation(shopOrder.getRouterBo(),userInfo.getOperationBo());
        if (!first){
            throw new CommonException("当前工序不是该工单的首工序，请切换工位", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //把工序工单状态更新为生产中
        OperationOrder operationOrder = new OperationOrder();
        operationOrder.setOperationOrderState("1");
        operationOrderService.update(operationOrder,new QueryWrapper<OperationOrder>().eq("operation_order",sfcDto.getOperationOrder()));

        for (String deviceBo : sfcDto.getDeviceList()) {
            //处理设备任务设备对应表
            SfcDevice sfcDeviceEntity = sfcDeviceMapper.selectById(deviceBo);

            SfcDevice sfcDevice = new SfcDevice();
            sfcDevice.setDeviceBo(deviceBo);
            sfcDevice.setSfc("");
            sfcDevice.setShopOrderBo(shopOrderBo);
            sfcDevice.setCreateDate(new Date());
            sfcDevice.setOperationOrder(sfcDto.getOperationOrder());
            sfcDevice.setUserBo(userInfo.getUserBo());
            sfcDevice.setDispatchCode(sfcDto.getDispatchCode());
            sfcDevice.setOperationBo(userInfo.getOperationBo());

            if (ObjectUtil.isEmpty(sfcDeviceEntity)){
                sfcDeviceMapper.insert(sfcDevice);
            }else {
                if (StringUtils.isNotBlank(sfcDeviceEntity.getSfc())){//如果当前设备上挂有SFC
                    String device = deviceBo.substring(deviceBo.substring(0, deviceBo.indexOf(",")).length() + 1);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
                    throw new CommonException("设备编号为" + device + "的设备上有" + sfcDeviceEntity.getSfc() + "的SFC在生产", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                sfcDeviceMapper.updateById(sfcDevice);
            }

            //记录首工序进站的时间
            Device device = new Device();
            device.setSfcInTime(new Date());
            deviceService.update(device,new QueryWrapper<Device>().eq("bo",deviceBo));
            saveStartTime(shopOrderBo);
            // 存进站记录表   m_enter
            Enter enter=new Enter();
            enter.setBo(UUID.uuid32());
            enter.setOperationBo(userInfo.getOperationBo());
            enter.setInTime(new Date());
            enter.setDeviceBo(deviceBo);
            enter.setState("1");
            enter.setShopOrderBo(shopOrderBo);
            enter.setOperationOrder(sfcDto.getOperationOrder());
            enter.setUserBo(userInfo.getUserBo());
            enterMapper.insert(enter);

            // 验证工单是否已首检
            Device deviceEntity=deviceService.getDeviceById(deviceBo);
            if(deviceEntity !=null && deviceEntity.getDevice() !=null){
                List<QualityCheckList> qualityCheckLists=qualityCheckListMapper.selectQualityInfo(deviceEntity.getDevice(),userInfo.getOperationBo(),sfcDto.getShopOrder());
                if(qualityCheckLists == null || qualityCheckLists.size()==0){
                    // 不存在已首检的当前工单,重置首检状态为未首检
                    myDeviceMapper.updateFirstInsState(userInfo.getUserId(),userInfo.getStationBo(),deviceBo);
                }
            }
        }
    }

    /**
     * 通过sfc编码获取sfc相关信息（出站、暂停）
     * @param outStationDto
     * @return
     * @throws CommonException
     */
    @Override
    public SfcVo getSfcInfoBySfcOut(OutStationDto outStationDto) throws CommonException {
        SfcVo sfcVo = sfcMapper.selectSfcInfo(outStationDto.getSfc());
        UserInfo userInfo = sfcServiceImpl.getUserInfo();
        List<Device> devices = Lists.newArrayList();
        BigDecimal inputQty = sfcVo.getSfcQty().subtract(sfcVo.getDoneQty()).subtract(sfcVo.getNcQty()).subtract(sfcVo.getScrapQty());
        //两种情况：检验工序（1）和过站工序（0）
        if (userInfo.getOperationType() == 0){//过站工序
            //这里又有两种情况：首工序或正常工序
            List<SfcDevice> canOutDevices;
            if (sfcVo.getState().equals("新建")){//首工序（这里指第一次出站的首工序，如果分多次出，该sfc状态会变为生产中，且input_qty数量也会变成正常生产中sfc一样）
                //根据工序工单查询可以出站的设备
                canOutDevices = sfcDeviceMapper.selectList(new QueryWrapper<SfcDevice>().eq("operation_order",sfcVo.getOperationOrder()));
            }else {//正常工序
                //根据sfc查询可以出站的设备
                canOutDevices = sfcDeviceMapper.selectList(new QueryWrapper<SfcDevice>().eq("sfc",sfcVo.getSfc()));
            }
            if (CollectionUtil.isEmpty(canOutDevices)){
                throw new CommonException("该sfc没有可出站的设备，请重新进站",504);
            }
            for (SfcDevice canOutDevice : canOutDevices) {
                Device device = deviceService.getById(canOutDevice.getDeviceBo());
                devices.add(device);
            }
            sfcVo.setDevices(devices);
            sfcVo.setInputQty(inputQty);
        }else {//检验工序
            sfcVo.setDevices(devices);
            sfcVo.setInputQty(sfcVo.getSfcQty().subtract(sfcVo.getScrapQty()));
        }

        return sfcVo;
    }

    /**
     * 出站逻辑
     * @param outStationDto
     * @throws CommonException
     */
    @Override
    public void outStation(OutStationDto outStationDto) throws CommonException {
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", outStationDto.getSfc()));
        UserInfo userInfo = sfcServiceImpl.getUserInfo();//获取人员信息
        if (!sfcObj.getOperationBo().equals(userInfo.getOperationBo())){
            throw new CommonException("SFC不在本工序生产，不能出站!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (userInfo.getOperationType() == 1){//如果为检验工序
            if (sfcObj.getState().equals("排队中")){
                sfcObj.setState("生产中");
            }else {
                throw new CommonException("在检验工序只有排队中的SFC能出站!", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        if (sfcObj.getState().equals("排队中") || sfcObj.getState().equals("维修中") || sfcObj.getState().equals("暂停")){
            throw new CommonException("非生产中、首工序的sfc不能出站!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        boolean canDisToDeviceOp = sfcServiceImpl.isCanDisToDeviceOp(userInfo.getOperationBo());//判断是否是需要派工至设备的工序

        BigDecimal deviceNcQty = BigDecimal.ZERO;//不良总数量
        BigDecimal deviceScrapQty = BigDecimal.ZERO;//报废总数量
        BigDecimal doneQty = BigDecimal.ZERO;//过站良品总数量

        boolean isFirstOp = false;//判断是否是首工序
        if (sfcObj.getState().equals("新建")){
            isFirstOp = true;
        }

        doneQty = handleTestOp(sfcObj, userInfo, doneQty);//处理检验工序情况

        for (OutStationDevice outStationDevice : outStationDto.getOutStationDevices()) {
            SfcWiplog sfcWiplog = insertSfcWiplog(sfcObj, isFirstOp, outStationDevice);//新增wip_log表出站信息

            BigDecimal currentDeviceNcQty = BigDecimal.ZERO;//当前设备不良数量
            BigDecimal currentDeviceScrapQty = BigDecimal.ZERO;//当前设备报废数量

            //处理不良和报废
            for (DeviceNc deviceNc : outStationDto.getDeviceNcs()) {
                if (outStationDevice.getDeviceBo().equals(deviceNc.getDeviceBo())){
                    if (deviceNc.getType().equals("0")){//不良
                        SfcNcLog sfcNcLog = new SfcNcLog();
                        BeanUtils.copyProperties(sfcObj,sfcNcLog);
                        sfcNcLog.setBo(UUID.uuid32());
                        sfcNcLog.setNcQty(deviceNc.getQty());
                        sfcNcLog.setNcCodeBo(deviceNc.getNcCodeBo());
                        sfcNcLog.setRemark(deviceNc.getMemo());
                        sfcNcLog.setWipLogBo(sfcWiplog.getBo());
                        sfcNcLog.setDeviceBo(outStationDevice.getDeviceBo());
                        sfcNcLog.setRecordTime(new Date());
                        sfcNcLogMapper.insert(sfcNcLog);
                        currentDeviceNcQty = currentDeviceNcQty.add(deviceNc.getQty());
                    }else if (deviceNc.getType().equals("1")){//报废
                        SfcRepair sfcRepair = new SfcRepair();
                        BeanUtils.copyProperties(sfcObj,sfcRepair);
                        sfcRepair.setBo(UUID.uuid32());
                        sfcRepair.setScrapQty(deviceNc.getQty());
                        sfcRepair.setWipLogBo(sfcWiplog.getBo());
                        sfcRepair.setRepairTime(new Date());
                        sfcRepair.setNcCodeBo(deviceNc.getNcCodeBo());
                        sfcRepair.setStationBo(userInfo.getStationBo());
                        sfcRepair.setDutyOperation(userInfo.getOperationBo());
                        sfcRepair.setDutyUser(userInfo.getUserBo());
                        sfcRepairMapper.insert(sfcRepair);
                        currentDeviceScrapQty = currentDeviceScrapQty.add(deviceNc.getQty());
                    }
                }
            }
            sfcWiplog.setNcQty(currentDeviceNcQty);
            sfcWiplog.setScrapQty(currentDeviceScrapQty);
            sfcWiplog.setInputQty(outStationDevice.getOutStationQty().add(currentDeviceNcQty).add(currentDeviceScrapQty));
            sfcWiplogMapper.updateById(sfcWiplog);

            deviceNcQty = deviceNcQty.add(currentDeviceNcQty);
            deviceScrapQty = deviceScrapQty.add(currentDeviceScrapQty);
            doneQty = doneQty.add(outStationDevice.getOutStationQty());

            checkNeedDisOpOutQty(sfcObj, canDisToDeviceOp, outStationDevice, currentDeviceNcQty, currentDeviceScrapQty);//校验需要派工至设备的出站数量是否大于设备的派工数量
//            outStationReportWork(sfcObj, userInfo, outStationDevice, sfcWiplog);//出站时候系统工步报工
            updateSfcDevice(sfcObj, outStationDevice);//出站时更新device_sfc设备对应表
//            BigDecimal allScrapQtyBySfc = getAllScrapQtyBySfc(sfcObj.getSfc());
            BigDecimal totalQty = outStationDevice.getOutStationQty().add(currentDeviceNcQty).add(currentDeviceScrapQty);
            updateWaitInQty(canDisToDeviceOp,sfcObj.getOperationOrder(),outStationDevice.getDevice(),totalQty);
        }

        Sfc sfc = updateSfc(sfcObj, deviceNcQty, deviceScrapQty, doneQty);//封装sfc对象
        feedInService.outStation(outStationDto);//出站扣料
        sfcMapper.updateById(sfc);//更新sfc

        handleSfcFinish(sfcObj, sfc);//处理sfc完成时情况
        handleOpOrderDone(sfcObj, deviceNcQty, deviceScrapQty, doneQty);//处理工序工单在这个工序的完成情况
    }

    /**
     * 查询该sfc目前全部报废数
     * @param sfc
     * @return
     */
    private BigDecimal getAllScrapQtyBySfc(String sfc){
        List<SfcWiplog> sfcWiplogs = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().eq("sfc", sfc));
        BigDecimal scrapQty = BigDecimal.ZERO;
        for (SfcWiplog sfcWiplog : sfcWiplogs) {
            scrapQty = scrapQty.add(sfcWiplog.getScrapQty());
        }
        return scrapQty;
    }
    /**
     * 更新wait_in数量
     */
    private void updateWaitInQty(boolean canDisToDeviceOp,String operationOrder,String device,BigDecimal outTotalQty) throws CommonException {
        QueryWrapper<Dispatch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operation_order", operationOrder);
        queryWrapper.eq("operation_bo", sfcServiceImpl.getUserInfo().getOperationBo());
        queryWrapper.eq("is_need_dispatch","1");
        if (canDisToDeviceOp){
            queryWrapper.eq("device",device);
        }
        queryWrapper.orderByDesc("create_date");
        List<Dispatch> dispatches = dispatchService.list(queryWrapper);
        BigDecimal minuend = outTotalQty;//被减数
        Dispatch dispatchObj = new Dispatch();
        for (Dispatch dispatch : dispatches) {
            if (dispatch.getWaitIn().compareTo(BigDecimal.ZERO) == 0){
                continue;
            }else {
                dispatchObj.setId(dispatch.getId());
                if (dispatch.getWaitIn().compareTo(minuend) == 1 || dispatch.getWaitIn().compareTo(minuend) == 0){
                    dispatchObj.setWaitIn(dispatch.getWaitIn().subtract(minuend));
                    dispatchService.updateById(dispatchObj);
                    break;
                }else {
                    dispatchObj.setWaitIn(new BigDecimal("0"));
                    dispatchService.updateById(dispatchObj);
                    minuend = minuend.subtract(dispatch.getWaitIn());
                    continue;
                }
            }
        }
    }
    /**
     * 出站时更新sfc_device表
     * @param sfcObj
     * @param outStationDevice
     */
    private void updateSfcDevice(Sfc sfcObj, OutStationDevice outStationDevice) throws CommonException {
        SfcDevice sfcDevice = sfcDeviceMapper.selectById(outStationDevice.getDeviceBo());
        if (StringUtils.isBlank(sfcDevice.getSfc())){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CommonException("该条码已经出站，请不要重复出站",504);
        }
        if (sfcDevice.getShopOrderBo().equals(sfcObj.getShopOrderBo())){//校验是否是这个工单
            SfcDevice sfcDeviceEntity = new SfcDevice();
            sfcDeviceEntity.setDeviceBo(outStationDevice.getDeviceBo());
            sfcDeviceEntity.setSfc("");
            sfcDeviceEntity.setShopOrderBo("");
            sfcDeviceEntity.setOperationOrder("");
            sfcDeviceEntity.setUserBo("");
            sfcDeviceEntity.setDispatchCode("");
            sfcDeviceEntity.setOperationBo("");
            sfcDeviceMapper.updateById(sfcDeviceEntity);
        }
    }

    /**
     * 出站时系统工步报工
     * @param sfcObj
     * @param userInfo
     * @param outStationDevice
     * @param sfcWiplog
     */
    private void outStationReportWork(Sfc sfcObj, UserInfo userInfo, OutStationDevice outStationDevice, SfcWiplog sfcWiplog,List<AutomaticReportWork> automaticReportWorkList) throws CommonException {
        boolean isAllStepReportWork = sfcServiceImpl.isCanAutomaticReportWork(userInfo.getOperationBo());
        if (isAllStepReportWork){//如果该工序是需要全部工步报工
            for (AutomaticReportWork automaticReportWork : automaticReportWorkList) {
                automaticReportWork(sfcObj, userInfo.getOperationBo(),
                        automaticReportWork.getQty(), sfcWiplog.getBo(),automaticReportWork.getUserBo(),automaticReportWork.getWorkStepBo());
            }
        }else {//否则只报当前工步
            automaticReportWork(sfcObj, userInfo.getOperationBo(),
                    outStationDevice.getOutStationQty(), sfcWiplog.getBo(),userInfo.getUserBo(),userInfo.getWorkStationBo());
        }
    }

    /**
     * 自动报工
     * @throws CommonException
     */
    public void automaticReportWork(Sfc sfcObj,String operationBo,BigDecimal outStationQty, String wipLogBo,String userBo,String stepBo) throws CommonException {
        ReportWork reportWork = new ReportWork();
        BeanUtils.copyProperties(sfcObj,reportWork);
        reportWork.setWorkStepCodeBo(stepBo);
        reportWork.setQty(outStationQty);
        if (stepBo.equals(sfcServiceImpl.getUserInfo().getWorkStationBo()))reportWork.setMeSfcWipLogBo(wipLogBo);
        reportWork.setState("0");
        reportWork.setTime(new Date());
        reportWork.setBo(UUID.uuid32());
        reportWork.setCreateTime(new Date());
        reportWork.setUserBo(userBo);
        reportWork.setOperationBo(operationBo);
        reportWorkMapper.insert(reportWork);
    }

    /**
     * 新增wip_log表出站信息
     * @param sfcObj
     * @param isFirstOp
     * @param outStationDevice
     * @return
     */
    private SfcWiplog insertSfcWiplog(Sfc sfcObj, boolean isFirstOp, OutStationDevice outStationDevice) throws CommonException {
        SfcWiplog sfcWiplog = new SfcWiplog();
        BeanUtils.copyProperties(sfcObj,sfcWiplog);
        sfcWiplog.setBo(UUID.uuid32());
        sfcWiplog.setOutTime(new Date());
        sfcWiplog.setInputQty(BigDecimal.ZERO);
        sfcWiplog.setDoneQty(outStationDevice.getOutStationQty());
        sfcWiplog.setNcQty(BigDecimal.ZERO);
        sfcWiplog.setScrapQty(BigDecimal.ZERO);
        sfcWiplog.setState("出站");
        sfcWiplog.setDeviceBo(outStationDevice.getDeviceBo());

        sfcWiplog.setOperationBo(sfcServiceImpl.getUserInfo().getOperationBo());
        sfcWiplog.setUserBo(sfcServiceImpl.getUserInfo().getUserBo());
        sfcWiplog.setStationBo(sfcServiceImpl.getUserInfo().getStationBo());
        Date inTime = sfcDeviceMapper.selectOne(new QueryWrapper<SfcDevice>().eq("device_bo", outStationDevice.getDeviceBo())).getCreateDate();
        sfcWiplog.setInTime(inTime);
        if (isFirstOp){
            Device device = deviceService.getOne(new QueryWrapper<Device>().eq("bo", outStationDevice.getDeviceBo()));
            sfcWiplog.setInTime(device.getSfcInTime());
        }
        sfcWiplogMapper.insert(sfcWiplog);
        return sfcWiplog;
    }

    /**
     * 校验需要派工至设备的出站数量是否大于设备的派工数量
     * @param sfcObj
     * @param canDisToDeviceOp
     * @param outStationDevice
     * @param currentDeviceNcQty
     * @param currentDeviceScrapQty
     * @throws CommonException
     */
    private void checkNeedDisOpOutQty(Sfc sfcObj, boolean canDisToDeviceOp, OutStationDevice outStationDevice, BigDecimal currentDeviceNcQty, BigDecimal currentDeviceScrapQty) throws CommonException {
        if (canDisToDeviceOp){
            if (sfcObj.getIsSkipStationSfc().equals("0")){//跳站过来的工序无需校验
                //判断是否是首工序
                boolean isTheFirstOp = sfcServiceImpl.checkIsFirstOperation(sfcObj.getSfcRouterBo(),sfcServiceImpl.getUserInfo().getOperationBo());
                if (sfcObj.getWorkShopBo().equals(Sfc.WORK_SHOP_BO_ZZ)){//转轴车间的话只校验首工序
                    if (isTheFirstOp){
                        checkDispatch(sfcObj, outStationDevice, currentDeviceNcQty, currentDeviceScrapQty);
                    }
                }else {//其他车间则都要校验
                    checkDispatch(sfcObj, outStationDevice, currentDeviceNcQty, currentDeviceScrapQty);
                }
            }
        }
    }

    private void checkDispatch(Sfc sfcObj, OutStationDevice outStationDevice, BigDecimal currentDeviceNcQty, BigDecimal currentDeviceScrapQty) throws CommonException {
        String device = deviceService.getOne(new QueryWrapper<Device>().eq("bo", outStationDevice.getDeviceBo())).getDevice();
        QueryWrapper<Dispatch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operation_order", sfcObj.getOperationOrder());
        queryWrapper.eq("operation_bo", sfcServiceImpl.getUserInfo().getOperationBo());
        queryWrapper.eq("device",device);
        queryWrapper.eq("is_need_dispatch","1");
        List<Dispatch> dispatches = dispatchService.list(queryWrapper);
        //派工至设备的总数
        BigDecimal dispatchTotalQty = BigDecimal.ZERO;
        for (Dispatch dispatch : dispatches) {
            dispatchTotalQty = dispatchTotalQty.add(dispatch.getDispatchQty());
        }
        BigDecimal outStationTotalQty = outStationDevice.getOutStationQty().add(currentDeviceNcQty).add(currentDeviceScrapQty);//当前设备出站总数
        if (outStationTotalQty.compareTo(dispatchTotalQty) == 1){//如果当前设备出站总数大于派工至设备的数
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CommonException("设备编码为" + device + "的设备派工至设备数量为" + dispatchTotalQty + ",当前设备出站数大于它", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
    }

    /**
     * 处理检验工序情况
     * @param sfcObj
     * @param userInfo
     * @param doneQty
     * @return
     */
    private BigDecimal handleTestOp(Sfc sfcObj, UserInfo userInfo, BigDecimal doneQty) {
        if (userInfo.getOperationType() == 1){//如果为检验工序,直接在出站日志表插入一条数据，完成数为sfc数量，没有不良和报废
            BigDecimal outQty = sfcObj.getSfcQty().subtract(sfcObj.getScrapQty());//检验工序实际出站数

            SfcWiplog sfcWiplogEntity = new SfcWiplog();
            BeanUtils.copyProperties(sfcObj,sfcWiplogEntity);
            sfcWiplogEntity.setBo(UUID.uuid32());
            sfcWiplogEntity.setOutTime(new Date());
            sfcWiplogEntity.setInputQty(outQty);
            sfcWiplogEntity.setDoneQty(outQty);
            sfcWiplogEntity.setNcQty(BigDecimal.ZERO);
            sfcWiplogEntity.setScrapQty(BigDecimal.ZERO);
            sfcWiplogEntity.setState("出站");
            sfcWiplogMapper.insert(sfcWiplogEntity);

            doneQty = doneQty.add(outQty);//过站良品数
        }
        return doneQty;
    }

    /**
     * 处理检验工序
     * @param sfcObj
     * @param doneQty   检验出站良品数
     * @param deviceNcs 检验报废数
     * @throws CommonException
     */
    private BigDecimal handleTestOpNew(Sfc sfcObj, BigDecimal doneQty,List<DeviceNc> deviceNcs) throws CommonException {
        String operationBo = sfcServiceImpl.getUserInfo().getOperationBo();
        String stationBo = sfcServiceImpl.getUserInfo().getStationBo();
        String userBo = sfcServiceImpl.getUserInfo().getUserBo();
        Date date = new Date();
        SfcWiplog sfcWiplogEntity = new SfcWiplog();
        BeanUtils.copyProperties(sfcObj,sfcWiplogEntity);
        sfcWiplogEntity.setBo(UUID.uuid32());
        sfcWiplogEntity.setOutTime(date);
        sfcWiplogEntity.setInputQty(doneQty);
        sfcWiplogEntity.setDoneQty(doneQty);
        sfcWiplogEntity.setNcQty(BigDecimal.ZERO);
        sfcWiplogEntity.setScrapQty(BigDecimal.ZERO);
        sfcWiplogEntity.setState("出站");
        sfcWiplogEntity.setOperationBo(operationBo);
        sfcWiplogEntity.setStationBo(stationBo);
        sfcWiplogEntity.setInTime(date);
        sfcWiplogEntity.setUserBo(userBo);
        sfcWiplogMapper.insert(sfcWiplogEntity);

        BigDecimal scrapQty = BigDecimal.ZERO;//报废数
        BigDecimal ncQty = BigDecimal.ZERO;//不良数
        //处理检验报废
        for (DeviceNc deviceNc : deviceNcs) {
            if (Sfc.WORK_SHOP_BO_XQ.equals(sfcObj.getWorkShopBo())){//如果是线圈车间，可以记录不良和报废
                if ("0".equals(deviceNc.getType())){//不良
                    insertNcLog(sfcObj, operationBo, stationBo, sfcWiplogEntity, deviceNc);
                    ncQty = ncQty.add(deviceNc.getQty());
                }else {//报废
                    insertRepair(sfcObj, operationBo, stationBo, userBo, sfcWiplogEntity, deviceNc);
                    scrapQty = scrapQty.add(deviceNc.getQty());
                }
            }else {//其他车间只能记录报废
                insertRepair(sfcObj, operationBo, stationBo, userBo, sfcWiplogEntity, deviceNc);
                scrapQty = scrapQty.add(deviceNc.getQty());
            }
        }
        if (scrapQty.compareTo(BigDecimal.ZERO) != 0 || ncQty.compareTo(BigDecimal.ZERO) != 0){
            sfcWiplogEntity.setScrapQty(scrapQty);
            sfcWiplogEntity.setNcQty(ncQty);
            sfcWiplogMapper.updateById(sfcWiplogEntity);
            updateScrapQtyBySfc(sfcObj.getScrapQty(),scrapQty,sfcObj.getSfc());
            updateNcQtyBySfc(sfcObj.getSfc(),ncQty);
        }
        return ncQty;
    }
    private void updateNcQtyBySfc(String sfc,BigDecimal ncQty){
        if (ncQty.compareTo(BigDecimal.ZERO) != 0){
            Sfc sfcEntity = new Sfc();
            sfcEntity.setNcQty(ncQty);
            sfcEntity.setState("维修中");
            sfcMapper.update(sfcEntity,new QueryWrapper<Sfc>().eq("sfc",sfc));
        }
    }
    private void insertRepair(Sfc sfcObj, String operationBo, String stationBo, String userBo, SfcWiplog sfcWiplogEntity, DeviceNc deviceNc) {
        SfcRepair sfcRepair;
        sfcRepair = new SfcRepair();
        BeanUtils.copyProperties(sfcObj,sfcRepair);
        sfcRepair.setBo(UUID.uuid32());
        sfcRepair.setScrapQty(deviceNc.getQty());
        sfcRepair.setWipLogBo(sfcWiplogEntity.getBo());
        sfcRepair.setRepairTime(new Date());
        sfcRepair.setNcCodeBo(deviceNc.getNcCodeBo());
        sfcRepair.setDutyOperation(operationBo);
        sfcRepair.setDutyUser(userBo);
        sfcRepair.setStationBo(stationBo);
        sfcRepairMapper.insert(sfcRepair);
    }

    private void insertNcLog(Sfc sfcObj, String operationBo, String stationBo, SfcWiplog sfcWiplogEntity, DeviceNc deviceNc) {
        SfcNcLog sfcNcLog;
        sfcNcLog = new SfcNcLog();
        BeanUtils.copyProperties(sfcObj,sfcNcLog);
        sfcNcLog.setBo(UUID.uuid32());
        sfcNcLog.setNcQty(deviceNc.getQty());
        sfcNcLog.setNcCodeBo(deviceNc.getNcCodeBo());
        sfcNcLog.setRemark(deviceNc.getMemo());
        sfcNcLog.setWipLogBo(sfcWiplogEntity.getBo());
        sfcNcLog.setRecordTime(new Date());
        sfcNcLog.setOperationBo(operationBo);
        sfcNcLog.setStationBo(stationBo);
        sfcNcLogMapper.insert(sfcNcLog);
    }

    /**
     * 更新wait_in数量
     * @param sfcObj
     * @param deviceNcQty
     * @param deviceScrapQty
     * @param doneQty
     */
    private void updateWaitInByDisCode(Sfc sfcObj, BigDecimal deviceNcQty, BigDecimal deviceScrapQty, BigDecimal doneQty) {
        Dispatch dispatchObj = dispatchService.getOne(new QueryWrapper<Dispatch>().eq("dispatch_code", sfcObj.getDispatchCode()));
        Dispatch dispatch = new Dispatch();
        dispatch.setId(dispatchObj.getId());
        dispatch.setWaitIn(dispatchObj.getWaitIn().subtract(deviceNcQty).subtract(deviceScrapQty).subtract(doneQty));
        dispatchService.updateById(dispatch);
    }
    //消息模板code
    @Value("${testOp.code}")
    private String code;
    //类型名称
    @Value("${testOp.typeName}")
    private String typeName;
    /**
     * 更新sfc
     * @param sfcObj
     * @param deviceNcQty
     * @param deviceScrapQty
     * @param doneQty
     * @return
     */
    private Sfc updateSfc(Sfc sfcObj, BigDecimal deviceNcQty, BigDecimal deviceScrapQty, BigDecimal doneQty) throws CommonException {
        UserInfo userInfo = sfcServiceImpl.getUserInfo();
        Sfc sfc = new Sfc();
        sfc.setBo(sfcObj.getBo());
        sfc.setInTime(new Date());

        BigDecimal currentSfcNcQty = sfcObj.getNcQty().add(deviceNcQty);//当前sfc不良总数

        sfc.setNcQty(currentSfcNcQty);

        sfc.setStopQty(BigDecimal.ZERO);
        sfc.setScrapQty(deviceScrapQty.add(sfcObj.getScrapQty()));

        sfc.setInputQty(BigDecimal.ZERO);

        //当前sfc本次出站总数
        BigDecimal outStationTotalQty = BigDecimal.ZERO;//设置为0是为了检验工序直接让他们相等
        //当前sfc可出站总数
        BigDecimal canOutStationTotalQty = BigDecimal.ZERO;

        if (userInfo.getOperationType() == 0){//如果过站工序，反之则为检验工序
            outStationTotalQty = doneQty.add(deviceScrapQty).add(deviceNcQty);
            canOutStationTotalQty = sfcObj.getSfcQty().subtract(sfcObj.getDoneQty()).subtract(sfcObj.getScrapQty()).subtract(sfcObj.getNcQty());
            sfc.setDoneQty(sfcObj.getDoneQty().add(doneQty));
        }

        //当前剩余可出站数
        BigDecimal inputQty = canOutStationTotalQty.subtract(outStationTotalQty);
        if (inputQty.compareTo(BigDecimal.ZERO) == 0){//如果可出站数为0,说明全部出站完成，反之，则可以继续出站，sfc状态不变还是为生产中（同理如果是首工序，状态也不变为新建）
            //判断工序是否是最后一道工序，不是就返回下一道工序
            OpAndId opAndId = sfcServiceImpl.getNextOpAndId(sfcObj.getSfcRouterBo(), sfcObj.getOpIds(), false);
            if (StringUtils.isBlank(opAndId.getOperationBo())){//是最后一道工序
                if (currentSfcNcQty.compareTo(BigDecimal.ZERO) == 0){//如果当前工序为最后一道工序，并且没有不良
                    sfc.setState("已完成");//把状态改成已完成
                }else {
                    sfc.setState("维修中");
                }
                sfc.setOperationBo(sfcObj.getOperationBo());
                sfc.setOpIds(sfcObj.getOpIds());
            }else {
                if(currentSfcNcQty.compareTo(BigDecimal.ZERO) == 0){
                    sfc.setState("排队中");
                }else {
                    sfc.setState("维修中");
                }
                sfc.setOperationBo(opAndId.getOperationBo());
                sfc.setOpIds(opAndId.getId());
                //判断下一道工序是否是检验工序
//                boolean testOperation = isTestOperation(opAndId.getOperationBo());
//                if (testOperation){
//                    Map<String, Object> notice;
//                    Map<String, Object> paramsMap;
//                    String operationName = operationMapper.selectById(opAndId.getOperationBo()).getOperationName();
//                    //查询需要发送消息的人员
//                    List<String> userList = sfcMapper.listAllSendMsgUser(workShopMapper.selectById(sfcObj.getWorkShopBo()).getWorkShopDesc(), typeName);
//                    for (String user : userList) {
//                        notice = new HashMap<>();
//                        notice.put("code",code);
//                        paramsMap = new HashMap<>();
//                        paramsMap.put("sfc",sfcObj.getSfc());
//                        paramsMap.put("operation_name",operationName);
//                        notice.put("params",paramsMap);
//                        notice.put("userId",user);
//                        noticeService.sendMessage(notice);//发送消息
//                    }
//
//                }
            }
            //如果全部出站完成,把所有挂这个sfc的设备全部清空
            SfcDevice sfcDeviceEntity = new SfcDevice();
            sfcDeviceEntity.setSfc("");
            sfcDeviceEntity.setShopOrderBo("");
            sfcDeviceEntity.setOperationOrder("");
            sfcDeviceEntity.setUserBo("");
            sfcDeviceEntity.setDispatchCode("");
            sfcDeviceMapper.update(sfcDeviceEntity,new QueryWrapper<SfcDevice>().eq("sfc",sfcObj.getSfc()));

            //只有当该sfc全部出站完成才把这些信息清空，否则该sfc这些信息不变
            sfc.setStationBo("");
//            sfc.setDeviceBo("");
            sfc.setUserBo("");
            sfc.setShitBo("");
            sfc.setTeamBo("");
        }else {
            sfc.setState(sfcObj.getState());
        }
        return sfc;
    }

    /**
     * 判断该工序是否是检验工序
     * @param operationBo 工序BO
     * @return
     */
    public boolean isTestOperation(String operationBo){
        Operation operation = operationMapper.selectById(operationBo);
        if (operation.getStationTypeBo().equals("ST:dongyin,检验采集")){
            return true;
        }
        return false;
    }
    /**
     * 处理sfc完成时情况
     * @param sfcObj
     * @param sfc
     */
    private void handleSfcFinish(Sfc sfcObj, Sfc sfc) {
        if (sfc.getState().equals("已完成")){
            //校验工单是否已完成
            QueryWrapper<Sfc> sfcQw = new QueryWrapper<>();
            sfcQw.eq("state","已完成");
            sfcQw.eq("shop_order_bo", sfcObj.getShopOrderBo());
            List<Sfc> sfcList = sfcMapper.selectList(sfcQw);
            BigDecimal sfcTotalQty = BigDecimal.ZERO;//工单完成的总数
            for (Sfc s : sfcList) {
                sfcTotalQty = sfcTotalQty.add(s.getSfcQty());
            }
            BigDecimal orderQty = shopOrderService.getOne(new QueryWrapper<ShopOrder>().eq("bo", sfcObj.getShopOrderBo())).getOrderQty();//工单总数
            if (sfcTotalQty.compareTo(orderQty) == 0){
                ShopOrder shopOrder = new ShopOrder();
                shopOrder.setBo(sfcObj.getShopOrderBo());
                shopOrder.setStateBo("STATE:dongyin,503");
                shopOrderService.updateById(shopOrder);
            }
            //校验工序工单是否完成
            QueryWrapper<Sfc> sfcQueryWrapper = new QueryWrapper<>();
            sfcQueryWrapper.eq("state","已完成");
            sfcQueryWrapper.eq("operation_order", sfcObj.getOperationOrder());
            List<Sfc> sfcObjList = sfcMapper.selectList(sfcQueryWrapper);
            BigDecimal sfcOpOrderTotalQty = BigDecimal.ZERO;//工序工单完成的总数
            for (Sfc s : sfcObjList) {
                sfcOpOrderTotalQty = sfcOpOrderTotalQty.add(s.getSfcQty());
            }
            BigDecimal operationOrderQty = operationOrderService.getOne(new QueryWrapper<OperationOrder>().eq("operation_order", sfcObj.getOperationOrder())).getOperationOrderQty();//工序工单总数

            if (sfcOpOrderTotalQty.compareTo(operationOrderQty) == 0){
                OperationOrder operationOrder = new OperationOrder();
                operationOrder.setOperationOrderState("2");//更新为已完成
                operationOrderService.update(operationOrder,new QueryWrapper<OperationOrder>().eq("operation_order", sfcObj.getOperationOrder()));
            }

        }
    }

    /**
     *  处理工序工单在这个工序的完成情况
     * @param sfcObj
     * @param deviceNcQty
     * @param deviceScrapQty
     * @param doneQty
     */
    private void handleOpOrderDone(Sfc sfcObj, BigDecimal deviceNcQty, BigDecimal deviceScrapQty, BigDecimal doneQty) {
        QueryWrapper<Dispatch> dispatchQueryWrapper = new QueryWrapper<>();
        dispatchQueryWrapper.eq("operation_bo", sfcObj.getOperationBo());
        dispatchQueryWrapper.eq("operation_order", sfcObj.getOperationOrder());
        dispatchQueryWrapper.eq("is_need_dispatch","0");
        Dispatch dispatchObj = dispatchService.getOne(dispatchQueryWrapper);
        if (ObjectUtil.isNotEmpty(dispatchObj)){
            Dispatch dispatch = new Dispatch();
            dispatch.setId(dispatchObj.getId());
            dispatch.setNotDoneQty(dispatchObj.getNotDoneQty().subtract(deviceNcQty).subtract(deviceScrapQty).subtract(doneQty));
            dispatchService.updateById(dispatch);
        }
    }
    /**
     * 通过当前工序找到该工艺路线上一个工序
     * @param routerBo
     * @param currentOp
     * @return
     */
    public String getPreviousOpByCurrentOp(String routerBo,String currentOp){
        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", routerBo));
        JSONObject jsonObject = JSONObject.parseObject(routerProcess.getProcessInfo());
        JSONArray nodeList = JSONArray.parseArray(jsonObject.getString("nodeList"));
        JSONArray lineList = JSONArray.parseArray(jsonObject.getString("lineList"));

        String currentOpId = sfcServiceImpl.getIdByOperation(nodeList, currentOp);//当前工序在这个工艺路线上的流程Id
        String previousId = sfcServiceImpl.getPreviousIdByCurrentId(currentOpId, lineList);//上一个工序的流程ID
        String operationBo = sfcServiceImpl.getOperationById(nodeList,previousId);//根据ID拿工序
        return operationBo;
    }

    @Override
    public synchronized void enterStationNew(SfcDto sfcDto) throws CommonException {
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfcDto.getSfc()));
        if (ObjectUtil.isEmpty(sfcObj)){
            throw new CommonException("该sfc不存在",504);
        }
        UserInfo userInfo = sfcServiceImpl.getUserInfo();
        if (Sfc.WORK_SHOP_BO_XQ.equals(sfcObj.getWorkShopBo())){
            if (userInfo.getOperationType() != 1){
                throw new CommonException("线圈车间的条码请送到检验直接过站入库！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        if ("已完成".equals(sfcObj.getState())){
            throw new CommonException("该sfc已完成",504);
        }
        String stateBo = shopOrderService.getById(sfcObj.getShopOrderBo()).getStateBo();
        if ("STATE:dongyin,502".equals(stateBo)){
            throw new CommonException("该sfc的工单已关闭，无法进站！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", sfcObj.getSfcRouterBo()));
        //验证是否是这条工艺路线上的工序
        if (!routerProcess.getProcessInfo().contains(userInfo.getOperationBo())){
            throw new CommonException("当前工序不在该SFC工艺路线上，请重新选择工序", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (StringUtils.isNotBlank(sfcObj.getParentSfcBo())){//拆分标签
            if (userInfo.getOperationType() != 1){
                throw new CommonException("入库拆分的标签请送到检验工序直接过站", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        //判断是否是首工序
        boolean isTheFirstOp = sfcServiceImpl.checkIsFirstOperation(sfcObj.getSfcRouterBo(),userInfo.getOperationBo());

        QueryWrapper<SfcStateTrack> stateTrackQueryWrapper = new QueryWrapper<>();
        stateTrackQueryWrapper.eq("sfc",sfcObj.getSfc());
        stateTrackQueryWrapper.eq("operation_bo",userInfo.getOperationBo());
        SfcStateTrack sfcStateTrackObj = sfcStateTrackMapper.selectOne(stateTrackQueryWrapper);

        if (isTheFirstOp){//如果是首工序则不需要校验上一个工序的完成情况，直接可以进站（要校验的条件只有一个：该sfc在该工序是否完成）
            if (ObjectUtil.isEmpty(sfcStateTrackObj)){//第一次进站，直接进站就行了，同时在sfc状态跟踪表中插入一条数据
                insertTrackInfo(sfcObj, userInfo,"1");
            }else {//如果不是第一次，则需要校验该sfc在此工序是否完成，完成则不允许进站
                if (sfcStateTrackObj.getState().equals("3") || sfcStateTrackObj.getState().equals("4")){
                    throw new CommonException("该sfc在当前工序已经完成，不能再进站", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                updateTrackInfo(sfcObj.getSfc(),userInfo.getOperationBo(),"1");//更新跟踪表的状态为生产中（1）
            }
        }else {//如果是正常工序
            //大前提,该sfc在当前工序是否已经完成
            if (checkCurrentSfcDoneState(sfcObj, userInfo)){
                throw new CommonException("该sfc在当前工序已经完成，不能再进站", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            //走到这里说明当前是未完成或还没开始做状态，这里分两种情况：上一个工序在生产中、上一个工序没有在生产（这里包含上个工序没有进过站的情况）
            handleNoDoneCheck(sfcObj, userInfo); //校验工单非已完成状态情况
        }
        saveStartTime(sfcObj.getShopOrderBo());//处理开工时间
        handleSfcDevice(sfcDto, sfcObj, userInfo);//处理sfc设备表信息
        handleEnterInfo(sfcDto, sfcObj, userInfo);//处理进站表信息

        //把工序工单状态更新为生产中
        OperationOrder operationOrder = new OperationOrder();
        operationOrder.setOperationOrderState("1");
        operationOrderService.update(operationOrder,new QueryWrapper<OperationOrder>().eq("operation_order",sfcObj.getOperationOrder()));

        //更新SFC为生产中
        Sfc sfc = new Sfc();
        sfc.setState("生产中");
        sfcMapper.update(sfc,new QueryWrapper<Sfc>().eq("sfc",sfcObj.getSfc()));
    }

    /**
     * 保存工单开工时间
     */
    public void saveStartTime(String shopOrderBo){
        List<Enter> enterList = enterMapper.selectList(new QueryWrapper<Enter>().eq("shop_order_bo", shopOrderBo));
        if (CollectionUtil.isEmpty(enterList)){//该工单第一次进站
            ShopOrder shopOrder = new ShopOrder();
            shopOrder.setBo(shopOrderBo);
            shopOrder.setStartTime(new Date());
            shopOrderService.updateById(shopOrder);
        }
    }
    @Override
    public synchronized int outStationNew(OutStationDto outStationDto) throws CommonException {
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", outStationDto.getSfc()));
        if (ObjectUtil.isEmpty(sfcObj)){
            throw new CommonException("该sfc不存在",504);
        }
        UserInfo userInfo = sfcServiceImpl.getUserInfo();//获取人员信息
        if (Sfc.WORK_SHOP_BO_XQ.equals(sfcObj.getWorkShopBo())){
            if (userInfo.getOperationType() != 1){
                throw new CommonException("线圈车间的条码请送到检验直接过站入库！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", sfcObj.getSfcRouterBo()));
        //验证是否是这条工艺路线上的工序
        if (!routerProcess.getProcessInfo().contains(userInfo.getOperationBo())){
            throw new CommonException("当前工序不在该SFC工艺路线上，请重新选择工序", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if ("已完成".equals(sfcObj.getState())){
            throw new CommonException("该sfc已完成",504);
        }
        if ("维修中".equals(sfcObj.getState())){
            throw new CommonException("该sfc在维修中，请送去返修或做不良标签拆分",504);
        }
        if (StringUtils.isNotBlank(sfcObj.getParentSfcBo())){//拆分标签
            if (sfcServiceImpl.getUserInfo().getOperationType() != 1){
                throw new CommonException("入库拆分的标签请送到检验工序直接过站", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        boolean canDisToDeviceOp = sfcServiceImpl.isCanDisToDeviceOp(userInfo.getOperationBo());//判断是否是需要派工至设备的工序

        BigDecimal deviceNcQty = BigDecimal.ZERO;//不良总数量
        BigDecimal deviceScrapQty = BigDecimal.ZERO;//报废总数量
        BigDecimal doneQty = BigDecimal.ZERO;//过站良品总数量

        for (OutStationDevice outStationDevice : outStationDto.getOutStationDevices()) {
            SfcWiplog sfcWiplog = insertSfcWiplog(sfcObj, false, outStationDevice);//新增wip_log表出站信息

            BigDecimal currentDeviceNcQty = BigDecimal.ZERO;//当前设备不良数量
            BigDecimal currentDeviceScrapQty = BigDecimal.ZERO;//当前设备报废数量

            //处理不良和报废
            for (DeviceNc deviceNc : outStationDto.getDeviceNcs()) {
                if (outStationDevice.getDeviceBo().equals(deviceNc.getDeviceBo())){
                    if (deviceNc.getType().equals("0")){//不良
                        SfcNcLog sfcNcLog = new SfcNcLog();
                        BeanUtils.copyProperties(sfcObj,sfcNcLog);
                        sfcNcLog.setBo(UUID.uuid32());
                        sfcNcLog.setNcQty(deviceNc.getQty());
                        sfcNcLog.setNcCodeBo(deviceNc.getNcCodeBo());
                        sfcNcLog.setRemark(deviceNc.getMemo());
                        sfcNcLog.setWipLogBo(sfcWiplog.getBo());
                        sfcNcLog.setDeviceBo(outStationDevice.getDeviceBo());
                        sfcNcLog.setRecordTime(new Date());
                        sfcNcLog.setOperationBo(userInfo.getOperationBo());
                        sfcNcLog.setStationBo(userInfo.getStationBo());
                        sfcNcLog.setUserBo(userInfo.getUserBo());
                        sfcNcLogMapper.insert(sfcNcLog);
                        currentDeviceNcQty = currentDeviceNcQty.add(deviceNc.getQty());
                    }else if (deviceNc.getType().equals("1")){//报废
                        SfcRepair sfcRepair = new SfcRepair();
                        BeanUtils.copyProperties(sfcObj,sfcRepair);
                        sfcRepair.setBo(UUID.uuid32());
                        sfcRepair.setScrapQty(deviceNc.getQty());
                        sfcRepair.setWipLogBo(sfcWiplog.getBo());
                        sfcRepair.setRepairTime(new Date());
                        sfcRepair.setNcCodeBo(deviceNc.getNcCodeBo());
                        sfcRepair.setDutyOperation(userInfo.getOperationBo());
                        sfcRepair.setDutyUser(userInfo.getUserBo());
                        sfcRepair.setStationBo(userInfo.getStationBo());
                        sfcRepairMapper.insert(sfcRepair);
                        currentDeviceScrapQty = currentDeviceScrapQty.add(deviceNc.getQty());
                    }
                }
            }
            sfcWiplog.setNcQty(currentDeviceNcQty);
            sfcWiplog.setScrapQty(currentDeviceScrapQty);
            sfcWiplog.setInputQty(outStationDevice.getOutStationQty().add(currentDeviceNcQty).add(currentDeviceScrapQty));
            sfcWiplogMapper.updateById(sfcWiplog);

            deviceNcQty = deviceNcQty.add(currentDeviceNcQty);
            deviceScrapQty = deviceScrapQty.add(currentDeviceScrapQty);
            doneQty = doneQty.add(outStationDevice.getOutStationQty());

            checkNeedDisOpOutQty(sfcObj, canDisToDeviceOp, outStationDevice, currentDeviceNcQty, currentDeviceScrapQty);//校验需要派工至设备的出站数量是否大于设备的派工数量
            outStationReportWork(sfcObj, userInfo, outStationDevice, sfcWiplog,outStationDto.getAutomaticReportWorkList());//出站时候系统工步报工
            updateSfcDevice(sfcObj, outStationDevice);//出站时更新device_sfc设备对应表
            BigDecimal currentDeviceOutTotalQty = outStationDevice.getOutStationQty().add(currentDeviceScrapQty).add(currentDeviceNcQty);//当前设备出站总数
            updateWaitInQty(canDisToDeviceOp,sfcObj.getOperationOrder(),outStationDevice.getDevice(),currentDeviceOutTotalQty);
        }
        int row = -1;
        if (userInfo.getOperationType() == 1){//如果是检验工序出站
            checkTestOp(outStationDto.getInputQty(),outStationDto.getTestOpDoneQty());
            BigDecimal ncQty = handleTestOpNew(sfcObj, outStationDto.getTestOpDoneQty(), outStationDto.getDeviceNcs());//处理检验工序情况
            handleSfcFinishNew(sfcObj,ncQty);//处理sfc完成时情况
            //自动入库
            row = automaticInStock(sfcObj.getSfc());
        }else {
            feedInService.outStation(outStationDto);//出站扣料
            handleOpOrderDone(sfcObj, deviceNcQty, deviceScrapQty, doneQty);//处理工序工单在这个工序的完成情况
            //出站时更新跟踪表状态
            updateTrackState(sfcObj, userInfo);
            updateScrapQtyBySfc(sfcObj.getScrapQty(),deviceScrapQty,sfcObj.getSfc());
        }
        return row;
    }
    /**
     * 更新本次报废数量通过sfc
     * @param initScrapQty  该sfc初始报废数
     * @param thisScrapQty  本次要加上报废数
     */
    public void updateScrapQtyBySfc(BigDecimal initScrapQty,BigDecimal thisScrapQty,String sfc){
        if (thisScrapQty.compareTo(BigDecimal.ZERO) == 1){
            Sfc sfcEntity = new Sfc();
            sfcEntity.setScrapQty(initScrapQty.add(thisScrapQty));
            sfcMapper.update(sfcEntity,new QueryWrapper<Sfc>().eq("sfc",sfc));
        }
    }

    /**
     * 更新track表状态
     * @param sfc
     */
    public void updateTrackBySfc(String sfc){
        SfcStateTrack sfcStateTrack = new SfcStateTrack();
        sfcStateTrack.setState(SfcStateTrack.F_STATE);
        sfcStateTrackMapper.update(sfcStateTrack,new QueryWrapper<SfcStateTrack>().eq("sfc",sfc));
    }
    /**
     * 校验检验工序
     * @param inputQty
     * @param testOpDoneQty
     */
    private void checkTestOp(BigDecimal inputQty,BigDecimal testOpDoneQty) throws CommonException{
        if (testOpDoneQty.compareTo(BigDecimal.ZERO) != 1){
            throw new CommonException("出站数量必须大于0",504);
        }
    }
    /**
     * 检验过站自动入库
     */
    private int automaticInStock(String sfc) throws CommonException {
        //判断是否触发自动入库，条件：sfc状态为已完成
        //入库数量标准，检验工序所有的良品数
        String state = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfc)).getState();
        if ("已完成".equals(state)){
            BigDecimal stockQty = BigDecimal.ZERO;//需要入库数量
            List<SfcWiplog> sfcWiplogs = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().eq("sfc", sfc).eq("operation_bo", sfcServiceImpl.getUserInfo().getOperationBo()));
            for (SfcWiplog sfcWiplog : sfcWiplogs) {
                stockQty = stockQty.add(sfcWiplog.getDoneQty());
            }
            String wareHouse = "";
            Sfc sfcEntity = sfcService.selectBySfc(sfc);
            Stock stock = stockMapper.selectById(sfc);
            if (ObjectUtil.isEmpty(stock)){
                sfcEntity.setDifQty(BigDecimal.ZERO);
                sfcEntity.setDoneQty(stockQty);
                String result = stockService.applyStock(sfcEntity, wareHouse,1);
                if (result.indexOf("SUCCESS！")==-1){//入库失败
                    return 0;
                }else {
                    return 1;//入库成功
                }
            }
        }
        return -1;
    }
    @Override
    public synchronized SfcVo getSfcInfoBySfcOutNew(OutStationDto outStationDto) throws CommonException {
        SfcVo sfcVo = sfcMapper.selectSfcInfo(outStationDto.getSfc());
        if (ObjectUtil.isEmpty(sfcVo)){
            throw new CommonException("该sfc不存在",504);
        }
        UserInfo userInfo = sfcServiceImpl.getUserInfo();
        List<Device> devices = Lists.newArrayList();
        if (Sfc.WORK_SHOP_BO_XQ.equals(sfcVo.getWorkShopBo())){//线圈车间
            sfcVo.setDevices(devices);
            sfcVo.setInputQty(sfcVo.getSfcQty());
        }else {//其他车间
            if (userInfo.getOperationType() == 0){//过站工序
                List<SfcDevice> canOutDevices = sfcDeviceMapper.selectList(new QueryWrapper<SfcDevice>().
                        eq("sfc",sfcVo.getSfc()).
                        eq("operation_bo",userInfo.getOperationBo()));

                if (CollectionUtil.isEmpty(canOutDevices)){
                    //可能是首工序，先判断是否是首工序,如果不是说明确实没有设备，抛出异常，如果是首工序，则根据工序工单查，工序工单也查不到则抛出异常
                    boolean isFirstOperation = sfcServiceImpl.checkIsFirstOperation(sfcVo.getSfcRouterBo(), userInfo.getOperationBo());
                    if (isFirstOperation){
                        List<SfcDevice> firstOpDevices = sfcDeviceMapper.listSfcDeviceByFirstOp(sfcVo.getOperationOrder(), userInfo.getOperationBo());
                        if (CollectionUtil.isEmpty(firstOpDevices)){
                            throw new CommonException("该sfc没有可出站的设备，请重新进站",504);
                        }
                        canOutDevices = firstOpDevices;
                    }else {
                        throw new CommonException("该sfc没有可出站的设备，请重新进站",504);
                    }
                }
                for (SfcDevice canOutDevice : canOutDevices) {
                    Device device = deviceService.getById(canOutDevice.getDeviceBo());
                    devices.add(device);
                }
                sfcVo.setDevices(devices);
                BigDecimal canOutQty = calculationCanOutQty(outStationDto.getSfc());

//            if (canOutQty.compareTo(BigDecimal.ZERO) != 1){
//                throw new CommonException("当前可出站数量为0",504);
//            }
                sfcVo.setInputQty(canOutQty);
            }else {//检验工序
                sfcVo.setDevices(devices);
                if (StringUtils.isNotBlank(sfcVo.getParentSfcBo())){//如果是拆分的标签,那可出站数量就是sfc数量-这条sfc已经出站的数量
                    List<SfcWiplog> sfcWiplogs = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().eq("sfc", sfcVo.getSfc()));
                    BigDecimal splitOutQty = BigDecimal.ZERO;//拆分的标签出站数量
                    BigDecimal scrapQty = BigDecimal.ZERO;//报废数
                    for (SfcWiplog sfcWiplog : sfcWiplogs) {
                        splitOutQty = splitOutQty.add(sfcWiplog.getDoneQty());
                        scrapQty = scrapQty.add(sfcWiplog.getScrapQty());
                    }
                    sfcVo.setInputQty(sfcVo.getSfcQty().subtract(splitOutQty).subtract(scrapQty));
                }else {
                    //获取检验工序可出站数量（上一个工序完成的良品数 - 该检验工序已经出过站的数量 - 该sfc拆分的标签sfc数量（如果有的话））
                    BigDecimal inputQty = calculationTestOpCanOutQty(sfcVo.getSfc(), sfcVo.getSfcRouterBo());
                    List<Sfc> sfcList = sfcMapper.selectList(new QueryWrapper<Sfc>().eq("init_parent_sfc_bo", sfcVo.getBo()));
                    BigDecimal splitQty = BigDecimal.ZERO;//标签拆分的数量
                    for (Sfc sfc : sfcList) {
                        splitQty = splitQty.add(sfc.getSfcQty());
                    }
                    sfcVo.setInputQty(inputQty.subtract(splitQty));
                }
            }
        }
        return sfcVo;
    }

    /**
     * 新增跟踪表信息
     * @param sfcObj
     * @param userInfo
     */
    private void insertTrackInfo(Sfc sfcObj, UserInfo userInfo,String state) {
        SfcStateTrack sfcStateTrack = new SfcStateTrack();
        sfcStateTrack.setId(UUID.uuid32());
        sfcStateTrack.setSfc(sfcObj.getSfc());
        sfcStateTrack.setOperationBo(userInfo.getOperationBo());
        sfcStateTrack.setState(state);
        sfcStateTrack.setCreateDate(new Date());
        sfcStateTrack.setUpdateDate(new Date());
        sfcStateTrackMapper.insert(sfcStateTrack);
    }

    /**
     * 修改跟踪表状态
     * @param state
     */
    public void updateTrackInfo(String sfc, String operationBo,String state){
        SfcStateTrack sfcStateTrack = new SfcStateTrack();
        sfcStateTrack.setState(state);
        sfcStateTrack.setUpdateDate(new Date());

        sfcStateTrackMapper.update(sfcStateTrack,new QueryWrapper<SfcStateTrack>().
                eq("sfc",sfc).
                eq("operation_bo",operationBo));
    }

    /**
     * 校验该sfc在当前工序是否已经完成
     * @param sfcObj
     * @param userInfo
     */
    private boolean checkCurrentSfcDoneState(Sfc sfcObj, UserInfo userInfo) {
        SfcStateTrack sfcStateTrackObj = getTrackInfoBySfcAndOp(sfcObj, userInfo.getOperationBo());
        if (ObjectUtil.isNotEmpty(sfcStateTrackObj)){
            if (sfcStateTrackObj.getState().equals("3") ||  sfcStateTrackObj.getState().equals("4")){
                return true;
            }
        }
        return false;
    }
    /**
     * 根据sfc和工序查询信息
     * @param sfcObj
     */
    private SfcStateTrack getTrackInfoBySfcAndOp(Sfc sfcObj, String operationBo) {
        QueryWrapper<SfcStateTrack> stateTrackQueryWrapper = new QueryWrapper<>();
        stateTrackQueryWrapper.eq("sfc", sfcObj.getSfc());
        stateTrackQueryWrapper.eq("operation_bo", operationBo);
        SfcStateTrack sfcStateTrackObj = sfcStateTrackMapper.selectOne(stateTrackQueryWrapper);
        return sfcStateTrackObj;
    }
    /**
     * 处理该sfc在当前工序未完成的进站校验
     * @param sfcObj
     * @param userInfo
     */
    private void handleNoDoneCheck(Sfc sfcObj, UserInfo userInfo) throws CommonException {
        //如果是跳站过来的并且当前进站的工序为跳站过来的工序，则选择第一种进站校验
        if ("1".equals(sfcObj.getIsSkipStationSfc()) && userInfo.getOperationBo().equals(sfcObj.getSkipOperationBo())){
            //这种校验无需校验上一个工序是否进过站，直接进站，可出站数量暂时定为SFC数量（是有问题的，需求紧急，后面再改）
            SfcStateTrack sfcStateTrackObjCurOp = getTrackInfoBySfcAndOp(sfcObj, userInfo.getOperationBo());//当前工序
            if (ObjectUtil.isEmpty(sfcStateTrackObjCurOp)){
                insertTrackInfo(sfcObj, userInfo,"1");
            }else {
                updateTrackInfo(sfcObj.getSfc(), userInfo.getOperationBo(),"1");
            }
        }else {//否则为这种校验
            //通过当前工序找到该工艺路线上一个工序
//            String previousOperation = getPreviousOpByCurrentOp(sfcObj.getSfcRouterBo(), userInfo.getOperationBo());
            String previousOperation = getPreviousOp(sfcObj.getSfcRouterBo(),userInfo.getOperationBo(),sfcObj.getSfc());
            //首先判断上一个工序是否进过站
            SfcStateTrack sfcStateTrackObj = getTrackInfoBySfcAndOp(sfcObj, previousOperation);//上一个工序
            if (ObjectUtil.isEmpty(sfcStateTrackObj)){//没有进过站
                String operationName = operationMapper.selectById(previousOperation).getOperationName();
                throw new CommonException("上一个工序还未进过站，请先在上一个工序生产,上一个工序为:" + operationName,CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else {//进过站
                //进过站校验分两种情况，一种是上个工序目前正在生产中（state==1）,可以直接进站，还有就是虽然上一个工序生产过，但是现在没有在生产（state==2），这个时候
                //需要校验上一个工序已经出站的数 是否大于 当前工序已经出站的数 因为如果相等说明本工序已经把上个工序流下来的做完了，就没有必要进站了，大于的情况不可能，只能是小于或等于
                if (!sfcStateTrackObj.getState().equals("1")){//上一个工序状态为（state==2、3、4）都要校验
                    //计算上一个工序的出站数（这里只拿良品数）
                    BigDecimal previousOpOutQty = getPreviousOpOutQty(sfcObj.getSfc(), previousOperation);
                    //计算当前工序的出站数（良品数 + 报废数 + 不良数），这里的不良数从me_sfc_nc_log表里面去拿，拿IS_REPAIRED状态为0（未返修）的不良数
                    BigDecimal currentOpOutQty = getCurrentOpOutQty(sfcObj.getSfc(), userInfo.getOperationBo());

                    if (currentOpOutQty.compareTo(previousOpOutQty) == 0){
                        throw new CommonException("上一个工序出站良品数为"+previousOpOutQty.toString()+",本工序已出站"+currentOpOutQty.toString()+",没有可生产数量了",CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }
                SfcStateTrack sfcStateTrackObjCurOp = getTrackInfoBySfcAndOp(sfcObj, userInfo.getOperationBo());//当前工序
                if (ObjectUtil.isEmpty(sfcStateTrackObjCurOp)){
                    insertTrackInfo(sfcObj, userInfo,"1");
                }else {
                    updateTrackInfo(sfcObj.getSfc(), userInfo.getOperationBo(),"1");
                }
            }
        }
    }
    /**
     * 找到上一个工序（实际生产）
     * 一、当前工序本身是并行工序
     *         (1)、两个工序都没进过站，那上一个工序肯定就为这个工序序号-1对应的工序
     *         (2)、只有一个工序进过站，1>进站的工序为该工序本身，那上一个工序就为这个工序序号-1对应的工序 2>如果进站的工序为另一个并行工序，那上一个工序就为这个并行工序
     *         (3)、两个工序都进过站，谁先进站谁放在前面，例：当前工序为B，B、C并行，B先进站，那上一个工序就为A，C先进站，那上一个就为B
     * 二、当前工序本身为非并行，但是上一个工序为并行的两个工序，所以需要确定为哪一个，判定条件；
     *         （1）、并行的两个都没进过站，这种情况肯定是跳站过来的，不存在走这个逻辑，忽略
     *         （2）、并行的两个进过一个其中一个，那这个就为该工序的上一个工序
     *         （3）、并行的两个都进过（这是正常情况），则根据时间排在后面的为该工序的上一个工序
     * 三、本身为并行，上一个也不是并行，这个很简单，只要获取当前工序的序号-1对应的工序就是该工序的上一个工序
     * @param routerBo
     * @param operationBo
     * @param sfc
     * @return
     */
    public String getPreviousOp(String routerBo,String operationBo,String sfc) throws CommonException {
        boolean isFirstOperation = sfcServiceImpl.checkIsFirstOperation(routerBo, operationBo);
        if (isFirstOperation){
            return null;
        }
        //当前工序
        RouterProcessTable routerProcessTableC = routerProcessTableMapper.selectOne(new QueryWrapper<RouterProcessTable>().
                eq("router_bo", routerBo).
                eq("operation_bo", operationBo));

        int positionFlagC = routerProcessTableC.getPositionFlag();//当前工序的顺序ID
        int parallelFlagC = routerProcessTableC.getParallelFlag();//当前工序是否是并行标识

        //上个工序
        List<RouterProcessTable> routerProcessTableListP = routerProcessTableMapper.selectList(new QueryWrapper<RouterProcessTable>().
                eq("router_bo", routerBo).
                eq("position_flag", positionFlagC - 1));
        int parallelFlagP = routerProcessTableListP.get(0).getParallelFlag();//上一个工序是否是并行标识

        if (parallelFlagC > 0){//当前工序本身是并行工序，那上个工序肯定不是并行工序
            List<RouterProcessTable> routerProcessTableListC = routerProcessTableMapper.selectList(new QueryWrapper<RouterProcessTable>().
                    eq("router_bo", routerBo).
                    eq("position_flag", positionFlagC));
            return currentOpIsParallel(operationBo, sfc, routerProcessTableListC, routerProcessTableListP);
        }else {//当前工序不是并行
            if (parallelFlagP > 0){//上一个工序为并行
                return previousOpIsParallel(operationBo, sfc, routerProcessTableListP);
            }else {//上一个工序不是并行
                return routerProcessTableListP.get(0).getOperationBo();
            }
        }

    }

    /**
     * 上一个工序为并行
     * @param operationBo
     * @param sfc
     * @param routerProcessTableListP
     * @return
     */
    private String previousOpIsParallel(String operationBo, String sfc, List<RouterProcessTable> routerProcessTableListP) {
        String parallelA = routerProcessTableListP.get(0).getOperationBo();
        String parallelB = routerProcessTableListP.get(1).getOperationBo();
        List<SfcStateTrack> sfcStateTracks = sfcStateTrackMapper.selectList(new QueryWrapper<SfcStateTrack>().
                eq("sfc", sfc).
                in("operation_bo", parallelA, parallelB));
        if (CollectionUtil.isEmpty(sfcStateTracks)){//两个工序都没进过站，随便取一条做该工序的上一条工序
            return parallelA;
        }
        if (sfcStateTracks.size() == 1){//只进了一个，那上一条工序就为另外一个
            if (sfcStateTracks.get(0).getOperationBo().equals(parallelA))return parallelB;
            if (sfcStateTracks.get(0).getOperationBo().equals(parallelB))return parallelA;
        }
        if (sfcStateTracks.size() == 2){//两个都进了，去最后进站的一条做为上一个工序
            Date parallelAInTime = sfcStateTracks.get(0).getCreateDate();
            Date parallelBInTime = sfcStateTracks.get(1).getCreateDate();
            if (parallelAInTime.getTime() < parallelBInTime.getTime())return sfcStateTracks.get(1).getOperationBo();
            if (parallelAInTime.getTime() > parallelBInTime.getTime())return sfcStateTracks.get(0).getOperationBo();
        }
        return null;
    }

    /**
     * 当前工序是并行工序
     * @param operationBo
     * @param sfc
     * @param routerProcessTableListC
     * @param routerProcessTableListP
     * @return
     */
    private String currentOpIsParallel(String operationBo, String sfc, List<RouterProcessTable> routerProcessTableListC, List<RouterProcessTable> routerProcessTableListP) {
        String parallelA = routerProcessTableListC.get(0).getOperationBo();
        String parallelB = routerProcessTableListC.get(1).getOperationBo();
        List<SfcStateTrack> sfcStateTracks = sfcStateTrackMapper.selectList(new QueryWrapper<SfcStateTrack>().
                eq("sfc", sfc).
                in("operation_bo", parallelA, parallelB));
        //没有进过站
        if (CollectionUtil.isEmpty(sfcStateTracks))return routerProcessTableListP.get(0).getOperationBo();

        String currentOp = null;//当前工序
        String otherOp = null;//另一个工序
        if (operationBo.equals(parallelA)){
            currentOp = parallelA;
            otherOp = parallelB ;
        }
        if (operationBo.equals(parallelB)){
            currentOp = parallelB;
            otherOp = parallelA;
        }
        //只有一个进过站，并且是这个工序本身
        if (sfcStateTracks.size() == 1 && sfcStateTracks.get(0).getOperationBo().equals(currentOp))return routerProcessTableListP.get(0).getOperationBo();
        //只有一个进过站，并且是另一个并行工序
        if (sfcStateTracks.size() == 1 && !sfcStateTracks.get(0).getOperationBo().equals(currentOp))return otherOp;

        //两个都进过站
        if (sfcStateTracks.size() == 2){
            Date parallelAInTime = sfcStateTracks.get(0).getCreateDate();
            Date parallelBInTime = sfcStateTracks.get(1).getCreateDate();
            if (parallelAInTime.getTime() < parallelBInTime.getTime()){
                //顺序A->B
                if (operationBo.equals(sfcStateTracks.get(0).getOperationBo())){
                    return routerProcessTableListP.get(0).getOperationBo();
                }else {
                    return sfcStateTracks.get(0).getOperationBo();
                }
            }else{
                //顺序B->A
                if (operationBo.equals(sfcStateTracks.get(0).getOperationBo())){
                    return sfcStateTracks.get(1).getOperationBo();
                }else{
                    return routerProcessTableListP.get(0).getOperationBo();
                }
            }
        }
        return null;
    }

    private BigDecimal getPreviousOpOutQty(String sfc, String previousOperation) {
        BigDecimal previousOpOutQty = BigDecimal.ZERO;//上一个工序已出站的数量(只计算良品数)

        List<SfcWiplog> sfcWipLogsPreOp = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().
                eq("sfc", sfc).
                eq("operation_bo", previousOperation));

        for (SfcWiplog sfcWipLog : sfcWipLogsPreOp) {
            previousOpOutQty = previousOpOutQty.add(sfcWipLog.getDoneQty());
        }
        return previousOpOutQty;
    }
    public BigDecimal getCurrentOpOutQty(String sfc, String operationBo) {
        BigDecimal currentOpDoneQty = BigDecimal.ZERO;//当前工序出站良品数
        BigDecimal currentOpScrapQty = BigDecimal.ZERO;//当前工序出站报废数
        BigDecimal currentOpNcQty = BigDecimal.ZERO;//当前工序出站不良数

        List<SfcWiplog> sfcWipLogsCurOp = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().
                eq("sfc", sfc).
                eq("operation_bo", operationBo));

        for (SfcWiplog sfcWiplog : sfcWipLogsCurOp) {
            currentOpDoneQty = currentOpDoneQty.add(sfcWiplog.getDoneQty());
            currentOpScrapQty = currentOpScrapQty.add(sfcWiplog.getScrapQty());
        }
        List<SfcNcLog> sfcNcLogs = sfcNcLogMapper.selectList(new QueryWrapper<SfcNcLog>().
                eq("sfc", sfc).
                eq("operation_bo", operationBo).
                eq("is_repaired", "0"));

        for (SfcNcLog sfcNcLog : sfcNcLogs) {
            currentOpNcQty = currentOpNcQty.add(sfcNcLog.getNcQty());
        }

        BigDecimal currentOpOutQty = currentOpDoneQty.add(currentOpScrapQty).add(currentOpNcQty);//当前工序已出站数量
        return currentOpOutQty;
    }
    /**
     * 处理sfc设备表信息
     * @param sfcDto
     * @param sfcObj
     * @param userInfo
     * @throws CommonException
     */
    private void handleSfcDevice(SfcDto sfcDto, Sfc sfcObj, UserInfo userInfo) throws CommonException {
        for (String deviceBo : sfcDto.getDeviceList()) {
            SfcDevice sfcDeviceEntity = sfcDeviceMapper.selectById(deviceBo);

            SfcDevice sfcDevice = new SfcDevice();
            sfcDevice.setDeviceBo(deviceBo);
            sfcDevice.setSfc(sfcObj.getSfc());
            sfcDevice.setShopOrderBo(sfcObj.getShopOrderBo());
            sfcDevice.setOperationOrder(sfcObj.getOperationOrder());
            sfcDevice.setUserBo(userInfo.getUserBo());
            sfcDevice.setDispatchCode(sfcObj.getDispatchCode());
            sfcDevice.setOperationBo(sfcServiceImpl.getUserInfo().getOperationBo());

            if (ObjectUtil.isEmpty(sfcDeviceEntity)) {
                sfcDeviceMapper.insert(sfcDevice);
            } else {
                if (StringUtils.isNotBlank(sfcDeviceEntity.getSfc())) {//如果当前设备上挂有SFC
                    String device = deviceBo.substring(deviceBo.substring(0, deviceBo.indexOf(",")).length() + 1);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//回滚事务
                    throw new CommonException("设备编号为" + device + "的设备上有" + sfcDeviceEntity.getSfc() + "的SFC在生产", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                if (StringUtils.isBlank(sfcDeviceEntity.getOperationOrder())){//如果工序工单有值说明之前有工序工单进站，进站时间还是取工序工单进站时间，反之取当前时间
                    sfcDevice.setCreateDate(new Date());
                }
                sfcDeviceMapper.updateById(sfcDevice);
            }
        }
    }
    /**
     * 处理进站信息
     * @param sfcDto
     * @param sfcObj
     * @param userInfo
     */
    private void handleEnterInfo(SfcDto sfcDto, Sfc sfcObj, UserInfo userInfo) {
        for(String str: sfcDto.getDeviceList()){
            Device device=deviceService.getDeviceById(str);
            // 将进站数据插入进站表
            Enter enter=new Enter();
            enter.setBo(UUID.uuid32());
            enter.setDeviceBo(str);
            enter.setSfc(sfcObj.getSfc());
            enter.setInTime(new Date());
            enter.setOperationBo(userInfo.getOperationBo());
            enter.setState("0");
            enter.setShopOrderBo(sfcObj.getShopOrderBo());
            enter.setOperationOrder(sfcObj.getOperationOrder());
            enter.setUserBo(userInfo.getUserBo());
            enterMapper.insert(enter);

            //验证工单是否已首检
            if(device !=null && device.getDevice() !=null){
                String shopOrder = shopOrderService.getById(sfcObj.getShopOrderBo()).getShopOrder();
                List<QualityCheckList> qualityCheckLists=qualityCheckListMapper.selectQualityInfo(device.getDevice(), userInfo.getOperationBo(),shopOrder);
                if(qualityCheckLists == null || qualityCheckLists.size()==0){
                    // 不存在已首检的当前工单,重置首检状态为未首检
                    myDeviceMapper.updateFirstInsState(userInfo.getUserId(), userInfo.getStationBo(),device.getBo());
                }
            }

        }
    }
    /**
     * 出站时更新跟踪表状态
     * @param sfcObj
     * @param userInfo
     * @throws CommonException
     */
    private void updateTrackState(Sfc sfcObj, UserInfo userInfo) throws CommonException {
        BigDecimal currentOpOutTotalQty = getCurrentOpOutQty(sfcObj.getSfc(), userInfo.getOperationBo());//当前工序已经出站数量（包括本次）
        //判断是否是首工序
        boolean isTheFirstOp = sfcServiceImpl.checkIsFirstOperation(sfcObj.getSfcRouterBo(),userInfo.getOperationBo());
        boolean flag = checkSfcAndOpIsAllOut(sfcObj.getSfc(), userInfo.getOperationBo());
        boolean existNc = isExistNc(sfcObj.getSfc(), userInfo.getOperationBo());//是否存在不良
        BigDecimal splitQty = getSplitQty(sfcObj.getBo());
        BigDecimal qty = sfcObj.getSfcQty().add(splitQty);
        BigDecimal totalQty;
        if (isTheFirstOp){//如果是首工序
            QueryWrapper<SfcStateTrack> stateTrackQueryWrapper = new QueryWrapper<>();
            stateTrackQueryWrapper.eq("sfc",sfcObj.getSfc());
            stateTrackQueryWrapper.eq("operation_bo",userInfo.getOperationBo());
            SfcStateTrack sfcStateTrackObj = sfcStateTrackMapper.selectOne(stateTrackQueryWrapper);
            //首工序工序工单进站的话可能没有在track表中新增数据，在这里新增一条
            if (ObjectUtil.isEmpty(sfcStateTrackObj)){
                insertTrackInfo(sfcObj,userInfo,"1");
            }
            totalQty = currentOpOutTotalQty.add(sfcObj.getSkipBeforeScrapQty());
            if (totalQty.compareTo(qty) == 0){
                if (existNc){
                    updateTrackInfo(sfcObj.getSfc(), userInfo.getOperationBo(),"4");
                }else {
                    updateTrackInfo(sfcObj.getSfc(), userInfo.getOperationBo(),"3");
                    //把sfc上的工序更新为下一道工序
                    updateOperationBySfc(sfcObj,userInfo.getOperationBo());
                }
                clearInfoBySfcAndOp(sfcObj.getSfc(),userInfo.getOperationBo());//清空所有的信息通过sfc和工序
            }else {
                extracted(sfcObj, userInfo, flag);
            }
        }else {
            //找到这个工序之前的工序的全部报废数
            BigDecimal currentOpPreviousScrapQty = getCurrentOpPreviousScrapQty(sfcObj, userInfo.getOperationBo());
            totalQty = currentOpOutTotalQty.add(currentOpPreviousScrapQty);
            if (totalQty.compareTo(qty) == 0){
                if (existNc){
                    updateTrackInfo(sfcObj.getSfc(), userInfo.getOperationBo(),"4");
                }else {
                    updateTrackInfo(sfcObj.getSfc(), userInfo.getOperationBo(),"3");
                    updateOperationBySfc(sfcObj,userInfo.getOperationBo());
                    //判断是否是最后一道过站工序
                    List<String> routerOrderList = sfcServiceImpl.getRouterOrderList(sfcObj.getSfcRouterBo());
                    String lastOperationBo = routerOrderList.get(routerOrderList.size() - 2);//最后一道过站工序
                    if (lastOperationBo.equals(userInfo.getOperationBo())){
                        //发送站内信
                        sfcServiceImpl.sendStationMsg(routerOrderList.get(routerOrderList.size() - 1),sfcObj.getWorkShopBo(),sfcObj.getSfc());
                    }
                }
                clearInfoBySfcAndOp(sfcObj.getSfc(),userInfo.getOperationBo());//清空所有的信息通过sfc和工序
            }else {
                extracted(sfcObj, userInfo, flag);
            }
        }
    }

//    /**
//     * 首工序查询跳站
//     * @param firstOperationBo
//     * @param sfc
//     * @return
//     */
//    public BigDecimal getFirstOpSkipStationScrapQty(String firstOperationBo,String sfc){
//
//    }
    /**
     * 获取该sfc拆分的标签数量
     * @return
     */
    public BigDecimal getSplitQty(String sfcBo){
        List<Sfc> sfcList = sfcMapper.selectList(new QueryWrapper<Sfc>().eq("init_parent_sfc_bo", sfcBo));
        BigDecimal splitQty = BigDecimal.ZERO;//标签拆分的数量
        for (Sfc sfc : sfcList) {
            splitQty = splitQty.add(sfc.getSfcQty());
        }
        return splitQty;
    }
    /**
     * 更新sfc为下一道工序
     * @param sfcObj
     */
    public void updateOperationBySfc(Sfc sfcObj,String operationBo) throws CommonException {

//        RouterProcess routerProcess = routerProcessService.getOne(new QueryWrapper<RouterProcess>().eq("router_bo", sfcObj.getSfcRouterBo()));
//        String processInfo = routerProcess.getProcessInfo();
//        JSONObject jsonObj = JSON.parseObject(processInfo);
//
//        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
//        JSONArray lineList = JSONArray.parseArray(jsonObj.getString("lineList"));
//
//        String nextId = sfcServiceImpl.getNextId(lineList, sfcObj.getOpIds());
//        String nextOperationBo = sfcServiceImpl.getOperationById(nodeList, nextId);
//        Sfc sfc = new Sfc();
//        sfc.setBo(sfcObj.getBo());
//        sfc.setOperationBo(nextOperationBo);
//        sfc.setOpIds(nextId);
//        sfcMapper.updateById(sfc);
        QueryWrapper<RouterProcessTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("position_flag");
        queryWrapper.eq("router_bo",sfcObj.getSfcRouterBo());
        List<RouterProcessTable> routerProcessTableList = routerProcessTableMapper.selectList(queryWrapper);
        String nextOperationBo = null;
        for (int i = 0; i < routerProcessTableList.size(); i++) {
            if (routerProcessTableList.get(i).getOperationBo().equals(operationBo)){
                nextOperationBo = routerProcessTableList.get(i + 1).getOperationBo();
            }
        }
        Sfc sfc = new Sfc();
        sfc.setBo(sfcObj.getBo());
        sfc.setOperationBo(nextOperationBo);
        sfcMapper.updateById(sfc);
    }
    /**
     * 判断是否存在不良
     * @param sfc
     * @param operationBo
     * @return
     */
    private boolean isExistNc(String sfc,String operationBo){
        List<SfcNcLog> sfcNcLogs = sfcNcLogMapper.selectList(new QueryWrapper<SfcNcLog>().
                eq("sfc", sfc).
                eq("operation_bo", operationBo).
                eq("is_repaired", "0").
                ne("nc_qty",0));
        if (CollectionUtil.isNotEmpty(sfcNcLogs)){
            return true;
        }
        return false;
    }
    /**
     * 校验该sfc在该工序是否把设备全部出站
     * @param sfc
     * @param operationBo
     */
    private boolean checkSfcAndOpIsAllOut(String sfc,String operationBo){
        List<SfcDevice> sfcDevices = sfcDeviceMapper.selectList(new QueryWrapper<SfcDevice>().eq("sfc", sfc).eq("operation_bo", operationBo));
        if (ObjectUtil.isNotEmpty(sfcDevices)){
            return false;
        }
        return true;
    }
    /**
     * 清空所有挂着这条sfc的设备上的信息
     * @param sfc
     * @param operationBo
     */
    private void clearInfoBySfcAndOp(String sfc,String operationBo){
        SfcDevice sfcDeviceEntity = new SfcDevice();
        sfcDeviceEntity.setSfc("");
        sfcDeviceEntity.setShopOrderBo("");
        sfcDeviceEntity.setOperationOrder("");
        sfcDeviceEntity.setUserBo("");
        sfcDeviceEntity.setDispatchCode("");
        sfcDeviceEntity.setOperationBo("");
        sfcDeviceMapper.update(sfcDeviceEntity,new QueryWrapper<SfcDevice>().eq("sfc",sfc).eq("operation_bo",operationBo));
    }
    private void extracted(Sfc sfcObj, UserInfo userInfo, boolean flag) throws CommonException {
        if (flag){
            updateTrackInfo(sfcObj.getSfc(), userInfo.getOperationBo(),"2");
        }else {//本次出站没有把设备出站完（比如进站进了两个设备，而出站只出了一个）
            BigDecimal canOutQty = calculationCanOutQty(sfcObj.getSfc());
            if (canOutQty.compareTo(BigDecimal.ZERO) == 1){//如果还剩可出站数
                updateTrackInfo(sfcObj.getSfc(), userInfo.getOperationBo(),"1");//跟踪表状态依旧为生产中
            }else {
                clearInfoBySfcAndOp(sfcObj.getSfc(), userInfo.getOperationBo());//清空所有的信息通过sfc和工序
                updateTrackInfo(sfcObj.getSfc(), userInfo.getOperationBo(),"2");
            }
        }
    }
    /**
     * 找到当前工序之前的工序的全部报废数（如果有跳站的加上跳站的）
     * @param sfcObj
     * @param currentOperation
     * @return
     */
    public BigDecimal getCurrentOpPreviousScrapQty(Sfc sfcObj,String currentOperation) throws CommonException {
        List<String> scrapOpList = Lists.newArrayList();
        String previousOp = getPreviousOp(sfcObj.getSfcRouterBo(), currentOperation, sfcObj.getSfc());
        scrapOpList.add(previousOp);

        String previousOp_ = previousOp;
        while (true){
            previousOp_ = getPreviousOp(sfcObj.getSfcRouterBo(), previousOp_, sfcObj.getSfc());
            if (StringUtils.isBlank(previousOp_)){
                break;
            }
            scrapOpList.add(previousOp_);
        }
        if ("1".equals(sfcObj.getIsSkipStationSfc())){//如果是跳站过来的
            List<SfcSkipBeforeOperation> SfcSkipBeforeOperationList = sfcSkipBeforeOperationMapper.selectList(new QueryWrapper<SfcSkipBeforeOperation>().eq("sfc", sfcObj.getSfc()));
            for (SfcSkipBeforeOperation sfcSkipBeforeOperation : SfcSkipBeforeOperationList) {
                scrapOpList.add(sfcSkipBeforeOperation.getOperationBo());
            }
        }
        List<String> distinctList = scrapOpList.stream().distinct().collect(Collectors.toList());//去重之后的

        QueryWrapper<SfcWiplog> wiplogQueryWrapper = new QueryWrapper<>();
        wiplogQueryWrapper.eq("sfc",sfcObj.getSfc());
        wiplogQueryWrapper.in("operation_bo",distinctList);
        List<SfcWiplog> sfcWiplogs = sfcWiplogMapper.selectList(wiplogQueryWrapper);
        BigDecimal scrapTotalQty = BigDecimal.ZERO;//报废总数
        for (SfcWiplog sfcWiplog : sfcWiplogs) {
            scrapTotalQty = scrapTotalQty.add(sfcWiplog.getScrapQty());
        }
        return scrapTotalQty;
    }
    /**
     * 计算可出站数量（普通工序）
     * @return
     */
    public BigDecimal calculationCanOutQty(String sfc) throws CommonException {
        Sfc sfcObj = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("sfc", sfc));
        UserInfo userInfo = sfcServiceImpl.getUserInfo();
        BigDecimal currentOpOutQty = getCurrentOpOutQty(sfcObj.getSfc(),userInfo.getOperationBo());//当前工序已经出站的数量
        BigDecimal canOutQty;

        boolean isTheFirstOp;//判断是否是首工序
        if ("1".equals(sfcObj.getIsSkipStationSfc()) && userInfo.getOperationBo().equals(sfcObj.getSkipOperationBo())){
            isTheFirstOp = true;//这个条件虽然不是判断是不是首工序，但是是一样的效果，都是为了计算可出站数量：sfc数量+标签拆分的数量-本工序已经出站的数量
        }else {
            isTheFirstOp = sfcServiceImpl.checkIsFirstOperation(sfcObj.getSfcRouterBo(),userInfo.getOperationBo());
        }
        if (isTheFirstOp){
            BigDecimal splitQty = BigDecimal.ZERO;//标签拆分的数量
            List<Sfc> sfcList = sfcMapper.selectList(new QueryWrapper<Sfc>().eq("init_parent_sfc_bo", sfcObj.getBo()));
            for (Sfc sfcEntity : sfcList) {
                splitQty = splitQty.add(sfcEntity.getSfcQty());
            }
            canOutQty = sfcObj.getSfcQty().add(splitQty).subtract(currentOpOutQty).subtract(sfcObj.getSkipBeforeScrapQty());
        }else {
            //找到上一个工序
//            String previousOperation = getPreviousOpByCurrentOp(sfcObj.getSfcRouterBo(), userInfo.getOperationBo());
            String previousOperation = getPreviousOp(sfcObj.getSfcRouterBo(), userInfo.getOperationBo(),sfcObj.getSfc());
            BigDecimal previousOpOutQty = getPreviousOpOutQty(sfcObj.getSfc(),previousOperation);//上一个工序出站良品数量
            canOutQty = previousOpOutQty.subtract(currentOpOutQty);//可出站数量
        }
        return canOutQty;
    }

    /**
     * 计算可出站数量（检验工序）
     * @param sfc
     * @return
     */
    public BigDecimal calculationTestOpCanOutQty(String sfc,String routerBo) throws CommonException {
        UserInfo userInfo = sfcServiceImpl.getUserInfo();
        BigDecimal testOpOutedQty = BigDecimal.ZERO;//检验工序已经出过站的数量
        List<SfcWiplog> sfcWiplogs = sfcWiplogMapper.selectList(new QueryWrapper<SfcWiplog>().eq("sfc", sfc).eq("operation_bo", userInfo.getOperationBo()));
        BigDecimal scrapQty = BigDecimal.ZERO;//检验工序报废数量
        for (SfcWiplog sfcWiplog : sfcWiplogs) {
            testOpOutedQty = testOpOutedQty.add(sfcWiplog.getDoneQty());
            scrapQty = scrapQty.add(sfcWiplog.getScrapQty());
        }
        //找到上一个工序
//        String previousOperation = getPreviousOpByCurrentOp(routerBo, userInfo.getOperationBo());
        String previousOperation = getPreviousOp(routerBo,userInfo.getOperationBo(),sfc);
        BigDecimal previousOpOutQty = getPreviousOpOutQty(sfc,previousOperation);//上一个工序出站良品数量

        BigDecimal testOpCanOutQty = previousOpOutQty.subtract(testOpOutedQty).subtract(scrapQty);//检验工序可出站数量
        return testOpCanOutQty;
    }

    /**
     * 获取检验工序 + 所有报废的数量
     * @return
     */
    public BigDecimal getTestOpOutQtyAndScrapTotalQty(String sfc,String operationBo) throws CommonException {
        QueryWrapper<SfcWiplog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sfc",sfc);

        BigDecimal scrapTotalQty = BigDecimal.ZERO;//报废总数

        List<SfcWiplog> scrapList = sfcWiplogMapper.selectList(queryWrapper);
        for (SfcWiplog sfcWiplog : scrapList) {
            scrapTotalQty = scrapTotalQty.add(sfcWiplog.getScrapQty());
        }
        queryWrapper.eq("operation_bo",operationBo);
        List<SfcWiplog> sfcWiplogsTestOp = sfcWiplogMapper.selectList(queryWrapper);
        BigDecimal testOpOutQty = BigDecimal.ZERO;//检验工序出站数量

        for (SfcWiplog sfcWiplogTest : sfcWiplogsTestOp) {
            testOpOutQty = testOpOutQty.add(sfcWiplogTest.getDoneQty());
        }
        return scrapTotalQty.add(testOpOutQty);
    }
    /**
     * 处理sfc完成情况
     */
    private void handleSfcFinishNew(Sfc sfcObj,BigDecimal ncQty) throws CommonException {
        //检验该sfc是否完成：检验工序出站数量 + 所有的报废数量= sfc数量
        //总数
        BigDecimal qty = getTestOpOutQtyAndScrapTotalQty(sfcObj.getSfc(),sfcServiceImpl.getUserInfo().getOperationBo()).add(ncQty);
        if (qty.compareTo(sfcObj.getSfcQty()) == 0){//说明sfc已完成
            //更新SFC状态
            Sfc sfc = new Sfc();
            sfc.setOperationBo(sfcServiceImpl.getUserInfo().getOperationBo());
            if (ncQty.compareTo(BigDecimal.ZERO) == 0){
                sfc.setState("已完成");
            }
            sfcMapper.update(sfc,new QueryWrapper<Sfc>().eq("sfc",sfcObj.getSfc()));

            //校验工单是否已完成
            QueryWrapper<Sfc> sfcQw = new QueryWrapper<>();
            sfcQw.eq("state","已完成");
            sfcQw.eq("shop_order_bo", sfcObj.getShopOrderBo());
            List<Sfc> sfcList = sfcMapper.selectList(sfcQw);
            BigDecimal sfcTotalQty = BigDecimal.ZERO;//工单完成的总数
            for (Sfc s : sfcList) {
                sfcTotalQty = sfcTotalQty.add(s.getSfcQty());
            }
            BigDecimal orderQty = shopOrderService.getOne(new QueryWrapper<ShopOrder>().eq("bo", sfcObj.getShopOrderBo())).getOrderQty();//工单总数
            if (sfcTotalQty.compareTo(orderQty) == 0){
                ShopOrder shopOrder = new ShopOrder();
                shopOrder.setBo(sfcObj.getShopOrderBo());
                shopOrder.setStateBo("STATE:dongyin,503");
                shopOrder.setEndTime(new Date());
                shopOrderService.updateById(shopOrder);
            }

            //校验工序工单是否完成
            QueryWrapper<Sfc> sfcQueryWrapper = new QueryWrapper<>();
            sfcQueryWrapper.eq("state","已完成");
            sfcQueryWrapper.eq("operation_order", sfcObj.getOperationOrder());
            List<Sfc> sfcObjList = sfcMapper.selectList(sfcQueryWrapper);
            BigDecimal sfcOpOrderTotalQty = BigDecimal.ZERO;//工序工单完成的总数
            for (Sfc s : sfcObjList) {
                sfcOpOrderTotalQty = sfcOpOrderTotalQty.add(s.getSfcQty());
            }
            BigDecimal operationOrderQty = operationOrderService.getOne(new QueryWrapper<OperationOrder>().eq("operation_order", sfcObj.getOperationOrder())).getOperationOrderQty();//工序工单总数

            if (sfcOpOrderTotalQty.compareTo(operationOrderQty) == 0){
                OperationOrder operationOrder = new OperationOrder();
                operationOrder.setOperationOrderState("2");//更新为已完成
                operationOrderService.update(operationOrder,new QueryWrapper<OperationOrder>().eq("operation_order", sfcObj.getOperationOrder()));
            }
            updateTrackBySfc(sfcObj.getSfc());
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CommonException("该标签数量为"+sfcObj.getSfcQty()+",请一次性出站完！",504);
        }
    }
}
