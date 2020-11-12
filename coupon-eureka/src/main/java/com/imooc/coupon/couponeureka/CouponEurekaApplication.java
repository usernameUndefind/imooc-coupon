package com.imooc.coupon.couponeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class CouponEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponEurekaApplication.class, args);
    }

}
