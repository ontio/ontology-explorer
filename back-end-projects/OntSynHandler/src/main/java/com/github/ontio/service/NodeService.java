package com.github.ontio.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ontio.mapper.CommonMapper;
import com.github.ontio.model.common.GovernanceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author LiuQi
 */
@Service
@RequiredArgsConstructor
public class NodeService {

	private final CommonMapper commonMapper;

	private final ObjectMapper objectMapper;

	@Transactional(rollbackFor = Exception.class)
	public void synchronizeGovernanceInfo(Path governanceInfoPath) throws IOException {
		String content = new String(Files.readAllBytes(governanceInfoPath));
		List<GovernanceInfo> infos = objectMapper.readValue(content, new TypeReference<List<GovernanceInfo>>() {});
		if (infos != null && !infos.isEmpty()){
			commonMapper.removeGovernanceInfos();
			commonMapper.saveGovernanceInfos(infos);
		}
	}


}
