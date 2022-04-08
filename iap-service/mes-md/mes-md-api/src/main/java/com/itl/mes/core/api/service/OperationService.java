package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.vo.OperationVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
public interface OperationService extends IService<Operation> {

    List<Operation> selectList();

    void saveByOperationVo(OperationVo operationVo)throws CommonException;
    Operation selectByOperation(String operation, String version)throws CommonException;

    /**
     * 查询工序当前版本
     *
     * @param site 工厂
     * @param operation 工序
     * @return Operation
     */
    Operation getBasicCurrentOperation(String site, String operation);

    OperationVo getOperationVoByOperationAndVersion(String operation, String version)throws CommonException;
    public List<Operation> selectOperation(String operation, String operationName, String operationDesc, String operationType, String productionLine, String version)throws CommonException;
    List<Operation> selectByOperation(String operation)throws CommonException;
    List<Operation> selectByOperation(Map<String, String> map)throws CommonException;
    int delete(String operation, String version, String modfiyDate)throws CommonException;

    List<NcCode> selectByNcCode(String ncCode, String ncName, String ncDesc) throws CommonException;
    List<NcGroup> selectByNcGroup(String ncGroup, String ncGroupName, String ncGroupDesc)throws CommonException;
    List<Station> selectStation(String station, String stationName, String stationDesc)throws CommonException;
    List<StationType> selectStationType(String stationType, String stationTypeName, String stationTypeDesc)throws CommonException;

    void updateOperationVersionType(String operation)throws CommonException;


    /**
     * 获取当前版本工序，返回字段包含：OPERATION：工序，OPERATION_DESC：工序描述
     *
     * @param site 工厂
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> selectCurrentVersionOperations(String site);

    List<Operation> getOperationByStationTypeBO(String stationTypeBO)throws CommonException;


    void exportOperation(String site, HttpServletResponse response);

    IPage<OperationOrder> selectAllOrderOperation(SfcDto sfcDto);
    String selectShopOrderByOperationOrder(String operationOrder);
}