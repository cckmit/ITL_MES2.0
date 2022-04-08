package com.itl.iap.mes.api.dto.prove;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/10/30 15:45
 */
@Data
@ApiModel(value = "用户证明查询实体")
public class UserProveQueryDTO {


    //分页对象
    private Page page;


    @ApiModelProperty(value = "工厂编号")
    private String site;

    @ApiModelProperty(value = "用户ID")
    private String userId;


    @ApiModelProperty(value = "证明编码")
    private String proveCode;


}
