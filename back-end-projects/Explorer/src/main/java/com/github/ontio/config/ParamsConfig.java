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


package com.github.ontio.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Service("ParamsConfig")
public class ParamsConfig {

    public static final String ONT_CONTRACT_HASH = "0100000000000000000000000000000000000000";

    public static final String ONG_CONTRACT_HASH = "0200000000000000000000000000000000000000";

    /**
     * 虚拟合约，表示对所有合约加总的统计
     */
    public static final String VIRTUAL_CONTRACT_ALL = "$$ALL$$";

    /**
     * 虚拟合约，表示对 ONT 和 ONG 加总的统计
     */
    public static final String VIRTUAL_CONTRACT_NATIVE = "$$NATIVE$$";

    /**
     * 虚拟合约，表示对所有 OEP4 合约加总的统计
     */
    public static final String VIRTUAL_CONTRACT_OEP4 = "$$OEP4$$";

    private static final Collection<String> VIRTUAL_CONTRACTS;

    static {
        Set<String> virtualContracts = new HashSet<>(Arrays.asList(VIRTUAL_CONTRACT_ALL, VIRTUAL_CONTRACT_NATIVE,
                VIRTUAL_CONTRACT_OEP4));
        VIRTUAL_CONTRACTS = Collections.unmodifiableSet(virtualContracts);
    }

    @Value("${masternode.restful.url}")
    public String MASTERNODE_RESTFUL_URL;

    @Value("${blockchain.node.count}")
    public Integer BLOCKCHAIN_NODE_COUNT;

    @Value("${blockchain.max.tps}")
    public Integer BLOCKCHAIN_MAX_TPS;

    @Value("${ong.second.generate}")
    public BigDecimal ONG_SECOND_GENERATE;

    @Value("${reqlimit.expire.millisecond}")
    public Integer REQLIMIT_EXPIRE_MILLISECOND;

    @Value("${oep5.dragon.contractHash}")
    public String OEP5_DRAGON_CONTRACTHASH;

    @Value("${dappbind.contracthash}")
    public String DAPPBIND_CONTRACTHASH;

    @Value("${dapp.reward.percentage}")
    public Integer DAPP_REWARD_PERCENTAGE;

    @Value("${node.reward.percentage}")
    public Integer NODE_REWARD_PERCENTAGE;

    @Value("${balanceservice.host}")
    public String BALANCESERVICE_HOST;

    @Value("${querybalance.mode}")
    public Integer QUERYBALANCE_MODE;

    @Value("${config.newStakingRoundBlockCount}")
    public Long newStakingRoundBlockCount;

    @Value("#{'${config.hosts}'.split(',')}")
    private List<String> hosts = new ArrayList<>();

    public interface Field {

        String maxStakingChangeCount = "maxStakingChangeCount";

    }


    @Value("${tomcat.maxThread}")
    public Integer TOMCAT_MAXTHREAD;

    @Value("${oep8.pumpkin.contractHash}")
    public String OEP8_PUMPKIN_CONTRACTHASH;

    @Value("${coinmarketcap.api.key}")
    private String coinMarketCapApiKey;

    @Value("${coinmarketcap.api.host:https://pro-api.coinmarketcap.com/}")
    private String coinMarketCapApiHost;

    @Value("${coinmarketcap.refresh.interval:15}")
    private int coinMarketCapRefreshInterval;

    public String getContractHash(String token) {
        switch (token.toLowerCase()) {
            case "ont":
                return ONT_CONTRACT_HASH;
            case "ong":
                return ONG_CONTRACT_HASH;
            case "native":
                return VIRTUAL_CONTRACT_NATIVE;
            case "oep4":
                return VIRTUAL_CONTRACT_OEP4;
            case "all":
                return VIRTUAL_CONTRACT_ALL;
            default:
                throw new IllegalArgumentException("unsupported token: " + token);
        }
    }

    @Value("${login.callbackUrl:https://explorer.ont.io/v2/users/login}")
    public String loginCallbackUrl;


    @Value("${login.token.expired.minute:5}")
    public int loginTokenExpiredMinute;

    @Value("${identity.ontid}")
    public String IDENTITY_ONTID;

    @Value("${identity.password}")
    public String IDENTITY_PASSWORD;

    @Value("${identity.salt}")
    public String IDENTITY_SALT;

}