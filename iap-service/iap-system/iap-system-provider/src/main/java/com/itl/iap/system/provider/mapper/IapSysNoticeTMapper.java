package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapSysNoticeTDto;
import com.itl.iap.system.api.entity.IapSysNoticeT;
import org.apache.ibatis.annotations.Param;

/**
 * 公告mapper
 *
 * @author 李骐光
 * @date 2020-06-22
 * @since jdk1.8
 */
public interface IapSysNoticeTMapper extends BaseMapper<IapSysNoticeT> {
    /**
     * 查询公告
     *
     * @param page 分页
     * @param noticeDto 公告对象
     * @return IPage<IapSysNoticeTDto>
     */
    IPage<IapSysNoticeTDto> query(Page page, @Param("noticeDto") IapSysNoticeTDto noticeDto);
}
