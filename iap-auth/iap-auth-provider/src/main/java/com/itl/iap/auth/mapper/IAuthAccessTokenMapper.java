package com.itl.iap.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.auth.dto.AuthAccessTokenDto;
import com.itl.iap.auth.entity.AuthAccessToken;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * (IapAuthAccessTokenT)表数据库访问层
 *
 * @author 汤俊
 * @date 2020-06-17 19:49:13
 * @since jdk1.8
 */
public interface IAuthAccessTokenMapper extends BaseMapper<AuthAccessToken> {

    /**
     * 分页查询
     *
     * @param authAccessTokenDto 需要查询的条件
     * @return IPage<AuthAccessTokenDto> 封装好的分页对象
     */
    IPage<AuthAccessTokenDto> pageQuery(Page page, @Param("authAccessTokenDto") AuthAccessTokenDto authAccessTokenDto);

}