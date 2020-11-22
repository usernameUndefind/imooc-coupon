package com.imooc.coupon.executor.impl;

import com.imooc.coupon.constant.RuleFlag;
import com.imooc.coupon.executor.AbstractExecutor;
import com.imooc.coupon.executor.RuleExecutor;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 立减优惠券计算规则执行器
 */
@Slf4j
@Component
public class LiJianExecutor extends AbstractExecutor implements RuleExecutor {


    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.LIJIAN;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlementInfo) {

        // 计算商品总价
        double goodsSum = retain2Decimals(goodsCostSum(settlementInfo.getGoodsInfos()));

        SettlementInfo probability = processGoodsTypeNotSatisfy(settlementInfo, goodsSum);

        if (null != probability) {
            log.debug("ManJian Template Is Not Match To GoodsType!");
            return probability;
        }

        // 判断满减是否符合折扣标准
        CouponTemplateSDK templateSDK  = settlementInfo.getCouponAndTemplateInfos().get(0).getTemplateSDK();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();


        // 计算使用优惠券之后的价格
        settlementInfo.setCost(
                retain2Decimals(Math.max((goodsSum - quota), minCost()))
        );

        log.debug("use lijian coupon make goods cost from {} to {}", goodsSum, settlementInfo.getCost());
        return settlementInfo;
    }
}
