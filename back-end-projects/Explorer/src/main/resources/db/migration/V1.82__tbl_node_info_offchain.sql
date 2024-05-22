ALTER TABLE tbl_node_info_off_chain CHANGE `node_type` `node_type` int(11) NOT NULL COMMENT '节点类型:1-候选节点;2-共识节点';

UPDATE tbl_node_info_off_chain SET node_type = 1 WHERE node_type NOT IN (1,2);