package com.github.ontio.mapper;

import com.github.ontio.model.Oep8TxnDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface Oep8TxnDetailMapper {
    int insert(Oep8TxnDetail record);

    int insertSelective(Oep8TxnDetail record);

    int updateByPrimaryKeySelective(Oep8TxnDetail record);

    int updateByPrimaryKey(Oep8TxnDetail record);

    void deleteByHeight(int height);

    int selectCountByHeight(@Param("height") int height);
}