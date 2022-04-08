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
 * (MsgPlaceholderType)实体类
 *
 * @author liaochengdian
 * @date  2020-04-07
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("msg_placeholder_type")
public class MsgPlaceholderType extends BaseModel {
    private static final long serialVersionUID = -66441578558090576L;
    
    /**
    * 主键
    */
    private String id;
    /**
    * 分类名称
    */
    private String name;
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

    @TableField(exist = false)
    private String msgPlaceholderNames;
    @TableField(exist = false)
    private List<MsgPlaceholder>  msgPlaceholderList;

}