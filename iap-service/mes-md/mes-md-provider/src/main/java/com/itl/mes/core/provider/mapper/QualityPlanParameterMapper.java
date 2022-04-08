package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.QualityPlanParameterDTO;
import com.itl.mes.core.api.entity.QualityPlanParameter;
import com.itl.mes.core.api.vo.QualityPlanExcelInfoVo;
import com.itl.mes.core.api.vo.QualityPlanVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>
 *  质量控制计划维护 Mapper 接口
 * </p>
 * @author lzh
 * @since 2019-08-29
 */
@Repository
public interface QualityPlanParameterMapper extends BaseMapper<QualityPlanParameter> {
  List<String> selectDistinctInfo(@Param("operationBo") String operationBo,@Param("enable") String enable);

  List<String> selectOriginInfo(@Param("operationBo")String operationBo,@Param("itemBo") String itemBo,@Param("enable") String enable);

  List<QualityPlanExcelInfoVo> selectParams(@Param("operationBo")String operationBo, @Param("itemBo") String itemBo);

  IPage<QualityPlanParameter>  selectQualityPlanPage(Page page, @Param("qualityPlanParameterDTO")QualityPlanParameterDTO qualityPlanParameterDTO,@Param("qualityPlanBo")String qualityPlanBo,@Param("site")String site);
}
