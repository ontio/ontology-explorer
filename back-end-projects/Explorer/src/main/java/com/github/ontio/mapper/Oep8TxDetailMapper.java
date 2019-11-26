package com.github.ontio.mapper;

import com.github.ontio.model.dto.Oep8TxDetailDto;
import com.github.ontio.model.dto.TxDetailDto;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface Oep8TxDetailMapper extends Mapper<Oep8TxDetailDto> {

    List<TxDetailDto> selectTxsByCalledContractHashAndTokenName(@Param("calledContractHash") String calledContractHash, @Param("tokenName") String tokenName, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    Integer selectCountByCalledContracthashAndTokenName(@Param("calledContractHash") String calledContractHash, @Param("tokenName") String tokenName);

}