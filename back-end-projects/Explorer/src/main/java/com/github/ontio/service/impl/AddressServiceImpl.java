package com.github.ontio.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.Oep4Mapper;
import com.github.ontio.mapper.Oep5Mapper;
import com.github.ontio.mapper.Oep8Mapper;
import com.github.ontio.mapper.TxDetailMapper;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.Oep4;
import com.github.ontio.model.dao.Oep5;
import com.github.ontio.model.dao.Oep8;
import com.github.ontio.model.dto.BalanceDto;
import com.github.ontio.model.dto.QueryBatchBalanceDto;
import com.github.ontio.model.dto.TransferTxDetailDto;
import com.github.ontio.model.dto.TransferTxDto;
import com.github.ontio.service.IAddressService;
import com.github.ontio.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/28
 */
@Slf4j
@Service("AddressService")
public class AddressServiceImpl implements IAddressService {

    private final Oep4Mapper oep4Mapper;
    private final Oep8Mapper oep8Mapper;
    private final Oep5Mapper oep5Mapper;
    private final TxDetailMapper txDetailMapper;
    private final ParamsConfig paramsConfig;
    private final CommonService commonService;

    @Autowired
    public AddressServiceImpl(Oep4Mapper oep4Mapper, Oep8Mapper oep8Mapper, Oep5Mapper oep5Mapper, TxDetailMapper txDetailMapper, ParamsConfig paramsConfig, CommonService commonService) {
        this.oep4Mapper = oep4Mapper;
        this.oep8Mapper = oep8Mapper;
        this.oep5Mapper = oep5Mapper;
        this.txDetailMapper = txDetailMapper;
        this.paramsConfig = paramsConfig;
        this.commonService = commonService;
    }

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(paramsConfig);
        }
    }

    private static final String BALANCESERVICE_ACTION_GETMULTIBALANCE = "getmultibalance";

    private static final String BALANCESERVICE_VERSION = "1.0.0";

    private static final Integer TIMESTAMP_20190630000000_UTC = 1561852800;


    @Override
    public ResponseBean queryAddressBalance(String address, String tokenType) {

        List<BalanceDto> balanceList = new ArrayList<>();

        if (paramsConfig.QUERYBALANCE_MODE == 1) {
            switch (tokenType.toLowerCase()) {
                case ConstantParam.ASSET_TYPE_OEP4:
                    balanceList = getOep4Balance(address, "");
                    break;
                case ConstantParam.ASSET_TYPE_OEP5:
                    balanceList = getOep5Balance(address, "", "");
                    break;
                case ConstantParam.ASSET_TYPE_OEP8:
                    balanceList = getOep8Balance(address, "");
                    break;
                case ConstantParam.ASSET_TYPE_NATIVE:
                    balanceList = getNativeBalance(address);
                    break;
                case ConstantParam.ASSET_TYPE_ALL:
                    balanceList = getAllAssetBalance(address);
                    break;
                default:
                    break;
            }
        } else if (paramsConfig.QUERYBALANCE_MODE == 0) {
            switch (tokenType.toLowerCase()) {
                case ConstantParam.ASSET_TYPE_OEP4:
                    balanceList = getOep4BalanceOld(address, "");
                    break;
                case ConstantParam.ASSET_TYPE_OEP5:
                    balanceList = getOep5BalanceOld(address, "", "");
                    break;
                case ConstantParam.ASSET_TYPE_OEP8:
                    balanceList = getOep8BalanceOld(address, "");
                    break;
                case ConstantParam.ASSET_TYPE_NATIVE:
                    balanceList = getNativeBalance(address);
                    break;
                case ConstantParam.ASSET_TYPE_ALL:
                    balanceList = getAllAssetBalanceOld(address);
                    break;
                default:
                    break;
            }
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), balanceList);
    }

    @Override
    public ResponseBean queryAddressBalanceByAssetName(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();

        if (ConstantParam.ONT.equals(assetName)) {

            initSDK();
            Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
            //ONT
            BalanceDto balanceDto4 = BalanceDto.builder()
                    .assetName(ConstantParam.ONT)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)))
                    .build();
            balanceList.add(balanceDto4);

        } else if (ConstantParam.ONG.equals(assetName)) {

            initSDK();
            Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
            //ONG
            BalanceDto balanceDto1 = BalanceDto.builder()
                    .assetName(ConstantParam.ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal((String) balanceMap.get(ConstantParam.ONG)).divide(ConstantParam.ONG_TOTAL)))
                    .build();
            balanceList.add(balanceDto1);

            //waiting bound ONG
            String waitBoundOng = calculateWaitingBoundOng(address, (String) balanceMap.get(ConstantParam.ONT));
            BalanceDto balanceDto2 = BalanceDto.builder()
                    .assetName(ConstantParam.WAITBOUND_ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal(waitBoundOng)))
                    .build();
            balanceList.add(balanceDto2);

            //Claimable ONG
            String unBoundOng = sdk.getUnBoundOng(address);
            if (Helper.isEmptyOrNull(unBoundOng)) {
                unBoundOng = "0";
            }
            BalanceDto balanceDto3 = BalanceDto.builder()
                    .assetName(ConstantParam.UNBOUND_ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal(unBoundOng)))
                    .build();
            balanceList.add(balanceDto3);
        } else {
            Oep4 oep4 = new Oep4();
            oep4.setSymbol(assetName);
            oep4 = oep4Mapper.selectOne(oep4);
            if (Helper.isNotEmptyOrNull(oep4)) {
                balanceList = getOep4Balance(address, assetName);
            } else {
                Oep5 oep5 = new Oep5();
                oep5.setSymbol(assetName);
                oep5 = oep5Mapper.selectOne(oep5);
                if (Helper.isNotEmptyOrNull(oep5)) {
                    balanceList = getOep5Balance(address, assetName, "");
                } else {
                    Oep8 oep8 = new Oep8();
                    oep8.setSymbol(assetName);
                    List<Oep8> oep8s = oep8Mapper.select(oep8);
                    if (oep8s.size() > 0) {
                        balanceList = getOep8Balance(address, assetName);
                    }
                }
            }
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), balanceList);
    }


    @Override
    public ResponseBean queryAddressBalanceByAssetName4Onto(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();

        if (ConstantParam.ONT.equals(assetName)) {

            initSDK();
            Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
            //ONT
            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName(ConstantParam.ONT)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)))
                    .build();
            balanceList.add(balanceDto);

        } else if (ConstantParam.ONG.equals(assetName)) {

            initSDK();
            Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
            //ONG
            BalanceDto balanceDto1 = BalanceDto.builder()
                    .assetName(ConstantParam.ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal((String) balanceMap.get(ConstantParam.ONG)).divide(ConstantParam.ONG_TOTAL)))
                    .build();
            balanceList.add(balanceDto1);

            //waiting bound ONG
            String waitBoundOng = calculateWaitingBoundOng(address, (String) balanceMap.get(ConstantParam.ONT));
            BalanceDto balanceDto2 = BalanceDto.builder()
                    .assetName(ConstantParam.WAITBOUND_ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal(waitBoundOng)))
                    .build();
            balanceList.add(balanceDto2);

            //Claimable ONG
            String unBoundOng = sdk.getUnBoundOng(address);
            if (Helper.isEmptyOrNull(unBoundOng)) {
                unBoundOng = "0";
            }
            BalanceDto balanceDto3 = BalanceDto.builder()
                    .assetName(ConstantParam.UNBOUND_ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal(unBoundOng)))
                    .build();
            balanceList.add(balanceDto3);
        }
        if (paramsConfig.QUERYBALANCE_MODE == 1) {
            //return oep4+oep5 token balance
            balanceList.addAll(getOep4Balance(address, ""));
            balanceList.addAll(getOep5Balance(address, "", ConstantParam.CHANNEL_ONTO));
            if (assetName.startsWith(ConstantParam.PUMPKIN_PREFIX)) {
                balanceList.addAll(getOep8Balance4Onto(address, ""));
            }
        } else if (paramsConfig.QUERYBALANCE_MODE == 0) {
            //return oep4+oep5 token balance
            balanceList.addAll(getOep4BalanceOld(address, ""));
            balanceList.addAll(getOep5BalanceOld(address, "", ConstantParam.CHANNEL_ONTO));
            if (assetName.startsWith(ConstantParam.PUMPKIN_PREFIX)) {
                balanceList.addAll(getOep8Balance4OntoOld(address, ""));
            }
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), balanceList);
    }

    /**
     * 获取原生资产余额
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getNativeBalance(String address) {

        List<BalanceDto> balanceList = new ArrayList<>();

        initSDK();
        Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
        //ONG
        BalanceDto balanceDto1 = BalanceDto.builder()
                .assetName(ConstantParam.ONG)
                .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                .balance((new BigDecimal((String) balanceMap.get(ConstantParam.ONG)).divide(ConstantParam.ONG_TOTAL)))
                .build();
        balanceList.add(balanceDto1);

        //waiting bound ONG
        String waitBoundOng = calculateWaitingBoundOng(address, (String) balanceMap.get(ConstantParam.ONT));
        BalanceDto balanceDto2 = BalanceDto.builder()
                .assetName(ConstantParam.WAITBOUND_ONG)
                .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                .balance((new BigDecimal(waitBoundOng)))
                .build();
        balanceList.add(balanceDto2);

        //Claimable ONG
        String unBoundOng = sdk.getUnBoundOng(address);
        if (Helper.isEmptyOrNull(unBoundOng)) {
            unBoundOng = "0";
        }
        BalanceDto balanceDto3 = BalanceDto.builder()
                .assetName(ConstantParam.UNBOUND_ONG)
                .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                .balance((new BigDecimal(unBoundOng)))
                .build();
        balanceList.add(balanceDto3);

        //ONT
        BalanceDto balanceDto4 = BalanceDto.builder()
                .assetName(ConstantParam.ONT)
                .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)))
                .build();
        balanceList.add(balanceDto4);

        return balanceList;
    }

    /**
     * get all type asset balance
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getAllAssetBalance(String address) {

        List<BalanceDto> balanceDtos = new ArrayList<>();
        balanceDtos.addAll(getNativeBalance(address));
        balanceDtos.addAll(getOep4Balance(address, ""));
        balanceDtos.addAll(getOep5Balance(address, "", ""));
        balanceDtos.addAll(getOep8Balance(address, ""));
        return balanceDtos;
    }


    /**
     * get all type asset balance
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getAllAssetBalanceOld(String address) {

        List<BalanceDto> balanceDtos = new ArrayList<>();
        balanceDtos.addAll(getNativeBalance(address));
        balanceDtos.addAll(getOep4BalanceOld(address, ""));
        balanceDtos.addAll(getOep5BalanceOld(address, "", ""));
        balanceDtos.addAll(getOep8BalanceOld(address, ""));
        return balanceDtos;
    }


    /**
     * 获取OEP4余额
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep4BalanceOld(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();
        //审核过的OEP4余额
        Oep4 oep4Temp = new Oep4();
        oep4Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);
        if (Helper.isNotEmptyOrNull(assetName)) {
            oep4Temp.setSymbol(assetName);
        }
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
        return balanceList;
    }


    /**
     * 获取OEP5余额
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep5BalanceOld(String address, String assetName, String channel) {

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();
        //审核过的OEP5余额
        Oep5 oep5Temp = new Oep5();
        oep5Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);
        if (Helper.isNotEmptyOrNull(assetName)) {
            oep5Temp.setSymbol(assetName);
        }
        List<Oep5> oep5s = oep5Mapper.select(oep5Temp);
        for (Oep5 oep5 :
                oep5s) {
            String contractHash = oep5.getContractHash();
            BigDecimal balance = new BigDecimal(sdk.getOep5AssetBalance(address, contractHash));
            if (balance.compareTo(ConstantParam.ZERO) == 0) {
                continue;
            }
            if (ConstantParam.CHANNEL_ONTO.equals(channel)) {
                //ONTO return name
                BalanceDto balanceDto = BalanceDto.builder()
                        .assetName(oep5.getName())
                        .assetType(ConstantParam.ASSET_TYPE_OEP5)
                        .balance(balance)
                        .build();
                balanceList.add(balanceDto);
            } else {
                //other return symbol
                BalanceDto balanceDto = BalanceDto.builder()
                        .assetName(oep5.getSymbol())
                        .assetType(ConstantParam.ASSET_TYPE_OEP5)
                        .balance(balance)
                        .build();
                balanceList.add(balanceDto);
            }
        }
        return balanceList;
    }


    /**
     * 获取OEP8余额
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep8BalanceOld(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();
        //审核过的OEP8余额
        if (Helper.isNotEmptyOrNull(assetName)) {
            assetName = assetName + "%";
        }
        List<Map<String, String>> oep8s = oep8Mapper.selectAuditPassedOep8(assetName);
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
        return balanceList;
    }


    /**
     * get oep4 balance
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep4Balance(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        //query audit passed oep4 token
        Oep4 oep4Temp = new Oep4();
        oep4Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);
        if (Helper.isNotEmptyOrNull(assetName)) {
            oep4Temp.setSymbol(assetName);
        }
        List<Oep4> oep4s = oep4Mapper.select(oep4Temp);
        oep4s.forEach(item -> stringBuilder.append(item.getContractHash()).append(","));
        String contractAddrsStr = stringBuilder.toString();
        if (Helper.isEmptyOrNull(contractAddrsStr)) {
            return balanceList;
        }

        QueryBatchBalanceDto queryBatchBalanceDto = QueryBatchBalanceDto.builder()
                .action(BALANCESERVICE_ACTION_GETMULTIBALANCE)
                .version(BALANCESERVICE_VERSION)
                .base58Addrs(address)
                .contractAddrs(contractAddrsStr.substring(0, contractAddrsStr.length() - 1))
                .build();

        String responseStr = commonService.httpPostRequest(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL,
                JacksonUtil.beanToJSonStr(queryBatchBalanceDto), null);
        if (Helper.isNotEmptyOrNull(responseStr)) {
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            JSONArray oepBalanceArray = ((JSONObject) jsonObject.getJSONArray("Result").get(0)).getJSONArray("OepBalance");

            Map<String, String> map = new HashMap<>();
            oepBalanceArray.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                map.put(obj.getString("contract_address"), obj.getString("balance"));
            });

            oep4s.forEach(item -> {
                        String contractHash = item.getContractHash();
                        BigDecimal balance = new BigDecimal(map.get(contractHash));
                        //only return balance != 0 token
                        if (balance.compareTo(ConstantParam.ZERO) != 0) {
                            BalanceDto balanceDto = BalanceDto.builder()
                                    .assetName(item.getSymbol())
                                    .assetType(ConstantParam.ASSET_TYPE_OEP4)
                                    .balance(balance)
                                    .build();
                            balanceList.add(balanceDto);
                        }
                    }
            );
        }
        return balanceList;
    }


    /**
     * get oep5 balance
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep5Balance(String address, String assetName, String channel) {

        List<BalanceDto> balanceList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        //query audit passed oep5 token
        Oep5 oep5Temp = new Oep5();
        oep5Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);
        if (Helper.isNotEmptyOrNull(assetName)) {
            oep5Temp.setSymbol(assetName);
        }
        List<Oep5> oep5s = oep5Mapper.select(oep5Temp);
        oep5s.forEach(item -> stringBuilder.append(item.getContractHash()).append(","));
        String contractAddrsStr = stringBuilder.toString();
        if (Helper.isEmptyOrNull(contractAddrsStr)) {
            return balanceList;
        }

        QueryBatchBalanceDto queryBatchBalanceDto = QueryBatchBalanceDto.builder()
                .action(BALANCESERVICE_ACTION_GETMULTIBALANCE)
                .version(BALANCESERVICE_VERSION)
                .base58Addrs(address)
                .contractAddrs(contractAddrsStr.substring(0, contractAddrsStr.length() - 1))
                .build();

        String responseStr = commonService.httpPostRequest(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL,
                JacksonUtil.beanToJSonStr(queryBatchBalanceDto), null);
        if (Helper.isNotEmptyOrNull(responseStr)) {
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            JSONArray oepBalanceArray = ((JSONObject) jsonObject.getJSONArray("Result").get(0)).getJSONArray("OepBalance");

            Map<String, String> map = new HashMap<>();
            oepBalanceArray.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                map.put(obj.getString("contract_address"), obj.getString("balance"));
            });

            oep5s.forEach(item -> {
                        String contractHash = item.getContractHash();
                        BigDecimal balance = new BigDecimal(map.get(contractHash));
                        if (balance.compareTo(ConstantParam.ZERO) != 0) {
                            //ONTO返回name
                            if (ConstantParam.CHANNEL_ONTO.equals(channel)) {
                                BalanceDto balanceDto = BalanceDto.builder()
                                        .assetName(item.getName())
                                        .assetType(ConstantParam.ASSET_TYPE_OEP5)
                                        .balance(balance)
                                        .build();
                                balanceList.add(balanceDto);
                            } else {
                                //其他渠道返回symbol
                                //TODO 后续统一
                                BalanceDto balanceDto = BalanceDto.builder()
                                        .assetName(item.getSymbol())
                                        .assetType(ConstantParam.ASSET_TYPE_OEP5)
                                        .balance(balance)
                                        .build();
                                balanceList.add(balanceDto);
                            }
                        }
                    }
            );
        }
        return balanceList;
    }

    /**
     * get oep8 token
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep8Balance(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        //query audit passed oep8 token
        if (Helper.isNotEmptyOrNull(assetName)) {
            assetName = assetName + "%";
        }
        List<Map<String, String>> oep8s = oep8Mapper.selectAuditPassedOep8(assetName);
        oep8s.forEach(item -> {
            String tokenIdStr = item.get("tokenId");
            String str = tokenIdStr.replace(",", "&");
            stringBuilder.append(item.get("contractHash")).append(":").append(str).append(",");
        });
        String contractAddrsStr = stringBuilder.toString();
        if (Helper.isEmptyOrNull(contractAddrsStr)) {
            return balanceList;
        }

        QueryBatchBalanceDto queryBatchBalanceDto = QueryBatchBalanceDto.builder()
                .action(BALANCESERVICE_ACTION_GETMULTIBALANCE)
                .version(BALANCESERVICE_VERSION)
                .base58Addrs(address)
                .contractAddrs(contractAddrsStr.substring(0, contractAddrsStr.length() - 1))
                .build();

        String responseStr = commonService.httpPostRequest(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL,
                JacksonUtil.beanToJSonStr(queryBatchBalanceDto), null);
        if (Helper.isNotEmptyOrNull(responseStr)) {
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            JSONArray oepBalanceArray = ((JSONObject) jsonObject.getJSONArray("Result").get(0)).getJSONArray("OepBalance");

            Map<String, String> map = new HashMap<>();
            oepBalanceArray.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                map.put(obj.getString("contract_address"), obj.getString("balance"));
            });

            oep8s.forEach(item -> {
                        String contractHash = item.get("contractHash");
                        String symbolStr = item.get("symbol");
                        String[] symbolArray = symbolStr.split(",");
                        String balanceStr = map.get(contractHash);
                        String[] balanceArray = balanceStr.split(",");
                        for (int i = 0; i < symbolArray.length; i++) {
                            BalanceDto balanceDto = BalanceDto.builder()
                                    .assetName(symbolArray[i])
                                    .assetType(ConstantParam.ASSET_TYPE_OEP8)
                                    .balance(new BigDecimal((String) balanceArray[i]))
                                    .build();
                            balanceList.add(balanceDto);
                        }
                    }
            );
        }

        return balanceList;
    }


    /**
     * get oep8 token
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep8Balance4Onto(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (Helper.isNotEmptyOrNull(assetName)) {
            assetName = assetName + "%";
        }
        //query audit passed oep8 token
        List<Map<String, String>> oep8s = oep8Mapper.selectAuditPassedOep8(assetName);
        oep8s.forEach(item -> {
            String tokenIdStr = item.get("tokenId");
            String str = tokenIdStr.replace(",", "&");
            stringBuilder.append(item.get("contractHash")).append(":").append(str).append(",");
        });
        String contractAddrsStr = stringBuilder.toString();
        if (Helper.isEmptyOrNull(contractAddrsStr)) {
            return balanceList;
        }

        QueryBatchBalanceDto queryBatchBalanceDto = QueryBatchBalanceDto.builder()
                .action(BALANCESERVICE_ACTION_GETMULTIBALANCE)
                .version(BALANCESERVICE_VERSION)
                .base58Addrs(address)
                .contractAddrs(contractAddrsStr.substring(0, contractAddrsStr.length() - 1))
                .build();

        String responseStr = commonService.httpPostRequest(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL,
                JacksonUtil.beanToJSonStr(queryBatchBalanceDto), null);
        if (Helper.isNotEmptyOrNull(responseStr)) {
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            JSONArray oepBalanceArray = ((JSONObject) jsonObject.getJSONArray("Result").get(0)).getJSONArray("OepBalance");

            Map<String, String> map = new HashMap<>();
            oepBalanceArray.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                map.put(obj.getString("contract_address"), obj.getString("balance"));
            });

            oep8s.forEach(item -> {
                        String contractHash = item.get("contractHash");
                        String symbolStr = item.get("symbol");
                        String[] symbolArray = symbolStr.split(",");
                        String balanceStr = map.get(contractHash);
                        String[] balanceArray = balanceStr.split(",");
                        int total = 0;
                        for (int i = 0; i < symbolArray.length; i++) {
                            BalanceDto balanceDto = BalanceDto.builder()
                                    .assetName(symbolArray[i])
                                    .assetType(ConstantParam.ASSET_TYPE_OEP8)
                                    .balance(new BigDecimal((String) balanceArray[i]))
                                    .build();
                            balanceList.add(balanceDto);
                            total += Integer.parseInt(balanceArray[i]);
                        }
                        BalanceDto balanceDto = BalanceDto.builder()
                                .assetName("totalpumpkin")
                                .assetType(ConstantParam.ASSET_TYPE_OEP8)
                                .balance(new BigDecimal(total))
                                .build();
                        balanceList.add(balanceDto);
                    }
            );
        }

        return balanceList;
    }


    /**
     * get oep8 token
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep8Balance4OntoOld(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();
        //审核过的OEP8余额
        if (Helper.isNotEmptyOrNull(assetName)) {
            assetName = assetName + "%";
        }
        List<Map<String, String>> oep8s = oep8Mapper.selectAuditPassedOep8(assetName);
        for (Map<String, String> map :
                oep8s) {
            String contractHash = map.get("contractHash");
            String symbol = map.get("symbol");
            String[] symbolArray = symbol.split(",");
            int total = 0;

            JSONArray balanceArray = sdk.getOpe8AssetBalance(address, contractHash);
            for (int i = 0; i < symbolArray.length; i++) {
                BalanceDto balanceDto = BalanceDto.builder()
                        .assetName(symbolArray[i])
                        .assetType(ConstantParam.ASSET_TYPE_OEP8)
                        .balance(new BigDecimal((String) balanceArray.get(i)))
                        .build();
                balanceList.add(balanceDto);
                total = total + Integer.valueOf((String) balanceArray.get(i));
            }
            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName("totalpumpkin")
                    .assetType(ConstantParam.ASSET_TYPE_OEP8)
                    .balance(new BigDecimal(total))
                    .build();
            balanceList.add(balanceDto);
        }
        return balanceList;
    }

    /**
     * 计算待提取的ong
     *
     * @param address
     * @param ont
     * @return
     */
    private String calculateWaitingBoundOng(String address, String ont) {

        Integer latestOntTransferTxTime = null;
        //mysql 4.0.14+ bug
        try {
            latestOntTransferTxTime = txDetailMapper.selectLatestOntTransferTxTime(address);
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }

        if (Helper.isEmptyOrNull(latestOntTransferTxTime)) {
            return "0";
        }
        long now = System.currentTimeMillis() / 1000L;
        log.info("calculateWaitingBoundOng latestOntTransferTxTime:{},now:{}", latestOntTransferTxTime, now);
        BigDecimal totalOng = new BigDecimal("0");
        //before 20190630000000 UTC
        if (latestOntTransferTxTime < TIMESTAMP_20190630000000_UTC) {
            BigDecimal ong01 = new BigDecimal(TIMESTAMP_20190630000000_UTC).subtract(new BigDecimal(latestOntTransferTxTime)).multiply(new BigDecimal(5));
            BigDecimal ong02 = new BigDecimal(now).subtract(new BigDecimal(TIMESTAMP_20190630000000_UTC)).multiply(paramsConfig.ONG_SECOND_GENERATE);
            totalOng = ong01.add(ong02);
        } else {
            totalOng = new BigDecimal(now).subtract(new BigDecimal(latestOntTransferTxTime)).multiply(paramsConfig.ONG_SECOND_GENERATE);
        }
        BigDecimal ong = totalOng.multiply(new BigDecimal(ont)).divide(ConstantParam.ONT_TOTAL);

        return ong.toPlainString();
    }


    @Override
    public ResponseBean queryTransferTxsByPage(String address, String assetName, Integer pageNumber, Integer pageSize) {

        List<TransferTxDto> returnList = new ArrayList<>();
        //查询前（pageNumber * pageSize * 3）条记录
        List<TransferTxDto> transferTxDtos = txDetailMapper.selectTransferTxsByPage(address, assetName, 0, pageNumber * pageSize * 3);
        //合并和格式化转账交易记录
        List<TransferTxDto> formattedTransferTxDtos = formatTransferTxDtos(transferTxDtos);

        if (formattedTransferTxDtos.size() > 0 && formattedTransferTxDtos.size() < pageSize * pageNumber) {
            //合并和格式化转账交易记录数 < （pageNumber * pageSize），根据总记录数再重新查询所有的记录
            int transferTxTotal = txDetailMapper.selectTransferTxCountByAddr(address);
            if (transferTxTotal > pageNumber * pageSize * 3) {
                //针对一个地址有T笔1对N转账or一笔1对M转账的特殊处理(T*N>pageNumber*pageSize*3 or M>pageNumber*pageSize*3)
                List<TransferTxDto> transferTxDtos2 = txDetailMapper.selectTransferTxsByPage(address, assetName, 0, transferTxTotal);
                List<TransferTxDto> formattedTransferTxDtos2 = formatTransferTxDtos(transferTxDtos2);

                returnList = getTransferTxDtosByPage(pageNumber, pageSize, formattedTransferTxDtos2);
            } else {
                //总的交易数 < （pageNumber * pageSize * 3），直接根据请求条数进行分页
                returnList = getTransferTxDtosByPage(pageNumber, pageSize, formattedTransferTxDtos);
            }
        } else {
            //格式化后的txlist条数满足分页条件，直接根据请求条数参数进行分页
            //根据分页确认start，end=start+pageSize
            returnList = getTransferTxDtosByPage(pageNumber, pageSize, formattedTransferTxDtos);
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), returnList);
    }


    /**
     * 获取分页后的转账交易列表
     *
     * @param pageNumber
     * @param pageSize
     * @param formattedTransferTxDtos
     * @return
     */
    private List<TransferTxDto> getTransferTxDtosByPage(int pageNumber, int pageSize, List<TransferTxDto> formattedTransferTxDtos) {

        int start = (pageNumber - 1) * pageSize <= 0 ? 0 : (pageNumber - 1) * pageSize;
        int end = (pageSize + start) > formattedTransferTxDtos.size() ? formattedTransferTxDtos.size() : (pageSize + start);

        return formattedTransferTxDtos.subList(start, end);
    }

    @Override
    public ResponseBean queryTransferTxsByTime(String address, String assetName, Long beginTime, Long endTime) {

        List<TransferTxDto> transferTxDtos = txDetailMapper.selectTransferTxsByTime(address, assetName, beginTime, endTime);

        List<TransferTxDto> formattedTransferTxDtos = formatTransferTxDtos(transferTxDtos);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), formattedTransferTxDtos);
    }

    @Override
    public ResponseBean queryTransferTxsByTime4Onto(String address, String assetName, Long beginTime, Long endTime) {

        List<TransferTxDto> transferTxDtos = new ArrayList<>();
        //云斗龙资产使用like查询, for ONTO
        if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
            assetName = assetName + "%";
            transferTxDtos = txDetailMapper.selectDragonTransferTxsByTime4Onto(address, assetName, beginTime, endTime);
        } else {
            transferTxDtos = txDetailMapper.selectTransferTxsByTime4Onto(address, assetName, beginTime, endTime);
        }
        List<TransferTxDto> formattedTransferTxDtos = formatTransferTxDtos(transferTxDtos);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), formattedTransferTxDtos);
    }

    @Override
    public ResponseBean queryTransferTxsByTimeAndPage4Onto(String address, String assetName, Long endTime, Integer pageSize) {

        List<TransferTxDto> transferTxDtos = new ArrayList<>();
        //云斗龙资产使用like查询，for ONTO，只查询转账不包括ong手续费记录
        if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
            assetName = assetName + "%";
            transferTxDtos = txDetailMapper.selectDragonTransferTxsByTimeAndPage4Onto(address, assetName, endTime, pageSize);
        } else {
            transferTxDtos = txDetailMapper.selectTransferTxsByTimeAndPage4Onto(address, assetName, endTime, pageSize);
        }

        List<TransferTxDto> formattedTransferTxDtos = formatTransferTxDtos(transferTxDtos);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), formattedTransferTxDtos);

    }

    /**
     * 格式化转账交易列表
     *
     * @return
     */
    private List<TransferTxDto> formatTransferTxDtos(List<TransferTxDto> transferTxDtos) {

        List<TransferTxDto> formattedTransferTxs = new ArrayList<>();

        String previousTxHash = "";
        int previousTxIndex = 0;
        for (int i = 0; i < transferTxDtos.size(); i++) {
            TransferTxDto transferTxDto = transferTxDtos.get(i);
            //ONG精度格式化
            String assetName = transferTxDto.getAssetName();
            BigDecimal amount = transferTxDto.getAmount();
            if (ConstantParam.ONG.equals(assetName)) {
                amount = amount.divide(ConstantParam.ONG_TOTAL);
            }

            String txHash = transferTxDto.getTxHash();
            log.info("txHash:{}", txHash);

            if (txHash.equals(previousTxHash)) {
                //自己给自己转账，sql会查询出两条记录.需要判断tx_index是否一样
                if (previousTxIndex != transferTxDto.getTxIndex()) {

                    TransferTxDetailDto transferTxDetailDto = TransferTxDetailDto.builder()
                            .amount(amount)
                            .fromAddress(transferTxDto.getFromAddress())
                            .toAddress(transferTxDto.getToAddress())
                            .assetName(transferTxDto.getAssetName())
                            .build();

                    List<TransferTxDetailDto> transferTxnList = (List<TransferTxDetailDto>) (formattedTransferTxs.get(formattedTransferTxs.size() - 1)).getTransfers();
                    transferTxnList.add(transferTxDetailDto);
                }
                previousTxIndex = transferTxDto.getTxIndex();
            } else {

                previousTxIndex = transferTxDto.getTxIndex();

                TransferTxDetailDto transferTxDetailDto = TransferTxDetailDto.builder()
                        .amount(amount)
                        .fromAddress(transferTxDto.getFromAddress())
                        .toAddress(transferTxDto.getToAddress())
                        .assetName(transferTxDto.getAssetName())
                        .build();
                List<TransferTxDetailDto> transferTxnList = new ArrayList<>();
                transferTxnList.add(transferTxDetailDto);

                transferTxDto.setTransfers(transferTxnList);
                transferTxDto.setFromAddress(null);
                transferTxDto.setToAddress(null);
                transferTxDto.setAmount(null);
                transferTxDto.setAssetName(null);
                transferTxDto.setTxIndex(null);

                formattedTransferTxs.add(transferTxDto);
            }

            previousTxHash = txHash;
        }

        return formattedTransferTxs;
    }


}
