package com.itl.iap.report.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.report.api.dto.ShopOrderTrackDto;
import com.itl.iap.report.api.entity.ShopOrderTrack;
import com.itl.iap.report.api.vo.ShopOrderTrackVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ShopOrderTrackService extends IService<ShopOrderTrack> {
   ShopOrderTrackVo selectByCondition(ShopOrderTrackDto shopOrderTrackDto);


   void export(ShopOrderTrackDto shopOrderTrackDto , HttpServletResponse response) throws CommonException;
}
