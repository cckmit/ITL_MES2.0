package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.service.StationTypeService;
import com.itl.mes.core.api.vo.StationTypeItemVo;
import com.itl.mes.core.api.vo.StationTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author space
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/stationTypes")
@Api(tags = " 工位类型表")
public class StationTypeController {
    private final Logger logger = LoggerFactory.getLogger(StationTypeController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public StationTypeService stationTypeService;


    @PostMapping("/save")
    @ApiOperation(value = "保存工位类型信息")
    public ResponseData<StationTypeVo> save(@RequestBody StationTypeVo stationTypeVo) throws CommonException {
        if (stationTypeVo == null) throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        stationTypeService.save(stationTypeVo);
        stationTypeVo = stationTypeService.selectStationTypeVo(stationTypeVo.getStationType());
        return ResponseData.success(stationTypeVo);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询工位类型信息")
    public ResponseData<StationTypeVo> getStationTypeVo(@RequestParam String stationType) throws CommonException {
        if (stationType == null) throw new CommonException("编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        StationTypeVo stationTypeVo = stationTypeService.selectStationTypeVo(stationType);
        return ResponseData.success(stationTypeVo);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除工位类型信息")
    public ResponseData<String> delete(String stationType, String modifyDate) throws CommonException {
        if (stationType == null) throw new CommonException("编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        int delete = stationTypeService.delete(stationType, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        if (delete == 0) throw new CommonException("此工位类型不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        return ResponseData.success("success");
    }

    @GetMapping("/getStationTypeItemVos")
    @ApiOperation(value = "获取工位信息")
    public ResponseData<List<StationTypeItemVo>> getStationTypeItemVos(String station) throws CommonException {
        List<StationTypeItemVo> stationTypeItemVo = stationTypeService.getStationTypeItemVo(station);
        return ResponseData.success(stationTypeItemVo);
    }

    /**
     @PostMapping("/page")
     @ApiOperation(value = "分页查询工序类型")
     @ApiOperationSupport(params = @DynamicParameters(name = "StationTypeRequestModel",properties = {
     @DynamicParameter( name="page", value = "页面，默认为1" ),
     @DynamicParameter( name="limit", value = "分页大小，默认100，可不填" ),
     @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
     @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
     @DynamicParameter(name = "stationType",value = "工位类型"),
     @DynamicParameter(name = "stationTypeName",value = "工位类型名称"),
     @DynamicParameter(name = "stationTypeDesc",value = "工位类型描述")
     }))
     public RestResult<Page<StationType>> page(@RequestBody Map<String,Object> params){
     EntityWrapper<StationType> entityWrapper = new EntityWrapper<>();
     entityWrapper.eq(StationType.SITE, UserUtils.getSite());
     if(!StrUtil.isBlank(params.getOrDefault("stationType","").toString())){
     entityWrapper.andNew().like(StationType.STATION_TYPE,params.get("stationType").toString());
     }
     if(!StrUtil.isBlank(params.getOrDefault("stationTypeName","").toString())){
     entityWrapper.andNew().like(StationType.STATION_TYPE_NAME,params.get("stationTypeName").toString());
     }
     if(!StrUtil.isBlank(params.getOrDefault("stationTypeDesc","").toString())){
     entityWrapper.andNew().like(StationType.STATION_TYPE_DESC,params.get("stationTypeDesc").toString());
     }
     try {
     Page<StationType> stationTypePage = stationTypeService.selectPage(new QueryPage<>(params), entityWrapper);
     return new RestResult<Page<StationType>>(stationTypePage);
     }catch (Exception e){
     logger.error("page -=- {}", ExceptionUtils.getFullStackTrace( e ) );
     return new RestResult<Page<StationType>>(false, 10000, e.getMessage() );
     }
     }*/

}