package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapSysNoticeTDto;
import com.itl.iap.system.api.entity.IapSysNoticeT;
import com.itl.iap.system.provider.mapper.IapSysNoticeTMapper;
import com.itl.iap.system.api.service.IapSysNoticeTService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 公告实现类
 *
 * @author 李骐光
 * @date 2020-06-22
 * @since jdk1.8
 */
@Service
public class IapSysNoticeTServiceImpl extends ServiceImpl<IapSysNoticeTMapper, IapSysNoticeT> implements IapSysNoticeTService {
    @Autowired
    private IapSysNoticeTMapper noticeMapper;

    /**
     * 查询公告
     * @param noticeDto 公告对象
     * @return IPage<IapSysNoticeTDto>
     */
    private IPage<IapSysNoticeTDto> getNotic(IapSysNoticeTDto noticeDto) {
        if (noticeDto.getPage() == null) {
            Page page = new Page(0, 10);
            noticeDto.setPage(page);
        }
        return noticeMapper.query(noticeDto.getPage(), noticeDto);
    }

    /**
     * 查询公告
     *
     * @param noticeDto 公告对象
     * @return IPage<IapSysNoticeTDto>
     */
    @Override
    public IPage<IapSysNoticeTDto> query(IapSysNoticeTDto noticeDto) {
        return getNotic(noticeDto);
    }

    /**
     * 查询平台公告
     *
     * @param noticeDto 公告对象
     * @return IPage<IapSysNoticeTDto>
     */
    @Override
    public IPage<IapSysNoticeTDto> getPlatform(IapSysNoticeTDto noticeDto) {
        return getNotic(noticeDto.setPublisherCategory((short) 1));
    }

    /**
     * 查询企业公告
     *
     * @param noticeDto 公告对象
     * @return IPage<IapSysNoticeTDto>
     */
    @Override
    public IPage<IapSysNoticeTDto> getEnterprise(IapSysNoticeTDto noticeDto) {
        return getNotic(noticeDto.setPublisherCategory((short) 2));
    }

    /**
     * 查询公告详情
     *
     * @param noticeDto 公告对象
     * @return IapSysNoticeT
     */
    @Override
    public IapSysNoticeT getNoticeById(IapSysNoticeTDto noticeDto) {
        IapSysNoticeT notice = new IapSysNoticeT();
        BeanUtils.copyProperties(noticeDto, notice);
        return noticeMapper.selectById(notice.getId());
    }

    /**
     * 新建公告
     *
     * @param noticeDto 公告对象
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addNotice(IapSysNoticeTDto noticeDto) {
        IapSysNoticeT notice = new IapSysNoticeT();
        noticeDto.setId(UUID.uuid32());
        BeanUtils.copyProperties(noticeDto, notice);
        noticeMapper.insert(notice);
        return notice.getId();
    }

    /**
     * 修改公告
     *
     * @param noticeDto 公告对象
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateNoticeDto(IapSysNoticeTDto noticeDto) {
        IapSysNoticeT notice = new IapSysNoticeT();
        noticeDto.setLastUpdateDate(new Date());
        BeanUtils.copyProperties(noticeDto, notice);
        noticeMapper.updateById(notice);
        return notice.getId();
    }

    /**
     * 根据id删除公告
     *
     * @param sysNoticeDtoList 公告id集合
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteNoticeById(List<IapSysNoticeTDto> sysNoticeDtoList) {
        if (!sysNoticeDtoList.isEmpty()){
            for (IapSysNoticeTDto iapSysNoticeDto : sysNoticeDtoList) {
                IapSysNoticeT notice = noticeMapper.selectById(iapSysNoticeDto.getId());
                if (notice != null && notice.getId() != null && notice.getId() != "") {
                    notice.setStatus((short) 9);
                    noticeMapper.updateById(notice);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 发布
     *
     * @param id 公告id
     * @return String
     */
    @Override
    public String publishById(String id) {
        IapSysNoticeT notice = noticeMapper.selectById(id);
        if (StringUtils.isBlank(notice.getId())) {
            notice.setStatus((short) 5);
            noticeMapper.updateById(notice);
            return id;
        }
        return null;
    }

}