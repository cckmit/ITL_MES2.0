package com.itl.iap.notice.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * (MsgPlaceholder)实体类
 *
 * @author liaochengdian
 * @date 2020-04-07
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("msg_placeholder")
public class MsgPlaceholder extends BaseModel {
    private static final long serialVersionUID = 819609707215507954L;
    
    /**
    * 主键
    */
    private String id;
    /**
    * 占位符类型主键
    */
    @TableField("msg_placeholder_type_id")
    private String msgPlaceholderTypeId;
    /**
    * 占位符名称
    */
    private String name;
    /**
    * 占位符描述
    */
    private String note;
    /**
    * 排序
    */
    private Integer sort;
    /**
    * 创建人
    */
    @TableField("create_name")
    private String createName;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;
    /**
    * 更新人
    */
    @TableField("update_name")
    private String updateName;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh", timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime;
    /**
     * 类型字典名称
     */
    @TableField("type_name")
    private String typeName;
    /**
     * 子树
     */
    @TableField(exist = false)
    private List<MsgPlaceholder> subModules;

}