CREATE TABLE `tbl_ong_supply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `ong_supply` decimal(25,9) DEFAULT ''0.000000000'' COMMENT ''ong流通总量'',
  `cycle` int(11) DEFAULT NULL COMMENT ''计算截止周期数'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;