package com.itl.iap.mes.provider.annotation;

import java.lang.annotation.*;
/**
 * XUEJIANHUI
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnablePaging {
    String value()  default "";
}