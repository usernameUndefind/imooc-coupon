package com.imooc.coupon.service;

import com.imooc.coupon.entity.CouponTemplate;

public interface IAsyncService {


    // 根据模板异步的创建优惠券码
    void asyncConstructCouponByTemplate(CouponTemplate template);
}
