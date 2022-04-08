package com.itl.iap.notice.provider.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.itl.iap.notice.api.dto.MsgPlaceholderDto;
import com.itl.iap.notice.api.entity.MsgPlaceholder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (MsgPlaceholder)表数据库访问层
 *
 * @author liaochengdian
 * @date  2020-04-07
 * @since jdk1.8
 */
public interface MsgPlaceholderMapper extends BaseMapper<MsgPlaceholder> {

    /**
     * 分页查询
     * @param page 分页对象
     * @param msgPlaceholderDto
     * @return IPage<MsgPlaceholderDto>
     */
    IPage<MsgPlaceholderDto> pageQuery(Page page, @Param("msgPlaceholderDto") MsgPlaceholderDto msgPlaceholderDto);

//    List<MsgPlaceholder> selectMsgPlaceholderTree(@Param("type") Short type);

    /**
     * 查询所有
     * @return List<MsgPlaceholder>
     */
    List<MsgPlaceholder> selectAll();

    /**
     * 根据id获取占位符详情
     * @param id
     * @return MsgPlaceholder
     */
    MsgPlaceholder selectMsgPlaceholderById(@Param("id") String id);

    /**
     * 分页查询占位符
     * @param query 实例
     * @return IPage<MsgPlaceholder>
     */
    IPage<MsgPlaceholder> getMsgPlaceholderList(Page page,MsgPlaceholderDto query);
}