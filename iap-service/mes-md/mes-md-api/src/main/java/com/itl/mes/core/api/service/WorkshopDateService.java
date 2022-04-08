package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.WorkshopDate;
import com.itl.mes.core.api.vo.CalendarShiftVo;
import com.itl.mes.core.api.vo.WorkshopCalendarVo;

import java.util.List;

/**
 * <p>
 * 车间日历表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-26
 */
public interface WorkshopDateService extends IService<WorkshopDate> {

    List<WorkshopDate> selectList();

    public void save(WorkshopCalendarVo workshopCalendarVo)throws CommonException;

    public int ContrastShiftDetail(List<CalendarShiftVo> calendarShiftVos )throws CommonException;

    public WorkshopCalendarVo getWorkshopCalendarVo(String workshop, String startPeriod, String endPeriod)throws CommonException;


    public void delete(String workshop,String startPeriod, String endPeriod)throws CommonException;


    public  List<WorkshopDate> selectWorkshopDateList(String workshop, String startPeriod, String endPeriod)throws CommonException;

    List<CalendarShiftVo> getShiftDetailData()throws CommonException;


    List<WorkshopDate> selectWorkshopDateList(String shiftBO)throws CommonException;
}