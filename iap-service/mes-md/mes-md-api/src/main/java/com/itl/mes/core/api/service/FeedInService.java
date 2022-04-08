package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.FeedInDTO;
import com.itl.mes.core.api.dto.OutStationDto;
import com.itl.mes.core.api.entity.FeedIn;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.vo.FeedInVo;

import java.util.List;
import java.util.Map;

public interface FeedInService extends IService<FeedIn> {
    FeedInVo query(FeedInDTO feedInDTO) throws CommonException;

    List<FeedInVo> queryList(FeedInDTO feedInDTO) throws CommonException;

    void outStation(OutStationDto outStationDto) throws CommonException;
}
