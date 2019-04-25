/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:55:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_block
-- ----------------------------
DROP TABLE IF EXISTS `tbl_tx_eventlog`;
CREATE TABLE `tbl_tx_eventlog` (
  `tx_hash` varchar(64) NOT NULL DEFAULT '' COMMENT '交易hash值',
  `tx_type` int(11) NOT NULL COMMENT '区块链交易类型，208：部署合约交易 209：调用合约交易',
  `tx_time` int(11) NOT NULL COMMENT '交易时间戳',
  `block_height` int(11) NOT NULL COMMENT '区块高度',
  `block_index` int(11) NOT NULL COMMENT '交易在区块里的索引',
  `event_log` text NOT NULL COMMENT '交易的event log',
  `called_contract_hash` varchar(255) NOT NULL DEFAULT '' COMMENT '该交易真正调用的合约hash',
  PRIMARY KEY (`tx_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


