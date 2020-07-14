CREATE TABLE `tbl_inspire_calculation_params` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `second_round_incentive` decimal(25,9) DEFAULT NULL COMMENT '第二轮激励系数',
  `gas_fee` decimal(25,9) DEFAULT NULL COMMENT '预测一年手续费',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;