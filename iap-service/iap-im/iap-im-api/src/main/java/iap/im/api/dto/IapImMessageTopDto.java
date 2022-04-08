package iap.im.api.dto;

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
 * DTO类-消息置顶类（iap_im_message_top_t）
 *
 * @author 李骐光
 * @date 2020-10-15
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IapImMessageTopDto implements Serializable {
    private String id;

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 消息列表id
     */
    private String messageListId;

    /**
     * 置顶人
     */
    private String topPerson;

    /**
     * 置顶内容
     */
    private String topContent;

    /**
     * 置顶时间
     */
    private long topTimestamp;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最后修改人
     */
    private String lastUpdateBy;

    /**
     * 最后修改时间
     */
    private Date lastUpdateDate;
}
