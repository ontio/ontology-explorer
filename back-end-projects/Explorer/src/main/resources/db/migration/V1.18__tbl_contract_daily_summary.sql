/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:55:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_contract_summary
-- ----------------------------
DROP TABLE IF EXISTS `tbl_contract_daily_summary`;
CREATE TABLE `tbl_contract_daily_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` int(11) NOT NULL COMMENT '当天的UTC0点时间戳',
  `contract_hash` varchar(64) NOT NULL DEFAULT '' COMMENT '合约hash值',
  `tx_count` int(10) NOT NULL COMMENT '此合约当天的交易数量',
  `ont_sum` decimal(25,9) NOT NULL COMMENT '此合约当天的ont流通量',
  `ong_sum` decimal(25,9) NOT NULL COMMENT '此合约当天的ong流通量',
  `active_address_count` int(10) NOT NULL COMMENT '此合约当天的活跃地址数',
  `new_address_count` int(10) NOT NULL COMMENT '此合约当天的新地址数',
  `dapp_name` varchar(255) NOT NULL DEFAULT '' COMMENT '合约所属dapp名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_time` (`time`) USING BTREE,
  KEY `idx_contract_hash` (`contract_hash`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
