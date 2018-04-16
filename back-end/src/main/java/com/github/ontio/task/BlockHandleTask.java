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



package com.github.ontio.task;

import com.github.ontio.OntSdk;
import com.github.ontio.core.block.Block;
import com.github.ontio.asyncService.BlockHandleService;
import com.github.ontio.dao.CurrentMapper;
import com.github.ontio.utils.ConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/13
 */
@Component("BlockHandleTask")
@Scope("prototype")
public class BlockHandleTask extends Thread {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private static List<String> nodeRestfulUrlList = new ArrayList<>();

    private static String masterNodeRestfulUrl = "";

    private static int masterNodeIndex = 0;

    private static int retryMaxTime = 0;

    private static OntSdk sdkService = null;

    @Autowired
    private CurrentMapper currentMapper;
    @Autowired
    private ConfigParam configParam;
    @Autowired
    private BlockHandleService blockManagementService;
    @Autowired
    private Environment env;


    @Override
    public void run() {
        logger.info("========{}.run=======", CLASS_NAME);
        try {
            retryMaxTime = configParam.NODE_AMOUNT * configParam.INTERRUPTTIME_MAX;
            masterNodeRestfulUrl = configParam.NODE_RESTFUL_URL;

            initNodeRestfulList();
            sdkService = initSdkService();

            while (true) {
                int remoteBlockHieght = getRemoteBlockHeight();
                logger.info("######remote blockheight:{}", remoteBlockHieght);

                int dbBlockHeight = currentMapper.selectDBHeight();
                logger.info("######db blockheight:{}", dbBlockHeight);

                //wait for generating block
                if (dbBlockHeight >= remoteBlockHieght) {
                    logger.info("+++++++++wait for block+++++++++");
                    try {
                        Thread.sleep(configParam.INTERVAL);
                    } catch (InterruptedException e) {
                        logger.error("error...", e);
                        e.printStackTrace();
                    }
                    continue;
                }

                //handle blocks and transactions
                for (int tHeight = dbBlockHeight + 1; tHeight <= remoteBlockHieght; tHeight++) {
                    Block block = getBlockByHeight(tHeight);
                    String blockBookKeeper = "firstBookKeeper";
                    if (tHeight > 1) {
                        blockBookKeeper = getBlockBookKeeperByHeight(tHeight - 1);
                    }
                    blockManagementService.handleOneBlock(sdkService, block, blockBookKeeper);
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
                remoteHeight = sdkService.getConnectMgr().getBlockHeight();
                break;
            } catch (Exception ex) {
                logger.error("getBlockHeight error, try again...restful:{},error:", masterNodeRestfulUrl, ex);
                if (tryTime < retryMaxTime && tryTime % configParam.INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else if (tryTime < retryMaxTime) {
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
    private Block getBlockByHeight(int height) throws Exception {

        Block block = new Block();
        int tryTime = 1;
        while (true) {
            try {
                block = sdkService.getConnectMgr().getBlock(height);
                break;
            } catch (Exception ex) {
                logger.error("getBlockByHeight error, try again...restful:{},error:", masterNodeRestfulUrl, ex);
                if (tryTime < retryMaxTime && tryTime % configParam.INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else if (tryTime < retryMaxTime) {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
                logger.error("getBlockByHeight thread can't work,error {} ", ex);
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
                nextBookKeeper = sdkService.getConnectMgr().getBlock(height).nextBookkeeper.toBase58();
                break;
            } catch (Exception ex) {
                logger.error("getBlockBookKeeperByHeight error, try again...restsful:{},error:", masterNodeRestfulUrl, ex);
                if (tryTime < retryMaxTime && tryTime % configParam.INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else if (tryTime < retryMaxTime) {
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
     * switch to another node and initialize sdkService object
     * when the master node have an exception
     *
     */
    private void switchNode() {
        masterNodeIndex++;
        if (masterNodeIndex >= configParam.NODE_AMOUNT) {
            masterNodeIndex = 0;
        }
        masterNodeRestfulUrl = nodeRestfulUrlList.get(masterNodeIndex);
        logger.warn("####switch node restfulurl to {}####", masterNodeRestfulUrl);
        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(masterNodeRestfulUrl);
        sdkService = wm;
    }

    /**
     * initialize node list for synchronization thread
     *
     */
    private void initNodeRestfulList() {
        for (int i = 0; i < configParam.NODE_AMOUNT; i++) {
            nodeRestfulUrlList.add(env.getProperty("node.restful.url_" + i));
        }
    }

    /**
     * initialize ontology sdkService object for synchronizing data
     *
     * @return
     */
    private OntSdk initSdkService() {
        OntSdk sdkService = OntSdk.getInstance();
        sdkService.setRestful(masterNodeRestfulUrl);
        return sdkService;
    }


}
