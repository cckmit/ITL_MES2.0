package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.FaultDto;
import com.itl.iap.mes.api.entity.CheckExecute;
import com.itl.iap.mes.api.entity.Fault;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * @time 2018年11月14日
 * @since JDK 1.8
 */

public interface CheckExecuteMapper  extends BaseMapper<CheckExecute> {

    IPage<CheckExecute> pageQuery(Page page, @Param("checkExecute") CheckExecute checkExecute);
}
