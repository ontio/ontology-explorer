DROP TABLE IF EXISTS tbl_node_overview_history;
CREATE TABLE `tbl_node_overview_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rnd_start_blk` bigint(20) DEFAULT NULL COMMENT '周期开始区块',
  `rnd_end_blk` bigint(20) DEFAULT NULL COMMENT '周期结束区块',
  `rnd_start_time` int(11) DEFAULT NULL COMMENT '周期开始时间',
  `rnd_end_time` int(11) DEFAULT NULL COMMENT '周期结束时间',
  PRIMARY KEY (`id`),
  KEY `idx_start_blc` (`rnd_start_blk`),
  KEY `idx_end_blc` (`rnd_end_blk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;