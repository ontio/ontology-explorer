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


package com.github.ontio.util;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public class ConstantParam {


    /**
     * claim
     */
    public static final String CLAIM = "claim";

    public static final String HYPERDRAGONS = "dragon";

    public static final String PUMPKIN_PREFIX = "pumpkin";


    /**
     * ONT asset
     */
    public static final String ONT = "ont";

    public static final String ONG = "ong";

    public static final String UNBOUND_ONG = "unboundong";

    public static final String WAITBOUND_ONG = "waitboundong";

    public static final String OEP8_PUMPKIN_PREFIX = "pumpkin";


    public static final BigDecimal ZERO = BigDecimal.ZERO;


    public static final BigDecimal ONG_SECONDMAKE = new BigDecimal("5");


    public static final BigDecimal ONT_TOTAL = new BigDecimal("1000000000");

    public static final BigDecimal ONG_TOTAL = ONT_TOTAL;

    public static final BigDecimal NEW_ONT_DECIMAL = ONT_TOTAL;

    public static final BigDecimal NEW_ONG_DECIMAL = new BigDecimal("1000000000000000000");


    public static final String GO_TOTALSUPPLY_URL = "/getAssetHolder";


    public static final List<String> SPECIALADDRLIST = Arrays.asList(
            //团队锁仓地址
            "AKac3Bd6usdivrnNN8tyRcDZN94vpaoAu2",
            //共建合作伙伴锁仓地址
            "AS7MjVEicEsJ4zjEfm2LoKoYoFsmapD7rT",
            //技术社区奖励锁仓地址
            "AcdUMgeF16ScW9ts3kiD3pZkjYRMdYwtVQ",
            //生态合作地址
            "AMX6ZebrPDFELCYRMpSMbZWrhWkKbKg4y8",
            //oge 地址
            "ARHGtgY9Z8HdChFEjdPKKhpT4WDKfVntfC",
            //社区地址
            "ATBdqiUBKnNoJE4L53UkZZjWyFjd1AdamL",
            //NEO counsel
            "AR36E5jLdWDKW3Yg51qDFWPGKSLvfPhbqS",
            //NGC
            "Af48R4EUNYm6kg9kS7rn5xj4fneuFpbkXi");


    //资产类型
    public static final String ASSET_TYPE_NATIVE = "native";

    public static final String ASSET_TYPE_OEP4 = "oep4";

    public static final String ASSET_TYPE_OEP5 = "oep5";

    public static final String ASSET_TYPE_OEP8 = "oep8";

    public static final String ASSET_TYPE_ORC20 = "orc20";

    public static final String ASSET_TYPE_ORC721 = "orc721";

    public static final String ASSET_TYPE_ORC1155 = "orc1155";

    public static final String ASSET_TYPE_ONG = "ong";

    public static final String ASSET_TYPE_ALL = "all";

    public static final String ASSET_TYPE_NATIVE_OEP4 = "native-oep4";

    //合约审核标识
    public static final Boolean AUDIT_PASSED = true;

    public static final Boolean AUDIT_UNPASSED = false;

    //合约类型
    public static final String CONTRACT_TYPE_OEP4 = "oep4";

    public static final String CONTRACT_TYPE_OEP5 = "oep5";

    public static final String CONTRACT_TYPE_OEP8 = "oep8";

    public static final String CONTRACT_TYPE_ORC20 = "orc20";

    public static final String CONTRACT_TYPE_ORC721 = "orc721";

    public static final String CONTRACT_TYPE_ORC1155 = "orc1155";

    public static final String CONTRACT_TYPE_OTHER = "other";


    /**
     * 对外访问实时性要求不高的接口中对应的mapper，属于历史数据和统计数据，基本不会修改。redis中可以设置过期时间较长，
     */
    public static final List<String> REDIS_LONGEXPIRETIME_KEYLIST = new ArrayList<String>(
            Arrays.asList(
                    "BlockMapper.selectOneByHeight",
                    "BlockMapper.selectOneByHash",
                    "TxDetailMapper.selectTransferTxDetailByHash",
                    "TxEventLogMapper.selectTxsByBlockHeight",
                    "ContractMapper.selectContractDetail",
                    "ContractDailySummaryMapper.selectDailySummaryByContractHash",
                    "DailySummaryMapper.selectSummaryByTime",
                    "DailySummaryMapper.selectAddrAndOntIdTotal",
                    "OntidTxDetailMapper.selectOneByTxHash"
            )
    );


    /**
     * 对外访问实时性要求不高的接口中对应的mapper，数据变动频率小，redis中可以设置过期时间较长
     */
    public static final List<String> REDIS_MEDIUMEXPIRETIME_KEYLIST = new ArrayList<String>(
            Arrays.asList(
                    "ContractMapper.selectApprovedContract",
                    "ContractMapper.selectApprovedContractCount",
                    "Oep4Mapper.selectOep4Tokens",
                    "Oep5Mapper.selectOep5Tokens",
                    "Oep8Mapper.selectOep8Tokens",
                    "Oep4Mapper.selectOep4TokenDetail",
                    "Oep5Mapper.selectOep5TokenDetail",
                    "Oep8Mapper.selectOep8TokenDetail",
                    "Oep8Mapper.selectAuditPassedOep8",
                    "TxEventLogMapper.queryTxCount"
            )
    );

    public static final String ADDR_DAILY_SUMMARY_ONTONG_CONTRACTHASH = "0000000000000000000000000000000000000000";

    public static final Integer REQTIME_MAX_RANGE = 40 * 24 * 60 * 60;

    public static final Integer REQTIME_MAX_RANGE_WEEK = 7 * 24 * 60 * 60;


    public static final String BALANCESERVICE_QUERYBALANCE_URL = "/api/v1/multibalance";

    public static final String CHANNEL_ONTO = "onto";

    public static final String VM_CATEGORY_NEOVM = "neovm";

    public static final String VM_CATEGORY_WASMVM = "wasmvm";


    public static final String CONTRACTHASH_ONG = "0200000000000000000000000000000000000000";

    public static final String CONTRACTHASH_ONT = "0100000000000000000000000000000000000000";

    public static final String CONTRACTHASH_ONTID = "0300000000000000000000000000000000000000";

    public static final String HTTPHEADER_TOKEN = "ont_exp_token";

    public static final String JWT_LOGINID = "loginId";


    /**
     * management url
     */
    public static final String MANAGEMENT_USER_URI = "/v2/users";

    public static final String MANAGEMENT_USERADDRESS_URI = "/v2/users/addresses";

    public static final String MANAGEMENT_NEWUSERADDRESS_URI = "/v2/users/new_address";

    /**
     * node verification
     */
    public static final Integer NODE_NOT_VERIFIED = 0;
    public static final Integer NODE_VERIFIED = 1;

    public static final Integer CANDIDATE_NODE = 1;

    public static final Integer UTC_20210801 = 1627776000;

    public static final String EVM_ADDRESS_PREFIX = "0x";

    public static final Map<String, String> CONTRACT_TAG = new HashMap<>();

    public static final String BALANCE_V2_URL = "%s/api/v1/balancev2/%s";

    public static final String GET_TRANSACTION_URL = "%s/api/v1/transaction/%s";

    public static final String GET_MEMPOOL_TRANSACTION_URL = "%s/api/v1/mempool/txstate/%s";

    public static final String GET_BLOCK_HASH_URL = "%s/api/v1/block/hash/%d";

    public static final String FUN_QUERY_TOKEN_IDS_BY_OWNER_ADDR = "queryTokenIDsByOwnerAddr";

    public static final String FUN_BALANCE_OF = "balanceOf";

    public static final String FUN_TOTAL_SUPPLY = "totalSupply";

    /**
     * pattern
     */
    public static final Pattern BASE58_ADDRESS_PATTERN = Pattern.compile("A[0-9a-zA-Z]{33}");

    public static final Pattern BLOCK_HEIGHT_PATTERN = Pattern.compile("^\\d+$");

    /**
     * export csv
     */
    public static final String RECAPTCHA_VERIFY_URL = "https://recaptcha.net/recaptcha/api/siteverify";

    public static final String LANGUAGE_CN = "cn";

    public static final String DESCRIPTION_DEPLOY_CONTRACTS = "Deploy Contracts";

    public static final String DESCRIPTION_TRANSACTION_FEE = "Transaction Fee";

    public static final String DESCRIPTION_TRANSFER = "Transfer";

    public static final String DESCRIPTION_REGISTER_ONT_ID = "Register ONT ID";

    public static final String DESCRIPTION_APPROVAL = "Approval";

    public static final String DESCRIPTION_OTHERS = "Others";

    public static final List<String> EXPORT_TX_TITLE_EN = Arrays.asList("\"Transaction Hash\"", "\"Block Height\"", "\"Unix Timestamp\"", "\"UTC\"",
            "\"From\"", "\"To\"", "\"Amount\"", "\"Asset Name\"", "\"Contract Hash\"", "\"Description\"", "\"Status\"");

    public static final List<String> EXPORT_TX_TITLE_CN = Arrays.asList("\"Transaction Hash\"", "\"Block Height\"", "\"Unix Timestamp\"", "\"UTC+8\"",
            "\"From\"", "\"To\"", "\"Amount\"", "\"Asset Name\"", "\"Contract Hash\"", "\"Description\"", "\"Status\"");

    public static final List<String> EXPORT_TX_KEY = Arrays.asList("tx_hash", "block_height", "tx_time", "utc",
            "from_address", "to_address", "amount", "asset_name", "contract_hash", "description", "confirm_flag");

    public static final String EXPORT_TX_FILENAME = "export-%s.csv";

    /**
     * decode input data
     */
    public static final String NATIVE_INPUT_DATA_END = "0068164f6e746f6c6f67792e4e61746976652e496e766f6b65";

    public static final String NATIVE_STRUCT_START = "00c66b";

    public static final String NATIVE_STRUCT_END = "6c";

    public static final String NATIVE_SECOND_STRUCT_START = NATIVE_STRUCT_END + NATIVE_STRUCT_START;

    public static final String NATIVE_ARGS_OP_CODE = "6a7cc8";

    public static final String OP_PACK = "c1";

    public static final String NATIVE_TYPE_STRUCT = "Struct";

    public static final String TYPE_ARRAY_SUFFIX = "[]";

    public static final String FUNCTION_PARAM_START = "(";
    public static final String FUNCTION_PARAM_END = ")";
    public static final String BLANK = " ";
    public static final String FUNCTION_PARAM_SPLIT = ",";
}
