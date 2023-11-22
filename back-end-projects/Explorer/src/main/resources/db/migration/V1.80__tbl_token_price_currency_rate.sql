CREATE TABLE `tbl_token_price`
(
    `symbol`             varchar(50) NOT NULL COMMENT 'token符号',
    `price`              decimal(60, 20) DEFAULT NULL COMMENT 'token兑USDT价格',
    `percent_change_24h` decimal(60, 20) DEFAULT NULL COMMENT '24小时价格变化',
    `rank`               int(11) DEFAULT NULL COMMENT '排名',
    `update_time`        datetime        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `tbl_currency_rate`
(
    `currency_name` varchar(20) NOT NULL COMMENT '法币名称',
    `price`         decimal(64, 20) DEFAULT NULL COMMENT '价格',
    `update_time`   datetime        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`currency_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;