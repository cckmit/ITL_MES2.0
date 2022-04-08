package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 公告dto
 *
 * @author 李骐光
 * @date 2020-06-22
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IapSysNoticeTDto {
    /**
     * 分页
     */
    private Page page;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 最后修改人
     */
    private String lastUpdateBy;
    /**
     * 公告类别（1-求购动态，2-商友圈,3-公司动态)
     */
    private Short noticeCategory;
    /**
     * 公告类型(1-平台公告,3-公司新闻,51- 招标预告,52- 中标公告,53- 招标澄清,54- 询价通知,55-报价通知)
     */
    private Short noticeType;
    /**
     * 发布对象类别(0-草稿，1-首页公告,2-集团内部公告，3-仅供应商)
     */
    private Short publisherCategory;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态(0-新建，5-已发布,9-已删除)
     */
    private Short status;
    /**
     * 有效期至
     */
    private Date termEnd;
    /**
     * 有效期从
     */
    private Date termStart;
    /**
     * 公告标题
     */
    private String title;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;
    /**
     * 公司id(此字段有值时为企业公告)
     */
    private String companyId;
    /**
     * 集团id(此字段有值时为平台公告)
     */
    private String enterpriseId;

}
