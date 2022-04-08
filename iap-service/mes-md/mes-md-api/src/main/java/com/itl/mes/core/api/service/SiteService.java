package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.Site;
import com.itl.mes.core.api.vo.SiteVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author space
 * @since 2019-05-20
 */
public interface SiteService extends IService<Site> {

    List<Site> selectList();

    /**
     * 通过工厂查询工厂数据
     *
     * @param site 工厂
     * @return Site
     */
    Site getSiteBySite(String site);

    /**
     * 通过工厂查询工厂数据，不存在则报错
     *
     * @param site 工厂
     * @return Site
     * @throws CommonException 异常
     */
    Site getExistSiteBySite(String site) throws CommonException;

    /**
     * 保存工厂数据
     * @param siteVo siteVo
     * @throws CommonException 异常
     */
    void saveSite(SiteVo siteVo) throws CommonException;
}