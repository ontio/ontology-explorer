/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:55:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_block
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_block`;
CREATE TABLE `tbl_ont_block` (
  `height` int(11) NOT NULL COMMENT '区块高度',
  `hash` varchar(64) NOT NULL DEFAULT '' COMMENT '区块hash',
  `prevblock` varchar(64) NOT NULL DEFAULT '' COMMENT '上一个区块的hash值',
  `nextblock` varchar(64) NOT NULL DEFAULT '' COMMENT '下一个区块的hash值',
  `txnsroot` varchar(64) NOT NULL DEFAULT '' COMMENT '区块的txnsroot',
  `blocktime` int(11) NOT NULL COMMENT '区块时间戳',
  `consensusdata` varchar(20) NOT NULL DEFAULT '' COMMENT '区块共识数据',
  `bookkeeper` varchar(500) NOT NULL DEFAULT '' COMMENT '区块keeper',
  `txnnum` int(10) NOT NULL COMMENT '区块里的交易数量',
  `blocksize` int(10) NOT NULL COMMENT '区块大小，单位b',
  PRIMARY KEY (`height`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
