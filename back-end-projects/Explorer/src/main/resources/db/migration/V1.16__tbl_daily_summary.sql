/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:57:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_daily_summary
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_daily_summary`;
CREATE TABLE `tbl_ont_daily_summary` (
  `time` int(11) NOT NULL COMMENT '当天的UTC0点时间戳',
  `block_count` int(11) NOT NULL COMMENT '当天的区块数量',
  `tx_count` int(11) NOT NULL COMMENT '当天的交易数量',
  `active_ontid_count` int(11) NOT NULL COMMENT '当天的活跃ONT ID数量',
  `new_ontid_count` int(11) NOT NULL COMMENT '当天的新ONT ID数量',
  `ont_sum` decimal(25,9) NOT NULL COMMENT '当天的ont流通量',
  `ong_sum` decimal(25,9) NOT NULL COMMENT '当天的ong流通量',
  `active_address_count` int(11) NOT NULL COMMENT '当天的活跃地址数量',
  `new_address_count` int(11) NOT NULL COMMENT '当天的新地址数量',
  PRIMARY KEY (`time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
