package com.itl.iap.auth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.auth.dto.AuthClientDto;
import com.itl.iap.auth.entity.AuthClient;
import com.itl.iap.auth.mapper.IAuthClientMapper;
import com.itl.iap.auth.service.IAuthClientService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;


/**
 * (IapAuthClientT)表服务实现类
 *
 * @author 汤俊
 * @date 2020-06-17 19:49:17
 * @since jdk1.8
 */
@Service("iapAuthClientTService")
public class AuthClientServiceImpl extends ServiceImpl<IAuthClientMapper, AuthClient> implements IAuthClientService {

    @Resource
    private IAuthClientMapper iAuthClientMapper;

    /**
     * 分页查询
     *
     * @param authClientDto 需要查询的条件
     * @return IPage<AuthClientDto> 查询结果的分页对象
     */
    @Override
    public IPage<AuthClientDto> pageQuery(AuthClientDto authClientDto) {
        if (null == authClientDto.getPage()) {
            authClientDto.setPage(new Page(0, 10));
        }
        return iAuthClientMapper.pageQuery(authClientDto.getPage(), authClientDto);
    }
    @Override
    public IPage<AuthClientDto> pageQueryByState(AuthClientDto authClientDto) {
        if (null == authClientDto.getPage()) {
            authClientDto.setPage(new Page(0, 10));
        }
        return iAuthClientMapper.pageQueryByState(authClientDto.getPage(), authClientDto);
    }

}