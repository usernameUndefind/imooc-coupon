package com.imooc.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IUserService;
import com.imooc.coupon.vo.AcquireTemplateRequest;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务controller
 */
@RestController
@Slf4j
public class UserServiceController {

    @Autowired
    private IUserService service;


    @GetMapping("/coupons")
    public List<Coupon> findCouponsByStatus(@RequestParam("userId") Long userId,
                                            @RequestParam("status") Integer status) throws CouponException {
        log.info("find coupons by status: {}, {}", userId, status);
        return service.findCouponsByStatus(userId, status);
    }

    /**
     * 根据用户id查找当前可以领取的优惠券模板
     * @param userId
     * @return
     */
    @GetMapping("template")
    public List<CouponTemplateSDK> findAvailableTemplate(@RequestParam("userId") Long userId) throws CouponException {
        log.info("find available template: {}", userId);
        return service.findAvailableTemplate(userId);
    }


    /**
     * 用户领取优惠券
     * @param request
     * @return
     * @throws CouponException
     */
    @PostMapping("/acquire/template")
    public Coupon acquireTemplate(@RequestBody AcquireTemplateRequest request) throws CouponException {
        log.info("acquire template : {}", JSON.toJSONString(request));
        return service.acquireTemplate(request);
    }

    /**
     * 结算优惠券
     * @param settlementInfo
     * @return
     * @throws CouponException
     */
    @PostMapping("settlement")
    public SettlementInfo settlement(@RequestBody SettlementInfo settlementInfo) throws CouponException {
        log.info("settlement: {}", JSON.toJSONString(settlementInfo));
        return service.settlement(settlementInfo);
    }
}
