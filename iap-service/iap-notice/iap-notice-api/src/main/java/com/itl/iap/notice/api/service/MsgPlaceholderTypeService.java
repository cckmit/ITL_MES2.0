package com.itl.iap.notice.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.notice.api.entity.MsgPlaceholderType;


/**
 * (MsgPlaceholderType)表Service
 *
 * @author liaochengdian
 * @date  2020-04-07
 * @since jdk1.8
 */
public interface MsgPlaceholderTypeService extends IService<MsgPlaceholderType> {

    /**
     * 新增
     * @param typeName
     * @return MsgPlaceholderType
     */
    MsgPlaceholderType updateTypeData(String typeName);
}
