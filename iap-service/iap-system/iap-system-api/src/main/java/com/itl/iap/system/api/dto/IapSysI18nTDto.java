package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 国际化dto
 *
 * @author 李骐光
 * @date 2020-06-29
 * @since jdk1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IapSysI18nTDto {

    /**
     * 分页
     */
    Page page;

    /**
     * 主键
     */
    private String id;

    /**
     * 键
     */
    private String i18nKey;

    /**
     * 值
     */
    private String i18nValue;

    /**
     * 语言类型(中文:zh_CN/英文:en)
     */
    private String languageType;

    /**
     * 客户端类型(pc-web:电脑浏览器/app)
     */
    private String clientType;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 创建岗位
     */
    private String createOrg;

    /**
     * 最后更新人
     */
    private String lastUpdateBy;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;

    /**
     * 键对应的语言配置列表
     */
    private List<IapSysI18nTDto> multipleLanguagesList;

    /**
     * 新键
     */
    private String newI18nKey;

}
