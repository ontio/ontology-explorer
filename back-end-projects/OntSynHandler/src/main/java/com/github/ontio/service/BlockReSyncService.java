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
import com.github.ontio.thread.TxReSyncThread;
import com.github.ontio.utils.Helper;
import com.github.ontio.utils.ReSyncConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class BlockReSyncService {

    private final TxReSyncThread txReSyncThread;

    @Autowired
    public BlockReSyncService(TxReSyncThread txReSyncThread) {
        this.txReSyncThread = txReSyncThread;
    }

    /**
     * handle the block and the transactions in this block
     *
     * @param blockJson
     * @throws Exception
     */
    public void handleOneBlock(JSONObject blockJson, JSONArray txEventLogArray, String contractHash) throws Exception {

        JSONObject blockHeader = blockJson.getJSONObject("Header");
        int blockHeight = blockHeader.getInteger("Height");
        int blockTime = blockHeader.getInteger("Timestamp");
        JSONArray txArray = blockJson.getJSONArray("Transactions");
        int txCountInOneBlock = txArray.size();
        log.info("{} run-------blockHeight:{},txCount:{}", Helper.currentMethod(), blockHeight, txCountInOneBlock);

        List<Future> futureList = new ArrayList<>();
        //asynchronize handle transaction
        for (int i = 0; i < txCountInOneBlock; i++) {
            JSONObject txJson = (JSONObject) txArray.get(i);
            txJson.put("EventLog", txEventLogArray.get(i));
            Future future = txReSyncThread.asyncHandleTx(txJson, blockHeight, blockTime, i + 1, contractHash);
            futureList.add(future);
            //future.get();
        }
        //等待线程池里的线程都执行结束
        for (int j = 0; j < futureList.size(); j++) {
            futureList.get(j).get();
        }
        ReSyncConstantParam.BATCHBLOCK_TX_COUNT += txCountInOneBlock;

        log.info("{} end-------height:{},txCount:{}", Helper.currentMethod(), blockHeight, txCountInOneBlock);
    }

}
