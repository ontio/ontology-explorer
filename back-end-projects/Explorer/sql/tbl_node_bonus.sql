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

INSERT INTO `tbl_node_bonus`
VALUES ('022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb', 'APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ',
        'Alioth', 91.20, 1561910400);
INSERT INTO `tbl_node_bonus`
VALUES ('022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb', 'APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ',
        'Alioth', 103.4, 1559318400);
INSERT INTO `tbl_node_bonus`
VALUES ('022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb', 'APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ',
        'Alioth', 52.6, 1556640000);
INSERT INTO `tbl_node_bonus`
VALUES ('022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb', 'APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ',
        'Alioth', 84.8, 1554048000);
INSERT INTO `tbl_node_bonus`
VALUES ('022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb', 'APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ',
        'Alioth', 76.4, 1551369600);
INSERT INTO `tbl_node_bonus`
VALUES ('022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb', 'APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ',
        'Alioth', 125.4, 1548950400);
INSERT INTO `tbl_node_bonus`
VALUES ('022bf80145bd448d993abffa237f4cd06d9df13eaad37afce5cb71d80c47b03feb', 'APSFBEbQzMUjuCtSVwHcRjiqCrDe56jAHJ',
        'Alioth', 140.2, 1546272000);

INSERT INTO `tbl_node_bonus`
VALUES ('02765d98bb092962734e365bd436bdc80c5b5991dcf22b28dbb02d3b3cf74d6444', 'AWWChRewNcQ5nZuh8LzF8ksqPaCW8EXPBU',
        'Mixar', 61.2, 1561910400);
INSERT INTO `tbl_node_bonus`
VALUES ('02765d98bb092962734e365bd436bdc80c5b5991dcf22b28dbb02d3b3cf74d6444', 'AWWChRewNcQ5nZuh8LzF8ksqPaCW8EXPBU',
        'Mixar', 51.6, 1559318400);
INSERT INTO `tbl_node_bonus`
VALUES ('02765d98bb092962734e365bd436bdc80c5b5991dcf22b28dbb02d3b3cf74d6444', 'AWWChRewNcQ5nZuh8LzF8ksqPaCW8EXPBU',
        'Alioth', 49.2, 1556640000);
INSERT INTO `tbl_node_bonus`
VALUES ('02765d98bb092962734e365bd436bdc80c5b5991dcf22b28dbb02d3b3cf74d6444', 'AWWChRewNcQ5nZuh8LzF8ksqPaCW8EXPBU',
        'Mixar', 81.4, 1554048000);
INSERT INTO `tbl_node_bonus`
VALUES ('02765d98bb092962734e365bd436bdc80c5b5991dcf22b28dbb02d3b3cf74d6444', 'AWWChRewNcQ5nZuh8LzF8ksqPaCW8EXPBU',
        'Mixar', 121.8, 1551369600);
INSERT INTO `tbl_node_bonus`
VALUES ('02765d98bb092962734e365bd436bdc80c5b5991dcf22b28dbb02d3b3cf74d6444', 'AWWChRewNcQ5nZuh8LzF8ksqPaCW8EXPBU',
        'Mixar', 143.6, 1548950400);
INSERT INTO `tbl_node_bonus`
VALUES ('02765d98bb092962734e365bd436bdc80c5b5991dcf22b28dbb02d3b3cf74d6444', 'AWWChRewNcQ5nZuh8LzF8ksqPaCW8EXPBU',
        'Mixar', 171.2, 1546272000);

INSERT INTO `tbl_node_bonus`
VALUES ('03c8f63775536eb420c96228cdccc9de7d80e87f1b562a6eb93c0838064350aa53', 'AXNxyP2HEKW7GoSqYfeqcYfCSE7XaaVVu4',
        'Alkaid', 84.2, 1561910400);
INSERT INTO `tbl_node_bonus`
VALUES ('03c8f63775536eb420c96228cdccc9de7d80e87f1b562a6eb93c0838064350aa53', 'AXNxyP2HEKW7GoSqYfeqcYfCSE7XaaVVu4',
        'Alkaid', 93, 1559318400);
INSERT INTO `tbl_node_bonus`
VALUES ('03c8f63775536eb420c96228cdccc9de7d80e87f1b562a6eb93c0838064350aa53', 'AXNxyP2HEKW7GoSqYfeqcYfCSE7XaaVVu4',
        'Alkaid', 76.6, 1556640000);
INSERT INTO `tbl_node_bonus`
VALUES ('03c8f63775536eb420c96228cdccc9de7d80e87f1b562a6eb93c0838064350aa53', 'AXNxyP2HEKW7GoSqYfeqcYfCSE7XaaVVu4',
        'Alkaid', 92, 1554048000);
INSERT INTO `tbl_node_bonus`
VALUES ('03c8f63775536eb420c96228cdccc9de7d80e87f1b562a6eb93c0838064350aa53', 'AXNxyP2HEKW7GoSqYfeqcYfCSE7XaaVVu4',
        'Alkaid', 101.2, 1551369600);
INSERT INTO `tbl_node_bonus`
VALUES ('03c8f63775536eb420c96228cdccc9de7d80e87f1b562a6eb93c0838064350aa53', 'AXNxyP2HEKW7GoSqYfeqcYfCSE7XaaVVu4',
        'Alkaid', 130.2, 1548950400);
INSERT INTO `tbl_node_bonus`
VALUES ('03c8f63775536eb420c96228cdccc9de7d80e87f1b562a6eb93c0838064350aa53', 'AXNxyP2HEKW7GoSqYfeqcYfCSE7XaaVVu4',
        'Alkaid', 154.8, 1546272000);

INSERT INTO `tbl_node_bonus`
VALUES ('022e911fb5a20b4b2e4f917f10eb92f27d17cad16b916bce8fd2dd8c11ac2878c0', 'AJEAVCJpa7JmpDZsJ9vPA1r9fPZAvjec8D',
        'Phecda', 68.2, 1561910400);
INSERT INTO `tbl_node_bonus`
VALUES ('022e911fb5a20b4b2e4f917f10eb92f27d17cad16b916bce8fd2dd8c11ac2878c0', 'AJEAVCJpa7JmpDZsJ9vPA1r9fPZAvjec8D',
        'Phecda', 76.8, 1559318400);
INSERT INTO `tbl_node_bonus`
VALUES ('022e911fb5a20b4b2e4f917f10eb92f27d17cad16b916bce8fd2dd8c11ac2878c0', 'AJEAVCJpa7JmpDZsJ9vPA1r9fPZAvjec8D',
        'Phecda', 92, 1556640000);
INSERT INTO `tbl_node_bonus`
VALUES ('022e911fb5a20b4b2e4f917f10eb92f27d17cad16b916bce8fd2dd8c11ac2878c0', 'AJEAVCJpa7JmpDZsJ9vPA1r9fPZAvjec8D',
        'Phecda', 94.8, 1554048000);
INSERT INTO `tbl_node_bonus`
VALUES ('022e911fb5a20b4b2e4f917f10eb92f27d17cad16b916bce8fd2dd8c11ac2878c0', 'AJEAVCJpa7JmpDZsJ9vPA1r9fPZAvjec8D',
        'Phecda', 75.2, 1551369600);
INSERT INTO `tbl_node_bonus`
VALUES ('022e911fb5a20b4b2e4f917f10eb92f27d17cad16b916bce8fd2dd8c11ac2878c0', 'AJEAVCJpa7JmpDZsJ9vPA1r9fPZAvjec8D',
        'Phecda', 147.4, 1548950400);
INSERT INTO `tbl_node_bonus`
VALUES ('022e911fb5a20b4b2e4f917f10eb92f27d17cad16b916bce8fd2dd8c11ac2878c0', 'AJEAVCJpa7JmpDZsJ9vPA1r9fPZAvjec8D',
        'Phecda', 168.8, 1546272000);