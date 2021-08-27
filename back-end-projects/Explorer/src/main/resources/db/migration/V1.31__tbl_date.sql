DROP TABLE IF EXISTS numbers_small;
CREATE TABLE numbers_small (
    number INT
);
INSERT INTO numbers_small
VALUES (0),
(1),
(2),
(3),
(4),
(5),
(6),
(7),
(8),
(9);

DROP TABLE IF EXISTS numbers;
CREATE TABLE numbers (
    number BIGINT
);
INSERT INTO numbers
SELECT thousands.number * 1000 + hundreds.number * 100 + tens.number * 10 + ones.number
FROM numbers_small thousands, numbers_small hundreds, numbers_small tens, numbers_small ones
LIMIT 1000000;

DROP TABLE IF EXISTS tbl_date;
CREATE TABLE tbl_date (
    id           INT PRIMARY KEY,
    date         DATE     NOT NULL COMMENT '实际日期',
    year         SMALLINT NOT NULL COMMENT '年',
    month        SMALLINT NOT NULL COMMENT '月份',
    day_of_year  SMALLINT NOT NULL COMMENT '一年中的第几天',
    day_of_month SMALLINT NOT NULL COMMENT '一个月中的第几天',
    weekday      SMALLINT NOT NULL COMMENT '周几，0 - 6',
    week_of_year SMALLINT NOT NULL COMMENT '一年中的第几周',
    is_weekend   BOOLEAN  NOT NULL COMMENT '是否周末',
    UNIQUE KEY `idx_date`(`date`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

INSERT INTO tbl_date
SELECT id,
    date,
    date_format(date, '%Y') AS year,
    date_format(date, '%c') AS month,
    date_format(date, '%y') AS day_of_year,
    date_format(date, '%e') AS day_of_month,
    date_format(date, '%w') AS weekday,
    date_format(date, '%u') AS week_of_year,
    if(date_format(date, '%w') IN (0, 6), TRUE, FALSE) AS is_weekend
FROM (SELECT number + 1 AS id, DATE_ADD('2015-01-01', INTERVAL number DAY) AS date
      FROM numbers
      WHERE DATE_ADD('2015-01-01', INTERVAL number DAY) BETWEEN '2015-01-01' AND '2999-12-31'
      ORDER BY number) d;

DROP TABLE numbers_small;
DROP TABLE numbers;