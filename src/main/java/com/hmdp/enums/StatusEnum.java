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
