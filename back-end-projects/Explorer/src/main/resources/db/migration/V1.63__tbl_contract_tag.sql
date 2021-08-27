SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_contract_tag
-- ----------------------------

DROP TABLE IF EXISTS `tbl_contract_tag`;

CREATE TABLE `tbl_contract_tag`
(
    `contract_hash` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '合约调用的hash',
    `name`          VARCHAR(255) NOT NULL DEFAULT '' COMMENT '被调用合约的标签',
    PRIMARY KEY (`contract_hash`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;