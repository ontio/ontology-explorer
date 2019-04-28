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

import com.github.ontio.paramBean.OldResult;
import com.github.ontio.paramBean.ResponseBean;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public interface ITransactionService {

    /**
     * query latest transactions
     *
     * @return
     */
    ResponseBean queryLatestTxs(int count);

    /**
     * query latest transactions
     *
     * @return
     */
    ResponseBean queryTxsByPage(int pageNumber, int pageSize);

    /**
     * query latest transactions
     *
     * @return
     */
    ResponseBean queryLatestNonontidTxs(int count);

    /**
     * query latest transactions
     *
     * @return
     */
    ResponseBean queryNonontidTxsByPage(int pageNumber, int pageSize);


    ResponseBean queryTxDetailByHash(String txHash);



    /**
     * query txn by txid
     *
     * @param txid
     * @return
     */
    OldResult queryTxnDetailByHash(String txid);





    /**
     *  query asset balance and transactions
     *
     * @param address
     * @return
     */
    OldResult queryAddressInfo(String address, int pageNumber, int pageSize);

    /**
     *  query the specially asset balance and transactions
     *
     * @param address
     * @return
     */
    OldResult queryAddressInfo(String address, int pageNumber, int pageSize, String assetName);

    /**
     *  query the specially asset balance and transactions
     *
     * @param address
     * @return
     */
    OldResult queryAddressInfoByTimeAndPage(String address, String assetName, int pageSize, int time);

    /**
     *  query the specially asset balance and transactions
     *
     * @param address
     * @return
     */
    OldResult queryAddressInfoByTime(String address, String assetName, int beginTime, int endTime);

    /**
     *  query the specially asset balance and transactions
     *
     * @param address
     * @return
     */
    OldResult queryAddressInfoByTime(String address, String assetName, int beginTime);

    /**
     * 查询地址余额
     * @param address
     * @return
     */
    OldResult queryAddressBalance(String address);

    /**
     * 查询地址所有交易
     * @param address
     * @return
     */
    OldResult queryAddressInfoForExcel(String address);
}
