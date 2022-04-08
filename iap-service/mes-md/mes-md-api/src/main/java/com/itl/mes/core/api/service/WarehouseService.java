package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.Warehouse;
import com.itl.mes.core.api.vo.WarehouseVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 线边仓信息表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-18
 */
public interface WarehouseService extends IService<Warehouse> {

    List<Warehouse> selectList();

   public Warehouse selectWarehouse(String warehouse)throws CommonException;

  public   void save(WarehouseVo warehouseVo)throws CommonException;

   public void delete(String warehouse, Date modifyDate)throws CommonException;

   public WarehouseVo getWarehouseVo(String warehouse)throws  CommonException;
}