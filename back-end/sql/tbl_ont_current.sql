/*
Navicat MySQL Data Transfer

Source Server         : ont-testNet
Source Server Version : 50721
Source Host           : 128.1.132.167:3306
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-30 12:17:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_current
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_current`;
CREATE TABLE `tbl_ont_current` (
  `height` int(12) NOT NULL,
  `txncount` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into tbl_ont_current(height,txncount) values(0,0);
