package com.itl.iap.system.provider.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapOpsLogTDto;
import com.itl.iap.system.api.entity.IapOpsLogT;
import org.apache.ibatis.annotations.Param;

/**
 * (IapOpsLogT)表数据库访问层
 *
 * @author iAP
 * @since 2020-06-29 14:37:50
 */
public interface IapOpsLogTMapper extends BaseMapper<IapOpsLogT> {

    IPage<IapOpsLogTDto> pageQuery(Page page, @Param("iapOpsLogTDto") IapOpsLogTDto iapOpsLogTDto);

}