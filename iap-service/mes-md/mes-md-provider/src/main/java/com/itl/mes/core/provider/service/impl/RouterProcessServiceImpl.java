package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.RouterProcess;
import com.itl.mes.core.api.service.RouterProcessService;
import com.itl.mes.core.api.service.RouterService;
import com.itl.mes.core.provider.mapper.RouterMapper;
import com.itl.mes.core.provider.mapper.RouterProcessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RouterProcessServiceImpl extends ServiceImpl<RouterProcessMapper, RouterProcess> implements RouterProcessService {
    @Autowired
    private RouterProcessMapper routerProcessMapper;

    @Override
    public RouterProcess selectByRouterBo(String routerBo) throws CommonException {
        return routerProcessMapper.selectByRouterBo(routerBo);
    }
}
