<<<<<<< Updated upstream
/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:01:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_oep8
-- ----------------------------
DROP TABLE IF EXISTS `tbl_oep8`;
CREATE TABLE `tbl_oep8`
(
    `contract_hash` varchar(255)   NOT NULL DEFAULT '' COMMENT '合约hash值',
    `token_id`      varchar(255)   NOT NULL DEFAULT '' COMMENT 'OEP8的token id',
    `name`          varchar(255)   NOT NULL DEFAULT '' COMMENT 'OEP8代币名称',
    `total_supply`  decimal(15, 0) NOT NULL COMMENT 'OEP8代币总量',
    `symbol`        varchar(255)   NOT NULL DEFAULT '' COMMENT 'OEP8代币符号',
    `create_time`   datetime       NOT NULL COMMENT '创建时间，yyyy-MM-dd',
    `audit_flag`    bool           NOT NULL COMMENT '审核标识，1：审核通过 0：未审核',
    `update_time`   datetime                DEFAULT NULL COMMENT '更新时间，yyyy-MM-dd',
    PRIMARY KEY (`contract_hash`, `token_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

alter table tbl_oep8 add COLUMN vm_category VARCHAR(255) NOT NULL DEFAULT 'neovm' comment '合约vm类型，分为neovm，wasmvm';
=======
/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:01:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_oep8
-- ----------------------------
DROP TABLE IF EXISTS `tbl_oep8`;
CREATE TABLE `tbl_oep8`
(
    `contract_hash` varchar(255)   NOT NULL DEFAULT '' COMMENT '合约hash值',
    `token_id`      varchar(255)   NOT NULL DEFAULT '' COMMENT 'OEP8的token id',
    `name`          varchar(255)   NOT NULL DEFAULT '' COMMENT 'OEP8代币名称',
    `total_supply`  decimal(15, 0) NOT NULL COMMENT 'OEP8代币总量',
    `symbol`        varchar(255)   NOT NULL DEFAULT '' COMMENT 'OEP8代币符号',
    `create_time`   datetime       NOT NULL COMMENT '创建时间，yyyy-MM-dd',
    `audit_flag`    bool           NOT NULL COMMENT '审核标识，1：审核通过 0：未审核',
    `update_time`   datetime                DEFAULT NULL COMMENT '更新时间，yyyy-MM-dd',
    PRIMARY KEY (`contract_hash`, `token_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
>>>>>>> Stashed changes
