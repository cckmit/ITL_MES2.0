package com.itl.im.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import iap.im.api.dto.IapImMessageTopDto;
import iap.im.api.entity.IapImMessageTop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息置顶
 *
 * @author 李骐光
 * @date 2020-10-15
 * @since jdk1.8
 */
public interface IapImMessageTopMapper extends BaseMapper<IapImMessageTop> {

    /**
     * 查询置顶消息
     *
     * @param ids 消息列表id集合
     * @return
     */
    List<IapImMessageTopDto> topMessage(@Param("ids") List<String> ids);

}
