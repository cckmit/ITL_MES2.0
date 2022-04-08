package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapSysApiTDto;
import com.itl.iap.system.api.entity.IapSysApiT;
import org.apache.ibatis.annotations.Param;

/**
 * 接口管理mapper
 *
 * @author 马家伦
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysApiTMapper extends BaseMapper<IapSysApiT> {

    /**
     * 查询接口信息
     *
     * @param iapSysApiT 接口管理对象
     * @return 接口管理分页对象
     */
    IPage<IapSysApiTDto> queryList(Page page, @Param("iapSysApiT") IapSysApiT iapSysApiT);

    /**
     * 通过微服务名称删除接口
     * @param iapSysApiT 接口管理对象
     * @return 删除数量
     */
    Integer deleteByIapSysApiT(@Param("iapSysApiT") IapSysApiT iapSysApiT);
}
