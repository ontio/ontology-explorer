package com.github.ontio.mapper;

import com.github.ontio.model.dao.TxDetailDaily;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface TxDetailDailyMapper extends Mapper<TxDetailDaily> {

    void batchInsert(List<TxDetailDaily> list);

}