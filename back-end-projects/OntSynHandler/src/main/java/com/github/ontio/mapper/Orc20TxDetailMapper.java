package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc20TxDetail;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface Orc20TxDetailMapper extends Mapper<Orc20TxDetail> {

    void batchInsert(List<Orc20TxDetail> list);

}