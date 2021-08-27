package com.github.ontio.mapper;

import com.github.ontio.model.common.GovernanceInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiuQi
 */
@Repository
public interface CommonMapper {
	
	int removeGovernanceInfos();
	
	int saveGovernanceInfos(List<GovernanceInfo> infos);
	
}
