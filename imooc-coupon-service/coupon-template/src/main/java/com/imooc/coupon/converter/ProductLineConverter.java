package com.imooc.coupon.converter;

import com.imooc.coupon.constant.CouponCategory;
import com.imooc.coupon.constant.ProductLine;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProductLineConverter implements AttributeConverter<ProductLine, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProductLine productLine) {
        return productLine.getCode();
    }

    // 将数据库中的字段Y转换为实体属性X
    @Override
    public ProductLine convertToEntityAttribute(Integer s) {
        return ProductLine.of(s);
    }
}
