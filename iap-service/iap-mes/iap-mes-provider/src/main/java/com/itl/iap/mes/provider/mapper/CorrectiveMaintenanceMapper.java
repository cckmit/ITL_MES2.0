package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.CorrectiveMaintenance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * @time 2018年11月14日
 * @since JDK 1.8
 */

public interface CorrectiveMaintenanceMapper extends BaseMapper<CorrectiveMaintenance> {
    IPage<CorrectiveMaintenance> findList(Page page, @Param("correctiveMaintenance") CorrectiveMaintenance correctiveMaintenance);

    List<Map<String,Object>> getDevice(String code);
}
