DROP INDEX idx_address_contract_hash ON tbl_tx_detail_index;
CREATE INDEX idx_contract_hash_block_height ON tbl_tx_detail_index(`called_contract_hash`, `desc_block_height`);