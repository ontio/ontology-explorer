package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc20TxDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface Orc20TxDetailMapper extends Mapper<Orc20TxDetail> {

    List<Orc20TxDetail> selectTxsByCalledContractHash(@Param("calledContractHash") String contractHash, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    Integer selectCountByCalledContracthash(@Param("calledContractHash") String calledContractHash);

}