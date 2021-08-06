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
     * 20336 新链的 RPC - url
     */

    public static String TESTNODE_RPCURL = "";

    /**
     * 主节点在列表中的序列号
     */
    public static int MASTERNODE_INDEX = 0;

    /**
     * Ontology SDK object
     */
    public static OntSdk ONT_SDKSERVICE = null;

    /**
     * the number of new ontid of batch block
     */
    public static int BATCHBLOCK_ONTID_COUNT = 0;

    /**
     * the number of transactions of batch block
     */
    public static int BATCHBLOCK_TX_COUNT = 0;

    /**
     * the contracthash list of batch block
     */
    public static List<String> BATCHBLOCK_CONTRACTHASH_LIST = new ArrayList<>();

    /**
     * the batchblockdto of batch block
     */
    public static BatchBlockDto BATCHBLOCKDTO = new BatchBlockDto();

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

    // todo Add Map & String

    public static Map<String, JSONObject> ERC20MAP = new HashMap<>();

    public static Set<String> ERC20CONTRACTS = new HashSet<>();

    public static Map<String, JSONObject> ERC721MAP = new HashMap<>();

    public static Set<String> ERC721CONTRACTS = new HashSet<>();


    public static final String ASSET_NAME_ONT = "ont";

    public static final String ASSET_NAME_ONG = "ong";

    public static final String ASSET_NAME_DRAGON = "HyperDragons: ";

    public static final String IS_OEP4TX = "is_oep4tx";

    public static final String IS_OEP5TX = "is_oep5tx";

    public static final String IS_OEP8TX = "is_oep8tx";


    // todo ERC20
    public static final String IS_ERC20TX = "is_erc20tx";
    public static final String IS_ERC721TX = "is_erc721tx";

    public static final String DEPLOY_EVM_CODE = "0x";


    public static final String TRANSFER_TX = "ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    public static final String Approval_TX = "8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925";

    public static final String ONG_CONTRACT_ADDRESS = "0200000000000000000000000000000000000000";


    public static final String TXPAYLOAD_CODE_FLAG = "67";

    public static final String ONG_CONTRACTHASH = "08b6dcfed2aace9190a44ae34a320e42c04b46ac";

    public static final String ONT_CONTRACTHASH = "0239dcf9b4a46f15c5f23f20d52fac916a0bac0d";

    public static final String ONTID_CONTRACTHASH = "6815cbe7b4dbad4d2d09ae035141b5254a002f79";

    public static final String AUTH_CONTRACTHASH = "24a15c6aed092dfaa711c4974caf1e9d307bf4b5";

    public static final String INVOKE_DEPLOY_CONTRACT_ACTION = "7365747570";





}
