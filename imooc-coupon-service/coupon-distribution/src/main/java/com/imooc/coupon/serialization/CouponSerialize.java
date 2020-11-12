package com.imooc.coupon.serialization;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.imooc.coupon.entity.Coupon;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 优惠券实体类自定义序列化器
 */
public class CouponSerialize extends JsonSerializer<Coupon> {


    @Override
    public void serialize(Coupon coupon, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

         // 开始序列化
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", coupon.getId().toString());
        jsonGenerator.writeStringField("templateId", coupon.getTemplateId().toString());
        jsonGenerator.writeStringField("userId", coupon.getUserId().toString());
        jsonGenerator.writeStringField("couponCode", coupon.getCouponCode());
        jsonGenerator.writeStringField("assignTime",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(coupon.getAssignTime()));

        jsonGenerator.writeStringField("name", coupon.getTemplateSDK().getName());
        jsonGenerator.writeStringField("logo", coupon.getTemplateSDK().getLogo());
        jsonGenerator.writeStringField("desc", coupon.getTemplateSDK().getDesc());
        jsonGenerator.writeStringField("expiration", JSON.toJSONString(
                coupon.getTemplateSDK().getRule().getExpiration()
        ));
        jsonGenerator.writeStringField("discount", JSON.toJSONString(
                coupon.getTemplateSDK().getRule().getDiscount()
        ));
        jsonGenerator.writeStringField("usage", JSON.toJSONString(
                coupon.getTemplateSDK().getRule().getUsage()
        ));

        jsonGenerator.writeEndObject();
    }
}
