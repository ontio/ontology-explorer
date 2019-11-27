package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/8
 */
public interface ITokenService {

    ResponseBean queryTokensByPage(String tokenType, Integer pageNumber, Integer pageSize);

    ResponseBean queryTokenDetail(String tokenType, String contractHash);

    ResponseBean queryOep8TxsByPage(String contractHash, String tokenName, Integer pageNumber, Integer pageSize);


}
