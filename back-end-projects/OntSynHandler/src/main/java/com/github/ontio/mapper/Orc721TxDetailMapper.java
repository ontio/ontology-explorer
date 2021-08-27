package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc721TxDetail;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface Orc721TxDetailMapper extends Mapper<Orc721TxDetail> {

    void batchInsert(List<Orc721TxDetail> list);

}