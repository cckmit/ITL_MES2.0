package com.itl.iap.attachment.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件上传dto
 *
 * @author 谭强
 * @date 2020-08-20
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class IapUploadFileDto implements Serializable {
    private static final long serialVersionUID = 112235816307748188L;
    // 状态 0 正常 1 被删除
    public static final short ON_TYPE_0 = 0;
    public static final short ON_TYPE_1 = 1;
    //分页对象
    private Page page;

    private String id;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 业务id
     */
    private String businessId;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 文件地址
     */
    private String fileUrl;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建者组织
     */
    private String creater;
    /**
     * 最后修改人
     */
    private String lastUpdateBy;
    /**
     * 最后修改时间
     */
    private Date lastUpdateDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态 （1被删除 0 正常）
     */
    private Short onType;

    /**
     * 文件存储地址
     */
    private String filePath;
    /**
     * 文件原名称
     */
    private String fileOldName;

}