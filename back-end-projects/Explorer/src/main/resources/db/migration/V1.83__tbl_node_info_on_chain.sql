ALTER TABLE tbl_node_info_on_chain ADD INDEX idx_public_key (public_key);
ALTER TABLE tbl_node_info_off_chain ADD INDEX idx_address (address);