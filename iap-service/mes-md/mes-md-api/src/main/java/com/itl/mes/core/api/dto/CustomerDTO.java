package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *客户表DTO
 */

@Data
@ApiModel(value = "CustomerDTO",description = "客户查询实体")
public class CustomerDTO {
    @ApiModelProperty("主键")
    private String bo;

    @NotNull
    @ApiModelProperty("客户编码")
    private String customer;

    @NotNull
    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户描述")
    private String customerDesc;

    @ApiModelProperty("省/州")
    private String stateProv;

    @ApiModelProperty("客户地址")
    private String address1;

    @ApiModelProperty("工厂")
    private String site;

    @ApiModelProperty("国家")
    private String country;


    @ApiModelProperty("邮政编码")
    private String postalCode;

    @ApiModelProperty("电话")
    private String tel;
    @ApiModelProperty("邮箱")
    private String email;


    @ApiModelProperty("版本")
    private String version;

    /**
     * 客户代表
     */
    @ApiModelProperty("客户代表")
    private String customerRepresentative;


    @ApiModelProperty("外部ID")
    private String address2;


    @ApiModelProperty("联系人")
    private String contact;

    @ApiModelProperty("城市")
    private String city;


    @ApiModelProperty("分页对象")
    private Page page;
}
