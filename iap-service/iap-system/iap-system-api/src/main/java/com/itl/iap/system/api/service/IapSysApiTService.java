package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapSysApiTDto;
import com.itl.iap.system.api.entity.IapSysApiT;

import java.util.List;

/**
 * 接口管理service
 *
 * @author 马家伦
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysApiTService extends IService<IapSysApiT> {

    /**
     * 通过微服务名称删除url
     * @param iapSysApiDto 接口管理对象
     * @return 删除数量
     */
    Integer delete(IapSysApiTDto iapSysApiDto);

    /**
     * 查询接口信息
     * @param iapSysApiDto 接口管理对象
     * @return 接口管理分页对象
     */
    IPage<IapSysApiTDto> queryList(IapSysApiTDto iapSysApiDto);

    /**
     * 通过id查询接口信息
     * @param iapSysApiDto 接口管理对象
     * @return 接口管理对象
     */
    IapSysApiTDto selectById(IapSysApiTDto iapSysApiDto);

    /**
     * 通过id列表批量删除接口
     * @param iapSysApiDtoList 接口管理对象
     * @return 删除数量
     */
    Integer deleteByIds(List<IapSysApiTDto> iapSysApiDtoList);
}
