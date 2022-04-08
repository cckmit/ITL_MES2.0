package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapEmployeeTDto;
import com.itl.iap.system.api.entity.IapEmployeeT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工管理service
 *
 * @author 马家伦
 * @date 2020-06-17
 * @since jdk1.8
 */
public interface IapEmployeeTService extends IService<IapEmployeeT> {

    /**
     * 通过条件模糊查询员工列表
     *
     * @param iapEmployeeDto 员工对象
     * @return IPage<IapEmployeeTDto>
     */
    IPage<IapEmployeeTDto> query(IapEmployeeTDto iapEmployeeDto);

    /**
     * 添加新员工
     *
     * @param iapEmployeeDto 员工对象
     * @return String
     */
    String add(IapEmployeeTDto iapEmployeeDto);

    /**
     * 更新员工信息
     *
     * @param iapEmployeeDto 员工对象
     * @return String
     */
    String update(IapEmployeeTDto iapEmployeeDto);

    /**
     * 通过员工id删除
     *
     * @param iapEmployeeDto 员工对象
     * @return Boolean
     */
    Boolean deleteById(IapEmployeeTDto iapEmployeeDto);

    /**
     * 通过传入的员工列表删除
     *
     * @param iapEmployeeDtoList 员工集合
     * @return Boolean
     */
    Boolean deleteByIapEmployeeDtoList(List<IapEmployeeTDto> iapEmployeeDtoList);
    /**
     * 修改员工关联账号
     *
     * @param iapEmployeeTDto 员工对象
     * @return Boolean
     */
     Boolean updateUserId(IapEmployeeTDto iapEmployeeDto);
}