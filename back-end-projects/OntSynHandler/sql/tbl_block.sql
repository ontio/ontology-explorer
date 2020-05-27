/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:55:33
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_ont_block
-- ----------------------------
DROP TABLE IF EXISTS `tbl_block`;
CREATE TABLE `tbl_block`
(
    `block_height`   int(11)      NOT NULL COMMENT '区块高度',
    `block_hash`     varchar(64)  NOT NULL DEFAULT '' COMMENT '区块hash值',
    `txs_root`       varchar(64)  NOT NULL DEFAULT '' COMMENT '区块里交易的merklroot',
    `block_time`     int(11)      NOT NULL COMMENT '区块时间戳',
    `consensus_data` varchar(64)  NOT NULL DEFAULT '' COMMENT '区块共识数据',
    `bookkeepers`    varchar(500) NOT NULL DEFAULT '' COMMENT '区块的bookkeepers',
    `tx_count`       int(11)      NOT NULL COMMENT '区块里的交易数量',
    `block_size`     int(11)      NOT NULL COMMENT '区块大小，单位b',
    PRIMARY KEY (`block_height`),
    KEY `idx_block_hash` (`block_hash`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


