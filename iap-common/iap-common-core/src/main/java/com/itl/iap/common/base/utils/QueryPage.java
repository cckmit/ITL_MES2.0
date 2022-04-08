package com.itl.iap.common.base.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Map;


public class QueryPage<T> extends Page<T> {

    private static final String PAGE = "page";
    private static final String LIMIT = "limit";
    private static final int SIZE = 20;
    private static final String ORDER_BY_FIELD = "orderByField";
    private static final String IS_ASC = "isAsc";
    private static final String TOTAL = "total";

    public QueryPage(Map<String, Object> params) {
        super(StrUtil.isBlankIfStr(params.get(PAGE)) ? 1 : Integer.parseInt(params.get(PAGE).toString())
                , StrUtil.isBlankIfStr(params.get(LIMIT)) ? SIZE : Integer.parseInt(params.get(LIMIT).toString()));

        String orderByField = params.getOrDefault(ORDER_BY_FIELD, "").toString();
        boolean isAsc = Boolean.parseBoolean(params.getOrDefault(IS_ASC, Boolean.TRUE).toString());
        if (isAsc) {
            if (StrUtil.isNotBlank(orderByField)) {
                this.setAsc(orderByField);
            }

        } else {
            if (StrUtil.isNotBlank(orderByField)){
                this.setDesc(orderByField);
            }
        }

        if (!StrUtil.isBlankIfStr(params.get(TOTAL))) {
            this.setTotal(Long.parseLong(params.get(TOTAL).toString()));
        }
    }

}
