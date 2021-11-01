SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_orc721_tx_detail
-- ----------------------------

DROP TABLE IF EXISTS `tbl_orc721_tx_detail`;

CREATE TABLE `tbl_orc721_tx_detail` (
        `id`                   INT(11)         NOT NULL AUTO_INCREMENT,
        `tx_hash`              VARCHAR(66)     NOT NULL DEFAULT '' COMMENT '交易hash',
        `tx_type`              INT(11)         NOT NULL COMMENT '区块链交易类型，211 调用EVM 类型的交易',
        `tx_time`              INT(11)         NOT NULL COMMENT '交易时间戳',
        `block_height`         INT(11)         NOT NULL COMMENT '区块高度',
        `amount`               DECIMAL(40, 20) NOT NULL COMMENT '交易金额',
        `fee`                  DECIMAL(40, 20)  NOT NULL COMMENT '交易手续费',
        `asset_name`           VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '交易资产名',
        `from_address`         VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '交易fromaddress',
        `to_address`           VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '交易toaddress',
        `description`          VARCHAR(1000)   NOT NULL DEFAULT '' COMMENT '交易描述',
        `block_index`          INT(11)         NOT NULL COMMENT '交易在区块里的索引',
        `tx_index`             INT(11)         NOT NULL COMMENT '该event在交易eventlog里的索引',
        `confirm_flag`         INT(11)         NOT NULL COMMENT '交易落账标识  1：成功 0：失败',
        `event_type`           INT(11)         NOT NULL COMMENT '交易event类型 0:其他 3:转账 7 Approval',
        `contract_hash`        VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '该event对应的合约hash',
        `payer`                VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '交易的payer',
        `called_contract_hash` VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '该交易真正调用的合约hash',
        `token_id`          INT(11) NOT NULL DEFAULT 0 COMMENT '交易调用的token的id标识',
        PRIMARY KEY (`id`),
        UNIQUE KEY `idx_tx_hash_index` (`tx_hash`, `tx_index`),
        KEY `idx_block_height` (`block_height`) USING BTREE,
        KEY `idx_called_contract_hash_block_height` (`called_contract_hash`, `block_height`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;
