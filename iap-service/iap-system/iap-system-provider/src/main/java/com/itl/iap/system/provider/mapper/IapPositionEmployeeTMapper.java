package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.system.api.entity.IapEmployeeT;
import com.itl.iap.system.api.entity.IapPositionEmployeeT;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mjl 岗位-员工 中间表
 * @since 2020-06-29
 */
public interface IapPositionEmployeeTMapper extends BaseMapper<IapPositionEmployeeT> {
    /**
     * 通过传入的员工列表id删除
     * @param iapEmployeeTList
     * @return
     */
    Integer deleteByIapEmployeeTList(@Param("iapEmployeeTList") List<IapEmployeeT> iapEmployeeTList);
}
