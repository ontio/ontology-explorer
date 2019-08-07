SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_bonus`;
CREATE TABLE IF NOT EXISTS `tbl_node_bonus`
(
    public_key VARCHAR(70) NOT NULL,
    address    VARCHAR(34) NOT NULL,
    name       VARCHAR(64) NOT NULL,
    bonus      DOUBLE      NOT NULL,
    unix_time  BIGINT      NOT NULL,
    INDEX address (address),
    PRIMARY KEY (public_key, unix_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `tbl_node_bonus`
VALUES ('02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7', 'AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1',
        'Dubhe', 75.8, 1561910400);
INSERT INTO `tbl_node_bonus`
VALUES ('02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7', 'AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1',
        'Dubhe', 73.2, 1559318400);
INSERT INTO `tbl_node_bonus`
VALUES ('02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7', 'AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1',
        'Dubhe', 64.4, 1556640000);
INSERT INTO `tbl_node_bonus`
VALUES ('02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7', 'AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1',
        'Dubhe', 85.8, 1554048000);
INSERT INTO `tbl_node_bonus`
VALUES ('02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7', 'AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1',
        'Dubhe', 94.8, 1551369600);
INSERT INTO `tbl_node_bonus`
VALUES ('02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7', 'AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1',
        'Dubhe', 89.8, 1548950400);
INSERT INTO `tbl_node_bonus`
VALUES ('02bcdd278a27e4969d48de95d6b7b086b65b8d1d4ff6509e7a9eab364a76115af7', 'AGqzuKoEeDfMHPEBPJVs2h2fapxDGoGtK1',
        'Dubhe', 100.20, 1546272000);

INSERT INTO `tbl_node_bonus`
VALUES ('0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4', 'AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci',
        'Merak', 88.6, 1561910400);
INSERT INTO `tbl_node_bonus`
VALUES ('0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4', 'AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci',
        'Merak', 78.8, 1559318400);
INSERT INTO `tbl_node_bonus`
VALUES ('0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4', 'AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci',
        'Merak', 61.6, 1556640000);
INSERT INTO `tbl_node_bonus`
VALUES ('0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4', 'AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci',
        'Merak', 94.8, 1554048000);
INSERT INTO `tbl_node_bonus`
VALUES ('0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4', 'AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci',
        'Merak', 101.4, 1551369600);
INSERT INTO `tbl_node_bonus`
VALUES ('0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4', 'AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci',
        'Merak', 133.4, 1548950400);
INSERT INTO `tbl_node_bonus`
VALUES ('0251f06bc247b1da94ec7d9fe25f5f913cedaecba8524140353b826cf9b1cbd9f4', 'AGEdeZu965DFFFwsAWcThgL6uduJf4U7ci',
        'Merak', 154.8, 1546272000);

INSERT INTO `tbl_node_bonus`
VALUES ('0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa', 'AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq',
        'Megrez', 82.6, 1561910400);
INSERT INTO `tbl_node_bonus`
VALUES ('0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa', 'AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq',
        'Megrez', 82.6, 1559318400);
INSERT INTO `tbl_node_bonus`
VALUES ('0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa', 'AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq',
        'Megrez', 69.4, 1556640000);
INSERT INTO `tbl_node_bonus`
VALUES ('0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa', 'AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq',
        'Megrez', 75.8, 1554048000);
INSERT INTO `tbl_node_bonus`
VALUES ('0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa', 'AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq',
        'Megrez', 76.4, 1551369600);
INSERT INTO `tbl_node_bonus`
VALUES ('0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa', 'AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq',
        'Megrez', 116.20, 1548950400);
INSERT INTO `tbl_node_bonus`
VALUES ('0253719ac66d7cafa1fe49a64f73bd864a346da92d908c19577a003a8a4160b7fa', 'AUy6TaM9wxTqo9T7FiaYMnDeVExhjsR1Pq',
        'Megrez', 134.0, 1546272000);
