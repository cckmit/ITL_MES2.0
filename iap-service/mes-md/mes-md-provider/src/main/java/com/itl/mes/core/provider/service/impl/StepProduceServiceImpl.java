package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.dto.StepProduceDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.StepProduceVo;
import com.itl.mes.core.provider.mapper.SfcWiplogMapper;
import com.itl.mes.core.provider.mapper.StemOperationConfigMapper;
import com.itl.mes.core.provider.mapper.StepProduceMapper;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StepProduceServiceImpl extends ServiceImpl<StepProduceMapper, StepProduce> implements StepProduceService {

    @Autowired
    private StepProduceMapper stepProduceMapper;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private CreateSnService createSnService;

    @Autowired
    private SfcService sfcService;

    @Autowired
    private RouterProcessService routerProcessService;

    @Autowired
    private StationService stationService;

    @Autowired
    private ReportWorkService reportWorkService;

    @Autowired
    private StemOperationConfigMapper stemOperationConfigMapper;

    @Autowired
    private OperationService operationService;

    @Autowired
    private SfcWiplogMapper sfcWiplogMapper;
    @Override
    public IPage<StepProduceVo> getCanInputStationOpOrder(StepProduceDTO stepProduceDTO) {
        if (ObjectUtil.isEmpty(stepProduceDTO.getPage())){
            stepProduceDTO.setPage(new Page(1,10));
        }
//        stepProduceDTO.setOperationBo("OP:dongyin,ZZ-13,1.0");//??????????????????
        encapsulationItemDescKeyWord(stepProduceDTO);
        IPage<StepProduceVo> canInputStationOpOrder = stepProduceMapper.getCanInputStationOpOrder(stepProduceDTO.getPage(), stepProduceDTO);
        for (int i = 0; i < canInputStationOpOrder.getRecords().size(); i++) {
            //???????????????????????????????????????????????????
            QueryWrapper<StepProduce> stepProduceQueryWrapper = new QueryWrapper<>();
            stepProduceQueryWrapper.eq("state","0");//????????????
            stepProduceQueryWrapper.eq("operation_order",canInputStationOpOrder.getRecords().get(i).getOperationOrder());
            stepProduceQueryWrapper.eq("operation_bo",stepProduceDTO.getOperationBo());

            List<StepProduce> stepProduces = stepProduceMapper.selectList(stepProduceQueryWrapper);
            BigDecimal inputQty = BigDecimal.ZERO;//???????????????????????????????????????
            BigDecimal orderQty = canInputStationOpOrder.getRecords().get(i).getOrderQty();//??????????????????
            for (StepProduce stepProduce : stepProduces) {
                inputQty = inputQty.add(stepProduce.getQty());
            }
            BigDecimal canInputQty = orderQty.subtract(inputQty);//????????????
//            if (canInputQty.compareTo(BigDecimal.ZERO) == 0){//???????????????????????????????????????????????????????????????
//                canInputStationOpOrder.getRecords().remove(i);
//                i--;
//                continue;
//            }
            canInputStationOpOrder.getRecords().get(i).setCanInputQty(canInputQty);
        }
        return canInputStationOpOrder;
    }

    @Override
    public IPage<StepProduceVo> getCanOutputStationOpOrder(StepProduceDTO stepProduceDTO) {
        if (ObjectUtil.isEmpty(stepProduceDTO.getPage())){
            stepProduceDTO.setPage(new Page(1,10));
        }
        encapsulationItemDescKeyWord(stepProduceDTO);
        IPage<StepProduceVo> canOutputStationOpOrder = stepProduceMapper.getCanOutputStationOpOrder(stepProduceDTO.getPage(), stepProduceDTO);
        for (int i = 0; i < canOutputStationOpOrder.getRecords().size(); i++) {
            //???????????????????????????????????????
            QueryWrapper<StepProduce> stepProduceQueryWrapper = new QueryWrapper<>();
            stepProduceQueryWrapper.eq("state",StepProduce.OUTPUT_FLAG);//????????????
            stepProduceQueryWrapper.eq("operation_order",canOutputStationOpOrder.getRecords().get(i).getOperationOrder());
            stepProduceQueryWrapper.eq("operation_bo",stepProduceDTO.getOperationBo());

            List<StepProduce> stepProduces = stepProduceMapper.selectList(stepProduceQueryWrapper);
            BigDecimal outQty = BigDecimal.ZERO;//???????????????????????????????????????
            BigDecimal inputQty = canOutputStationOpOrder.getRecords().get(i).getInputQty();//????????????
            for (StepProduce stepProduce : stepProduces) {
                outQty = outQty.add(stepProduce.getQty());
            }
            BigDecimal canOutputQty = inputQty.subtract(outQty);//????????????
            canOutputStationOpOrder.getRecords().get(i).setCanOutputQty(canOutputQty);

            //?????????????????????????????????????????????????????????????????????????????????
            QueryWrapper<StepProduce> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("state",StepProduce.INPUT_FLAG);
            queryWrapper.eq("operation_order",canOutputStationOpOrder.getRecords().get(i).getOperationOrder());
            queryWrapper.eq("operation_bo",stepProduceDTO.getOperationBo());
            queryWrapper.orderByDesc("create_date");

            List<StepProduce> stepProducesTimeList = stepProduceMapper.selectList(queryWrapper);
            canOutputStationOpOrder.getRecords().get(i).setCreateDate(stepProducesTimeList.get(0).getCreateDate());

            //??????????????????????????????????????????????????????????????????????????????????????????????????? , ????????????
            List<String> userBos = Lists.newArrayList();//???????????????
            for (StepProduce stepProduce : stepProducesTimeList) {
                userBos.add(stepProduce.getUserBo());
            }
            List<String> userDisBos = userBos.stream().distinct().collect(Collectors.toList());//????????????????????????
            String userName = null;//????????????????????????
            for (String userDisBo : userDisBos) {
                String realName = stepProduceMapper.selectUserInfoById(null, userDisBo).getRealName();
                if (StringUtils.isNotBlank(userName))userName = userName + "," + realName;
                else userName = realName;
            }
            canOutputStationOpOrder.getRecords().get(i).setCreateUser(userName);
        }
        return canOutputStationOpOrder;
    }

    @Override
    @Transactional
    public String stepOutStation(StepProduceDTO stepProduceDTO) throws CommonException {
        BigDecimal canOutQty = checkOutStationQty(stepProduceDTO.getOperationOrder(), stepProduceDTO.getOperationBo());
        if (stepProduceDTO.getQty().compareTo(canOutQty) == 1){
            throw new CommonException("????????????????????????????????????????????????",504);
        }
        Date date = new Date();
        String sfcCode = "";
        //======>1?????????
        StepProduce stepProduce = new StepProduce();
        BeanUtils.copyProperties(stepProduceDTO,stepProduce);
        stepProduce.setState(StepProduce.OUTPUT_FLAG);
        stepProduce.setUserBo(sfcServiceImpl.getUserInfo().getUserBo());
        stepProduce.setId(UUID.uuid32());
        stepProduce.setCreateDate(date);
        stepProduce.setReportWorkUser(stepProduceDTO.getReportWorkUser());
        if (stepProduce.getQty().compareTo(BigDecimal.ZERO) != 0){

            //??????????????????
            QueryWrapper<StepProduce> qw = new QueryWrapper<>();
            qw.eq("state",StepProduce.INPUT_FLAG);
            qw.eq("operation_order",stepProduceDTO.getOperationOrder());
            qw.eq("operation_bo",stepProduceDTO.getOperationBo());

            List<StepProduce> stepProduceInputList = stepProduceMapper.selectList(qw);
            BigDecimal inputTotal = BigDecimal.ZERO;//????????????

            for (StepProduce produce : stepProduceInputList) {
                inputTotal = inputTotal.add(produce.getQty());
            }
            //???????????????????????????????????????????????????
            QueryWrapper<StepProduce> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("state",StepProduce.OUTPUT_FLAG);
            queryWrapper.eq("operation_order",stepProduceDTO.getOperationOrder());
            queryWrapper.eq("operation_bo",stepProduceDTO.getOperationBo());

            List<StepProduce> stepProduces = stepProduceMapper.selectList(queryWrapper);
            BigDecimal outedQty = BigDecimal.ZERO;//????????????????????????????????????????????????
            for (StepProduce produce : stepProduces) {
                outedQty = outedQty.add(produce.getQty());
            }
            outedQty = outedQty.add(stepProduceDTO.getQty());//?????????????????????
            if (inputTotal.compareTo(outedQty) == 0){//?????????????????????????????????????????????????????????,?????????????????????????????????????????????????????????IS_STEM_OUTPUT_FINISHED???????????????1
                StepProduce stepProduceEntity = new StepProduce();
                stepProduceEntity.setIsStemOutputFinished(StepProduce.OUTPUT_FLAG);

                QueryWrapper<StepProduce> stepProduceQueryWrapper = new QueryWrapper<>();
                stepProduceQueryWrapper.eq("state",StepProduce.INPUT_FLAG);
                stepProduceQueryWrapper.eq("operation_order",stepProduceDTO.getOperationOrder());
                stepProduceQueryWrapper.eq("operation_bo",stepProduceDTO.getOperationBo());

                stepProduceMapper.update(stepProduceEntity,stepProduceQueryWrapper);
            }
            stepProduceMapper.insert(stepProduce);

            //======>2???????????????
            List<StemOperationConfig> stemOperationConfigs = stemOperationConfigMapper.selectList(new QueryWrapper<StemOperationConfig>().orderByDesc("create_date"));
            String codeRuleType = stemOperationConfigs.get(0).getCodeRuleType();//???????????????????????????????????????
            List<Sfc> sfcList = createSnService.createSnByRule("1", codeRuleType, stepProduce.getQty().toString(),
                    stepProduceDTO.getOperationOrder(), null,1);

            Sfc sfc = sfcList.get(0);
            sfcCode = sfc.getSfc();
            //??????????????????
            Sfc sfcObj = sfcService.getOne(new QueryWrapper<Sfc>().eq("sfc",sfcCode));
            SfcWiplog sfcWiplog = new SfcWiplog();
            BeanUtils.copyProperties(sfcObj,sfcWiplog);
            sfcWiplog.setBo(UUID.uuid32());
            sfcWiplog.setInTime(date);
            sfcWiplog.setOutTime(date);
            sfcWiplog.setInputQty(stepProduce.getQty());
            sfcWiplog.setDoneQty(stepProduce.getQty());
            sfcWiplog.setNcQty(BigDecimal.ZERO);
            sfcWiplog.setScrapQty(BigDecimal.ZERO);
            sfcWiplog.setState("??????");
            sfcWiplog.setOperationBo(stepProduceDTO.getOperationBo());
            sfcWiplog.setUserBo(sfcServiceImpl.getUserInfo().getUserBo());
            sfcWiplog.setStationBo(sfcServiceImpl.getUserInfo().getStationBo());
            sfcWiplogMapper.insert(sfcWiplog);

            //======>3???????????????
            ReportWork reportWork = new ReportWork();
            String workStepBo = stationService.getOne(new QueryWrapper<Station>().eq("station", sfcServiceImpl.getUserInfo().getStation())).getWorkstationBo();
            reportWork.setSfc(sfc.getSfc());
            reportWork.setItemBo(stepProduceDTO.getItemBo());
            reportWork.setWorkStepCodeBo(workStepBo);
            reportWork.setOperationBo(stepProduceDTO.getOperationBo());
            reportWork.setShopOrderBo(stepProduceDTO.getShopOrderBo());
            reportWork.setOperationOrder(stepProduceDTO.getOperationOrder());
            reportWork.setUserBo(stepProduceDTO.getReportWorkUser());
            reportWork.setQty(stepProduce.getQty());
            reportWork.setMeSfcWipLogBo(stepProduce.getId());
            reportWork.setState("0");
            reportWork.setTime(date);
            reportWork.setBo(UUID.uuid32());
            reportWork.setCreateTime(date);
            reportWorkService.save(reportWork);
        }
        return sfcCode;
    }

    @Override
    public StemOperationConfig selectCurrentOpConfig() {
        List<StemOperationConfig> stemOperationConfigs = stemOperationConfigMapper.selectList(new QueryWrapper<StemOperationConfig>().orderByDesc("create_date"));
        if (CollectionUtil.isNotEmpty(stemOperationConfigs)){
            String operationBo = stemOperationConfigs.get(0).getOperationBo();
            Operation operation = operationService.getById(operationBo);
            stemOperationConfigs.get(0).setOperationName(operation.getOperationName());
            stemOperationConfigs.get(0).setOperation(operation.getOperation());
            stemOperationConfigs.get(0).setBo(operationBo);
            return stemOperationConfigs.get(0);
        }
        return null;
    }

    /**
     * ???????????????????????????
     * @param stepProduceDTO
     */
    private void encapsulationItemDescKeyWord(StepProduceDTO stepProduceDTO){
        if (StringUtils.isNotBlank(stepProduceDTO.getItemDescKeyWord())){
            String keyword = stepProduceDTO.getItemDescKeyWord();
            List<String> itemDescKeyWordList = Lists.newArrayList();
            for (String s : keyword.split("\\*")) {
                String word = "\"" + s + "*" + "\"";
                itemDescKeyWordList.add(word);
            }
            stepProduceDTO.setItemDescKeyWordList(itemDescKeyWordList);
        }
    }

    /**
     * ??????????????????????????????(?????????????????????)
     * @param operationOrder
     * @param operationBo
     */
    private BigDecimal checkOutStationQty(String operationOrder,String operationBo){
        String s = stepProduceMapper.selectInputQty(operationOrder, operationBo);
        BigDecimal inputQty;//????????????
        if (StringUtils.isBlank(s)){
            inputQty = BigDecimal.ZERO;
        }else {
            inputQty = new BigDecimal(s);
        }
        //???????????????????????????????????????
        QueryWrapper<StepProduce> stepProduceQueryWrapper = new QueryWrapper<>();
        stepProduceQueryWrapper.eq("state",StepProduce.OUTPUT_FLAG);//????????????
        stepProduceQueryWrapper.eq("operation_order",operationOrder);
        stepProduceQueryWrapper.eq("operation_bo",operationBo);

        List<StepProduce> stepProduces = stepProduceMapper.selectList(stepProduceQueryWrapper);
        BigDecimal outQty = BigDecimal.ZERO;//???????????????????????????????????????

        for (StepProduce stepProduce : stepProduces) {
            outQty = outQty.add(stepProduce.getQty());
        }
        return inputQty.subtract(outQty);
    }
}
