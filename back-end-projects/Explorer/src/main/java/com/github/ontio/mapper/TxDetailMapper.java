package com.github.ontio.mapper;

import com.github.ontio.model.dto.TransferTxDto;
import com.github.ontio.model.dto.TxDetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TxDetailMapper extends Mapper<TxDetailDto> {

    // self-defined SQL

    TxDetailDto selectTxByHash(@Param("txHash") String txHash);

    List<TxDetailDto> selectTransferTxDetailByHash(@Param("txHash") String txHash);

    Integer selectLatestOntTransferTxTime(@Param("address") String address);

    List<TransferTxDto> selectTransferTxsByPage(@Param("address") String address, @Param("assetName") String assetName, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    List<TransferTxDto> selectTransferTxsByTime(@Param("address") String address, @Param("assetName") String assetName, @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    List<TransferTxDto> selectTransferTxsByTime4Onto(@Param("address") String address, @Param("assetName") String assetName, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<TransferTxDto> selectTransferTxsByTimeInFromAddr4Onto(@Param("address") String address, @Param("assetName") String assetName, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<TransferTxDto> selectTransferTxsByTimeInToAddr4Onto(@Param("address") String address, @Param("assetName") String assetName, @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    List<TransferTxDto> selectTransferTxsByTimeAndPage4Onto(@Param("address") String address, @Param("assetName") String assetName, @Param("endTime") Long endTime, @Param("pageSize") Integer pageSize);

    List<TransferTxDto> selectTransferTxsByTimeAndPageInFromAddr4Onto(@Param("address") String address, @Param("assetName") String assetName, @Param("endTime") Long endTime, @Param("pageSize") Integer pageSize);

    List<TransferTxDto> selectTransferTxsByTimeAndPageInToAddr4Onto(@Param("address") String address, @Param("assetName") String assetName, @Param("endTime") Long endTime, @Param("pageSize") Integer pageSize);


    List<TransferTxDto> selectDragonTransferTxsByTime4Onto(@Param("address") String address, @Param("assetName") String assetName, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<TransferTxDto> selectDragonTransferTxsByTimeInFromAddr4Onto(@Param("address") String address, @Param("assetName") String assetName, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<TransferTxDto> selectDragonTransferTxsByTimeInToAddr4Onto(@Param("address") String address, @Param("assetName") String assetName, @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    List<TransferTxDto> selectDragonTransferTxsByTimeAndPage4Onto(@Param("address") String address,
            @Param("assetName") String assetName, @Param("endTime") Long endTime, @Param("pageSize") Integer pageSize);

    List<TransferTxDto> selectDragonTransferTxsByTimeAndPageInFromAddr4Onto(@Param("address") String address,
            @Param("assetName") String assetName, @Param("endTime") Long endTime, @Param("pageSize") Integer pageSize);

    List<TransferTxDto> selectDragonTransferTxsByTimeAndPageInToAddr4Onto(@Param("address") String address,
            @Param("assetName") String assetName, @Param("endTime") Long endTime, @Param("pageSize") Integer pageSize);


    Integer selectTransferTxCountByAddr(@Param("address") String address);

    List<TransferTxDto> selectTransferTxsOfTokenType(@Param("address") String address, @Param("token_type") String tokenType,
            @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    List<String> selectCalledContractHashesOfTokenType(@Param("tokenType") String tokenType);

    List<String> selectAssetNamesOfTokenType(@Param("tokenType") String tokenType);

    List<TransferTxDto> selectTransferTxsOfHashes(@Param("address") String address, @Param("hashes") List<String> contractHashes,
            @Param("assetNames") List<String> assetNames, @Param("tokenType") String tokenType,
            @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

}