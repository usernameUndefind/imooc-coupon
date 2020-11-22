package com.imooc.coupon.executor.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constant.CouponCategory;
import com.imooc.coupon.constant.RuleFlag;
import com.imooc.coupon.executor.AbstractExecutor;
import com.imooc.coupon.executor.RuleExecutor;
import com.imooc.coupon.vo.GoodsInfo;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 满减和折扣优惠券执行器
 */
@Slf4j
@Component
public class ManJianZheKouExecutor extends AbstractExecutor implements RuleExecutor {

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN_ZHEKOU;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlementInfo) {

        double goodsSum = retain2Decimals(goodsCostSum(settlementInfo.getGoodsInfos()));

        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlementInfo, goodsSum
        );

        if (probability != null) {
            log.debug("manjian and zhekou template is not match to goodsType");
            return probability;
        }

        SettlementInfo.CouponAndTemplateInfo manJian = null;
        SettlementInfo.CouponAndTemplateInfo zheKou = null;

        for (SettlementInfo.CouponAndTemplateInfo ct : settlementInfo.getCouponAndTemplateInfos()) {
            if (CouponCategory.of(ct.getTemplateSDK().getCategory()) == CouponCategory.MANJIAN) {
                manJian = ct;
            } else {
                zheKou = ct;
            }
        }

        assert null != manJian;
        assert null != zheKou;

        // 当前的优惠券和满减券如果不能共用， 清空优惠券，返回商品原价
        if (!isTemplateCanShare(manJian, zheKou)) {
            log.debug("current manjian and zhekou can not shared");
            settlementInfo.setCost(goodsSum);
            settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
            return settlementInfo;
        }

        // 计算满减
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = new ArrayList<>();
        double manJianBase = (double) manJian.getTemplateSDK().getRule().getDiscount().getBase();
        double manJianQuota = (double) manJian.getTemplateSDK().getRule().getDiscount().getQuota();

        // 最终的价格
        double targetSum = goodsSum;
        if (targetSum >= manJianBase) {
            targetSum -= manJianQuota;
            ctInfos.add(manJian);
        }


        // 在计算折扣
        double zheKouQuota = (double) zheKou.getTemplateSDK().getRule().getDiscount().getQuota();

        targetSum *= zheKouQuota * 1.0 / 100;
        ctInfos.add(zheKou);

        settlementInfo.setCouponAndTemplateInfos(ctInfos);
        settlementInfo.setCost(retain2Decimals(Math.max(targetSum, minCost())));

        log.debug("use manjian and zhekou coupon make goods cost from {} to {}", goodsSum, settlementInfo.getCost());

        return settlementInfo;
    }

    /**
     * 当前的两张优惠券是否可以共用
     * 校验TemplateRule中的weight是否满足条件
     * @param manJian
     * @param zheKou
     * @return
     */
    @SuppressWarnings("all")
    private boolean isTemplateCanShare(SettlementInfo.CouponAndTemplateInfo manJian,
                                       SettlementInfo.CouponAndTemplateInfo zheKou) {

        String manjianKey = manJian.getTemplateSDK().getKey() + String.format("%04d", manJian
        .getTemplateSDK().getId());
        String zhekouKey = zheKou.getTemplateSDK().getKey() + String.format("%04d", zheKou
        .getTemplateSDK().getId());

        List<String> allSharedKeyForManjian = new ArrayList<>();
        allSharedKeyForManjian.add(manjianKey);
        allSharedKeyForManjian.addAll(JSON.parseObject(
                manJian.getTemplateSDK().getRule().getWeight(), List.class
        ));

        List<String> allSharedKeyForZheKou = new ArrayList<>();
        allSharedKeyForManjian.add(zhekouKey);
        allSharedKeyForManjian.addAll(JSON.parseObject(
                zheKou.getTemplateSDK().getRule().getWeight(), List.class
        ));

        return CollectionUtils.isSubCollection(Arrays.asList(manjianKey, zhekouKey), allSharedKeyForManjian)
                || CollectionUtils.isSubCollection(Arrays.asList(manjianKey, zhekouKey), allSharedKeyForZheKou
        );
    }

    /**
     * 满减 + 折扣
     * @param settlementInfo
     * @return
     */
    @Override
    @SuppressWarnings("all")
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlementInfo) {
        log.debug("check manjian and zhekou is match or not");

        List<Integer> goodsType = settlementInfo.getGoodsInfos().stream().map(GoodsInfo::getType)
                .collect(Collectors.toList());

        List<Integer> templateGoodsType = new ArrayList<>();

        settlementInfo.getCouponAndTemplateInfos().forEach(ct -> {
           templateGoodsType.add(JSON.parseObject(ct.getTemplateSDK().getRule().getUsage().getGoodsType(),
                   Integer.class));
        });

        // 如果想要使用多类优惠券， 则必须要所有的商品类型都要包含在内， 即差集为空
        return CollectionUtils.isEmpty(CollectionUtils.subtract(goodsType, templateGoodsType));
    }


}
