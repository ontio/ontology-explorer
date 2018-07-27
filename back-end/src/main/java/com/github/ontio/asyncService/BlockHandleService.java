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
import com.github.ontio.dao.BlockMapper;
import com.github.ontio.dao.CurrentMapper;
import com.github.ontio.model.Current;
import com.github.ontio.thread.TxnHandlerThread;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/14
 */
@Service
public class BlockHandleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private CurrentMapper currentMapper;
    @Autowired
    private TxnHandlerThread txnHandlerThread;


    /**
     * handle the block and the transactions in this block
     *
     * @param blockJson
     * @param blockBookKeeper
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleOneBlock(JSONObject blockJson, String blockBookKeeper) throws Exception {

        int blockHeight = blockJson.getJSONObject("Header").getInteger("Height");
        int blockTime = blockJson.getJSONObject("Header").getInteger("Timestamp");
        JSONArray txnArray = blockJson.getJSONArray("Transactions");
        int txnNum = txnArray.size();
        logger.info("{} run-------blockHeight:{},txnSum:{}", Helper.currentMethod(), blockHeight, txnNum);

        ConstantParam.TXN_INIT_AMOUNT = 0;
        ConstantParam.ONTIDTXN_INIT_AMOUNT = 0;

        //asynchronize handle transaction
        for (int i = 0; i < txnNum; i++) {
            JSONObject txnJson = (JSONObject) txnArray.get(i);
            Future future = txnHandlerThread.asyncHandleTxn(txnJson, blockHeight, blockTime, i + 1);
            future.get();
        }

        while (ConstantParam.TXN_INIT_AMOUNT < txnNum) {
            logger.info("wait for multi thread tasks ......");
            try {
                Thread.sleep(50 * 1);
            } catch (InterruptedException e) {
                logger.error("error...{}", e);
            }
        }

        if (blockHeight >= 1) {
            blockMapper.updateNextBlockHash(blockJson.getString("Hash"), blockHeight - 1);
        }
        insertBlock(blockJson, blockBookKeeper);

        Map<String, Integer> txnMap = currentMapper.selectTxnCount();
        int txnCount = txnMap.get("TxnCount");
        int ontIdTxnCount = txnMap.get("OntIdCount");
        updateCurrent(blockHeight, txnCount + txnNum, ontIdTxnCount + ConstantParam.ONTIDTXN_INIT_AMOUNT);

        logger.info("{} end-------height:{},txnSum:{}", Helper.currentMethod(), blockHeight, txnNum);
    }


    @Transactional(rollbackFor = Exception.class)
    public void insertBlock(JSONObject blockJson, String blockBookKeeper) throws Exception {

        com.github.ontio.model.Block blockDO = new com.github.ontio.model.Block();
        blockDO.setBlocksize(blockJson.getInteger("Size"));
        blockDO.setBlocktime(blockJson.getJSONObject("Header").getInteger("Timestamp"));
        blockDO.setHash(blockJson.getString("Hash"));
        blockDO.setHeight(blockJson.getJSONObject("Header").getInteger("Height"));
        blockDO.setTxnsroot(blockJson.getJSONObject("Header").getString("TransactionsRoot"));
        blockDO.setPrevblock(blockJson.getJSONObject("Header").getString("PrevBlockHash"));
        blockDO.setConsensusdata(blockJson.getJSONObject("Header").getString("ConsensusData"));
        blockDO.setTxnnum(blockJson.getJSONArray("Transactions").size());
        blockDO.setBookkeeper(blockBookKeeper);
        blockDO.setNextblock("");

        blockMapper.insert(blockDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCurrent(int height, int txnCount, int ontIdTxnCount) throws Exception {

        Current currentDO = new Current();
        currentDO.setHeight(height);
        currentDO.setTxncount(txnCount);
        currentDO.setOntidcount(ontIdTxnCount);

        currentMapper.update(currentDO);
    }


}
