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


package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.network.exception.ConnectorException;

import java.io.IOException;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public interface ITransactionService {

    /**
     * query latest transaction list
     *
     * @return
     */
    ResponseBean queryLatestTxs(int count);

    /**
     * query transaction list by page
     *
     * @return
     */
    ResponseBean queryTxsByPage(int pageNumber, int pageSize);

    /**
     * query latest nonontid transaction list
     *
     * @return
     */
    ResponseBean queryLatestNonontidTxs(int count);

    /**
     * query nonontid transaction list by page
     *
     * @return
     */
    ResponseBean queryNonontidTxsByPage(int pageNumber, int pageSize);

    /**
     * query transaction detail by hash
     *
     * @param txHash
     * @return
     */
    ResponseBean queryTxDetailByHash(String txHash);


    ResponseBean queryInputDataByHash(String txHash) throws IOException, ConnectorException;

    ResponseBean queryInputDataAndDecode(String txHash);

    ResponseBean decodeInputData(String inputData);

}
