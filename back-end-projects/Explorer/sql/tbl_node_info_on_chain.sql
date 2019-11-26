SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_info_on_chain`;
CREATE TABLE IF NOT EXISTS `tbl_node_info_on_chain`
(
    node_rank                INT          NOT NULL,
    name                     VARCHAR(64)  NOT NULL DEFAULT '',
    current_stake            BIGINT       NOT NULL,
    progress                 VARCHAR(10)  NOT NULL,
    detail_url               VARCHAR(110) NOT NULL,
    public_key               VARCHAR(70)  NOT NULL,
    address                  VARCHAR(34)  NOT NULL,
    status                   INT          NOT NULL,
    init_pos                 BIGINT       NOT NULL,
    total_pos                BIGINT       NOT NULL,
    max_authorize            BIGINT       NOT NULL,
    node_proportion          VARCHAR(64)  NOT NULL,
    current_stake_percentage VARCHAR(20)  NOT NULL,
    primary key (node_rank)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;