package com.itl.iap.mes.api.dto.prove;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/10/30 15:44
 */
@Data
@ApiModel(value = "工位证明查询实体")
public class StationProveQueryDTO {


    private static final long serialVersionUID = 123250230159623466L;
    //分页对象
    private Page page;


    @ApiModelProperty(value = "工厂编号")
    private String site;


    @ApiModelProperty(value = "证明编码")
    private String proveCode;


    @ApiModelProperty(value = "工位编号")
    private String stationBo;


}
