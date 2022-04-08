package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.report.api.entity.AndonException;
import com.itl.iap.report.api.vo.AndonWarningVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AndonWarningDto {

    @ApiModelProperty("安灯车间异常信息")
    private List<AndonWarningVo> andonWarningVos;

    @ApiModelProperty("安灯异常")
    private IPage<AndonException> andonExceptions;
}
