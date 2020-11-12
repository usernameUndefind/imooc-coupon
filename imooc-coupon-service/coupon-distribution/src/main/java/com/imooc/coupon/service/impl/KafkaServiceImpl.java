package com.imooc.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constans.CouponStatus;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.dao.CouponDao;
import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.service.IKafkaService;
import com.imooc.coupon.vo.CouponKafkaMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * kafka相关的服务接口实现
 * 是将cache中的coupon的状态变化同步到DB中
 */
@Slf4j
@Service
@AllArgsConstructor
public class KafkaServiceImpl implements IKafkaService {

    private final CouponDao couponDao;


    @Override
    @KafkaListener(topics ={Constant.TOPIC}, groupId = "imooc-coupon-1")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Objects message = (Objects) kafkaMessage.get();
            CouponKafkaMessage couponInfo = JSON.parseObject(
                    message.toString(),
                    CouponKafkaMessage.class
            );
            log.info("Receive CouponKafkaMessage: {}", message.toString());

            CouponStatus status = CouponStatus.of(couponInfo.getStatus());
            switch (status) {
                case USABLE:
                    break;
                case USED:
                    processUsedCoupons(couponInfo, status);
                    break;
                case EXPIRED:
                    processExpiredCoupons(couponInfo, status);
                    break;
            }
        }
    }

    private void processUsedCoupons(CouponKafkaMessage message, CouponStatus status) {
        processCouponsByStatus(message, status);
    }

    private void processExpiredCoupons(CouponKafkaMessage message, CouponStatus status) {
        processCouponsByStatus(message, status);
    }

    private void processCouponsByStatus(CouponKafkaMessage message, CouponStatus status) {
        List<Coupon> coupons = couponDao.findAllById(message.getIds());

        if (CollectionUtils.isEmpty(coupons) || coupons.size() != message.getIds().size()) {
            log.error("Cannot Find Right Coupon Info: {}", JSON.toJSONString(message));
            // TODO 发送短信
            return;
        }

        coupons.forEach(c -> c.setStatus(status));
        log.info("CouponKafkaMessage Op Coupon Count: {}",
                couponDao.saveAll(coupons).size());

    }
}
