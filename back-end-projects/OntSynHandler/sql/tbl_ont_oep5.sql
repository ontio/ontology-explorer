/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:00:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_oep5
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_oep5`;
CREATE TABLE `tbl_ont_oep5` (
  `contract` varchar(255) NOT NULL DEFAULT '' COMMENT '此oep5代币的合约hash值',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '此oep5代币的名称',
  `totalsupply` decimal(30,0) NOT NULL COMMENT '此oep5代币的总发行量',
  `symbol` varchar(255) NOT NULL DEFAULT '' COMMENT '此oep5代币的符号',
  `description` varchar(255) NOT NULL DEFAULT '',
  `contactinfo` varchar(2000) NOT NULL DEFAULT '',
  `createtime` datetime NOT NULL,
  `auditflag` int(1) NOT NULL COMMENT '审核标识，1：审核通过 0：未审核',
  `updatetime` datetime DEFAULT NULL,
  `logo` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`contract`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
