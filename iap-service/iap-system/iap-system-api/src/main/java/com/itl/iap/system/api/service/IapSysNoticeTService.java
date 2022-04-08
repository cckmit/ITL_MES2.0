package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapSysNoticeTDto;
import com.itl.iap.system.api.entity.IapSysNoticeT;

import java.util.List;

/**
 * 公告service
 *
 * @author 李骐光
 * @date 2020-06-22
 * @since jdk1.8
 */
public interface IapSysNoticeTService extends IService<IapSysNoticeT> {
    /**
     * 查询公告
     *
     * @param noticeDto 公告对象
     * @return IPage<IapSysNoticeTDto>
     */
    IPage<IapSysNoticeTDto> query(IapSysNoticeTDto noticeDto);

    /**
     * 查询平台公告
     *
     * @param noticeDto 公告对象
     * @return IPage<IapSysNoticeTDto>
     */
    IPage<IapSysNoticeTDto> getPlatform(IapSysNoticeTDto noticeDto);

    /**
     * 查询企业公告
     *
     * @param noticeDto 公告对象
     * @return IPage<IapSysNoticeTDto>
     */
    IPage<IapSysNoticeTDto> getEnterprise(IapSysNoticeTDto noticeDto);

    /**
     * 查询公告详情
     *
     * @param noticeDto 公告对象
     * @return IapSysNoticeT
     */
    IapSysNoticeT getNoticeById(IapSysNoticeTDto noticeDto);

    /**
     * 新建公告
     *
     * @param noticeDto 公告对象
     * @return String
     */
    String addNotice(IapSysNoticeTDto noticeDto);

    /**
     * 修改公告
     *
     * @param noticeDto 公告对象
     * @return String
     */
    String updateNoticeDto(IapSysNoticeTDto noticeDto);

    /**
     * 根据id删除公告
     *
     * @param sysNoticeDtoList 公告id集合
     * @return Boolean
     */
    Boolean deleteNoticeById(List<IapSysNoticeTDto> sysNoticeDtoList);

    /**
     * 发布
     *
     * @param id 公告id
     * @return String
     */
    String publishById(String id);

}
