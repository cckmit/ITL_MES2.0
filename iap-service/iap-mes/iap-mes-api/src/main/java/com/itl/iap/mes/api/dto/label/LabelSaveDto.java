package com.itl.iap.mes.api.dto.label;

import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.api.entity.label.LabelEntityParams;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/29
 * @since JDK1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelSaveDto {

    private String id;

    private String labelTypeId;

    private String site;

    private Integer useDataSource;

    private String label;

    private String labelDescription;

    private Integer state;

    private MesFiles jasperFile;

    private List<LabelEntityParams> labelEntityParamsList;

    private List<CustomDataAndValVo> customDataAndValVoList;

    private List<CustomDataValVo> customDataValVoList;
}
