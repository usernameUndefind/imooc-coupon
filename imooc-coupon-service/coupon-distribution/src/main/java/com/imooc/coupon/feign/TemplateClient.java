package com.imooc.coupon.feign;

import com.imooc.coupon.feign.hystrix.TemplateClientHystrix;
import com.imooc.coupon.vo.CommonResponse;
import com.imooc.coupon.vo.CouponTemplateSDK;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板微服务feign接口定义
 */
@FeignClient(value = "eureka-client-coupon-template", fallback = TemplateClientHystrix.class)
public interface TemplateClient {


    // 查找所有可用的优惠券模板
    @GetMapping("/coupon-template/template/sdk/all")
    CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate();

    // 获取模板ids到TemplateSDK的映射
    @GetMapping("/coupon-template/template/sdk/infos")
    CommonResponse<Map<Integer, CouponTemplateSDK>> findId2TemplateSDK(@RequestParam("ids") Collection<Integer> ids);
}
