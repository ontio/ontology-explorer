package com.github.ontio.mapper;

import com.github.ontio.model.dao.TxDetailDaily;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface TxDetailDailyMapper extends Mapper<TxDetailDaily> {

    void deleteByHeight(@Param("blockHeight") int height);

    int selectCountByHeight(@Param("blockHeight") int height);

    void batchInsert(List<TxDetailDaily> list);


}