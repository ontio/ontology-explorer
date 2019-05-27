package com.github.ontio.mapper;

import com.github.ontio.model.dto.TxEventLogDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TxEventLogMapper extends Mapper<TxEventLogDto> {

    List<TxEventLogDto> selectTxsByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    List<TxEventLogDto> selectNonontidTxsByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    Integer queryTxCount(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

}