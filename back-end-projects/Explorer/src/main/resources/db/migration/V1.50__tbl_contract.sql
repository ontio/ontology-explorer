ALTER TABLE tbl_contract
    ADD re_sync_status INT NOT NULL DEFAULT 0 COMMENT '重新同步状态：0 - 无需重新同步；1 - 需要重新同步；2 - 重新同步完成；3 - 完成重新统计';
ALTER TABLE tbl_contract
    ADD re_sync_from_block INT NOT NULL DEFAULT 0 COMMENT '重新同步起始高度';
ALTER TABLE tbl_contract
    ADD re_sync_to_block INT NOT NULL DEFAULT 0 COMMENT '重新同步结束高度';
ALTER TABLE tbl_contract
    ADD re_sync_stat_block INT NOT NULL DEFAULT 0 COMMENT '重新统计完成高度';