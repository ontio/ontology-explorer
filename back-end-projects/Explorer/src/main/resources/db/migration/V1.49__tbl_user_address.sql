ALTER TABLE tbl_user_address add COLUMN amount_threshold DECIMAL (40,20) NOT NULL DEFAULT 0 COMMENT '转账金额阈值';
ALTER TABLE tbl_user_address add COLUMN channel varchar(255) NOT NULL DEFAULT 'explorer' COMMENT '登记渠道。explorer，onto';
