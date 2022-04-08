package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.entity.QualityPlan;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-08-29
 */
@Repository
public interface QualityPlanMapper extends BaseMapper<QualityPlan> {
    /**
     * 分页查询
     * @param page
     * @param params
     * @return
     */
    List<Map> selectQualityPlanPage(IPage<Map> page, @Param("params") Map<String, Object> params);

    void setDefaultPlan(@Param("param")Map<String, String> param);

    List<String> selectOperation(@Param("qualityPlan") String qualityPlan);

    String selectMaxPlan();
}