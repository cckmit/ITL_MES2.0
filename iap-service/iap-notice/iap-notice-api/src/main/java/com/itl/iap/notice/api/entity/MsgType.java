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
 * 消息类型表(MsgType)实体类
 *
 * @author liaochengdian
 * @date  2020-03-25
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("msg_type")
public class MsgType extends BaseModel {
    private static final long serialVersionUID = -91573585666602223L;
    
    /**
    * 主键
    */    private String id;
    /**
    * 类型编码
    */
    private String code;
    /**
    * 类型名称
    */
    private String name;
    /**
    * 父id
    */
    @TableField("parent_id")
    private String parentId;
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
    private List<MsgType> subModules;


}