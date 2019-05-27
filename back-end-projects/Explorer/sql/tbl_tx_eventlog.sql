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
DROP TABLE IF EXISTS `tbl_tx_eventlog`;
CREATE TABLE `tbl_tx_eventlog`
(
    `tx_hash`              varchar(64)  NOT NULL DEFAULT '' COMMENT '交易hash值',
    `tx_type`              int(11)      NOT NULL COMMENT '区块链交易类型，208：部署合约交易 209：调用合约交易',
    `tx_time`              int(11)      NOT NULL COMMENT '交易时间戳',
    `block_height`         int(11)      NOT NULL COMMENT '区块高度',
    `block_index`          int(11)      NOT NULL COMMENT '交易在区块里的索引',
    `fee`                  decimal(25, 9) NOT NULL COMMENT '交易手续费',
    `confirm_flag`         int(11)        NOT NULL COMMENT '交易落账标识  1：成功 0：失败',
    `event_log`            varchar(5000)NOT NULL COMMENT '交易的event log',
    `called_contract_hash` varchar(255) NOT NULL DEFAULT '' COMMENT '该交易真正调用的合约hash',
    `ontid_tx_flag`        tinyint(1)   NOT NULL DEFAULT '0' COMMENT '是否属于ontid事件交易 true:属于 false：不属于',
    PRIMARY KEY (`tx_hash`),
    KEY `idx_block_height` (`block_height`),
    KEY `idx_called_contract_hash` (`called_contract_hash`),
    KEY `idx_tx_time` (`tx_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


