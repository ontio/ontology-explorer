/*
Navicat MySQL Data Transfer



Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2019-04-12 10:56:00
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_ont_contracts
-- ----------------------------
DROP TABLE IF EXISTS `tbl_contract`;
CREATE TABLE `tbl_contract` (
    `contract_hash`   VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约hash值',
    `name`            VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '名称',
    `abi`             TEXT           NOT NULL COMMENT '合约abi',
    `code`            TEXT           NOT NULL COMMENT '合约code',
    `source_code`     TEXT           NOT NULL COMMENT '合约源码',
    `create_time`     INT(11)        NOT NULL COMMENT '创建时间戳',
    `update_time`     INT(11)                 DEFAULT NULL COMMENT '更新时间戳',
    `audit_flag`      TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '审核标识，1：审核通过 0：未审核',
    `contact_info`    VARCHAR(1000)  NOT NULL DEFAULT '' COMMENT '合约项目方联系信息.json格式字符串',
    `description`     VARCHAR(1000)  NOT NULL DEFAULT '' COMMENT '合约描述',
    `type`            VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约类型，oep4，oep5，oep8，others等',
    `logo`            VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约logo的url',
    `creator`         VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约创建者',
    `address_count`   INT(11)        NOT NULL COMMENT '该合约的总的地址数 ',
    `tx_count`        INT(11)        NOT NULL COMMENT '合约总的交易量',
    `ont_sum`         DECIMAL(25, 9) NOT NULL COMMENT '该合约的总的ont流通量',
    `ong_sum`         DECIMAL(25, 9) NOT NULL COMMENT '该合约的总的ong流通量',
    `token_sum`       VARCHAR(255)   NOT NULL COMMENT '该合约的总的token流通量.json格式字符串',
    `category`        VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约分类',
    `dapp_name`       VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约所属Dapp名称',
    `dappstore_flag`  TINYINT(1)     NOT NULL DEFAULT '0' COMMENT 'Dappstore审核标识。1：合约属于dappstore，0：合约不属于dappstore',
    `total_reward`    DECIMAL(25, 9) NOT NULL DEFAULT '0.000000000' COMMENT '总激励',
    `lastweek_reward` DECIMAL(25, 9) NOT NULL DEFAULT '0.000000000' COMMENT '上周激励',
    PRIMARY KEY (`contract_hash`) USING BTREE
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;


ALTER TABLE tbl_contract
    ADD COLUMN channel VARCHAR(255) NOT NULL DEFAULT 'syn-service' COMMENT '提交渠道';



-- baice update

ALTER TABLE tbl_contract ADD COLUMN `compiler_type` VARCHAR(255) DEFAULT '' COMMENT '编译器类型';
ALTER TABLE tbl_contract ADD COLUMN `compiler_version` VARCHAR(255) DEFAULT '' COMMENT '编译器版本';
ALTER TABLE tbl_contract ADD COLUMN `vm_type` VARCHAR(255) DEFAULT '' COMMENT '虚拟机类型';
ALTER TABLE tbl_contract ADD COLUMN `vm_version` VARCHAR(255) DEFAULT '' COMMENT '虚拟机版本';
