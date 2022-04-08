package com.itl.iap.system.provider.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 崔翀赫
 * @date 2021/3/5$
 * @since JDK1.8
 */
public interface AdvanceMapper {

    List<Map> advanceQuery(@Param("queryConditions") String queryConditions, @Param("tables") String tables, @Param("columns") String columns);

    Map<String,String> getColumnsDefault(@Param("id") String id);
    String getTables(@Param("id") String id);
    List<Map<String, String>> getColumn(@Param("list") List list);
}
