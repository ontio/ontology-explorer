package com.github.ontio.mapper;

import com.github.ontio.model.dao.OntidTxDetail;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface OntidTxDetailMapper extends Mapper<OntidTxDetail> {

    void batchInsert(List<OntidTxDetail> list);

}