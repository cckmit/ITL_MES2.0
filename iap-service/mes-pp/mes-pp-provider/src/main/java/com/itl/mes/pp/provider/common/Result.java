package com.itl.mes.pp.provider.common;

public class Result extends ResponseResult {
    public Result(ResultCode resultCode, Object obj) {
        super(resultCode);
        this.obj = obj;
    }
    Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
