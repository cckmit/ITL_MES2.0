package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.workflow.api.dto.Page;
import com.itl.mes.core.api.dto.BomDto;
import com.itl.mes.core.api.entity.Bom;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 物料清单表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-05
 */
@Repository
public interface BomMapper extends BaseMapper<Bom> {


    IPage<Bom> findBom(IPage page, @Param("bomDto")BomDto bomDto);
}