DROP TABLE IF EXISTS tbl_ranking;
CREATE TABLE tbl_ranking (
    ranking_group    SMALLINT       NOT NULL COMMENT '排名分组，由业务系统决定',
    ranking_id       SMALLINT       NOT NULL COMMENT '具体排名，由业务系统决定',
    ranking_duration SMALLINT       NOT NULL COMMENT '排名区间，1：24小时；3：3天；7：7天',
    ranking          SMALLINT       NOT NULL COMMENT '排名数，1 - N',
    member           VARCHAR(80)    NOT NULL COMMENT '排名对象，根据不同的排名有不同含义',
    amount           DECIMAL(40, 20) NOT NULL COMMENT '排名数值',
    percentage       DECIMAL(4, 2)  NOT NULL DEFAULT 0 COMMENT '占比',
    update_time      TIMESTAMP      NOT NULL DEFAULT now() ON UPDATE now(),
    PRIMARY KEY (`ranking_group`, `ranking_id`, `ranking_duration`, `ranking`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;