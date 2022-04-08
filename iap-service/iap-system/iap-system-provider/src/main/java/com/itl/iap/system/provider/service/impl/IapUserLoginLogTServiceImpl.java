package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.system.api.dto.IapUserLoginLogTDto;
import com.itl.iap.system.api.entity.IapUserLoginLogT;
import com.itl.iap.system.provider.mapper.IapUserLoginLogTMapper;
import com.itl.iap.system.api.service.IapUserLoginLogTService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * 用户登录日志Service
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Service
public class IapUserLoginLogTServiceImpl extends ServiceImpl<IapUserLoginLogTMapper, IapUserLoginLogT> implements IapUserLoginLogTService {

    @Resource
    private IapUserLoginLogTMapper iapUserLoginLogMapper;
    @Resource
    private UserUtil userUtil;

    /**
     * 分页查询
     *
     * @param iapUserLoginLogDto 登录日志实例
     * @return IPage<IapUserLoginLogTDto>
     */
    @Override
    public IPage<IapUserLoginLogTDto> pageQuery(IapUserLoginLogTDto iapUserLoginLogDto) {
        if (null == iapUserLoginLogDto.getPage()) {
            iapUserLoginLogDto.setPage(new Page(0, 10));
        }
        if (iapUserLoginLogDto.getUserId() == null) {
            iapUserLoginLogDto.setUserId(userUtil.getUser().getUserName());
        }
        return iapUserLoginLogMapper.pageQuery(iapUserLoginLogDto.getPage(), iapUserLoginLogDto);
    }

}
