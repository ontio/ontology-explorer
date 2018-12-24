DROP TABLE IF EXISTS `tbl_ont_block`;
CREATE TABLE `tbl_ont_block` (
  `height` int(11) NOT NULL,
  `hash` varchar(64) NOT NULL DEFAULT '',
  `prevblock` varchar(64) NOT NULL DEFAULT '',
  `nextblock` varchar(64) NOT NULL DEFAULT '',
  `txnsroot` varchar(64) NOT NULL DEFAULT '',
  `blocktime` int(11) NOT NULL,
  `consensusdata` varchar(20) NOT NULL DEFAULT '',
  `bookkeeper` varchar(500) NOT NULL DEFAULT '',
  `txnnum` int(10) NOT NULL,
  `blocksize` int(10) NOT NULL,
  PRIMARY KEY (`height`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE tbl_ont_block ADD  index idx_hash (hash);

DROP TABLE IF EXISTS `tbl_ont_current`;
CREATE TABLE `tbl_ont_current` (
  `height` int(12) NOT NULL,
  `txncount` int(12) NOT NULL,
  `ontidcount` int(12) NOT NULL,
  `nonontidtxncount` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into tbl_ont_current(height,txncount,ontidcount,nonontidtxncount) values(-1,0,0,0);

DROP TABLE IF EXISTS `tbl_ont_oep4`;
CREATE TABLE `tbl_ont_oep4` (
  `contract` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `totalsupply` decimal(30,0) NOT NULL,
  `symbol` varchar(255) NOT NULL DEFAULT '',
  `decimals` decimal(20,0) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT '',
  `contactinfo` varchar(2000) NOT NULL DEFAULT '',
  `createtime` datetime NOT NULL,
  `auditflag` int(1) NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  `logo` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`contract`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tbl_ont_oep4txn_detail`;
CREATE TABLE `tbl_ont_oep4txn_detail` (
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
  `eventtype` int(2) NOT NULL COMMENT '0:其他 1:手续费 2:部署合约 3:转账',
  `contracthash` varchar(60) NOT NULL DEFAULT '',
  PRIMARY KEY (`txnhash`,`txnindex`),
  KEY `idx_fromaddr` (`fromaddress`),
  KEY `idx_toaddr` (`toaddress`),
  KEY `idx_height` (`height`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tbl_ont_oep8`;
CREATE TABLE `tbl_ont_oep8` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contract` varchar(255) NOT NULL DEFAULT '',
  `tokenid` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `totalsupply` decimal(30,0) NOT NULL,
  `symbol` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `contactinfo` varchar(2000) NOT NULL DEFAULT '',
  `createtime` datetime NOT NULL,
  `auditflag` int(1) NOT NULL DEFAULT 0,
  `updatetime` datetime DEFAULT NULL,
  `logo` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `idx_contract` (`contract`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tbl_ont_oep8txn_detail`;
CREATE TABLE `tbl_ont_oep8txn_detail` (
  `txnhash` varchar(64) NOT NULL DEFAULT '',
  `txntype` int(4) NOT NULL,
  `txntime` int(11) NOT NULL,
  `height` int(12) NOT NULL,
  `tokenname` varchar(64) NOT NULL,
  `amount` decimal(40,9) NOT NULL,
  `fee` decimal(25,9) NOT NULL DEFAULT '0.000000000',
  `assetname` varchar(255) NOT NULL DEFAULT '',
  `fromaddress` varchar(255) NOT NULL DEFAULT '',
  `toaddress` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(1500) NOT NULL DEFAULT '',
  `blockindex` int(10) NOT NULL,
  `txnindex` int(10) NOT NULL,
  `confirmflag` int(1) NOT NULL,
  `eventtype` int(2) NOT NULL COMMENT '0:其他 1:手续费 2:部署合约 3:转账',
  `contracthash` varchar(60) NOT NULL DEFAULT '',
  PRIMARY KEY (`txnhash`,`txnindex`),
  KEY `idx_fromaddr` (`fromaddress`),
  KEY `idx_toaddr` (`toaddress`),
  KEY `idx_height` (`height`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tbl_ont_ontid_detail`;
CREATE TABLE `tbl_ont_ontid_detail` (
  `txnhash` varchar(64) NOT NULL DEFAULT '',
  `txntype` int(4) NOT NULL,
  `ontid` varchar(255) NOT NULL DEFAULT '',
  `txntime` int(11) NOT NULL,
  `height` int(12) NOT NULL,
  `description` varchar(255) NOT NULL DEFAULT '',
  `fee` decimal(25,8) NOT NULL DEFAULT '0.00000000',
  PRIMARY KEY (`txnhash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE tbl_ont_ontid_detail ADD INDEX idx_ontid (ontid);

DROP TABLE IF EXISTS `tbl_ont_txn_detail`;
CREATE TABLE `tbl_ont_txn_detail` (
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

DROP TABLE IF EXISTS `tbl_ont_oep5`;
CREATE TABLE `tbl_ont_oep5` (
  `contract` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `totalsupply` decimal(30,0) NOT NULL,
  `symbol` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `contactinfo` varchar(2000) NOT NULL DEFAULT '',
  `createtime` datetime NOT NULL,
  `auditflag` int(1) NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  `logo` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`contract`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tbl_ont_oep5txn_detail`;
CREATE TABLE `tbl_ont_oep5txn_detail` (
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
  `eventtype` int(2) NOT NULL COMMENT '0:其他 1:手续费 2:部署合约 3:转账',
  `contracthash` varchar(60) NOT NULL DEFAULT '',
  PRIMARY KEY (`txnhash`,`txnindex`),
  KEY `idx_fromaddr` (`fromaddress`),
  KEY `idx_toaddr` (`toaddress`),
  KEY `idx_height` (`height`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tbl_ont_contracts`;
CREATE TABLE `tbl_ont_contracts` (
  `project` varchar(255) NOT NULL DEFAULT '',
  `contract` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `txcount` int(10) NOT NULL,
  `abi` text,
  `code` text,
  `createtime` int(11) NOT NULL,
  `auditflag` int(1) NOT NULL,
  `updatetime` int(11) DEFAULT NULL,
  `contactinfo` varchar(2000) NOT NULL DEFAULT '',
  `type` varchar(55) NOT NULL DEFAULT '',
  `description` varchar(1500) NOT NULL DEFAULT '',
  `logo` varchar(255) NOT NULL DEFAULT '',
  `creator` varchar(1000) NOT NULL DEFAULT '',
  `addresscount` int(10) NOT NULL,
  `ontcount` decimal(25,9) NOT NULL,
  `ongcount` decimal(25,9) NOT NULL,
  PRIMARY KEY (`contract`)
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

DROP TABLE IF EXISTS `tbl_ont_oep5_dragon`;
CREATE TABLE `tbl_ont_oep5_dragon` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `contract` varchar(255) NOT NULL DEFAULT '',
  `assertname` varchar(255) NOT NULL DEFAULT '',
  `jsonurl` varchar(255) NOT NULL,
  PRIMARY KEY (`contract`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;