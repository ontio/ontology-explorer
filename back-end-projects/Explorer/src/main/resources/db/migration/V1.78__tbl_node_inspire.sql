ALTER TABLE tbl_node_inspire ADD COLUMN `node_apy` varchar(100) DEFAULT NULL COMMENT '节点年化收益';
ALTER TABLE tbl_node_inspire ADD COLUMN `user_apy` varchar(100) DEFAULT NULL COMMENT '用户年化收益';
ALTER TABLE tbl_contract MODIFY COLUMN `token_sum` VARCHAR(5000) NOT NULL COMMENT '该合约的总的token流通量.json格式字符串';