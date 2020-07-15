DROP TABLE IF EXISTS tbl_inspire_calculation_params;
CREATE TABLE `tbl_inspire_calculation_params` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `total_fp_fu` decimal(25,12) DEFAULT NULL COMMENT '7个共识团队所有质押',
  `total_sr` decimal(25,12) DEFAULT NULL COMMENT '第二轮再分配的权益量',
  `gas_fee` decimal(25,12) DEFAULT NULL COMMENT '预测一年手续费',
  `ont_price` decimal(25,12) DEFAULT NULL COMMENT 'ont价格',
  `ong_price` decimal(25,12) DEFAULT NULL COMMENT 'ong价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;