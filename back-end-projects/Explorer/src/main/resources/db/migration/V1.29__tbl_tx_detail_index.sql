-- ----------------------------
-- Table structure for tbl_tx_detail_index
-- ----------------------------
DROP TABLE IF EXISTS `tbl_tx_detail_index`;
CREATE TABLE `tbl_tx_detail_index` (
    `address`              CHAR(80)     NOT NULL DEFAULT '' COMMENT '交易地址',
    `desc_block_height`    INT(11)      NOT NULL COMMENT '倒序区块高度',
    `tx_hash`              VARCHAR(66)  NOT NULL DEFAULT '' COMMENT '交易hash',
    `tx_index`             INT(11)      NOT NULL COMMENT '该event在交易eventlog里的索引',
    `called_contract_hash` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '该交易真正调用的合约hash',
    `tx_time`              INT(11)      NOT NULL COMMENT '交易时间戳',
    `event_type`           INT(11)      NOT NULL COMMENT '交易event类型 0:其他 1:部署合约 2:手续费 3:转账 4:ONT ID 5:存证 6:权限',
    `asset_name`           VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '交易资产名',
    `tx_direction`         TINYINT      NOT NULL COMMENT '交易方向 0:FROM 1:TO 2:BOTH',
    PRIMARY KEY (`address`, `desc_block_height`, `tx_hash`, `tx_index`),
    KEY `idx_contract_block_height`(`called_contract_hash`, `desc_block_height`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
