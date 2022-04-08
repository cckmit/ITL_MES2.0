package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.Shift;
import com.itl.mes.core.api.vo.ShiftVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 班次信息主表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
public interface ShiftService extends IService<Shift> {

    List<Shift> selectList();

    Shift selectShift(String shift)throws CommonException;

    ShiftVo getShiftVoByShift(String shift)throws CommonException;

    void save(ShiftVo shiftVo)throws CommonException;

    void delete(String shift, Date modifyDate)throws CommonException;

}