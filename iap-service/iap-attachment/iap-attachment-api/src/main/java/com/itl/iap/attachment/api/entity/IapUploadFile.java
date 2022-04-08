package com.itl.iap.attachment.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 文件上传实体类
 *
 * @author 谭强
 * @date 2020-08-20
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_upload_file_t")
public class IapUploadFile extends BaseModel {
    private static final long serialVersionUID = -31915431782467210L;

    @TableId
    /**
     * id
     */
    @TableField("id")
    private String id;
    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 业务id
     */
    @TableField("business_id")
    private String businessId;
    /**
     * 文件大小
     */
    @TableField("file_size")
    private String fileSize;
    /**
     * 文件地址
     */
    @TableField("file_url")
    private String fileUrl;
    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;
    /**
     * 创建者
     */
    @TableField("creater")
    private String creater;
    /**
     * 最后修改人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
     * 最后修改时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 文件存储地址
     */
    @TableField("file_path")
    private String filePath;
    /**
     * 文件原名称
     */
    @TableField("file_old_name")
    private String fileOldName;
    /**
     * 分组
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 状态 （1被删除 0 正常）
     */
    @TableField("on_type")
    private Short onType;
    /**
     * 文件字节码
     */
    @TableField(exist = false)
    private byte[] bytes;

}