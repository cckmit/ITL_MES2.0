package com.itl.mes.andon.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.andon.api.dto.AndonLookPlateQueryDTO;
import com.itl.mes.andon.api.vo.AndonLookPlateQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/25
 */
public interface AndonLookPlateMapper extends BaseMapper {


    List<AndonLookPlateQueryVo> findProductLineAndonLookPlant(@Param("andonLookPlateQueryDTO") AndonLookPlateQueryDTO andonLookPlateQueryDTO);

    List<AndonLookPlateQueryVo> findStationAndonLookPlate(@Param("andonLookPlateQueryDTO") AndonLookPlateQueryDTO andonLookPlateQueryDTO);

    List<String> getShopOrdersByProductLineBo(String productLineBo);
}
