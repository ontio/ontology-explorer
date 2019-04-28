package com.github.ontio.mapper;

import com.github.ontio.model.dto.TxDetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Component
public interface TxDetailMapper extends Mapper<TxDetailDto> {

    List<TxDetailDto> selectTxsByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    List<TxDetailDto> selectNonontidTxsByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    TxDetailDto selectTxByHash(@Param("txHash") String txHash);

    List<TxDetailDto> selectTransferTxDetailByHash(@Param("txHash") String txHash);

    Integer selectLatestONTTransferTxTime(@Param("address") String address);

    List<Map> selectTransferTxByToAddr(Map<String, Object> param);

    List<Map> selectTransferTxByFromAddr(Map<String, Object> param);

    Integer selectTxCountByAddr(@Param("address") String address);

}