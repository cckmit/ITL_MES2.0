package com.itl.iap.system.api.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/10 11:32
 */
@Data
@ApiModel(value = "角色用户的保存实体")
public class IapRoleUserSaveDTO {


    @ApiModelProperty(value = "角色ID")
    private String roleId;


    @ApiModelProperty(value = "用户ID")
    private List<String> userIds;


}
