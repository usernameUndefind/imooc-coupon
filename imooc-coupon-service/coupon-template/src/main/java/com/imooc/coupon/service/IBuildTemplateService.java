package com.imooc.coupon.service;

import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.TemplateRequest;

public interface IBuildTemplateService {

    CouponTemplate buildTemplate(TemplateRequest request) throws CouponException;
}
