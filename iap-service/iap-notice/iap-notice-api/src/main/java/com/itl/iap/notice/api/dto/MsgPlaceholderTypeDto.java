package com.itl.iap.notice.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.notice.api.pojo.BaseRequestPojo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (MsgPlaceholderType)DTO类
 *
 * @author liaochengdian
 * @date  2020-04-07
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class  MsgPlaceholderTypeDto extends BaseRequestPojo implements Serializable {
    private static final long serialVersionUID = -28255853331624956L;
    //分页对象
    private Page page;
    
    /**
    * 主键
    */    private String id;
    /**
    * 分类名称
    */    private String name;
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
}