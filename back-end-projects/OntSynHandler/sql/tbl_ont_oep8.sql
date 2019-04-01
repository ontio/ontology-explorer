/*
Navicat MySQL Data Transfer

Source Server         : 
Source Server Version : 50639
Source Host           : 
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-01 11:31:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_oep8
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_oep8`;
CREATE TABLE `tbl_ont_oep8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract` varchar(255) NOT NULL DEFAULT '',
  `tokenid` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `totalsupply` decimal(30,0) NOT NULL,
  `symbol` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `contactinfo` varchar(2000) NOT NULL DEFAULT '',
  `createtime` datetime NOT NULL,
  `auditflag` int(1) NOT NULL DEFAULT '0',
  `updatetime` datetime DEFAULT NULL,
  `logo` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `idx_contract` (`contract`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
