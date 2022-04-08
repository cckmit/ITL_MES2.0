package com.itl.mes.andon.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.andon.api.vo.RecordVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 安灯日志
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {


    RecordVo getRevord(String andonBo);
	
}
