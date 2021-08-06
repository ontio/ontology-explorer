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


package com.github.ontio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("ParamsConfig")
public class ParamsConfig {


    /**
     * ontology blockchain restful url
     */
    @Value("${masternode.restful.url}")
    public String MASTERNODE_RESTFUL_URL;

    @Value("${testnode.rpc.url}")
    public String TESTNODE_RPC_URL;


    /**
     * the amount of the ontology blockchain nodes in properties
     */
    @Value("${node.count}")
    public int NODE_COUNT;

    /**
     * the interval for waiting block generation
     */
    @Value("${block.interval}")
    public int BLOCK_INTERVAL;

    /**
     * each node fault tolerance maximum time.
     */
    @Value("${node.interruptTime.max}")
    public int NODE_INTERRUPTTIME_MAX;

    /**
     * the maximum time of each node for waiting for generating block
     */
    @Value("${node.waitForBlockTime.max}")
    public int NODE_WAITFORBLOCKTIME_MAX;

    /**
     * ontology blockchain ONT asset smartcontract codehash
     */
    @Value("${ont.contractHash}")
    public String ONT_CONTRACTHASH;

    /**
     * ontology blockchain ONG asset smartcontract codehash
     */
    @Value("${ong.contractHash}")
    public String ONG_CONTRACTHASH;

    /**
     * ontology blockchain ontId smartcontract codehash
     */
    @Value("${ontId.contractHash}")
    public String ONTID_CONTRACTHASH;

    /**
     * ontology blockchain record smartcontract codehash
     */
    @Value("${claimRecord.contractHash}")
    public String CLAIMRECORD_CONTRACTHASH;

    /**
     * ontology blockchainauth smartcontract codehash
     */
    @Value("${auth.contractHash}")
    public String AUTH_CONTRACTHASH;

    @Value("${pax.contractHash}")
    public String PAX_CONTRACTHASH;

    @Value("${threadPoolSize.max}")
    public int THREADPOOLSIZE_MAX;

    @Value("${threadPoolSize.core}")
    public int THREADPOOLSIZE_CORE;

    @Value("${threadPoolSize.queue}")
    public int THREADPOOLSIZE_QUEUE;

    @Value("${threadPoolSize.keepalive}")
    public int THREADPOOLSIZE_KEEPALIVE_SECOND;


    @Value("${batchInsert.blockCount}")
    public int BATCHINSERT_BLOCK_COUNT;

    @Value("${batchInsert.sqlCount}")
    public int BATCHINSERT_SQL_COUNT;
    
    @Value("${reSync.enabled:true}")
    public boolean reSyncEnabled = true;

    @Value("${uniswap.factory.contractHash}")
    public String UNISWAP_FACTORY_CONTRACTHASH;

}