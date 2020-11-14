package com.imooc.coupon.feign.hystrix;

import com.imooc.coupon.feign.TemplateClient;
import com.imooc.coupon.vo.CommonResponse;
import com.imooc.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板feigh接口的熔断降级策略
 */
@Slf4j
@Component
public class TemplateClientHystrix implements TemplateClient {


    @SuppressWarnings("all")
    @Override
    public CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate() {
        log.error("[eureka-client-coupon-template] findAllUsableTemplate request error");
        return new CommonResponse<>(-1, "[eureka-client-coupon-template] findAllUsableTemplate request error",
                Collections.EMPTY_LIST);
    }

    @SuppressWarnings("all")
    @Override
    public CommonResponse<Map<Integer, CouponTemplateSDK>> findId2TemplateSDK(Collection<Integer> ids) {
        log.error("[eureka-client-coupon-template] findId2TemplateSDK request error");
        return new CommonResponse<>(-1, "[eureka-client-coupon-template] findId2TemplateSDK request error",
                Collections.EMPTY_MAP);
    }
}
