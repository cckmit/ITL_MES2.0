package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.StationTypeItem;
import com.itl.mes.core.api.vo.StationTypeItemVo;

import java.util.List;


/**
 * <p>
 * 工位类型明细表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-29
 */
public interface StationTypeItemService extends IService<StationTypeItem> {

    List<StationTypeItem> selectList();

    void save(String stationTypeBO, List<StationTypeItemVo> assignedStationTypeItemVos)throws CommonException;

    void delete(String stationTypeBO)throws CommonException;
}