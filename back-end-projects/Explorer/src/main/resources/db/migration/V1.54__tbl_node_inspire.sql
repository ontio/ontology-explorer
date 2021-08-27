DROP TABLE IF EXISTS tbl_node_inspire;
CREATE TABLE `tbl_node_inspire` (
  `public_key` varchar(70) NOT NULL COMMENT '节点公钥',
  `address` varchar(34) NOT NULL COMMENT '节点钱包地址',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '节点名称',
  `status` int(11) NOT NULL COMMENT '节点类型，1是候选节点，2是共识节点',
  `current_stake` bigint(20) NOT NULL COMMENT '节点的当前质押总量',
  `init_pos` bigint(20) NOT NULL COMMENT '节点初始质押量',
  `total_pos` bigint(20) NOT NULL COMMENT '用户当前质押量',
  `node_release_inspire` bigint(20) NOT NULL COMMENT '节点释放ONG激励',
  `node_release_inspire_rate` varchar(100) NOT NULL COMMENT '节点释放ONG激励收益率',
  `user_release_inspire` bigint(20) NOT NULL COMMENT '用户释放ONG激励',
  `user_release_inspire_rate` varchar(100) NOT NULL COMMENT '用户释放ONG激励收益率',
  `node_commission_inspire` bigint(20) NOT NULL COMMENT '节点手续费激励',
  `node_commission_inspire_rate` varchar(100) NOT NULL COMMENT '节点手续费激励收益率',
  `user_commission_inspire` bigint(20) NOT NULL COMMENT '用户手续费激励',
  `user_commission_inspire_rate` varchar(100) NOT NULL COMMENT '用户手续费激励收益率',
  `node_foundation_inspire` bigint(20) NOT NULL COMMENT '节点基金会激励',
  `node_foundation_inspire_rate` varchar(100) NOT NULL COMMENT '节点基金会激励收益率',
  `user_foundation_inspire` bigint(20) NOT NULL COMMENT '用户基金会激励',
  `user_foundation_inspire_rate` varchar(100) NOT NULL COMMENT '用户基金会激励收益率',
  PRIMARY KEY (`public_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;