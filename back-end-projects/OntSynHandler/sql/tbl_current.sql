/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:56:14
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_ont_current
-- ----------------------------
DROP TABLE IF EXISTS `tbl_current`;
CREATE TABLE `tbl_current`
(
    `block_height`      int(11) NOT NULL COMMENT '当前同步的最新区块高度',
    `tx_count`          int(11) NOT NULL COMMENT '当前同步的最新交易数量',
    `ontid_count`       int(11) NOT NULL COMMENT '当前同步的最新ONT ID数量',
    `ontid_tx_count` int(11) NOT NULL COMMENT '当前同步的最新ONT ID相关的交易数量'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


insert into tbl_current(block_height, tx_count, ontid_count, ontid_tx_count)
values (-1, 0, 0, 0);
