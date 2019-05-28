package com.github.ontio.mapper;

import com.github.ontio.model.dto.TransferTxDto;
import com.github.ontio.model.dto.TxBasicDto;
import com.github.ontio.model.dto.TxDetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TxDetailMapper extends Mapper<TxDetailDto> {

    // self-defined SQL

    List<TxDetailDto> selectTxsByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    List<TxDetailDto> selectNonontidTxsByPage(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    TxDetailDto selectTxByHash(@Param("txHash") String txHash);

    List<TxDetailDto> selectTransferTxDetailByHash(@Param("txHash") String txHash);

    Integer selectLatestOntTransferTxTime(@Param("address") String address);

    List<TxBasicDto> selectTxsByBlockHeight(@Param("blockHeight") Integer blockHeight);

    List<TxDetailDto> selectTxsByCalledContractHash(@Param("calledContractHash") String contractHash, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    Integer selectCountByCalledContracthash(@Param("calledContractHash") String calledContractHash);

    List<TransferTxDto> selectTransferTxsByPage(@Param("address") String address, @Param("assetName") String assetName, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    List<TransferTxDto> selectTransferTxsByTime(@Param("address") String address, @Param("assetName") String assetName, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<TransferTxDto> selectTransferTxsByTimeAndPage(@Param("address") String address, @Param("assetName") String assetName, @Param("endTime") Long endTime, @Param("pageSize") Integer pageSize);

    List<TransferTxDto> selectDragonTransferTxsByTime(@Param("address") String address, @Param("assetName") String assetName, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<TransferTxDto> selectDragonTransferTxsByTimeAndPage(@Param("address") String address, @Param("assetName") String assetName, @Param("endTime") Long endTime, @Param("pageSize") Integer pageSize);

    Integer selectTransferTxCountByAddr(@Param("address") String address);

}