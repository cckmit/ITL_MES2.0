package com.itl.iap.mes.provider.exception;


import com.itl.iap.mes.provider.common.ResultCode;

public class CustomException extends RuntimeException {

    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
    public ResultCode getResultCode(){
        return resultCode;
    }


}
