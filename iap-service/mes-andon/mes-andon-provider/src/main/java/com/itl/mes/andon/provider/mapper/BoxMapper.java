package com.itl.mes.andon.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.andon.api.entity.Box;
import com.itl.mes.andon.api.vo.BoxForShowVo;
import com.itl.mes.andon.api.vo.BoxQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 安灯灯箱
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Mapper
public interface BoxMapper extends BaseMapper<Box> {

    IPage<BoxForShowVo> findList(Page page, @Param("boxQuery") BoxQueryVo boxQueryVo);

    String getResourceType(@Param("type") String type);
}
