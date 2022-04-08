package com.itl.iap.workflow.api.entity;

import lombok.Data;
import org.camunda.bpm.engine.impl.persistence.entity.CommentEntity;

/**
 * @author 汤俊
 * @date 2020-07-13
 * @since jdk1.8
 */
@Data
public class BusinessCommentEntity extends CommentEntity {

    /**
     * 节点名称
     */
    private String taskName;

    /**
     * 当前处理人
     */
    private String assign;

    /**
     * 操作类型 approve:同意  reject:拒绝
     */
    private String opType;

}
