-- 重建tbl_ontid_tx_detail的索引
ALTER TABLE tbl_ontid_tx_detail DROP INDEX idx_tx_hash;
CREATE UNIQUE INDEX idx_tx_hash_ontid ON tbl_ontid_tx_detail (`tx_hash`,`ontid`);
