/*
Navicat MySQL Data Transfer

Source Server         : ont-testNet
Source Server Version : 50721
Source Host           : 
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
  `amount` decimal(40,9) NOT NULL,
  `fee` decimal(25,9) NOT NULL DEFAULT '0.000000000',
  `assetname` varchar(255) NOT NULL DEFAULT '',
  `fromaddress` varchar(255) NOT NULL DEFAULT '',
  `toaddress` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `blockindex` int(10) NOT NULL,
  `txnindex` int(10) NOT NULL,
  `confirmflag` int(1) NOT NULL,
  `eventtype` int(2) NOT NULL COMMENT '0:其他 1:手续费 2:部署合约 3:转账 4:ontid 5:存证 6:权限',
  PRIMARY KEY (`txnhash`,`txnindex`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE tbl_ont_txn_detail ADD INDEX idx_fromaddr (fromaddress);
ALTER TABLE tbl_ont_txn_detail ADD INDEX idx_toaddr (toaddress);
