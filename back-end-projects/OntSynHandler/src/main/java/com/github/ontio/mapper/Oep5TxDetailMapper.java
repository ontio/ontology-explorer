package com.github.ontio.mapper;

import com.github.ontio.model.dao.Oep5TxDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface Oep5TxDetailMapper extends Mapper<Oep5TxDetail> {

    void deleteByHeight(@Param("blockHeight") int height);

    int selectCountByHeight(@Param("blockHeight") int height);

    void batchInsert(List<Oep5TxDetail> list);


}