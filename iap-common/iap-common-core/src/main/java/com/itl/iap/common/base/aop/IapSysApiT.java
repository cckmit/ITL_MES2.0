package com.itl.iap.common.base.aop;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Objects;

/**
 * 接口实体类（iap_sys_api_t）表
 *
 * @author mjl
 * @date 2020-06-22 16:35
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IapSysApiT {

    /**
     * 主键id
     */
    private String id;

    /**
     * 系统代码
     */
    private String systemCode;

    /**
     * 模块名称
     */
    private String modelName;

    /**
     * 类名
     */
    private String className;

    /**
     * 类描述信息
     */
    private String classDesc;

    /**
     * 类url
     */
    private String classUrl;

    /**
     * 类中的方法名称
     */
    private String methodName;

    /**
     * 方法描述信息
     */
    private String methodDesc;

    /**
     * 方法url
     */
    private String methodUrl;

    /**
     * 请求方式
     */
    private String requestType;

    /**
     * 启用/禁用（0-启用， 1- 禁用）
     */
    private Short enabled;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;

    /**
     * 校验（0-已校验，1-未校验）
     */
    private Short checked;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 重写 equals方法，接口相同的定义为，服务名相同，全限定类名相同，方法名称相同，接口url相同，请求方法相同
     *
     * @param object
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof IapSysApiT)) {
            return false;
        }
        IapSysApiT that = (IapSysApiT) object;
        return Objects.equals(modelName, that.modelName) &&
                Objects.equals(className, that.className) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(methodUrl, that.methodUrl) &&
                Objects.equals(requestType, that.requestType);
    }

    /**
     * 重写hashCode()方法
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(modelName, className, methodName, methodUrl, requestType);
    }
}
