package com.imooc.coupon.controller;

import com.alibaba.fastjson.JSONObject;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IBuildTemplateService;
import com.imooc.coupon.service.ITemplateBaseService;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.TemplateRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板相关的功能控制器
 */
@RestController
@AllArgsConstructor
@Slf4j
public class CouponTemplateController {

    // 优惠券模板服务
    private final IBuildTemplateService templateService;

    // 优惠券基础服务
    private final ITemplateBaseService templateBaseService;


    /**
     * 构建优惠券模板
     * @param request
     * @return
     * @throws CouponException
     */
    @PostMapping("/template/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateRequest request) throws CouponException {
        log.info("Build Template: {}", JSONObject.toJSONString(request));
        return templateService.buildTemplate(request);
    }

    /**
     * 构造优惠券模板详情
     * @param id
     * @return
     * @throws CouponException
     */
    @GetMapping("/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam("id") Integer id) throws CouponException {
        return templateBaseService.buildTemplateInfo(id);
    }

    /**
     * 查找所有可用的优惠券模板
     * @return
     */
    @GetMapping("/template/sdk/all")
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        return templateBaseService.findAllUsableTemplate();
    }


    /**
     *
     * @param ids
     * @return
     */
    @GetMapping("/template/sdk/infos")
    public Map<Integer, CouponTemplateSDK> findId2TemplateSDK(@RequestParam("ids") Collection<Integer> ids) {
        return templateBaseService.findIds2TemplateSDK(ids);
    }
}
