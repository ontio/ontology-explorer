/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:00:20
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_oep5
-- ----------------------------
DROP TABLE IF EXISTS `tbl_oep5`;
CREATE TABLE `tbl_oep5` (
    `contract_hash` VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约hash值',
    `name`          VARCHAR(255)   NOT NULL DEFAULT '' COMMENT 'OEP5代币名称',
    `total_supply`  DECIMAL(15, 0) NOT NULL COMMENT 'OEP5代币总量',
    `symbol`        VARCHAR(255)   NOT NULL DEFAULT '' COMMENT 'OEP5代币符号',
    `create_time`   DATETIME       NOT NULL COMMENT '创建时间，yyyy-MM-dd',
    `audit_flag`    BOOL           NOT NULL COMMENT '审核标识，1：审核通过 0：未审核',
    `update_time`   DATETIME                DEFAULT NULL COMMENT '更新时间，yyyy-MM-dd',
    PRIMARY KEY (`contract_hash`)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

ALTER TABLE tbl_oep5
    ADD COLUMN vm_category VARCHAR(255) NOT NULL DEFAULT 'neovm' COMMENT '合约vm类型，分为neovm，wasmvm';
