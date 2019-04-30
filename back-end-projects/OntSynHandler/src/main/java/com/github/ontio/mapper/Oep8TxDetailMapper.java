package com.github.ontio.mapper;

import com.github.ontio.model.dao.Oep8TxDetail;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface Oep8TxDetailMapper extends Mapper<Oep8TxDetail> {

    void deleteByHeight(@Param("blockHeight") int height);

    int selectCountByHeight(@Param("blockHeight") int height);

}