-- ----------------------------
-- Change index structure for tbl_oep5_tx_detail
-- ----------------------------
DROP INDEX idx_called_contract_hash ON tbl_oep5_tx_detail;
CREATE INDEX idx_called_contract_hash_block_height ON tbl_oep5_tx_detail(`called_contract_hash`, `block_height`);
