/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 11:00:33
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_ont_oep5_dragon
-- ----------------------------
DROP TABLE IF EXISTS `tbl_oep5_dragon`;
CREATE TABLE `tbl_oep5_dragon`
(
    `id`            int(11)      NOT NULL AUTO_INCREMENT,
    `contract_hash` varchar(255) NOT NULL DEFAULT '' COMMENT '合约hash',
    `asset_name`    varchar(255) NOT NULL DEFAULT '' COMMENT '该dragon的名称',
    `json_url`      varchar(255) NOT NULL COMMENT '该dragon的基本信息url',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
