ALTER TABLE `tbl_node_info_off_chain` ADD COLUMN `telegram` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '节点方电报用户名';
ALTER TABLE `tbl_node_info_off_chain` ADD COLUMN `twitter` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '节点方推特';
ALTER TABLE `tbl_node_info_off_chain` ADD COLUMN `facebook` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '节点方脸书';
ALTER TABLE `tbl_node_info_off_chain` ADD COLUMN `open_mail` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '公开的邮箱';
ALTER TABLE `tbl_node_info_off_chain` ADD COLUMN `contact_mail` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '联系本体的邮箱';
ALTER TABLE `tbl_node_info_off_chain` ADD COLUMN `open_flag` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否公开展示，0：不公开 1：公开';
ALTER TABLE `tbl_node_info_off_chain` ADD COLUMN `verification` int(2) NOT NULL DEFAULT 0 COMMENT '认证类型，0：未认证 1：本体认证';

UPDATE tbl_node_info_off_chain SET open_flag = 1;
UPDATE tbl_node_info_off_chain SET verification = 1;