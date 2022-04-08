package com.itl.iap.system.api.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/11/10 9:11
 */
@Data
@ApiModel(value = "角色用户的查询实体")
public class IapRoleUserQueryDTO {

    @ApiModelProperty(name = "分页对象")
    private Page page;


    @ApiModelProperty(name = "角色ID")
    private String roleId;

    @ApiModelProperty(name = "用户名称")
    private String userName;



}
