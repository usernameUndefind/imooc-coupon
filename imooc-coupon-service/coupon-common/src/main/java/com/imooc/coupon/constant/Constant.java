package com.imooc.coupon.constant;

public class Constant {

    // kafka 消息的 Topic
    public static final String TOPIC = "imooc_user_coupon_op";


    public static class RedisPrefix {

        // 优惠券码的优惠券前缀
        public static final String COUPON_TEMPLATE = "imooc_coupon_template_code_";


        // 用户当前所有可用的优惠券key前缀
        public static final String USER_COUPON_USABLE =
                "imooc_user_coupon_usable_";


        public static final String USER_COUPON_USED =
                "imooc_user_coupon_used_";

        public static final String USER_COUPON_EXPIRED =
                "imooc_user_coupon_expired_";


    }
}
