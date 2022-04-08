package iap.im.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类-用户IM配置类（iap_im_setting_t）
 *
 * @author tanq
 * @date 2020/10/25
 * @since jdk1.8
 */
@Data
@TableName("iap_im_setting_t")
@Accessors(chain = true)
public class IapImMessageSetting implements Serializable {

    private transient static final long serialVersionUID = 4733464888738356502L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 是否开启自动回复功能(0:关闭,1: 开启)
     */
    @TableField("auto_type")
    private Short autoType;

    /**
     * 回复时间
     */
    @TableField("timestamp")
    private Long timestamp;

    /**
     * 回复时间类型（0：秒，1：分，2：时）
     */
    @TableField("time_type")
    private Short timeType;

    /**
     * 回复频率
     */
    @TableField("frequency")
    private Integer frequency;

    /**
     * 关联用户
     */
    @TableField("user_id")
    private String userId;

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;

}
