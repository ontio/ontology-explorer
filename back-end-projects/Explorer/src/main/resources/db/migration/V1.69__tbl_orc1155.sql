DROP TABLE IF EXISTS `tbl_orc1155`;
CREATE TABLE `tbl_orc1155`
(
    `contract_hash` varchar(255)   NOT NULL DEFAULT '' COMMENT '合约hash值',
    `token_id`      varchar(255)   NOT NULL DEFAULT '' COMMENT 'ORC1155的token id',
    `name`          varchar(255)   NOT NULL DEFAULT '' COMMENT 'ORC1155代币名称',
    `total_supply`  decimal(15, 0) NOT NULL COMMENT 'ORC1155代币总量',
    `symbol`        varchar(255)   NOT NULL DEFAULT '' COMMENT 'ORC1155代币符号',
    `create_time`   datetime       NOT NULL COMMENT '创建时间，yyyy-MM-dd',
    `audit_flag`    tinyint(1) NOT NULL COMMENT '审核标识，1：审核通过 0：未审核',
    `update_time`   datetime                DEFAULT NULL COMMENT '更新时间，yyyy-MM-dd',
    `vm_category`   varchar(255)   NOT NULL DEFAULT 'evm' COMMENT '合约vm类型 默认使用EVM类型',
    PRIMARY KEY (`contract_hash`, `token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
