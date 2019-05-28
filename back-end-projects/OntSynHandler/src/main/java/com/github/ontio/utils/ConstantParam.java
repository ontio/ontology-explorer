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

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.OntSdk;
import com.github.ontio.model.common.BatchBlockDto;

import java.math.BigDecimal;
import java.util.*;

public final class ConstantParam {
    /**
     * 节点restfulurl列表
     */
    public static List<String> NODE_RESTFULURLLIST = new ArrayList<>();

    /**
     * 主节点restfulurl
     */
    public static String MASTERNODE_RESTFULURL = "";

    /**
     * 主节点在列表中的序列号
     */
    public static int MASTERNODE_INDEX = 0;

    /**
     * Ontology SDK object
     */
    public static OntSdk ONT_SDKSERVICE = null;

    /**
     * the number of ontid transactions of batch block
     */
    public static int BATCHBLOCK_ONTID_COUNT = 0;

    /**
     * the number of nonontid transactions of batch block
     */
    public static int BATCHBLOCK_ONTIDTX_COUNT = 0;

    /**
     * the number of nonontid transactions of batch block
     */
    public static int BATCHBLOCK_TX_COUNT = 0;

    /**
     * the contracthash list of batch block
     */
    public static List<String> BATCHBLOCK_CONTRACTHASH_LIST = new ArrayList<>();

    /**
     * Push
     */
    public static final String CLAIMRECORD_OPE_PREFIX = "Push";

    /**
     * registerCandidate
     */
    public static final String REGISTER = "Register";

    /**
     * add action
     */
    public static final String ADD = "add";

    public static final BigDecimal ZERO = new BigDecimal("0");

    public static final BigDecimal ONE = new BigDecimal("1");

    public static final BigDecimal ONG_DECIMAL = new BigDecimal("1000000000");

    //ontid operation description separator
    public static final String ONTID_SEPARATOR = "||";

    //ontid operation description separator
    public static final String ONTID_SEPARATOR2 = "&";

    public static Map<String, JSONObject> OEP4MAP = new HashMap<>();

    public static Set<String> OEP4CONTRACTS = new HashSet<>();

    public static Map<String, JSONObject> OEP5MAP = new HashMap<>();

    public static Set<String> OEP5CONTRACTS = new HashSet<>();

    public static Map<String, JSONObject> OEP8MAP = new HashMap<>();

    public static Set<String> OEP8CONTRACTS = new HashSet<>();



    public static final String ASSET_NAME_ONT = "ont";

    public static final String ASSET_NAME_ONG = "ong";

    public static final String ASSET_NAME_DRAGON = "HyperDragons: ";

    public static final String IS_OEP4TX = "is_oep4tx";

    public static final String IS_OEP5TX = "is_oep5tx";

    public static final String IS_OEP8TX = "is_oep8tx";

    public static BatchBlockDto BATCHBLOCKDTO = new BatchBlockDto();

    public static final String TXPAYLOAD_CODE_FLAG = "67";

}
