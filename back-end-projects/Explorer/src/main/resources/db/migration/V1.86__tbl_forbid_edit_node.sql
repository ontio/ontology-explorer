CREATE TABLE `tbl_forbid_edit_node`
(
    `public_key` varchar(70) NOT NULL COMMENT '节点公钥',
    `end_cycle`  int(11) NOT NULL COMMENT '直到该周期才能编辑',
    PRIMARY KEY (`public_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;