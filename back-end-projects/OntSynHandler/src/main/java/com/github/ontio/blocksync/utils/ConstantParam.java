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


package com.github.ontio.blocksync.utils;

import com.github.ontio.OntSdk;

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
     * 尝试连接的最大次数
     */
    public static int NODE_RETRYMAXTIME = 0;

    /**
     * Ontology SDK object
     */
    public static OntSdk ONT_SDKSERVICE = null;

    /**
     * the number of transactions of one block
     */
    public static int TXN_INIT_AMOUNT = 0;

    /**
     * the number of ontid transactions of one block
     */
    public static int ONEBLOCK_ONTID_AMOUNT = 0;

    /**
     * the number of nonontid transactions of one block
     */
    public static int ONEBLOCK_ONTIDTXN_AMOUNT = 0;

    /**
     * ontId operation transaction smart contract event description
     */
    public static final String ONTID_OPE_PREFIX = "ontId-";

    /**
     * record transaction smart contract event description
     */
    public static final String CLAIMRECORD_OPE = "claimRecord-";

    /**
     * auth
     */
    public static final String AUTH_OPE_PREFIX = "auth";

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

    public static final BigDecimal ONG_DECIMAL = new BigDecimal("1000000000");

    //ontid operation description separator
    public static final String ONTID_SEPARATOR = "||";

    //ontid operation description separator
    public static final String ONTID_SEPARATOR2 = "&";

    public static Map<String, Object> OEP4MAP = new HashMap<>();

    public static Set<String> OEP4CONTRACTS = new HashSet<>();

    public static Map<String, Object> OEP5MAP = new HashMap<>();

    public static Set<String> OEP5CONTRACTS = new HashSet<>();

    public static Map<String, Object> OEP8MAP = new HashMap<>();

    public static Set<String> OEP8CONTRACTS = new HashSet<>();


    public static final int DAPPSTOREFLAG_YES = 1;

    public static final int DAPPSTOREFLAG_NO = 0;
}
