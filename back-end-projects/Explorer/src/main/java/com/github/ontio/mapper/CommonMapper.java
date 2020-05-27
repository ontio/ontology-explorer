package com.github.ontio.mapper;

import com.github.ontio.model.dto.GovernanceInfoDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiuQi
 */
@Repository
public interface CommonMapper {

	List<GovernanceInfoDto> findGovernanceInfo(@Param("pubKey") String pubKey, @Param("start") int start, @Param("size") int size);

	int countGovernanceInfo(@Param("pubKey") String pubKey);

}
