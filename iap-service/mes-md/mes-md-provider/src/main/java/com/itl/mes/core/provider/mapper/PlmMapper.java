package com.itl.mes.core.provider.mapper;

import com.itl.mes.core.api.dto.PlmDto;
import com.itl.mes.core.api.entity.WorkStation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PlmMapper {
    String getOperationList(@Param("router") String router,@Param("version") String version);

    Map<String,String> getProcessInfo(@Param("item") String item, @Param("version") String version);

    List<WorkStation> selectWorkStep(@Param("site") String site,@Param("workingProcess") String workingProcess);
}
