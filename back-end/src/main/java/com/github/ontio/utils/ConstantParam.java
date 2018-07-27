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

import com.github.ontio.OntSdk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public class ConstantParam {


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
     * the number of transactions of one block
     *
     */
    public static int TXN_INIT_AMOUNT = 0;


    /**
     * the number of ontid transactions of one block
     *
     */
    public static int ONTIDTXN_INIT_AMOUNT = 0;

    /**
     * Ontology SDK object
     */
    public static OntSdk ONT_SDKSERVICE = null;


    /**
     * transfer transaction smart contract event description
     *
     */
    public static final String AUTH_OPE = "auth";


    /**
     * transfer transaction smart contract event description
     *
     */
    public static final String TRANSFER_OPE = "transfer";

    /**
     * ontId operation transaction smart contract event description
     *
     */
    public static final String ONTID_OPE_PREFIX = "ontId-";

    /**
     * record transaction smart contract event description
     *
     */
    public static final String CLAIMRECORD_OPE = "claimRecord-";

    /**
     * registerCandidate
     */
    public static final String AUTH_OPE_PREFIX = "auth";


    public static final String CLAIMRECORD_OPE_PREFIX = "Push";

    public static final String REGISTER = "Register";

    /**
     * claim
     */
    public static final String CLAIM = "claim";

    /**
     * add action
     *
     */
    public static final String ADD = "add";

    /**
     * ONG asset
     */
    public static final String ONG = "ong";

    /**
     * ONT asset
     */
    public static final String ONT = "ont";





    public static final BigDecimal ZERO = new BigDecimal("0");


    public static final BigDecimal ONG_SECONDMAKE = new BigDecimal("5");


    public static final BigDecimal ONT_TOTAL = new BigDecimal("1000000000");



}
