/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:01:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_oep8
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_oep8`;
CREATE TABLE `tbl_ont_oep8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract` varchar(255) NOT NULL DEFAULT '',
  `tokenid` varchar(255) NOT NULL DEFAULT '' COMMENT '该oep8的tokenid',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '该oep8的name',
  `totalsupply` decimal(30,0) NOT NULL COMMENT '该oep8 tokenid的总发行量',
  `symbol` varchar(255) NOT NULL DEFAULT '' COMMENT '该oep8 tokenid的符号',
  `description` varchar(255) NOT NULL DEFAULT '' ,
  `contactinfo` varchar(2000) NOT NULL DEFAULT '' COMMENT '联系信息',
  `createtime` datetime NOT NULL,
  `auditflag` int(1) NOT NULL DEFAULT '0' COMMENT '审核标识 1：审核通过 0：未审核',
  `updatetime` datetime DEFAULT NULL,
  `logo` varchar(255) NOT NULL DEFAULT '' COMMENT '该oep8token的logo的url',
  PRIMARY KEY (`id`),
  KEY `idx_contract` (`contract`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
