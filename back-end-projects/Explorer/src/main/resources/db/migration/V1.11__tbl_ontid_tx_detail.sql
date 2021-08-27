/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : explorer

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2020-02-10 17:46:10
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_ontid_tx_detail
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ontid_tx_detail`;
CREATE TABLE `tbl_ontid_tx_detail` (
    `id`           INT(11)        NOT NULL AUTO_INCREMENT,
    `tx_hash`      VARCHAR(66)    NOT NULL DEFAULT '' COMMENT '交易hash',
    `tx_type`      INT(11)        NOT NULL COMMENT '区块链交易类型，208：部署合约交易 209：调用合约交易',
    `ontid`        VARCHAR(255)   NOT NULL DEFAULT '' COMMENT 'ONT ID',
    `tx_time`      INT(11)        NOT NULL COMMENT '交易时间戳',
    `block_height` INT(12)        NOT NULL COMMENT '区块高度',
    `description`  VARCHAR(255)   NOT NULL DEFAULT '' COMMENT 'ONT ID交易描述',
    `fee`          DECIMAL(25, 9) NOT NULL COMMENT '交易手续费',
    PRIMARY KEY (`id`),
    KEY `idx_ontid`(`ontid`),
    KEY `idx_tx_hash`(`tx_hash`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
