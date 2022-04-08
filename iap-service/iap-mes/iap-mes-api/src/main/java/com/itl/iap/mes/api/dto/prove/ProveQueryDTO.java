package com.itl.iap.mes.api.dto.prove;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/11/2 9:42
 */
@Data
@ApiModel(value = "证明查询实体")
public class ProveQueryDTO  {



    //分页对象
    private Page page;


    @ApiModelProperty(value = "工厂编号")
    private String site;


    @ApiModelProperty(value = "证明编号")
    private String proveCode;


}
