package com.github.ontio.mapper;

import com.github.ontio.model.dao.TxDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface TxDetailMapper extends Mapper<TxDetail> {

    void deleteByHeight(@Param("blockHeight") int height);

    int selectCountByHeight(@Param("blockHeight") int height);
}