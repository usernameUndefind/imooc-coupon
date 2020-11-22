package com.imooc.coupon.service;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constant.CouponCategory;
import com.imooc.coupon.constant.GoodsType;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.executor.ExecuteManager;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.GoodsInfo;
import com.imooc.coupon.vo.SettlementInfo;
import com.imooc.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ExecuteManagerTest {

    private Long fakeUserId = 2000L;

    @Autowired
    private ExecuteManager manager;

    @Test
    public void testComputeRule() throws CouponException {
//        // 满减优惠券结算测试
//        log.info("manjian coupon executor test");
//        SettlementInfo manjianInfo = fakeManJianCouponSettlement();
//        SettlementInfo result = manager.computeRule(manjianInfo);
//
//        log.info("{}", result.getCost());
//        log.info("{}", result.getCouponAndTemplateInfos().size());
//        log.info("{}", result.getCouponAndTemplateInfos());


//        // 折扣优惠券结算测试
//        log.info("zhekou coupon executor test");
//        SettlementInfo zhekouInfo = fakeZheKouCouponSettlement();
//        SettlementInfo result = manager.computeRule(zhekouInfo);
//
//        log.info("{}", result.getCost());
//        log.info("{}", result.getCouponAndTemplateInfos().size());
//        log.info("{}", result.getCouponAndTemplateInfos());

//        // 立减优惠券结算测试
//        log.info("lijian coupon executor test");
//        SettlementInfo lijianInfo = fakeLiJianCouponSettlement();
//        SettlementInfo result = manager.computeRule(lijianInfo);
//
//        log.info("{}", result.getCost());
//        log.info("{}", result.getCouponAndTemplateInfos().size());
//        log.info("{}", result.getCouponAndTemplateInfos());

        // 立减优惠券结算测试
        log.info("lijian coupon executor test");
        SettlementInfo manjianzhekouInfo = fakeManJianZheKouCouponSettlement();
        SettlementInfo result = manager.computeRule(manjianzhekouInfo);

        log.info("{}", result.getCost());
        log.info("{}", result.getCouponAndTemplateInfos().size());
        log.info("{}", result.getCouponAndTemplateInfos());
    }

    /**
     * fake 满减优惠券结算信息
     * @return
     */
    private SettlementInfo fakeManJianCouponSettlement() {
        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setEmploy(false);
        info.setCost(0.0);

        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setCount(2);
        goodsInfo.setPrice(10.88);
        goodsInfo.setType(GoodsType.WENYU.getCode());

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(5);
        goodsInfo01.setPrice(20.88);
        goodsInfo01.setType(GoodsType.WENYU.getCode());

        info.setGoodsInfos(Arrays.asList(goodsInfo, goodsInfo01));

        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();

        ctInfo.setId(1);

        CouponTemplateSDK templateSDK = new CouponTemplateSDK();
        templateSDK.setId(1);
        templateSDK.setCategory(CouponCategory.MANJIAN.getCode());
        templateSDK.setKey("100120190801");

        TemplateRule rule = new TemplateRule();
        rule.setDiscount(new TemplateRule.Discount(20, 199));
        rule.setUsage(new TemplateRule.Usage("湖北省", "汉川市", JSON.toJSONString(
                Arrays.asList(GoodsType.WENYU.getCode(),
                        GoodsType.JIAJU.getCode())
        )));

        templateSDK.setRule(rule);
        ctInfo.setTemplateSDK(templateSDK);
        info.setCouponAndTemplateInfos(Collections.singletonList(ctInfo));

        return info;
    }

    private SettlementInfo fakeZheKouCouponSettlement() {

        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setEmploy(false);
        info.setCost(0.0);

        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setCount(2);
        goodsInfo.setPrice(10.88);
        goodsInfo.setType(GoodsType.WENYU.getCode());

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(10);
        goodsInfo01.setPrice(20.88);
        goodsInfo01.setType(GoodsType.WENYU.getCode());

        info.setGoodsInfos(Arrays.asList(goodsInfo, goodsInfo01));

        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo.setId(1);

        CouponTemplateSDK templateSDK = new CouponTemplateSDK();
        templateSDK.setId(2);
        templateSDK.setCategory(CouponCategory.ZHEKOU.getCode());
        templateSDK.setKey("100220190801");


        TemplateRule rule = new TemplateRule();
        rule.setDiscount(new TemplateRule.Discount(85, 1));
        rule.setUsage(new TemplateRule.Usage("湖北省", "汉川市", JSON.toJSONString(
                Arrays.asList(GoodsType.WENYU.getCode(),
                        GoodsType.JIAJU.getCode())
        )));

        templateSDK.setRule(rule);
        ctInfo.setTemplateSDK(templateSDK);
        info.setCouponAndTemplateInfos(Collections.singletonList(ctInfo));

        return info;
    }


    private SettlementInfo fakeLiJianCouponSettlement() {

        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setEmploy(false);
        info.setCost(0.0);

        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setCount(2);
        goodsInfo.setPrice(10.88);
        goodsInfo.setType(GoodsType.WENYU.getCode());

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(10);
        goodsInfo01.setPrice(20.88);
        goodsInfo01.setType(GoodsType.WENYU.getCode());

        info.setGoodsInfos(Arrays.asList(goodsInfo, goodsInfo01));

        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo.setId(1);

        CouponTemplateSDK templateSDK = new CouponTemplateSDK();
        templateSDK.setId(3);
        templateSDK.setCategory(CouponCategory.LIJIAN.getCode());
        templateSDK.setKey("100320190801");


        TemplateRule rule = new TemplateRule();
        rule.setDiscount(new TemplateRule.Discount(5, 1));
        rule.setUsage(new TemplateRule.Usage("湖北省", "汉川市", JSON.toJSONString(
                Arrays.asList(GoodsType.WENYU.getCode(),
                        GoodsType.JIAJU.getCode())
        )));

        templateSDK.setRule(rule);
        ctInfo.setTemplateSDK(templateSDK);
        info.setCouponAndTemplateInfos(Collections.singletonList(ctInfo));

        return info;
    }


    private SettlementInfo fakeManJianZheKouCouponSettlement() {
        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setEmploy(false);
        info.setCost(0.0);

        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setCount(2);
        goodsInfo.setPrice(10.88);
        goodsInfo.setType(GoodsType.WENYU.getCode());

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(10);
        goodsInfo01.setPrice(20.88);
        goodsInfo01.setType(GoodsType.WENYU.getCode());

        info.setGoodsInfos(Arrays.asList(goodsInfo, goodsInfo01));

        SettlementInfo.CouponAndTemplateInfo manjianctInfo = new SettlementInfo.CouponAndTemplateInfo();
        manjianctInfo.setId(1);

        CouponTemplateSDK manjiantemplateSDK = new CouponTemplateSDK();
        manjiantemplateSDK.setId(1);
        manjiantemplateSDK.setCategory(CouponCategory.MANJIAN.getCode());
        manjiantemplateSDK.setKey("100120190712");


        TemplateRule manjianRule = new TemplateRule();
        manjianRule.setDiscount(new TemplateRule.Discount(20, 199));
        manjianRule.setUsage(new TemplateRule.Usage("湖北省", "汉川市", JSON.toJSONString(
                Arrays.asList(GoodsType.WENYU.getCode(),
                        GoodsType.JIAJU.getCode())
        )));
        manjianRule.setWeight(JSON.toJSONString(Collections.emptyList()));

        manjiantemplateSDK.setRule(manjianRule);
        manjianctInfo.setTemplateSDK(manjiantemplateSDK);


        SettlementInfo.CouponAndTemplateInfo zhekouInfo = new SettlementInfo.CouponAndTemplateInfo();
        zhekouInfo.setId(1);

        CouponTemplateSDK zhekouTemplate = new CouponTemplateSDK();
        zhekouTemplate.setId(2);
        zhekouTemplate.setCategory(CouponCategory.ZHEKOU.getCode());
        zhekouTemplate.setKey("100220190712");

        TemplateRule zhekouRule = new TemplateRule();
        zhekouRule.setDiscount(new TemplateRule.Discount(85, 1));
        zhekouRule.setUsage(new TemplateRule.Usage("湖北省", "汉川市", JSON.toJSONString(
                Arrays.asList(GoodsType.WENYU.getCode(),
                        GoodsType.JIAJU.getCode())
        )));
        zhekouTemplate.setRule(zhekouRule);
        zhekouRule.setWeight(JSON.toJSONString(Collections.singletonList("1001201907120001")));
        zhekouInfo.setTemplateSDK(zhekouTemplate);

        info.setCouponAndTemplateInfos(Arrays.asList(manjianctInfo, zhekouInfo));

        return info;
    }


}
