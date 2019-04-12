/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:01:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_oep8txn_detail
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_oep8txn_detail`;
CREATE TABLE `tbl_ont_oep8txn_detail` (
  `txnhash` varchar(64) NOT NULL DEFAULT '' COMMENT '交易hash',
  `txntype` int(4) NOT NULL COMMENT '区块链交易类型，208：部署合约交易 209：调用合约交易',
  `txntime` int(11) NOT NULL COMMENT '交易时间',
  `height` int(12) NOT NULL COMMENT '交易所在区块高度',
  `tokenname` varchar(64) NOT NULL COMMENT '交易token名称',
  `amount` decimal(40,9) NOT NULL COMMENT '交易金额',
  `fee` decimal(25,9) NOT NULL DEFAULT '0.000000000' COMMENT '交易ong手续费',
  `assetname` varchar(255) NOT NULL DEFAULT '' COMMENT '交易资产名',
  `fromaddress` varchar(255) NOT NULL DEFAULT '' COMMENT '交易fromaddress',
  `toaddress` varchar(255) NOT NULL DEFAULT '' COMMENT '交易toaddress',
  `description` varchar(1500) NOT NULL DEFAULT '' COMMENT '交易描述',
  `blockindex` int(10) NOT NULL COMMENT '交易在区块里的顺序',
  `txnindex` int(10) NOT NULL COMMENT '该event信息在交易里的顺序',
  `confirmflag` int(1) NOT NULL COMMENT '交易落账标识  1：成功 2：失败',
  `eventtype` int(2) NOT NULL COMMENT '应用层交易类型 0:其他 1:手续费 2:部署合约 3:转账 4:ontid 5:存证 6:权限',
  `contracthash` varchar(60) NOT NULL DEFAULT ''  COMMENT '该event对应的合约hash值',
  PRIMARY KEY (`txnhash`,`txnindex`),
  KEY `idx_fromaddr` (`fromaddress`),
  KEY `idx_toaddr` (`toaddress`),
  KEY `idx_height` (`height`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
