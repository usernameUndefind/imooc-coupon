package com.imooc.coupon.service;

import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;

import java.util.List;

/**
 * redis 相关服务接口定义
 */
public interface IRedisService {

    /**
     * 根据userid和状态找到缓存的优惠券列表数据
     * @param userId
     * @param status
     * @return
     */
    List<Coupon> getCachedCoupons(Long userId, Integer status);


    /**
     * 保存空的优惠券列表到缓存中
     * @param userId
     * @param status
     */
    void saveEmptyCouponListToCache(Long userId, List<Integer> status);

    /**
     * 尝试从cache中获取一个优惠券码
     * @param templateId
     * @return
     */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * 将优惠券保存到cache中
     * @param userId
     * @param coupons
     * @param status
     * @return
     * @throws CouponException
     */
    Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException;

}
