package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 接口管理dto
 *
 * @author 马家伦
 * @date 2020-06-19
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IapSysApiTDto {
    private Page page;

    /**
     * 主键id
     */
    @TableId
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

}
