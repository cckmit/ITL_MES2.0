package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.job.admin.core.model.IapXxlJobRegistry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 表（xxl_job_registry）的操作
 * 执行器注册表，维护在线的执行器和调度中心机器地址信息
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Mapper
public interface IapXxlJobRegistryDao extends BaseMapper<IapXxlJobRegistry> {

    /**
     * 找到无效的XxlJobRegistry，并且返回它们的ID
     *
     * @param timeout 超时时间
     * @param nowTime 现在时间
     * @return List<Integer>
     */
    public List<Integer> findDead(@Param("timeout") int timeout,
                                  @Param("nowTime") Date nowTime);

    /**
     * 根据 XxlJobRegistry的ID删除
     *
     * @param ids XxlJobRegistry的ID列表
     * @return int
     */
    public int removeDead(@Param("ids") List<Integer> ids);

    /**
     * 根据timeout和nowTime查找
     *
     * @param timeout 超时时间
     * @param nowTime 现在时间
     * @return List<XxlJobRegistry>
     */
    public List<IapXxlJobRegistry> findAll(@Param("timeout") int timeout,
                                           @Param("nowTime") Date nowTime);

    /**
     * 更新
     *
     * @param registryGroup 注册分组
     * @param registryKey   注册键
     * @param registryValue 注册值
     * @param updateTime    更新时间
     * @return
     */
    public int registryUpdate(@Param("registryGroup") String registryGroup,
                              @Param("registryKey") String registryKey,
                              @Param("registryValue") String registryValue,
                              @Param("updateTime") Date updateTime);

    /**
     * 保存
     *
     * @param registryGroup 注册分组
     * @param registryKey   注册键
     * @param registryValue 注册值
     * @param updateTime    更新时间
     * @return
     */
    public int registrySave(@Param("registryGroup") String registryGroup,
                            @Param("registryKey") String registryKey,
                            @Param("registryValue") String registryValue,
                            @Param("updateTime") Date updateTime);

    /**
     * 删除
     *
     * @param registryGroup 注册分组
     * @param registryKey   注册键
     * @param registryValue 注册值
     * @return
     */
    public int registryDelete(@Param("registryGroup") String registryGroup,
                              @Param("registryKey") String registryKey,
                              @Param("registryValue") String registryValue);

}
