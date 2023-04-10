alter table tbl_node_info_off_chain add bad_actor tinyint(1) default 0 null comment 'bad actor节点标签，前十个周期内任一周期fee ratio下调超过50%';

alter table tbl_node_info_off_chain add risky tinyint(1) default 0 null comment '风险提示标签单次fee sharing向上调整超过50%';