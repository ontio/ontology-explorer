/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2018-08-01 10:29:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_current
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_current`;
CREATE TABLE `tbl_ont_current` (
  `height` int(12) NOT NULL,
  `txncount` int(12) NOT NULL,
  `ontidcount` int(12) NOT NULL,
  `nonontidtxncount` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into tbl_ont_current(height,txncount,ontidcount,nonontidtxncount) values(-1,0,0,0);