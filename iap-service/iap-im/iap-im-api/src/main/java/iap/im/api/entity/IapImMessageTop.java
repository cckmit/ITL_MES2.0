package iap.im.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类-消息置顶类（iap_im_message_top_t）
 *
 * @author 李骐光
 * @date 2020-10-15
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("iap_im_message_top_t")
public class IapImMessageTop implements Serializable {

    /**
     * 主键ID
     */
    @TableId
    private String id;

    /**
     * 消息ID
     */
    @TableField("message_id")
    private String messageId;

    /**
     * 消息列表ID
     */
    @TableField("message_list_id")
    private String messageListId;

    /**
     * 置顶人
     */
    @TableField("top_person")
    private String topPerson;

    /**
     * 置顶内容
     */
    @TableField("top_content")
    private String topContent;

    /**
     * 置顶时间
     */
    @TableField("top_timestamp")
    private long topTimestamp;

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
     * 最后更新时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;
}
