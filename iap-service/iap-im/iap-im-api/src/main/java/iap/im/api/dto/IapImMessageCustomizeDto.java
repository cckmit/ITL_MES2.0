package iap.im.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO类-自定义消息内容回复（iap_im_customize_t）
 *
 * @author tanq
 * @date 2020/10/25
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class IapImMessageCustomizeDto implements Serializable {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 自定义消息
     */
    private String customizeMessage;

    /**
     * 回复消息排序
     */
    private Integer customizeShort;

    /**
     * 关联自定义配置
     */
    private String settingId;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    private Date lastUpdateDate;

    private List<IapImMessageCustomizeDto> customizeDtoList;


}
