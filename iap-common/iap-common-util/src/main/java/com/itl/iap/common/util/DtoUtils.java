package com.itl.iap.common.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体类与DTO相互转换工具类
 *
 * @author tanq
 * @date 2020/6/19 10:07
 * @since jdk1.8
 */
@Slf4j
public class DtoUtils {

    /**
     * 转换List
     *
     * @param target     需要转的do或dto类  Do.class或者Dto.class
     * @param sourceList 数组
     * @param <T>
     * @param <F>
     * @return 返回转换后的List<do>或者List<dto>
     */
    public static <T, F> List<F> convertList(Class<F> target, List<T> sourceList) {
        if (!CollectionUtils.isEmpty(sourceList)) {
            List<F> targetList = Lists.newArrayList();
            for (T t : sourceList) {
                try {
                    F f = target.newInstance();
                    BeanUtils.copyProperties(t, f);
                    targetList.add(f);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("====================格式转换异常===========================");
                }
            }
            return targetList;
        }
        return null;
    }

    /**
     * 转换单个对象
     *
     * @param targetClass 转换的类型
     * @param sourceObj   需要转换的参数
     * @param <T>
     * @return 返回转换的完成的对象
     */
    public static <T> T convertObj(@NotNull Object sourceObj, @NotNull Class<T> targetClass) {
        try {
            T t = targetClass.newInstance();
            BeanUtils.copyProperties(sourceObj, t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====================格式转换异常===========================");
        }
        return null;
    }

    /**
     * 转换List，(Excel导入专用，将 int 类型的值转为 short）
     *
     * @param target     需要转的do或dto类  Do.class或者Dto.class   EasyPoi专用
     * @param sourceList 数组
     * @param <T>
     * @param <F>
     * @return 返回转换后的List<do>或者List<dto>
     */
    public static <T, F> List<F> convertListForImportExcel(Class<F> target, List<T> sourceList) {
        List<F> targetList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(sourceList)) {
            for (T t : sourceList) {
                try {
                    F f = target.newInstance();
                    // 将bean转为beanMap，为了将 int 类型的值转为 short
                    Map<String, Object> beanMap = beanToMap(t);
                    mapToBean(beanMap, f);
                    targetList.add(f);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("====================格式转换异常===========================");
                }
            }
        }
        return targetList;
    }

    /**
     * 转换List，(Excel导出专用，将 int 类型的值转为 short）
     *
     * @param target     需要转的do或dto类  Do.class或者Dto.class   EasyPoi专用
     * @param sourceList 数组
     * @param <T>
     * @param <F>
     * @return 返回转换后的List<do>或者List<dto>
     */
    public static <T, F> List<F> convertListForExportExcel(Class<F> target, List<T> sourceList) {
        List<F> targetList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(sourceList)) {
            for (T t : sourceList) {
                try {
                    F f = target.newInstance();
                    // 将bean转为beanMap，为了将 int 类型的值转为 short
                    Map<String, Object> beanMap = beanToMapForDownload(t);
                    mapToBean(beanMap, f);
                    targetList.add(f);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("====================格式转换异常===========================");
                }
            }
        }
        return targetList;
    }


    /**
     * 将bean装换为map对象
     *
     * @param obj
     * @return Map<String, Object>
     */
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        } else if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        BeanMap beanMap = BeanMap.create(obj);
        for (Object key : beanMap.keySet()) {
            if (beanMap.get(key) != null && (beanMap.get(key).getClass() == Integer.class || beanMap.get(key).getClass() == int.class)) {
                map.put(key + "", Short.parseShort(beanMap.get(key).toString()));
            } else {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将bean装换为map对象
     *
     * @param obj
     * @return Map<String, Object>
     */
    public static Map<String, Object> beanToMapForDownload(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        } else if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        BeanMap beanMap = BeanMap.create(obj);
        for (Object key : beanMap.keySet()) {
            if (beanMap.get(key) != null && (beanMap.get(key).getClass() == Short.class || beanMap.get(key).getClass() == short.class)) {
                map.put(key + "", Integer.parseInt(beanMap.get(key).toString()));
            } else {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map
     * @param bean
     * @return T
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 对象转map
     *
     * @param obj
     * @param <T>
     * @return Map<String, String>
     */
    public static <T> Map<String, String> objectToMap(T obj) {
        Map<String, String> map = new HashMap<String, String>();
        // 获取f对象对应类中的所有属性域
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
             /*   if (fields[i].getType() == List.class) {
                    continue;
                }*/
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fields[i].get(obj);
                if (o != null)
                    map.put(varName, o.toString());
                // System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }

}
