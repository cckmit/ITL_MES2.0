package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.job.admin.core.model.IapXxlJobGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表（xxl_job_group）的操作
 * 执行器信息表: 维护任务执行器信息
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Mapper
public interface IapXxlJobGroupDao extends BaseMapper<IapXxlJobGroup> {

    /**
     * 查询所有 XxlJobGroup
     *
     * @return List<XxlJobGroup>
     */
    public List<IapXxlJobGroup> findAll();

    /**
     * 通过 addressType 查询符合条件的 XxlJobGroup
     *
     * @param addressType
     * @return List<XxlJobGroup>
     */
    public List<IapXxlJobGroup> findByAddressType(@Param("addressType") int addressType);

    /**
     * 保存 xxlJobGroup
     *
     * @param iapXxlJobGroup
     * @return int
     */
    public int save(IapXxlJobGroup iapXxlJobGroup);

    /**
     * 更新 xxlJobGroup
     *
     * @param iapXxlJobGroup
     * @return int
     */
    public int update(IapXxlJobGroup iapXxlJobGroup);

    /**
     * 根据xxlJobGroup的id 删除 xxlJobGroup
     *
     * @param id xxlJobGroup的id
     * @return int
     */
    public int remove(@Param("id") int id);

    /**
     * 根据xxlJobGroup的id 查询 xxlJobGroup
     *
     * @param id xxlJobGroup的id
     * @return XxlJobGroup
     */
    public IapXxlJobGroup load(@Param("id") int id);

    /**
     * 分页查询（条件） XxlJobGroup
     *
     * @param offset   偏移坐标
     * @param pagesize 每页大小
     * @param appname 执行器AppName
     * @param title 执行器名称
     * @return List<XxlJobGroup>
     */
    public List<IapXxlJobGroup> pageList(@Param("offset") int offset,
                                         @Param("pagesize") int pagesize,
                                         @Param("appname") String appname,
                                         @Param("title") String title);

    /**
     * 查询总页数
     *
     * @param offset   偏移坐标
     * @param pagesize 每页大小
     * @param appname 执行器AppName
     * @param title 执行器名称
     * @return
     */
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("appname") String appname,
                             @Param("title") String title);

}
