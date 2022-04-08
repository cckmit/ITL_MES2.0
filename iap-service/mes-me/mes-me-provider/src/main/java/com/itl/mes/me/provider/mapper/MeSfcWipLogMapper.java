package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.me.api.dto.snLifeCycle.StationRecord;
import com.itl.mes.me.api.entity.MeSfcWipLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生产过程日志
 *
 * @author renren
 * @date 2021-01-25 14:43:26
 */
@Mapper
public interface MeSfcWipLogMapper extends BaseMapper<MeSfcWipLog> {

    List<StationRecord> getStationRecord(@Param("sn")String sn);
}
