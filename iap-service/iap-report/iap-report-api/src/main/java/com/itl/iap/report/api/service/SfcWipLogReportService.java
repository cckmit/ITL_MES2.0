package com.itl.iap.report.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.report.api.dto.*;
import com.itl.iap.report.api.entity.SfcWipLog;
import com.itl.iap.report.api.vo.*;
import com.itl.mes.core.api.vo.SfcDeviceVO;

import java.text.ParseException;
import java.util.List;

public interface SfcWipLogReportService extends IService<SfcWipLog> {
    IPage<SfcWipLog> queryList(SfcWipLogReportDto sfcWipLogReportDto);

    /**
     * 查询进站信息
     * @param sfcDeviceVO
     * @return
     */
    IPage<SfcDeviceVO> queryPullIn(SfcDeviceVO sfcDeviceVO);

    IPage<ProductVo> queryProductInfo(ProductDto productDto);

    IPage<ProductOperationVo> selectProductInfoByOperationBo(ProductDto productDto);

    IPage<ProductShopOrderVo> selectProductShopOrderInfo(ProductDto productDto);

    UserProductVo selectUserProduct(UserProductDto userProductDto);

    IPage<OperationCapacityVo> listOperationCapacity(OperationCapacityDto operationCapacityDto);

    IPage<StaffAttendanceVo> listStaffAttendance(StaffAttendanceDto staffAttendanceDto) throws ParseException;

    IPage<PlanReachedVo> listPlanReached(PlanReachedDto planReachedDto);

    List<PlanReachedOrderDetailsVo> planReachedOrderDetails(PlanReachedDto planReachedDto);
}
