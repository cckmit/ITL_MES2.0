package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.dto.AndonParamDto;
import com.itl.iap.report.api.dto.AndonStateDto;
import com.itl.iap.report.api.entity.AndonException;
import com.itl.iap.report.api.entity.ShopOrderTrack;
import com.itl.iap.report.api.vo.AndonExceptionVo;
import com.itl.iap.report.api.vo.AndonTypeVo;
import com.itl.iap.report.api.vo.AndonVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface AndonReportMapper extends BaseMapper<AndonException> {
    List<AndonVo> selectCountInfo();

    List<AndonVo> selectGroupByAndonType();

    List<AndonVo> selectTimeGroupByAndonType();

    List<AndonVo> selectGroupByWorkShopName();

    List<AndonVo> selectGoupByMonth();

    int selectGroupByWeek(@Param("diff") int diff);

    List<AndonVo> selectGroupByDay();

    List<AndonVo> selectAllInfo(@Param("state") int state);

    IPage<AndonException> selectAndonException(Page page);

    List<AndonTypeVo> selectAndonType(@Param("andonParamDto") AndonParamDto andonParamDto);
    List<AndonTypeVo> selectAndonAllTime(@Param("andonParamDto") AndonParamDto andonParamDto);

    IPage<AndonExceptionVo> selectAndonExceptionInfo(Page page,@Param("andonParamDto") AndonParamDto andonParamDto);

    AndonStateDto selectNumByWorkshopNameAndAndonTypeName(@Param("andonTypeName") String andonTypeName,@Param("workShopName") String workShopName,@Param("andonParamDto") AndonParamDto andonParamDto);

    List<String> selectDistinctAndonTypeName(@Param("andonParamDto") AndonParamDto andonParamDto);

    AndonStateDto selectNumByAndonTypeName(@Param("andonTypeName") String andonTypeName,@Param("andonParamDto") AndonParamDto andonParamDto);

    List<AndonException> selectListByState(@Param("andonTypeName") String andonTypeName,@Param("workShopName") String workShopName,@Param("state")String state,@Param("andonParamDto") AndonParamDto andonParamDto);


}
