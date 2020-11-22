package com.imooc.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.executor.ExecuteManager;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 结算服务的controller
 */
@RestController
@Slf4j
public class SettlementController {

    @Autowired
    private ExecuteManager executeManager;

    /**
     * 优惠券结算
     * @param settlementInfo
     * @return
     * @throws CouponException
     */
    @PostMapping("/settlement/compute")
    public SettlementInfo computeRule(@RequestBody SettlementInfo settlementInfo) throws CouponException {
        log.info("settlement:{}", JSON.toJSONString(settlementInfo));
        return executeManager.computeRule(settlementInfo);
    }
}
