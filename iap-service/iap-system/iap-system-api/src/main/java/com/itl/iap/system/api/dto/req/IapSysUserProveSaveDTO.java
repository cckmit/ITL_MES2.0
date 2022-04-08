package com.itl.iap.system.api.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/10/29 11:23
 */
@Data
@ApiModel(value = "保存用户证明关联表对象",description = "保存用户证明关联表对象")
public class IapSysUserProveSaveDTO {


    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "证明id集合")
    private List<String> proveIds;


}
