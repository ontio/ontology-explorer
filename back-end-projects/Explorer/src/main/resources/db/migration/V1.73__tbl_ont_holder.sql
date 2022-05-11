CREATE TABLE IF NOT EXISTS `tbl_holder_height` (
    `id` int(11) NOT NULL COMMENT '主键',
    `height` int(11) DEFAULT '-1' COMMENT '块高',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `tbl_holder_contract` (
    `contract_hash` varchar(255) NOT NULL DEFAULT '' COMMENT '合约hash值',
    `type` varchar(20) NOT NULL DEFAULT '' COMMENT '代币类型',
    `vm_type` varchar(20) NOT NULL COMMENT 'vm类型',
    `holder_count` int(11) DEFAULT NULL COMMENT '持有人数量',
    `re_sync_status` int(2) NOT NULL DEFAULT '0' COMMENT '重新同步:0-无需；1-需要；2-完成',
    `re_sync_from_block` int(11) DEFAULT NULL COMMENT '重新同步开始块高',
    `re_sync_to_block` int(11) DEFAULT NULL COMMENT '重新同步结束块高',
    PRIMARY KEY (`contract_hash`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `tbl_native_holder` (
    `address` varchar(48) NOT NULL COMMENT '地址',
    `contract` varchar(48) NOT NULL COMMENT '合约',
    `balance` varchar(255) DEFAULT NULL COMMENT '余额',
    PRIMARY KEY (`address`,`contract`) USING BTREE,
    KEY `idx_balance` (`balance`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `tbl_oep4_holder` (
    `address` varchar(48) NOT NULL COMMENT '地址',
    `contract` varchar(48) NOT NULL COMMENT '合约',
    `balance` varchar(255) DEFAULT NULL COMMENT '余额',
    PRIMARY KEY (`address`,`contract`) USING BTREE,
    KEY `idx_balance` (`balance`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `tbl_orc20_holder` (
    `address` varchar(48) NOT NULL COMMENT '地址',
    `contract` varchar(48) NOT NULL COMMENT '合约',
    `balance` varchar(255) DEFAULT NULL COMMENT '余额',
    PRIMARY KEY (`address`,`contract`) USING BTREE,
    KEY `idx_balance` (`balance`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;