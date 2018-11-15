/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : explorer02

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-11-15 18:47:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_oep4
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_oep4`;
CREATE TABLE `tbl_ont_oep4` (
  `Contract` varchar(255) NOT NULL DEFAULT '',
  `Name` varchar(255) NOT NULL DEFAULT '',
  `TotalSupply` decimal(30,0) NOT NULL,
  `Symbol` varchar(255) NOT NULL DEFAULT '',
  `Decimals` decimal(20,0) NOT NULL,
  `Description` varchar(255) NOT NULL DEFAULT '',
  `ContactInfo` varchar(2000) NOT NULL DEFAULT '',
  `CreateTime` datetime NOT NULL,
  `AuditFlag` int(1) NOT NULL,
  `UpdateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`Contract`,`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
