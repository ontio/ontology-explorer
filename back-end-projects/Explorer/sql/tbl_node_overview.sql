SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_overview`;
CREATE TABLE `tbl_node_overview`
(
    `id`                 INT    NOT NULL,
    `blk_cnt_to_nxt_rnd` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO `tbl_node_overview`
VALUES (1, 1);