package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.SfcNcLog;
import org.apache.ibatis.annotations.Param;


public interface SfcNcLogMapper  extends BaseMapper<SfcNcLog> {
    void updateBySfc(@Param("sonSfc") String sonSfc,@Param("parentSfc") String parentSfc);
}
