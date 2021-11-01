DROP TABLE IF EXISTS tbl_address_daily_aggregation;
CREATE TABLE tbl_address_daily_aggregation (
    address                CHAR(80)       NOT NULL COMMENT '地址',
    token_contract_hash    CHAR(42)       NOT NULL COMMENT 'Token合约hash',
    date_id                INT            NOT NULL COMMENT '日期维度ID',
    balance                DECIMAL(40, 20) NOT NULL DEFAULT 0 COMMENT '日终余额',
    usd_price              DECIMAL(40, 20) NOT NULL DEFAULT 0 COMMENT '当天USD价格',
    max_balance            DECIMAL(40, 20) NOT NULL DEFAULT 0 COMMENT '最高余额',
    min_balance            DECIMAL(40, 20) NOT NULL DEFAULT 0 COMMENT '最低余额',
    deposit_tx_count       INT            NOT NULL DEFAULT 0 COMMENT '入金交易数量',
    withdraw_tx_count      INT            NOT NULL DEFAULT 0 COMMENT '出金交易数量',
    deposit_amount         DECIMAL(40, 20) NOT NULL DEFAULT 0 COMMENT '入金金额',
    withdraw_amount        DECIMAL(40, 20) NOT NULL DEFAULT 0 COMMENT '出金金额',
    deposit_address_count  INT            NOT NULL DEFAULT 0 COMMENT '去重入金地址数量',
    withdraw_address_count INT            NOT NULL DEFAULT 0 COMMENT '去重出金地址数量',
    tx_address_count       INT            NOT NULL DEFAULT 0 COMMENT '去重交易地址数量',
    fee_amount             DECIMAL(40, 20) NOT NULL DEFAULT 0 COMMENT '消耗手续费总额',
    contract_count         INT            NOT NULL DEFAULT 0 COMMENT '调用合约数量，只适用于虚拟Token统计',
    is_virtual             BOOLEAN        NOT NULL DEFAULT FALSE COMMENT '是否为虚拟Token统计',
    update_time            TIMESTAMP      NOT NULL DEFAULT now() ON UPDATE now(),
    PRIMARY KEY (`address`, `token_contract_hash`, `date_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;