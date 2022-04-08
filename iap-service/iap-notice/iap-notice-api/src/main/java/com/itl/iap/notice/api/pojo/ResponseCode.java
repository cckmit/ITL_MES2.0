package com.itl.iap.notice.api.pojo;

/**
 * 请求响应码
 *
 * @author Linjl
 * @date  2020/3/19
 * @since jdk1.8
 */
public enum ResponseCode {
    /** 成功*/
    SUCCESS("1000", "success."),
    ERROR("1001", "error."),
    PARA_ERROR("1002", "parameters error.");

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
