package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 有效期类型
 */
@Getter
@AllArgsConstructor
public enum PeriodType {

    REGULAR("固定日期", 1),
    SHIFT("变动日期（以领取之日开始计算）", 1);

    // 有效期描述
    private String description;

    // 优惠券编码
    private Integer code;



    public static PeriodType of(Integer code) {
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
