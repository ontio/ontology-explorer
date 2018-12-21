CREATE PROCEDURE add_tbl_ont_contracts()
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS
            WHERE TABLE_NAME='tbl_ont_contracts'
            AND COLUMN_NAME='project')
    THEN
          ALTER TABLE tbl_ont_contracts ADD COLUMN project varchar(255) NOT NULL DEFAULT '' COMMENT '项目名称';
        END IF;
  END;

CALL add_tbl_ont_contracts;
DROP PROCEDURE add_tbl_ont_contracts;


DROP TABLE IF EXISTS `tbl_ont_contract_summary`;
CREATE TABLE `tbl_ont_contract_summary` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`time` int(11) NOT NULL,
`contracthash` varchar(64) NOT NULL DEFAULT '',
`txncount` int(10) NOT NULL,
`ontcount` decimal(25,9) NOT NULL,
`ongcount` decimal(25,9) NOT NULL,
`activeaddress` int(10) NOT NULL,
`newaddress` int(10) NOT NULL,
PRIMARY KEY (`id`),
KEY `idx_time` (`time`),
KEY `idx_contracthash` (`contracthash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tbl_ont_daily_summary`;
CREATE TABLE `tbl_ont_daily_summary` (
`time` int(11) NOT NULL,
`blockcount` int(10) NOT NULL,
`txncount` int(10) NOT NULL,
`ontidactivecount` int(10) NOT NULL,
`ontidnewcount` int(10) NOT NULL,
`ontcount` decimal(25,9) NOT NULL,
`ongcount` decimal(25,9) NOT NULL,
`activeaddress` int(10) NOT NULL,
`newaddress` int(10) NOT NULL,
PRIMARY KEY (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tbl_ont_txn_detail_tmp`;
CREATE TABLE `tbl_ont_txn_detail_tmp` (
`txnhash` varchar(64) NOT NULL DEFAULT '',
`txntype` int(4) NOT NULL,
`txntime` int(11) NOT NULL,
`height` int(12) NOT NULL,
`amount` decimal(40,9) NOT NULL,
`fee` decimal(25,9) NOT NULL DEFAULT '0.000000000',
`assetname` varchar(255) NOT NULL DEFAULT '',
`fromaddress` varchar(255) NOT NULL DEFAULT '',
`toaddress` varchar(255) NOT NULL DEFAULT '',
`description` varchar(1500) NOT NULL DEFAULT '',
`blockindex` int(10) NOT NULL,
`txnindex` int(10) NOT NULL,
`confirmflag` int(1) NOT NULL,
`eventtype` int(2) NOT NULL COMMENT '0:其他 1:手续费 2:部署合约 3:转账 4:ontid 5:存证 6:权限',
`contracthash` varchar(60) NOT NULL DEFAULT '',
PRIMARY KEY (`txnhash`,`txnindex`),
KEY `idx_eventtype` (`eventtype`),
KEY `idx_fromaddr` (`fromaddress`),
KEY `idx_toaddr` (`toaddress`),
KEY `idx_height` (`height`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tbl_ont_address_summary`;
CREATE TABLE `tbl_ont_address_summary` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `time` int(11) NOT NULL,
  `type` varchar(64) NOT NULL COMMENT 'common:普通转账 contracthash：合约地址',
  `address` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `idx_time` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
