package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.OperationDTO;
import com.itl.mes.core.api.dto.QualityCheckListDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.QualityCheckListService;
import com.itl.mes.core.api.vo.QualityCheckAtParameterVO;
import com.itl.mes.core.api.vo.QualityCheckListVO;
import com.itl.mes.core.provider.mapper.*;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/monopy/qualityCheckList")
@Api(tags = "质量检验单基础维护" )
public class QualityCheckListController {
    private final Logger logger = LoggerFactory.getLogger(QualityCheckListController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public QualityCheckListService qualityCheckListService;

    @Autowired
    private QualityCheckListMapper qualityCheckListMapper;

    @Autowired
    private SfcMapper sfcMapper;

    @Autowired
    private WorkShopMapper workShopMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private QualityCheckResultMapper qualityCheckResultMapper;

    @Autowired
    private MyDeviceMapper myDeviceMapper;

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return ResponseData
     */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<QualityCheckList> getQualityCheckListById(@PathVariable String id) {
        return ResponseData.success(qualityCheckListMapper.selectById(id));
    }

    /**
     * 根据id查询
     *
     * @param dto 主键
     * @return ResponseData
     */
    @PostMapping("/getBySfc")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<QualityCheckAtParameterVO> getQualityCheckListBySfc(@RequestBody QualityCheckListDTO dto) {
        QualityCheckAtParameterVO qualityCheckAtParameterVo = qualityCheckListService.getQualityCheckAtParameterVo(dto);
        if (qualityCheckAtParameterVo==null){
            return ResponseData.error("500","该批次下没有对应的检验项");
        }
        return ResponseData.success(qualityCheckAtParameterVo);
    }

    /**
     * 获取检验单编号
     *
     * @return ResponseData
     */
    @GetMapping("/getCheckCode")
    @ApiOperation(value="获取检验单编号")
    public ResponseData<String> getCheckCode() throws CommonException {
        String str = qualityCheckListMapper.selectMaxCode();
        int qnum = 0;
        if (StrUtil.isNotBlank(str)){
            qnum = Integer.parseInt(str.substring(str.length()- 5,str.length()));
        }
        //String site = UserUtils.getSite();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String format = String.format("%05d", qnum+1);
        String checkCode = "DYQC"+sdf.format(new Date())+format;
        return ResponseData.success(checkCode);
    }

    /**
     * 保存或新增控制质量数据
     * @param qclVO
     * @return ResponseData
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存或新增控制质量数据")
    public ResponseData<QualityCheckList> saveInUpdate(@RequestBody QualityCheckListVO qclVO) throws CommonException {
        if (qclVO == null) {
            throw new CommonException("参数值不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        QualityCheckList qualityCheckList = qualityCheckListService.saveInUpdate(qclVO);
        if (qualityCheckList == null){
            return ResponseData.error("500","该批次下没有该工序");
        }
        return ResponseData.success(qualityCheckList);
    }

    /**
     * 精确查询
     * @param qualityCheckListDTO
     * @return ResponseData
     */
    @PostMapping("/query")
    @ApiOperation(value = "分页查询检验单数据")
    public ResponseData<IPage<QualityCheckList>> getQualityCheckList(@RequestBody QualityCheckListDTO qualityCheckListDTO) throws CommonException {
        //QualityPlanAtParameterVo qualityPlanAtParameterVo =  qualityPlanService.getQpapVoByQualityPlan(qualityPlan,version);
        IPage<QualityCheckList> list = qualityCheckListService.getQualityCheckList(qualityCheckListDTO);
        return ResponseData.success(list);
    }

    /**
     * 删除控制质量数据
     * @param id
     * @return ResponseData
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除控制质量数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ID", dataType = "string", paramType = "query")
    })
    public ResponseData deleteQuality(String id) throws CommonException {
        qualityCheckListService.deleteQualityCheckList(id);
        return ResponseData.success("success");
    }

    @DeleteMapping("/deleteList")
    @ApiOperation(value = "根据ID批量删除", notes = "根据ID批量删除")
    public ResponseData deleteList(@RequestBody List<String> Ids) {
        for (String id : Ids) {
            QualityCheckList qualityCheckList = qualityCheckListMapper.selectById(id);
            qualityCheckResultMapper.delete(new QueryWrapper<QualityCheckParameter>().eq("CHECK_CODE", qualityCheckList.getCheckCode()));
        }
        return ResponseData.success(qualityCheckListService.removeByIds(Ids));
    }

    /**
     * 根据批次ID查询工序
     * @param operationDTO
     * @return ResponseData
     */
    @PostMapping("/getOperationBySfc")
    @ApiOperation(value = "根据批次ID查询工序")
    public ResponseData<IPage<List<Operation>>> getOperationBySfc(@RequestBody OperationDTO operationDTO) throws CommonException {
        String sfc = operationDTO.getSfc();
        String operations = operationDTO.getOperation();
        String operationName = operationDTO.getOperationName();
        String processInfo = sfcMapper.checkOperation(sfc);
        Sfc sfcEntity = sfcMapper.selectBySfc(sfc);
        List<String> operationBos = new ArrayList<>();
        if (StrUtil.isNotEmpty(processInfo)){
            JSONObject jsonObj = JSON.parseObject(processInfo);
            JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
            if (nodeList.size() > 0){
                for (Object o : nodeList) {
                    JSONObject operationObj = JSON.parseObject(o.toString());
                    //获取工序
                    String operation = operationObj.getString("operation");
                    operationBos.add(operation);
                }
            }
        }
        QueryWrapper<Operation> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("BO",operationBos);
        if (StrUtil.isNotEmpty(operations)){
            queryWrapper.eq("OPERATION",operations);
        }
        if (StrUtil.isNotEmpty(operationName)){
            queryWrapper.eq("OPERATION_NAME",operationName);
        }
        IPage<List<Operation>> list = operationMapper.selectPage(operationDTO.getPage(),queryWrapper);
        return ResponseData.success(list);
    }

    /**
     * 根据批次ID查询工序
     * @param operationDTO
     * @return ResponseData
     */
    @PostMapping("/getQcoVOBySfcOperation")
    @ApiOperation(value = "根据批次ID和工序查询点检项")
    public ResponseData<QualityCheckAtParameterVO> getQcoVOBySfcOperation(@RequestBody OperationDTO operationDTO) throws CommonException {
       if(operationDTO.getSfc() !=null && operationDTO.getSfc() !=""){
           if(!(operationDTO.getSfc().contains("D-") && operationDTO.getSfc().contains("JC") && operationDTO.getSfc().length()==15)){
               throw new CommonException("请输入正确的批次条码",30002);
           }
       }
        QualityCheckAtParameterVO qcpVO = new QualityCheckAtParameterVO();
        if (StrUtil.isEmpty(operationDTO.getCheckCode())){      //检验单编号不存在时为生产执行首检获取检验项
            if (operationDTO.getCheckType().equals("2")){       //检验类型为2是为终检
                qcpVO = qualityCheckListService.getQcoVOBySfcOperation(operationDTO);
                qcpVO.setCheckType("2");
            }
            else {
                qcpVO = qualityCheckListService.getQcoVOByOperation(operationDTO);
                qcpVO.setCheckType(operationDTO.getCheckType());
            }
        }
        else {//检验单编号存在时为检验单列表品检
            qcpVO = qualityCheckListService.getQcoVOBySfcOperation(operationDTO);
        }
        if (qcpVO==null){
            return ResponseData.error("500","该批次下没有对应的检验项");
        }
        return ResponseData.success(qcpVO);
    }

    /**
     * 用户验证
     * @param params
     * @return ResponseData
     */
    @PostMapping("/userVerify")
    @ApiOperation(value = "用户验证并进行签到或解除")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "userVerify",
                    properties = {
                            @DynamicParameter(name = "userName", value = ""),
                            @DynamicParameter(name = "pwd", value = "")
                    }))
    public ResponseData userVerify(@RequestBody Map<String, Object> params) throws CommonException {
        String uname = qualityCheckListService.userVerify(params);
        if (StrUtil.isEmpty(uname)){
            return ResponseData.error("500","用户名或密码错误！");
        }
        return ResponseData.success(uname);
    }
}
