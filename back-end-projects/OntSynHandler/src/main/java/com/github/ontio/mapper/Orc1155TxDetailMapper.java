package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc1155TxDetail;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface Orc1155TxDetailMapper extends Mapper<Orc1155TxDetail> {

    void batchInsert(List<Orc1155TxDetail> list);

}