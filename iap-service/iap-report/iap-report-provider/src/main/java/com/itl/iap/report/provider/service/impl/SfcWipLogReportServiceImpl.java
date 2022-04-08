package com.itl.iap.report.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.CalendarAdjust;
import com.itl.iap.report.api.dto.*;
import com.itl.iap.report.api.entity.NotAttendanceDetailed;
import com.itl.iap.report.api.entity.SfcWipLog;
import com.itl.iap.report.api.service.SfcWipLogReportService;
import com.itl.iap.report.api.vo.*;
import com.itl.iap.report.provider.mapper.SfcWipLogReportMapper;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.mes.core.api.vo.SfcDeviceVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
@Transactional
public class SfcWipLogReportServiceImpl extends ServiceImpl<SfcWipLogReportMapper, SfcWipLog> implements SfcWipLogReportService {

    @Autowired
    private SfcWipLogReportMapper sfcWipLogReportMapper;

    @Override
    public IPage<SfcWipLog> queryList(SfcWipLogReportDto sfcWipLogReportDto) {
        IPage<SfcWipLog> page = sfcWipLogReportMapper.queryList(sfcWipLogReportDto.getPage(),sfcWipLogReportDto);
        return page;
    }

    @Override
    public IPage<SfcDeviceVO> queryPullIn(SfcDeviceVO sfcDeviceVO) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(sfcDeviceVO!=null){
            if (sfcDeviceVO.getInTime() != null) {
                sfcDeviceVO.setSelectInTime(simpleDateFormat.format(sfcDeviceVO.getInTime()));
            }
        }
        return sfcWipLogReportMapper.queryPullIn(sfcDeviceVO.getPage(), sfcDeviceVO);
    }

    @Override
    public IPage<ProductVo> queryProductInfo(ProductDto productDto) {
        IPage<ProductVo> productVoIPage=sfcWipLogReportMapper.selectProductInfo(productDto.getPage(),productDto);
        return productVoIPage;
    }

    @Override
    public IPage<ProductOperationVo> selectProductInfoByOperationBo(ProductDto productDto) {
        IPage<ProductOperationVo> productOperationVoIPage= sfcWipLogReportMapper.selectProductInfoByOperationBo(productDto.getPage(),productDto);
        return productOperationVoIPage;
    }

    @Override
    public IPage<ProductShopOrderVo> selectProductShopOrderInfo(ProductDto productDto) {
        IPage<ProductShopOrderVo> productShopOrderVoIPage= sfcWipLogReportMapper.selectProductShopOrderInfo(productDto.getPage(),productDto);
        return productShopOrderVoIPage;
    }

    @Override
    public UserProductVo selectUserProduct(UserProductDto userProductDto) {
        UserProductVo userProductVo=new UserProductVo();
        IPage<UserProduct> userProductIPage=sfcWipLogReportMapper.selectUserProduct(userProductDto.getPage(), userProductDto);
        int allQty=0;
        if(StringUtils.isNotBlank(userProductDto.getUserName()) || StringUtils.isNotBlank(userProductDto.getRealName())){
            allQty=sfcWipLogReportMapper.selectAllQtyByUser(userProductDto);
        }
        userProductVo.setUserProductIPage(userProductIPage);
        userProductVo.setAllQty(allQty);
        return userProductVo;
    }

    @Override
    public IPage<OperationCapacityVo> listOperationCapacity(OperationCapacityDto operationCapacityDto) {
        IPage<OperationCapacityVo> operationCapacityVoIPage = sfcWipLogReportMapper.listOperationCapacity(operationCapacityDto.getPage(), operationCapacityDto);
        BigDecimal hour;
        for (OperationCapacityVo operationCapacityVo : operationCapacityVoIPage.getRecords()) {
            hour = getHour(operationCapacityVo.getProductTime());
            operationCapacityVo.setProductTime(hour.toString());
            if (hour.compareTo(BigDecimal.ZERO) == 0){
                operationCapacityVo.setCapacity(BigDecimal.ZERO);
            }else {
                operationCapacityVo.setCapacity(operationCapacityVo.getOutStationQty().divide(hour,2,ROUND_HALF_UP));
            }
        }
        return operationCapacityVoIPage;
    }
    public static BigDecimal getHour(String second) {
        BigDecimal secondBig = new BigDecimal(second);
        if (secondBig.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }
        BigDecimal hour = secondBig.divide(new BigDecimal("3600"),2, ROUND_HALF_UP);
        if (hour.compareTo(BigDecimal.ZERO) == 0){
            return new BigDecimal("0.01");
        }
        return hour;
    }

    @Override
    public IPage<StaffAttendanceVo> listStaffAttendance(StaffAttendanceDto staffAttendanceDto) throws ParseException {
        long current = staffAttendanceDto.getPage().getCurrent() - 1;
        long size = staffAttendanceDto.getPage().getSize();

        List<StaffAttendanceVo> staffAttendanceVoList = sfcWipLogReportMapper.listStaffAttendance(staffAttendanceDto);
        List<StaffAttendanceVo> collect = staffAttendanceVoList.stream().skip(current * size).limit(size).collect(Collectors.toList());
        String totalPeoples;
        int absenceFromDutyPeoplesInt;
        int totalPeoplesInt;
        String attendance;
        List<NotAttendanceDetailed> notAttendanceDetailedList;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        for (StaffAttendanceVo staffAttendance : collect) {
            totalPeoples = sfcWipLogReportMapper.getTotalPeoplesByCondition(staffAttendance.getWorkShopBo(),
                    staffAttendance.getProductLineBo(),
                    staffAttendance.getAttendanceDate());
            staffAttendance.setTotalPeoples(totalPeoples);
            absenceFromDutyPeoplesInt = Integer.parseInt(staffAttendance.getAbsenceFromDutyPeoples());
            totalPeoplesInt = Integer.parseInt(totalPeoples);
            attendance = df.format(absenceFromDutyPeoplesInt * 100.00 / totalPeoplesInt) + "%";
            staffAttendance.setAttendance(attendance);
            notAttendanceDetailedList = sfcWipLogReportMapper.getNotAttendanceDetailed(staffAttendance.getWorkShopBo(),
                    staffAttendance.getProductLineBo(),
                    staffAttendance.getAttendanceDate());
            staffAttendance.setNotAttendanceDetailedList(notAttendanceDetailedList);
            staffAttendance.setNotAttendancePeoples(notAttendanceDetailedList.size());
//            for (NotAttendanceDetailed notAttendanceDetailed : notAttendanceDetailedList) {
//                String subDay = subDay(staffAttendance.getAttendanceDate());
//                int i = 1;
//                while (true){
//                    if (i>3){
//                        break;
//                    }
//                    int isAttendance = sfcWipLogReportMapper.currentDayIsAttendance(subDay, notAttendanceDetailed.getUserBo());
//                    if (isAttendance > 0){
//                        break;
//                    }
//                    subDay = subDay(subDay);
//                    i++;
//                }
//                if (i>3){
//                    notAttendanceDetailed.setContinuityAbsenceFromDutyDays("超过3天");
//                }else {
//                    notAttendanceDetailed.setContinuityAbsenceFromDutyDays(String.valueOf(i));
//                }
//            }
        }
        long total;
        long pages;
        if (CollectionUtils.isEmpty(collect)) {
            total = 0L;
            pages = 0L;
        } else {
            total = staffAttendanceVoList.size();
            pages = total / size;
            if (total % size != 0) {
                pages++;
            }
        }
        Page<StaffAttendanceVo> staffAttendanceVoIPage = new Page<>(current, size);
        staffAttendanceVoIPage.setTotal(total);
        staffAttendanceVoIPage.setRecords(collect);
        staffAttendanceVoIPage.setPages(pages);
        return staffAttendanceVoIPage;
    }

    /**
     * 时间减少一天
     * @param date
     * @return
     */
    public String subDay(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DAY_OF_MONTH, -1);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    /**
     * 封装时间
     * @param staffDailyEnergyVo
     */
    public static void encapsulationDate(StaffDailyEnergyVo staffDailyEnergyVo) {
        if (StringUtils.isBlank(staffDailyEnergyVo.getStartTime()) && StringUtils.isBlank(staffDailyEnergyVo.getEndTime())){
            //如果前台没有传时间，则默认为当天
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = simpleDateFormat.format(new Date());
            staffDailyEnergyVo.setStartTime(currentDate + " 00:00:00");
            staffDailyEnergyVo.setEndTime(currentDate + " 23:59:59");
        }
    }

    @Override
    public IPage<PlanReachedVo> listPlanReached(PlanReachedDto planReachedDto) {
        encapsulationDate(planReachedDto);
        IPage<PlanReachedVo> planReachedVoIPage = sfcWipLogReportMapper.listPlanReached(planReachedDto.getPage(), planReachedDto);
        for (PlanReachedVo planReachedVo : planReachedVoIPage.getRecords()) {
            String itemBo = planReachedVo.getItemBo();
            planReachedDto.setItemBo(itemBo);
            BigDecimal doneTotalQty = new BigDecimal(sfcWipLogReportMapper.sumDoneQtyByItem(planReachedDto));
            BigDecimal stockTotalQty = new BigDecimal(sfcWipLogReportMapper.sumStockQtyByItem(planReachedDto));
            BigDecimal makingTotalQty = new BigDecimal(sfcWipLogReportMapper.sumMakingQtyByItem(planReachedDto));
            planReachedVo.setDoneTotalQty(doneTotalQty);
            planReachedVo.setStockTotalQty(stockTotalQty);
            planReachedVo.setMakingTotalQty(makingTotalQty);
            //待生产数量 = 计划总数 - 完成数量 - 在制数量
            planReachedVo.setWaitInTotalQty(new BigDecimal(planReachedVo.getPlanTotalQty()).subtract(doneTotalQty).subtract(makingTotalQty));
        }
        return planReachedVoIPage;
    }

    /**
     * 封装时间（当月）
     * @param planReachedDto
     */
    public static void encapsulationDate(PlanReachedDto planReachedDto){
        if (StringUtils.isBlank(planReachedDto.getPlanStartTimeStart()) &&
                StringUtils.isBlank(planReachedDto.getPlanStartTimeEnd()) &&
                StringUtils.isBlank(planReachedDto.getPlanEndTimeStart()) &&
                StringUtils.isBlank(planReachedDto.getPlanEndTimeEnd())){
            Long currentTime = System.currentTimeMillis();
            String startTime = CalendarAdjust.timestampToStr(CalendarAdjust.getMonthStartTime(currentTime, "GMT+8:00"), "GMT+8").replaceAll("T"," ");
            String endTime = CalendarAdjust.timestampToStr(CalendarAdjust.getMonthEndTime(currentTime, "GMT+8:00"), "GMT+8").replaceAll("T"," ");
            planReachedDto.setPlanStartTimeStart(startTime);
            planReachedDto.setPlanStartTimeEnd(endTime);
            planReachedDto.setPlanEndTimeStart(startTime);
            planReachedDto.setPlanEndTimeEnd(endTime);
        }
    }

    @Override
    public List<PlanReachedOrderDetailsVo> planReachedOrderDetails(PlanReachedDto planReachedDto) {
        encapsulationDate(planReachedDto);
//        String itemBo = planReachedDto.getItemBo();
        List<PlanReachedOrderDetailsVo> planReachedOrderDetailsVos = sfcWipLogReportMapper.planReachedOrderDetails(planReachedDto);
        for (PlanReachedOrderDetailsVo planReachedOrderDetailsVo : planReachedOrderDetailsVos) {
            String shopOrderBo = planReachedOrderDetailsVo.getShopOrderBo();
            planReachedDto.setShopOrderBo(shopOrderBo);
            BigDecimal makingQty = new BigDecimal(sfcWipLogReportMapper.sumMakingQtyByItem(planReachedDto));
            BigDecimal doneQty = new BigDecimal(sfcWipLogReportMapper.sumDoneQtyByItem(planReachedDto));
            BigDecimal stockQty = new BigDecimal(sfcWipLogReportMapper.sumStockQtyByItem(planReachedDto));
            BigDecimal waitInQty = new BigDecimal(planReachedOrderDetailsVo.getOrderQty()).subtract(doneQty).subtract(makingQty);
            planReachedOrderDetailsVo.setDoneQty(doneQty);
            planReachedOrderDetailsVo.setStockQty(stockQty);
            planReachedOrderDetailsVo.setMakingQty(makingQty);
            planReachedOrderDetailsVo.setWaitInQty(waitInQty);
        }
        return planReachedOrderDetailsVos;
    }
}
