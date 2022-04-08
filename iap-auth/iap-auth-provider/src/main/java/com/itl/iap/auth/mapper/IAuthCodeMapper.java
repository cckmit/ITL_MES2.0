package com.itl.iap.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.auth.dto.AuthCodeDto;
import com.itl.iap.auth.entity.AuthCode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * (IapAuthCodeT)表数据库访问层
 *
 * @author 汤俊
 * @date  2020-06-17 19:49:17
 * @since jdk1.8
 */
public interface IAuthCodeMapper extends BaseMapper<AuthCode> {

    /**
     * 分页查询
     *
     * @param authCodeDto 需要查询的条件
     * @return IPage<AuthCodeDto> 封装好的分页对象
     */
    IPage<AuthCodeDto> pageQuery(Page page, @Param("authCodeDto") AuthCodeDto authCodeDto);

}