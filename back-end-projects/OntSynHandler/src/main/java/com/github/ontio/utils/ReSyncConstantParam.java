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

import com.github.ontio.model.common.BatchBlockDto;

import java.util.ArrayList;
import java.util.List;

public final class ReSyncConstantParam {
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


}
