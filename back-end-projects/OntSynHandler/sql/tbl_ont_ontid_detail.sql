/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-06-27 10:28:45
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
  `fee` decimal(25,9) NOT NULL DEFAULT '0.00000000',
  PRIMARY KEY (`txnhash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE tbl_ont_ontid_detail ADD INDEX idx_ontid (ontid)
