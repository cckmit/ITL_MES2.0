package com.itl.mes.andon.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.andon.api.entity.Push;
import org.apache.ibatis.annotations.Mapper;

/**
 * 安灯推送设置
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Mapper
public interface PushMapper extends BaseMapper<Push> {
  /**
   * 根据bo查询
   *
   * @param bo
   * @return
   */
  Push getByBo(String bo);
}
