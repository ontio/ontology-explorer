/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:56:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_current
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_current`;
CREATE TABLE `tbl_ont_current` (
  `height` int(12) NOT NULL COMMENT '当前同步的最新区块高度',
  `txncount` int(12) NOT NULL COMMENT '当前同步的最新交易数量',
  `ontidcount` int(12) NOT NULL COMMENT '当前同步的最新ontid数量',
  `nonontidtxncount` int(12) NOT NULL COMMENT '当前同步的最新非ontid相关的交易数量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into tbl_ont_current(height,txncount,ontidcount,nonontidtxncount) values(-1,0,0,0);
