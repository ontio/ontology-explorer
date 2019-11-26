SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_config
-- ----------------------------

DROP TABLE IF EXISTS `tbl_config`;
CREATE TABLE `tbl_config`
(
    `field` varchar(256) NOT NULL,
    `value` varchar(256) NOT NULL,
    PRIMARY KEY (`field`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;