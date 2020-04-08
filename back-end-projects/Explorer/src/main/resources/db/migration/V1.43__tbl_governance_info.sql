DROP TABLE IF EXISTS `tbl_governance_info`;
CREATE TABLE `tbl_governance_info` (
    peer_pub_key CHAR(80) NOT NULL COMMENT '',
    address CHAR(80) NOT NULL COMMENT '地址',
    consensus_pos INT COMMENT '',
    candidate_pos INT COMMENT '',
    new_pos INT COMMENT '',
    withdraw_consensus_pos INT COMMENT '',
    withdraw_candidate_pos INT COMMENT '',
    withdraw_unfreeze_pos INT COMMENT '',
    PRIMARY KEY (`peer_pub_key`, `address`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;