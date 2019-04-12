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
  `blockcount` int(10) NOT NULL COMMENT '当天的区块数量',
  `txncount` int(10) NOT NULL COMMENT '当天的交易数量',
  `ontidactivecount` int(10) NOT NULL COMMENT '当天的活跃ontid数量',
  `ontidnewcount` int(10) NOT NULL COMMENT '当天的新ontid数量',
  `ontcount` decimal(25,9) NOT NULL COMMENT '当天的ont流通量',
  `ongcount` decimal(25,9) NOT NULL COMMENT '当天的ong流通量',
  `activeaddress` int(10) NOT NULL COMMENT '当天的活跃地址数量',
  `newaddress` int(10) NOT NULL COMMENT '当天的新地址数量',
  PRIMARY KEY (`time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
