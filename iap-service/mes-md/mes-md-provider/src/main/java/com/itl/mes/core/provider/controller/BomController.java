package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.BomHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.BomDto;
import com.itl.mes.core.api.entity.Bom;
import com.itl.mes.core.api.service.BomService;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.vo.BomVo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author space
 * @since 2019-06-05
 */
@RestController
@RequestMapping("/bom")
@Api(tags = " 物料清单表")
public class BomController {
    private final Logger logger = LoggerFactory.getLogger(BomController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BomService bomService;

    @Autowired
    private OperationService operationService;


    @ApiOperation(value = "查询螺杆组合")
    @PostMapping("/getScrewAssembly")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "PushRequestModel",
                    properties = {
                            @DynamicParameter(name = "page", value = "页面，默认为1"),
                            @DynamicParameter(name = "limit", value = "分页大小，默20，可不填"),
                            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
                            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
                            @DynamicParameter(name = "item", value = "查询字段")
                    }))
    public ResponseData<IPage<Map<String, Object>>> getScrewAssembly(@RequestBody Map<String, Object> params) {
        IPage<Map<String, Object>> ret;
        if (StringUtils.isNotEmpty(params.getOrDefault("item", "").toString())) {
            ret = bomService.pageMaps(new QueryPage<>(params)
                    , new QueryWrapper<Bom>().select("SCREW_ASSEMBLY")
                            .isNotNull("SCREW_ASSEMBLY")
                            .like("SCREW_ASSEMBLY", params.get("item").toString())
                            .groupBy("SCREW_ASSEMBLY"));
        }else {
            ret = bomService.pageMaps(new QueryPage<>(params)
                    , new QueryWrapper<Bom>().select("SCREW_ASSEMBLY")
                            .isNotNull("SCREW_ASSEMBLY")
                            .groupBy("SCREW_ASSEMBLY"));
        }
        return ResponseData.success(ret);
    }

    @ApiOperation(value = "查询工艺编号")
    @PostMapping("/getProcessNumber")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "PushRequestModel",
                    properties = {
                            @DynamicParameter(name = "page", value = "页面，默认为1"),
                            @DynamicParameter(name = "limit", value = "分页大小，默20，可不填"),
                            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
                            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
                            @DynamicParameter(name = "item", value = "查询字段")
                    }))
    public ResponseData<IPage<Map<String, Object>>> getProcessNumber(@RequestBody Map<String, Object> params) {
        IPage<Map<String, Object>> ret;
        if (StringUtils.isNotEmpty(params.getOrDefault("item", "").toString())) {
            ret = bomService.pageMaps(new QueryPage<>(params)
                    , new QueryWrapper<Bom>().select("PROCESS_NUMBER")
                            .isNotNull("PROCESS_NUMBER")
                            .like("PROCESS_NUMBER", params.get("item").toString())
                            .groupBy("PROCESS_NUMBER"));
        }else {
            ret = bomService.pageMaps(new QueryPage<>(params)
                    , new QueryWrapper<Bom>().select("PROCESS_NUMBER")
                            .isNotNull("PROCESS_NUMBER")
                            .groupBy("PROCESS_NUMBER"));
        }
        return ResponseData.success(ret);
    }

    @ApiOperation(value = "查询工艺特性")
    @PostMapping("/getProcessCharacteristics")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "PushRequestModel",
                    properties = {
                            @DynamicParameter(name = "page", value = "页面，默认为1"),
                            @DynamicParameter(name = "limit", value = "分页大小，默20，可不填"),
                            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
                            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
                            @DynamicParameter(name = "item", value = "查询字段")
                    }))
    public ResponseData<IPage<Map<String, Object>>> getProcessCharacteristics(@RequestBody Map<String, Object> params) {
        IPage<Map<String, Object>> ret;
        QueryWrapper<Bom> query = new QueryWrapper<Bom>()
                .select("PROCESS_CHARACTERISTICS")
                .isNotNull("PROCESS_CHARACTERISTICS")
                .eq("SITE", UserUtils.getSite())
                .groupBy("PROCESS_CHARACTERISTICS");
        if (params.get("item") != null) {
            query.ne("PROCESS_CHARACTERISTICS", params.get("item").toString());
        }
        ret = bomService.pageMaps(new QueryPage<>(params), query);
        return ResponseData.success(ret);
    }

    @ApiOperation(value = "查询配方组")
    @PostMapping("/getFormulaGroup")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "PushRequestModel",
                    properties = {
                            @DynamicParameter(name = "page", value = "页面，默认为1"),
                            @DynamicParameter(name = "limit", value = "分页大小，默20，可不填"),
                            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
                            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
                            @DynamicParameter(name = "item", value = "查询字段")
                    }))
    public ResponseData<IPage<Map<String, Object>>> getFormulaGroup(@RequestBody Map<String, Object> params) {
        IPage<Map<String, Object>> ret;
        if (StringUtils.isNotEmpty(params.getOrDefault("item", "").toString())) {
            ret = bomService.pageMaps(new QueryPage<>(params)
                    , new QueryWrapper<Bom>().select("FORMULA_GROUP")
                            .isNotNull("FORMULA_GROUP")
                            .like("FORMULA_GROUP", params.get("item").toString())
                            .groupBy("FORMULA_GROUP"));
        }else {
            ret = bomService.pageMaps(new QueryPage<>(params)
                    , new QueryWrapper<Bom>().select("FORMULA_GROUP")

                            .isNotNull("FORMULA_GROUP")
                            .groupBy("FORMULA_GROUP"));
        }
        return ResponseData.success(ret);
    }


    @ApiOperation(value = "查询bom")
    @PostMapping("/getBom")
    public ResponseData<IPage<Bom>> getBom(@RequestBody BomDto bomDto){
        return ResponseData.success(bomService.getBom(bomDto));
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询物料清单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bom", value = "物料", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本", dataType = "string", paramType = "query")
    })
    public ResponseData<BomVo> selectByBom(String bom, String version) throws CommonException {
        BomVo bomVoByBomAndVersion = bomService.getBomVoByBomAndVersion(bom, version);
        return ResponseData.success(bomVoByBomAndVersion);
    }

    @GetMapping("/queryByBo")
    @ApiOperation(value = "查询物料清单ByBo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bo", value = "bo", dataType = "string", paramType = "query")
    })
    public BomVo selectByBo(@RequestParam("bo") String bo) throws CommonException {
        return bomService.getBomVoByBo(bo);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存物料清单")
    public ResponseData<BomVo> save(@RequestBody BomVo bomVo) throws CommonException {
        bomVo.setModifyDate(new Date());
        bomService.save(bomVo);
        return ResponseData.success(bomVo);
    }


    @GetMapping("/delete")
    @ApiOperation(value = "删除物料清单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bom", value = "物料", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "modifyDate", value = "修改时间", dataType = "string", paramType = "query")
    })
    public ResponseData<String> delete(String bom, String version, String modifyDate) throws CommonException {
        bomService.delete(bom, version, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }

    /**
     @GetMapping("/fuzzyQueryItem")
     @ApiOperation(value="查询物料信息") public RestResult<List<Item>> selectItem(String item,String itemName,String itemDesc,String version){
     try {
     List<Item> items = itemService.selectItem(item, itemName, itemDesc, version);
     return  new RestResult<List<Item>> (items);
     } catch (BusinessException e) {
     logger.error("selectItem -=- {}", ExceptionUtils.getFullStackTrace( e ) );
     return  new RestResult<List<Item>> (false, 10000, e.getMessage() );
     }

     }
     @GetMapping("/fuzzyQueryOperation")
     @ApiOperation(value="查询工序信息") public RestResult<List<Operation>> selectOperation(String operation, String operationName, String operationDesc,String operationType,String productionLine,String version){
     try {
     List<Operation> operations = operationService.selectOperation(operation, operationName, operationDesc, operationType, productionLine, version);
     return  new RestResult<List<Operation>>(operations);
     } catch (BusinessException e) {
     logger.error("selectOperation -=- {}", ExceptionUtils.getFullStackTrace( e ) );
     return  new RestResult<List<Operation>>(false, 10000, e.getMessage() );
     }
     }

     @GetMapping("/fuzzyQuery")
     @ApiOperation(value="查询物料清单") public RestResult<List<Bom>> select(String bom,String bomDesc,String zsType,String state,String version){
     try {
     List<Bom> boms = bomService.select(bom, bomDesc, zsType, state, version);
     return new  RestResult<List<Bom>> (boms);
     } catch (BusinessException e) {
     logger.error("select -=- {}", ExceptionUtils.getFullStackTrace( e ) );
     return new RestResult<List<Bom>> ( false, 10000, e.getMessage() );
     }

     }



     @GetMapping("/{id}")
     @ApiOperation(value="通过主键查询数据") public RestResult<Bom> getBomById(@PathVariable String id) {
     Bom result = null;
     try {
     result = bomService.selectById(id);
     } catch (BusinessException e) {
     logger.error("getBomById -=- {}", ExceptionUtils.getFullStackTrace( e ) );
     return new RestResult<Bom>( false, 10000, e.getMessage() );
     }
     return new RestResult<Bom>(result);
     }
     */

}
