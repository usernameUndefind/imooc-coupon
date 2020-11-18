package com.imooc.coupon.service;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constans.CouponStatus;
import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 用户服务功能测试用例环境
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

     private Long fakeUserId = 2001L;

     @Autowired
     private IUserService userService;

     @Test
     public void testFindCouponByStatus() throws CouponException {
         System.out.println(JSON.toJSONString(userService.
                 findCouponsByStatus(fakeUserId, CouponStatus.USABLE.getCode())));


     }
}
