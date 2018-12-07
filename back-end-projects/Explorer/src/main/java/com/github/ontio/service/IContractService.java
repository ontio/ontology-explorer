package com.github.ontio.service;

import com.github.ontio.paramBean.Result;

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
    Result queryContract(Integer pagesize, Integer pageNum);

    /**
     * query txn by page
     * @param contractHash   contractHash
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    Result queryContractByHash(String contractHash, int pageSize, int pageNumber);

    /**
     * query txn by page
     * @param type   type
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    Result queryOEPContract(String type, int pageSize, int pageNumber);

    /**
     * query txn by page
     * @param type   type
     * @param pageNumber the start page
     * @param pageSize   the amount of each page
     * @return
     */
    Result queryOEPContractByHash(String contractHash, String type, int pageSize, int pageNumber);
}
