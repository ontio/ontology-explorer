package com.github.ontio.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface TxDetailIndexMapper {

	Integer selectLatestOntTransferTxTime(@Param("address") String address);

}
