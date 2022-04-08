package com.itl.iap.auth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.auth.dto.AuthAccessTokenDto;
import com.itl.iap.auth.entity.AuthAccessToken;
import com.itl.iap.auth.mapper.IAuthAccessTokenMapper;
import com.itl.iap.auth.service.IAuthAccessTokenService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;


/**
 * (IapAuthAccessTokenT)表服务实现类
 *
 * @author 汤俊
 * @date 2020-06-17 19:49:14
 * @since jdk1.8
 */
@Service("authAccessTokenService")
public class AuthAccessTokenServiceImpl extends ServiceImpl<IAuthAccessTokenMapper, AuthAccessToken> implements IAuthAccessTokenService {

    @Resource
    private IAuthAccessTokenMapper iAuthAccessTokenMapper;

    /**
     * 分页查询
     *
     * @param authAccessTokenDto 需要查询的条件
     * @return IPage<AuthAccessTokenDto> 封装好的分页对象
     */
    @Override
    public IPage<AuthAccessTokenDto> pageQuery(AuthAccessTokenDto authAccessTokenDto) {
        if (null == authAccessTokenDto.getPage()) {
            authAccessTokenDto.setPage(new Page(0, 10));
        }
        return iAuthAccessTokenMapper.pageQuery(authAccessTokenDto.getPage(), authAccessTokenDto);
    }

}