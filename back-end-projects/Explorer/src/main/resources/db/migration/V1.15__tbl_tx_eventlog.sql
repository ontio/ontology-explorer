/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2020-02-10 17:46:35
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_tx_eventlog
-- ----------------------------
DROP TABLE IF EXISTS `tbl_tx_eventlog`;
CREATE TABLE `tbl_tx_eventlog` (
    `id`                   INT(11)        NOT NULL AUTO_INCREMENT,
    `tx_hash`              VARCHAR(66)    NOT NULL DEFAULT '' COMMENT '交易hash值',
    `tx_type`              INT(11)        NOT NULL COMMENT '区块链交易类型，208：部署合约交易 209：调用合约交易',
    `tx_time`              INT(11)        NOT NULL COMMENT '交易时间戳',
    `block_height`         INT(11)        NOT NULL COMMENT '区块高度',
    `block_index`          INT(11)        NOT NULL COMMENT '交易在区块里的索引',
    `fee`                  DECIMAL(25, 9) NOT NULL COMMENT '交易手续费',
    `confirm_flag`         INT(11)        NOT NULL COMMENT '交易落账标识  1：成功 0：失败',
    `event_log`            VARCHAR(5000)  NOT NULL COMMENT '交易的event log',
    `called_contract_hash` VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '该交易真正调用的合约hash',
    `ontid_tx_flag`        TINYINT(1)     NOT NULL DEFAULT '0' COMMENT '是否属于ontid事件交易 true:属于 false：不属于',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_tx_hash`(`tx_hash`),
    KEY `idx_block_height`(`block_height`),
    KEY `idx_called_contract_hash`(`called_contract_hash`),
    KEY `idx_tx_time`(`tx_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
