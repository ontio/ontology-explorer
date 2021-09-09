SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `tbl_node_cycle_data`;
CREATE TABLE `tbl_node_cycle_data` (
       `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
       `public_key` VARCHAR(70) NOT NULL COMMENT '节点公钥',
       `address` VARCHAR(34) NOT NULL COMMENT '节点钱包地址',
       `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '节点名称',
       `status` INT(11) NOT NULL COMMENT ' 节点变更状态 0 新注册, 1 正常运行, 2退出状态 ',
       `node_type` INT(11) NOT NULL COMMENT ' 节点状态 0 注册候选节点1 候选节点 2共识节点3 已退出  ',
       `node_proportion_t` VARCHAR(64) NOT NULL COMMENT 't周期手续费分配比例',
       `user_proportion_t` VARCHAR(64) NOT NULL COMMENT 't周期用户质押部分分配比例',
       `node_proportion_t2` VARCHAR(64) NOT NULL COMMENT 't2周期手续费分配比例',
       `user_proportion_t2` VARCHAR(64) NOT NULL COMMENT 't2周期用户质押部分分配比例',
       `max_authorize` INT(11) NOT NULL DEFAULT 0 COMMENT '用户质押数额上限',
       `bonus_ong` DECIMAL(40, 9) NOT NULL COMMENT 'ong激励, 9位精度',
       `node_stake_ont` INT(11) NOT NULL COMMENT '节点实际质押的ONT数量',
       `user_stake_ont` INT(11) NOT NULL COMMENT '用户实际质押的ONT数量',
       `cycle` INT(11) NOT NULL COMMENT '周期数',
       PRIMARY KEY (`id`),
       KEY `public_key_index` (`public_key`, `cycle`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

