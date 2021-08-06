

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_erc20
-- ----------------------------

DROP TABLE IF EXISTS `tbl_erc20`;
CREATE TABLE `tbl_erc20` (
     `contract_hash` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '合约hash值',
     `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'erc20代币名称',
     `total_supply` DECIMAL(15,0) NOT NULL COMMENT 'erc20代币总量',
     `symbol` VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'erc20代币符号',
     `decimals` INT(11) NOT NULL COMMENT 'erc20代币精度',
     `create_time` DATETIME NOT NULL COMMENT '创建时间',
     `audit_flag` TINYINT(1) NOT NULL COMMENT '审核标识，1：审核通过 0：未审核',
     `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
     `vm_category` VARCHAR(255) NOT NULL DEFAULT 'evm' COMMENT '合约vm类型 默认使用EVM类型',
     PRIMARY KEY (`contract_hash`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
