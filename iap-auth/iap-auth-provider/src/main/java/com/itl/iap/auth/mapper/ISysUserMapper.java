package com.itl.iap.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.auth.dto.SysUserDto;
import com.itl.iap.auth.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (IapSysUserT)表数据库访问层
 *
 * @author 汤俊
 * @date  2020-06-17 19:49:19
 * @since jdk1.8
 */
public interface ISysUserMapper extends BaseMapper<SysUser> {

    /**
     * 分页查询
     *
     * @param sysUserDto 需要查询的条件
     * @return IPage<AuthCodeDto> 封装好的分页对象
     */
    IPage<SysUserDto> pageQuery(Page page, @Param("sysUserDto") SysUserDto sysUserDto);


     List<Map<String, String>> getAllMenuByType(@Param("userName") String userName);

}