package com.github.ontio.dao;

import com.github.ontio.model.TransactionDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "TransactionDetailTmpMapper")
public interface TransactionDetailTmpMapper {
    int insert(TransactionDetail record);

    int banchInsertSelective(List<TransactionDetail> records);

    int InsertSelective(Map paramMap);

    int InsertSelectiveByStartTime(Map paramMap);

    int selectTxnCountInOneDay();

    BigDecimal selectOntCountInOneDay();

    BigDecimal selectOngCountInOneDay();

    List selectAddressInOneDay();

    int deleteAll();

    BigDecimal selectContractAssetSum(Map<String, Object> paramMap);

    BigDecimal selectContractAssetSumNew(Map<String, Object> paramMap);

    Integer selectTxnAmount(Map<String, Object> paramMap);

    List<String> selectAllAddressByContract(String contractHash);

    List<String> selectToAddressCountByContractNew(String contractHash);

    List<String> selectFromAddressCountByContractNew(String contractHash);

    List<String> selectAllAddressByAddress(String toAddress);
}