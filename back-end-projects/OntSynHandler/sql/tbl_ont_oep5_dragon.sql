/*
Navicat MySQL Data Transfer

Source Server         : 
Source Server Version : 50639
Source Host           : 
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-01 11:30:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_oep5_dragon
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_oep5_dragon`;
CREATE TABLE `tbl_ont_oep5_dragon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract` varchar(255) NOT NULL DEFAULT '',
  `assertname` varchar(255) NOT NULL DEFAULT '',
  `jsonurl` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2131 DEFAULT CHARSET=utf8;
