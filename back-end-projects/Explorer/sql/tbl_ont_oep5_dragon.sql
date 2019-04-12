/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:00:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_oep5_dragon
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_oep5_dragon`;
CREATE TABLE `tbl_ont_oep5_dragon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract` varchar(255) NOT NULL DEFAULT '' COMMENT '合约hash',
  `assertname` varchar(255) NOT NULL DEFAULT '' COMMENT '该dragon的name',
  `jsonurl` varchar(255) NOT NULL COMMENT '该dragon的url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2322 DEFAULT CHARSET=utf8;
