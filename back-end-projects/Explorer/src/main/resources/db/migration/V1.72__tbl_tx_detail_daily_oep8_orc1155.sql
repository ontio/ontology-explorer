ALTER TABLE tbl_tx_detail_daily ADD index `idx_contract_hash`(`contract_hash`);
ALTER TABLE tbl_oep8 ADD COLUMN `collection` varchar(255) NOT NULL DEFAULT '' COMMENT 'NFT集合名称' AFTER `token_id`;
ALTER TABLE tbl_orc1155 ADD COLUMN `collection` varchar(255) NOT NULL DEFAULT '' COMMENT 'NFT集合名称' AFTER `token_id`;