package com.itl.iap.notice.api.dto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.notice.api.pojo.BaseRequestPojo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息类型表(MsgType)DTO类
 *
 * @author liaochengdian
 * @date  2020-03-25
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class  MsgTypeDto extends BaseRequestPojo implements Serializable {
    private static final long serialVersionUID = 570702225733441741L;
    //分页对象
    private Page page;
    
    /**
    * 主键
    */    private String id;
    /**
    * 类型编码
    */    private String code;
    /**
    * 类型名称
    */    private String name;
    /**
    * 父id
    */    private String parentId;
    /**
    * 创建人
    */    private String createName;
    /**
    * 创建时间
    */    private Date createTime;
    /**
    * 更新人
    */    private String updateName;
    /**
    * 更新时间
    */    private Date updateTime;


    /**
     * 接收人主键
     */
    private String receiverUid;
}