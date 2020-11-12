package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 结算信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementInfo {

    private Long userId;

    private List<CouponAndTemplateInfo> couponAndTemplateInfos;

    private List<GoodsInfo> goodsInfos;

    private Double cost;

    // 是否是核销  true 核销 false  结算
    private Boolean employ;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CouponAndTemplateInfo {

        private Integer id;

        private CouponTemplateSDK templateSDK;
    }
}
