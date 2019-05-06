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

package com.github.ontio.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.common.Address;
import com.github.ontio.mapper.BlockMapper;
import com.github.ontio.mapper.CurrentMapper;
import com.github.ontio.model.dao.Block;
import com.github.ontio.model.dao.Current;
import com.github.ontio.thread.TxHandlerThread;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/14
 */
@Slf4j
@Service
public class BlockHandleService {

    private final BlockMapper blockMapper;

    private final CurrentMapper currentMapper;

    private final TxHandlerThread txHandlerThread;

    @Autowired
    public BlockHandleService(BlockMapper blockMapper, CurrentMapper currentMapper, TxHandlerThread txHandlerThread) {
        this.blockMapper = blockMapper;
        this.currentMapper = currentMapper;
        this.txHandlerThread = txHandlerThread;
    }

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
        JSONArray txArray = blockJson.getJSONArray("Transactions");
        int txCountInBlock = txArray.size();
        log.info("{} run-------blockHeight:{},txCount:{}", Helper.currentMethod(), blockHeight, txCountInBlock);

        ConstantParam.ONEBLOCK_ONTID_COUNT = 0;
        ConstantParam.ONEBLOCK_ONTIDTX_COUNT = 0;

        List<Future> futureList = new ArrayList<>();
        //asynchronize handle transaction
        for (int i = 0; i < txCountInBlock; i++) {
            JSONObject txJson = (JSONObject) txArray.get(i);
            Future future = txHandlerThread.asyncHandleTx(txJson, blockHeight, blockTime, i + 1);
            futureList.add(future);
            //future.get();
        }
        //等待线程池里的线程都执行结束
        for (int j = 0; j < futureList.size(); j++) {
            futureList.get(j).get();
        }

        insertBlock(blockJson);

        List<Current> currents = currentMapper.selectAll();
        int txCount = currents.get(0).getTxCount();
        int ontIdCount = currents.get(0).getOntidCount();
        int nonOntIdTxCount = currents.get(0).getNonontidTxCount();
        updateCurrent(blockHeight, txCount + txCountInBlock,
                ontIdCount + ConstantParam.ONEBLOCK_ONTID_COUNT, nonOntIdTxCount + txCountInBlock - ConstantParam.ONEBLOCK_ONTIDTX_COUNT);

        log.info("{} end-------height:{},txCount:{}", Helper.currentMethod(), blockHeight, txCountInBlock);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertBlock(JSONObject blockJson) {
        String blockKeeperStr = "";
        StringBuilder sb = new StringBuilder(400);

        JSONObject blockHeader = blockJson.getJSONObject("Header");
        JSONArray blockKeepers = blockHeader.getJSONArray("Bookkeepers");
        blockKeepers.forEach(item -> {
            sb.append(Address.addressFromPubKey((String) item).toBase58());
            sb.append("&");
        });
        blockKeeperStr = sb.toString();
        if (Helper.isNotEmptyOrNull(blockKeeperStr)) {
            blockKeeperStr = blockKeeperStr.substring(0, blockKeeperStr.length() - 1);
        }

        Block block = Block.builder()
                .blockHash(blockJson.getString("Hash"))
                .blockSize(blockJson.getInteger("Size"))
                .blockTime(blockHeader.getInteger("Timestamp"))
                .blockHeight(blockHeader.getInteger("Height"))
                .txsRoot(blockHeader.getString("TransactionsRoot"))
                .consensusData(blockHeader.getString("ConsensusData"))
                .txCount(blockJson.getJSONArray("Transactions").size())
                .bookkeepers(blockKeeperStr)
                .build();
        blockMapper.insert(block);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCurrent(int blockHeight, int txCount, int ontIdTxCount, int nonontIdTxCount) {

        Current current = Current.builder()
                .blockHeight(blockHeight)
                .txCount(txCount)
                .ontidCount(ontIdTxCount)
                .nonontidTxCount(nonontIdTxCount)
                .build();
        currentMapper.update(current);
    }
}
