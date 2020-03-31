-- ----------------------------
-- Table structure for tbl_oep_logo
-- ----------------------------
DROP TABLE IF EXISTS `tbl_oep_logo`;
CREATE TABLE `tbl_oep_logo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contract_hash` varchar(255) NOT NULL DEFAULT '' COMMENT '合约hash',
  `type` varchar(255) NOT NULL DEFAULT '' COMMENT 'oep5，oep8',
  `token_id` varchar(255) NOT NULL DEFAULT '' COMMENT '合约token id',
  `logo` varchar(255) NOT NULL DEFAULT '' COMMENT 'logo链接',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT 'token名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_contracthash_tokenid` (`contract_hash`,`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
