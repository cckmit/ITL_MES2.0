package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.entity.InspectType;
import com.itl.mes.core.api.service.CustomDataService;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.InspectTypeService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataVo;
import com.itl.mes.core.api.vo.InspectTypeVo;
import io.swagger.annotations.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author lzh
 * @since 2019-08-28
 */
@RestController
@RequestMapping("/monopy/inspectTypes")
@Api(tags = " 检验类型维护")
public class InspectTypeController {
    private final Logger logger = LoggerFactory.getLogger(InspectTypeController.class);

    @Autowired
    private CustomDataService customDataService;
    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public InspectTypeService inspectTypeService;

    /**
     * 查询信息
     *
     * @param params 对象
     * @return 对象
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询检验类型")
    @ApiOperationSupport(params = @DynamicParameters(name = "InspectTypeRequestModel", properties = {
            @DynamicParameter(name = "inspectTypeDesc", value = "检验类型描述，可不传"),
            @DynamicParameter(name = "inspectTypeName", value = "检验名称，可不传"),
            @DynamicParameter(name = "inspectType", value = "检验类型编号，可不传"),
            @DynamicParameter(name = "INSPECT_TYPE", value = "返回字段：检验类型，不需要传入"),
            @DynamicParameter(name = "INSPECT_TYPE_NAME", value = "返回字段：检验类型名称，不需要传入"),
            @DynamicParameter(name = "INSPECT_TYPE_DESC", value = "返回字段：检验类型描述，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入")
    }))
    public ResponseData<List<Map>> selectInspectList(@RequestBody Map<String, Object> params) throws CommonException {
        return ResponseData.success(inspectTypeService.selectInspectTypeList(params));

    }


    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/pageOriginal")
    @ApiOperation(value = "分页查询检验类型")
    @ApiOperationSupport(params = @DynamicParameters(name = "InspectTypeRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不传"),
            @DynamicParameter(name = "inspectTypeDesc", value = "检验类型描述，可不传"),
            @DynamicParameter(name = "inspectTypeName", value = "检验名称，可不传"),
            @DynamicParameter(name = "inspectType", value = "检验类型编号，可不传"),
            @DynamicParameter(name = "INSPECT_TYPE", value = "返回字段：检验类型，不需要传入"),
            @DynamicParameter(name = "INSPECT_TYPE_NAME", value = "返回字段：检验类型名称，不需要传入"),
            @DynamicParameter(name = "INSPECT_TYPE_DESC", value = "返回字段：检验类型描述，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入")
    }))
    public ResponseData<IPage<Map>> selectPageInspectList(@RequestBody Map<String, Object> params) throws CommonException {
        return ResponseData.success(inspectTypeService.selectPageInspectTypeList(new QueryPage<>(params), params));

    }




    /**
     * 分页查询信息ByState
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/pageByState")
    @ApiOperation(value = "分页查询检验类型ByState")
    @ApiOperationSupport(params = @DynamicParameters(name = "InspectTypeRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不传"),
            @DynamicParameter(name = "inspectTypeDesc", value = "检验类型描述，可不传"),
            @DynamicParameter(name = "inspectTypeName", value = "检验名称，可不传"),
            @DynamicParameter(name = "inspectType", value = "检验类型编号，可不传"),
            @DynamicParameter(name = "INSPECT_TYPE", value = "返回字段：检验类型，不需要传入"),
            @DynamicParameter(name = "INSPECT_TYPE_NAME", value = "返回字段：检验类型名称，不需要传入"),
            @DynamicParameter(name = "INSPECT_TYPE_DESC", value = "返回字段：检验类型描述，不需要传入"),
            @DynamicParameter(name = "STATE", value = "返回字段：状态，不需要传入")
    }))
    public ResponseData<IPage<Map>> selectPageInspectListByState(@RequestBody Map<String, Object> params) throws CommonException {
        return ResponseData.success(inspectTypeService.selectPageInspectTypeListByState(new QueryPage<>(params), params));

    }

    /**
     * 精确查询
     *
     * @param inspectType
     * @return
     */
    @GetMapping("/select")
    @ApiOperation(value = "精确查询检验类型数据")
    @ApiImplicitParam(name = "inspectType", value = "检验类型编号", dataType = "string", paramType = "query")
    public ResponseData<InspectTypeVo> select(String inspectType) throws CommonException {
        if (inspectType == null || StrUtil.isBlank(inspectType)) {
            throw new CommonException("检验类型不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        InspectTypeVo inspectTypeVo = inspectTypeService.getInspectTypeVoByInspectType(inspectType);
        return ResponseData.success(inspectTypeVo);
    }

    /**
     * 保存或新增检验类型数据
     *
     * @param inspectTypeVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存或新增检验类型数据")
    public ResponseData<InspectTypeVo> save(@RequestBody InspectTypeVo inspectTypeVo) throws CommonException {
        if (inspectTypeVo == null || StrUtil.isBlank(inspectTypeVo.getInspectType())) {
            throw new CommonException("参数值不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        InspectType inspectType =  inspectTypeService.saveInspectType(inspectTypeVo);
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), inspectType.getBo(), CustomDataTypeEnum.INSPECT_TYPE.getDataType());
        inspectTypeVo.setModifyDate(inspectType.getModifyDate());
        inspectTypeVo.setCustomDataAndValVoList(customDataAndValVos);
        return ResponseData.success(inspectTypeVo);
    }


    /**
     * 删除检验类型数据
     * @param inspectType
     * @param modifyDate
     * @return
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除检验类型数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "inspectType", value = "检验类型编号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "modifyDate", value = "修改日期", dataType = "string", paramType = "query")
    })
    public ResponseData<String> delete(String inspectType, String modifyDate) throws CommonException {
        if (inspectType == null || StrUtil.isBlank(inspectType)) {
            throw new CommonException("检验类型不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (modifyDate == null || StrUtil.isBlank(modifyDate)) {
            throw new CommonException("时间不能为空", CommonExceptionDefinition.TIME_VERIFY_EXCEPTION);
        }

        inspectTypeService.deleteInspectType(inspectType, DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS ));
        return ResponseData.success( "success" );
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportInspectFile(String site, HttpServletResponse response){
        try {
            inspectTypeService.exportInspectFile(site, response);
        } catch (CommonException e) {
            logger.error("exportMouldGroup -=- {}", ExceptionUtils.getFullStackTrace(e));
        }
    }

    @RequestMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
//        Map<String, String> resultMap = new HashMap<>();
//        resultMap.put("FileName", file.getOriginalFilename());
//        try {
//            List<InspectTypeVo> inspectTypeVoList = ExcelUtils.importExcel(file, 1, 1, InspectTypeVo.class);
//            for (InspectTypeVo inspectTypeVo : inspectTypeVoList) {
//                inspectTypeService.importExcel(inspectTypeVo);
//            }
//        } catch (CommonException e) {
//            logger.error("saveInspectFile -=- {}", ExceptionUtils.getFullStackTrace(e));
//            return new RestResult<String>(false, e.getCode(), e.getMessage());
//        }
//        return new RestResult<String>("success");

        inspectTypeService.importExcel(file);
        return ResponseData.success("success");

    }

    /**
     * 获取自定义数据
     * @return
     */
    @GetMapping("/getInspectTypeCustomData")
    @ApiOperation(value = "获取检验类型维护自定义数据")
    public ResponseData<List<CustomDataVo>> getInspectTypeCustomData() {
        return ResponseData.success(customDataService.selectCustomDataVoListByDataType(CustomDataTypeEnum.INSPECT_TYPE.getDataType()));
    }
}
