package com.github.ontio.dao;

import com.github.ontio.model.Oep4TxnDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface Oep4TxnDetailMapper {
    int insert(Oep4TxnDetail record);

    int insertSelective(Oep4TxnDetail record);

    int updateByPrimaryKeySelective(Oep4TxnDetail record);

    int updateByPrimaryKey(Oep4TxnDetail record);

    void deleteByHeight(int height);

    int selectCountByHeight(@Param("height") int height);
}