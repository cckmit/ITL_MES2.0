package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapSysI18nTDto;
import com.itl.iap.system.api.entity.IapSysI18nT;

import java.util.List;

/**
 * 国际化service
 *
 * @author 李骐光
 * @date 2020-06-29
 * @since jdk1.8
 */
public interface IapSysI18nTService extends IService<IapSysI18nT> {

    /**
     * 分页查询
     *
     * @param iapSysI18nDto 国际化对象
     * @return IPage<IapSysI18nTDto>
     */
    IPage<IapSysI18nTDto> queryAll(IapSysI18nTDto iapSysI18nDto);
    /**
     * 分页查询
     *
     * @param iapSysI18nDto 国际化对象
     * @return List<IapSysI18nT>
     */
    List<IapSysI18nT> queryById(IapSysI18nTDto iapSysI18nDto);

    /**
     * 新增数据
     *
     * @param iapSysI18nDto 国际化对象
     * @return boolean
     */
    boolean add(IapSysI18nTDto iapSysI18nDto);

    /**
     * 修改数据
     *
     * @param iapSysI18nDto 国际化对象
     * @return boolean
     */
    boolean update(IapSysI18nTDto iapSysI18nDto);

    /**
     * 批量删除
     *
     * @param sysI18nDtoList 国际化id集合
     * @return boolean
     */
    boolean deleteBatch(List<IapSysI18nTDto> sysI18nDtoList);

    /**
     * 批量修改键
     * @param iapSysI18nTDto
     * @return
     */
    Boolean updateKey(IapSysI18nTDto iapSysI18nTDto);
}
