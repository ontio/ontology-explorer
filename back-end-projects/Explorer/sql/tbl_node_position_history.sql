SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_position_history`;
CREATE TABLE IF NOT EXISTS `tbl_node_position_history`
(
    public_key   VARCHAR(70) NOT NULL,
    address      VARCHAR(34) NOT NULL,
    name         VARCHAR(64) NOT NULL,
    node_rank    INT         NOT NULL,
    block_height BIGINT      NOT NULL,
    PRIMARY KEY (public_key, block_height)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `tbl_node_position_history`
VALUES ('02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7', 'AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1',
        'Dubhe', 5, 6250000);

INSERT INTO `tbl_node_position_history`
VALUES ('02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7', 'AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1',
        'Dubhe', 28, 6130000);

INSERT INTO `tbl_node_position_history`
VALUES ('0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4', 'AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci',
        'Merak', 1, 6250000);
INSERT INTO `tbl_node_position_history`
VALUES ('0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4', 'AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci',
        'Merak', 15, 6130000);

INSERT INTO `tbl_node_position_history`
VALUES ('0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa', 'AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq',
        'Megrez', 3, 6250000);
INSERT INTO `tbl_node_position_history`
VALUES ('0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa', 'AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq',
        'Megrez', 20, 6130000);

INSERT INTO `tbl_node_position_history`
VALUES ('022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb', 'APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ',
        'Alioth', 5, 6250000);
INSERT INTO `tbl_node_position_history`
VALUES ('022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb', 'APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ',
        'Alioth', 12, 6130000);