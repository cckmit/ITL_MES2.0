package com.itl.iap.gateway.exception;

/**
 * 未认证异常
 *
 * @author tanq
 * @date 2020-10-12
 * @since jdk1.8
 */
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(Throwable throwable) {
        super(throwable);
    }
}
