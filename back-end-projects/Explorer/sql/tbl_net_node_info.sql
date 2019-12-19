SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_net_node_info`;
CREATE TABLE IF NOT EXISTS `tbl_net_node_info`
(
    ip               VARCHAR(15) NOT NULL COMMENT '节点的IP',
    version          VARCHAR(50) NOT NULL DEFAULT '' COMMENT '节点的版本号',
    is_consensus     BOOLEAN     NOT NULL COMMENT '是否是共识节点',
    is_active        BOOLEAN     NOT NULL COMMENT '是否处于活跃状态',
    last_active_time BIGINT      NOT NULL COMMENT '最新的活跃时间',
    country          VARCHAR(20) NOT NULL DEFAULT '' COMMENT '节点所在的国家',
    longitude        VARCHAR(20) NOT NULL DEFAULT '' COMMENT '节点的经度',
    latitude         VARCHAR(20) NOT NULL DEFAULT '' COMMENT '节点的纬度',
    primary key (ip)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;