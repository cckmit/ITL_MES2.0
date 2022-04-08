package com.itl.iap.notice.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.notice.api.dto.MsgTypeDto;
import com.itl.iap.notice.api.entity.MsgType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息类型表(MsgType)表数据库访问层
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
public interface MsgTypeMapper extends BaseMapper<MsgType> {
    /**
     * 查询消息类型树
     *
     * @return
     */
    List<MsgType> selectMsgTypeTree(@Param("id") String id);

    List<MsgType> selectDynamicMsgTypes(MsgTypeDto msgTypeDto);
//    IPage<MsgTypeDto> pageQuery(Page page, @Param("msgTypeDto") MsgTypeDto msgTypeDto);

}