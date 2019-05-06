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

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.OntSdk;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.*;
import com.github.ontio.model.dao.Oep4;
import com.github.ontio.model.dao.Oep5;
import com.github.ontio.model.dao.Oep8;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.service.BlockHandleService;
import com.github.ontio.utils.ConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component("BlockHandlerThread")
@Scope("prototype")
public class BlockHandlerThread extends Thread {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final ParamsConfig paramsConfig;
    private final BlockHandleService blockManagementService;
    private final CurrentMapper currentMapper;
    private final OntidTxDetailMapper ontidTxDetailMapper;
    private final TxDetailMapper txDetailMapper;
    private final Oep4TxDetailMapper oep4TxDetailMapper;
    private final Oep5TxDetailMapper oep5TxDetailMapper;
    private final Oep8TxDetailMapper oep8TxDetailMapper;
    private final TxDetailDailyMapper txDetailDailyMapper;
    private final TxEventLogMapper txEventLogMapper;
    private final Environment env;

    @Autowired
    public BlockHandlerThread(TxDetailMapper txDetailMapper, ParamsConfig paramsConfig, BlockHandleService blockManagementService, CurrentMapper currentMapper, OntidTxDetailMapper ontidTxDetailMapper, Oep4TxDetailMapper oep4TxDetailMapper, Environment env, Oep5TxDetailMapper oep5TxDetailMapper, Oep8TxDetailMapper oep8TxDetailMapper, TxDetailDailyMapper txDetailDailyMapper, TxEventLogMapper txEventLogMapper) {
        this.txDetailMapper = txDetailMapper;
        this.paramsConfig = paramsConfig;
        this.blockManagementService = blockManagementService;
        this.currentMapper = currentMapper;
        this.ontidTxDetailMapper = ontidTxDetailMapper;
        this.oep4TxDetailMapper = oep4TxDetailMapper;
        this.env = env;
        this.oep5TxDetailMapper = oep5TxDetailMapper;
        this.oep8TxDetailMapper = oep8TxDetailMapper;
        this.txDetailDailyMapper = txDetailDailyMapper;
        this.txEventLogMapper = txEventLogMapper;
    }

    /**
     * BlockHandlerThread
     */
    @Override
    public void run() {
        log.info("========{}.{}run=======", CLASS_NAME, Thread.currentThread().getName());
        try {
            ConstantParam.MASTERNODE_RESTFULURL = paramsConfig.MASTERNODE_RESTFUL_URL;
            //初始化node列表
            initNodeRestfulList();
            //初始化sdk object
            initSdkService();

            int oneBlockTryTime = 1;
            while (true) {

                int remoteBlockHieght = getRemoteBlockHeight();
                log.info("######remote blockheight:{}", remoteBlockHieght);

                int dbBlockHeight = currentMapper.selectBlockHeight();
                log.info("######db blockheight:{}", dbBlockHeight);

                //wait for generating block
                if (dbBlockHeight >= remoteBlockHieght) {
                    log.info("+++++++++wait for block+++++++++");
                    try {
                        Thread.sleep(paramsConfig.BLOCK_INTERVAL);
                    } catch (InterruptedException e) {
                        log.error("error...", e);
                        e.printStackTrace();
                    }
                    oneBlockTryTime++;
                    if (oneBlockTryTime >= paramsConfig.NODE_WAITFORBLOCKTIME_MAX) {
                        switchNode();
                        oneBlockTryTime = 1;
                    }
                    continue;
                }
                oneBlockTryTime = 1;

                //每次删除当前current表height+1的交易，防止上次程序异常退出时，因为多线程事务插入了height+1的交易
                //而current表height未更新，则本次同步再次插入会报主键重复异常
                ontidTxDetailMapper.deleteByHeight(dbBlockHeight + 1);
                txDetailMapper.deleteByHeight(dbBlockHeight + 1);
                txDetailDailyMapper.deleteByHeight(dbBlockHeight + 1);
                oep4TxDetailMapper.deleteByHeight(dbBlockHeight + 1);
                oep5TxDetailMapper.deleteByHeight(dbBlockHeight + 1);
                oep8TxDetailMapper.deleteByHeight(dbBlockHeight + 1);
                txEventLogMapper.deleteByHeight(dbBlockHeight + 1);

                //handle blocks and transactions
                for (int tHeight = dbBlockHeight + 1; tHeight <= remoteBlockHieght; tHeight++) {
                    JSONObject blockJson = getBlockJsonByHeight(tHeight);
                    long time1 = System.currentTimeMillis();
                    blockManagementService.handleOneBlock(blockJson);
                    long time2 = System.currentTimeMillis();
                    log.info("handle block {} used time:{}", tHeight, (time2 - time1));
                }
            }

        } catch (Exception e) {
            log.error("Exception occured，Synchronization thread can't work,error ...", e);
        }
    }


    /**
     * get the the blockheight of the ontology blockchain
     *
     * @return
     * @throws Exception
     */
    private int getRemoteBlockHeight() throws Exception {
        int remoteHeight = 0;
        int tryTime = 1;
        while (true) {
            try {
                remoteHeight = ConstantParam.ONT_SDKSERVICE.getConnect().getBlockHeight();
                break;
            } catch (ConnectorException ex) {
                log.error("getBlockHeight error, try again...restful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % paramsConfig.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
            } catch (IOException e) {
                log.error("get blockheight thread can't work,error {} ", e);
                throw new Exception(e);
            }
        }

        return remoteHeight;
    }

    /**
     * get the block by height
     *
     * @return
     * @throws Exception
     */
    private JSONObject getBlockJsonByHeight(int height) throws Exception {
        JSONObject blockObj = new JSONObject();
        int tryTime = 1;
        while (true) {
            try {
                blockObj = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getBlockJson(height);
                break;
            } catch (ConnectorException ex) {
                log.error("getBlockJsonByHeight error, try again...restful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % paramsConfig.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
            } catch (IOException ex) {
                log.error("getBlockJsonByHeight thread can't work,error {} ", ex);
                throw new Exception(ex);
            }
        }

        return blockObj;
    }

    /**
     * switch to another node and initialize ONT_SDKSERVICE object
     * when the master node have an exception
     */
    private void switchNode() {
        ConstantParam.MASTERNODE_INDEX++;
        if (ConstantParam.MASTERNODE_INDEX >= paramsConfig.NODE_COUNT) {
            ConstantParam.MASTERNODE_INDEX = 0;
        }
        ConstantParam.MASTERNODE_RESTFULURL = ConstantParam.NODE_RESTFULURLLIST.get(ConstantParam.MASTERNODE_INDEX);
        log.warn("####switch node restfulurl to {}####", ConstantParam.MASTERNODE_RESTFULURL);

        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(ConstantParam.MASTERNODE_RESTFULURL);
        ConstantParam.ONT_SDKSERVICE = wm;
    }

    /**
     * initialize node list for synchronization thread
     */
    private void initNodeRestfulList() {
        for (int i = 0; i < paramsConfig.NODE_COUNT; i++) {
            ConstantParam.NODE_RESTFULURLLIST.add(env.getProperty("node.restful.url_" + i));
        }
    }

    /**
     * initialize ontology ONT_SDKSERVICE object for synchronizing data
     *
     * @return
     */
    private void initSdkService() {
        OntSdk sdkService = OntSdk.getInstance();
        sdkService.setRestful(ConstantParam.MASTERNODE_RESTFULURL);
        ConstantParam.ONT_SDKSERVICE = sdkService;
    }
}
