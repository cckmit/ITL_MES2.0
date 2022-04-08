package com.itl.mes.pp.api.service;


import com.itl.mes.pp.api.dto.scheduleplan.*;
import com.itl.mes.pp.api.entity.scheduleplan.ResourcesCalendarEntity;

import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2020/12/1 11:30
 */
public interface ResourcesCalendarService {


    /**
     * 查询车间下的资源信息
     * @param workShopBo
     * @return
     */
    List<WorkShopUnderResourcesRespDTO> workShopUnderResources(String workShopBo);


    /**
     * 查询班制班次信息树
     * @return
     */
    List<ResourcesCalendarClassRespDTO> classResources();


    /**
     * 生成资源日历信息
     * @param createResourcesCalendarDTOS
     */
    List<ResourcesCalendarRespDTO> createResourcesCalendars(CreateResourcesCalendarDTO createResourcesCalendarDTOS);


    /**
     * 批量保存,先删除之前的旧数据，再重新插入
     * @param resourcesCalendarEntities
     */
    void batchSave(List<ResourcesCalendarEntity> resourcesCalendarEntities) throws Exception;

    /**
     * 修改
     * @param resourcesCalendarEntity
     */
    void update(ResourcesCalendarEntity resourcesCalendarEntity);



    /**
     * 查询资源日期信息
     * @param resourcesCalendarQueryDTO
     * @return
     */
    ResourcesCalendarGatherRespDTO resourcesCalendars(ResourcesCalendarQueryDTO resourcesCalendarQueryDTO);


    /**
     * 复制资源日历
     * @param resourceCopySaveDTO
     * @throws Exception
     */
    void copyResourcesCalendars(ResourceCopySaveDTO resourceCopySaveDTO) throws Exception;


    /**
     * 根据条件查询资源日历信息，目前用于安灯日志的异常工时计算，获取产线在资源日历的时间
     * @param resourcesCalendarFeignQueryDTO
     * @return
     */
    List<ResourcesCalendarEntity> getResourcesCalendarByTerm(ResourcesCalendarFeignQueryDTO resourcesCalendarFeignQueryDTO);



    void occupyResourcesCalendar(List<Map<String,Object>> maps);
}
