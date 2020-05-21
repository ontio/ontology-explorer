package com.github.ontio.mapper;

import com.github.ontio.model.dao.TxDetail;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface TxDetailMapper extends Mapper<TxDetail> {

    void batchInsert(List<TxDetail> list);
    
    Integer findMissingFromBlock(String contractHash);
    
    Integer findMissingToBlock(String contractHash);
    
}