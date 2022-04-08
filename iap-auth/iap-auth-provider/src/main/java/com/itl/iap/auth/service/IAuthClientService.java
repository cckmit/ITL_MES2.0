package com.itl.iap.auth.service;

import com.itl.iap.auth.dto.AuthClientDto;
import com.itl.iap.auth.entity.AuthClient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * (IapAuthClientT)表服务接口
 *
 * @author 汤俊
 * @date 2020-06-17 19:49:17
 * @since jdk1.8
 */
public interface IAuthClientService extends IService<AuthClient> {

    /**
     * 分页查询
     *
     * @param authClientDto 需要查询的条件
     * @return IPage<AuthClientDto> 查询结果的分页对象
     */
    IPage<AuthClientDto> pageQuery(AuthClientDto authClientDto);
    IPage<AuthClientDto> pageQueryByState(AuthClientDto authClientDto);
}