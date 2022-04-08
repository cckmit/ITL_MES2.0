package com.itl.iap.workflow.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 抄送通知实体类
 *
 * @author 罗霄
 * @date 2020-09-16
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("ACT_HI_NOTIFY")
public class ActHiNotify extends BaseModel {
    private static final long serialVersionUID = 793796431322199404L;

    @TableId("id")
    private Long id;
    /**
     * 执行器id
     */
    @TableField("execution_id")
    private String executionId;
    /**
     * 通知人id
     */
    @TableField("notify_id")
    private String notifyId;
    /**
     * 通知状态
     */
    @TableField("notify_state")
    private Integer notifyState;
    /**
     * 新增人
     */
    @TableField("creater")
    private String creater;
    /**
     * 新增时间
     */
    @TableField("create_date")
    private Date createDate;
    /**
     * 修改人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
     * 修改时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;

}