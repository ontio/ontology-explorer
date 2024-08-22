CREATE TABLE `tbl_bad_node`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `public_key` varchar(70) NOT NULL COMMENT '节点公钥',
    `cycle`      int(11) NOT NULL COMMENT '周期数',
    PRIMARY KEY (`id`),
    KEY          `idx_public_key` (`public_key`),
    KEY          `idx_cycle` (`cycle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;