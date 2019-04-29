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
import com.github.ontio.mapper.BlockMapper;
import com.github.ontio.mapper.CurrentMapper;
import com.github.ontio.model.Current;
import com.github.ontio.thread.TxnHandlerThread;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/14
 */
@Slf4j
@Service
public class BlockHandleService {

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
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleOneBlock(JSONObject blockJson) throws Exception {

        JSONObject blockHeader = blockJson.getJSONObject("Header");
        int blockHeight = blockHeader.getInteger("Height");
        int blockTime = blockHeader.getInteger("Timestamp");
        JSONArray txnArray = blockJson.getJSONArray("Transactions");
        int txnNum = txnArray.size();
        log.info("{} run-------blockHeight:{},txnSum:{}", Helper.currentMethod(), blockHeight, txnNum);

        ConstantParam.ONEBLOCK_ONTID_AMOUNT = 0;
        ConstantParam.ONEBLOCK_ONTIDTXN_AMOUNT = 0;

        List<Future> futureList = new ArrayList<>();
        //asynchronize handle transaction
        for (int i = 0; i < txnNum; i++) {
            JSONObject txnJson = (JSONObject) txnArray.get(i);
            Future future = txnHandlerThread.asyncHandleTxn(txnJson, blockHeight, blockTime, i + 1);
            futureList.add(future);
            //future.get();
        }
        //等待线程池里的线程都执行结束
        for (int j = 0; j < futureList.size(); j++) {
            futureList.get(j).get();
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

        log.info("{} end-------height:{},txnSum:{}", Helper.currentMethod(), blockHeight, txnNum);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertBlock(JSONObject blockJson) {
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
    public void updateCurrent(int height, int txnCount, int ontIdTxnCount, int nonontIdTxnCount) {

        Current currentDO = new Current();
        currentDO.setHeight(height);
        currentDO.setTxncount(txnCount);
        currentDO.setOntidcount(ontIdTxnCount);
        currentDO.setNonontidtxncount(nonontIdTxnCount);

        currentMapper.update(currentDO);
    }
}
