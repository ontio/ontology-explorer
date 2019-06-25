SET FOREIGN_KEY_CHECKS = 0;

-- --------------------------------
-- Table structure for tbl_candidate_node
-- --------------------------------

DROP TABLE IF EXISTS `tbl_node_info_register`;
CREATE TABLE IF NOT EXISTS `tbl_node_info_register`
(
    name         VARCHAR(64)   NOT NULL DEFAULT '',
    address      CHARACTER(34) NOT NULL,
    ont_id       CHARACTER(42) NOT NULL,
    public_key   VARCHAR(70)   NOT NULL,
    node_type    INT           NOT NULL,
    introduction VARCHAR(1000) NOT NULL DEFAULT '',
    logo_url     VARCHAR(125)  NOT NULL DEFAULT '',
    region       VARCHAR(20)   NOT NULL DEFAULT '',
    longitude    DECIMAL(5, 2) NOT NULL DEFAULT 0,
    latitude     DECIMAL(4, 2) NOT NULL DEFAULT 0,
    ip           VARCHAR(15)   NOT NULL DEFAULT '',
    website      VARCHAR(60)   NOT NULL DEFAULT '',
    primary key (public_key)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;