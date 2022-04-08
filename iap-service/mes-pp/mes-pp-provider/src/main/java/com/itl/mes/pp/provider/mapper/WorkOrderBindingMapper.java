package com.itl.mes.pp.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.pp.api.entity.WorkOrderBinding;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * @author xuejh
 * @date 2020/11/12 9:52
 */
public interface WorkOrderBindingMapper extends BaseMapper<WorkOrderBinding> {

    IPage getProductLine(Page page, @Param("params") Map<String, Object> params);


    List<Map<String,Object>> getAllCustVals(String machineProperties);

    String getWorkshop(Map<String, Object> productLineMap);
}
