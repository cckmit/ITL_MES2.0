package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.NcDispRouter;
import com.itl.mes.core.api.vo.NcDispRouterVo;

import java.util.List;

/**
 * <p>
 * 不合格代码处理工艺路线表 服务类
 * </p>
 *
 * @author space
 * @since 2019-07-08
 */
public interface NcDispRouterService extends IService<NcDispRouter> {

    List<NcDispRouter> selectList();

    void save(String ncCodeBO, List<NcDispRouterVo> assignedNcDispRouterVos)throws CommonException;

    void delete(String ncCodeBO)throws CommonException;
}