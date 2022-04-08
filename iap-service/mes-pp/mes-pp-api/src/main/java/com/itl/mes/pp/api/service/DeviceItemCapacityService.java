package com.itl.mes.pp.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.pp.api.dto.MachineProductCapacityDto;
import com.itl.mes.pp.api.entity.DeviceItemCapacityEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cuichonghe
 * @date 2020-12-17 11:28:39
 */
public interface DeviceItemCapacityService extends IService<DeviceItemCapacityEntity> {

  /**
   * 查询全部
   * @param machineProductCapacityDto
   * @return
   */
  IPage<DeviceItemCapacityEntity> getPage(MachineProductCapacityDto machineProductCapacityDto);

  /**
   * getOne
   * @param bo
   * @return
   */
  DeviceItemCapacityEntity getOneById(String bo);

  /**
   * 保存或更新
   *
   * @param deviceItemCapacityEntity
   */
  void saveMachineProductCapacityEntity(DeviceItemCapacityEntity deviceItemCapacityEntity);

  /**
   * excel导出
   * @param response
   */
  void exportOperaton(HttpServletResponse response);

}
