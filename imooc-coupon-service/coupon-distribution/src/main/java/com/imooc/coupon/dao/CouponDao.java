package com.imooc.coupon.dao;

import com.imooc.coupon.constans.CouponStatus;
import com.imooc.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * coupon dao接口定义
 */
public interface CouponDao extends JpaRepository<Coupon, Integer> {

    /**
     * 根据userId + 状态寻找优惠券
     * @param userId
     * @param status
     * @return
     */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
