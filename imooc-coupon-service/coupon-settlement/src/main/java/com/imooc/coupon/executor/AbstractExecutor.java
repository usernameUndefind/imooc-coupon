package com.imooc.coupon.executor;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.vo.GoodsInfo;
import com.imooc.coupon.vo.SettlementInfo;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则执行器抽象类, 定义通用方法
 */
public abstract class AbstractExecutor {

    /**
     * 校验商品类型与优惠券是否匹配
     * @param settlementInfo
     * @return
     */
    @SuppressWarnings("all")
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlementInfo) {
        List<Integer> goodsType = settlementInfo.getGoodsInfos()
                .stream().map(GoodsInfo::getType)
                .collect(Collectors.toList());

        List<Integer> templateGoodsType = JSON.parseObject(settlementInfo.getCouponAndTemplateInfos()
        .get(0).getTemplateSDK().getRule().getUsage().getGoodsType(), List.class);

        // 是子集就返回true否则 false
        return CollectionUtils.isNotEmpty(
                CollectionUtils.intersection(goodsType, templateGoodsType)
        );
    }

    /**
     * 处理商品类型与优惠券限制不匹配的情况
     * @param settlementInfo
     * @param goodsSum
     * @return
     */
    protected SettlementInfo processGoodsTypeNotSatisfy(SettlementInfo settlementInfo, double goodsSum) {

        boolean isGoodsTypeSatisfy = isGoodsTypeSatisfy(settlementInfo);

        if (!isGoodsTypeSatisfy) {
            settlementInfo.setCost(goodsSum);
            settlementInfo.setCouponAndTemplateInfos(Collections.emptyList());
            return settlementInfo;
        }

        return null;
    }

    /**
     * 商品总价
     * @param goodsInfos
     * @return
     */
    protected double goodsCostSum(List<GoodsInfo> goodsInfos) {
        return goodsInfos.stream().mapToDouble(g -> g.getPrice() * g.getCount()).sum();
    }

    protected double retain2Decimals(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    protected double minCost() {
        return 0.1;
    }
}
