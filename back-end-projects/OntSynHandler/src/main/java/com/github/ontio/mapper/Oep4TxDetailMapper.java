package com.github.ontio.mapper;

import com.github.ontio.model.dao.Oep4TxDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
@Component
public interface Oep4TxDetailMapper extends Mapper<Oep4TxDetail> {

    void deleteByHeight(@Param("blockHeight") int height);

    int selectCountByHeight(@Param("blockHeight") int height);

}