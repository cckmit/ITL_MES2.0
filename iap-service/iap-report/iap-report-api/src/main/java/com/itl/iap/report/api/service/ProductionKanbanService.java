package com.itl.iap.report.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.dto.AndonDto;
import com.itl.iap.report.api.dto.AndonExceptionDto;
import com.itl.iap.report.api.dto.AndonParamDto;
import com.itl.iap.report.api.dto.AndonWarningDto;
import com.itl.iap.report.api.vo.AndonTypeVo;

import java.util.List;
import java.util.Map;

/**
 * 产线生产看板
 */
public interface ProductionKanbanService {

   /**
    * 根据产线名称筛选
    * @param name
    * @return
    */
   List<Map<String,Object>> selectDeviceState(String name);

   /**
    * 查看产线下的综合信息
    * @param name
    * @return
    */
   Map<String,Object> selectSynthesize(String name);

   /**
    * 查询产线人员良品数
    * @param name
    * @return
    */
   IPage<Map<String,Object>> selectPersonQty(Page page, String name);
}
