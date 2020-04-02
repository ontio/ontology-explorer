package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;

import java.util.Date;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/8
 */
public interface ITokenService {

    ResponseBean queryTokensByPage(String tokenType, Integer pageNumber, Integer pageSize, List<String> sorts);

    ResponseBean queryTokenDetail(String tokenType, String contractHash);

    ResponseBean queryOep8TxsByPage(String contractHash, String tokenName, Integer pageNumber, Integer pageSize);

    ResponseBean queryDailyAggregations(String tokenType, String contractHash, Date from, Date to);

    ResponseBean queryRankings(List<Short> rankingIds, short duration);

    ResponseBean queryPrice(String token, String fiat);

}
