package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapChangeDetailLogTDto;
import com.itl.iap.system.api.entity.IapChangeDetailLogT;
import org.apache.ibatis.annotations.Param;

/**
 * 操作变动明细日志(IapChangeDetailLogT)表数据库访问层
 *
 * @author linjs
 * @since 2020-10-30 11:12:21
 */
public interface IapChangeDetailLogTMapper extends BaseMapper<IapChangeDetailLogT> {

    IPage<IapChangeDetailLogTDto> pageQuery(Page page, @Param("iapChangeDetailLogTDto") IapChangeDetailLogTDto iapChangeDetailLogTDto);

}