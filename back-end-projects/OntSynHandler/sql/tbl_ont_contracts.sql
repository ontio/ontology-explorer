/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-12-10 11:44:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_contracts
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_contracts`;
CREATE TABLE `tbl_ont_contracts` (
  `contract` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `txcount` int(10) NOT NULL,
  `abi` text,
  `code` text,
  `createtime` int(11) NOT NULL,
  `auditflag` int(1) NOT NULL,
  `updatetime` int(11) DEFAULT NULL,
  `contactinfo` varchar(2000) NOT NULL DEFAULT '',
  `type` varchar(55) NOT NULL DEFAULT '',
  `description` varchar(1500) NOT NULL DEFAULT '',
  `logo` varchar(255) NOT NULL DEFAULT '',
  `creator` varchar(1000) NOT NULL DEFAULT '',
  `addresscount` int(10) NOT NULL,
  `ontcount` decimal(25,9) NOT NULL,
  `ongcount` decimal(25,9) NOT NULL,
  PRIMARY KEY (`contract`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
