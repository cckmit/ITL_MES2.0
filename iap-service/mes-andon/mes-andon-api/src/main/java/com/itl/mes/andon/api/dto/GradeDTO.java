package com.itl.mes.andon.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "andon类型")
public class GradeDTO {

    /*分页*/
    private Page page;

    /*车间Bo*/
    private String workShopBo;

    /*车间名*/
    private String workShopName;

    /*等级*/
    private int alertGrade;

    /*andon类型名称*/
    private String andonTypeName;
}
