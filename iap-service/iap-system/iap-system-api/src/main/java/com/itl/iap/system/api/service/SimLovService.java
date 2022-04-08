package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.api.dto.SimLovDto;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.iap.system.api.entity.SimLov;

import java.util.List;

/**
 * @author EbenChan
 * @date 2020/11/21 18:08
 **/
public interface SimLovService extends IService<SimLov> {
    /**
     * 分页查询
     *
     * @param simLovDto Lov实例
     * @return IPage<SimLovDto>
     */
    IPage<SimLovDto> pageQuery(SimLovDto simLovDto);

    /**
     * 根据编码获取详情
     * @param code
     * @return SimLovDto
     */
    SimLovDto getByCode(String code);
    /**
     * 新增Lov
     *
     * @param simLov Lov实例
     * @return Boolean
     */
    Boolean insertLov(SimLov simLov) throws CommonException;

    /**
     * 修改用户
     *
     * @param simLov Lov实例
     * @return Boolean
     */
    Boolean updateLov(SimLov simLov) throws CommonException;

    /**
     * 根据用户id集合删除用户
     *
     * @param ids LovId列表
     * @return Boolean
     */
    Boolean removeByIds(List<String> ids);
}
