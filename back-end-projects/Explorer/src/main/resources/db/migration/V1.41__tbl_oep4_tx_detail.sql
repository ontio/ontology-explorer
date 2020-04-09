-- ----------------------------
-- Change index structure for tbl_oep4_tx_detail
-- ----------------------------
DROP INDEX idx_called_contract_hash ON tbl_oep4_tx_detail;
CREATE INDEX idx_called_contract_hash_block_height ON tbl_oep4_tx_detail (`called_contract_hash`, `block_height`);