package com.itl.iap.system.provider.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.itl.iap.system.api.entity.IapSysAuthResourceT;
import com.itl.iap.system.api.entity.IapSysAuthT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户菜单mapper
 *
 * @author 谭强
 * @date 2020-06-22
 * @since jdk1.8
 */
public interface IapSysAuthResourceTMapper extends BaseMapper<IapSysAuthResourceT> {

    /**
     * 删除权限菜单
     * @param authId 权限id
     * @return Integer
     */
    Integer deleteByAuthId(@Param("authId")String authId);

    /**
     * 通过权限列表批量删除权限-菜单中间表
     * @param sysAuthList 权限列表
     * @return boolean
     */
    boolean deleteBatchByAuthIdList(@Param("sysAuthList") List<IapSysAuthT> sysAuthList);
}