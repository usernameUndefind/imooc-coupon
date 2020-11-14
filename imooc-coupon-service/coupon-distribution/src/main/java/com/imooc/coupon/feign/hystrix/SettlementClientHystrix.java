package com.imooc.coupon.feign.hystrix;

import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.feign.SettlementClient;
import com.imooc.coupon.vo.CommonResponse;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SettlementClientHystrix implements SettlementClient {

    @SuppressWarnings("all")
    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo settlementInfo) throws CouponException {
        log.error("[eureka-client-coupon-settlement] computeRule request error");

        settlementInfo.setEmploy(false);
        settlementInfo.setCost(-1.0);
        return new CommonResponse<>(-1, "[eureka-client-coupon-settlement] computeRule request error",
                settlementInfo);
    }
}
