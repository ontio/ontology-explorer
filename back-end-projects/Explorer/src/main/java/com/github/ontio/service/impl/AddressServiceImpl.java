package com.github.ontio.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.ontio.config.ConfigParam;
import com.github.ontio.mapper.Oep4Mapper;
import com.github.ontio.mapper.Oep5Mapper;
import com.github.ontio.mapper.Oep8Mapper;
import com.github.ontio.mapper.TxDetailMapper;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.dao.Oep4;
import com.github.ontio.model.dao.Oep5;
import com.github.ontio.model.dto.BalanceDto;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.service.IAddressService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.OntologySDKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/28
 */
@Slf4j
@Service("AddressService")
public class AddressServiceImpl implements IAddressService {


    @Autowired
    private Oep4Mapper oep4Mapper;
    @Autowired
    private Oep8Mapper oep8Mapper;
    @Autowired
    private Oep5Mapper oep5Mapper;
    @Autowired
    private TxDetailMapper txDetailMapper;

    @Autowired
    private ConfigParam configParam;

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(configParam);
        }
    }


    @Override
    public ResponseBean queryAddressBalance(String address) {

        List<BalanceDto> balanceList = getAddressBalance(address, "");
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), balanceList);
    }


    /**
     * 获取账户余额，可提取的ong，待提取的ong
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getAddressBalance(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();

        initSDK();
        Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);

        if (Helper.isEmptyOrNull(assetName) || ConstantParam.ONG.equals(assetName)) {

            BalanceDto balanceDto1 = BalanceDto.builder()
                    .assetName(ConstantParam.ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal((String) balanceMap.get(ConstantParam.ONG)).divide(ConstantParam.ONG_TOTAL)))
                    .build();
            balanceList.add(balanceDto1);

            //计算等待提取的ong
            String waitBoundOng = calculateWaitingBoundOng(address, (String) balanceMap.get(ConstantParam.ONT));
            BalanceDto balanceDto2 = BalanceDto.builder()
                    .assetName(ConstantParam.WAITBOUND_ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal(waitBoundOng)))
                    .build();
            balanceList.add(balanceDto2);


            //获取可提取的ong
            String unBoundOng = sdk.getUnBoundOng(address);
            if (Helper.isEmptyOrNull(unBoundOng)) {
                unBoundOng = "0";
            }
            //计算等待提取的ong
            BalanceDto balanceDto3 = BalanceDto.builder()
                    .assetName(ConstantParam.UNBOUND_ONG)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance((new BigDecimal(unBoundOng)))
                    .build();
            balanceList.add(balanceDto3);

            //加上ont资产
            if (Helper.isEmptyOrNull(assetName)) {
                BalanceDto balanceDto4 = BalanceDto.builder()
                        .assetName(ConstantParam.ONT)
                        .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                        .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)))
                        .build();
                balanceList.add(balanceDto4);
            }

        } else if (ConstantParam.ONT.equals(assetName)) {

            BalanceDto balanceDto = BalanceDto.builder()
                    .assetName(ConstantParam.ONT)
                    .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                    .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)))
                    .build();
            balanceList.add(balanceDto);
        }

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

        //审核过的OEP5余额
        Oep5 oep5Temp = new Oep5();
        oep5Temp.setAuditflag(ConstantParam.AUDIT_PASSED);
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

        //审核过的OEP8余额,南瓜的也合并在一起了
        List<Map<String,String>> oep8s = oep8Mapper.selectAuditPassedOep8();
        for (Map<String,String> map :
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
     * 计算待提取的ong
     *
     * @param address
     * @param ont
     * @return
     */
    private String calculateWaitingBoundOng(String address, String ont) {

        Integer txntime = txDetailMapper.selectLatestONTTransferTxTime(address);

        if (Helper.isEmptyOrNull(txntime)) {
            return "0";
        }

        long now = System.currentTimeMillis() / 1000L;
        log.info("txntime:{},now:{}", txntime, now);

        BigDecimal totalOng = new BigDecimal(now).subtract(new BigDecimal(txntime)).multiply(configParam.ONG_SECOND_GENERATE);
        BigDecimal ong = totalOng.multiply(new BigDecimal(ont)).divide(ConstantParam.ONT_TOTAL);

        return ong.toPlainString();
    }

    @Override
    public ResponseBean queryTransferTxsByPage(String address, String assetName, Integer pageNumber, Integer pageSize) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("address", address);
        paramMap.put("assetName", assetName);
        paramMap.put("startIndex", 0);
        paramMap.put("pageSize", pageNumber * pageSize * 3);

        List<Map> fromAddrTxnList = txDetailMapper.selectTransferTxByFromAddr(paramMap);
        List<Map> toAddrTxnList = txDetailMapper.selectTransferTxByToAddr(paramMap);

        List<Map> dbTxnList = new ArrayList<>();
        dbTxnList.addAll(fromAddrTxnList);
        dbTxnList.addAll(toAddrTxnList);

        if (fromAddrTxnList.size() != 0 && toAddrTxnList.size() != 0) {
            sortTxnList(dbTxnList);
        }

        List<Map> formattedTxnList = formatTransferTxnList2(dbTxnList);

        List<Map> returnTxnList = new LinkedList<>();

        if (formattedTxnList.size() < pageSize * pageNumber && formattedTxnList.size() > 0) {
            int amount = txDetailMapper.selectTxCountByAddr(address);
            //针对一个地址有T笔1对N转账or一笔1对M转账的特殊处理(T*N>pageNumber*pageSize*3 or M>pageNumber*pageSize*3)
            if (amount > pageNumber * pageSize * 3) {
                returnTxnList = queryAddressInfoSpe(address, pageNumber, pageSize, amount, assetName);
            } else {
                //先查询出txnlist，再根据请求条数进行分页
                //根据分页确认start，end即请求的pageSize条数
                int start = (pageNumber - 1) * pageSize <= 0 ? 0 : (pageNumber - 1) * pageSize;
                int end = (pageSize + start) > formattedTxnList.size() ? formattedTxnList.size() : (pageSize + start);
                for (int k = start; k < end; k++) {
                    returnTxnList.add(formattedTxnList.get(k));
                }
            }
        } else {
            //先查询出txnlist，再根据请求条数进行分页
            //根据分页确认start，end即请求的pageSize条数
            int start = (pageNumber - 1) * pageSize <= 0 ? 0 : (pageNumber - 1) * pageSize;
            int end = (pageSize + start) > formattedTxnList.size() ? formattedTxnList.size() : (pageSize + start);
            for (int k = start; k < end; k++) {
                returnTxnList.add(formattedTxnList.get(k));
            }
        }

        PageResponseBean pageResponseBean = new PageResponseBean();
        pageResponseBean.setRecords(returnTxnList);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }

    /**
     * 根据height倒序,再txnhash,txnindex正序排序
     *
     * @param list
     * @return
     */
    private List<Map<String, Object>> sortTxnList(List list) {

        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer height1 = (Integer) o1.get("blockHeight");
                Integer height2 = (Integer) o2.get("blockHeight");

                if (height1.compareTo(height2) == 0) {
                    String txnHash1 = o1.get("txHash").toString();
                    String txnHash2 = o2.get("txHash").toString();
                    if (txnHash1.compareTo(txnHash2) == 0) {
                        if (o1.containsKey("txIndex") && o2.containsKey("txIndex")) {
                            Integer txnindex1 = (Integer) o1.get("txIndex");
                            Integer txnindex2 = (Integer) o2.get("txIndex");
                            return txnindex1.compareTo(txnindex2);
                        }
                        return txnHash1.compareTo(txnHash2);
                    } else {
                        return txnHash1.compareTo(txnHash2);
                    }
                } else {
                    return -height1.compareTo(height2);
                }
            }
        });

        return list;
    }

    /**
     * 格式化转账交易列表
     *
     * @param dbTxnList
     * @return
     */
    private List<Map> formatTransferTxnList2(List<Map> dbTxnList) {

        List<Map> formattedTxnList = new ArrayList<>();

        String previousTxnHash = "";
        int previousTxnIndex = 0;
        for (int i = 0; i < dbTxnList.size(); i++) {
            Map map = dbTxnList.get(i);
            //金额转换string给前端显示
            map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
            //ONG精度格式化
            String assetName = (String) map.get("AssetName");
            BigDecimal amount = (BigDecimal) map.get("Amount");
            if (ConstantParam.ONG.equals(assetName)) {
                amount = amount.divide(ConstantParam.ONT_TOTAL);
            }

            String txnhash = (String) map.get("TxnHash");
            //   log.info("txnhash:{}", txnhash);

            if (txnhash.equals(previousTxnHash)) {
                //自己给自己转账，sql会查询出两条记录.
                if (previousTxnIndex != (Integer) map.get("TxnIndex")) {

                    Map<String, Object> transfertxnListMap = new HashMap<>();
                    transfertxnListMap.put("Amount", amount.toPlainString());
                    transfertxnListMap.put("FromAddress", map.get("FromAddress"));
                    transfertxnListMap.put("ToAddress", map.get("ToAddress"));
                    transfertxnListMap.put("AssetName", assetName);

                    List<Map> transferTxnList = (List<Map>) (formattedTxnList.get(formattedTxnList.size() - 1)).get("TransferList");
                    transferTxnList.add(transfertxnListMap);
                }
                previousTxnIndex = (Integer) map.get("TxnIndex");
            } else {

                Map<String, Object> transfertxnListMap = new HashMap<>();
                transfertxnListMap.put("Amount", amount.toPlainString());
                transfertxnListMap.put("FromAddress", map.get("FromAddress"));
                transfertxnListMap.put("ToAddress", map.get("ToAddress"));
                transfertxnListMap.put("AssetName", assetName);

                List<Map> transferTxnList = new ArrayList<>();
                transferTxnList.add(transfertxnListMap);

                map.put("TransferList", transferTxnList);

                map.remove("FromAddress");
                map.remove("ToAddress");
                map.remove("Amount");
                map.remove("AssetName");
                previousTxnIndex = (Integer) map.get("TxnIndex");
                map.remove("TxnIndex");

                formattedTxnList.add(map);
            }

            previousTxnHash = txnhash;
        }

        return formattedTxnList;
    }


    /**
     * 针对一个地址有T笔1对N转账or一笔1对M转账的特殊处理(T*N>pageNumber*pageSize*3 or M>pageNumber*pageSize*3)
     *
     * @param address
     * @param pageNumber
     * @param pageSize
     * @param amount
     * @param assetName
     * @return
     */
    private List<Map> queryAddressInfoSpe(String address, int pageNumber, int pageSize, int amount, String assetName) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("address", address);
        paramMap.put("assetName", assetName);
        //int txnAmount = transactionDetailMapper.selectTxnAmountByAddressInfo(paramMap);
        //db查询返回总条数or分页的前几十条
//        int dbReturnNum = (pageNumber * pageSize) > txnAmount ? txnAmount : (pageNumber * pageSize);
        paramMap.put("startIndex", 0);
        paramMap.put("pageSize", amount);

        List<Map> fromAddrTxnList = txDetailMapper.selectTransferTxByFromAddr(paramMap);
        List<Map> toAddrTxnList = txDetailMapper.selectTransferTxByToAddr(paramMap);

        List<Map> dbTxnList = new ArrayList<>();
        dbTxnList.addAll(fromAddrTxnList);
        dbTxnList.addAll(toAddrTxnList);

        if (fromAddrTxnList.size() != 0 && toAddrTxnList.size() != 0) {
            sortTxnList(dbTxnList);
        }

        List<Map> formattedTxnList = formatTransferTxnList2(dbTxnList);

        List<Map> returnTxnList = new LinkedList<>();
        //先查询出txnlist，再根据请求条数进行分页
        //根据分页确认start，end即请求的pageSize条数
        int start = (pageNumber - 1) * pageSize <= 0 ? 0 : (pageNumber - 1) * pageSize;
        int end = (pageSize + start) > formattedTxnList.size() ? formattedTxnList.size() : (pageSize + start);
        for (int k = start; k < end; k++) {
            returnTxnList.add(formattedTxnList.get(k));
        }

        return returnTxnList;
    }



    @Override
    public ResponseBean queryTransferTxsByTime(String address, String assetName, Integer beginTime, Integer endTime) {
        return null;
    }
}
