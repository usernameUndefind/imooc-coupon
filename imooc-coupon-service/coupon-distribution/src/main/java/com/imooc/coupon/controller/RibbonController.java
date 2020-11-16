package com.imooc.coupon.controller;

import com.imooc.coupon.annotation.IgnoreResponseAdvise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class RibbonController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 通过Ribbon组件调用模板微服务
     * @return
     */
    @GetMapping("/info")
    @IgnoreResponseAdvise
    private TemplateInfo getTemplate() {
        String infoUrl = "http://eureka-client-coupon-template/coupon-template/info";
        return restTemplate.getForEntity(infoUrl, TemplateInfo.class).getBody();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TemplateInfo {

        private Integer code;
        private String message;
        private List<Map<String, Object>> data;
    }
}
