package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.UserStationQueryDTO;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.Station;
import com.itl.mes.core.api.entity.StationTypeItem;
import com.itl.mes.core.api.vo.StationProveVo;
import com.itl.mes.core.api.vo.StationVo;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工位表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-30
 */
public interface StationService extends IService<Station> {

    List<Station> selectList();
    void saveByStationVo(StationVo stationVo)throws CommonException;
    Station selectByStation(String station)throws CommonException;
    StationVo getStationVoByStation(String station)throws CommonException;
    List<Station> selectByStationVo(StationVo stationVo)throws CommonException;
    List<Station> selectStation(String station, String stationName, String stationDesc)throws CommonException;
    int delete(String station, String modifyDate)throws CommonException;
    List<Operation> selectByOpertion(String operation, String operationName, String operationDesc, String version)throws CommonException;
    List<StationTypeItem> selectStationByStationTypeBO(String stationTypeBO)throws CommonException;

    void exportOperation(String site, HttpServletResponse response);

    void saveStationProve(StationProveVo stationProveVo);
    Map<String,Object> getByStation(String station);
    /**
     * 查询用户关联的工位信息
     * @param userStationQueryDTO
     * @return
     */
    IPage<Station> findUserStations(UserStationQueryDTO userStationQueryDTO);

    /**
     * 查询用户未关联的工位信息
     * @param userStationQueryDTO
     * @return
     */
    IPage<Station> findUncorrelatedUserStations(UserStationQueryDTO userStationQueryDTO);

    List<Station> getStationBySiteAndUserName(String site, String userNameOrCardNumber);

    List<Station> getStationBySiteAndCardNumber(String site, String userNameOrCardNumber);
}