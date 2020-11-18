/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : imooc_coupon_data

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 16/11/2020 23:20:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for coupon_template
-- ----------------------------
DROP TABLE IF EXISTS `imooc_coupon_data.coupon_template`;
CREATE TABLE `imooc_coupon_data.coupon_template`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `available` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是可用状态; true: 可用, false: 不可用',
  `expired` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否过期; true: 是, false: 否',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `logo` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠券 logo',
  `intro` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠券描述',
  `category` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠券分类',
  `product_line` int(0) NOT NULL DEFAULT 0 COMMENT '产品线',
  `coupon_count` int(0) NOT NULL DEFAULT 0 COMMENT '总数',
  `create_time` datetime(0) NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `user_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '创建用户',
  `template_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠券模板的编码',
  `target` int(0) NOT NULL DEFAULT 0 COMMENT '目标用户',
  `rule` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠券规则: TemplateRule 的 json 表示',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE,
  INDEX `idx_category`(`category`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优惠券模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coupon_template
-- ----------------------------
INSERT INTO `coupon_template` VALUES (10, 1, 0, '优惠券模板-1603809231926', 'http://www.baidu.com', '这是一张优惠券模板', '001', 1, 10000, '2020-10-27 14:33:52', 10001, '100120201027', 1, '{\"discount\":{\"base\":1,\"quota\":5},\"expiration\":{\"deadline\":1608993231927,\"gap\":1,\"period\":1},\"limitation\":1,\"usage\":{\"city\":\"桐城市\",\"goodsType\":\"[\\\"文娱\\\",\\\"家居\\\"]\",\"province\":\"安徽省\"},\"weight\":\"[]\"}');

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS `imooc_coupon_data`.`coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `template_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联优惠券模板的主键',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '领取用户',
  `coupon_code` varchar(64) NOT NULL DEFAULT '' COMMENT '优惠券码',
  `assign_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '领取时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '优惠券的状态',
  PRIMARY KEY (`id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='优惠券(用户领取的记录)';
-- 创建 coupon 数据表


-- 清空表数据
-- truncate coupon;
