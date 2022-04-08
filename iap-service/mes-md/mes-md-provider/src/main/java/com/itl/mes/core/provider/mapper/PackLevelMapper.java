package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.PackLevel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 包装级别表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-07-12
 */
@Repository
public interface PackLevelMapper extends BaseMapper<PackLevel> {

    List<String> selectPackingBO(@Param("objectBO") String objectBO);

}