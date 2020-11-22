package com.imooc.coupon.executor;

import com.imooc.coupon.constant.CouponCategory;
import com.imooc.coupon.constant.RuleFlag;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券结算规则执行管理器
 * Bean后置处理器
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class ExecuteManager implements BeanPostProcessor {


    // 规则执行器映射
    private static Map<RuleFlag, RuleExecutor> executorIndex = new HashMap<>(RuleFlag.values().length);

    /**
     * 优惠券结算规则计算入口
     * 注意
     * @param settlementInfo
     * @return
     * @throws CouponException
     */
    public SettlementInfo computeRule(SettlementInfo settlementInfo) throws CouponException {
        SettlementInfo result = null;

        // 单类优惠券
        if (settlementInfo.getCouponAndTemplateInfos().size() == 1) {
            CouponCategory category = CouponCategory.of(
              settlementInfo.getCouponAndTemplateInfos().get(0).getTemplateSDK().getCategory()
            );

            switch (category) {
                case MANJIAN:
                    result = executorIndex.get(RuleFlag.MANJIAN).computeRule(settlementInfo);
                    break;
                case ZHEKOU:
                    result = executorIndex.get(RuleFlag.ZHEKOU).computeRule(settlementInfo);
                    break;
                case LIJIAN:
                    result = executorIndex.get(RuleFlag.LIJIAN).computeRule(settlementInfo);
                    break;
            }
        } else {
            // 多类优惠券
            List<CouponCategory> categories = new ArrayList<>(settlementInfo.getCouponAndTemplateInfos().size());

            settlementInfo.getCouponAndTemplateInfos().forEach(ct -> {
                categories.add(CouponCategory.of(ct.getTemplateSDK().getCategory()));
            });

            if (categories.size() != 2) {
                throw new CouponException("not support for more template category");
            } else {
                if (categories.contains(CouponCategory.MANJIAN) && categories.contains(CouponCategory.ZHEKOU)) {
                    result = executorIndex.get(RuleFlag.MANJIAN_ZHEKOU).computeRule(settlementInfo);
                } else {
                    throw new CouponException("not support for other template category");
                }
            }
        }
        return result;
    }

    /**
     * bean初始化之前执行
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof RuleExecutor))
            return bean;

        RuleExecutor executor = (RuleExecutor) bean;
        RuleFlag ruleFlag = executor.ruleConfig();
        if (executorIndex.containsKey(ruleFlag)) {
            throw new IllegalStateException("there is already an executor for rule flag "+ ruleFlag);
        }

        log.info("load executor{} for rule flag {}", executor.getClass(), ruleFlag);

        executorIndex.put(ruleFlag, executor);
        return bean;
    }

    /**
     *
     * bean 初始化之后去执行
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
