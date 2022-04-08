package com.itl.mes.andon.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.andon.api.dto.AndonQueryDTO;
import com.itl.mes.andon.api.entity.Andon;
import com.itl.mes.andon.api.vo.AndonVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 安灯
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Mapper
public interface AndonMapper extends BaseMapper<Andon> {

    IPage<AndonVo> findList(Page page, @Param("andonQueryDTO") AndonQueryDTO andonQueryDTO);


    AndonVo findAndonById(String id);

}
