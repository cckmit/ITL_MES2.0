package com.itl.iap.common.base.interceptor;

import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SQL 拦截器
 *
 * @author tanq
 * @date 2020/7/28 15:51
 * @since jdk1.8
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Slf4j
@SuppressWarnings("all")
public class SqlInterceptor implements Interceptor {
    /**
     * 创建时间
     */
    private static final String CREATE_TIME = "createDate";
    private static final String CREATER = "creater";
    /**
     * 更新时间
     */
    private static final String UPDATE_TIME = "lastUpdateDate";
    private static final String UPDATE_BY = "lastUpdateBy";
    @Autowired
    private UserUtil userUtil;

    /**
     * 判断SQL操作类型，如果是 INSERT/UPDATE语句，则进行拦截，进行下一步操作
     *
     * @param invocation
     * @return Object
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String name = invocation.getMethod().getName();
        if (name.toLowerCase().equals("update") || name.toLowerCase().equals("install")) {
            return invokeUpdate(invocation);
        } else {
            return invocation.proceed();
        }
    }

    /**
     * 对 UPDATE 操作进一步处理（添加修改人，修改时间）
     *
     * @param invocation
     * @return Object
     * @throws Exception
     */
    private Object invokeUpdate(Invocation invocation) throws Exception {
        // 获取第一个参数
        Object[] args1 = invocation.getArgs();
        // 去除null
        List<Object> objList = Arrays.stream(args1).filter(x -> x != null).collect(Collectors.toList());
        MappedStatement ms = null;
        Object args = null;   // 获取参数
        // 赋值
        for (Object obj : objList) {
            if (obj instanceof MappedStatement) {
                ms = (MappedStatement) obj;
            } else if (obj instanceof Object) {
                args = obj;
            }
        }
        if (ms == null || args == null) {
            return invocation.proceed();
        }
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        // 判断参数类型  是不是MapperMethod.ParamMap 是 就循环更改  不是就是对象直接更改
        if (args instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap<Object> mapObj = (MapperMethod.ParamMap<Object>) args;
            for (Map.Entry<String, Object> obj : mapObj.entrySet()) {
                Object paramObj = mapObj.get(obj.getKey());
                if (paramObj instanceof List) {
                    this.objList(paramObj, ms);
                } else {
                    Field[] fields = paramObj.getClass().getDeclaredFields();
                    this.upField(fields, ms, paramObj);
                }
            }
        } else if (args instanceof DefaultSqlSession.StrictMap) {
            DefaultSqlSession.StrictMap<Object> mapObj = (DefaultSqlSession.StrictMap<Object>) args;
            for (Map.Entry<String, Object> obj : mapObj.entrySet()) {
                Object paramObj = mapObj.get(obj.getKey());
                if (paramObj instanceof List) {
                    this.objList(paramObj, ms);
                } else {
                    Field[] fields = paramObj.getClass().getDeclaredFields();
                    this.upField(fields, ms, paramObj);
                }
            }
        } else {
            Field[] fields = args.getClass().getDeclaredFields();
            this.upField(fields, ms, args);
        }
        return invocation.proceed();
    }

    /**
     * 如果是集合就递归
     *
     * @param paramObj List 对象
     * @param ms       MappedStatement
     */
    private void objList(Object paramObj, MappedStatement ms) {
        List<Object> listObj = (List<Object>) paramObj;
        for (Object obj : listObj) {
            if (obj instanceof List) {
                this.objList(obj, ms);
            }
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            this.upField(declaredFields, ms, obj);
        }
    }

    /**
     * 开始执行修改方法
     *
     * @param fields 反射获取字段列表
     * @param ms     MappedStatement
     * @param args   对象参数
     */
    private void upField(Field[] fields, MappedStatement ms, Object args) {
        // 如果 insert 语句  则添加创建时间创建人 修改时间和修改人
        if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            this.setAllParams(fields, args, CREATE_TIME, new Date());
            this.setAllParams(fields, args, CREATER, UPDATE_BY.toString());
            this.setAllParams(fields, args, UPDATE_BY, UPDATE_BY.toString());
            this.setAllParams(fields, args, UPDATE_TIME, new Date());
            // 如果是update语句  则添加修改时间修改人
        } else if (ms.getSqlCommandType() == SqlCommandType.UPDATE) {
            this.setAllParams(fields, args, UPDATE_BY, UPDATE_BY.toString());
            this.setAllParams(fields, args, UPDATE_TIME, new Date());
        }
    }

    /**
     * 根据传递参数放进行修改
     *
     * @param fields   反射存在的参数
     * @param obj      需要改变的对象
     * @param valueKey 变更的字段
     * @param valObj   变更参数类型
     */
    private void setAllParams(Field[] fields, Object obj, String valueKey, Object valObj) {
        UserTDto user = this.getUser();

        for (int i = 0; i < fields.length; i++) {
            if (valueKey.toLowerCase().equals(fields[i].getName().toLowerCase())) {
                try {
                    if (valObj instanceof Date) {
                        fields[i].setAccessible(true);
                        fields[i].set(obj, new Date());
                        fields[i].setAccessible(false);
                    }
                    if (user != null && user.getUserName() != null) {
                        if (valObj instanceof String) {
                            fields[i].setAccessible(true);
                            fields[i].set(obj, user.getUserName());
                            fields[i].setAccessible(false);
                        }
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 获取当前操作的用户
     *
     * @return UserTDto
     */
    private UserTDto getUser() {
        UserTDto user = userUtil.getUser();
        if (user != null && StringUtils.isNotEmpty(user.getUserName())) {
            return user;
        }
        return null;
    }

    /**
     * 包装目标对象的：为目标对象创建一个代理对象
     *
     * @param target
     * @return Object
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    /**
     * 设置参数
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {

    }


}


