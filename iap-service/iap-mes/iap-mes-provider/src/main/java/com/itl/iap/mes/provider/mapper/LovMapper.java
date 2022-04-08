package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.Lov;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * lov，定义查询数据逻辑
 * @author 胡广
 * @version 1.0
 * @name TableConfigRepository
 * @description
 * @date 2019-08-1513:34
 */
@Repository
public interface LovMapper extends BaseMapper<Lov> {

    Page<Lov> findAll(Page page, @Param("lov") Lov lov);
}
