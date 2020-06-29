package com.github.ontio.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author LiuQi
 */
@Component
public interface TxDetailIndexMapper {

	int buildTxDetailIndexForFromAddress(@Param("beginHeight") int beginHeight, @Param("endHeight") int endHeight, @Param(
			"contractHash") String contractHash);

	int buildTxDetailIndexForToAddress(@Param("beginHeight") int beginHeight, @Param("endHeight") int endHeight, @Param(
			"contractHash") String contractHash);

}
