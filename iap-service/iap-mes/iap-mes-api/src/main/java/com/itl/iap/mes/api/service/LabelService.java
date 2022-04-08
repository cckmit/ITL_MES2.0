package com.itl.iap.mes.api.service;

import com.itl.iap.common.base.exception.CommonException;

import java.util.List;

public interface LabelService {
    List<String> selectFieldByTableName(String tableName) throws CommonException;
}
