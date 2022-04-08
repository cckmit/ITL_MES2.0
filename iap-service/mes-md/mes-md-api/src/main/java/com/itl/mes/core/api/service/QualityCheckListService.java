package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.OperationDTO;
import com.itl.mes.core.api.dto.QualityCheckListDTO;
import com.itl.mes.core.api.entity.QualityCheckList;
import com.itl.mes.core.api.vo.OperationParamsVo;
import com.itl.mes.core.api.vo.QualityCheckAtParameterVO;
import com.itl.mes.core.api.vo.QualityCheckListVO;

import java.util.Map;

public interface QualityCheckListService extends IService<QualityCheckList> {

    /**
     * 保存或新增控制质量数据
     *
     * @param qclVO
     * @return
     */
    QualityCheckList saveInUpdate(QualityCheckListVO qclVO) throws CommonException;

    IPage<QualityCheckList> getQualityCheckList(QualityCheckListDTO qualityCheckListDTO);

    void deleteQualityCheckList(String id) throws CommonException;

    QualityCheckAtParameterVO getQualityCheckAtParameterVo(QualityCheckListDTO dto);

    QualityCheckAtParameterVO getQcoVOBySfcOperation(OperationDTO operationDTO) throws CommonException;

    String userVerify(Map<String, Object> params);

    QualityCheckAtParameterVO getQcoVOByOperation(OperationDTO operationDTO) throws CommonException;
}
