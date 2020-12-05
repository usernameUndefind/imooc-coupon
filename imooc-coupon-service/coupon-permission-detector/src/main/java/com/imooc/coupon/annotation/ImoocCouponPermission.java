package com.imooc.coupon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImoocCouponPermission {

    String description() default "";

    // 此接口是否为只读， 默认是true
    boolean readOnly() default true;

    // 扩展属性
    String extra() default "";
}
