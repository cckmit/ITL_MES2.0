package com.itl.iap.mes.provider.aop;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.mes.provider.annotation.ParseState;
import com.itl.iap.mes.provider.config.BaseEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XUEJIANHUI
 */
@Component
@Aspect
@Order(1)
public class StateAopConfig {

    @Pointcut("@annotation(com.itl.iap.mes.provider.annotation.ParseState)")
    public void serviceAspect(){
    }

    @Around(value = "serviceAspect()")
    public Object doAround(ProceedingJoinPoint point) throws  Throwable{
        Object object = point.proceed();
        MethodSignature signature = (MethodSignature) point.getSignature();
        ParseState declaredAnnotation = signature.getMethod().getDeclaredAnnotation(ParseState.class);
        Class aClass = declaredAnnotation.value();
        Class<?>[] iter=aClass.getInterfaces();
        System.out.print(iter);
        Method method = aClass.getMethod("values");
        BaseEnum inter[] = (BaseEnum[])method.invoke(null, null);
        Map<Integer,String> map = new HashMap<>();
        for (BaseEnum enumMessage : inter) {
            map.put(enumMessage.getItem(),enumMessage.getItemName());
        }

        if(object instanceof IPage){
            IPage pageInfo = (IPage)object;
            List list = pageInfo.getRecords();
            list.forEach(obj->{
                Class cls = obj.getClass();
                Field[] fields = cls.getDeclaredFields();
                Integer value = -1;
                for (int i=0;i<fields.length;i++){//遍历
                    try {
                        //得到属性
                        Field field = fields[i];
                        //打开私有访问
                        field.setAccessible(true);
                        //获取属性
                        String name = field.getName();
                        if("state".equals(name)){
                            if(field.get(obj)!=null){
                                value = (Integer)field.get(obj);
                            }
                        }
                        if("stateName".equals(name)){
                            if(map.get(value) != null){
                                field.set(obj,map.get(value));
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return  object;
    }
}
