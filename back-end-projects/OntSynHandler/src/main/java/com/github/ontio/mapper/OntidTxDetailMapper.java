package com.github.ontio.mapper;

import com.github.ontio.model.dao.OntidTxDetail;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface OntidTxDetailMapper extends Mapper<OntidTxDetail> {

    void deleteByHeight(@Param("blockHeight") int height);

    int selectCountByHeight(@Param("blockHeight") int height);

}