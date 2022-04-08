//package com.itl.iap.mes.provider.aop;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import java.util.List;
///**
// * XUEJIANHUI
// */
//@Component
//@Aspect
//@Order(2)
//public class PageAopConfig {
//
//    @Pointcut("@annotation(com.itl.iap.annotation.EnablePaging)")
//    public void serviceAspect(){
//    }
//
//    @Around(value = "serviceAspect()")
//    public Object doAround(ProceedingJoinPoint point) throws  Throwable{
//        Object[] args = point.getArgs();
//        Signature signature = point.getSignature();
//        Integer page = 1;
//        Integer pageSize = 10;
//        MethodSignature methodSignature = (MethodSignature) signature;
//        //通过这获取到方法的所有参数名称的字符串数组
//        String[] parameterNames = methodSignature.getParameterNames();
//        for(int n = 0; n<parameterNames.length; n++){
//            if(parameterNames[n].equals("page")){
//                page = (int)args[n];
//            }
//            if(parameterNames[n].equals("pageSize")){
//                pageSize = (int)args[n];
//            }
//        }
//        Page pagehelper = new Page(page,pageSize);
//        Object object = point.proceed();
//        if(object instanceof List){
//            List objList = (List)object;
//            PageInfo pageInfo = new PageInfo(objList);
//            return pageInfo;
//        }
//        return  object;
//    }
//}
