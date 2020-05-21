ALTER TABLE tbl_contract ADD re_sync_status INT NOT NULL DEFAULT 0 COMMENT '重新同步状态：0 - 等待重新同步；1 - 已重新同步';
ALTER TABLE tbl_contract ADD re_sync_from_block INT NOT NULL DEFAULT 0 COMMENT '重新同步起始高度';
ALTER TABLE tbl_contract ADD re_sync_to_block INT NOT NULL DEFAULT 0 COMMENT '重新同步结束高度';