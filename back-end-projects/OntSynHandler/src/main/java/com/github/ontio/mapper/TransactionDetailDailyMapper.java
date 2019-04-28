package com.github.ontio.mapper;

import com.github.ontio.model.TransactionDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface TransactionDetailDailyMapper {
    int deleteByPrimaryKey(TransactionDetail key);

    int insert(TransactionDetail record);

    int insertSelective(TransactionDetail record);

    TransactionDetail selectByPrimaryKey(TransactionDetail key);

    int updateByPrimaryKeySelective(TransactionDetail record);

    int updateByPrimaryKey(TransactionDetail record);

    void deleteByHeight(int height);

    int selectCountByHeight(@Param("height") int height);
}