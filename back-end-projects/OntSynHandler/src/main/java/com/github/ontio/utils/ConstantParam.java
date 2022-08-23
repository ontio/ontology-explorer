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
import com.github.ontio.smartcontract.nativevm.Governance;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

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

    public static Governance ONT_GOVERNANCE = null;

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

    public static final BigDecimal ZERO = BigDecimal.ZERO;

    public static final BigDecimal ONE = BigDecimal.ONE;

    public static final BigDecimal ONG_DECIMAL = new BigDecimal("1000000000");

    public static final BigDecimal NEW_ONT_DECIMAL = ONG_DECIMAL;

    public static final BigDecimal NEW_ONG_DECIMAL = new BigDecimal("1000000000000000000");

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

    public static Map<String, JSONObject> ORC20MAP = new HashMap<>();

    public static Set<String> ORC20CONTRACTS = new HashSet<>();

    public static Map<String, JSONObject> ORC721MAP = new HashMap<>();

    public static Set<String> ORC721CONTRACTS = new HashSet<>();

    public static Map<String, JSONObject> ORC1155MAP = new HashMap<>();

    public static Set<String> ORC1155CONTRACTS = new HashSet<>();


    public static final String ASSET_NAME_ONT = "ont";

    public static final String ASSET_NAME_ONG = "ong";

    public static final String ASSET_NAME_DRAGON = "HyperDragons: ";

    public static final String IS_OEP4TX = "is_oep4tx";

    public static final String IS_OEP5TX = "is_oep5tx";

    public static final String IS_OEP8TX = "is_oep8tx";

    public static final String IS_ORC20TX = "is_orc20tx";

    public static final String IS_ORC721TX = "is_orc721tx";

    public static final String IS_ORC1155TX = "is_orc1155tx";


    public static final String TRANSFER_TX = "ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";

    public static final String Approval_TX = "8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925";

    public static final String TRANSFER_SINGLE_TX = "c3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62";

    public static final String TRANSFER_BATCH_TX = "4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb";

    public static final String ONG_CONTRACT_ADDRESS = "0200000000000000000000000000000000000000";

    public static final String GOVERNANCE_CONTRACT_ADDRESS_REV = "0000000000000000000000000000000000000007";

    public static final String NATIVE_INPUT_DATA_END = "0068164f6e746f6c6f67792e4e61746976652e496e766f6b65";

    public static final String NATIVE_STRUCT_START = "00c66b";

    public static final String NATIVE_ARGS_OP_CODE = "6a7cc8";

    public static final String OP_PACK = "c1";

    public static final List<String> INVOLVE_USER_STAKE_METHOD = Arrays.asList("authorizeForPeer", "unAuthorizeForPeer", "withdraw");

    public static final String TXPAYLOAD_CODE_FLAG = "67";

    public static final String ONG_CONTRACTHASH = "08b6dcfed2aace9190a44ae34a320e42c04b46ac";

    public static final String ONT_CONTRACTHASH = "0239dcf9b4a46f15c5f23f20d52fac916a0bac0d";

    public static final String ONTID_CONTRACTHASH = "6815cbe7b4dbad4d2d09ae035141b5254a002f79";

    public static final String AUTH_CONTRACTHASH = "24a15c6aed092dfaa711c4974caf1e9d307bf4b5";

    public static final String INVOKE_DEPLOY_CONTRACT_ACTION = "7365747570";

    public static final String EVM_ADDRESS_PREFIX = "0x";

    public static final BigDecimal MAX_APPROVAL_AMOUNT = new BigDecimal("99999999999999999999");

    public static final String EMPTY = "";

    public static final String FUN_BALANCE_OF = "balanceOf";

    public static final String FUN_SYMBOL = "symbol";

    public static final String FUN_DECIMALS = "decimals";

    public static final String NEOVM_TRANSFER_ACTION = "7472616e73666572";

    public static final String NEOVM_APPROVAL_ACTION = "617070726f76616c";

    public static final String NATIVE_CALLED_CONTRACT_HASH = "792e4e61746976652e496e766f6b65";
    /**
     * evm TypeReference
     */
    public static TypeReference<Address> TYPE_REFERENCE_ADDRESS = new TypeReference<Address>() {
    };
    public static TypeReference<Uint256> TYPE_REFERENCE_UINT256 = new TypeReference<Uint256>() {
    };
    public static TypeReference<DynamicArray<Uint256>> TYPE_REFERENCE_UINT256_ARRAY = new TypeReference<DynamicArray<Uint256>>() {
    };
    public static TypeReference<Utf8String> TYPE_REFERENCE_UTF8 = new TypeReference<Utf8String>() {
    };
    public static TypeReference<Uint8> TYPE_REFERENCE_UINT8 = new TypeReference<Uint8>() {
    };
}
