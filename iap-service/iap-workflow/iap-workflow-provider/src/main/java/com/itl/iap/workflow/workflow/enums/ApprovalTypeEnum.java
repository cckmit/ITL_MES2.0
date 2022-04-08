package com.itl.iap.workflow.workflow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * 审批类型枚举
 *
 * @author 黄建明
 * @date 2020-07-06
 * @since jdk1.8
 */
@Getter
@AllArgsConstructor
public enum ApprovalTypeEnum {

    /**
     * 指定用户
     */
    SPECIFIED_USER("1", "指定用户"),

    /**
     * 角色
     */
    ROLE("2", "角色"),

    /**
     * 岗位
     */
    JOBS("3", "岗位"),

    /**
     * 当前操作者
     */
    CURRENT("4", "当前操作者");

    private String code;

    private String desc;


    /**
     * 通过value取枚举
     *
     * @param value
     */
    public static ApprovalTypeEnum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (ApprovalTypeEnum enums : ApprovalTypeEnum.values()) {
            if (enums.getCode().equals(value)) {
                return enums;
            }
        }
        return null;
    }

}

