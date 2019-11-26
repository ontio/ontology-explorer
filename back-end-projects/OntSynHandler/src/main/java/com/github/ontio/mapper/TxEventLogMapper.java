package com.github.ontio.mapper;

import com.github.ontio.model.dao.TxEventLog;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface TxEventLogMapper extends Mapper<TxEventLog> {

    void batchInsert(List<TxEventLog> list);

}