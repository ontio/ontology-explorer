/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:01:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_ontid_detail
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_ontid_detail`;
CREATE TABLE `tbl_ont_ontid_detail` (
  `txnhash` varchar(64) NOT NULL DEFAULT '' COMMENT '交易hash',
  `txntype` int(4) NOT NULL COMMENT '区块链交易类型，208：部署合约交易 209：调用合约交易',
  `ontid` varchar(255) NOT NULL DEFAULT '' COMMENT 'ontid',
  `txntime` int(11) NOT NULL COMMENT '交易时间',
  `height` int(12) NOT NULL COMMENT '交易所在区块高度',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '交易描述',
  `fee` decimal(25,9) NOT NULL DEFAULT '0.000000000' COMMENT '交易ong手续费',
  PRIMARY KEY (`txnhash`),
  KEY `idx_ontid` (`ontid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
