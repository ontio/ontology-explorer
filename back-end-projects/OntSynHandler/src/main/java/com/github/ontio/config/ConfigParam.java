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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service("ConfigParam")
@ConfigurationProperties(prefix = "config")
public class ConfigParam {


    /**
     * ontology blockchain restful url
     */
    @Value("${config.main.node}")
    public String MASTERNODE_RESTFUL_URL;

    /**
     * the amount of the ontology blockchain nodes in properties
     */
    @Value("${config.main.node_count}")
    public int NODE_AMOUNT;

    /**
     * the interval for waiting block generation
     */
    @Value("${config.block.interval}")
    public int BLOCK_INTERVAL;

    /**
     * each node fault tolerance maximum time.
     */
    @Value("${config.node.interruptTime}")
    public int NODE_INTERRUPTTIME_MAX;

    /**
     * the maximum time of each node for waiting for generating block
     */
    @Value("${config.node.waitForBlockTime}")
    public int NODE_WAITFORBLOCKTIME_MAX;

    /**
     * ontology blockchain ontId smartcontract codehash
     */
    @Value("${config.contract.ontId}")
    public String ONTID_CODEHASH;


    /**
     * ontology blockchain record smartcontract codehash
     */
    @Value("${config.contract.claimRecord}")
    public String CLAIMRECORD_CODEHASH;

    /**
     * ontology blockchain ONT asset smartcontract codehash
     */
    @Value("${config.contract.ont}")
    public String ASSET_ONT_CODEHASH;

    /**
     * ontology blockchain ONG asset smartcontract codehash
     */
    @Value("${config.contract.ong}")
    public String ASSET_ONG_CODEHASH;



    @Value("${config.contract.pumpkin}")
    public String OEP8_PUMPKIN_CODEHASH;

    @Value("${config.contract.dragon}")
    public String DRAGON_CODEHASH;


    /**
     * ontology blockchainauth smartcontract codehash
     */
    @Value("${config.contract.auth}")
    public String AUTH_CODEHASH;

    @Value("${config.threadPool.max}")
    public int THREADPOOLSIZE_MAX;

    @Value("${config.threadPool.core}")
    public int THREADPOOLSIZE_CORE;

    @Value("${config.threadPool.queue}")
    public int THREADPOOLSIZE_QUEUE;

    @Value("${config.threadPool.keepalive}")
    public int THREADPOOLSIZE_KEEPALIVE_SECOND;

}