package com.imooc.coupon.converter;

import com.imooc.coupon.constans.CouponStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * 优惠券状态枚举属性转换器
 */
@Convert
public class CouponStatusConverter implements AttributeConverter<CouponStatus, Integer> {


    @Override
    public Integer convertToDatabaseColumn(CouponStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer dbData) {
        return CouponStatus.of(dbData);
    }
}
