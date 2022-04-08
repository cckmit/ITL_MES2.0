package com.itl.mes.core.provider.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.entity.IapSysUserStationT;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.dto.StemStepConfigDTO;
import com.itl.mes.core.api.dto.StepProduceDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.DispatchService;
import com.itl.mes.core.api.service.OperationOrderService;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.service.StepProduceService;
import com.itl.mes.core.api.vo.StepProduceVo;
import com.itl.mes.core.provider.mapper.StemOperationConfigMapper;
import com.itl.mes.core.provider.mapper.StepProduceMapper;
import com.itl.mes.core.provider.service.impl.SfcServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = "线圈生产执行")
@RestController
@RequestMapping("/stepProduce")
public class StepProduceController {

    @Autowired
    private StepProduceService stepProduceService;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private StemOperationConfigMapper stemOperationConfigMapper;

    @Autowired
    private OperationService operationService;

    @Autowired
    private StepProduceMapper stepProduceMapper;

    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private OperationOrderService operationOrderService;
    /**
     * 查询所有可进站的工序工单
     */
    @ApiOperation(value = "查询所有可进站的工序工单")
    @PostMapping("/getCanInputStationOpOrder")
    public ResponseData<IPage<StepProduceVo>> getCanInputStationOpOrder(@RequestBody StepProduceDTO stepProduceDTO){
        return ResponseData.success(stepProduceService.getCanInputStationOpOrder(stepProduceDTO));
    }

    /**
     * 线圈进站
     * @return
     */
    @ApiOperation(value = "线圈进站")
    @PostMapping("/stepInStation")
    @Transactional
    public ResponseData stepInStation(@RequestBody List<StepProduceDTO> stepProduceDTOS) throws CommonException {
        for (StepProduceDTO stepProduceDTO : stepProduceDTOS) {
            StepProduce stepProduce = new StepProduce();
            BeanUtils.copyProperties(stepProduceDTO,stepProduce);
            stepProduce.setState(StepProduce.INPUT_FLAG);
            stepProduce.setUserBo(sfcServiceImpl.getUserInfo().getUserBo());

            OperationOrder operationOrderEntity = operationOrderService.getOne(new QueryWrapper<OperationOrder>().eq("operation_order", stepProduceDTO.getOperationOrder()));
            BigDecimal orderQty = operationOrderEntity.getOperationOrderQty();//工序工单总数

            QueryWrapper<StepProduce> stepProduceQw = new QueryWrapper<>();
            stepProduceQw.eq("state",StepProduce.INPUT_FLAG);
            stepProduceQw.eq("operation_order",stepProduceDTO.getOperationOrder());
            stepProduceQw.eq("operation_bo",stepProduceDTO.getOperationBo());

            List<StepProduce> stepProduceList = stepProduceService.list(stepProduceQw);
            BigDecimal enteredQty = BigDecimal.ZERO;//该工序工单在此工序已经进站的数量
            for (StepProduce produce : stepProduceList) {
                enteredQty = enteredQty.add(produce.getQty());
            }
            enteredQty = enteredQty.add(stepProduceDTO.getQty());//加上本次的数量

            if (orderQty.compareTo(enteredQty) == 0){//如果相等，说明在此工序已经全部进站完成，把m_operation_order表中is_stem_input_finished字段更新为1
                OperationOrder operationOrder = new OperationOrder();
                operationOrder.setIsStemInputFinished(OperationOrder.INPUT_FINISHED_SUCCESS_FLAG);
                operationOrderService.update(operationOrder,new QueryWrapper<OperationOrder>().eq("operation_order",stepProduceDTO.getOperationOrder()));
            }

            stepProduce.setId(UUID.uuid32());
            stepProduce.setCreateDate(new Date());
//            stepProduce.setDispatchCode(stepProduceDTO.getDispatchCode());
            if (stepProduceDTO.getQty().compareTo(BigDecimal.ZERO) != 0){
                stepProduceService.save(stepProduce);
            }
        }
        return ResponseData.success();
    }

    @ApiOperation(value = "查询所有可以出站的工序工单")
    @PostMapping("/getCanOutputStationOpOrder")
    public ResponseData<IPage<StepProduceVo>> getCanOutputStationOpOrder(@RequestBody StepProduceDTO stepProduceDTO){
        return ResponseData.success(stepProduceService.getCanOutputStationOpOrder(stepProduceDTO));
    }

    @ApiOperation(value = "线圈出站")
    @PostMapping("/stepOutStation")
    public ResponseData<String> stepOutStation(@RequestBody StepProduceDTO stepProduceDTO) throws CommonException {
        return ResponseData.success(stepProduceService.stepOutStation(stepProduceDTO));
    }

    @ApiOperation(value = "线圈工序配置")
    @PostMapping("/stemStepConfig")
    public ResponseData stemOperationConfig(@RequestBody StemStepConfigDTO stemStepConfigDTO) throws CommonException {
        //新增配置
        StemOperationConfig stemOperationConfig = new StemOperationConfig();
        stemOperationConfig.setId(UUID.uuid32());
        stemOperationConfig.setOperationBo(stemStepConfigDTO.getOperationBo());
        stemOperationConfig.setCreateDate(new Date());
        stemOperationConfig.setCreateUser(sfcServiceImpl.getUserInfo().getUserBo());
        stemOperationConfig.setCodeRuleType(stemStepConfigDTO.getCodeRuleType());
        stemOperationConfig.setLabelId(stemStepConfigDTO.getLabelId());

        stemOperationConfigMapper.insert(stemOperationConfig);
        return ResponseData.success("配置成功");
    }

    @ApiOperation(value = "查询当前配置的工序（取最新的一个配置工序）")
    @GetMapping("/selectCurrentOpConfig")
    public ResponseData<StemOperationConfig> selectCurrentOpConfig(){
        StemOperationConfig stemOperationConfig = stepProduceService.selectCurrentOpConfig();
        if (ObjectUtil.isNotEmpty(stemOperationConfig)){
            return ResponseData.success(stemOperationConfig);
        }
        return ResponseData.error("当前没有配置的工序");
    }

    @ApiOperation(value = "通过当前工位查询所有在这个工位的用户")
    @GetMapping("/selectUserByStation")
    public ResponseData<List<IapSysUserT>> selectUserByStation() throws CommonException {
        String stationBo = sfcServiceImpl.getUserInfo().getStationBo();//当前工位
        List<IapSysUserStationT> iapSysUserStationTS = stepProduceMapper.selectUserIdByStationBo(stationBo);
        List<IapSysUserT> userTSList = Lists.newArrayList();
        for (IapSysUserStationT iapSysUserStationT : iapSysUserStationTS) {
            String userId = iapSysUserStationT.getUserId();
            IapSysUserT userTS = stepProduceMapper.selectUserInfoById(userId,null);
            userTSList.add(userTS);
        }
        return ResponseData.success(userTSList);
    }
}
