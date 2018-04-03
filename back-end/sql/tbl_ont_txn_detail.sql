/*
Navicat MySQL Data Transfer

Source Server         : ont-testNet
Source Server Version : 50721
Source Host           : 128.1.132.167:3306
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-30 12:18:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_txn_detail
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_txn_detail`;
CREATE TABLE `tbl_ont_txn_detail` (
  `txnhash` varchar(64) NOT NULL DEFAULT '',
  `txntype` int(4) NOT NULL,
  `txntime` int(11) NOT NULL,
  `height` int(12) NOT NULL,
  `amount` decimal(25,8) DEFAULT NULL,
  `fee` decimal(12,0) DEFAULT '0',
  `assetname` varchar(255) NOT NULL DEFAULT '',
  `fromaddress` varchar(255) NOT NULL DEFAULT '',
  `toaddress` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `blockindex` int(10) NOT NULL,
  `txnindex` int(10) NOT NULL,
  `confirmflag` int(1) NOT NULL,
  PRIMARY KEY (`txnhash`,`txnindex`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
