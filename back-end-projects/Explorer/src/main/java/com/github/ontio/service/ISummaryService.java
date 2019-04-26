package com.github.ontio.service;

import com.github.ontio.paramBean.OldResult;

/**
 * @author king
 * @version 1.0
 * @date 2018/12/14
 */
public interface ISummaryService {

    /**
     * Marketing Info
     * @return
     */
    OldResult summaryAllInfo();

    /**
     * query current information
     *
     * @param amount
     * @return
     */
    OldResult querySummary(int amount);

    /**
     * TPS查询
     * @return
     */
    OldResult queryTps();

    /**
     * 项目信息查询
     * @param project
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    OldResult queryProjectInfo(String project, String type, int startTime, int endTime);

    /**
     * 日常统计查询
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    OldResult querySummary(String type, int startTime, int endTime);

    /**
     * 合约统计查询
     * @param contractHash
     * @param type
     * @param startTime
     * @param endTime
     * @return
     */
    OldResult queryContract(String contractHash, String type, int startTime, int endTime);


    OldResult queryTotalSupply();

    OldResult queryNativeTotalSupply();

}
