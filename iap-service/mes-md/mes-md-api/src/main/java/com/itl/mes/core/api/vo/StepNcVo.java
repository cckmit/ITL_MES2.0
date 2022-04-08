package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "不良工步Vo")
public class StepNcVo {

    @ApiModelProperty("物料信息")
    private Item item;

    @ApiModelProperty("工步信息")
    private List<WorkStep> workStepList;

    @ApiModelProperty("责任人信息")
    private List<PersonLiable> personLiableList;

    @Data
    @ApiModel(value = "物料信息")
    public static class Item{
        @ApiModelProperty("物料bo")
        private String itemBo;
        @ApiModelProperty("物料bo")
        private String item;
        @ApiModelProperty("物料bo")
        private String itemName;
        @ApiModelProperty("物料bo")
        private String itemDesc;
    }

    @Data
    @ApiModel(value = "工步信息")
    public static class WorkStep{
        @ApiModelProperty(value = "工步bo")
        private String workStepBo;
        @ApiModelProperty(value = "工步编码")
        private String workStepCode;
        @ApiModelProperty(value = "工步编码")
        private String workStepName;
    }

    @Data
    @ApiModel(value = "责任人信息")
    public static class PersonLiable{
        @ApiModelProperty("责任人id")
        private String userId;
        @ApiModelProperty("责任人姓名")
        private String userName;
        @ApiModelProperty("内部工号")
        private String insideNo;
    }
}
