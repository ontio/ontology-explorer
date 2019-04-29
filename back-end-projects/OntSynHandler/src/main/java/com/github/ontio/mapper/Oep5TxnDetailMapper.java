package com.github.ontio.mapper;

import com.github.ontio.model.Oep5TxnDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface Oep5TxnDetailMapper {
    int insert(Oep5TxnDetail record);

    int insertSelective(Oep5TxnDetail record);

    int updateByPrimaryKeySelective(Oep5TxnDetail record);

    int updateByPrimaryKey(Oep5TxnDetail record);

    void deleteByHeight(int height);

    int selectCountByHeight(@Param("height") int height);
}