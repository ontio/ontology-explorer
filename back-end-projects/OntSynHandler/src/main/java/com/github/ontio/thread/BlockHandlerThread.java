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
import com.github.ontio.OntSdk;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.BatchBlockDto;
import com.github.ontio.service.BlockHandleService;
import com.github.ontio.service.CommonService;
import com.github.ontio.utils.ConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component("BlockHandlerThread")
@Scope("prototype")
public class BlockHandlerThread extends Thread {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final ParamsConfig paramsConfig;
    private final BlockHandleService blockManagementService;
    private final CurrentMapper currentMapper;
    private final Environment env;
    private final CommonService commonService;

    @Autowired
    public BlockHandlerThread(ParamsConfig paramsConfig, BlockHandleService blockManagementService, CurrentMapper currentMapper,
                               Environment env, CommonService commonService) {
        this.paramsConfig = paramsConfig;
        this.blockManagementService = blockManagementService;
        this.currentMapper = currentMapper;
        this.env = env;
        this.commonService = commonService;
    }

    /**
     * main thread
     */
    @Override
    public void run() {
        log.info("========{}.run=======", CLASS_NAME);
        try {
            ConstantParam.MASTERNODE_RESTFULURL = paramsConfig.MASTERNODE_RESTFUL_URL;
            //初始化node列表
            initNodeRestfulList();
            //初始化sdk object
            initSdkService();

            int oneBlockTryTime = 1;
            while (true) {

                int remoteBlockHieght = commonService.getRemoteBlockHeight();
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
                        commonService.switchNode();
                        oneBlockTryTime = 1;
                    }
                    continue;
                }
                oneBlockTryTime = 1;

                //初始化全局参数
                initConstantParam();

                int blockCount = remoteBlockHieght - dbBlockHeight;
                if (blockCount >= paramsConfig.BATCHINSERT_BLOCK_COUNT) {
                    for (int j = 0; j <= blockCount / paramsConfig.BATCHINSERT_BLOCK_COUNT; j++) {
                        int beginHeight = dbBlockHeight + 1 + j * paramsConfig.BATCHINSERT_BLOCK_COUNT;
                        int endHeight = dbBlockHeight + (j + 1) * paramsConfig.BATCHINSERT_BLOCK_COUNT > remoteBlockHieght ? remoteBlockHieght : dbBlockHeight + (j + 1) * paramsConfig.BATCHINSERT_BLOCK_COUNT;
                        //刚好差paramsConfig.BATCHINSERT_BLOCK_COUNT整数倍的情况
                        if (beginHeight <= remoteBlockHieght) {
                            batchHandleBlockAndInsertDb(beginHeight, endHeight);
                        }
                    }
                } else {
                    batchHandleBlockAndInsertDb(dbBlockHeight + 1, remoteBlockHieght);
                }
            }

        } catch (Exception e) {
            log.error("Exception occured，Synchronization thread can't work,error ...", e);
        }
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


    /**
     * 批量处理区块和插入DB
     *
     * @param beginHeight
     * @param endHeight
     * @throws Exception
     */
    private void batchHandleBlockAndInsertDb(int beginHeight, int endHeight) throws Exception {
        long beginTime = System.currentTimeMillis();
        //handle blocks and transactions
        for (int tHeight = beginHeight; tHeight <= endHeight; tHeight++) {
            JSONObject blockJson = commonService.getBlockJsonByHeight(tHeight);
            JSONArray txEventLogArray = commonService.getTxEventLogsByHeight(tHeight);
            blockManagementService.handleOneBlock(blockJson, txEventLogArray);
        }
        long endTime1 = System.currentTimeMillis();
        log.info("batch handle {} block from {} use time:{},txCount:{}", paramsConfig.BATCHINSERT_BLOCK_COUNT, beginHeight, (endTime1 - beginTime), ConstantParam.BATCHBLOCK_TX_COUNT);
        commonService.batchInsertDb(endHeight, ConstantParam.BATCHBLOCKDTO);
        long endTime2 = System.currentTimeMillis();
        log.info("batch handle {} block from {} insert to db use time:{}", paramsConfig.BATCHINSERT_BLOCK_COUNT, beginHeight, (endTime2 - endTime1));
        initConstantParam();
    }

    /**
     * 重新初始化全局参数
     */
    private void initConstantParam() {
        ConstantParam.BATCHBLOCKDTO = new BatchBlockDto();
        ConstantParam.BATCHBLOCK_TX_COUNT = 0;
        ConstantParam.BATCHBLOCK_ONTID_COUNT = 0;
        ConstantParam.BATCHBLOCK_ONTIDTX_COUNT = 0;
        ConstantParam.BATCHBLOCK_CONTRACTHASH_LIST = new ArrayList<>();
    }

}
