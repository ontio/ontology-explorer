/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:59:58
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_ont_oep4
-- ----------------------------
DROP TABLE IF EXISTS `tbl_oep4`;
CREATE TABLE `tbl_oep4` (
    `contract_hash` VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '合约hash值',
    `name`          VARCHAR(255)            NOT NULL DEFAULT '' COMMENT 'OEP4代币名称',
    `total_supply`  DECIMAL(15, 0)          NOT NULL COMMENT 'OEP4代币总量',
    `symbol`        VARCHAR(255)            NOT NULL DEFAULT '' COMMENT 'OEP4代币符号',
    `decimals`      INT(11)                 NOT NULL COMMENT 'OEP4代币精度',
    `create_time`   DATETIME COMMENT '创建时间' NOT NULL,
    `audit_flag`    BOOL                    NOT NULL COMMENT '审核标识，1：审核通过 0：未审核',
    `update_time`   DATETIME COMMENT '更新时间'          DEFAULT NULL,
    PRIMARY KEY (`contract_hash`)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

ALTER TABLE tbl_oep4
    ADD COLUMN vm_category VARCHAR(255) NOT NULL DEFAULT 'neovm' COMMENT '合约vm类型，分为neovm，wasmvm';
