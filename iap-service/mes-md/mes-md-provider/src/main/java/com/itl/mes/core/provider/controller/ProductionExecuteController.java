package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.OutStationDto;
import com.itl.mes.core.api.dto.ReworkDto;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.dto.SkipStationDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.DeviceService;
import com.itl.mes.core.api.service.ProductionExecuteService;
import com.itl.mes.core.api.service.SfcService;
import com.itl.mes.core.api.service.ShopOrderService;
import com.itl.mes.core.api.vo.ReworkVo;
import com.itl.mes.core.api.vo.SfcVo;
import com.itl.mes.core.api.vo.WorkShopVo;
import com.itl.mes.core.provider.mapper.*;
import com.itl.mes.core.provider.service.impl.DataServiceImpl;
import com.itl.mes.core.provider.service.impl.ProductionExecuteServiceImpl;
import com.itl.mes.core.provider.service.impl.SfcServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productionExecute")
@Api(tags = " 生产执行 " )
public class ProductionExecuteController{

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SfcService sfcService;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private DataServiceImpl dataServiceImpl;

    @Autowired
    private SfcMapper sfcMapper;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private ProductionExecuteService productionExecuteService;

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private StationMapper stationMapper;

    @ApiOperation(value = "查询工位、工序等基本信息")
    @GetMapping("/getUserInfo")
    public ResponseData<UserInfo> getUserInfo() throws CommonException {
        return ResponseData.success(sfcServiceImpl.getUserInfo());
    }

    @ApiOperation(value = "通过sfc编码获取sfc相关信息（进站）")
    @GetMapping("/getSfcInfoBySfcIn")
    public ResponseData<SfcVo> getSfcInfoBySfcIn(@RequestParam(value = "sfc") String sfc) throws CommonException {
        return ResponseData.success(productionExecuteService.getSfcInfoBySfcIn(sfc));
    }

    @ApiOperation(value = "进站")
    @PostMapping("/enterStation")
    public ResponseData enterStation(@RequestBody SfcDto sfcDto) throws CommonException {
        if (StringUtils.isNotBlank(sfcDto.getSfc())){//正常进站
//            productionExecuteService.enterStation(sfcDto);
            productionExecuteService.enterStationNew(sfcDto);
        }else {//首工序加工
            productionExecuteService.enterStationByFirstOperation(sfcDto);
        }
        return ResponseData.success();
    }

    @ApiOperation(value = "通过sfc编码获取sfc相关信息（出站、暂停）")
    @PostMapping("/getSfcInfoBySfcOut")
    public ResponseData<SfcVo> getSfcInfoBySfcOut(@RequestBody OutStationDto outStationDto) throws CommonException {
//        productionExecuteService.getSfcInfoBySfcOut(outStationDto);
        return ResponseData.success(productionExecuteService.getSfcInfoBySfcOutNew(outStationDto));
    }

    @ApiOperation(value = "出站")
    @PostMapping("/outStation")
    public ResponseData outStation(@RequestBody OutStationDto outStationDto) throws CommonException {
//        productionExecuteService.outStation(outStationDto);
        int i = productionExecuteService.outStationNew(outStationDto);
        if (i == 1){
            return ResponseData.success("出站成功,自动入库成功！");
        }else if (i == 0){
            return ResponseData.success("出站成功,但自动入库失败,请手动入库！");
        }
        return ResponseData.success("出站成功！");
    }

    @ApiOperation(value = "暂停")
    @PostMapping("/stopOperate")
    public ResponseData stopOperate(@RequestBody OutStationDto outStationDto) throws CommonException {
        sfcService.stopOperate(outStationDto);
        return ResponseData.success();
    }

    @ApiOperation(value = "查询该sfc需要返修的信息")
    @GetMapping("/selectReworkList")
    public ResponseData<List<ReworkVo>> selectReworkList(@RequestParam(value = "sfc") String sfc) throws CommonException {
        return ResponseData.success(sfcService.selectReworkList(sfc));
    }

    @ApiOperation(value = "确认返修")
    @PostMapping("/okRework")
    public ResponseData okRework(@RequestBody List<ReworkDto> reworkDto) throws CommonException {
//        sfcService.okRework(reworkDto);
        sfcService.okReworkNew(reworkDto);
        return ResponseData.success();
    }

    @ApiOperation(value = "查询车间和处理人信息")
    @PostMapping("/queryInfo")
    public ResponseData<WorkShopVo> queryInfo(@RequestBody Map<String,String> params){
        return ResponseData.success(dataServiceImpl.queryInfo(params));
    }

    @ApiOperation(value = "根据sfc条码查询sfc信息")
    @GetMapping("/getSfcInfoBySfc")
    public ResponseData<Sfc> getSfcInfoBySfc(@RequestParam(value = "sfc") String sfc){
        return ResponseData.success(sfcMapper.getSfcInfoBySfc(sfc));
    }

    @ApiOperation(value = "根据工艺路线编码找到下面所有可跳站的工序")
    @GetMapping("/getOperationByRouterBo")
    public ResponseData<List<Operation>> getOperationByRouterBo(@RequestParam(value = "routerBo") String routerBo,
                                                                @RequestParam(value = "sfc") String sfc) throws CommonException {
        return ResponseData.success(sfcService.getOperationByRouterBo(routerBo,sfc));
    }

    @ApiOperation(value = "跳站")
    @PostMapping("/skipStation")
    public ResponseData skipStation(@RequestBody SkipStationDTO skipStationDTO) throws CommonException{
        Sfc sfc = sfcService.getOne(new QueryWrapper<Sfc>().eq("sfc", skipStationDTO.getSfc()));
        String state = sfc.getState();
        String stateBo = shopOrderService.getOne(new QueryWrapper<ShopOrder>().eq("bo", sfc.getShopOrderBo())).getStateBo();
        if ("已完成".equals(state)){
            throw new CommonException("该sfc已完成，不能跳站", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if ("STATE:dongyin,502".equals(stateBo)){
            throw new CommonException("该sfc的工单已关闭，无法跳站", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (StringUtils.isNotBlank(sfc.getParentSfcBo())){
            throw new CommonException("拆分出来的标签不允许跳站，请送到检验直接过站", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        sfcService.skipStation(skipStationDTO);
        return ResponseData.success();
    }

    @ApiOperation(value = "sfc删除")
    @DeleteMapping("/deleteSfc")
    public ResponseData deleteSfc(@RequestBody List<SfcDto> sfcDtos){
        sfcDtos.forEach(
                sfcDto -> {
                    sfcMapper.delete(new QueryWrapper<Sfc>().eq("sfc",sfcDto.getSfc()));
                    Dispatch dispatchObj = dispatchMapper.selectOne(new QueryWrapper<Dispatch>().eq("dispatch_code", sfcDto.getDispatchCode()));
                    dispatchObj.setCanPrintQty(dispatchObj.getCanPrintQty().add(sfcDto.getSfcQty()));
                    dispatchMapper.updateById(dispatchObj);
                }
        );
        return ResponseData.success("删除成功");
    }

    @Autowired
    private ProductionExecuteServiceImpl productionExecuteServiceImpl;
    @GetMapping("/getPreviousOp")
    public ResponseData<String> test(@RequestParam("routerBo") String routerBo,
                                     @RequestParam("operationBo") String operationBo,
                                     @RequestParam("sfc") String sfc) throws CommonException {
        return ResponseData.success(productionExecuteServiceImpl.getPreviousOp(routerBo,operationBo,sfc));
    }

    @GetMapping("/getAutomaticReportWorkInfo")
    public ResponseData<List<AutomaticReportWork>> getAutomaticReportWorkInfo() throws CommonException {
        String productLineBo = stationMapper.getProductLineBoByUser(sfcServiceImpl.getUserInfo().getUserBo());
        if (StringUtils.isBlank(productLineBo)){
            throw new CommonException("该用户产线不存在，请检查",504);
        }
        List<String> stepList = operationMapper.listAllWorkStationByOperationBo(sfcServiceImpl.getUserInfo().getOperationBo());
        List<AutomaticReportWork> automaticReportWorkList = new ArrayList<>();
        for (String step : stepList) {
            Station station = stationMapper.getStationByStepAndProductLineBo(step,productLineBo);
            if (ObjectUtil.isNotEmpty(station)){
                List<String> userBoList = stationMapper.getUserByStationBo(station.getBo());
                for (String userBo : userBoList) {
                    if (!userBo.equals("admin")){
                        AutomaticReportWork automaticReportWork = new AutomaticReportWork();
                        automaticReportWork.setWorkStepBo(step);
                        automaticReportWork.setStationBo(station.getBo());
                        automaticReportWork.setStationName(station.getStationDesc());
                        automaticReportWork.setUserBo(userBo);
                        automaticReportWork.setUserName(stationMapper.getUserNameByUserBo(userBo));
                        automaticReportWorkList.add(automaticReportWork);
                    }
                }
            }
        }
        return ResponseData.success(automaticReportWorkList);
    }
}
