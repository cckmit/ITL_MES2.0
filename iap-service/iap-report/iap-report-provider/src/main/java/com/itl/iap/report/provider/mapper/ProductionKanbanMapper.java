package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.dto.AndonParamDto;
import com.itl.iap.report.api.dto.AndonStateDto;
import com.itl.iap.report.api.entity.AndonException;
import com.itl.iap.report.api.vo.AndonExceptionVo;
import com.itl.iap.report.api.vo.AndonTypeVo;
import com.itl.iap.report.api.vo.AndonVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 产线生产看板
 */
public interface ProductionKanbanMapper extends BaseMapper<AndonException> {

    /**
     * 根据产线名称筛选
     * @param name
     * @return
     */
    List<Map<String,Object>> selectDeviceState(String name);

    /**
     * 根据产线名称筛选人员
     * @param name
     * @return
     */
    Integer selectProductionWorkers(String name);

    /**
     * 根据产线名称筛选出勤人员
     * @param name
     * @return
     */
    Integer selectProductionWorkersAttendance(String name);


    /**
     *根据产线筛选当日产能
     */
    Integer selectCapacity(String name);

    /**
     *根据产线筛选本周产能
     */
    List<Map<String,Object>> selectCapacitys(String name);

    /**
     * 查询产线下的安灯异常信息
     * @param name
     * @return
     */
    List<Map<String,Object>> selectAndonObject(String name);

    /**
     * 查询产线人员良品数
     * @param name
     * @return
     */
    IPage<Map<String,Object>> selectPersonQty(Page page,@Param("name") String name);

    /**
     * 查询产线人员良品数
     * @param name
     * @return
     */
    List<Map<String,Object>> selectPersonDoneQty(String name);
}
