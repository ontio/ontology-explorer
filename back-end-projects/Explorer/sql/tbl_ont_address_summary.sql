/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-12-06 17:43:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_address_summary
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_address_summary`;
CREATE TABLE `tbl_ont_address_summary` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `time` int(11) NOT NULL,
  `contract` varchar(64) NOT NULL COMMENT 'common:普通转账 contracthash：合约地址',
  `address` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `idx_time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
