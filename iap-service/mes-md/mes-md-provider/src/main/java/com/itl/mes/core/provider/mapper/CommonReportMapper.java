package com.itl.mes.core.provider.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用报表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-09-17
 */
@Repository
public interface CommonReportMapper {


    /**
     * 拉环报表数据查询
     *
     * @param site 工厂
     * @return List<Map<String, Object>>
     */
    //List<Map<String, Object>> selectAllCallDragData( @Param( "site" ) String site, @Param( "sectionCodes" ) List<String> sectionCodes );

    //车间计划跟踪报表
    /*List<WorkShopPlanVo> selectShopOrderByVals(@Param( "site" )String site, @Param( "vals" )String vals,
                                               @Param( "startDate" )String startDate, @Param( "endDate" ) String endDate);*/

    List<Map<String,Object>> selectManualSql( @Param("sql") String sql, @Param( "params" ) Map<String,Object> params );
}
