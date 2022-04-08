package com.itl.mes.core.api.dto;

import com.itl.mes.core.api.entity.OperationOrderAndQty;
import com.itl.mes.core.api.vo.StemDispatchVo;
import lombok.Data;

import java.util.List;
@Data
public class OkStemDispatchDTO {
    private List<StemDispatchVo> stemDispatchVo;
    private List<OperationOrderAndQty> operationOrderAndQtyList;
    private WorkStationDTO workStationDTO;
}
