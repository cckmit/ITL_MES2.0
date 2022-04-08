package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/29
 * @since JDK1.8
 */
@Data
@ApiModel(value = "指导书工序保存Dto")
public class InstructorOperationDto {
    @ApiModelProperty("指导书Bo")
    private String instructorBo;

    @ApiModelProperty("工序Bo集合")
    private List<String> operationBos;
}
