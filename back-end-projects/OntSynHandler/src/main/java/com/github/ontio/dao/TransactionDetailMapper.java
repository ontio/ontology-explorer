package com.github.ontio.dao;

import com.github.ontio.model.TransactionDetail;
import com.github.ontio.model.TransactionDetailKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component
public interface TransactionDetailMapper {
    int deleteByPrimaryKey(TransactionDetailKey key);

    int insert(TransactionDetail record);

    int insertSelective(TransactionDetail record);

    TransactionDetail selectByPrimaryKey(TransactionDetailKey key);

    int updateByPrimaryKeySelective(TransactionDetail record);

    int updateByPrimaryKey(TransactionDetail record);

    void deleteByHeight(int height);

    int selectCountByHeight(@Param("height") int height);

}