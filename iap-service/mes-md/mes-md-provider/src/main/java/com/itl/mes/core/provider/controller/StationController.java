package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.dto.UserStationQueryDTO;
import com.itl.mes.core.api.entity.Station;
import com.itl.mes.core.api.service.StationService;
import com.itl.mes.core.api.service.StationTypeService;
import com.itl.mes.core.api.vo.StationProveVo;
import com.itl.mes.core.api.vo.StationVo;
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
 * @author space
 * @since 2019-05-30
 */
@RestController
@RequestMapping("/stations")
@Api(tags = " 工位表")
public class StationController {
    private final Logger logger = LoggerFactory.getLogger(StationController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public StationService stationService;
    @Autowired
    public StationTypeService stationTypeService;

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过主键查询数据")
    public ResponseData<Station> getStationById(@PathVariable String id) {
        return ResponseData.success(stationService.getById(id));
    }

    @GetMapping("/site")
    @ApiOperation(value = "通过工厂和用户账号查询查询数据")
    public ResponseData<List<Station>> getStationBySite(@RequestParam("site") String site,@RequestParam("userNameOrCardNumber") String userNameOrCardNumber ,@RequestParam("type") String type) {
        /*return ResponseData.success(stationService.list(
                new QueryWrapper<Station>().lambda().eq(Station::getSite, site)));*/
        if (type.equals("0")){
            return ResponseData.success(stationService.getStationBySiteAndUserName(site,userNameOrCardNumber));
        }
        else {
            return ResponseData.success(stationService.getStationBySiteAndCardNumber(site,userNameOrCardNumber));
        }
    }
    @GetMapping("/station/{station}")
    @ApiOperation(value = "通过工位查询数据")
    public ResponseData<Map<String,Object>> getByStation(@PathVariable String station) {
        return ResponseData.success(stationService.getByStation(station));
    }
    @PostMapping("/save")
    @ApiOperation(value = "保存工位数据")
    public ResponseData<StationVo> saveByStationVo(@RequestBody StationVo stationVo) throws CommonException {
        if (stationVo == null) throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        stationService.saveByStationVo(stationVo);
        return ResponseData.success(stationService.getStationVoByStation(stationVo.getStation()));
    }

    @GetMapping("/query")
    @ApiOperation(value = "通过工位编号查询")
    @ApiImplicitParam(name = "station", value = "工位", dataType = "string", paramType = "query")
    public ResponseData<StationVo> selectByStation(@RequestParam String station) throws CommonException {
        return ResponseData.success(stationService.getStationVoByStation(station));
    }

    @GetMapping("/fuzzyQuery")
    @ApiOperation(value = "通过对象模糊查询")
    public ResponseData<List<Station>> selectByStationVo(@RequestBody StationVo stationVo) throws CommonException {
        return ResponseData.success(stationService.selectByStationVo(stationVo));
    }

    @GetMapping("/mFuzzyQuery")
    @ApiOperation(value = "多条件模糊查询")
    public ResponseData<List<Station>> selectStation(String station, String stationName, String stationDesc) throws CommonException {
        return ResponseData.success(stationService.selectStation(station, stationName, stationDesc));
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除工位数据")
    public ResponseData<String> delete(@RequestParam String station, String modifyDate) throws CommonException {
        if (StrUtil.isBlank(modifyDate)) {
            throw new CommonException("修改时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        stationService.delete(station, modifyDate);
        return ResponseData.success("success");
    }


    @PostMapping("/page")
    @ApiOperation(value = "分页查询工位")
    @ApiOperationSupport(params = @DynamicParameters(name = "StationRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面,默认1"),
            @DynamicParameter(name = "limit", value = "分页大小,默认100,可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性,可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式,true/false,可不填"),
            @DynamicParameter(name = "station", value = "工位"),
            @DynamicParameter(name = "stationName", value = "工位名称"),
            @DynamicParameter(name = "stationDesc", value = "工位描述")
    }))
    public ResponseData<IPage<Station>> page(@RequestBody Map<String, Object> params) {
        QueryWrapper<Station> entityWrapper = new QueryWrapper();
        entityWrapper.eq(Station.SITE, UserUtils.getSite());
        if (!StrUtil.isBlank(params.getOrDefault("station", "").toString())) {
            entityWrapper.like(Station.STATION, params.get("station").toString());
        }
        if (!StrUtil.isBlank(params.getOrDefault("stationName", "").toString())) {
            entityWrapper.like(Station.STATION_NAME, params.get("stationName").toString());
        }
        if (!StrUtil.isBlank(params.getOrDefault("stationDesc", "").toString())) {
            entityWrapper.like(Station.STATION_DESC, params.get("stationDesc").toString());
        }
        IPage<Station> page = stationService.page(new QueryPage<>(params), entityWrapper);
        return ResponseData.success(page);
    }


    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportItem(String site, HttpServletResponse response) {
        stationService.exportOperation(site, response);
    }

    @RequestMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("FileName", file.getOriginalFilename());
        //POIUtils.checkFile(file);
        List<StationVo> stationVos = ExcelUtils.importExcel(file, 1, 1, StationVo.class);
        for (StationVo stationVo : stationVos) {
            stationService.saveByStationVo(stationVo);
        }
        return ResponseData.success("success");
    }


    @PostMapping("saveStationProve")
    @ApiOperation(value = "保存工位证明关联数据")
    public ResponseData saveStationProve(@RequestBody StationProveVo stationProveVo) {

        try {
            stationService.saveStationProve(stationProveVo);
            return ResponseData.success();
        } catch (Exception e) {
            logger.error("saveStationProve -=- {}", ExceptionUtils.getFullStackTrace(e));
            return ResponseData.error(e.getMessage());
        }

    }

    @PostMapping("/findUserStations")
    @ApiOperation(value = "查询用户关联的工位信息")
    public ResponseData<IPage<Station>> findUserStations(@RequestBody UserStationQueryDTO userStationQueryDTO) {
        return ResponseData.success(stationService.findUserStations(userStationQueryDTO));
    }

    @PostMapping("/findUncorrelatedUserStations")
    @ApiOperation(value = "查询用户未关联的工位信息")
    public ResponseData<IPage<Station>> findUncorrelatedUserStations(@RequestBody UserStationQueryDTO userStationQueryDTO) {
        return ResponseData.success(stationService.findUncorrelatedUserStations(userStationQueryDTO));
    }

}