package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapSysI18nTDto;
import com.itl.iap.system.api.entity.IapSysI18nT;
import com.itl.iap.system.provider.mapper.IapSysI18nTMapper;
import com.itl.iap.system.api.service.IapSysI18nTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 国际化实现类
 *
 * @author 李骐光
 * @date 2020-06-29
 * @since jdk1.8
 */
@Service
public class IapSysI18nTServiceImpl extends ServiceImpl<IapSysI18nTMapper, IapSysI18nT> implements IapSysI18nTService {
    @Autowired
    private IapSysI18nTMapper iapSysI18nMapper;

    /**
     * 分页查询
     *
     * @param iapSysI18nDto 国际化对象
     * @return IPage<IapSysI18nTDto>
     */
    @Override
    public IPage<IapSysI18nTDto> queryAll(IapSysI18nTDto iapSysI18nDto) {
        if (iapSysI18nDto.getPage() == null) {
            iapSysI18nDto.setPage(new Page(0, 10));
        }
        return iapSysI18nMapper.queryAll(iapSysI18nDto.getPage(), iapSysI18nDto);
    }

    /**
     * 分页查询
     *
     * @param iapSysI18nTDto 国际化对象
     * @return List<IapSysI18nT>
     */
    @Override
    public List<IapSysI18nT> queryById(IapSysI18nTDto iapSysI18nTDto) {
        return iapSysI18nMapper.selectList(new QueryWrapper<IapSysI18nT>().eq("language_type", iapSysI18nTDto.getLanguageType()));
    }

    /**
     * 新增数据
     *
     * @param iapSysI18nDto 国际化对象
     * @return boolean
     */
    @Override
    public boolean add(IapSysI18nTDto iapSysI18nDto) {
        if (iapSysI18nDto.getId() == null) {
            iapSysI18nDto.setId(UUID.uuid32());
        }
        IapSysI18nT iapSysI18nT = DtoUtils.convertObj(iapSysI18nDto, IapSysI18nT.class);
        return this.save(iapSysI18nT);
    }

    /**
     * 修改数据
     *
     * @param iapSysI18nDto 国际化对象
     * @return boolean
     */
    @Override
    public boolean update(IapSysI18nTDto iapSysI18nDto) {
        iapSysI18nDto.setLastUpdateDate(new Date());
        IapSysI18nT iapSysI18nT1 = DtoUtils.convertObj(iapSysI18nDto, IapSysI18nT.class);
        return this.updateById(iapSysI18nT1);
    }

    /**
     * 批量删除
     *
     * @param sysI18nDtoList 国际化id集合
     * @return boolean
     */
    @Override
    public boolean deleteBatch(List<IapSysI18nTDto> sysI18nDtoList) {
        if (!sysI18nDtoList.isEmpty()) {
            List<String> sysI18nDtoIds = new ArrayList<>();
            for (IapSysI18nTDto sysI18nDto : sysI18nDtoList) {
                sysI18nDtoIds.add(sysI18nDto.getId());
            }
            this.removeByIds(sysI18nDtoIds);
            return true;
        }
        return false;
    }

    /**
     * 批量修改键
     *
     * @param iapSysI18nTDto
     * @return
     */
    @Override
    public Boolean updateKey(IapSysI18nTDto iapSysI18nTDto) {
        return iapSysI18nMapper.updateKey(iapSysI18nTDto);
    }
}
