package com.itl.iap.system.provider.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.system.api.dto.IapSysAuthTDto;
import com.itl.iap.system.api.entity.IapSysAuthT;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.entity.IapSysRoleT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限表mapper
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
public interface IapSysAuthTMapper extends BaseMapper<IapSysAuthT> {

    /**
     * 分页查询
     *
     * @param iapSysAuthTDto 权限对象
     * @return 权限分页对象
     */
    IPage<IapSysAuthTDto> pageQuery(Page page, @Param("iapSysAuthTDto") IapSysAuthTDto iapSysAuthTDto);

    /**
     * 模糊查询权限
     *
     * @param iapSysAuthTDto 权限对象
     * @return 权限集合
     */
    List<IapSysAuthTDto> selectTree(@Param("iapSysAuthTDto")IapSysAuthTDto iapSysAuthTDto);
    List<IapSysAuthTDto> selectTreeByState(@Param("iapSysAuthTDto")IapSysAuthTDto iapSysAuthTDto);

    /**
     * 批量删除
     * @param ids id集合
     * @return
     */
    int deleteTreeByIds(String ids);

    /**
     * 批量删除权限表
     * @param sysAuthList 权限id集合
     * @return
     */
    boolean deleteList(@Param("sysAuthList") List<IapSysAuthT> sysAuthList);

}