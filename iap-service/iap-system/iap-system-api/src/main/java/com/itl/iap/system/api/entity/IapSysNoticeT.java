package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.io.Serializable;

/**
 * 公告实体类
 *
 * @author 李骐光
 * @date 2020-06-22
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("iap_sys_notice_t")
public class IapSysNoticeT  implements Serializable {
    /**
    * 主键ID
    */
    @TableId(type = IdType.UUID)
    private String id;
    /**
    * 内容
    */
    @TableField("content")
    private String content;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_date")
    private Date createDate;
    /**
    * 创建人
    */
    @TableField("creater")
    private String creater;
    /**
    * 最后修改人
    */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
    * 公告类别（1-求购动态，2-商友圈,3-公司动态)
    */
    @TableField("notice_category")
    private Short noticeCategory;
    /**
    * 公告类型(1-平台公告,3-公司新闻,51- 招标预告,52- 中标公告,53- 招标澄清,54- 询价通知,55-报价通知)
    */
    @TableField("notice_type")
    private Short noticeType;
    /**
    * 发布对象类别(0-草稿，1-首页公告,2-集团内部公告，3-仅供应商)
    */
    @TableField("publisher_category")
    private Short publisherCategory;
    /**
    * 备注
    */
    @TableField("remark")
    private String remark;
    /**
    * 状态(0-新建，5-已发布,9-已删除)
    */
    @TableField("status")
    private Short status;
    /**
    * 有效期至
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("term_end")
    private Date termEnd;
    /**
    * 有效期从
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("term_start")
    private Date termStart;
    /**
    * 公告标题
    */
    @TableField("title")
    private String title;
    /**
    * 修改时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("last_Update_date")
    private Date lastUpdateDate;
    /**
    * 公司id(此字段有值时为企业公告)
    */
    @TableField("company_id")
    private String companyId;
    /**
    * 集团id(此字段有值时为平台公告)
    */
    @TableField("enterprise_id")
    private String enterpriseId;

}
