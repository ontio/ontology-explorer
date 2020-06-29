-- ----------------------------
-- Change index structure for tbl_tx_eventlog
-- ----------------------------
DROP INDEX idx_called_contract_hash ON tbl_tx_eventlog;
CREATE INDEX idx_called_contract_hash_block_height ON tbl_tx_eventlog (`called_contract_hash`, `block_height`);
