SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_overview`;
CREATE TABLE `tbl_node_overview` (
    `id`                 INT    NOT NULL COMMENT '主键',
    `blk_cnt_to_nxt_rnd` BIGINT NOT NULL COMMENT '距离下一个质押周期的区块数',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

INSERT INTO `tbl_node_overview`
VALUES (1, 1);