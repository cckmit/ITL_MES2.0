package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapChangeLogTDto;
import com.itl.iap.system.api.entity.IapChangeLogT;

/**
 * 操作变动日志(IapChangeLogT)表服务接口
 *
 * @author linjs
 * @since 2020-10-30 10:40:19
 */
public interface IapChangeLogTService extends IService<IapChangeLogT>{

    /**
     * 分页查询
     * @param iapChangeLogTDto 操作变动日志
     * @return
     */
    IPage<IapChangeLogTDto> pageQuery(IapChangeLogTDto iapChangeLogTDto);

    /**
     * 根据操作变动ID查询操作变动明细
     * @param iapChangeLogDto 接口管理对象
     * @return
     */
    IapChangeLogT queryChangeDetailById(IapChangeLogTDto iapChangeLogDto);
}