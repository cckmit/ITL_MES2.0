package com.itl.iap.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.auth.dto.AuthClientDto;
import com.itl.iap.auth.entity.AuthClient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * (IapAuthClientT)表数据库访问层
 *
 * @author 汤俊
 * @date  2020-06-17 19:49:17
 */
public interface IAuthClientMapper extends BaseMapper<AuthClient> {

    /**
     * 分页查询
     *
     * @param authClientDto 需要查询的条件
     * @return IPage<AuthClientDto> 封装好的分页对象
     */
    IPage<AuthClientDto> pageQuery(Page page, @Param("authClientDto") AuthClientDto authClientDto);
    IPage<AuthClientDto> pageQueryByState(Page page, @Param("authClientDto") AuthClientDto authClientDto);

}