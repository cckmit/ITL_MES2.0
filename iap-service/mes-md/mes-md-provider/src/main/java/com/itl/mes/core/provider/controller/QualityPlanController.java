package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.QualityPlanDTO;
import com.itl.mes.core.api.dto.QualityPlanParameterDTO;
import com.itl.mes.core.api.entity.QualityPlan;
import com.itl.mes.core.api.entity.QualityPlanParameter;
import com.itl.mes.core.api.service.CustomDataService;
import com.itl.mes.core.api.service.QualityCheckParameterService;
import com.itl.mes.core.api.service.QualityPlanParameterService;
import com.itl.mes.core.api.service.QualityPlanService;
import com.itl.mes.core.api.vo.CustomDataVo;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.api.vo.QualityPlanExcelInfoVo;
import com.itl.mes.core.api.vo.QualityPlanVO;
import com.itl.mes.core.provider.mapper.QualityPlanMapper;
import io.swagger.annotations.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author lzh
 * @since 2019-08-29
 */
@RestController
@RequestMapping("/monopy/qualityPlans")
@Api(tags = "质量控制计划维护" )
public class QualityPlanController {
    private final Logger logger = LoggerFactory.getLogger(QualityPlanController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public QualityPlanService qualityPlanService;

    @Autowired
    private QualityPlanMapper qualityPlanMapper;

    @Autowired
    private CustomDataService customDataService;

    @Autowired
    private QualityPlanParameterService qualityPlanParameterService;

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<QualityPlan> getQualityPlanById(@PathVariable String id) {
        return ResponseData.success(qualityPlanService.getById(id));
    }

    /**
     * 分页查询
     * @param qualityPlanDTO
     * @return
     */
    @PostMapping("/selectQualityPlanPage")
    @ApiOperation(value="分页查询质量控制计划")
    public ResponseData<IPage<Map>> selectQualityPlanPage(@RequestBody QualityPlanDTO qualityPlanDTO){
        QueryWrapper<QualityPlan> queryWrapper = new QueryWrapper<>();
        /*queryWrapper.eq("IS_CURRENT_VERSION","Y");*/
        if (StrUtil.isNotEmpty(qualityPlanDTO.getQualityPlan())){
            queryWrapper.like("QUALITY_PLAN",qualityPlanDTO.getQualityPlan());
        }
        if (StrUtil.isNotEmpty(qualityPlanDTO.getQualityPlanDesc())){
            queryWrapper.like("QUALITY_PLAN_DESC",qualityPlanDTO.getQualityPlanDesc());
        }
        return ResponseData.success(qualityPlanMapper.selectPage(qualityPlanDTO.getPage(),queryWrapper));
    }

    /**
     * 获取自定义数据
     * @return
     */
    @GetMapping("/getQualityPlanCustomData")
    @ApiOperation(value = "获取质量控制计划维护自定义数据")
    public ResponseData<List<CustomDataVo>> getQualityPlanCustomData() {
        return ResponseData.success(customDataService.selectCustomDataVoListByDataType(CustomDataTypeEnum.QUALITY_PLAN.getDataType()));
    }

    /**
     * 获取质量控制编号
     * @return
     */
    @GetMapping("/getNewsNumber")
    @ApiOperation(value = "创建质量控制计划编号")
    public ResponseData<String> getNewsNumber() throws CommonException {
        return ResponseData.success(qualityPlanService.getQPnumber());
    }

    /**
     * 保存或新增控制质量数据
     * @param qualityPlanVO
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存或新增控制质量数据")
    public ResponseData saveInUpdate(@RequestBody QualityPlanVO qualityPlanVO) throws CommonException {
        if (qualityPlanVO == null) {
            throw new CommonException("参数值不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (StrUtil.isBlank(qualityPlanVO.getQualityPlan())) {
            throw new CommonException("控制质量编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        QualityPlanParameter parameter= qualityPlanService.saveInUpdate(qualityPlanVO);
        if (parameter==null){
            return ResponseData.error("500","该控制计划工序已存在该检验项");
        }
        return ResponseData.success(parameter);
    }

    /**
     * 保存或新增控制质量数据
     * @param qualityPlan
     * @return
     */
    @PostMapping("/saveQualityPlan")
    @ApiOperation(value = "新增控制计划")
    public ResponseData<QualityPlan> saveQualityPlan(@RequestBody QualityPlan qualityPlan) throws CommonException {
        if (qualityPlan == null) {
            throw new CommonException("参数值不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (StrUtil.isBlank(qualityPlan.getQualityPlan())) {
            throw new CommonException("控制质量编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return ResponseData.success(qualityPlanService.saveQualityPlan(qualityPlan));
    }

    /**
     * 精确查询
     * @param qualityPlanParameterDTO
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "分页查询控制质量数据")
    public ResponseData<IPage<QualityPlanParameter>> getQpapVoByQualityPlan(@RequestBody QualityPlanParameterDTO qualityPlanParameterDTO) throws CommonException {
        return ResponseData.success(qualityPlanService.getQpapVoByQualityPlan(qualityPlanParameterDTO));
    }

    /**
     * 删除控制质量数据
     * @param qualityPlan
     * @return
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除控制质量数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "qualityPlan", value = "检验类型编号", dataType = "string", paramType = "query")
    })
    public ResponseData deleteQuality(String qualityPlan) throws CommonException {
        if (qualityPlan == null || StrUtil.isBlank(qualityPlan)) {
            throw new CommonException("检验类型不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        qualityPlanService.deleteQuality(qualityPlan);
        return ResponseData.success("success");
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportQplan(String site, HttpServletResponse response) {

        try {
            qualityPlanService.exportQplan(site, response);
        } catch (CommonException e) {
            logger.error("exportQplan -=- {}", ExceptionUtils.getFullStackTrace(e));
        }
    }

 /*   @RequestMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
        qualityPlanService.importExcel(file);
        return ResponseData.success("success");
    }*/

    @DeleteMapping("/deleteList")
    @ApiOperation(value = "根据BO批量删除明细", notes = "根据BO批量删除明细")
    public ResponseData deleteList(@RequestBody List<String> BOList) {
        return ResponseData.success(qualityPlanParameterService.removeByIds(BOList));
    }

    /**
     * 精确查询
     * @param qualityPlan
     * @return
     */
    @PostMapping("/setDefaultPlan")
    @ApiOperation(value = "设置默认计划")
    public ResponseData<Boolean> setDefaultPlan(String qualityPlan) throws CommonException {
        return ResponseData.success(qualityPlanService.setDefaultPlan(qualityPlan));
    }

    /**
     * 获取当前默认计划
     * @return
     */
    @GetMapping("/getDefaultPlan")
    @ApiOperation(value = "获取默认计划")
    public ResponseData<QualityPlan> getDefaultPlan() throws CommonException {
        return ResponseData.success(qualityPlanService.getDefaultPlan());
    }

    @GetMapping("/exportByItem")
    @ApiOperation(value = "根据物料导出文件")
    public ResponseData<String> exportByItem(String item, HttpServletResponse response) throws CommonException {
        qualityPlanService.exportByItem(item,response);
        return ResponseData.success();
    }

    @RequestMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
        List<QualityPlanExcelInfoVo> qualityPlanExcelInfoVos = ExcelUtils.importExcel(file, 1, 1, QualityPlanExcelInfoVo.class);
        int row=3;
        for (QualityPlanExcelInfoVo qualityPlanExcelInfoVo : qualityPlanExcelInfoVos) {
            qualityPlanService.saveQualityPlanParameter(qualityPlanExcelInfoVo,row);
            row++;
        }
        return ResponseData.success();
    }
}