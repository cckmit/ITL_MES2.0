package com.itl.iap.common.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 常见异常定义类
 *
 * @author 汤俊
 * @date 2020-4-15 17:15
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonExceptionDefinition {

    public static final Integer LEVEL = 2;

    /**
     * 主数据
     */
    public static final Integer BASIC_EXCEPTION = 10000;

    /**
     * 组织架构
     */
    public static final Integer ORG_EXCEPTION = 11000;

    /**
     * 系统模块
     */
    public static final Integer SYS_EXCEPTION = 12000;

    /**
     * 工具
     */
    public static final Integer CORE_EXCEPTION = 13000;

    /**
     * 工作流
     */
    public static final Integer WORK_EXCEPTION = 14000;

    /**
     * 供应商
     */
    public static final Integer SUP_EXCEPTION = 15000;

    /**
     * 供应商绩效
     */
    public static final Integer PER_EXCEPTION = 16000;

    /**
     * 采购寻源
     */
    public static final Integer BID_EXCEPTION = 17000;

    /**
     * 采购池
     */
    public static final Integer POOL_EXCEPTION = 18000;

    /**
     * 合同
     */
    public static final Integer CON_EXCEPTION = 19000;

    /**
     * 采购执行
     */
    public static final Integer PUR_EXCEPTION = 20000;

    /**
     * 报表
     */
    public static final Integer FORM_EXCEPTION = 21000;

    /**
     * 公告
     */
    public static final Integer NOC_EXCEPTION = 22000;

    /**
     * 验证信息
     */
    public static final Integer VERIFY_EXCEPTION = 23000;

    /**
     * EXCEI导入导出
     */
    public static final Integer EXCEI_EXCEPTION = 24000;

    /**
     * SN编码规则
     */
    public static final Integer SN_ENCODING_EXCEPTION = 25000;
    /**
     * 修改时间验证信息
     */
    public static final Integer TIME_VERIFY_EXCEPTION = 26000;

    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String msg;


    /**
     * logInfo日志
     *
     * @return String
     */
    public static String getCurrentClassError() {
        return getCurrentClassName() + "类的" + getCurrentMethodName() + "方法的第" + getLineNumber() + "行====>";
    }

    /**
     * 获取方法名
     *
     * @return String
     */
    public static String getCurrentMethodName() {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        return stacks[LEVEL].getMethodName();
    }

    /**
     * 获取类名
     *
     * @return String
     */
    public static String getCurrentClassName() {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        return stacks[LEVEL].getClassName();
    }

    /**
     * 获取行号
     *
     * @return int
     */
    public static int getLineNumber() {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        return stacks[LEVEL].getLineNumber();
    }
}
