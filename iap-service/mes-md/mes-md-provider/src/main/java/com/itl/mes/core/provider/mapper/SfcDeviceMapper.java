package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.SfcDevice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SfcDeviceMapper extends BaseMapper<SfcDevice> {
    List<SfcDevice> listSfcDeviceByFirstOp(@Param("operationOrder") String operationOrder,@Param("operationBo") String operationBo);
}
