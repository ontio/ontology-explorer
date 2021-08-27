ALTER  TABLE  `tbl_ontid_tx_detail`  DROP KEY idx_tx_hash_ontid;
ALTER  TABLE  `tbl_ontid_tx_detail`  ADD  UNIQUE `idx_tx_hash_ontid` (`tx_hash`,`ontid`,`description`);