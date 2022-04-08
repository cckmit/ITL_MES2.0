package com.itl.iap.workflow.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.workflow.api.dto.ActHiNotifyDto;
import com.itl.iap.workflow.api.entity.ActHiNotify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 抄送通知(ActHiNotify)表数据库访问层
 *
 * @author 罗霄
 * @date 2020-09-16
 * @since jdk1.8
 */
@Mapper
public interface ActHiNotifyMapper extends BaseMapper<ActHiNotify> {

    /**
     * 分页查询
     *
     * @param actHiNotifyDto 抄送通知实例
     * @return IPage<ActHiNotifyDto>
     */
    IPage<ActHiNotifyDto> pageQuery(Page page, @Param("actHiNotifyDto") ActHiNotifyDto actHiNotifyDto);
}