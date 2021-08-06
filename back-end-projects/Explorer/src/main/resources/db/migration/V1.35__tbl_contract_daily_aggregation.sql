DROP TABLE IF EXISTS tbl_contract_daily_aggregation;
CREATE TABLE tbl_contract_daily_aggregation (
    contract_hash          CHAR(80)       NOT NULL COMMENT '合约hash',
    token_contract_hash    CHAR(42)       NOT NULL COMMENT 'Token合约hash',
    date_id                INT            NOT NULL COMMENT '日期维度ID',
    tx_count               INT            NOT NULL DEFAULT 0 COMMENT '交易数量',
    tx_amount              DECIMAL(40, 20) NOT NULL DEFAULT 0 COMMENT '交易金额',
    deposit_address_count  INT            NOT NULL DEFAULT 0 COMMENT '去重入金交易地址数量',
    withdraw_address_count INT            NOT NULL DEFAULT 0 COMMENT '去重出金交易地址数量',
    tx_address_count       INT            NOT NULL DEFAULT 0 COMMENT '去重交易地址数量',
    fee_amount             DECIMAL(40, 9) NOT NULL DEFAULT 0 COMMENT '消耗手续费总额',
    contract_count         INT            NOT NULL DEFAULT 0 COMMENT '调用合约数量，只适用于虚拟Token统计',
    is_virtual             BOOLEAN        NOT NULL DEFAULT FALSE COMMENT '是否为虚拟Token统计',
    update_time            TIMESTAMP      NOT NULL DEFAULT now() ON UPDATE now(),
    PRIMARY KEY (`contract_hash`, `token_contract_hash`, `date_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;