package com.github.ontio.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.Oep4Mapper;
import com.github.ontio.mapper.Oep5Mapper;
import com.github.ontio.mapper.Oep8Mapper;
import com.github.ontio.model.dao.Oep4;
import com.github.ontio.model.dao.Oep5;
import com.github.ontio.model.dto.BalanceDto;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.OntologySDKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/10
 */
@Component
@Slf4j
public class BalanceTask {

    @Autowired
    private Oep4Mapper oep4Mapper;
    @Autowired
    private Oep5Mapper oep5Mapper;
    @Autowired
    private Oep8Mapper oep8Mapper;
    @Autowired
    private ParamsConfig paramsConfig;

    private OntologySDKService sdk;

    private void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(paramsConfig);
        }
    }

    @Async
    public Future<List<BalanceDto>> getOep4Balance(String address) {

        log.info("getOep4Balance begin");
        initSDK();
        List<BalanceDto> balanceList = new ArrayList<>();

        //审核过的OEP4余额
        Oep4 oep4Temp = new Oep4();
        oep4Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);
        List<Oep4> oep4s = oep4Mapper.select(oep4Temp);
        for (Oep4 oep4 :
                oep4s) {
            String contractHash = oep4.getContractHash();
            BigDecimal balance = new BigDecimal(sdk.getOep4AssetBalance(address, contractHash)).divide(new BigDecimal(Math.pow(10, oep4.getDecimals())));
            if (balance.compareTo(ConstantParam.ZERO) == 0) {
                continue;
            }
            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName(oep4.getSymbol())
                    .assetType(ConstantParam.ASSET_TYPE_OEP4)
                    .balance(balance)
                    .build();
            balanceList.add(balanceDto);
        }
        return new AsyncResult<>(balanceList);
    }

    @Async
    public Future<List<BalanceDto>> getOep5Balance(String address) {

        log.info("getOep5Balance begin");

        initSDK();
        List<BalanceDto> balanceList = new ArrayList<>();

        //审核过的OEP5余额
        Oep5 oep5Temp = new Oep5();
        oep5Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);
        List<Oep5> oep5s = oep5Mapper.select(oep5Temp);
        for (Oep5 oep5 :
                oep5s) {
            String contractHash = oep5.getContractHash();
            BigDecimal balance = new BigDecimal(sdk.getOep5AssetBalance(address, contractHash));
            if (balance.compareTo(ConstantParam.ZERO) == 0) {
                continue;
            }
            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName(oep5.getSymbol())
                    .assetType(ConstantParam.ASSET_TYPE_OEP5)
                    .balance(balance)
                    .build();
            balanceList.add(balanceDto);
        }
        return new AsyncResult<>(balanceList);
    }

    @Async
    public Future<List<BalanceDto>> getOep8Balance(String address) {

        log.info("getOep8Balance begin");
        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initSDK();
        List<BalanceDto> balanceList = new ArrayList<>();

        //审核过的OEP8余额
        List<Map<String, String>> oep8s = oep8Mapper.selectAuditPassedOep8();
        for (Map<String, String> map :
                oep8s) {
            String contractHash = map.get("contractHash");
            String symbol = map.get("symbol");

            JSONArray balanceArray = sdk.getOpe8AssetBalance(address, contractHash);
            String[] symbolArray = symbol.split(",");
            for (int i = 0; i < symbolArray.length; i++) {
                if (Integer.parseInt((String) balanceArray.get(i)) == 0) {
                    continue;
                }
                BalanceDto balanceDto = BalanceDto.builder()
                        .assetName(symbolArray[i])
                        .assetType(ConstantParam.ASSET_TYPE_OEP8)
                        .balance(new BigDecimal((String) balanceArray.get(i)))
                        .build();
                balanceList.add(balanceDto);
            }
        }

        return new AsyncResult<>(balanceList);
    }


    /**
     * 根据OEP合约hash获取对应的余额
     *
     * @param address
     * @param assetType
     * @param contractHash
     * @param symbol
     * @param decimals
     * @return
     */
    @Async
    public Future<List<BalanceDto>> getBalance(String address, String assetType, String contractHash, String symbol, Integer decimals) {

        log.info("{} getbalance....", Thread.currentThread().getName());

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();
        if (ConstantParam.ASSET_TYPE_OEP4.equals(assetType)) {

            BigDecimal balance = new BigDecimal(sdk.getOep4AssetBalance(address, contractHash)).divide(new BigDecimal(Math.pow(10, decimals)));
            if (balance.compareTo(ConstantParam.ZERO) != 0) {
                BalanceDto balanceDto = BalanceDto.builder()
                        .assetName(symbol)
                        .assetType(ConstantParam.ASSET_TYPE_OEP4)
                        .balance(balance)
                        .build();
                balanceList.add(balanceDto);
            }
        } else if (ConstantParam.ASSET_TYPE_OEP5.equals(assetType)) {

            BigDecimal balance = new BigDecimal(sdk.getOep5AssetBalance(address, contractHash));
            if (balance.compareTo(ConstantParam.ZERO) != 0) {
                BalanceDto balanceDto = BalanceDto.builder()
                        .assetName(symbol)
                        .assetType(ConstantParam.ASSET_TYPE_OEP5)
                        .balance(balance)
                        .build();
                balanceList.add(balanceDto);
            }
        } else if (ConstantParam.ASSET_TYPE_OEP8.equals(assetType)) {

            JSONArray balanceArray = sdk.getOpe8AssetBalance(address, contractHash);
            String[] symbolArray = symbol.split(",");
            for (int i = 0; i < symbolArray.length; i++) {
                if (Integer.parseInt((String) balanceArray.get(i)) != 0) {
                    BalanceDto balanceDto = BalanceDto.builder()
                            .assetName(symbolArray[i])
                            .assetType(ConstantParam.ASSET_TYPE_OEP8)
                            .balance(new BigDecimal((String) balanceArray.get(i)))
                            .build();
                    balanceList.add(balanceDto);
                }
            }
        }
        return new AsyncResult<>(balanceList);
    }


}
