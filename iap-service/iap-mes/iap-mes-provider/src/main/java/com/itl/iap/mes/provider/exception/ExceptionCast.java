package com.itl.iap.mes.provider.exception;


import com.itl.iap.mes.provider.common.ResultCode;

public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
