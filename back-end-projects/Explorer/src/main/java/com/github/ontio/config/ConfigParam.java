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

import java.math.BigDecimal;


/**
 * @author zhouq
 * @date 2018/2/27
 */

@Service("ConfigParam")
public class ConfigParam {

    /**
     * ontology blockchain restful url
     */
    @Value("${masternode.restful.url}")
    public String MASTERNODE_RESTFUL_URL;


    @Value("${genesisblock.time}")
    public int GENESISBLOCKTIME;


    @Value("${queryAddr.pageSize}")
    public int QUERYADDRINFO_PAGESIZE;


    @Value("${oep8.pumpkin.codeHash}")
    public String OEP8_PUMPKIN_CODEHASH;

    @Value("${sdk.nodecount}")
    public Integer SDK_NODE_COUNT;

    @Value("${explorer.dailyschedule}")
    public String EXPLORER_DAILY_SCHEDULE;

    @Value("${goserver.domain}")
    public String GOSERVER_DOMAIN;

    /**
     * 主网每秒生成的ong个数
     */
    @Value("${ong.second.generate}")
    public BigDecimal ONG_SECOND_GENERATE;

    //创世区块时间
    @Value("${genesisblock.time}")
    public int GENESISBLOCK_TIME;


    @Value("${ongreward.daily}")
    public BigDecimal ONGREWARD_DAILY;

    @Value("${ontreward.week}")
    public BigDecimal ONTREWARD_WEEK;


    @Value("${blockchain.max.tps}")
    public Integer BLOCKCHAIN_MAX_TPS;


}