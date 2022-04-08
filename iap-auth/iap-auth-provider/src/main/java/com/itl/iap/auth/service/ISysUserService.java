package com.itl.iap.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.auth.dto.SysUserDto;
import com.itl.iap.auth.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * (IapSysUserT)表服务接口
 *
 * @author iAP
 * @since 2020-06-17 19:49:20
 */
public interface ISysUserService extends IService<SysUser> {

    IPage<SysUserDto> pageQuery(SysUserDto iapSysUserDto);

    List<Map<String ,String>> getAllMenuByType(String username);
}