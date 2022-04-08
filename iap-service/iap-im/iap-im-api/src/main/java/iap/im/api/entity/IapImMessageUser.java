package iap.im.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 实体类-消息列表类（iap_im_message_user_t）
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_im_message_user_t")
@Accessors(chain = true)
public class IapImMessageUser implements Serializable {

    /**
     * 主键ID
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 当前用户（username）
     */
    @TableField("sender")
    private String sender;

    /**
     * 对方用户(username)
     */
    @TableField("receiver")
    private String receiver;

    /**
     * 显示状态（0：显示，1：不显示）
     */
    @TableField("show_type")
    private short showType;

    /**
     * 聊天类型（0：用户，1：群组）
     */
    @TableField("list_type")
    private short listType;

    /**
     * 置顶时间
     */
    @TableField("top_time")
    private long topTime;

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
