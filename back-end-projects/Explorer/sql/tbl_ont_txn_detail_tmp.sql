/*
Navicat MySQL Data Transfer

Source Server         : explorer03.mysql.database.azure.com
Source Server Version : 50639
Source Host           : explorer03.mysql.database.azure.com:3306
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-03-28 18:17:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_txn_detail_tmp
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_txn_detail_tmp`;
CREATE TABLE `tbl_ont_txn_detail_tmp` (
  `txnhash` varchar(64) NOT NULL DEFAULT '',
  `txntype` int(4) NOT NULL,
  `txntime` int(11) NOT NULL,
  `height` int(12) NOT NULL,
  `amount` decimal(40,9) NOT NULL,
  `fee` decimal(25,9) NOT NULL DEFAULT '0.000000000',
  `assetname` varchar(255) NOT NULL DEFAULT '',
  `fromaddress` varchar(255) NOT NULL DEFAULT '',
  `toaddress` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(1500) NOT NULL DEFAULT '',
  `blockindex` int(10) NOT NULL,
  `txnindex` int(10) NOT NULL,
  `confirmflag` int(1) NOT NULL,
  `eventtype` int(2) NOT NULL COMMENT '0:其他 1:手续费 2:部署合约 3:转账 4:ontid 5:存证 6:权限',
  `contracthash` varchar(60) NOT NULL DEFAULT '',
  `payer` varchar(255) NOT NULL DEFAULT '' COMMENT '交易的payer',
  `calledcontracthash` varchar(255) NOT NULL DEFAULT '' COMMENT '这笔交易真正调用的合约hash',
  PRIMARY KEY (`txnhash`,`txnindex`) USING BTREE,
  KEY `idx_eventtype` (`eventtype`) USING BTREE,
  KEY `idx_fromaddr` (`fromaddress`) USING BTREE,
  KEY `idx_toaddr` (`toaddress`) USING BTREE,
  KEY `idx_height` (`height`) USING BTREE,
  KEY `index_contracthash` (`contracthash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
