alter table `tbl_address_daily_summary` add index contract_address_index(`contract_hash`, `address`);

ALTER TABLE tbl_tx_detail_index ADD COLUMN `block_index` int(11) NOT NULL COMMENT '交易在区块里的索引' AFTER `tx_hash`;