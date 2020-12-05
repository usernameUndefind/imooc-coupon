package com.imooc.coupon.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 接口权限信息组装类定义
 */
@Data
@ToString
public class PermissionInfo {

    // controller 的url
    private String url;

    private String method;

    private Boolean isRead;

    private String description;

    private String extra;

}
