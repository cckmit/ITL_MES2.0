package com.itl.mes.pp.api.dto.schedule;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "ProductionLineResDTO",description = "产线返回实体")
public class ProductionLineResDTO {


    private String productionLineBo;


    private String productionLine;


    private String productionLineName;

}
