package com.github.ontio.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.*;
import com.github.ontio.model.dto.BalanceDto;
import com.github.ontio.model.dto.QueryBatchBalanceDto;
import com.github.ontio.model.dto.TransferTxDetailDto;
import com.github.ontio.model.dto.TransferTxDto;
import com.github.ontio.model.dto.aggregation.AddressAggregationDto;
import com.github.ontio.model.dto.aggregation.AddressBalanceAggregationsDto;
import com.github.ontio.model.dto.aggregation.ExtremeBalanceDto;
import com.github.ontio.model.dto.ranking.AddressRankingDto;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.service.IAddressService;
import com.github.ontio.util.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/28
 */
@Slf4j
@Service("AddressService")
public class AddressServiceImpl implements IAddressService {

    private static final String ADDRESS_TYPE_FROM = "fromAddress";

    private static final String ADDRESS_TYPE_TO = "toAddress";

    private final Oep4Mapper oep4Mapper;
    private final Oep8Mapper oep8Mapper;
    private final Oep5Mapper oep5Mapper;
    private final Orc20Mapper orc20Mapper;
    private final Orc721Mapper orc721Mapper;
    private final Orc1155Mapper orc1155Mapper;
    private final TxDetailMapper txDetailMapper;
    private final ParamsConfig paramsConfig;
    private final CommonService commonService;
    private final AddressDailyAggregationMapper addressDailyAggregationMapper;
    private final RankingMapper rankingMapper;


    @Autowired
    public AddressServiceImpl(Oep4Mapper oep4Mapper, Oep8Mapper oep8Mapper, Oep5Mapper oep5Mapper, Orc20Mapper orc20Mapper, Orc721Mapper orc721Mapper, Orc1155Mapper orc1155Mapper,
                              TxDetailMapper txDetailMapper, ParamsConfig paramsConfig, CommonService commonService, AddressDailyAggregationMapper addressDailyAggregationMapper,
                              RankingMapper rankingMapper) {
        this.oep4Mapper = oep4Mapper;
        this.oep8Mapper = oep8Mapper;
        this.oep5Mapper = oep5Mapper;
        this.orc20Mapper = orc20Mapper;
        this.orc721Mapper = orc721Mapper;
        this.orc1155Mapper = orc1155Mapper;
        this.txDetailMapper = txDetailMapper;
        this.paramsConfig = paramsConfig;
        this.commonService = commonService;
        this.addressDailyAggregationMapper = addressDailyAggregationMapper;
        this.rankingMapper = rankingMapper;
    }

    private OntologySDKService sdk;


    @Autowired
    private Web3jSdkUtil web3jSdkUtil;


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
                case ConstantParam.ASSET_TYPE_NATIVE_OEP4:
                    balanceList = getNativeAndOep4Balance(address);
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
                case ConstantParam.ASSET_TYPE_NATIVE_OEP4:
                    balanceList = getNativeAndOep4BalanceOld(address);
                    break;
                default:
                    break;
            }
        }

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), balanceList);
    }


    @Override
    public ResponseBean queryEVMAddressBalance(String address, String tokenType) {
        List<BalanceDto> balanceList = new ArrayList<>();
        switch (tokenType.toLowerCase()) {
            case ConstantParam.ASSET_TYPE_ORC20:
                balanceList = getOrc20BalanceOld(address);
                break;
            case ConstantParam.ASSET_TYPE_ORC721:
                balanceList = getOrc721Balance(address);
                break;
            case ConstantParam.ASSET_TYPE_ORC1155:
//                balanceList = getOrc1155Balance(address);
                break;
            case ConstantParam.ASSET_TYPE_NATIVE:
                balanceList = getEvmOngBalance(address);
                break;
            case ConstantParam.ASSET_TYPE_ALL:
//                balanceList = getAllEvmAssetBalance(address);
                break;
            default:
                break;
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
                    .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)).divide(ConstantParam.NEW_ONT_DECIMAL))
                    .contractHash(ConstantParam.CONTRACTHASH_ONT)
                    .build();
            balanceList.add(balanceDto4);

        } else if (ConstantParam.ONG.equals(assetName)) {

            initSDK();
            Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
            //ONG
            BalanceDto balanceDto1 = BalanceDto.builder()
                    .assetName(ConstantParam.ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal((String) balanceMap.get(ConstantParam.ONG)).divide(ConstantParam.NEW_ONG_DECIMAL)))
                    .contractHash(ConstantParam.CONTRACTHASH_ONG)
                    .build();
            balanceList.add(balanceDto1);

            //waiting bound ONG
            String waitBoundOng = calculateWaitingBoundOng(address, new BigDecimal((String) balanceMap.get(ConstantParam.ONT)).divide(ConstantParam.NEW_ONT_DECIMAL));
            BalanceDto balanceDto2 = BalanceDto.builder()
                    .assetName(ConstantParam.WAITBOUND_ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal(waitBoundOng)))
                    .contractHash(ConstantParam.CONTRACTHASH_ONG)
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
                    .contractHash(ConstantParam.CONTRACTHASH_ONG)
                    .build();
            balanceList.add(balanceDto3);
        } else {
            Oep4 oep4 = new Oep4();
            oep4.setSymbol(assetName);
            oep4.setAuditFlag(ConstantParam.AUDIT_PASSED);
            oep4 = oep4Mapper.selectOne(oep4);
            if (Helper.isNotEmptyAndNull(oep4)) {
                balanceList = getOep4Balance2(address, oep4);
            } else {
                Oep5 oep5 = new Oep5();
                oep5.setSymbol(assetName);
                oep5 = oep5Mapper.selectOne(oep5);
                if (Helper.isNotEmptyAndNull(oep5)) {
                    balanceList = getOep5Balance2(address, oep5);
                } else {
                    //return all pumpkins and total pumpkin number
                    if (assetName.equals(ConstantParam.OEP8_PUMPKIN_PREFIX)) {
                        balanceList = getOep8Balance4OntoOld(address, ConstantParam.OEP8_PUMPKIN_PREFIX);
                    } else {
                        Oep8 oep8 = new Oep8();
                        oep8.setSymbol(assetName);
                        List<Oep8> oep8s = oep8Mapper.select(oep8);
                        if (oep8s.size() > 0) {
                            balanceList = getOep8Balance2(address, assetName);
                        }
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
                    .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)).divide(ConstantParam.NEW_ONT_DECIMAL))
                    .build();
            balanceList.add(balanceDto);

        } else if (ConstantParam.ONG.equals(assetName)) {

            initSDK();
            Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
            //ONG
            BalanceDto balanceDto1 = BalanceDto.builder()
                    .assetName(ConstantParam.ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal((String) balanceMap.get(ConstantParam.ONG)).divide(ConstantParam.NEW_ONG_DECIMAL)))
                    .build();
            balanceList.add(balanceDto1);

            //waiting bound ONG
            String waitBoundOng = calculateWaitingBoundOng(address, new BigDecimal((String) balanceMap.get(ConstantParam.ONT)).divide(ConstantParam.NEW_ONT_DECIMAL));
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


    @Override
    public ResponseBean queryAddressBalanceByContractHash(String address, String contractHash) {
        List<BalanceDto> balanceList = new ArrayList<>();

        if (ConstantParam.CONTRACTHASH_ONT.equals(contractHash)) {

            initSDK();
            Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
            //ONT
            BalanceDto balanceDto4 = BalanceDto.builder()
                    .assetName(ConstantParam.ONT)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)).divide(ConstantParam.NEW_ONT_DECIMAL))
                    .contractHash(ConstantParam.CONTRACTHASH_ONT)
                    .build();
            balanceList.add(balanceDto4);

        } else if (ConstantParam.CONTRACTHASH_ONG.equals(contractHash)) {

            initSDK();
            Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
            //ONG
            BalanceDto balanceDto1 = BalanceDto.builder()
                    .assetName(ConstantParam.ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal((String) balanceMap.get(ConstantParam.ONG)).divide(ConstantParam.NEW_ONG_DECIMAL)))
                    .contractHash(ConstantParam.CONTRACTHASH_ONG)
                    .build();
            balanceList.add(balanceDto1);

            //waiting bound ONG
            String waitBoundOng = calculateWaitingBoundOng(address, new BigDecimal((String) balanceMap.get(ConstantParam.ONT)).divide(ConstantParam.NEW_ONT_DECIMAL));
            BalanceDto balanceDto2 = BalanceDto.builder()
                    .assetName(ConstantParam.WAITBOUND_ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal(waitBoundOng)))
                    .contractHash(ConstantParam.CONTRACTHASH_ONG)
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
                    .contractHash(ConstantParam.CONTRACTHASH_ONG)
                    .build();
            balanceList.add(balanceDto3);
        } else {
            Oep4 oep4 = new Oep4();
            oep4.setContractHash(contractHash);
            oep4.setAuditFlag(ConstantParam.AUDIT_PASSED);
            oep4 = oep4Mapper.selectOne(oep4);
            if (Helper.isNotEmptyAndNull(oep4)) {
                balanceList = getOep4Balance2(address, oep4);
            } else {
                Oep5 oep5 = new Oep5();
                oep5.setContractHash(contractHash);
                oep5 = oep5Mapper.selectOne(oep5);
                if (Helper.isNotEmptyAndNull(oep5)) {
                    balanceList = getOep5Balance2(address, oep5);
                } else {
                    //return all pumpkins and total pumpkin number
                    if (paramsConfig.OEP8_PUMPKIN_CONTRACTHASH.equals(contractHash)) {
                        balanceList = getOep8Balance4OntoOld(address, ConstantParam.OEP8_PUMPKIN_PREFIX);
                    } else {
                        Oep8 oep8 = new Oep8();
                        oep8.setContractHash(contractHash);
                        List<Oep8> oep8s = oep8Mapper.select(oep8);
                        if (oep8s.size() > 0) {
                            balanceList = getOep8Balance2(address, oep8s.get(0).getSymbol());
                        }
                    }
                }
            }
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), balanceList);
    }

    /**
     * query native balance
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
                .balance((new BigDecimal((String) balanceMap.get(ConstantParam.ONG)).divide(ConstantParam.NEW_ONG_DECIMAL)))
                .contractHash(ConstantParam.CONTRACTHASH_ONG)
                .build();
        balanceList.add(balanceDto1);

        //waiting bound ONG
        String waitBoundOng = calculateWaitingBoundOng(address, (new BigDecimal((String) balanceMap.get(ConstantParam.ONT)).divide(ConstantParam.NEW_ONT_DECIMAL)));
        BalanceDto balanceDto2 = BalanceDto.builder()
                .assetName(ConstantParam.WAITBOUND_ONG)
                .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                .balance((new BigDecimal(waitBoundOng)))
                .contractHash(ConstantParam.CONTRACTHASH_ONG)
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
                .contractHash(ConstantParam.CONTRACTHASH_ONG)
                .build();
        balanceList.add(balanceDto3);

        //ONT
        BalanceDto balanceDto4 = BalanceDto.builder()
                .assetName(ConstantParam.ONT)
                .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)).divide(ConstantParam.NEW_ONT_DECIMAL))
                .contractHash(ConstantParam.CONTRACTHASH_ONT)
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
     * get native and oep4 asset balance
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getNativeAndOep4Balance(String address) {

        List<BalanceDto> balanceDtos = new ArrayList<>();
        balanceDtos.addAll(getNativeBalance(address));
        balanceDtos.addAll(getOep4Balance(address, ""));
        return balanceDtos;
    }

    private List<BalanceDto> getNativeAndOep4BalanceOld(String address) {

        List<BalanceDto> balanceDtos = new ArrayList<>();
        balanceDtos.addAll(getNativeBalance(address));
        balanceDtos.addAll(getOep4BalanceOld(address, ""));
        return balanceDtos;
    }


    /**
     * query OEP4 balance
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep4BalanceOld(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();

        Oep4 oep4Temp = new Oep4();
        oep4Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);
        if (Helper.isNotEmptyAndNull(assetName)) {
            oep4Temp.setSymbol(assetName);
        }
        // 去oep4 表中查询所有的 oep4 合约hash
        List<Oep4> oep4s = oep4Mapper.select(oep4Temp);
        for (Oep4 oep4 :
                oep4s) {
            BigDecimal balance = new BigDecimal("0");
            String contractHash = oep4.getContractHash();
            String vmCategory = oep4.getVmCategory();
            if (ConstantParam.VM_CATEGORY_NEOVM.equals(vmCategory)) {
                balance = new BigDecimal(sdk.getNeovmOep4AssetBalance(address, contractHash)).divide(new BigDecimal(Math.pow(10,
                        oep4.getDecimals())));
            } else if (ConstantParam.VM_CATEGORY_WASMVM.equals(vmCategory)) {
                balance = new BigDecimal(sdk.getWasmvmOep4AssetBalance(address, contractHash)).divide(new BigDecimal(Math.pow(10,
                        oep4.getDecimals())));
            }
            if (balance.compareTo(ConstantParam.ZERO) == 0) {
                continue;
            }
            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName(oep4.getSymbol())
                    .assetType(ConstantParam.ASSET_TYPE_OEP4)
                    .balance(balance)
                    .contractHash(oep4.getContractHash())
                    .build();
            balanceList.add(balanceDto);
        }
        return balanceList;
    }


    /**
     * query OEP4 balance
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep4Balance2(String address, Oep4 oep4) {

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();
        String contractHash = oep4.getContractHash();
        String vmCategory = oep4.getVmCategory();
        BigDecimal balance = new BigDecimal("0");
        if (ConstantParam.VM_CATEGORY_NEOVM.equals(vmCategory)) {
            balance = new BigDecimal(sdk.getNeovmOep4AssetBalance(address, contractHash)).divide(new BigDecimal(Math.pow(10,
                    oep4.getDecimals())));
        } else if (ConstantParam.VM_CATEGORY_WASMVM.equals(vmCategory)) {
            balance = new BigDecimal(sdk.getWasmvmOep4AssetBalance(address, contractHash)).divide(new BigDecimal(Math.pow(10,
                    oep4.getDecimals())));
        }
        BalanceDto balanceDto = BalanceDto.builder()
                .assetName(oep4.getSymbol())
                .assetType(ConstantParam.ASSET_TYPE_OEP4)
                .balance(balance)
                .contractHash(oep4.getContractHash())
                .build();
        balanceList.add(balanceDto);
        return balanceList;
    }


    private List<BalanceDto> getOrc20BalanceOld(String address) {
        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();

        Orc20 orc20Temp = new Orc20();
        orc20Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);

        // 查到库中所有 通过执行的合约
        List<Orc20> orc20s = orc20Mapper.select(orc20Temp);
        for (Orc20 orc20 : orc20s) {
            BigDecimal balance;
            String contractHash = orc20.getContractHash();
            balance = web3jSdkUtil.queryOrc20Balance(address, orc20.getContractHash()).divide(BigDecimal.valueOf(Math.pow(10, orc20.getDecimals())));
            if (balance.compareTo(ConstantParam.ZERO) == 0) {
                continue;
            }
            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName(orc20.getSymbol())
                    .assetType(ConstantParam.ASSET_TYPE_ORC20)
                    .balance(balance)
                    .contractHash(contractHash)
                    .build();

            balanceList.add(balanceDto);
        }
        return balanceList;
    }

    /**
     * 获取ORC721余额
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOrc721Balance(String address) {

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();
        //审核过的ORC721余额
        Orc721 orc721Temp = new Orc721();
        orc721Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);
        List<Orc721> orc721s = orc721Mapper.select(orc721Temp);
        for (Orc721 orc721 : orc721s) {
            String contractHash = orc721.getContractHash();
            String vmCategory = orc721.getVmCategory();
            String orc721AssetName = orc721.getSymbol();
            BigDecimal balance = web3jSdkUtil.queryOrc721Balance(address, contractHash);

            if (ConstantParam.ZERO.compareTo(balance) == 0) {
                continue;
            }
            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName(orc721AssetName)
                    .assetType(ConstantParam.ASSET_TYPE_ORC721)
                    .balance(balance)
                    .contractHash(contractHash)
                    .build();
            balanceList.add(balanceDto);
        }
        return balanceList;
    }

    private List<BalanceDto> getEvmOngBalance(String address) {
        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();
        String ongBalance = sdk.getOngBalanceByEvmAddress(address);
        BalanceDto balanceDto = BalanceDto.builder()
                .assetName(ConstantParam.ONG)
                .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                .balance((new BigDecimal(ongBalance).divide(ConstantParam.NEW_ONG_DECIMAL)))
                .contractHash(ConstantParam.CONTRACTHASH_ONG)
                .build();
        balanceList.add(balanceDto);
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
        if (Helper.isNotEmptyAndNull(assetName)) {
            oep5Temp.setSymbol(assetName);
        }
        List<Oep5> oep5s = oep5Mapper.select(oep5Temp);
        for (Oep5 oep5 : oep5s) {
            String contractHash = oep5.getContractHash();
            String vmCategory = oep5.getVmCategory();
            String oep5AssetName;
            if (ConstantParam.CHANNEL_ONTO.equals(channel)) {
                //ONTO return name
                oep5AssetName = oep5.getName();
            } else {
                //other return symbol
                oep5AssetName = oep5.getSymbol();
            }
            BigDecimal balance;
            if (ConstantParam.VM_CATEGORY_NEOVM.equalsIgnoreCase(vmCategory)) {
                balance = new BigDecimal(sdk.getOep5AssetBalance(address, contractHash));
            } else {
                balance = new BigDecimal(sdk.getWasmVmOep5Balance(address, contractHash));
            }
            if (balance.compareTo(ConstantParam.ZERO) == 0) {
                continue;
            }
            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName(oep5AssetName)
                    .assetType(ConstantParam.ASSET_TYPE_OEP5)
                    .balance(balance)
                    .contractHash(oep5.getContractHash())
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
    private List<BalanceDto> getOep5Balance2(String address, Oep5 oep5) {

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();

        String contractHash = oep5.getContractHash();
        BigDecimal balance = new BigDecimal(sdk.getOep5AssetBalance(address, contractHash));
        BalanceDto balanceDto = BalanceDto.builder()
                .assetName(oep5.getSymbol())
                .assetType(ConstantParam.ASSET_TYPE_OEP5)
                .balance(balance)
                .contractHash(oep5.getContractHash())
                .build();
        balanceList.add(balanceDto);
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
        if (Helper.isNotEmptyAndNull(assetName)) {
            assetName = assetName + "%";
        }
        List<Map<String, String>> oep8s = oep8Mapper.selectAuditPassedOep8(assetName);
        for (Map<String, String> map : oep8s) {
            String contractHash = map.get("contractHash");
            String symbol = map.get("symbol");
            String vmCategory = map.get("vmCategory");
            String[] symbolArray = symbol.split(",");
            if (ConstantParam.VM_CATEGORY_NEOVM.equalsIgnoreCase(vmCategory)) {
                JSONArray balanceArray = sdk.getOpe8AssetBalance(address, contractHash);
                for (int i = 0; i < symbolArray.length; i++) {
                    if (Integer.parseInt((String) balanceArray.get(i)) == 0) {
                        continue;
                    }
                    BalanceDto balanceDto = BalanceDto.builder()
                            .assetName(symbolArray[i])
                            .assetType(ConstantParam.ASSET_TYPE_OEP8)
                            .balance(new BigDecimal((String) balanceArray.get(i)))
                            .contractHash(contractHash)
                            .build();
                    balanceList.add(balanceDto);
                }
            } else {
                JSONArray balanceArray = sdk.getWasmVmOep8Ids(address, contractHash);
                if (balanceArray != null) {
                    for (int i = 0; i < balanceArray.size(); i++) {
                        BigInteger tokenId = balanceArray.getBigInteger(i);
                        BigInteger amount = sdk.getWasmVmOep8TokenIdBalance(address, contractHash, tokenId);
                        BalanceDto balanceDto = BalanceDto.builder()
                                .assetName(symbolArray[tokenId.intValue() - 1])
                                .assetType(ConstantParam.ASSET_TYPE_OEP8)
                                .balance(new BigDecimal(amount))
                                .contractHash(contractHash)
                                .build();
                        balanceList.add(balanceDto);
                    }

                }
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
        if (Helper.isNotEmptyAndNull(assetName)) {
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

        String responseStr =
                commonService.httpPostRequest(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL,
                        JacksonUtil.beanToJSonStr(queryBatchBalanceDto), null);
        if (Helper.isNotEmptyAndNull(responseStr)) {
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
                                    .contractHash(item.getContractHash())
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
        if (Helper.isNotEmptyAndNull(assetName)) {
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

        String responseStr =
                commonService.httpPostRequest(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL,
                        JacksonUtil.beanToJSonStr(queryBatchBalanceDto), null);
        if (Helper.isNotEmptyAndNull(responseStr)) {
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
                                        .contractHash(item.getContractHash())
                                        .build();
                                balanceList.add(balanceDto);
                            } else {
                                //其他渠道返回symbol
                                //TODO 后续统一
                                BalanceDto balanceDto = BalanceDto.builder()
                                        .assetName(item.getSymbol())
                                        .assetType(ConstantParam.ASSET_TYPE_OEP5)
                                        .balance(balance)
                                        .contractHash(item.getContractHash())
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
        if (Helper.isNotEmptyAndNull(assetName)) {
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

        String responseStr =
                commonService.httpPostRequest(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL,
                        JacksonUtil.beanToJSonStr(queryBatchBalanceDto), null);
        if (Helper.isNotEmptyAndNull(responseStr)) {
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
                                    .contractHash(item.get("contractHash"))
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
        if (Helper.isNotEmptyAndNull(assetName)) {
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

        String responseStr =
                commonService.httpPostRequest(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL,
                        JacksonUtil.beanToJSonStr(queryBatchBalanceDto), null);
        if (Helper.isNotEmptyAndNull(responseStr)) {
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
        if (Helper.isNotEmptyAndNull(assetName)) {
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
                        .contractHash(contractHash)
                        .build();
                balanceList.add(balanceDto);
                total = total + Integer.valueOf((String) balanceArray.get(i));
            }
            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName("totalpumpkin")
                    .assetType(ConstantParam.ASSET_TYPE_OEP8)
                    .balance(new BigDecimal(total))
                    .contractHash(contractHash)
                    .build();
            balanceList.add(balanceDto);
        }
        return balanceList;
    }


    /**
     * get oep8 token
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep8Balance2(String address, String inputSymbol) {

        List<BalanceDto> balanceList = new ArrayList<>();
        initSDK();
        int i = Integer.valueOf(inputSymbol.substring(inputSymbol.length() - 1, inputSymbol.length()));
        List<Map<String, String>> oep8s = oep8Mapper.selectAuditPassedOep8(inputSymbol);

        String contractHash = oep8s.get(0).get("contractHash");
        String symbol = oep8s.get(0).get("symbol");

        JSONArray balanceArray = sdk.getOpe8AssetBalance(address, contractHash);

        BalanceDto balanceDto = BalanceDto.builder()
                .assetName(symbol)
                .assetType(ConstantParam.ASSET_TYPE_OEP8)
                .balance(new BigDecimal((String) balanceArray.get(i - 1)))
                .contractHash(contractHash)
                .build();
        balanceList.add(balanceDto);
        return balanceList;
    }

    /**
     * 计算待提取的ong
     *
     * @param address
     * @param ont
     * @return
     */
    private String calculateWaitingBoundOng(String address, BigDecimal ont) {

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
        BigDecimal totalOng;
        //before 20190630000000 UTC
        if (latestOntTransferTxTime < TIMESTAMP_20190630000000_UTC) {
            BigDecimal ong01 =
                    new BigDecimal(TIMESTAMP_20190630000000_UTC).subtract(new BigDecimal(latestOntTransferTxTime)).multiply(new BigDecimal(5));
            BigDecimal ong02 =
                    new BigDecimal(1593388800).subtract(new BigDecimal(TIMESTAMP_20190630000000_UTC)).multiply(BigDecimal.valueOf(4));
            BigDecimal ong03 =
                    new BigDecimal(paramsConfig.TIMESTAMP_20200707000000_UTC).subtract(new BigDecimal(1593388800)).multiply(paramsConfig.ONG_SECOND_GENERATE);
            totalOng = ong01.add(ong02).add(ong03);
        } else if (latestOntTransferTxTime < paramsConfig.TIMESTAMP_20200707000000_UTC) {
            if (now < paramsConfig.TIMESTAMP_20200707000000_UTC) {
                if (latestOntTransferTxTime < 1593388800) {
                    BigDecimal totalOng1 =
                            new BigDecimal(1593388800).subtract(new BigDecimal(latestOntTransferTxTime)).multiply(new BigDecimal(4));
                    BigDecimal totalOng2 =
                            new BigDecimal(now).subtract(new BigDecimal(1593388800)).multiply(paramsConfig.ONG_SECOND_GENERATE);
                    totalOng = totalOng1.add(totalOng2);
                } else {
                    totalOng =
                            new BigDecimal(now).subtract(new BigDecimal(latestOntTransferTxTime)).multiply(paramsConfig.ONG_SECOND_GENERATE);
                }
            } else {
                if (latestOntTransferTxTime < 1593388800) {
                    BigDecimal totalOng1 =
                            new BigDecimal(1593388800).subtract(new BigDecimal(latestOntTransferTxTime)).multiply(new BigDecimal(4));
                    BigDecimal totalOng2 =
                            new BigDecimal(paramsConfig.TIMESTAMP_20200707000000_UTC).subtract(new BigDecimal(1593388800)).multiply(paramsConfig.ONG_SECOND_GENERATE);
                    totalOng = totalOng1.add(totalOng2);
                } else {
                    totalOng =
                            new BigDecimal(paramsConfig.TIMESTAMP_20200707000000_UTC).subtract(new BigDecimal(latestOntTransferTxTime)).multiply(paramsConfig.ONG_SECOND_GENERATE);
                }
            }

        } else {
            totalOng = BigDecimal.ZERO;
        }
        BigDecimal ong = totalOng.multiply(ont).divide(ConstantParam.ONT_TOTAL);

        return ong.toPlainString();
    }


    @Override
    public ResponseBean queryTransferTxsByPage(String address, String assetName, Integer pageNumber, Integer
            pageSize) {

        int start = pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1);
        List<TransferTxDto> transferTxDtos = txDetailMapper.selectTransferTxsByPage(address, assetName, start, pageSize);
        transferTxDtos = formatTransferTxDtos(transferTxDtos);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), transferTxDtos);
    }


    /**
     * 获取分页后的转账交易列表
     *
     * @param pageNumber
     * @param pageSize
     * @param formattedTransferTxDtos
     * @return
     */
    private List<TransferTxDto> getTransferTxDtosByPage(int pageNumber, int pageSize,
                                                        List<TransferTxDto> formattedTransferTxDtos) {

        int start = (pageNumber - 1) * pageSize > formattedTransferTxDtos.size() ? formattedTransferTxDtos.size() :
                (pageNumber - 1) * pageSize;
        int end = (pageSize + start) > formattedTransferTxDtos.size() ? formattedTransferTxDtos.size() : (pageSize + start);

        return formattedTransferTxDtos.subList(start, end);
    }

    @Override
    public ResponseBean queryTransferTxsByTime(String address, String assetName, Long beginTime, Long endTime) {

        List<TransferTxDto> transferTxDtos = txDetailMapper.selectTransferTxsByTime(address, assetName, beginTime, endTime);
        transferTxDtos = formatTransferTxDtos(transferTxDtos);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), transferTxDtos);
    }

    @Override
    public ResponseBean queryTransferTxsByTime4Onto(String address, String assetName, Long beginTime, Long
            endTime,
                                                    String addressType) {

        List<TransferTxDto> transferTxDtos = new ArrayList<>();

        if (Helper.isEmptyOrNull(addressType)) {
            //云斗龙资产使用like查询, for ONTO
            if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
                assetName = assetName + "%";
                transferTxDtos = txDetailMapper.selectDragonTransferTxsByTime4Onto(address, assetName, beginTime, endTime);
            } else {
                transferTxDtos = txDetailMapper.selectTransferTxsByTime4Onto(address, assetName, beginTime, endTime);
            }
        } else if (ADDRESS_TYPE_FROM.equals(addressType)) {
            //query transfer txs by fromaddress
            //dragon asset use 'like' query, for ONTO
            if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
                assetName = assetName + "%";
                transferTxDtos = txDetailMapper.selectDragonTransferTxsByTimeInFromAddr4Onto(address, assetName, beginTime,
                        endTime);
            } else {
                transferTxDtos = txDetailMapper.selectTransferTxsByTimeInFromAddr4Onto(address, assetName, beginTime, endTime);
            }
        } else if (ADDRESS_TYPE_TO.equals(addressType)) {
            //query transfer txs by toaddress
            //dragon asset use 'like' query, for ONTO
            if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
                assetName = assetName + "%";
                transferTxDtos = txDetailMapper.selectDragonTransferTxsByTimeInToAddr4Onto(address, assetName, beginTime, endTime);
            } else {
                transferTxDtos = txDetailMapper.selectTransferTxsByTimeInToAddr4Onto(address, assetName, beginTime, endTime);
            }
        }
        List<TransferTxDto> formattedTransferTxDtos = formatTransferTxDtos(transferTxDtos);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), formattedTransferTxDtos);
    }

    @Override
    public ResponseBean queryTransferTxsByTimeAndPage4Onto(String address, String assetName, Long
            endTime, Integer pageSize,
                                                           String addressType) {

        List<TransferTxDto> transferTxDtos = new ArrayList<>();

        if (Helper.isEmptyOrNull(addressType)) {
            //dragon asset use 'like' query, for ONTO
            if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
                assetName = assetName + "%";
                transferTxDtos = txDetailMapper.selectDragonTransferTxsByTimeAndPage4Onto(address, assetName, endTime, pageSize);
            } else {
                transferTxDtos = txDetailMapper.selectTransferTxsByTimeAndPage4Onto(address, assetName, endTime, pageSize);
            }
        } else if (ADDRESS_TYPE_FROM.equals(addressType)) {
            //query transfer txs by fromaddress
            //dragon asset use 'like' query, for ONTO
            if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
                assetName = assetName + "%";
                transferTxDtos = txDetailMapper.selectDragonTransferTxsByTimeAndPageInFromAddr4Onto(address, assetName, endTime,
                        pageSize);
            } else {
                transferTxDtos = txDetailMapper.selectTransferTxsByTimeAndPageInFromAddr4Onto(address, assetName, endTime,
                        pageSize);
            }
        } else if (ADDRESS_TYPE_TO.equals(addressType)) {
            //query transfer txs by toaddress
            //dragon asset use 'like' query, for ONTO
            if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
                assetName = assetName + "%";
                transferTxDtos = txDetailMapper.selectDragonTransferTxsByTimeAndPageInToAddr4Onto(address, assetName, endTime,
                        pageSize);
            } else {
                transferTxDtos = txDetailMapper.selectTransferTxsByTimeAndPageInToAddr4Onto(address, assetName, endTime, pageSize);
            }
        }
        List<TransferTxDto> formattedTransferTxDtos = formatTransferTxDtos(transferTxDtos);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), formattedTransferTxDtos);
    }

    private Cache<String, ExtremeBalanceDto> extremeBalances = Caffeine.newBuilder()
            .expireAfter(new Expiry<String, ExtremeBalanceDto>() {
                             @Override
                             public long expireAfterCreate(String key, ExtremeBalanceDto value, long currentTime) {
                                 DateTime now = DateTime.now(DateTimeZone.UTC);
                                 return (now.plusDays(1).withTime(0, 5, 0, 0).getMillis() - System.currentTimeMillis()) * 1000000;
                             }

                             @Override
                             public long expireAfterUpdate(String key, ExtremeBalanceDto value, long currentTime,
                                                           long currentDuration) {
                                 return currentDuration;
                             }

                             @Override
                             public long expireAfterRead(String key, ExtremeBalanceDto value, long currentTime,
                                                         long currentDuration) {
                                 return currentDuration;
                             }
                         }
            ).maximumSize(0).build(); // TODO disable cache temporarily


    @Override
    public ResponseBean queryDailyAggregation(String address, String token, Date from, Date to) throws
            SDKException {
        String tokenContractHash = paramsConfig.getContractHash(token);
        List<AddressAggregationDto> aggregations = addressDailyAggregationMapper.findAggregations(address, tokenContractHash, from, to);

        String ontAddr = "";
        String ethAddr = "";
        if (address.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            ethAddr = address;
            ontAddr = Helper.EthAddrToOntAddr(address);
        } else {
            ethAddr = Helper.ontAddrToEthAddr(address);
            ontAddr = address;
        }

 /*
        String finalOntAddress = ontAddr;
        String finalEthAddress = ethAddr;
        ExtremeBalanceDto max1 = extremeBalances.get(address + tokenContractHash + "max", key -> addressDailyAggregationMapper.findMaxBalance(finalOntAddress, tokenContractHash));
        ExtremeBalanceDto max2 = extremeBalances.get(address + tokenContractHash + "max", key -> addressDailyAggregationMapper.findMaxBalance(finalEthAddress, tokenContractHash));


        String finalOntAddr = ontAddr;
        String finalEthAddr = ethAddr;*/

        String finalAddress = address;
        ExtremeBalanceDto max = extremeBalances.get(address + tokenContractHash + "max", key -> addressDailyAggregationMapper.findMaxBalance(address, tokenContractHash));


        String finalAddress1 = address;
        ExtremeBalanceDto min = extremeBalances.get(address + token + "min", key -> addressDailyAggregationMapper.findMinBalance(finalAddress1, tokenContractHash));

//        ExtremeBalanceDto min = extremeBalances.get(address + token + "min", key -> addressDailyAggregationMapper.findMinBalance(finalAddress1, tokenContractHash));


        AddressBalanceAggregationsDto result = new AddressBalanceAggregationsDto(max, min, aggregations);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    @Override
    public ResponseBean queryDailyAggregationOfTokenType(String address, String tokenType, Date from, Date to) {
        String tokenContractHash = paramsConfig.getContractHash(tokenType);
        List<AddressAggregationDto> aggregations = addressDailyAggregationMapper.findAggregationsForToken(address,
                tokenContractHash, from, to);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), aggregations);
    }

    @Override
    public ResponseBean queryRankings(List<Short> rankingIds, short duration) {
        List<AddressRankingDto> rankings = rankingMapper.findAddressRankings(rankingIds, duration);
        Map<Short, List<AddressRankingDto>> rankingMap =
                rankings.stream().collect(Collectors.groupingBy(AddressRankingDto::getRankingId));
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), rankingMap);
    }

    // 查询所有交易的接口
    @Override
    public ResponseBean queryTransferTxsWithTotalByPage(String address, String assetName, Integer
            pageNumber, Integer pageSize) {
        PageResponseBean pageResponse;
        Integer txCount = addressDailyAggregationMapper.countAddressTotalTx(address, assetName);
        if (txCount == null || txCount == 0) {
            pageResponse = new PageResponseBean(Collections.emptyList(), 0);
        } else {
            int start = Math.max(pageSize * (pageNumber - 1), 0);
            List<TransferTxDto> transferTxDtos = txDetailMapper.selectTransferTxsByPage(address, assetName, start, pageSize);
            transferTxDtos = formatTransferTxDtos(transferTxDtos);
            pageResponse = new PageResponseBean(transferTxDtos, txCount);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponse);
    }

    // 查询一个地址的所有转账信息
    @Override
    public ResponseBean queryTransferTxsOfTokenTypeByPage(String address, String tokenType, Integer
            pageNumber, Integer pageSize) {
        tokenType = tokenType.toLowerCase();
        PageResponseBean pageResponse;
        Integer txCount = addressDailyAggregationMapper.countAddressTotalTxOfTokenType(address, tokenType);
        if (txCount == null || txCount == 0) {
            pageResponse = new PageResponseBean(Collections.emptyList(), 0);
        } else {
            List<String> contractHashes = txDetailMapper.selectCalledContractHashesOfTokenType(tokenType);
            List<String> assetNames = null;
            if ("oep4".equalsIgnoreCase(tokenType)) {
                assetNames = "oep4".equalsIgnoreCase(tokenType) ? txDetailMapper.selectAssetNamesOfTokenType(tokenType) : null;
            } else if ("orc20".equalsIgnoreCase(tokenType)) {
                assetNames = "orc20".equalsIgnoreCase(tokenType) ? txDetailMapper.selectAssetNamesOfTokenType(tokenType) : null;
            }

            int start = Math.max(pageSize * (pageNumber - 1), 0);
            List<TransferTxDto> transferTxDtos = txDetailMapper.selectTransferTxsOfHashes(address, contractHashes, assetNames,
                    tokenType, start, pageSize);
            transferTxDtos = formatTransferTxDtos(transferTxDtos);
            pageResponse = new PageResponseBean(transferTxDtos, txCount);
        }
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponse);
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
            String assetName = transferTxDto.getAssetName();
            BigDecimal amount = transferTxDto.getAmount();

            String txHash = transferTxDto.getTxHash();
            if (txHash.equals(previousTxHash)) {
                //自己给自己转账，sql会查询出两条记录.需要判断tx_index是否一样
                if (previousTxIndex != transferTxDto.getTxIndex()) {

                    TransferTxDetailDto transferTxDetailDto = TransferTxDetailDto.builder()
                            .amount(amount)
                            .fromAddress(transferTxDto.getFromAddress())
                            .toAddress(transferTxDto.getToAddress())
                            .assetName(transferTxDto.getAssetName())
                            .contractHash(transferTxDto.getContractHash())
                            .build();

                    List<TransferTxDetailDto> transferTxnList =
                            (List<TransferTxDetailDto>) (formattedTransferTxs.get(formattedTransferTxs.size() - 1)).getTransfers();
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
                        .contractHash(transferTxDto.getContractHash())
                        .build();
                List<TransferTxDetailDto> transferTxnList = new ArrayList<>();
                transferTxnList.add(transferTxDetailDto);

                transferTxDto.setTransfers(transferTxnList);
                transferTxDto.setFromAddress(null);
                transferTxDto.setToAddress(null);
                transferTxDto.setAmount(null);
                transferTxDto.setAssetName(null);
                transferTxDto.setTxIndex(null);
                transferTxDto.setContractHash(null);

                formattedTransferTxs.add(transferTxDto);
            }

            previousTxHash = txHash;
        }

        return formattedTransferTxs;
    }


}
