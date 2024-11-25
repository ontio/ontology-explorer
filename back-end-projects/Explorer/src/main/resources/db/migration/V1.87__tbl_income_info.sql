CREATE TABLE `tbl_income_info`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `peer_pub_key` char(80) NOT NULL,
    `address`      char(80) NOT NULL,
    `ong_income`   varchar(50) DEFAULT NULL,
    `staking_pos`  int(11) DEFAULT NULL,
    `cycle`        int(11) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY            `idx_peer_address` (`peer_pub_key`,`address`) USING BTREE,
    KEY            `idx_address` (`address`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;