package com.itl.iap.system.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapSysI18nTDto;
import com.itl.iap.system.api.entity.IapSysI18nT;
import org.apache.ibatis.annotations.Param;

/**
 * 国际化mapper
 *
 * @author 李骐光
 * @date 2020-06-29
 * @since jdk1.8
 */
public interface IapSysI18nTMapper extends BaseMapper<IapSysI18nT> {
    /**
     * 分页查询
     *
     * @param iapSysI18nTDto 国际化对象
     * @return IPage<IapSysI18nTDto>
     */
    IPage<IapSysI18nTDto> queryAll(Page page, @Param("iapSysI18nTDto") IapSysI18nTDto iapSysI18nTDto);

    /**
     * 批量修改键
     *
     * @param iapSysI18nTDto
     * @return
     */
    Boolean updateKey(@Param("iapSysI18nDto") IapSysI18nTDto iapSysI18nTDto);
}
