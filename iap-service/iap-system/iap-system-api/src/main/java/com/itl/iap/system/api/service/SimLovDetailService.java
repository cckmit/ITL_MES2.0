package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.system.api.dto.SimLovDetailDto;
import com.itl.iap.system.api.dto.SimLovDto;
import com.itl.iap.system.api.entity.SimLov;
import com.itl.iap.system.api.entity.SimLovDetail;

import java.util.List;

/**
 * @author EbenChan
 * @date 2020/11/21 18:08
 **/
public interface SimLovDetailService extends IService<SimLovDetail> {
    /**
     * 分页查询
     *
     * @param simLovDetailDto Lov实例
     * @return IPage<SimLovDto>
     */
    IPage<SimLovDetailDto> pageQuery(SimLovDetailDto simLovDetailDto);
    /**
     * 新增Lov
     *
     * @param simLovDetail Lov实例
     * @return Boolean
     */
    Boolean insertLovDetail(SimLovDetail simLovDetail) throws CommonException;

    /**
     * 修改用户
     *
     * @param simLovDetail Lov实例
     * @return Boolean
     */
    Boolean updateLovDetail(SimLovDetail simLovDetail) throws CommonException;

    /**
     * 根据用户id集合删除用户
     *
     * @param ids LovId列表
     * @return Boolean
     */
    Boolean removeByIds(List<String> ids);
}
