package com.itl.iap.common.base.utils;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;

public class StrNotNull {

    /**
     * 验证为空报错
     *
     * @param str 验证字符
     * @param message 报错消息
     * @throws CommonException 异常
     */
    public static void validateNotNull( String str,String message ) throws CommonException {

        if( StrUtil.isBlank( str ) ){
            throw new CommonException( message, CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

    }
}
