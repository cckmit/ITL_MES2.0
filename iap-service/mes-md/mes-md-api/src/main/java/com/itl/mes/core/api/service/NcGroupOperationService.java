package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.NcGroupOperation;
import com.itl.mes.core.api.vo.NcGroupOperationVo;

import java.util.List;

/**
 * <p>
 * 不合格组工序表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-15
 */
public interface NcGroupOperationService extends IService<NcGroupOperation> {

    List<NcGroupOperation> selectList();

    void save(String ncGroupBO, List<NcGroupOperationVo> ncGroupOperationVos)throws CommonException;

    void delete(String ncGroupBO)throws CommonException;
}