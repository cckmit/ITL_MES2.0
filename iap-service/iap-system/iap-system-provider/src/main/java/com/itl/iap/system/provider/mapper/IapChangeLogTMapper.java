package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapChangeLogTDto;
import com.itl.iap.system.api.entity.IapChangeLogT;
import org.apache.ibatis.annotations.Param;

/**
 * 操作变动日志(IapChangeLogT)表数据库访问层
 *
 * @author linjs
 * @since 2020-10-30 10:48:36
 */
public interface IapChangeLogTMapper extends BaseMapper<IapChangeLogT> {

    IPage<IapChangeLogTDto> pageQuery(Page page, @Param("iapChangeLogTDto") IapChangeLogTDto iapChangeLogTDto);

}