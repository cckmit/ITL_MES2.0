package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
/**
 * 操作变动明细日志(IapChangeDetailLogT)DTO类
 *
 * @author linjs
 * @since 2020-10-30 11:06:41
 */
@Data
@Accessors(chain = true)
public class  IapChangeDetailLogTDto implements Serializable {
    private static final long serialVersionUID = -34014069883486752L;
    //分页对象
    private Page page;

    /**
     * 主键ID
     */
          private String id;
    /**
    * 操作变动日志ID
    */    private String changeLogId;
    /**
    * 字段名
    */    private String fieldCode;
    /**
    * 字段描述
    */    private String fieldName;
    /**
    * 旧值
    */    private String oldValue;
    /**
    * 新值
    */    private String newValue;
    /**
    * 操作人员
    */    private String creater;
    /**
    * 操作日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;
    /**
    * 更新操作人员
    */    private String lastUpdateBy;
    /**
    * 更新操作日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastUpdateDate;
}