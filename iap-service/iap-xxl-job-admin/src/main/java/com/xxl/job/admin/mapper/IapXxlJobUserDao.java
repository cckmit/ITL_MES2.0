package com.xxl.job.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.job.admin.core.model.IapXxlJobUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表（xxl_job_user）的操作
 * XXL用户表
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Mapper
public interface IapXxlJobUserDao extends BaseMapper<IapXxlJobUser> {

    /**
     * 分页查询
     *
     * @param offset   偏移量
     * @param pagesize 每页大小
     * @param username 用户名
     * @param role     角色
     * @return List<XxlJobUser>
     */
    public List<IapXxlJobUser> pageList(@Param("offset") int offset,
                                        @Param("pagesize") int pagesize,
                                        @Param("username") String username,
                                        @Param("role") int role);

    /**
     * 统计总数
     *
     * @param offset   偏移量
     * @param pagesize 每页大小
     * @param username 用户名
     * @param role     角色
     * @return int
     */
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("username") String username,
                             @Param("role") int role);

    /**
     * 根据用户名查找
     *
     * @param username 用户名
     * @return XxlJobUser
     */
    public IapXxlJobUser loadByUserName(@Param("username") String username);

    /**
     * 保存
     *
     * @param iapXxlJobUser
     * @return int
     */
    public int save(IapXxlJobUser iapXxlJobUser);

    /**
     * 更新
     *
     * @param iapXxlJobUser
     * @return int
     */
    public int update(IapXxlJobUser iapXxlJobUser);

    /**
     * 删除
     *
     * @param id 用户ID
     * @return
     */
    public int delete(@Param("id") int id);

}
