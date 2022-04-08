package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.dto.*;
import com.itl.iap.report.api.entity.NotAttendanceDetailed;
import com.itl.iap.report.api.entity.SfcWipLog;
import com.itl.iap.report.api.vo.*;
import com.itl.mes.core.api.vo.SfcDeviceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SfcWipLogReportMapper extends BaseMapper<SfcWipLog> {
    IPage<SfcWipLog> queryList(Page page, @Param("sfcWipLogReportDto") SfcWipLogReportDto sfcWipLogReportDto);

    /**
     * 查询进站信息
     * @param sfcDeviceVO
     * @return
     */
    IPage<SfcDeviceVO> queryPullIn(Page page,@Param("sfcDeviceVO")SfcDeviceVO sfcDeviceVO);

    IPage<ProductVo> selectProductInfo(Page page,@Param("productDto")ProductDto productDto);
    IPage<ProductOperationVo> selectProductInfoByOperationBo(Page page,@Param("productDto")ProductDto productDto);

    IPage<ProductShopOrderVo> selectProductShopOrderInfo(Page page, @Param("productDto")ProductDto productDto);

    IPage<UserProduct> selectUserProduct(Page page, @Param("userProductDto")UserProductDto userProductDto);

    //查询某个用户的加工总数
    int selectAllQtyByUser(@Param("userProductDto")UserProductDto userProductDto);

    IPage<OperationCapacityVo> listOperationCapacity(Page page,@Param("operationCapacityDto") OperationCapacityDto operationCapacityDto);

    IPage<OperationCapacityDetailedVo> getOperationCapacityDetailed(Page page, @Param("operationCapacityDto") OperationCapacityDto operationCapacityDto);

    List<StaffAttendanceVo> listStaffAttendance(@Param("staffAttendanceDto") StaffAttendanceDto staffAttendanceDto);

    String getWorkShopDescByBo(@Param("bo") String bo);

    String getProductLineDescByBo(@Param("bo") String bo);

    String getTotalPeoplesByCondition(@Param("workShopBo") String workShopBo,@Param("productLineBo") String productLineBo,@Param("dTime") String dTime);

    List<NotAttendanceDetailed> getNotAttendanceDetailed(@Param("workShopBo") String workShopBo,@Param("productLineBo") String productLineBo,@Param("dTime") String dTime);

    /**
     * 查询某人当天是否出勤
     * @return
     */
    int currentDayIsAttendance(@Param("dTime") String dTime,@Param("userId") String userId);

    IPage<StaffDailyEnergyVo> getStaffDailyEnergyPageList(Page page,@Param("staffDailyEnergyVo") StaffDailyEnergyVo staffDailyEnergyVo);

    List<StaffDailyEnergyVo> staffDailyEnergyMachiningDetails(@Param("staffDailyEnergyVo") StaffDailyEnergyVo staffDailyEnergyVo);

    List<StaffDailyEnergyAndonDetailsVo> staffDailyEnergyAndonDetails(@Param("staffDailyEnergyAndonDetailsVo") StaffDailyEnergyAndonDetailsVo staffDailyEnergyAndonDetailsVo);

    IPage<PlanReachedVo> listPlanReached(Page page, @Param("planReachedDto") PlanReachedDto planReachedDto);

    String sumDoneQtyByItem(@Param("planReachedDto") PlanReachedDto planReachedDto);

    String sumStockQtyByItem(@Param("planReachedDto") PlanReachedDto planReachedDto);

    String sumMakingQtyByItem(@Param("planReachedDto") PlanReachedDto planReachedDto);

    List<PlanReachedOrderDetailsVo> planReachedOrderDetails(@Param("planReachedDto") PlanReachedDto planReachedDto);

    List<PlanReachedMakingDetailsVo> planReachedMakingDetails(@Param("planReachedDto") PlanReachedDto planReachedDto);

    String getSumStaffDailyEnergy(@Param("staffDailyEnergyVo") StaffDailyEnergyVo staffDailyEnergyVo);

    void updateUserReform(@Param("bo") String bo);
}
