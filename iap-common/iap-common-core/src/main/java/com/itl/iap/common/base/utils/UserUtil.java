package com.itl.iap.common.base.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.aop.JdbcConfig;
import com.itl.iap.common.base.dto.UserTDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户工具类
 *
 * @author tanq
 * @date 2020/7/16 15:43
 * @since jdk1.8
 */
@Component
public class UserUtil {
    /**
     * 认证方式
     */
    private static final String HEADER_TITLE = "Bearer ";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取用户信息
     * 1. 后期可能根据token解析拿到id  然后去redis获取用户对象
     * 2. 现在可以更改为从数据区获取  只需要注入一个JDBCConfig 就可以了
     *
     * @return UserTDto
     */
    public UserTDto getUser() {
        UserTDto userTDto = new UserTDto();
        userTDto.setUserName("");
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return userTDto;
            }
            HttpServletRequest request = requestAttributes.getRequest();
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isNotEmpty(authorization)) {
                String token = authorization.substring(HEADER_TITLE.length(), authorization.length());
                UserTDto username = JWTUtil.getUsername(token);
                Object o = redisTemplate.opsForValue().get(username.getUserName() + "-user");
                if (o == null) {
                    throw new RuntimeException("user is Expired");
                }
                return JSONObject.parseObject(o.toString(), UserTDto.class);
            }
            
            return userTDto;
        } catch (Exception e) {
            e.printStackTrace();
            return userTDto;
        }
    }


    public static Field[] addAll(Field[] target, Field[] source) {
        if (target != null) {
            List<Field> fieldTarget = Stream.of(target).collect(Collectors.toList());
            if (source != null) {
                List<Field> fieldsSource = Stream.of(source).collect(Collectors.toList());
                for (Field field : fieldsSource) {
                    fieldTarget.add(field);
                }
            }
            target = fieldTarget.toArray(new Field[fieldTarget.size()]);
            return target;
        }
        return target;
    }
}
