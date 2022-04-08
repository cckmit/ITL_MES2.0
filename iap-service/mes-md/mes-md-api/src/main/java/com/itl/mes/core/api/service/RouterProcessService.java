package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.api.entity.RouterProcess;

/**
 * <p>
 * 工艺路线路线图
 * </p>
 *
 * @author linjl
 * @since 2020-01-28
 */
public interface RouterProcessService extends IService<RouterProcess> {
    RouterProcess selectByRouterBo(String routerBo) throws CommonException;
}