SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_info_off_chain`;
CREATE TABLE IF NOT EXISTS `tbl_node_info_off_chain` (
    name         VARCHAR(64)   NOT NULL DEFAULT '' COMMENT '节点名称',
    address      CHARACTER(34) NOT NULL COMMENT '节点钱包地址',
    ont_id       CHARACTER(42) NOT NULL COMMENT '节点的 ONT ID',
    public_key   VARCHAR(70)   NOT NULL COMMENT '节点的公钥',
    node_type    INT           NOT NULL COMMENT '节点类型（官网后台中定义的数据，现在没有意义）',
    introduction VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '节点介绍',
    logo_url     VARCHAR(125)  NOT NULL DEFAULT '' COMMENT '节点logo的地址',
    region       VARCHAR(20)   NOT NULL DEFAULT '' COMMENT '节点所处的地区',
    longitude    DECIMAL(5, 2) NOT NULL DEFAULT 0 COMMENT '节点所处位置的经度',
    latitude     DECIMAL(4, 2) NOT NULL DEFAULT 0 COMMENT '节点所处位置的纬度',
    ip           VARCHAR(15)   NOT NULL DEFAULT '' COMMENT '节点的 IP 信息',
    website      VARCHAR(60)   NOT NULL DEFAULT '' COMMENT '节点的网站',
    social_media VARCHAR(60)   NOT NULL DEFAULT '' COMMENT '节点的社媒信息',
    PRIMARY KEY (public_key)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;