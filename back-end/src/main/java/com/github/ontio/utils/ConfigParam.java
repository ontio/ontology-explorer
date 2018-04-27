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


package com.github.ontio.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author zhouq
 * @date 2018/2/27
 */

@Service("ConfigParam")
public class ConfigParam {


    /**
     * ontology blockchain restful url
     */
    @Value("${node.restful.url}")
    public String NODE_RESTFUL_URL;

    /**
     * ontology blockchain ontId smartcontract codehash
     */
    @Value("${ontId.codeHash}")
    public String ONTID_CODEHASH;

    /**
     * ontology blockchain ontId smartcontract codehash
     */
    @Value("${ontId.codeHash2}")
    public String ONTID_CODEHASH2;

    /**
     * ontology blockchain record smartcontract codehash
     */
    @Value("${record.codeHash}")
    public String RECORD_CODEHASH;

    /**
     * ontology blockchain ONT asset smartcontract codehash
     */
    @Value("${asset.ont.codeHash}")
    public String ASSET_ONT_CODEHASH;

    /**
     * ontology blockchain ONG asset smartcontract codehash
     */
    @Value("${asset.ong.codeHash}")
    public String ASSET_ONG_CODEHASH;

    /**
     * the amount of the ontology blockchain nodes in properties
     */
    @Value("${node.amount}")
    public int NODE_AMOUNT;

    /**
     * the interval for waiting block generation
     */
    @Value("${interval}")
    public int INTERVAL;

    /**
     * node fault tolerance maximum time.
     */
    @Value("${interruptTime.max}")
    public int INTERRUPTTIME_MAX;


}