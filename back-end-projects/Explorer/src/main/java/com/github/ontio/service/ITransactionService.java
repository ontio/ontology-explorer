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

import com.github.ontio.paramBean.Result;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public interface ITransactionService {

    /**
     * query txn by txid
     *
     * @param txid
     * @return
     */
    Result queryTxnDetailByHash(String txid);

    /**
     * query the last few transactions
     *
     * @return
     */
    Result queryTxnList(int amount);

    /**
     * query txn by page
     *
     * @param pageSize
     * @param pageNumber
     * @return
     */
    Result queryTxnList(int pageSize, int pageNumber);

    /**
     *  query asset balance and transactions
     *
     * @param address
     * @return
     */
    Result queryAddressInfo(String address, int pageNumber, int pageSize);

    /**
     *  query the specially asset balance and transactions
     *
     * @param address
     * @return
     */
    Result queryAddressInfo(String address, int pageNumber, int pageSize, String assetName);

    /**
     *  query the specially asset balance and transactions
     *
     * @param address
     * @return
     */
    Result queryAddressInfoByTimeAndPage(String address, String assetName, int pageSize, int time);

    /**
     *  query the specially asset balance and transactions
     *
     * @param address
     * @return
     */
    Result queryAddressInfoByTime(String address, String assetName, int beginTime, int endTime);

    /**
     *  query the specially asset balance and transactions
     *
     * @param address
     * @return
     */
    Result queryAddressInfoByTime(String address, String assetName, int beginTime);

    /**
     * 查询地址余额
     * @param address
     * @return
     */
    Result queryAddressBalance(String address);

}
