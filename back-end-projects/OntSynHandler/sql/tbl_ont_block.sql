/*
Navicat MySQL Data Transfer

Source Server         : 
Source Server Version : 50639
Source Host           : 
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-01 11:29:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_block
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_block`;
CREATE TABLE `tbl_ont_block` (
  `height` int(11) NOT NULL,
  `hash` varchar(64) NOT NULL DEFAULT '',
  `prevblock` varchar(64) NOT NULL DEFAULT '',
  `nextblock` varchar(64) NOT NULL DEFAULT '',
  `txnsroot` varchar(64) NOT NULL DEFAULT '',
  `blocktime` int(11) NOT NULL,
  `consensusdata` varchar(20) NOT NULL DEFAULT '',
  `bookkeeper` varchar(500) NOT NULL DEFAULT '',
  `txnnum` int(10) NOT NULL,
  `blocksize` int(10) NOT NULL,
  PRIMARY KEY (`height`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
