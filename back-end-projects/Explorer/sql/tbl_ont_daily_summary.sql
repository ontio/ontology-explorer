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
-- Table structure for tbl_ont_daily_summary
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_daily_summary`;
CREATE TABLE `tbl_ont_daily_summary` (
  `time` int(11) NOT NULL,
  `blockcount` int(10) NOT NULL,
  `txncount` int(10) NOT NULL,
  `ontidactivecount` int(10) NOT NULL,
  `ontidnewcount` int(10) NOT NULL,
  `ontcount` decimal(25,9) NOT NULL,
  `ongcount` decimal(25,9) NOT NULL,
  `activeaddress` int(10) NOT NULL,
  `newaddress` int(10) NOT NULL,
  PRIMARY KEY (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;