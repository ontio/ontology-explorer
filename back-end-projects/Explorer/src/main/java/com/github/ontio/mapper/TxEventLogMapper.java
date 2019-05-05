package com.github.ontio.mapper;

import com.github.ontio.model.dao.TxEventLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface TxEventLogMapper extends Mapper<TxEventLog> {
}