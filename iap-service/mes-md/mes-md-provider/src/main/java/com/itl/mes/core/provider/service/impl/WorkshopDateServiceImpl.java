package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.entity.Shift;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.entity.WorkshopDate;
import com.itl.mes.core.api.service.ShiftDetailService;
import com.itl.mes.core.api.service.ShiftService;
import com.itl.mes.core.api.service.WorkShopService;
import com.itl.mes.core.api.service.WorkshopDateService;
import com.itl.mes.core.api.vo.CalendarShiftVo;
import com.itl.mes.core.api.vo.WorkshopCalendarVo;
import com.itl.mes.core.api.vo.WorkshopDateVo;
import com.itl.mes.core.provider.mapper.WorkshopDateMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车间日历表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-26
 */
@Service
@Transactional
public class WorkshopDateServiceImpl extends ServiceImpl<WorkshopDateMapper, WorkshopDate> implements WorkshopDateService {


    @Autowired
    private WorkshopDateMapper workshopDateMapper;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private ShiftDetailService shiftDetailService;
    @Autowired
    private WorkShopService workShopService;
    @Resource
    private UserUtil userUtil;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public List<WorkshopDate> selectList() {
        QueryWrapper<WorkshopDate> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, workshopDate);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(WorkshopCalendarVo workshopCalendarVo) throws CommonException {
        delete(workshopCalendarVo.getWorkshop(),workshopCalendarVo.getStartPeriod(),workshopCalendarVo.getEndPeriod());
        List<CalendarShiftVo> calendarShiftVos = workshopCalendarVo.getCalendarShiftVos();
        if(ContrastShiftDetail(calendarShiftVos) != -1){
            CalendarShiftVo calendarShiftVo = calendarShiftVos.get(ContrastShiftDetail(calendarShiftVos));

            throw new CommonException("班次:"+calendarShiftVo.getShift()+"的时间和其他班次时间重叠", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        WorkShop workShop = workShopService.validateWorkShopIsExist(UserUtils.getSite(), workshopCalendarVo.getWorkshop());
        List<String> periodList = workshopCalendarVo.getPeriod();
        for(CalendarShiftVo calendarShiftVo:calendarShiftVos){
            for(String period : periodList ){
                WorkshopDate workshopDate = new WorkshopDate();
                workshopDate.setWorkshopBo(workShop.getBo());
                workshopDate.setSite(UserUtils.getSite());
                String startDate = period+" "+calendarShiftVo.getShiftStartDate();
                if(calendarShiftVo.getIsCurrent().equals("Y")) {
                    String endDate = period + " " + calendarShiftVo.getShiftEndDate();
                    try {
                        Date parse = formatter.parse(endDate);
                        Calendar c = Calendar.getInstance();
                        c.setTime(parse);
                        c.add(Calendar.DAY_OF_MONTH,1);     //利用Calendar 实现 Date日期+1天
                        parse = c.getTime();
                        endDate = formatter.format(parse);
                    } catch (ParseException e) {
                        throw new CommonException("时间格式错误", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    workshopDate.setEndTime(endDate);
                }else if(calendarShiftVo.getIsCurrent().equals("N")){
                    String endDate = period + " " + calendarShiftVo.getShiftEndDate();
                    workshopDate.setEndTime(endDate);
                }
                workshopDate.setProductDate(period);
                workshopDate.setStartTime(startDate);
                Shift shift = shiftService.selectShift(calendarShiftVo.getShift());
                workshopDate.setShiftBo(shift.getBo());
                workshopDate.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),new Date());
                workshopDateMapper.insert(workshopDate);
            }
        }

    }

    @Override
    public int ContrastShiftDetail(List<CalendarShiftVo> calendarShiftVos) throws CommonException {
        int k = -1;
        if(calendarShiftVos == null||calendarShiftVos.isEmpty()) throw new CommonException("班次不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);

            for(int i =0 ;i<calendarShiftVos.size()-1;i++){
                for(int j=(i+1);j<calendarShiftVos.size();j++){
                    try {
                    CalendarShiftVo calendarShiftVoI = calendarShiftVos.get(i);
                    String shiftStartDateI ="2019-07-27 "+ calendarShiftVoI.getShiftStartDate();
                        String shiftEndDateI = null;
                    if(calendarShiftVoI.getIsCurrent().equals("N")) {
                        shiftEndDateI = "2019-07-27 " + calendarShiftVoI.getShiftEndDate() ;
                    }else if(calendarShiftVoI.getIsCurrent().equals("Y")){
                        shiftEndDateI = "2019-07-28 " + calendarShiftVoI.getShiftEndDate();
                    }else{
                        throw  new CommonException("是否属于当天值填写不正确", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    Date startDateI = formatter.parse(shiftStartDateI);
                    Date endDateI = formatter.parse(shiftEndDateI);
                    CalendarShiftVo calendarShiftVoJ = calendarShiftVos.get(j);
                    String shiftStartDateJ ="2019-07-27 "+ calendarShiftVoJ.getShiftStartDate();
                        String shiftEndDateJ = null;
                    if(calendarShiftVoJ.getIsCurrent().equals("N")) {
                         shiftEndDateJ = "2019-07-27 " + calendarShiftVoJ.getShiftEndDate();
                    }else if(calendarShiftVoJ.getIsCurrent().equals("Y")){
                        shiftEndDateJ = "2019-07-28 " + calendarShiftVoJ.getShiftEndDate();
                    }else{
                        throw  new CommonException("是否属于当天值填写不正确", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    Date startDateJ = formatter.parse(shiftStartDateJ);
                    Date endDateJ = formatter.parse(shiftEndDateJ);
                        if(startDateI.compareTo(startDateJ)>0){
                           if( endDateI.compareTo(startDateJ)<0) {
                               return i;
                           }
                        }else if(startDateI.compareTo(startDateJ)<0){
                            if(startDateJ.compareTo(endDateI)<0){
                                return i;
                            }
                        }else if(startDateI.compareTo(startDateJ)==0){
                            return i;
                        }
                    } catch (ParseException e) {
                       throw new  CommonException("班次中的时间格式不对", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }

            }


        return k;
    }

    @Override
    public WorkshopCalendarVo getWorkshopCalendarVo(String workshop, String startPeriod, String endPeriod) throws CommonException {
        List<WorkshopDate> workshopDates = selectWorkshopDateList(workshop, startPeriod, endPeriod);
        List<WorkshopDateVo> workshopDateVos = new ArrayList<WorkshopDateVo>();
        if(workshopDates != null&&workshopDates.size()>0){
            for(WorkshopDate workshopDate:workshopDates) {
                Shift shift = shiftService.getById(workshopDate.getShiftBo());
                WorkshopDateVo workshopDateVo = new WorkshopDateVo();
                BeanUtils.copyProperties(workshopDate,workshopDateVo);
                workshopDateVo.setShift(shift.getShift());
                workshopDateVo.setWorkShop(workshop);
                workshopDateVo.setShiftName(shift.getShiftName());
                workshopDateVos.add(workshopDateVo);
            }
        }
        WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO(UserUtils.getSite(),workshop);
        List<String> existsValue = workshopDateMapper.getExistsValue(UserUtils.getSite(), workShopHandleBO.getBo(), startPeriod, endPeriod);
        WorkshopCalendarVo workshopCalendarVo = new WorkshopCalendarVo();
        workshopCalendarVo.setWorkshopDateVoList(workshopDateVos);
        workshopCalendarVo.setPeriod(existsValue);
        workshopCalendarVo.setStartPeriod(startPeriod);
        workshopCalendarVo.setEndPeriod(endPeriod);
        workshopCalendarVo.setWorkshop(workshop);
        return  workshopCalendarVo;
    }



    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void delete(String workshop, String startPeriod, String endPeriod) throws CommonException {
        WorkShop workShop = workShopService.validateWorkShopIsExist(UserUtils.getSite(), workshop);
        Integer integer = workshopDateMapper.deleteWorkShopDate(UserUtils.getSite(), workShop.getBo(), startPeriod, endPeriod);
    }



    @Override
    public List<WorkshopDate> selectWorkshopDateList(String workshop, String startPeriod, String endPeriod) throws CommonException {
        WorkShop workShop = workShopService.validateWorkShopIsExist(UserUtils.getSite(), workshop);
        List<WorkshopDate> workshopDates = workshopDateMapper.selectWorkShopDate(UserUtils.getSite(), workShop.getBo(), startPeriod, endPeriod);
        return workshopDates;
    }

    @Override
    public List<CalendarShiftVo> getShiftDetailData() throws CommonException {
        List<CalendarShiftVo> shiftDetailData = workshopDateMapper.getShiftDetailData(UserUtils.getSite());
        return shiftDetailData;
    }

    @Override
    public List<WorkshopDate> selectWorkshopDateList(String shiftBO) throws CommonException {
        QueryWrapper<WorkshopDate> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(WorkshopDate.SHIFT_BO,shiftBO);
        List<WorkshopDate> workshopDates = workshopDateMapper.selectList(entityWrapper);
        return workshopDates;
    }


}