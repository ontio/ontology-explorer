package com.github.ontio.schedule;

import com.github.ontio.service.NodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author LiuQi
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NodeScheduledTask {
	
	private final NodeService nodeService;
	
	@Value("${task.syncGovernanceInfo.filePath}")
	private String governanceInfoFilePath;
	
	@Scheduled(initialDelay = 5000, fixedRateString = "${task.syncGovernanceInfo.interval}")
	public void synchronizeGovernanceInfo() {
		try {
			log.info("synchronize governance info task begin...");
			Path path = FileSystems.getDefault().getPath(governanceInfoFilePath);
			nodeService.synchronizeGovernanceInfo(path);
			log.info("synchronize governance info task finished...");
		} catch (Exception e) {
			log.error("synchronize governance info task failed...",e);
		}
	}
	
}
