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

import java.math.BigDecimal;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public class ConstantParam {

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
