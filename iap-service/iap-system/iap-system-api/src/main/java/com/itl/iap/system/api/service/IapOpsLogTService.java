package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapOpsLogTDto;
import com.itl.iap.system.api.entity.IapOpsLogT;

/**
 * 操作日志service
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
public interface IapOpsLogTService extends IService<IapOpsLogT>{

    /**
     * 分页查询
     * @param iapOpsLogDto
     * @return
     */
    IPage<IapOpsLogTDto> pageQuery(IapOpsLogTDto iapOpsLogDto);
}