package com.imooc.coupon.executor.impl;

import com.imooc.coupon.constant.RuleFlag;
import com.imooc.coupon.executor.AbstractExecutor;
import com.imooc.coupon.executor.RuleExecutor;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 折扣优惠券结算规则执行器
 */
@Slf4j
@Component
public class ZheKouExecutor extends AbstractExecutor implements RuleExecutor {


    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.ZHEKOU;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlementInfo) {

        double goodsSum = retain2Decimals(goodsCostSum(settlementInfo.getGoodsInfos()));
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlementInfo, goodsSum);

        if (null != probability) {
            log.debug("ZheKou template is not match goodstype");
            return probability;
        }

        // 折扣优惠券可以直接使用， 没有门槛
        CouponTemplateSDK templateSDK = settlementInfo.getCouponAndTemplateInfos().get(0).getTemplateSDK();

        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        // 计算使用优惠券之后的价格
        settlementInfo.setCost(
                retain2Decimals((goodsSum * (quota * 1.0 / 100))) > minCost() ? (goodsSum * (quota * 1.0 / 100))
                        : minCost()
        );

        log.debug("use zhekou coupon make goods cost from {} to {}", goodsSum, settlementInfo.getCost());
        return settlementInfo;
    }
}
