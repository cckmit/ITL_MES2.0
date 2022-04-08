package com.itl.mes.core.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.dto.RouteStationDTO;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.RouteStation;
import com.itl.mes.core.api.entity.WorkStation;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.service.RouteStationService;
import com.itl.mes.core.api.service.WorkStationService;
import com.itl.mes.core.provider.mapper.RouteStationMapper;
import com.itl.mes.core.provider.mapper.WorkStationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工艺路线工步配置
 * </p>
 *
 * @author pwy
 * @since 2021-3-12
 */
@Service
@Transactional
public class RouteStationServiceImpl extends ServiceImpl<RouteStationMapper, RouteStation> implements RouteStationService {

    @Autowired
    RouteStationMapper routerStationMapper;
    @Autowired
    RouteStationService routeStationService;
    @Autowired
    WorkStationService workStationService;
    @Autowired
    OperationService operationService;
    @Resource
    private UserUtil userUtil;
    @Autowired
    WorkStationMapper workStationMapper;

    public int updateEffective(String bo,String effective){
        return routerStationMapper.updateEffective(bo,effective);
    }

    @Override
    public String queryRouterOperation(String router, String version) {
        return routerStationMapper.queryRouterOperation(router,version);
    }


    @Override
    public IPage<WorkStation> selectWsByRouterAndVersion(String router, String version, Integer page, Integer pageSize) {
        String processInfo = routeStationService.queryRouterOperation(router,version);//通过工艺编号和版本查询processInfo信息

        JSONObject processInfoObj = JSONObject.parseObject(processInfo);
        JSONArray nodeList = JSONArray.parseArray(processInfoObj.getString("nodeList"));
        List<String> operationList = new ArrayList<>();
        if (nodeList.size() > 0 ){
            for (int i = 0; i < nodeList.size(); i++) {
                JSONObject operationObj = JSON.parseObject(nodeList.get(i).toString());
                if (StringUtils.isNotBlank(operationObj.getString("operation"))){
                    operationList.add(operationObj.getString("operation"));
                }
            }
        }
        if (operationList.size() >0 ){
            return workStationMapper.selectByWorkingProcess(new Page(page,pageSize), operationList);
        }
        return null;
    }

    @Override
    public void updateRouteStation(List<RouteStationDTO> routeStationDTO) {
        for (RouteStationDTO stationDTO : routeStationDTO) {
            QueryWrapper<RouteStation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("process_route",stationDTO.getProcessRoute());
            queryWrapper.eq("route_ver",stationDTO.getRouteVer());
            queryWrapper.eq("work_step_code",stationDTO.getWorkStepCode());
            queryWrapper.eq("working_process",stationDTO.getWorkingProcess());

            List<RouteStation> routeStationList = routeStationService.list(queryWrapper);
            RouteStation routeStation = new RouteStation();
            routeStation.setSite(UserUtils.getSite());
            routeStation.setBo(UserUtils.getSite() + ":" + stationDTO.getWorkStepCode());
            routeStation.setUpdatedBy(userUtil.getUser().getUserName());
            routeStation.setUpdateTime(new Date());
            BeanUtils.copyProperties(stationDTO,routeStation);
            if (CollectionUtils.isEmpty(routeStationList)){//如果没有数据
                routerStationMapper.insert(routeStation);
            }else {
                routerStationMapper.update(routeStation,queryWrapper);
            }
        }

    }

    @Override
    public ResponseData<String> insertOrUpdate(String router, String version) throws CommonException {
        //查询出工艺路线对应工序
        String processInfo = routeStationService.queryRouterOperation(router,version);
        JSONObject jsonObj = JSON.parseObject(processInfo);
        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        int length = nodeList.size();
        if(length>0){
            for(int i=0;i<nodeList.size();i++){
                JSONObject operationObj = JSON.parseObject(nodeList.get(i).toString());
                //查询工序
                String operation = operationObj.getString("operation");
                List<Operation> operationEntity = operationService.selectByOperation(operation);
                String operationName = "";
                String operationVersion = "";
                if(operationEntity!=null){
                    operationName = operationEntity.get(0).getOperationName();
                    operationVersion = operationEntity.get(0).getVersion();
                }
                //查询工步
                QueryWrapper wrapper = new QueryWrapper<WorkStation>();
                wrapper.eq("WORKING_PROCESS",operation);
                List<WorkStation> workStations = workStationService.list(wrapper);
                //封装工艺路线工步信息
                List<RouteStation> routeStationList = new ArrayList<>();
                List<RouteStation> routeStations = new ArrayList<>();
                if (workStations!=null){
                    for(int j=0;j<workStations.size();j++){
                        WorkStation workStation = workStations.get(i);
                        RouteStation routeStation = new RouteStation();
                        routeStation.setWorkingProcess(operation);
                        routeStation.setWorkingProcessName(operationName);
                        routeStation.setWorkStepCode(workStation.getWorkStepCode());
                        routeStation.setWorkStepName(workStation.getWorkStepName());
                        routeStation.setProcessRoute(router);
                        routeStation.setRouteVer(operationVersion);
                        routeStation.setUpdatedBy(userUtil.getUser().getUserName());
                        routeStation.setEffective("1");//给默认值为有效
                        Date newDate = new Date();
                        routeStation.setUpdateTime(newDate);
                        routeStationList.add(routeStation);
                    }
                    QueryWrapper routeWrapper = new QueryWrapper<RouteStation>();
                    routeWrapper.eq("PROCESS_ROUTE",router);
                    routeWrapper.eq("ROUTE_VER",version);
                    routeStations = routeStationService.list(routeWrapper);
                    if(routeStations!=null){
                        //已有数据，先删除后插入
                        routeStationService.remove(routeWrapper);
                        routeStationService.saveBatch(routeStationList);
                    }else{
                        //没有保存过，直接插入
                        routeStationService.saveBatch(routeStationList);
                    }
                }else{
                    throw new CommonException("未获取到工艺路线工步信息！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }
        }else{
            throw new CommonException("未获取到工艺路线工序信息！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return  ResponseData.success("保存工艺路线工步配置信息成功！");
    }
}
