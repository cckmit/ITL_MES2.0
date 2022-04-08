package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.me.api.dto.InstructorQueryDto;
import com.itl.mes.me.api.entity.Instructor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 作业指导书
 *
 * @author yaoxiang
 * @date 2020-12-25
 */
@Mapper
public interface InstructorMapper extends BaseMapper<Instructor> {

    /**
     * 分页查询
     * @param page
     * @param queryDto
     * @return
     */
    IPage<Instructor> queryList(Page page, @Param("queryDto") InstructorQueryDto queryDto);
    /**
     * 分页查询
     * @param page
     * @param queryDto
     * @return
     */
    IPage<Instructor> queryListByState(Page page, @Param("queryDto") InstructorQueryDto queryDto);
}
