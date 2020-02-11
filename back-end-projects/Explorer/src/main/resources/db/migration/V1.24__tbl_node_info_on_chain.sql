SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_info_on_chain`;
CREATE TABLE IF NOT EXISTS `tbl_node_info_on_chain` (
    node_rank                INT          NOT NULL COMMENT '节点排名',
    name                     VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '节点名称',
    current_stake            BIGINT       NOT NULL COMMENT '节点的当前质押总量',
    progress                 VARCHAR(10)  NOT NULL COMMENT '节点的质押进度',
    detail_url               VARCHAR(110) NOT NULL COMMENT '节点详情的查看地址',
    public_key               VARCHAR(70)  NOT NULL COMMENT '节点公钥',
    address                  VARCHAR(34)  NOT NULL COMMENT '节点钱包地址',
    status                   INT          NOT NULL COMMENT '节点类型，1是候选节点，2是共识节点',
    init_pos                 BIGINT       NOT NULL COMMENT '节点初始质押量',
    total_pos                BIGINT       NOT NULL COMMENT '用户当前质押量',
    max_authorize            BIGINT       NOT NULL COMMENT '用户最大质押量',
    node_proportion          VARCHAR(64)  NOT NULL COMMENT '手续费分配比例',
    current_stake_percentage VARCHAR(20)  NOT NULL COMMENT '用户质押进度',
    PRIMARY KEY (node_rank)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;