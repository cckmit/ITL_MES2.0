package com.itl.iap.auth.service;

import com.itl.iap.auth.dto.AuthCodeDto;
import com.itl.iap.auth.entity.AuthCode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (IapAuthCodeT)表服务接口
 *
 * @author 汤俊
 * @date 2020-06-17 19:49:17
 * @since jdk1.8
 */
public interface IAuthCodeService extends IService<AuthCode> {

    /**
     * 分页查询
     *
     * @param authCodeDto 需要查询的条件
     * @return IPage<AuthCodeDto> 查询结果的分页对象
     */
    IPage<AuthCodeDto> pageQuery(AuthCodeDto authCodeDto);
}