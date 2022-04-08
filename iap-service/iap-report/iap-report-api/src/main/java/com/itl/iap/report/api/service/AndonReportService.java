package com.itl.iap.report.api.service;

import com.itl.iap.report.api.dto.AndonDto;
import com.itl.iap.report.api.dto.AndonExceptionDto;
import com.itl.iap.report.api.dto.AndonParamDto;
import com.itl.iap.report.api.dto.AndonWarningDto;
import com.itl.iap.report.api.vo.AndonTypeVo;
import com.itl.iap.report.api.vo.AndonVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AndonReportService {
   AndonDto selectCountInfo();

   AndonWarningDto selectAndonWarning(AndonParamDto andonParamDto);

   List<AndonTypeVo> selectAndonType(AndonParamDto andonParamDto);

   List<AndonTypeVo> selectAndonAllTime(AndonParamDto andonParamDto);

   AndonExceptionDto selectAndonExceptionInfo(AndonParamDto andonParamDto);
}
