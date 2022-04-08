package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.ShiftDetail;
import com.itl.mes.core.api.vo.ShiftDetailVo;

import java.util.List;

/**
 * <p>
 * 班次时段明细表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
public interface ShiftDetailService extends IService<ShiftDetail> {

    List<ShiftDetail> selectList();

    void save(String shiftBO, List<ShiftDetailVo> shiftDetailVos)throws CommonException;

    void delete(String shiftBO)throws CommonException;

    List<ShiftDetail> getShiftDetailVos(String shiftBO)throws CommonException;
}