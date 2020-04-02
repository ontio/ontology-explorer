SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_rank_change`;
CREATE TABLE IF NOT EXISTS `tbl_node_rank_change` (
    public_key          VARCHAR(70) NOT NULL COMMENT '节点的公钥',
    address             VARCHAR(34) NOT NULL COMMENT '节点的钱包地址',
    name                VARCHAR(64) NOT NULL COMMENT '节点的名称',
    rank_change         INT         NOT NULL COMMENT '节点当前排名距离上一个质押周期结束时的排名变化',
    change_block_height BIGINT      NOT NULL COMMENT '',
    PRIMARY KEY (public_key)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;
