-- ----------------------------
-- Table structure for tbl_address_blacklist
-- ----------------------------
DROP TABLE IF EXISTS `tbl_address_blacklist`;
CREATE TABLE `tbl_address_blacklist` (
  `address` varchar(255) NOT NULL DEFAULT '',
  `note` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
