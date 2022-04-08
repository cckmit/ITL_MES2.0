package com.itl.iap.mes.provider.common;

import lombok.ToString;

@ToString
public enum CommonCode implements ResultCode {
    INVALID_PARAM(false, 10003, "非法参数！"),
    SUCCESS(true, 10000, "操作成功！"),
    FAIL(false, 11111, "操作失败！"),
    UNAUTHENTICATED(false, 10001, "此操作需要登陆系统！"),
    UNAUTHORISE(false, 10002, "权限不足，无权操作！"),
    SCHEDULER_EXCEPTION(false, 10003, "初始化SCHEDULER失败！"),
    CODE_REPEAT(false, 10004, "编码不能重复！"),
    CODE_EMPTY(false, 10005, "编码不能为空！"),
    PARSE_TIME_FAIL(false, 10006, "转化时间失败！"),
    TIME_FAIL(false, 10007, "开始时间不能小于当前时间！"),
    FILE_UPLOAD_FAIL(false, 10021, "上传文件错误!"),
    FILE_SIZE_BIG(false, 10022, "文件太大，请上传10M以内的文件!"),
    FILE_EMPTY(false, 10023, "文件为空！"),
    FILE_DOWN_FAIL(false, 10024, "下载失败！"),
    IS_NOT_NUM(false, 10025, "excel格式错误！"),
    CON_FAIL(false, 10026, "连接数据库异常！"),
    JASPER_FAIL(false, 10027, "模板或参数错误！"),
    PRINT_FAIL(false, 10028, "打印失败！"),
    IO_FAIL(false, 10029, "图片地址错误！"),
    TYPE_REPEAT(false, 10030, "类型不能重复！"),
    SERVER_ERROR(false, 99999, "抱歉，系统繁忙，请稍后重试！"),
    DATA_REPEAT(false, 10031, "数据已存在"),
    LABEL_REPEAT(false, 10032, "该标签类型中已存在对应标签!"),
    SN(false, 10033, "Sn条码未通过验证"),
    EXECUTE_FAIL(false, 10034, "还未到保养执行时间"),
    TIME_FAILE(false, 10035, "开始时间和结束时间不能为空!"),
    DEVICE_REPEAT(false, 10036, "该点检计划下已有此设备类型"),
    CHOICE_DELETE(false, 10037, "请选择要删除的信息"),
    UPKEEP_CREATE(false, 10038, "该设备类型未创建保养计划，请创建保养计划或选择其他设备类型"),
    UPKEEP_STATE(false, 10039, "该设备对应保养计划未启用,请启用"),
    CHECK_CREATE(false, 10040, "该设备类型未创建点检计划，请创建点检计划或选择其他设备类型"),
    CHECK_STATE(false, 10041, "该设备对应点检计划未启用,请启用"),
    CHECK_REPEAT(false, 10042, "该保养计划下已有此设备类型"),
    NUMBER_ERROR(false, 10043, "周期不能小于或者小于0"),
    START_END(false, 10044, "开始时间不能大于结束时间"),
    NEXTTIME_ERROR(false, 10045, "下一次定时任务开始时间,不能大于计划结束时间"),
    LABEL_CHOOSE(false,10046,"请选择标签模板");
    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    private CommonCode(boolean success, int code, String message) {
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
