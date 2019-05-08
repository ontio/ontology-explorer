package com.github.ontio.mapper;

import com.github.ontio.model.dto.TxBasicDto;
import com.github.ontio.model.dto.TxDetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface TxDetailMapper extends Mapper<TxDetailDto> {

    // self-defined SQL

    List<TxDetailDto> selectTxsByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    List<TxDetailDto> selectNonontidTxsByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    TxDetailDto selectTxByHash(@Param("txHash") String txHash);

    List<TxDetailDto> selectTransferTxDetailByHash(@Param("txHash") String txHash);

    Integer selectLatestONTTransferTxTime(@Param("address") String address);

    List<Map> selectTransferTxByToAddr(Map<String, Object> param);

    List<Map> selectTransferTxByFromAddr(Map<String, Object> param);

    Integer selectTxCountByAddr(@Param("address") String address);

    Integer queryTransactionCount(Map<String, Object> param);

    List<TxBasicDto> selectTxsByBlockHeight(@Param("blockHeight") Integer blockHeight);


    List<TxDetailDto> selectTxsByCalledContractHash(@Param("calledContractHash") String contractHash, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    Integer selectCountByCalledContracthash(@Param("calledContractHash") String calledContractHash);


}