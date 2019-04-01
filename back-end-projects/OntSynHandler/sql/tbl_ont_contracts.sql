/*
Navicat MySQL Data Transfer

Source Server         : 
Source Server Version : 50639
Source Host           : 
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-01 11:29:51
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
  `project` varchar(255) NOT NULL DEFAULT '' COMMENT '项目名称',
  `tokencount` varchar(1000) NOT NULL DEFAULT '' COMMENT 'token数量',
  `dappstoreflag` int(1) NOT NULL DEFAULT '0' COMMENT '是否是dappstore中的dapp。1：是，0：不是',
  `category` varchar(255) NOT NULL DEFAULT '' COMMENT '合约分类',
  PRIMARY KEY (`contract`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
