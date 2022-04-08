package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.entity.Attached;
import com.itl.mes.core.api.entity.InspectTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检验任务 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-08-30
 */
@Repository
public interface InspectTaskMapper extends BaseMapper<InspectTask> {

    List<InspectTask> selectinspectTaskPage(IPage<InspectTask> page, @Param("params") Map<String, Object> params);

    List<InspectTask> selectinspectTaskPage(@Param("params") Map<String, Object> params);

    List<Attached> selectAttached(@Param("params")Map<String, Object> params);
}