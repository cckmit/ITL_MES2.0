package com.itl.mes.core.provider.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.CollectorsUtil;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.dto.*;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.StemDispatchService;
import com.itl.mes.core.api.vo.StemDispatchListVo;
import com.itl.mes.core.api.vo.StemDispatchVo;
import com.itl.mes.core.api.vo.StepNcRecordVo;
import com.itl.mes.core.api.vo.StepNcVo;
import com.itl.mes.core.provider.mapper.DyYaoXianMapper;
import com.itl.mes.core.provider.mapper.StemDispatchMapper;
import com.itl.mes.core.provider.mapper.StepNcMapper;
import com.itl.mes.core.provider.service.impl.StemDispatchServiceImpl;
import com.itl.mes.core.provider.util.KeyWordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/stemDispatch")
@Api(tags = " 线圈工步派工")
@Slf4j
public class StemDispatchController {
    @Autowired
    private StemDispatchService stemDispatchService;

    @Autowired
    private StemDispatchMapper stemDispatchMapper;

    @Autowired
    private DyYaoXianMapper dyYaoXianMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private StepNcMapper stepNcMapper;

    @PostMapping("/shakeLineList")
    @ApiOperation(value = "摇线查询列表")
    public ResponseData<IPage<DyYaoXian>> shakeLineList(@RequestBody ShakeLineDTO shakeLineDTO){
        QueryWrapper<DyYaoXian> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(shakeLineDTO.getItem())){
            queryWrapper.eq("item",shakeLineDTO.getItem());
        }
        if (StringUtils.isNotBlank(shakeLineDTO.getVersion())){
            queryWrapper.eq("version",shakeLineDTO.getVersion());
        }
        return ResponseData.success(dyYaoXianMapper.selectPage(shakeLineDTO.getPage(),queryWrapper));
    }

    @PostMapping("/insertShakeLine")
    @ApiOperation(value = "新增摇线")
    public ResponseData<DyYaoXian> insertShakeLine(@RequestBody DyYaoXian dyYaoXian){
        dyYaoXian.setId(UUID.uuid32());
        dyYaoXianMapper.insert(dyYaoXian);
        return ResponseData.success(dyYaoXian);
    }
    @PostMapping("/updateShakeLine")
    @ApiOperation(value = "修改摇线")
    public ResponseData<DyYaoXian> updateShakeLine(@RequestBody DyYaoXian dyYaoXian){
        dyYaoXianMapper.updateById(dyYaoXian);
        return ResponseData.success(dyYaoXian);
    }

    @DeleteMapping("/deleteShakeLine")
    @ApiOperation(value = "删除摇线")
    public ResponseData deleteShakeLine(@RequestBody List<String> ids){
        ids.forEach(
                id ->{
                    dyYaoXianMapper.deleteById(id);
                }
        );
        return ResponseData.success();
    }
    
    /**
     * 查询需要派工的工序工单
     * @param stemDispatchDTO
     * @return
     */
    @ApiOperation(value = "查询需要派工的工序工单")
    @PostMapping("/needDispatchOrders")
    public ResponseData<IPage<OperationOrder>> needDispatchOrders(@RequestBody StemDispatchDTO stemDispatchDTO){
        return ResponseData.success(stemDispatchService.queryNeedDispatchOrders(stemDispatchDTO));
    }

    /**
     * 分派
     * @return
     */
    @ApiOperation(value = "分派")
    @PostMapping("/assignment")
    public ResponseData<List<StemDispatchVo>> assignment(@RequestBody StemDispatchDTO stemDispatchDTO) throws CommonException {
        return ResponseData.success(stemDispatchService.assignment(stemDispatchDTO));
    }

    /**
     * 确认分派
     * @return
     */
    @ApiOperation(value = "确认分派")
    @PostMapping("/okAssignment")
    public ResponseData okAssignment(@RequestBody OkStemDispatchDTO okStemDispatchDTO){
        stemDispatchService.okAssignment(okStemDispatchDTO);
        return ResponseData.success();
    }

    /**
     * 线圈派工查询页面
     * @param stemDispatchDTO
     * @return
     */
    @ApiOperation(value = "线圈派工查询页面")
    @PostMapping("/queryStemDispatchList")
    public ResponseData<IPage<StemDispatchListVo>> queryStemDispatchList(@RequestBody StemDispatchDTO stemDispatchDTO){
        return ResponseData.success(stemDispatchMapper.queryStemDispatchList(stemDispatchDTO.getPage(),stemDispatchDTO));
    }

    @ApiOperation(value = "线圈派工导出")
    @PostMapping("/exportStemDispatch")
    public void exportStemDispatch(@RequestBody StemDispatchDTO stemDispatchDTO){
        try {
            stemDispatchService.exportStemDispatch(stemDispatchDTO);
        } catch (CommonException | IOException e) {
            e.printStackTrace();
            log.error("exportStemDispatch -=- {}", ExceptionUtils.getFullStackTrace(e));
        }
    }
//    /**
//     * 改派查询界面
//     */
//    @ApiOperation(value = "改派查询界面")
//    @PostMapping("/editDispatchList")
//    public ResponseData<List<StemDispatch>> editDispatchList(@RequestBody List<OperationOrderAndQty> operationOrderAndQtys,
//                                                             @RequestParam(value = "stepDispatchCode",required = false) String stepDispatchCode){
//        List<String> operationList = new ArrayList<>();
//        operationOrderAndQtys.forEach(
//                operationOrderAndQty -> {
//                    operationList.add(operationOrderAndQty.getOperationOrder());
//                }
//        );
//        QueryWrapper<StemDispatch> queryWrapper = new QueryWrapper();
//        queryWrapper.in("operation_order",operationList);
//        if (StringUtils.isNotBlank(stepDispatchCode)){
//            queryWrapper.eq("step_dispatch_code",stepDispatchCode);
//        }
//        return ResponseData.success(stemDispatchMapper.selectList(queryWrapper));
//    }
//
//    /**
//     * 确认改派
//     */
//    @ApiOperation(value = "确认改派")
//    @PostMapping("/okEditDispatch")
//    public ResponseData okEditDispatch(@RequestBody List<StemDispatchDTO> stemDispatchDTOS){
//        stemDispatchDTOS.forEach(
//                stemDispatchDTO -> {
//                    StemDispatch stemDispatch = new StemDispatch();
//                    stemDispatch.setDispatchQty(stemDispatchDTO.getDispatchQty());
//                    stemDispatchMapper.update(stemDispatch,new QueryWrapper<StemDispatch>().eq("id",stemDispatchDTO.getId()));
//                }
//        );
//        return ResponseData.success();
//    }

    @PostMapping("/editDispatchList")
    @ApiOperation(value = "改派查询")
    public ResponseData<List<StemDispatch>> editDispatchList(@RequestBody StemDispatchDTO stemDispatchDTO){
        QueryWrapper<StemDispatch> queryWrapper = new QueryWrapper<>();
        if (stemDispatchDTO.getStepDispatchCode() !=null && stemDispatchDTO.getStepDispatchCode() !=""){
            queryWrapper.like("step_dispatch_code",stemDispatchDTO.getStepDispatchCode());

        }
        queryWrapper.orderByDesc("CREATE_DATE");
        return ResponseData.success(stemDispatchMapper.selectList(queryWrapper));
    }

    @ApiOperation(value = "确认改派")
    @PostMapping("/okEditDispatch")
    public ResponseData okEditDispatch(@RequestBody List<StemDispatchDTO> stemDispatchDTO){
        stemDispatchDTO.forEach(
                stemDispatchObj -> {
                    StemDispatch stemDispatch = new StemDispatch();
                    stemDispatch.setId(stemDispatchObj.getId());
                    stemDispatch.setDispatchQty(stemDispatchObj.getDispatchQty());
                    stemDispatchMapper.updateById(stemDispatch);
                }
        );
        return ResponseData.success();
    }

    @ApiOperation(value = "线圈报工")
    @PostMapping("/selectStemReportWorkList")
    public ResponseData<List<StemDispatchListVo>> selectStemReportWorkList(@RequestBody List<StemDispatchDTO> stemDispatchDTO){
        return ResponseData.success(stemDispatchService.selectStemReportWorkList(stemDispatchDTO));
    }

    @ApiOperation(value = "确认线圈报工")
    @PostMapping("/okStemReportWork")
    public ResponseData okStemReportWork(@RequestBody List<StemReportWorkDto> stemReportWorkDtoList) throws CommonException{
//        String userName = userUtil.getUser().getUserName();
//        log.info("当前登录人 ===>" + userName);
//        //验证报工人是否是当前登录人
//        for (StemReportWorkDto stemReportWorkDto : stemReportWorkDtoList) {
//            if (!stemReportWorkDto.getUserBo().equals(userName)){
//                throw new CommonException("有不是当前用户的报工记录，请检查！", CommonExceptionDefinition.VERIFY_EXCEPTION);
//            }
//        }
        stemDispatchService.okStemReportWork(stemReportWorkDtoList);
        return ResponseData.success();
    }

    @ApiOperation(value = "根据工单查找相关信息")
    @GetMapping("/getItemInfoByShopOrder")
    public ResponseData<StepNcVo> getAllInfoByShopOrderBo(@RequestParam("shopOrderBo") String shopOrderBo) throws CommonException {
        return ResponseData.success(stemDispatchService.getAllInfoByShopOrderBo(shopOrderBo));
    }

    @ApiOperation(value = "线圈不良or报废登记")
    @PostMapping("/stemNcRecord")
    public ResponseData stemNcRecord(@RequestBody StemNcDto stemNcDto){
        stemNcDto.getStemNcDetailsDtos().forEach(
                stemNcDetailsDto -> {
                    StepNc stepNc = new StepNc();
                    stepNc.setId(UUID.uuid32());
                    stepNc.setShopOrderBo(stemNcDto.getShopOrderBo());
                    stepNc.setItemBo(stemNcDto.getItemBo());
                    stepNc.setNcCodeBo(stemNcDetailsDto.getNcCodeBo());
                    stepNc.setNcQty(stemNcDetailsDto.getNcQty());
                    stepNc.setNcType(stemNcDetailsDto.getNcType());
                    stepNc.setNcWorkStepBo(stemNcDetailsDto.getNcWorkStepBo());
                    stepNc.setPersonLiable(stemNcDetailsDto.getPersonLiable());
                    stepNc.setNcProject(stemNcDetailsDto.getNcProject());
                    stepNc.setCreateDate(new Date());
                    stepNcMapper.insert(stepNc);
                }
        );
        return ResponseData.success();
    }

    @ApiOperation(value = "线圈不良登记记录")
    @PostMapping("/getStepNcRecord")
    public ResponseData<IPage<StepNcRecordVo>> getStepNcRecord(@RequestBody StepNcRecordDto stepNcRecordDto){
        if (ObjectUtil.isEmpty(stepNcRecordDto.getPage())){
            stepNcRecordDto.setPage(new Page(1,10));
        }
        return ResponseData.success(stepNcMapper.getStepNcRecord(stepNcRecordDto.getPage(),stepNcRecordDto));
    }
    @Autowired
    private StemDispatchServiceImpl stemDispatchServiceImpl;
    @GetMapping("/export")
    public void export() throws IOException {
        stemDispatchServiceImpl.exportStemDispatchYx(new StemDispatchDTO());
    }

    @GetMapping("/listAllCanCancelDispatchOrder")
    @ApiOperation(value = "查询所有可撤回的派工单")
    public ResponseData<IPage<StemDispatch>> listAllCanCancelDispatchOrder(@RequestParam("current") Integer current,@RequestParam("size") Integer size,@RequestParam("dispatchOrder") String dispatchOrder,@RequestParam("itemDesc") String itemDesc){
        List<String> itemDescKeyWordList= KeyWordUtil.encapsulationItemDescKeyWord(itemDesc);
        IPage<StemDispatch> page=stemDispatchMapper.listAllCanCancelDispatchOrder(new Page(current, size),dispatchOrder,itemDescKeyWordList);

        if(page !=null && CollectionUtil.isNotEmpty(page.getRecords())){
            //查询每个派工单对应的工序工单，用逗号拼接
            page.getRecords().forEach(stemDispatch ->{
                List<String> operationOrders=stemDispatchMapper.selectOperationOrderByStepDispatchCode(stemDispatch.getStepDispatchCode());
                String operationOrder="";
                if(CollectionUtil.isNotEmpty(operationOrders)){
                    //只有一个，直接赋值
                    if(operationOrders.size()<=1){
                        operationOrder=operationOrders.get(0);
                    }else{
                        //遍历
                        for(int i=0;i<operationOrders.size();i++){
                            if(i == operationOrders.size()-1){
                                //最后一个元素
                                operationOrder+=operationOrders.get(i);
                            }else{
                                operationOrder+=operationOrders.get(i)+"、";
                            }
                        }
                    }
                }
                stemDispatch.setOperationOrder(operationOrder);
            });
        }
        return ResponseData.success(page);
    }

    @PostMapping("/dispatchOrders")
    @ApiOperation(value = "派工单批量撤回")
    public ResponseData cancelDispatchOrder(@RequestBody List<String> dispatchOrders) throws CommonException {
        stemDispatchService.cancelDispatchOrder(dispatchOrders);
        return ResponseData.success();
    }
}
