SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_rank_change`;
CREATE TABLE IF NOT EXISTS `tbl_node_rank_change`
(
    public_key  VARCHAR(70) NOT NULL,
    address     VARCHAR(34) NOT NULL,
    name        VARCHAR(64) NOT NULL,
    rank_change INT         NOT NULL,
    PRIMARY KEY (public_key)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
