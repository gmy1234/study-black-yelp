package com.hmdp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gmydl
 * @title: StatusEnum
 * @projectName yelp
 * @description: TODO
 * @date 2023/4/20 15:15
 */
@AllArgsConstructor
@Getter
public enum StatusEnum implements CommonEnum {

    SUCCESS(0, "成功"),
    NOT_PHONE(1, "手机号不符合"),
    NOT_PHONE_CODE(2, "该手机号的验证码错误"),
    NOT_USER(401, "没有该用户信息"),
    NOT_SHOP(3, "没有该店铺信息"),
    NOT_SHOP_ID(5, "该店铺ID信息查不到"),



    DATA_CHANGED(10, "数据已被修改"),
    DATA_NOT_EXIST(11, "数据不存在"),
    DATA_WRONG(12, "未知错误"),
    INVALID_DATA(13, "数据无效"),
    PARAM_ERROR(14, "参数异常"),
    ERROR_NOT_FOUND(15, "资源不存在"),
    PARAM_MISS(16, "参数[s%]错误"),
    DATA_MAPPER_FAILD(17, "数据转化失败"),
    JSON_CONVERT_FAILED(18, "Json转换失败"),
    REST_CLIENT_ERROR(19, "feign请求调用失败"),
    DATE_FORMAT_ERROR(20, "日期转换异常"),
    PAGE_PARAM_NULL(21, "分页参数为空"),
    NO_LOGIN(22, "请先登录"),
    ASYNC_ERROR(23, "异步任务异常"),
    PARAM_NULL(14, "参数为空"),
    EXCEL_EXPORT_ERROR(25, "excel导出失败"),
    FILE_STREAM_ERROR(26, "文件流获取异常"),
    INVOKE_ERROR(27, "反射异常"),
    CLASS_ILLEGAL_ACCESS_ERROR(28, "非法访问"),
    USERID_ERROR(29, "用户id获取失败"),
    GET_FACTORY_ERROR(30, "获取电厂错误"),
    THREAD_SLEEP_ERROR(31, "线程睡眠异常"),
    FACTORY_CACHE_ERROR(32, "电厂缓存异常"),
    FILE_STREAM_CLOSE_ERROR(33, "文件流关闭异常"),
    ENCODING_ERROR(34, "URL 转义异常"),

    last(1000, "?");
    /**
     * 状态码
     */
    private final Integer index;
    /**
     * 状态描述
     */
    private final String desc;
}
