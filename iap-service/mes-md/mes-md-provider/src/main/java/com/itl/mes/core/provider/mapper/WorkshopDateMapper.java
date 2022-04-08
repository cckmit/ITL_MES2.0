package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.WorkshopDate;
import com.itl.mes.core.api.vo.CalendarShiftVo;
import com.itl.mes.core.api.vo.PlainShiftVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 车间日历表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-26
 */

public interface WorkshopDateMapper extends BaseMapper<WorkshopDate> {

    List<PlainShiftVo> selectShiftList();

    List<String> getExistsValue(@Param("site") String site, @Param("workshopBO") String workshopBO,@Param("startPeriod")String startPeriod,@Param("endPeriod") String endPeriod);

    List<CalendarShiftVo> getShiftDetailData(@Param("site")String site);

    List<WorkshopDate>  selectWorkShopDate(@Param("site")String site,@Param("workshopBO") String workshopBO,@Param("startPeriod")String startPeriod,@Param("endPeriod") String endPeriod);


    Integer deleteWorkShopDate(@Param("site")String site,@Param("workshopBO") String workshopBO,@Param("startPeriod")String startPeriod,@Param("endPeriod") String endPeriod);
}