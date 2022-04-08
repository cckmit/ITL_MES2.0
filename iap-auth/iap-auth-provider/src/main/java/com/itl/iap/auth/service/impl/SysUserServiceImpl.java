package com.itl.iap.auth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.auth.dto.SysUserDto;
import com.itl.iap.auth.entity.SysUser;
import com.itl.iap.auth.service.ISysUserService;
import org.springframework.stereotype.Service;
import com.itl.iap.auth.mapper.ISysUserMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * (IapSysUserT)表服务实现类
 *
 * @author 汤俊
 * @date 2020-06-17 19:49:20
 * @since jdk1.8
 */
@Service("iapSysUserTService")
public class SysUserServiceImpl extends ServiceImpl<ISysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private ISysUserMapper iSysUserMapper;

    /**
     * 分页查询
     *
     * @param iapSysUserDto 需要查询的条件
     * @return IPage<SysUserDto> 查询结果的分页对象
     */
    @Override
    public IPage<SysUserDto> pageQuery(SysUserDto iapSysUserDto) {
        if (null == iapSysUserDto.getPage()) {
            iapSysUserDto.setPage(new Page(0, 10));
        }
        return iSysUserMapper.pageQuery(iapSysUserDto.getPage(), iapSysUserDto);
    }

    @Override
    public List<Map<String, String>> getAllMenuByType(String username) {
        return iSysUserMapper.getAllMenuByType(username);
    }


}