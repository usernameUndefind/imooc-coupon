package com.imooc.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constans.CouponStatus;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.dao.CouponDao;
import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.feign.SettlementClient;
import com.imooc.coupon.feign.TemplateClient;
import com.imooc.coupon.service.IRedisService;
import com.imooc.coupon.service.IUserService;
import com.imooc.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户服务相关接口实现
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private TemplateClient templateClient;

    @Autowired
    private SettlementClient settlementClient;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException {

        List<Coupon> curCached = redisService.getCachedCoupons(userId, status);
        List<Coupon> preTarget;
        if (CollectionUtils.isNotEmpty(curCached)) {
            log.debug("coupon cache is not empty:{}, {}", userId, status);
            preTarget = curCached;
        } else {
            log.debug("coupon cache is empty, get coupon from db: {}, {}", userId, status);
            List<Coupon> dbCoupons = couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));
            // 如果数据库中没有记录，直接返回就可以， cache中已经加入了一个无效的优惠券
            if (CollectionUtils.isEmpty(dbCoupons)) {
                log.debug("current user do not have coupon: {}, {}", userId, status);
                return dbCoupons;
            }
            // 填充dbCoupons的templateSDK字段
            Map<Integer, CouponTemplateSDK> id2TemplateSDK = templateClient.findId2TemplateSDK(
                    dbCoupons.stream().map(Coupon::getTemplateId).collect(Collectors.toList())
            ).getData();
            dbCoupons.forEach(dc -> {
                id2TemplateSDK.get(dc.getTemplateId());
            });
            preTarget = dbCoupons;
            // 将记录写入cache
            redisService.addCouponToCache(userId, preTarget, status);
        }

        // 将无效优惠券剔除
        preTarget = preTarget.stream().filter(c -> c.getId() != -1).collect(Collectors.toList());
        // 如果当前获取的是可用优惠券，还需要对已过期优惠券做延迟处理
        if (CouponStatus.of(status) == CouponStatus.USABLE) {
            CouponClassify classify = CouponClassify.classify(preTarget);
            // 如果已过期状态不为空， 需要做延迟处理
            if (CollectionUtils.isNotEmpty(classify.getExpired())) {
                log.info("Add Expired Coupons To Cache From FindCouponsByStatus:{}, {}", userId, status);
                redisService.addCouponToCache(userId, classify.getExpired(), CouponStatus.EXPIRED.getCode());
            }
            // 发送到 kafka 中做异步处理
            kafkaTemplate.send(Constant.TOPIC, JSON.toJSONString(new CouponKafkaMessage(CouponStatus.EXPIRED.getCode(),
                    classify.getExpired().stream().map(Coupon::getId).collect(Collectors.toList()))));
            return classify.getUsable();
        }
        return preTarget;
    }

    @Override
    public List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException {

        long curTime = new Date().getTime();
        // 获取所有可用的优惠券模板
        List<CouponTemplateSDK> templateSDKS = templateClient.findAllUsableTemplate().getData();
        log.debug("find All Template from templateClient Count: {}", templateSDKS.size());

        // 过滤过期的优惠券模板
        templateSDKS = templateSDKS.stream().filter(t ->
            t.getRule().getExpiration().getDeadline() > curTime
        ).collect(Collectors.toList());

        log.info("find usable template count:{}", templateSDKS.size());


        // 将所有可用的优惠券模板转换为Map类型，key=模板id, value=pair key:模板领取上限次数, value:模板
        Map<Integer, Pair<Integer, CouponTemplateSDK>> limit2Template = new HashMap<>(templateSDKS.size());
        templateSDKS.forEach(t -> limit2Template.put(t.getId(), Pair.of(t.getRule().getLimitation(), t)));

        List<CouponTemplateSDK> result = new ArrayList<>(limit2Template.size());
        // 获取当前用户的所有已领取优惠券
        List<Coupon> userUsableCoupons = findCouponsByStatus(userId, CouponStatus.USABLE.getCode());
        log.debug("current user has usable coupons:{}, {}", userId, userUsableCoupons.size());

        // 将已领取优惠券转换为map
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons
                .stream().collect(Collectors.groupingBy(Coupon::getTemplateId));

        limit2Template.forEach((k, v) -> {
            int limitation = v.getLeft();
            CouponTemplateSDK templateSDK = v.getRight();

            if (templateId2Coupons.containsKey(k) && templateId2Coupons.get(k).size() >= limitation) {
                return;
            }
            result.add(templateSDK);
        });

        return result;
    }

    /**
     * 1. 从templateClient 拿到对应的优惠券， 并检查是否过期
     * 2. 根据limitation判断用户是否可以领取
     * 3. save to db
     * 4. 填充 couponTemplateSDK
     * 5. save to cache
     * @param request
     * @return
     * @throws CouponException
     */
    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException {
        Map<Integer, CouponTemplateSDK> id2Template = templateClient.findId2TemplateSDK(
                Collections.singletonList(request.getTemplateSDK().getId())
        ).getData();

        // 优惠券模板是需要存在的
        if (id2Template.size() <= 0) {
            log.error("can not acquire template from templateClient: {}, ", request.getTemplateSDK().getId());
            throw new CouponException("can not acquire template from templateClient");
        }

        // 用户是否可以领取这张优惠券
        List<Coupon> userUsableCoupons = findCouponsByStatus(request.getUserId(), CouponStatus.USABLE.getCode());

        Map<Integer, List<Coupon>> templateId3Coupons = userUsableCoupons.stream().collect(Collectors.groupingBy(Coupon::getTemplateId));


        if (templateId3Coupons.containsKey(request.getTemplateSDK().getId()) && templateId3Coupons.get(request.getTemplateSDK().getId())
        .size() >= id2Template.get(request.getTemplateSDK().getId()).getRule().getLimitation()) {
            log.error("Exceed Template Assign  Limitation:{}", request.getTemplateSDK().getId());
            throw new CouponException("Exceed Template Assign  Limitation");
        }

        // 尝试去获取优惠券码
        String couponCode = redisService.tryToAcquireCouponCodeFromCache(request.getTemplateSDK().getId());
        if (StringUtils.isEmpty(couponCode)) {
            log.error("can not acquire coupon code: {}", request.getTemplateSDK().getId());
            throw new CouponException("can not acquire coupon code");
        }

        Coupon newCoupon = new Coupon(request.getTemplateSDK().getId(), request.getUserId(),
                couponCode, CouponStatus.USABLE);

        // save to db
        newCoupon = couponDao.save(newCoupon);

        // 填充coupon对象的CouponTemplateSDK, 一定嗷在放入缓存中之前去填充
        newCoupon.setTemplateSDK(request.getTemplateSDK());

        redisService.addCouponToCache(request.getUserId(), Collections.singletonList(newCoupon),
                CouponStatus.USABLE.getCode());
        return newCoupon;
    }

    /**
     * 这里需要注意，规则相关处理需要由Settlement 系统去做， 当前系统仅仅做业务处理过程
     *
     * @param info
     * @return
     * @throws CouponException
     */
    @Override
    public SettlementInfo settlement(SettlementInfo info) throws CouponException {
        // 当没有传递优惠券时，直接返回商品总价
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos =
                info.getCouponAndTemplateInfos();
        if (CollectionUtils.isEmpty(ctInfos)) {
            log.info("Empty Coupon for settle");
            double goodsSum = 0.0;
            for (GoodsInfo gi : info.getGoodsInfos()) {
                goodsSum += gi.getPrice();
            }
            // 没有优惠券也就不存在优惠券的核销
            info.setCost(retain2Decimals(goodsSum));
        }

        // 校验当前优惠券
        // 获取当前用户自己的优惠券
        List<Coupon> coupons = findCouponsByStatus(info.getUserId(), CouponStatus.USABLE.getCode());
        Map<Integer, Coupon> id2Coupon = coupons.stream().collect(Collectors.toMap(Coupon::getId, Function.identity()));
        if (MapUtils.isEmpty(id2Coupon) || !CollectionUtils.isSubCollection(
                ctInfos.stream().map(SettlementInfo.CouponAndTemplateInfo::getId)
                .collect(Collectors.toList()), id2Coupon.keySet())) {
            log.info("{}", id2Coupon.keySet());
            log.error("User Coupon has some problem , it is not subCollection of Coupons");
            throw new CouponException("User Coupon has some problem , it is not subCollection of Coupons");
        }

        log.debug("Current Settlement Coupons is user's: {}", ctInfos.size());

        List<Coupon> settleCoupons = new ArrayList<>(ctInfos.size());
        ctInfos.forEach(ci -> settleCoupons.add(id2Coupon.get(ci.getId())));

        SettlementInfo processedInfo = settlementClient.computeRule(info).getData();

        if (processedInfo.getEmploy() && CollectionUtils.isNotEmpty(processedInfo.getCouponAndTemplateInfos())) {
            log.info("Settle user coupon: {}, {}", info.getUserId(), JSON.toJSONString(settleCoupons));
            // 更新缓存
            redisService.addCouponToCache(info.getUserId(), settleCoupons, CouponStatus.USED.getCode());
            kafkaTemplate.send(Constant.TOPIC, JSON.toJSONString(new CouponKafkaMessage(CouponStatus.USED.getCode()
            , settleCoupons.stream().map(Coupon::getId).collect(Collectors.toList()))));
        }
        return processedInfo;
    }

    /**
     * 保留两位小数
     * @param value
     * @return
     */
    private double retain2Decimals(double value) {
        // 四舍五入
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
