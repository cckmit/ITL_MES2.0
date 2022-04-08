package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapEmployeeTDto;
import com.itl.iap.system.api.entity.IapEmployeeT;
import com.itl.iap.system.api.entity.IapPositionEmployeeT;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 员工管理mapper
 *
 * @author 马家伦
 * @date 2020-06-17
 * @since jdk1.8
 */
public interface IapSysEmployeeTMapper extends BaseMapper<IapEmployeeT> {

    /**
     * 查询员工列表
     *
     * @param page            分页
     * @param iapEmployeeTDto 员工集合
     * @return IPage<IapEmployeeTDto>
     */
    IPage<IapEmployeeTDto> queryList(Page page, @Param("iapEmployeeTDto") IapEmployeeTDto iapEmployeeTDto);

    /**
     * 插入岗位-员工中间表
     *
     * @param iapPositionEmployeeT 岗位员工中间表
     * @return Integer
     */
    Integer insertIntoIapSysPositionEmployeeT(@Param("iapPositionEmployeeT") IapPositionEmployeeT iapPositionEmployeeT);

    /**
     * 批量删除
     *
     * @param iapEmployeeTList 员工id集合
     * @return Integer
     */
    Integer deleteByList(@Param("iapEmployeeTList") List<IapEmployeeT> iapEmployeeTList);

    /**
     * 修改员工关联用户的userId
     *
     * @param iapEmployeeTDto 员工集合
     * @return Integer
     */
    Integer updateUserId(@Param("iapEmployeeTDto") IapEmployeeTDto iapEmployeeTDto);

    /**
     * 批量插入
     *
     * @param entityList 员工集合
     * @return boolean
     */
    boolean insertListFromExcel(@Param("list") Collection<IapEmployeeT> entityList);
}

