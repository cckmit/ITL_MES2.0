package com.itl.iap.common.base.serviceImpl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IMOM Service实现基类
 *
 * @author Linjl
 * @date 2020-01-28
 */
public abstract class ImomServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    //获取当前站点
    protected String getCurSite(){
        return UserUtils.getSite();
    }

    //判断是否站点内的BO
    protected boolean isInSite(String bo) {
        if (StringUtils.isBlank(bo))
            return true;

        String strPattern = ".[^:]+:";
        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(strPattern);
        // 现在创建 matcher 对象
        Matcher matcher = pattern.matcher(bo);
        if (matcher.find()) {
            return bo.replace(matcher.group(0), "").startsWith(this.getCurSite() + ",");
        }

        return true;
    }

}
