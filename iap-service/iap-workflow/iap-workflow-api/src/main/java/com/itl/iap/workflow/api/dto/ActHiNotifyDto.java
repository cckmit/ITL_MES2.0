package com.itl.iap.workflow.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 抄送通知dto
 *
 * @author 罗霄
 * @date 2020-09-16
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class ActHiNotifyDto implements Serializable {
    private static final long serialVersionUID = 571804206739458176L;
    //分页对象
    private Page page;

    private Long id;
    /**
     * 执行器id
     */
    private String executionId;
    /**
     * 通知人id
     */
    private String notifyId;
    /**
     * 通知状态
     */
    private Integer notifyState;
    /**
     * 新增人
     */
    private String creater;
    /**
     * 新增时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String lastUpdateBy;
    /**
     * 修改时间
     */
    private Date lastUpdateDate;
}