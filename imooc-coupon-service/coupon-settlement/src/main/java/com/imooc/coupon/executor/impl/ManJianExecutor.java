package com.imooc.coupon.executor.impl;

import com.imooc.coupon.constant.RuleFlag;
import com.imooc.coupon.executor.AbstractExecutor;
import com.imooc.coupon.executor.RuleExecutor;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 满减优惠券结算规则执行器
 */
@Component
@Slf4j
public class ManJianExecutor extends AbstractExecutor implements RuleExecutor {


    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN;
    }

    @Override
    @SuppressWarnings("all")
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
        double base = (double) templateSDK.getRule().getDiscount().getBase();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        // 如果不符合标准， 则直接返回商品总价
        if (goodsSum < base) {
            log.debug("Current Goods Cost Sum < ManJian Coupon Base!");
            settlementInfo.setCost(goodsSum);
            settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
            return settlementInfo;
        }

        // 计算使用优惠券之后的价格
        settlementInfo.setCost(retain2Decimals(
                Math.max((goodsSum - quota), minCost())
        ));

        log.debug("use manjian coupon make goods cost from {} to {}", goodsSum, settlementInfo.getCost());

        return settlementInfo;
    }
}
