-- ----------------------------
-- table structure for view_pro_transactions
-- ----------------------------
drop view if exists view_ont_transaction;


CREATE VIEW view_ont_transaction AS SELECT DISTINCT
	`a`.`txnhash` AS `txnhash`,
	`a`.`height` AS `height`,
	`a`.`txntype` AS `txntype`,
	`a`.`txntime` AS `txntime`,
	`a`.`description` AS `description`,
	`a`.`blockindex` AS `blockindex`,
	`a`.`confirmflag` AS `confirmflag`,
	`a`.`fee` AS `fee`
FROM
	`tbl_ont_txn_detail` `a`
WHERE `a`.`description` <> 'gasconsume'
	