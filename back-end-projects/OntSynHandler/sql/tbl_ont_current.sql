/*
Navicat MySQL Data Transfer

Source Server         : 
Source Server Version : 50639
Source Host           : 
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-01 11:30:03
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

