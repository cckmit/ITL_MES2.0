package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.vo.OperationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * @author space
 * @since 2019-05-28
 */
@RestController
@RequestMapping("/operations")
@Api(tags = " 工序表" )
public class OperationController {
    private final Logger logger = LoggerFactory.getLogger(OperationController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public OperationService operationService;

    /**
    * 根据id查询
    *
    * @param id 主键
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<Operation> getOperationById(@PathVariable String id) {
        return ResponseData.success(operationService.getById(id));
    }



    @PostMapping("/save")
    @ApiOperation(value = "保存工序数据")
    public ResponseData<OperationVo> saveByOperationVo(@RequestBody OperationVo operationVo) throws CommonException {
        if(operationVo==null) throw  new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        operationService.saveByOperationVo(operationVo);
        operationVo = operationService.getOperationVoByOperationAndVersion(operationVo.getOperation(), operationVo.getVersion());
        return ResponseData.success(operationVo);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询工序信息")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "operation", value = "工序", dataType = "string", paramType = "query" ),
            @ApiImplicitParam( name = "version", value = "版本", dataType = "string", paramType = "query" )
    })
    public ResponseData<OperationVo> selectByOperation(String operation,String version) throws CommonException {
        if(StrUtil.isBlank(operation)) throw new CommonException("工序编号参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        if(StrUtil.isBlank(version)) throw new CommonException("工序编号参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        OperationVo operationVoByOperationAndVersion = operationService.getOperationVoByOperationAndVersion(operation, version);
        return ResponseData.success(operationVoByOperationAndVersion);
    }





   @GetMapping("/delete")
   @ApiOperation(value = "删除工序信息")
    public ResponseData<String> delete(String operation,String version,String modifyDate) throws CommonException {
       if(StrUtil.isBlank(operation)) throw new CommonException("工序编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
       if(StrUtil.isBlank(modifyDate)) throw new CommonException("时间不能为空", CommonExceptionDefinition.TIME_VERIFY_EXCEPTION);
       int delete = operationService.delete(operation,version,modifyDate);
       if(delete==0)throw new CommonException("工序编号不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
       return ResponseData.success("success");
    }



    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportItem(String site, HttpServletResponse response) {
        operationService.exportOperation(site, response);
    }


    @RequestMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("FileName", file.getOriginalFilename());
        //POIUtils.checkFile(file);
        List<OperationVo> operationVos = ExcelUtils.importExcel(file, 1, 1, OperationVo.class);
        for (OperationVo operationVo : operationVos) {
            operationService.saveByOperationVo(operationVo);
        }
        return ResponseData.success("success");
    }





    /**


     @PostMapping("/page")
     @ApiOperation(value = "分页查询工序数据")
     @ApiOperationSupport(params = @DynamicParameters(name = "OperationRequestModel",properties={
     @DynamicParameter( name="page", value = "页面，默认为1" ),
     @DynamicParameter( name="limit", value = "分页大小，默认100，可不填" ),
     @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
     @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
     @DynamicParameter( name="operation", value = "工序" ),
     @DynamicParameter( name="operationDesc", value = "工序描述" ),
     @DynamicParameter( name="productLine", value = "产线" ),
     @DynamicParameter( name="operationType", value = "工序类型" )
     }))
     public RestResult<Page<Operation>> page(@RequestBody Map<String,Object> params ){
     EntityWrapper<Operation> entityWrapper = new EntityWrapper<>();
     entityWrapper.eq(Operation.SITE, UserUtils.getSite());
     if(StrUtil.isBlank(params.getOrDefault("operation","").toString())){
     entityWrapper.andNew().like(Operation.OPERATION,params.get("operation").toString());
     }
     if(StrUtil.isBlank(params.getOrDefault("operationDesc","").toString())){
     entityWrapper.andNew().like(Operation.OPERATION_DESC,params.get("operationDesc").toString());
     }
     if(StrUtil.isBlank(params.getOrDefault("productLine","").toString())){
     entityWrapper.andNew().like(Operation.PRODUCTION_LINE_BO,params.get("productLine").toString());
     }
     if(StrUtil.isBlank(params.getOrDefault("operationType","").toString())){
     entityWrapper.andNew().like(Operation.OPERATION_TYPE,params.get("operationType").toString());
     }
     try {
     Page<Operation> operationPage = operationService.selectPage(new QueryPage<>(params), entityWrapper);
     return new RestResult<Page<Operation>>(operationPage);
     }catch (BusinessException e){
     logger.error("saveByOperationVo -=- {}", ExceptionUtils.getFullStackTrace( e ) );
     return new RestResult<Page<Operation>>( false, 10000, e.getMessage() );
     }
     }



     @GetMapping("/mFuzzyQuery")
     @ApiOperation(value ="多条件模糊查询" )
     public RestResult<List<Operation>> selectOperation(String operation, String operationName, String operationDesc,String operationType,String productionLine,String version){
     try {
     List<Operation> operations = operationService.selectOperation(operation, operationName, operationDesc, operationType, productionLine, version);
     return new RestResult<List<Operation>>(operations);
     } catch (BusinessException e) {
     logger.error("selectOperation -=- {}", ExceptionUtils.getFullStackTrace( e ) );
     return new RestResult<List<Operation>>( false, 10000, e.getMessage() );
     }

     }



     @GetMapping("/fuzzyQuery")
     @ApiOperation(value = "模糊查询工序信息")
     public RestResult<List<Operation>> selectByOperationVo(@RequestBody OperationVo operationVo){
     try {
     if(operationVo==null) throw  new BusinessException("参数不能为空");
     List<Operation> operations = operationService.selectByOperationVo(operationVo);
     return new RestResult<List<Operation>>(operations);
     } catch (BusinessException e) {
     logger.error("selectByOperationVo -=- {}", ExceptionUtils.getFullStackTrace( e ) );
     return new RestResult<List<Operation>>( false, 10000, e.getMessage() );
     }
     }


    @GetMapping("/queryNcGroup")
    @ApiOperation(value = "查询不良代码组信息")
    public RestResult<List<NcGroup>> selectNgGroup( String ncGroup,  String ncGroupName , String ncGroupDesc){
        try {
            List<NcGroup> ncGroups = operationService.selectByNcGroup(ncGroup, ncGroupName, ncGroupDesc);
            return new RestResult<List<NcGroup>>(ncGroups);
        } catch (BusinessException e) {
            logger.error("selectNgGroup -=- {}", ExceptionUtils.getFullStackTrace( e ) );
            return new RestResult<List<NcGroup>>(false, 10000, e.getMessage());
        }
    }
    @GetMapping("/queryNcCode")
    @ApiOperation(value = "查询不良代码信息")
    public RestResult<List<NcCode>> selectNgCode(String ncCode,  String ncName, String ncDesc){
        try {
            List<NcCode> ncCodes = operationService.selectByNcCode(ncCode, ncName, ncDesc);
            return new RestResult<List<NcCode>>(ncCodes);
        } catch (BusinessException e) {
            logger.error("selectNgCode -=- {}", ExceptionUtils.getFullStackTrace( e ) );
            return new RestResult<List<NcCode>>(false, 10000, e.getMessage());
        }
    }

    @GetMapping("/queryStation")
    @ApiOperation(value = "查询工位信息")
    public RestResult<List<Station>> selectStation( String station, String stationName,String stationDesc){
    try {
        List<Station> stations = operationService.selectStation(station, stationName, stationDesc);
        return new RestResult<List<Station>>(stations);
    } catch (BusinessException e) {
        logger.error("selectStation -=- {}", ExceptionUtils.getFullStackTrace( e ) );
        return new RestResult<List<Station>>(false, 10000, e.getMessage());
    }

}
    @GetMapping("/queryStationType")
    @ApiOperation(value = "查询工位类型信息")
    public RestResult<List<StationType>> selectStationType( String stationType, String stationTypeName, String stationTypeDesc){
    try {
        List<StationType> stationTypes = operationService.selectStationType(stationType, stationTypeName, stationTypeDesc);
        return  new RestResult<List<StationType>>(stationTypes);
    } catch (BusinessException e) {
        logger.error("selectStationType -=- {}", ExceptionUtils.getFullStackTrace( e ) );
        return  new RestResult<List<StationType>>(false, 10000, e.getMessage());
    }
}
     */
}