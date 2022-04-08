package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.mes.api.entity.label.LabelEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/3 17:39
 */
public interface LabelMapper extends BaseMapper<LabelEntity> {

    List<String> selectFieldByTableName(String tableName);

    String selectSfcByFiled(@Param("fieldName") String fieldName,@Param("sfc") String sfc);

    String selectOperationOrderByFiled(@Param("fieldName") String fieldName,@Param("sfc") String sfc);

    String selectItemByFiled(@Param("fieldName") String fieldName,@Param("sfc") String sfc);

    String selectRouterByFiled(@Param("fieldName") String fieldName, @Param("sfc") String sfc);

    String selectRouterNameByFiled(@Param("fieldName") String fieldName, @Param("sfc") String sfc);

    List<String> selectOperationNameByFiled(@Param("fieldName") String fieldName, @Param("sfc") String sfc);

    String selectDeviceByFiled(@Param("fieldName") String fieldName, @Param("sfc") String sfc);



}
