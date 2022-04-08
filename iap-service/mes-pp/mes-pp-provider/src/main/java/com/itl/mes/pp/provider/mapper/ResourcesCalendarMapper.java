package com.itl.mes.pp.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.pp.api.dto.scheduleplan.ClassSystemFrequencyDTO;
import com.itl.mes.pp.api.dto.scheduleplan.ResourcesCalendarQueryDTO;
import com.itl.mes.pp.api.dto.scheduleplan.ResourcesCalendarRespDTO;
import com.itl.mes.pp.api.dto.scheduleplan.WorkShopUnderResourcesRespDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ResourcesCalendarEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2020/12/1 13:40
 */
public interface ResourcesCalendarMapper extends BaseMapper<ResourcesCalendarEntity> {


    /**
     * 查询车间下的资源
     * @param workShopBo
     * @return
     */
    List<WorkShopUnderResourcesRespDTO> workShopUnderResources(String workShopBo);



    List<ClassSystemFrequencyDTO> classResources();

    /**
     * 查询工厂的资源日历信息
     * @param resourcesCalendarQueryDTO
     * @return
     */
    List<ResourcesCalendarRespDTO> getSiteResourcesCalendars(@Param("rcq") ResourcesCalendarQueryDTO resourcesCalendarQueryDTO);

    /**
     * 查询车间的资源日历信息
     * @param resourcesCalendarQueryDTO
     * @return
     */
    List<ResourcesCalendarRespDTO> getWorkShopResourcesCalendars(@Param("rcq") ResourcesCalendarQueryDTO resourcesCalendarQueryDTO);


    /**
     * 查询厂线的资源日历信息
     * @param resourcesCalendarQueryDTO
     * @return
     */
    List<ResourcesCalendarRespDTO> getProductLineResourcesCalendars(@Param("rcq") ResourcesCalendarQueryDTO resourcesCalendarQueryDTO);


    /**
     * 查询设备的资源日历信息
     * @param resourcesCalendarQueryDTO
     * @return
     */
    List<ResourcesCalendarRespDTO> getDeviceResourcesCalendars(@Param("rcq") ResourcesCalendarQueryDTO resourcesCalendarQueryDTO);


    /**
     * 查询用户的资源日历信息
     * @param resourcesCalendarQueryDTO
     * @return
     */
    List<ResourcesCalendarRespDTO> getUserResourcesCalendars(@Param("rcq") ResourcesCalendarQueryDTO resourcesCalendarQueryDTO);

    List<ResourcesCalendarEntity> getResourceCalender(Map<String, Object> params);


    String getProductLineBoByDeviceBo(String deviceBo);


}
