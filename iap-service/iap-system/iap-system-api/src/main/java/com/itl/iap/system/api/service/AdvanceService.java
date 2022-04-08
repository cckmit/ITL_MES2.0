package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.system.api.dto.AdvancedQueryDto;
import com.itl.iap.common.base.exception.CommonException;

import java.util.List;
import java.util.Map;

/**
 * @author 崔翀赫
 * @date 2021/3/5$
 * @since JDK1.8
 */
public interface AdvanceService {
    IPage<Map> queryAdvance(AdvancedQueryDto advancedQueryDtos) throws CommonException;
    List<Map<String, String>> getColumn(String pageId);
}
