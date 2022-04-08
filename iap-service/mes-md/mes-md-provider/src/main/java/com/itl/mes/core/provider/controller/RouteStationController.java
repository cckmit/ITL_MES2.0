package com.itl.mes.core.provider.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.RouteStationDTO;
import com.itl.mes.core.api.entity.RouteStation;
import com.itl.mes.core.api.entity.WorkStation;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.service.RouteStationService;
import com.itl.mes.core.api.service.RouterService;
import com.itl.mes.core.api.service.WorkStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 *
 * @author pwy
 * @since 2021-3-12
 */
@RestController
@RequestMapping("/routeStations")
@Api(tags = " 工艺路线工步配置表" )
public class RouteStationController {

    @Autowired
    RouteStationService routeStationService;
    @Autowired
    WorkStationService workStationService;
    @Autowired
    OperationService operationService;
    @Autowired
    RouterService routerService;


    /**
     * 根据工艺路线编号和工艺路线版本查询工步信息
     * @param router
     * @param version
     * @return
     */
    @GetMapping("/selectWsByRouterAndVersion")
    @ApiOperation(value="根据工艺路线编号和工艺路线版本查询工步信息")
    public ResponseData<IPage<WorkStation>> selectWsByRouterAndVersion(@RequestParam(name="router")String router,
                                                                       @RequestParam(name="version")String version,
                                                                       @RequestParam(value = "page", required = true) Integer page,
                                                                       @RequestParam(value = "pageSize", required = true) Integer pageSize){
        return ResponseData.success(routeStationService.selectWsByRouterAndVersion(router,version,page,pageSize));
    }

    /**
     * 更新或者新增配置
     * @return
     */
    @PostMapping("/saveConfig")
    @ApiOperation(value="更新或者新增配置")
    public ResponseData saveConfig(@RequestBody List<RouteStationDTO> routeStationDTO){
        routeStationService.updateRouteStation(routeStationDTO);
        return ResponseData.success("操作成功");
    }

    /**
     * 分页查询工步配置信息
     */
    @GetMapping("/queryRouteStation")
    @ApiOperation(value="分页查询工步配置信息")
    public ResponseData<IPage<RouteStation>> queryRouteStation(@RequestParam(name="router")String router,
                                                               @RequestParam(name="version")String version,
                                                               @RequestParam(value = "page", required = true) Integer page,
                                                               @RequestParam(value = "pageSize", required = true) Integer pageSize){
        QueryWrapper<RouteStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROCESS_ROUTE",router);
        queryWrapper.eq("ROUTE_VER",version);
        return ResponseData.success(routeStationService.page(new Page(page,pageSize),queryWrapper));
    }
}
