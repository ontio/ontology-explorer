DROP TABLE IF EXISTS tbl_token_daily_aggregation;
CREATE TABLE tbl_token_daily_aggregation (
    token_contract_hash    CHAR(42)       NOT NULL COMMENT 'Token合约hash',
    date_id                INT            NOT NULL COMMENT '日期维度ID',
    usd_price              DECIMAL(40, 9) NOT NULL DEFAULT 0 COMMENT '当天USD价格',
    tx_count               INT            NOT NULL DEFAULT 0 COMMENT '交易数量',
    tx_amount              DECIMAL(40, 20) NOT NULL DEFAULT 0 COMMENT '交易金额',
    deposit_address_count  INT            NOT NULL DEFAULT 0 COMMENT '去重入金地址数量',
    withdraw_address_count INT            NOT NULL DEFAULT 0 COMMENT '去重出金地址数量',
    tx_address_count       INT            NOT NULL DEFAULT 0 COMMENT '去重交易地址数量',
    fee_amount             DECIMAL(40, 9) NOT NULL DEFAULT 0 COMMENT '消耗手续费总额',
    update_time            TIMESTAMP      NOT NULL DEFAULT now() ON UPDATE now(),
    PRIMARY KEY (`token_contract_hash`, `date_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;