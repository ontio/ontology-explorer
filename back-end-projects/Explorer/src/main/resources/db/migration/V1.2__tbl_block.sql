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
CREATE TABLE `tbl_block` (
    `id`             INT(11) PRIMARY KEY AUTO_INCREMENT,
    `block_height`   INT(11)      NOT NULL COMMENT '区块高度',
    `block_hash`     VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '区块hash值',
    `txs_root`       VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '区块里交易的merklroot',
    `block_time`     INT(11)      NOT NULL COMMENT '区块时间戳',
    `consensus_data` VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '区块共识数据',
    `bookkeepers`    VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '区块的bookkeepers',
    `tx_count`       INT(11)      NOT NULL COMMENT '区块里的交易数量',
    `block_size`     INT(11)      NOT NULL COMMENT '区块大小，单位b',
    UNIQUE KEY `idx_block_height`(`block_height`),
    KEY `idx_block_hash`(`block_hash`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

