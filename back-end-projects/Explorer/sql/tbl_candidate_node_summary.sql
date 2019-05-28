SET FOREIGN_KEY_CHECKS = 0;

-- --------------------------------
-- Table structure for tbl_candidate_node
-- --------------------------------

DROP TABLE IF EXISTS `tbl_candidate_node_summary`;
CREATE TABLE IF NOT EXISTS tbl_candidate_node_summary
(
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    name            VARCHAR(64)  NOT NULL DEFAULT '',
    node_rank       INT          NOT NULL,
    current_stake   BIGINT       NOT NULL,
    progress        VARCHAR(10)  NOT NULL,
    detail_url      VARCHAR(110) NOT NULL,
    node_index      INT          NOT NULL,
    public_key      VARCHAR(70)  NOT NULL,
    address         VARCHAR(34)  NOT NULL,
    status          INT          NOT NULL,
    init_pos        BIGINT       NOT NULL,
    total_pos       BIGINT       NOT NULL,
    max_authorize   BIGINT       NOT NULL,
    node_proportion VARCHAR(64)  NOT NULL,
    primary key (id)
);