package com.itl.iap.system.provider.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapUserLoginLogTDto;
import com.itl.iap.system.api.entity.IapUserLoginLogT;
import org.apache.ibatis.annotations.Param;

/**
 * 用户登录日志mapper
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
public interface IapUserLoginLogTMapper extends BaseMapper<IapUserLoginLogT> {

    /**
     * 分页查询
     *
     * @param iapUserLoginLogTDto 登录日志实例
     * @return IPage<IapUserLoginLogTDto>
     */
    IPage<IapUserLoginLogTDto> pageQuery(Page page, @Param("iapUserLoginLogTDto") IapUserLoginLogTDto iapUserLoginLogTDto);

}