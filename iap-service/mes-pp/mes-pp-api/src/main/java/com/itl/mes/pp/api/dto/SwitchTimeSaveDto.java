package com.itl.mes.pp.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author yaoxiang
 * @date 2020/12/17
 * @since JDK1.8
 */
@Data
@Accessors(chain = true)
@ApiModel("生产切换时间DtoForSave")
public class SwitchTimeSaveDto {
    private String bo;
    private String processCharacteristics;
    private String targetProcessCharacteristics;
    private String switchType;
    private Float switchTime;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;
}
