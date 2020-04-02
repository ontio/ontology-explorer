/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:55:16
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_ont_address_summary
-- ----------------------------
DROP TABLE IF EXISTS `tbl_address_daily_summary`;
CREATE TABLE `tbl_address_daily_summary` (
    `id`            INT(11)      NOT NULL AUTO_INCREMENT,
    `time`          INT(11)      NOT NULL COMMENT '当天UTC0点时间戳',
    `contract_hash` VARCHAR(64)  NOT NULL COMMENT '合约hash值',
    `address`       VARCHAR(255) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_time`(`time`) USING BTREE
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8
    ROW_FORMAT = DYNAMIC;

