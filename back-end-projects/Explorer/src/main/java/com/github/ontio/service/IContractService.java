package com.github.ontio.service;

import com.github.ontio.paramBean.OldResult;

/**
 * @author king
 * @version 1.0
 * @date 12.6
 */
public interface IContractService {

    /**
     * query Contracts by page
     * @param pagesize
     * @param pageNum
     * @return
     */
    OldResult queryContract(Integer pagesize, Integer pageNum);

    /**
     * query txn by page
     * @param contractHash   contractHash
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    OldResult queryContractByHash(String contractHash, int pageSize, int pageNumber);

    /**
     * query txn by page
     * @param type   type
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    OldResult queryOEPContract(String type, int pageSize, int pageNumber);

    /**
     *  依据合约hash查询Token合约
     * @param contractHash   contractHash
     * @param type   type
     * @param tokenName   tokenName
     * @param pageSize   the amount of each page
     * @param pageNumber the start page
     * @return
     */
     OldResult queryOEPContractByHashAndTokenName(String contractHash, String type, String tokenName, int pageSize, int pageNumber);



     OldResult queryDappstoreContractInfo(Integer pageSize, Integer pageNumber);


     OldResult queryDappstore24hSummary();
}
