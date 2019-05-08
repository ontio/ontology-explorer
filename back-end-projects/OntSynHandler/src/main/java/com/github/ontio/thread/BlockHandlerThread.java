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
import com.github.ontio.service.BlockHandleService;
import com.github.ontio.service.CommonService;
import com.github.ontio.utils.ConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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
    private final CommonService commonService;

    @Autowired
    public BlockHandlerThread(TxDetailMapper txDetailMapper, ParamsConfig paramsConfig, BlockHandleService blockManagementService, CurrentMapper currentMapper, OntidTxDetailMapper ontidTxDetailMapper, Oep4TxDetailMapper oep4TxDetailMapper, Environment env, Oep5TxDetailMapper oep5TxDetailMapper, Oep8TxDetailMapper oep8TxDetailMapper, TxDetailDailyMapper txDetailDailyMapper, TxEventLogMapper txEventLogMapper, CommonService commonService) {
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
        this.commonService = commonService;
    }

    /**
     * BlockHandlerThread
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
                    JSONObject blockJson = commonService.getBlockJsonByHeight(tHeight);
                    JSONArray txEventLogArray = commonService.getTxEventLogsByHeight(tHeight);
                    long time1 = System.currentTimeMillis();
                    blockManagementService.handleOneBlock(blockJson, txEventLogArray);
                    long time2 = System.currentTimeMillis();
                    log.info("handle block {} used time:{}", tHeight, (time2 - time1));
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
}
