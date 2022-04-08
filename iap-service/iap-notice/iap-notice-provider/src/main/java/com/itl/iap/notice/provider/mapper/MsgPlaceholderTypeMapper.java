package com.itl.iap.notice.provider.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.itl.iap.notice.api.dto.MsgPlaceholderTypeDto;
import com.itl.iap.notice.api.entity.MsgPlaceholderType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (MsgPlaceholderType)表数据库访问层
 *
 * @author liaochengdian
 * @date  2020-04-07
 * @since jdk1.8
 */
public interface MsgPlaceholderTypeMapper extends BaseMapper<MsgPlaceholderType> {

    IPage<MsgPlaceholderTypeDto> pageQuery(Page page, @Param("msgPlaceholderTypeDto") MsgPlaceholderTypeDto msgPlaceholderTypeDto);

    List<MsgPlaceholderType> selectByGroup();

    MsgPlaceholderType selectByName(@Param("typeName") String typeName);
}