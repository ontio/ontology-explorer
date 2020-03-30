-- ----------------------------
-- Table structure for tbl_user_address
-- ----------------------------
DROP TABLE IF EXISTS `tbl_user_address`;
CREATE TABLE `tbl_user_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ont_id` varchar(255) NOT NULL DEFAULT '' COMMENT 'ONT ID',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '地址',
  `note` varchar(255) NOT NULL DEFAULT '' COMMENT '地址备注',
  `strategy` int(11) NOT NULL DEFAULT '0' COMMENT '监听策略。0:不监听 1:监听所有入金出金，2:只监听入金 3:只监听出金',
  `include_oep_token` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1:监听oep资产  0:不监听oep资产',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_ontid_address` (`address`,`ont_id`),
  KEY `fkey_ontId` (`ont_id`),
  CONSTRAINT `fkey_ontId` FOREIGN KEY (`ont_id`) REFERENCES `tbl_user` (`ont_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
