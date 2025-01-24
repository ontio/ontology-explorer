ALTER TABLE tbl_income_info ADD COLUMN withdraw_pos int(11) DEFAULT NULL AFTER staking_pos;
ALTER TABLE tbl_income_info ADD COLUMN new_pos int(11) DEFAULT NULL AFTER withdraw_pos;
ALTER TABLE tbl_income_info ADD COLUMN next_withdrawable_pos int(11) DEFAULT NULL AFTER new_pos;
ALTER TABLE tbl_income_info ADD COLUMN peer int(2) DEFAULT NULL AFTER next_withdrawable_pos;

CREATE TABLE `tbl_node_staking_info` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `peer_pub_key` char(80) NOT NULL COMMENT '节点公钥',
    `address` char(80) NOT NULL COMMENT '质押地址',
    `amount` int(11) NOT NULL COMMENT '数量',
    `action` varchar(50) NOT NULL COMMENT '质押行为',
    `node_status` int(2) NOT NULL COMMENT '1-候选节点;2-共识节点',
    `block_height` int(11) NOT NULL COMMENT '块高',
    `tx_hash` varchar(100) NOT NULL COMMENT '交易hash',
    `cycle` int(11) NOT NULL COMMENT '周期',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_peer_address` (`peer_pub_key`,`address`) USING BTREE,
    KEY `idx_address` (`address`,`cycle`) USING BTREE,
    KEY `idx_tx_hash` (`tx_hash`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;