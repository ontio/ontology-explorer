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
DROP TABLE IF EXISTS `tbl_ont_contract_summary`;
CREATE TABLE `tbl_ont_contract_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` int(11) NOT NULL COMMENT '当天的UTC0点时间戳',
  `contracthash` varchar(64) NOT NULL DEFAULT '' COMMENT '合约hash值',
  `txncount` int(10) NOT NULL COMMENT '此合约当天的交易数量',
  `ontcount` decimal(25,9) NOT NULL COMMENT '此合约当天的ont流通量',
  `ongcount` decimal(25,9) NOT NULL COMMENT '此合约当天的ong流通量',
  `activeaddress` int(10) NOT NULL COMMENT '此合约当天的活跃地址数',
  `newaddress` int(10) NOT NULL COMMENT '此合约当天的新地址数',
  `score` int(10) NOT NULL DEFAULT '0' COMMENT '此合约当天的得分',
  `ongreward` decimal(25,9) NOT NULL DEFAULT '0.000000000' COMMENT '此合约的日奖励',
  `ontreward` decimal(25,9) NOT NULL DEFAULT '0.000000000' COMMENT '此合约的周奖励',
  `project` varchar(255) NOT NULL DEFAULT '' COMMENT '此合约属于的项目名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_time` (`time`) USING BTREE,
  KEY `idx_contracthash` (`contracthash`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
