/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:00:09
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_ont_oep4txn_detail
-- ----------------------------
DROP TABLE IF EXISTS `tbl_oep4_tx_detail`;
CREATE TABLE `tbl_oep4_tx_detail`
(
    `tx_hash`              varchar(64)    NOT NULL DEFAULT '' COMMENT '交易hash',
    `tx_type`              int(11)        NOT NULL COMMENT '区块链交易类型，208：部署合约交易 209：调用合约交易',
    `tx_time`              int(11)        NOT NULL COMMENT '交易时间戳',
    `block_height`         int(11)        NOT NULL COMMENT '区块高度',
    `amount`               decimal(40, 9) NOT NULL COMMENT '交易金额',
    `fee`                  decimal(25, 9) NOT NULL COMMENT '交易手续费',
    `asset_name`           varchar(64)    NOT NULL DEFAULT '' COMMENT '交易资产名',
    `from_address`         varchar(255)   NOT NULL DEFAULT '' COMMENT '交易fromaddress',
    `to_address`           varchar(255)   NOT NULL DEFAULT '' COMMENT '交易toaddress',
    `description`          varchar(1000)  NOT NULL DEFAULT '' COMMENT '交易描述',
    `block_index`          int(11)        NOT NULL COMMENT '交易在区块里的索引',
    `tx_index`             int(11)        NOT NULL COMMENT '该event在交易eventlog里的索引',
    `confirm_flag`         int(11)        NOT NULL COMMENT '交易落账标识  1：成功 0：失败',
    `event_type`           int(11)        NOT NULL COMMENT '交易event类型 0:其他 1:部署合约 2:手续费 3:转账 4:ONT ID 5:存证 6:权限',
    `contract_hash`        varchar(255)   NOT NULL DEFAULT '' COMMENT '该event对应的合约hash',
    `payer`                varchar(255)   NOT NULL DEFAULT '' COMMENT '交易的payer',
    `called_contract_hash` varchar(255)   NOT NULL DEFAULT '' COMMENT '该交易真正调用的合约hash',
    PRIMARY KEY (`tx_hash`, `tx_index`),
    KEY `idx_block_height` (`block_height`) USING BTREE,
    KEY `idx_called_contract_hash` (`called_contract_hash`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;