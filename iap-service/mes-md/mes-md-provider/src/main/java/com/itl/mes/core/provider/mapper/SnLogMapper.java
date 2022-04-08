package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.entity.Sn;
import com.itl.mes.core.api.entity.SnLog;
import com.itl.mes.core.api.vo.SnLogVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SN日志表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-09-25
 */
@Repository
public interface SnLogMapper extends BaseMapper<SnLog> {

    List<SnLog> selectPageByDate(IPage<SnLogVo> page, @Param("params")Map<String, Object> params);

    List<SnLog> selectPageByDate(@Param("params")Map<String, Object> params);

    List<Sn> getSnByDateJudge(@Param("complementCodeState")String complementCodeState, @Param("itemType")String itemType,
                              @Param("startMaxSerialNumber")Integer startMaxSerialNumber, @Param("endMaxSerialNumber")Integer endMaxSerialNumber);
}