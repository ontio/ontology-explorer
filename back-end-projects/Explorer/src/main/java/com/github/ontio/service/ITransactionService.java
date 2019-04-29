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
import com.github.ontio.model.common.ResponseBean;

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
     * @param txHash
     * @return
     */
    ResponseBean queryTxDetailByHash(String txHash);




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


}
