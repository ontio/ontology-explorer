/*
Navicat MySQL Data Transfer

Source Server         : 
Source Server Version : 50639
Source Host           : 
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-01 11:31:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_ontid_detail
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_ontid_detail`;
CREATE TABLE `tbl_ont_ontid_detail` (
  `txnhash` varchar(64) NOT NULL DEFAULT '',
  `txntype` int(4) NOT NULL,
  `ontid` varchar(255) NOT NULL DEFAULT '',
  `txntime` int(11) NOT NULL,
  `height` int(12) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT '',
  `fee` decimal(25,9) NOT NULL DEFAULT '0.000000000',
  PRIMARY KEY (`txnhash`),
  KEY `idx_ontid` (`ontid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
