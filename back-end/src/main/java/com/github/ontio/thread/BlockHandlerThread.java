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
import com.github.ontio.asyncService.BlockHandleService;
import com.github.ontio.dao.CurrentMapper;
import com.github.ontio.dao.OntIdMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.utils.ConfigParam;
import com.github.ontio.utils.ConstantParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/13
 */
@Component("BlockHandlerThread")
@Scope("prototype")
public class BlockHandlerThread extends Thread {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private ConfigParam configParam;
    @Autowired
    private BlockHandleService blockManagementService;
    @Autowired
    private CurrentMapper currentMapper;
    @Autowired
    private OntIdMapper ontIdMapper;
    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private Environment env;


    @Override
    public void run() {
        logger.info("========{}.run=======", CLASS_NAME);
        try {
            ConstantParam.NODE_RETRYMAXTIME = configParam.NODE_AMOUNT * configParam.NODE_INTERRUPTTIME_MAX;
            ConstantParam.MASTERNODE_RESTFULURL = configParam.MASTERNODE_RESTFUL_URL;

            //初始化node列表
            initNodeRestfulList();
            //初始化sdk object
            initSdkService();

            int oneBlockTryTime = 1;

            while (true) {

                int remoteBlockHieght = getRemoteBlockHeight();
                logger.info("######remote blockheight:{}", remoteBlockHieght);

                int dbBlockHeight = currentMapper.selectDBHeight();
                logger.info("######db blockheight:{}", dbBlockHeight);

                //wait for generating block
                if (dbBlockHeight >= remoteBlockHieght) {
                    logger.info("+++++++++wait for block+++++++++");
                    try {
                        Thread.sleep(configParam.BLOCK_INTERVAL);
                    } catch (InterruptedException e) {
                        logger.error("error...", e);
                        e.printStackTrace();
                    }
                    oneBlockTryTime++;
                    if (oneBlockTryTime >= configParam.NODE_WAITFORBLOCKTIME_MAX) {
                        switchNode();
                        oneBlockTryTime = 1;
                    }
                    continue;
                }

                oneBlockTryTime = 1;

                //每次删除当前current表height+1的交易，防止上次程序异常退出因为事务性异常插入的交易导致本次再次插入主键重复
                //即防止current表height高度未更新，但该区块高度的交易已经插入tbl_ont_ontid_detail或tbl_ont_txn_detail
                ontIdMapper.deleteByHeight(dbBlockHeight + 1);
                transactionDetailMapper.deleteByHeight(dbBlockHeight + 1);

                //handle blocks and transactions
                for (int tHeight = dbBlockHeight + 1; tHeight <= remoteBlockHieght; tHeight++) {
                    JSONObject blockJson = getBlockJsonByHeight(tHeight);
                    String blockBookKeeper = "";
                    if (tHeight >= 1) {
                        blockBookKeeper = getBlockBookKeeperByHeight(tHeight - 1);
                    }
                    blockManagementService.handleOneBlock(blockJson, blockBookKeeper);
                }

            }

        } catch (Exception e) {
            logger.error("Exception occured，Synchronization thread can't work,error ...", e);
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
            } catch (Exception ex) {
                logger.error("getBlockHeight error, try again...restful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime < ConstantParam.NODE_RETRYMAXTIME && tryTime % configParam.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else if (tryTime < ConstantParam.NODE_RETRYMAXTIME) {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
                logger.error("get blockheight thread can't work,error {} ", ex);
                throw new Exception(ex);
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

        JSONObject block = new JSONObject();
        int tryTime = 1;
        while (true) {
            try {
                block = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getBlockJson(height);
                break;
            } catch (Exception ex) {
                logger.error("getBlockJsonByHeight error, try again...restful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime < ConstantParam.NODE_RETRYMAXTIME && tryTime % configParam.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else if (tryTime < ConstantParam.NODE_RETRYMAXTIME) {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
                logger.error("getBlockJsonByHeight thread can't work,error {} ", ex);
                throw new Exception(ex);
            }
        }

        return block;
    }


    /**
     * get the bookkeeper of the block by height
     *
     * @return
     * @throws Exception
     */
    private String getBlockBookKeeperByHeight(int height) throws Exception {

        String nextBookKeeper = "";
        int tryTime = 1;
        while (true) {
            try {
                JSONObject blockJson = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getBlockJson(height);
                nextBookKeeper = blockJson.getJSONObject("Header").getString("NextBookkeeper");
                break;
            } catch (Exception ex) {
                logger.error("getBlockBookKeeperByHeight error, try again...restsful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime < ConstantParam.NODE_RETRYMAXTIME && tryTime % configParam.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else if (tryTime < ConstantParam.NODE_RETRYMAXTIME) {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
                logger.error("getBlockBookKeeperByHeight thread can't work,error {} ", ex);
                throw new Exception(ex);
            }
        }

        return nextBookKeeper;
    }


    /**
     * switch to another node and initialize ONT_SDKSERVICE object
     * when the master node have an exception
     */
    private void switchNode() {
        ConstantParam.MASTERNODE_INDEX++;
        if (ConstantParam.MASTERNODE_INDEX >= configParam.NODE_AMOUNT) {
            ConstantParam.MASTERNODE_INDEX = 0;
        }
        ConstantParam.MASTERNODE_RESTFULURL = ConstantParam.NODE_RESTFULURLLIST.get(ConstantParam.MASTERNODE_INDEX);
        logger.warn("####switch node restfulurl to {}####", ConstantParam.MASTERNODE_RESTFULURL);

        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(ConstantParam.MASTERNODE_RESTFULURL);
        ConstantParam.ONT_SDKSERVICE = wm;
    }

    /**
     * initialize node list for synchronization thread
     */
    private void initNodeRestfulList() {
        for (int i = 0; i < configParam.NODE_AMOUNT; i++) {
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