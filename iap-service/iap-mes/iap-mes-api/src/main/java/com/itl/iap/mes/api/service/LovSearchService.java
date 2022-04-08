package com.itl.iap.mes.api.service;


import com.itl.iap.mes.api.dto.LovData;

import java.util.Map;

/**
 * Lov查询统一接口
 * @author 胡广
 * @version 1.0
 * @name LovSearchService
 * @description
 * @date 2019-09-02
 */
public interface LovSearchService {
    /**
     * 默认分页
     * @param param
     * @param page
     * @param pageSize
     * @return
     */
    LovData search(Map<String, String> param, int page, int pageSize);
}
