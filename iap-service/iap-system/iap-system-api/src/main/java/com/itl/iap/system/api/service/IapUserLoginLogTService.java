package com.itl.iap.system.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapUserLoginLogTDto;
import com.itl.iap.system.api.entity.IapUserLoginLogT;

/**
 * 用户登录日志Service
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
public interface IapUserLoginLogTService extends IService<IapUserLoginLogT>{

    /**
     * 分页查询
     *
     * @param iapUserLoginLogDto 登录日志实例
     * @return IPage<IapUserLoginLogTDto>
     */
    IPage<IapUserLoginLogTDto> pageQuery(IapUserLoginLogTDto iapUserLoginLogDto);
}