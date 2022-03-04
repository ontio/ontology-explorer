package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc1155TxDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface Orc1155TxDetailMapper extends Mapper<Orc1155TxDetail> {

    List<Orc1155TxDetail> selectTxsByCalledContractHashAndTokenName(@Param("calledContractHash") String calledContractHash, @Param("tokenName") String tokenName, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    Integer selectCountByCalledContracthashAndTokenName(@Param("calledContractHash") String calledContractHash, @Param("tokenName") String tokenName);

}