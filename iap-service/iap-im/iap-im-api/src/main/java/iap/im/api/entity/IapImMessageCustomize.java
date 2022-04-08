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
 * 实体类-自定义消息内容回复（iap_im_customize_t）
 *
 * @author tanq
 * @date 2020/10/25
 * @since jdk1.8
 */
@Data
@TableName("iap_im_customize_t")
@Accessors(chain = true)
public class IapImMessageCustomize implements Serializable {
    private transient static final long serialVersionUID = 4733464888738356502L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 自定义消息
     */
    @TableField("customize_message")
    private String customizeMessage;

    /**
     * 回复消息排序
     */
    @TableField("customize_short")
    private Integer customizeShort;

    /**
     * 关联自定义配置
     */
    @TableField("setting_id")
    private String settingId;

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
