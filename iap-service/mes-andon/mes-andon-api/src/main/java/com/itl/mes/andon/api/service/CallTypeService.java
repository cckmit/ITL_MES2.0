package com.itl.mes.andon.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.andon.api.dto.CallTypeDTO;
import com.itl.mes.andon.api.entity.CallType;
import com.itl.mes.andon.api.entity.GradePush;

import java.util.List;

public interface CallTypeService extends IService<CallType> {
    void saveCallType(CallType callType);

    void saveGradePush(List<GradePush> gradePush);

    IPage<CallType> queryPage(CallTypeDTO callTypeDTO);
}
