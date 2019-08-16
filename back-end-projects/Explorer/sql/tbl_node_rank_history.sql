SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_rank_history`;
CREATE TABLE IF NOT EXISTS `tbl_node_rank_history`
(
    public_key   VARCHAR(70) NOT NULL,
    address      VARCHAR(34) NOT NULL,
    name         VARCHAR(64) NOT NULL DEFAULT '',
    node_rank    INT         NOT NULL,
    block_height BIGINT      NOT NULL,
    PRIMARY KEY (public_key, block_height)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
