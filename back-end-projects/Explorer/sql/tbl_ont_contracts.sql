/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:56:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_ont_contracts
-- ----------------------------
DROP TABLE IF EXISTS `tbl_ont_contracts`;
CREATE TABLE `tbl_ont_contracts` (
  `contract` varchar(255) NOT NULL DEFAULT '' COMMENT '合约hash值',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '合约name',
  `txcount` int(10) NOT NULL COMMENT '合约总的交易量',
  `abi` text COMMENT '合约abi',
  `code` text COMMENT '合约code',
  `createtime` int(11) NOT NULL,
  `auditflag` int(1) NOT NULL COMMENT '审核标识，1：审核通过 0：未审核',
  `updatetime` int(11) DEFAULT NULL,
  `contactinfo` varchar(2000) NOT NULL DEFAULT '' COMMENT '合约项目方联系信息',
  `type` varchar(55) NOT NULL DEFAULT ''  COMMENT '合约类型，oep4，oep5，oep8等',
  `description` varchar(1500) NOT NULL DEFAULT '' COMMENT '合约描述',
  `logo` varchar(255) NOT NULL DEFAULT '' COMMENT '合约logo的url',
  `creator` varchar(1000) NOT NULL DEFAULT '' COMMENT '合约创建者',
  `addresscount` int(10) NOT NULL COMMENT '合约总的地址数',
  `ontcount` decimal(25,9) NOT NULL COMMENT '合约总的ont流通量',
  `ongcount` decimal(25,9) NOT NULL COMMENT '合约总的ong流通量',
  `project` varchar(255) NOT NULL DEFAULT '' COMMENT '合约所属项目名称',
  `tokencount` varchar(1000) NOT NULL DEFAULT '' COMMENT '合约总的token流通量',
  `dappstoreflag` int(1) NOT NULL DEFAULT '0' COMMENT 'dappstore标识。1：合约属于dappstore，0：合约不属于dappstore',
  `category` varchar(255) NOT NULL DEFAULT '' COMMENT '合约分类',
  `totalreward` decimal(25,9) NOT NULL DEFAULT '0.000000000' COMMENT '总激励',
  PRIMARY KEY (`contract`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
