package com.github.ontio.mapper;

import com.github.ontio.model.dto.Oep4TxDetailDto;
import com.github.ontio.model.dto.TxDetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface Oep4TxDetailMapper extends Mapper<Oep4TxDetailDto> {

    List<TxDetailDto> selectTxsByCalledContractHash(@Param("calledContractHash") String contractHash, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    Integer selectCountByCalledContracthash(@Param("calledContractHash") String calledContractHash);

    BigDecimal selectTransferSumByCondition(String fromAddress, String toAddress, List<String> calledContractHash, String assetName, Integer fromBlock, Integer toBlock);

}