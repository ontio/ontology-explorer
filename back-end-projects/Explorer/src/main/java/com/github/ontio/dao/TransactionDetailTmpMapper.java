package com.github.ontio.dao;

import com.github.ontio.model.TransactionDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "TransactionDetailTmpMapper")
public interface TransactionDetailTmpMapper {

    int insert(TransactionDetail record);

    int InsertSelectiveFromDetailTable(Map paramMap);

    int selectTxnCountInOneDay();

    BigDecimal selectOntAmountInOneDay();

    BigDecimal selectOngAmountInOneDay();

    List selectAddressInOneDay();

    int deleteAll();

    BigDecimal selectContractAssetAmount(@Param("contractHash") String contractHash, @Param("assetName") String assetName);

    Integer selectContractTxnCount(@Param("contractHash") String contractHash);

    List<String> selectContractAddr(@Param("contractHash") String contractHash);
}