package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.WorkStationDTO;
import com.itl.mes.core.api.entity.WorkStation;
import com.itl.mes.core.api.vo.WorkStationVo;

import java.util.List;


/**
 * <p>
 * 工步表 服务类
 * </p>
 *
 * @author pwy
 * @since 2021-3-11
 */
public interface WorkStationService extends IService<WorkStation> {

     void saveByWorkStationVo(WorkStationVo workStationVo) throws CommonException;
     IPage<WorkStation> query(WorkStationDTO workStationDTO);
}
