package com.github.ontio.dao;

import com.github.ontio.model.TransactionDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "TransactionDetailDailyMapper")
public interface TransactionDetailDailyMapper {
    int insert(TransactionDetail record);

    TransactionDetail selectByPrimaryKey(TransactionDetail key);

    int updateByPrimaryKeySelective(TransactionDetail record);

    int updateByPrimaryKey(TransactionDetail record);

    BigDecimal selectContractAssetSum(Map<String, Object> paramMap);

    Integer selectTxnAmount(Map<String, Object> paramMap);

    List<String> selectAllAddressByContract(String contractHash);

    List<String> selectAllAddressByAddress(String toAddress);

    int selectiveByEndTime(Map paramMap);

    int deleteByEndTime(Map paramMap);





    BigDecimal selectContractAssetSumNew(Map<String, Object> paramMap);

    List<String> selectToAddressCountByContractNew(String contractHash);

    List<String> selectFromAddressCountByContractNew(String contractHash);

}