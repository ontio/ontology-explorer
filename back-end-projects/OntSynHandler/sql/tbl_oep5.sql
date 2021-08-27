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
CREATE TABLE `tbl_oep5`
(
    `contract_hash` varchar(255) NOT NULL DEFAULT '' COMMENT '合约hash值',
    `name`          varchar(255) NOT NULL DEFAULT '' COMMENT 'OEP5代币名称',
    `total_supply`  decimal(15,0) NOT NULL COMMENT 'OEP5代币总量',
    `symbol`        varchar(255) NOT NULL DEFAULT '' COMMENT 'OEP5代币符号',
    `create_time`   datetime     NOT NULL COMMENT '创建时间，yyyy-MM-dd',
    `audit_flag`    bool         NOT NULL COMMENT '审核标识，1：审核通过 0：未审核',
    `update_time`   datetime              DEFAULT NULL COMMENT '更新时间，yyyy-MM-dd',
    PRIMARY KEY (`contract_hash`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
