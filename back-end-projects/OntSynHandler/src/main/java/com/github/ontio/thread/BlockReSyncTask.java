/*
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.github.ontio.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.ContractMapper;
import com.github.ontio.mapper.TxDetailMapper;
import com.github.ontio.model.common.BatchBlockDto;
import com.github.ontio.model.dao.Contract;
import com.github.ontio.service.BlockReSyncService;
import com.github.ontio.service.CommonService;
import com.github.ontio.service.ReSyncService;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.ReSyncConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BlockReSyncTask {

	private final String CLASS_NAME = this.getClass().getSimpleName();

	private final ParamsConfig paramsConfig;
	private final BlockReSyncService blockReSyncService;
	private final ReSyncService reSyncService;
	private final ContractMapper contractMapper;
	private final TxDetailMapper txDetailMapper;
	private final CommonService commonService;

	@Autowired
	public BlockReSyncTask(ParamsConfig paramsConfig, BlockReSyncService blockReSyncService, ReSyncService reSyncService,
			ContractMapper contractMapper, TxDetailMapper txDetailMapper, CommonService commonService) {
		this.paramsConfig = paramsConfig;
		this.blockReSyncService = blockReSyncService;
		this.reSyncService = reSyncService;
		this.contractMapper = contractMapper;
		this.txDetailMapper = txDetailMapper;
		this.commonService = commonService;
	}

	@Scheduled(initialDelay = 1000 * 5, fixedRate = 1000 * 60 * 60)
	public void reSyncContracts() {
		if (paramsConfig.reSyncEnabled) {
			log.info("Staring block re-sync");
			try {
				for (Contract contract : findContractsForReSync()) {
					reSyncContract(contract);
				}
			} catch (Exception e) {
				log.error("Exception occured，Synchronization thread can't work,error ...", e);
			}
		}
	}

	private void reSyncContract(Contract contract) throws Exception {
		TimeUnit.SECONDS.sleep(30); // waiting for normal block syncing to avoid stale state
		String contractHash = contract.getContractHash();
		Integer fromBlock = contract.getReSyncFromBlock();
		if (fromBlock == null || fromBlock == 0) {
			fromBlock = txDetailMapper.findMissingFromBlock(contractHash);
		}
		Integer toBlock = contract.getReSyncToBlock();
		if (toBlock == null || toBlock == 0) {
			toBlock = txDetailMapper.findMissingToBlock(contractHash);
		}
		
		if (fromBlock == null || toBlock == null) {
			log.info("contract {} has been fully synced", contractHash);
			contract.setReSyncFromBlock(0);
			contract.setReSyncToBlock(0);
			contract.setReSyncStatus(2);
			contractMapper.updateByPrimaryKeySelective(contract);
			return;
		}
		contract.setReSyncFromBlock(fromBlock);
		contract.setReSyncToBlock(toBlock);
		contractMapper.updateByPrimaryKeySelective(contract);

		log.info("start re-sync contract {} from {} to {}", contractHash, fromBlock, toBlock);

		for (int beginHeight = fromBlock; beginHeight <= toBlock; beginHeight += paramsConfig.BATCHINSERT_BLOCK_COUNT) {
			batchHandleBlockAndInsertDb(beginHeight, Math.min(beginHeight + paramsConfig.BATCHINSERT_BLOCK_COUNT - 1, toBlock),
					contractHash);
		}
		contract.setReSyncStatus(2);
		contractMapper.updateByPrimaryKeySelective(contract);
	}

	private List<Contract> findContractsForReSync() {
		Example example = new Example(Contract.class);
		example.and()
				.andEqualTo("auditFlag", 1)
				.andEqualTo("reSyncStatus", 1);
		List<Contract> contracts = contractMapper.selectByExample(example);
		return contracts.stream().filter(contract -> {
			String hash = contract.getContractHash();
			return ConstantParam.OEP4CONTRACTS.contains(hash) || ConstantParam.OEP5CONTRACTS.contains(hash) || ConstantParam.OEP8CONTRACTS.contains(hash);
		}).collect(Collectors.toList());
	}

	/**
	 * 批量处理区块和插入DB
	 */
	private void batchHandleBlockAndInsertDb(int beginHeight, int endHeight, String contractHash) throws Exception {
		long beginTime = System.currentTimeMillis();
		//handle blocks and transactions
		for (int tHeight = beginHeight; tHeight <= endHeight; tHeight++) {
			JSONObject blockJson = commonService.getBlockJsonByHeight(tHeight);
			JSONArray txEventLogArray = commonService.getTxEventLogsByHeight(tHeight);
			blockReSyncService.handleOneBlock(blockJson, txEventLogArray, contractHash);
		}
		long endTime1 = System.currentTimeMillis();
		log.info("batch re-sync {} block from {} use time:{},txCount:{}", paramsConfig.BATCHINSERT_BLOCK_COUNT, beginHeight,
				(endTime1 - beginTime), ReSyncConstantParam.BATCHBLOCK_TX_COUNT);
		reSyncService.batchInsertDb(beginHeight, endHeight, ReSyncConstantParam.BATCHBLOCKDTO, contractHash);
		long endTime2 = System.currentTimeMillis();
		log.info("batch re-sync {} block from {} insert to db use time:{}", paramsConfig.BATCHINSERT_BLOCK_COUNT, beginHeight,
				(endTime2 - endTime1));
		initConstantParam();
	}

	/**
	 * 重新初始化全局参数
	 */
	private void initConstantParam() {
		ReSyncConstantParam.BATCHBLOCKDTO = new BatchBlockDto();
		ReSyncConstantParam.BATCHBLOCK_TX_COUNT = 0;
		ReSyncConstantParam.BATCHBLOCK_ONTID_COUNT = 0;
		ReSyncConstantParam.BATCHBLOCK_CONTRACTHASH_LIST = new ArrayList<>();
	}

}
