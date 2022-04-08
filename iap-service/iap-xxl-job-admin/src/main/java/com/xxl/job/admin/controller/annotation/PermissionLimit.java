package com.xxl.job.admin.controller.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义了一个方法上的权限限制注解，主要控制登录拦截和是否要求有管理员权限。
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionLimit {

    /**
     * 登录拦截 (默认拦截)
     */
    boolean limit() default true;

    /**
     * 要求管理员权限
     *
     * @return
     */
    boolean adminuser() default false;

}