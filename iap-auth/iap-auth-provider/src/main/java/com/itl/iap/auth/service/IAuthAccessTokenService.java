package com.itl.iap.auth.service;

import com.itl.iap.auth.dto.AuthAccessTokenDto;
import com.itl.iap.auth.entity.AuthAccessToken;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (IapAuthAccessTokenT)表服务接口
 *
 * @author 汤俊
 * @date 2020-06-17 19:49:14
 * @since jdk1.8
 */
public interface IAuthAccessTokenService extends IService<AuthAccessToken> {

    /**
     * 分页查询
     *
     * @param authAccessTokenDto 需要查询的条件
     * @return IPage<AuthAccessTokenDto> 查询结果的分页对象
     */
    IPage<AuthAccessTokenDto> pageQuery(AuthAccessTokenDto authAccessTokenDto);
}