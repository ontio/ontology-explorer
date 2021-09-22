

ALTER TABLE tbl_node_info_off_chain  ADD COLUMN `contact_info_verified` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已验证联系方式, 0未验证 1 已验证';
ALTER TABLE tbl_node_info_off_chain  ADD COLUMN `fee_sharing_ratio` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '收益分配比例是否稳定, 0不稳定 1 已验证 ';
ALTER TABLE tbl_node_info_off_chain  ADD COLUMN `ontology_harbinger` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否是ontology_harbinger, 0 不是, 1 已经成为  ';
ALTER TABLE tbl_node_info_off_chain  ADD COLUMN `old_node` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否老节点 0 否  1 是 ';

ALTER TABLE tbl_node_overview ADD COLUMN `left_time_to_next_rnd` INT(11) DEFAULT NULL COMMENT '本周期剩余时间';