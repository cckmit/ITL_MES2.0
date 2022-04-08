package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.vo.BomComponnetVo;
import com.itl.mes.me.api.dto.ItemWithTemplateDto;
import com.itl.mes.me.api.dto.MeSfcAssyAssyDto;
import com.itl.mes.me.api.entity.MeSfcAssy;

import java.util.List;


/**
 * Sfc装配表
 *
 * @author renren
 * @date 2021-01-25 14:43:36
 */
public interface MeSfcAssyService extends IService<MeSfcAssy> {

    /**
     * 根据成品sn和成品物料编号等获取装配清单
     * @param assyDto
     * @return
     * @throws CommonException
     */
    List<BomComponnetVo> getAssyList(MeSfcAssyAssyDto assyDto) throws CommonException;

    /**
     * 根据成品sn和组件sn获取组件物料编码
     * @param sn
     * @param componentSn
     * @return
     * @throws CommonException
     */
    String assyComponentBySn(String sn, String componentSn) throws CommonException;

    /**
     * 根据station获取模板
     * @param station
     * @return
     */
    List<ItemWithTemplateDto> getTemplates(String station);
}

