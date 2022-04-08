package com.itl.mes.andon.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "呼叫类型")
public class CallTypeDTO {

    /*分页*/
    private Page page;

    /*车间名*/
    private String workShopBo;

    /*安灯类型BO*/
    private String andonTypeBo;

    /*安灯类型BO*/
    private String andonTypeName;

    private String workshopName;
}
