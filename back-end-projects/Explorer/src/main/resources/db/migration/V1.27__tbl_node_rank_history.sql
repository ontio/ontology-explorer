SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_rank_history`;
CREATE TABLE IF NOT EXISTS `tbl_node_rank_history` (
    public_key   VARCHAR(70) NOT NULL COMMENT '节点的公钥',
    address      VARCHAR(34) NOT NULL COMMENT '节点的地址',
    name         VARCHAR(64) NOT NULL DEFAULT '' COMMENT '节点的名称',
    node_rank    INT         NOT NULL COMMENT '节点的排名',
    block_height BIGINT      NOT NULL COMMENT '与节点排名相对应的区块高度',
    PRIMARY KEY (public_key, block_height)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;
