SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_net_node_info`;
CREATE TABLE IF NOT EXISTS `tbl_net_node_info`
(
    ip               VARCHAR(15) NOT NULL,
    version          VARCHAR(50) NOT NULL DEFAULT '',
    is_consensus     BOOLEAN     NOT NULL,
    is_active        BOOLEAN     NOT NULL,
    last_active_time BIGINT      NOT NULL,
    country          VARCHAR(20) NOT NULL DEFAULT '',
    longitude        VARCHAR(20) NOT NULL DEFAULT '',
    latitude         VARCHAR(20) NOT NULL DEFAULT '',
    primary key (ip)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;