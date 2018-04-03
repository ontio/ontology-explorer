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


import com.github.ontio.OntSdk;
import com.github.ontio.core.block.Block;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.dao.BlockMapper;
import com.github.ontio.dao.CurrentMapper;
import com.github.ontio.model.Current;
import com.github.ontio.task.TxnHandlerThread;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/14
 */
@Service
public class BlockManagementService {

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
     * @param sdk
     * @param block
     * @param blockBookKeeper
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleOneBlock(OntSdk sdk, Block block, String blockBookKeeper) throws Exception {

        int blockHeight = block.height;
        int blockTime = block.timestamp;
        int txnNum = block.transactions.length;
        logger.info("{} run-------blockHeight:{},txnSum:{}", CLASS_NAME, blockHeight, txnNum);

        ConstantParam.INIT_AMOUNT = 0;

        //asynchronize handle transaction
        for (int i = 0; i < txnNum; i++) {
            Transaction txn = block.transactions[i];
            Future future = txnHandlerThread.asyncHandleTxn(sdk, txn, blockHeight, blockTime, i);
            future.get();
        }

        while (ConstantParam.INIT_AMOUNT < txnNum) {
            logger.info("wait for multi thread tasks ......");
            try {
                Thread.sleep(50 * 1);
            } catch (InterruptedException e) {
                logger.error("error...{}", e);
            }
        }

        if (blockHeight > 1) {
            blockMapper.updateNextBlockHash(block.hash().toString(), blockHeight - 1);
        }
        insertBlock(block, blockBookKeeper);
        int txnCount = currentMapper.selectTxnCount();
        //get rid of BookKeeperTransaction
        updateCurrent(blockHeight, txnCount + txnNum - 1);

        logger.info("{} end-------height:{},txnSum:{}", CLASS_NAME, blockHeight, txnNum);
    }


    @Transactional(rollbackFor = Exception.class)
    public void insertBlock(Block block, String blockBookKeeper) throws Exception{

        com.github.ontio.model.Block blockDO = new com.github.ontio.model.Block();
        blockDO.setBlocksize(block.toArray().length);
        blockDO.setBlocktime(block.timestamp);
        blockDO.setHash(block.hash().toString());
        blockDO.setHeight(block.height);
        blockDO.setTxnsroot(block.transactionsRoot.toString());
        blockDO.setPrevblock(block.prevBlockHash.toString());
        blockDO.setConsensusdata(Helper.asUnsignedDecimalString(block.consensusData));
        blockDO.setTxnnum(block.transactions.length);
        blockDO.setBookkeeper(blockBookKeeper);
        blockDO.setNextblock("");

        blockMapper.insert(blockDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCurrent(int height, int txnCount) throws Exception{
        Current currentDO = new Current();
        currentDO.setHeight(height);
        currentDO.setTxncount(txnCount);
        currentMapper.update(currentDO);
    }


}
