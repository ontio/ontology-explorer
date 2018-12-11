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

package com.github.ontio.asyncService;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Address;
import com.github.ontio.dao.BlockMapper;
import com.github.ontio.dao.CurrentMapper;
import com.github.ontio.dao.ContractsMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.model.Contracts;
import com.github.ontio.model.Current;
import com.github.ontio.thread.TxnHandlerThread;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.Helper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Future;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/14
 */
@Service
public class BlockHandleService {

    private static final Logger logger = LoggerFactory.getLogger(BlockHandleService.class);

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private CurrentMapper currentMapper;
    @Autowired
    private ContractsMapper contractsMapper;
    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private TxnHandlerThread txnHandlerThread;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * handle the block and the transactions in this block
     *
     * @param blockJson
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleOneBlock(JSONObject blockJson) throws Exception {

        //设置一个模式为BATCH，自动提交为false的session，最后统一提交，需防止内存溢出
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);

        JSONObject blockHeader = blockJson.getJSONObject("Header");
        int blockHeight = blockHeader.getInteger("Height");
        int blockTime = blockHeader.getInteger("Timestamp");
        JSONArray txnArray = blockJson.getJSONArray("Transactions");
        int txnNum = txnArray.size();
        logger.info("{} run-------blockHeight:{},txnSum:{}", Helper.currentMethod(), blockHeight, txnNum);

        ConstantParam.ONEBLOCK_ONTID_AMOUNT = 0;
        ConstantParam.ONEBLOCK_ONTIDTXN_AMOUNT = 0;

        try {
            //asynchronize handle transaction
            //future.get() 主线程阻塞等待
            for (int i = 0; i < txnNum; i++) {
                JSONObject txnJson = (JSONObject) txnArray.get(i);
                Future future = txnHandlerThread.asyncHandleTxn(session, txnJson, blockHeight, blockTime, i + 1);
                future.get();
            }
            // 手动提交
            session.commit();
            // 清理缓存，防止溢出
            session.clearCache();
            logger.info("###batch insert success!!");

            // 更新合约列表涉及的交易量
            //updateContractTxCount(blockHeight);
        } catch (Exception e) {
            logger.error("error...session.rollback", e);
            session.rollback();
            throw e;
        } finally {
            session.close();
        }

        if (blockHeight >= 1) {
            blockMapper.updateNextBlockHash(blockJson.getString("Hash"), blockHeight - 1);
        }
        insertBlock(blockJson);

        Map<String, Integer> txnMap = currentMapper.selectSummary();
        int txnCount = txnMap.get("TxnCount");
        int ontIdCount = txnMap.get("OntIdCount");
        int nonOntIdTxnCount = txnMap.get("NonOntIdTxnCount");
        updateCurrent(blockHeight, txnCount + txnNum,
                ontIdCount + ConstantParam.ONEBLOCK_ONTID_AMOUNT, nonOntIdTxnCount + txnNum - ConstantParam.ONEBLOCK_ONTIDTXN_AMOUNT);

        logger.info("{} end-------height:{},txnSum:{}", Helper.currentMethod(), blockHeight, txnNum);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertBlock(JSONObject blockJson) throws Exception {

        JSONObject blockHeader = blockJson.getJSONObject("Header");


        com.github.ontio.model.Block blockDO = new com.github.ontio.model.Block();
        blockDO.setHash(blockJson.getString("Hash"));
        blockDO.setBlocksize(blockJson.getInteger("Size"));
        blockDO.setBlocktime(blockHeader.getInteger("Timestamp"));
        blockDO.setHeight(blockHeader.getInteger("Height"));
        blockDO.setTxnsroot(blockHeader.getString("TransactionsRoot"));
        blockDO.setPrevblock(blockHeader.getString("PrevBlockHash"));
        blockDO.setConsensusdata(blockHeader.getString("ConsensusData"));
        blockDO.setTxnnum(blockJson.getJSONArray("Transactions").size());

        String blockKeeperStr = "";
        JSONArray blockKeepers = blockHeader.getJSONArray("Bookkeepers");
        if (blockKeepers.size() > 0) {
            StringBuilder sb = new StringBuilder(400);
            for (Object obj :
                    blockKeepers) {
                sb.append(Address.addressFromPubKey((String) obj).toBase58());
                sb.append("&");
            }
            blockKeeperStr = sb.toString();
            blockKeeperStr = blockKeeperStr.substring(0, blockKeeperStr.length() - 1);
        }

        blockDO.setBookkeeper(blockKeeperStr);
        blockDO.setNextblock("");

        blockMapper.insert(blockDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCurrent(int height, int txnCount, int ontIdTxnCount, int nonontIdTxnCount) throws Exception {

        Current currentDO = new Current();
        currentDO.setHeight(height);
        currentDO.setTxncount(txnCount);
        currentDO.setOntidcount(ontIdTxnCount);
        currentDO.setNonontidtxncount(nonontIdTxnCount);

        currentMapper.update(currentDO);
    }

    private void updateContractTxCount(Integer blockHeight){
        List<Map> contractsList = transactionDetailMapper.selectContractTxCountByHeight(blockHeight);
        if (contractsList.size() > 0){
            List contracts = new ArrayList();

            for (Map map: contractsList) {
                Contracts contract = new Contracts();
                contract.setTxcount(Integer.valueOf(map.get("txcount").toString()));
                contract.setContract((String) map.get("contract"));
                contract.setUpdatetime(Integer.valueOf(String.valueOf(System.currentTimeMillis() / 1000)));

                contracts.add(contract);
            }

        boolean result = contractsMapper.updateContractTxCount(contracts);
        logger.info("{} update contracts count: {}",  Helper.currentMethod(), result);
        }
    }
}
