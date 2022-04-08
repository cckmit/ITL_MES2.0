package com.itl.mes.pp.provider.common;

import lombok.ToString;

@ToString
public enum CommonCode implements ResultCode{
    INVALID_PARAM(false,10003,"非法参数！"),
    SUCCESS(true,10000,"操作成功！"),
    FAIL(false,11111,"操作失败！"),
    STARTTIME_IS_NULL(false,10002,"计划开始时间不能为空"),
    STARTTIME_IS_LONG(false,10004,"计划开始时间不能大于T+7"),
    PARSE_TIME_FAIL(false,10003,"转化时间失败"),
    ITEM_ISEMPTY(false,10005,"物料不能为空"),
    ITEM_ONLY(false,10006,"绑定必须为同一物料"),
    ITEM_OUT(false,10007,"只能对工单计划开始时间 T+3日内进行锁定"),
    RESOURCE_OUT(false,10008,"请分配更多的资源日历！"),
    LOCK_NO_MOVE(false,10009,"有锁定订单不可移动！"),
    PRODUCT_TIME_EMPTY(false,10010,"生产时间不能为空！"),
    DELIVERY_TIME_EMPTY(false,10011,"交期不能为空！"),
    NO_NUMBER(false,10012,"预排产无数据！"),
    NO_RESOURCE(false,10013,"请先维护该产线的资源日历！"),
    NO_MORE(false,10014,"批次号不能重复！"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！");

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private CommonCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }
    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


}
