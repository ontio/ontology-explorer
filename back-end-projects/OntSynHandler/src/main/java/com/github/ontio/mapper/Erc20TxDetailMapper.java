package com.github.ontio.mapper;

import com.github.ontio.model.dao.Erc20TxDetail;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface Erc20TxDetailMapper extends Mapper<Erc20TxDetail> {

    void batchInsert(List<Erc20TxDetail> list);

}