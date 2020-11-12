package com.imooc.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * kafka相关的服务接口定义
 */
public interface IKafkaService {


    void consumeCouponKafkaMessage(ConsumerRecord<?,?> record);
}
