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
import com.github.ontio.model.dto.BalanceDto;
import com.github.ontio.model.dto.QueryBatchBalanceDto;
import com.github.ontio.model.dto.TransferTxDetailDto;
import com.github.ontio.model.dto.TransferTxDto;
import com.github.ontio.service.IAddressService;
import com.github.ontio.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.Charset;
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

    @Autowired
    public AddressServiceImpl(Oep4Mapper oep4Mapper, Oep8Mapper oep8Mapper, Oep5Mapper oep5Mapper, TxDetailMapper txDetailMapper, ParamsConfig paramsConfig) {
        this.oep4Mapper = oep4Mapper;
        this.oep8Mapper = oep8Mapper;
        this.oep5Mapper = oep5Mapper;
        this.txDetailMapper = txDetailMapper;
        this.paramsConfig = paramsConfig;
    }

    private OntologySDKService sdk;

    private synchronized void initSDK() {
        if (sdk == null) {
            sdk = OntologySDKService.getInstance(paramsConfig);
        }
    }

    private static HttpClient httpClient;

    static {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        //客户端和服务器建立连接的timeout
        requestConfigBuilder.setConnectTimeout(30000);
        //从连接池获取连接的timeout
        requestConfigBuilder.setConnectionRequestTimeout(30000);
        //连接建立后，request没有回应的timeout
        requestConfigBuilder.setSocketTimeout(30000);

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        clientBuilder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(30000).build()); //连接建立后，request没有回应的timeout
        clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(60);
        httpClient = clientBuilder.setConnectionManager(cm).build();
    }


    @Override
    public ResponseBean queryAddressBalance(String address, String tokenType) {

        List<BalanceDto> balanceList = new ArrayList<>();

        switch (tokenType.toLowerCase()) {
            case ConstantParam.ASSET_TYPE_OEP4:
                balanceList = getOep4Balance(address, "");
                break;
            case ConstantParam.ASSET_TYPE_OEP5:
                balanceList = getOep5Balance(address, "");
                break;
            case ConstantParam.ASSET_TYPE_OEP8:
                balanceList = getOep8Balance(address);
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

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), balanceList);
    }

    @Override
    public ResponseBean queryAddressBalanceByAssetName(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();

        if (ConstantParam.ONT.equals(assetName) || ConstantParam.ONG.equals(assetName)) {

            initSDK();
            Map<String, Object> balanceMap = sdk.getNativeAssetBalance(address);
            if (ConstantParam.ONT.equals(assetName)) {
                //ONT
                BalanceDto balanceDto4 = BalanceDto.builder()
                        .assetName(ConstantParam.ONT)
                        .assetType(ConstantParam.ASSET_TYPE_NATIVE)
                        .balance(new BigDecimal((String) balanceMap.get(ConstantParam.ONT)))
                        .build();
                balanceList.add(balanceDto4);
            } else {
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
                    balanceList = getOep5Balance(address, assetName);
                }
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
        balanceDtos.addAll(getOep5Balance(address, ""));
        balanceDtos.addAll(getOep8Balance(address));
        return balanceDtos;
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

        QueryBatchBalanceDto queryBatchBalanceDto = QueryBatchBalanceDto.builder()
                .action("getmultibalance")
                .version("1.0.0")
                .base58Addrs(address)
                .contractAddrs(contractAddrsStr.substring(0, contractAddrsStr.length() - 1))
                .build();
        //make request body
        StringEntity strEntity = new StringEntity(JacksonUtil.beanToJSonStr(queryBatchBalanceDto), Charset.forName("UTF-8"));
        strEntity.setContentEncoding("UTF-8");
        strEntity.setContentType("application/json");
        //send request
        HttpPost httpPost = new HttpPost(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL);
        httpPost.setEntity(strEntity);
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost);
            HttpEntity entitys = response.getEntity();
            String responseStr = EntityUtils.toString(entitys);
            log.info("batchbalance response:{}", responseStr);
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
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
        return balanceList;
    }

    /**
     * get oep5 balance
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep5Balance(String address, String assetName) {

        List<BalanceDto> balanceList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        //query audit passed oep5 token
        Oep5 oep5Temp = new Oep5();
        oep5Temp.setAuditFlag(ConstantParam.AUDIT_PASSED);
        List<Oep5> oep5s = oep5Mapper.select(oep5Temp);
        if (Helper.isNotEmptyOrNull(assetName)) {
            oep5Temp.setSymbol(assetName);
        }
        oep5s.forEach(item -> stringBuilder.append(item.getContractHash()).append(","));
        String contractAddrsStr = stringBuilder.toString();

        QueryBatchBalanceDto queryBatchBalanceDto = QueryBatchBalanceDto.builder()
                .action("getmultibalance")
                .version("1.0.0")
                .base58Addrs(address)
                .contractAddrs(contractAddrsStr.substring(0, contractAddrsStr.length() - 1))
                .build();
        //make request body
        StringEntity strEntity = new StringEntity(JacksonUtil.beanToJSonStr(queryBatchBalanceDto), Charset.forName("UTF-8"));
        strEntity.setContentEncoding("UTF-8");
        strEntity.setContentType("application/json");
        //send request
        HttpPost httpPost = new HttpPost(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL);
        httpPost.setEntity(strEntity);
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost);
            HttpEntity entitys = response.getEntity();
            String responseStr = EntityUtils.toString(entitys);
            log.info("batchbalance response:{}", responseStr);
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
                            BalanceDto balanceDto = BalanceDto.builder()
                                    .assetName(item.getSymbol())
                                    .assetType(ConstantParam.ASSET_TYPE_OEP5)
                                    .balance(balance)
                                    .build();
                            balanceList.add(balanceDto);
                        }
                    }
            );
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
        return balanceList;
    }

    /**
     * get oep8 token
     *
     * @param address
     * @return
     */
    private List<BalanceDto> getOep8Balance(String address) {

        List<BalanceDto> balanceList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        //query audit passed oep8 token
        List<Map<String, String>> oep8s = oep8Mapper.selectAuditPassedOep8();
        oep8s.forEach(item -> stringBuilder.append(item.get("contractHash")).append(","));
        String contractAddrsStr = stringBuilder.toString();

        QueryBatchBalanceDto queryBatchBalanceDto = QueryBatchBalanceDto.builder()
                .action("getmultibalance")
                .version("1.0.0")
                .base58Addrs(address)
                .contractAddrs(contractAddrsStr.substring(0, contractAddrsStr.length() - 1))
                .build();
        log.info("request:{}", JacksonUtil.beanToJSonStr(queryBatchBalanceDto));
        //make request body
        StringEntity strEntity = new StringEntity(JacksonUtil.beanToJSonStr(queryBatchBalanceDto), Charset.forName("UTF-8"));
        strEntity.setContentEncoding("UTF-8");
        strEntity.setContentType("application/json");
        //send request
        HttpPost httpPost = new HttpPost(paramsConfig.BALANCESERVICE_HOST + ConstantParam.BALANCESERVICE_QUERYBALANCE_URL);
        httpPost.setEntity(strEntity);
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost);
            HttpEntity entitys = response.getEntity();
            String responseStr = EntityUtils.toString(entitys);
            log.info("batchbalance response:{}", responseStr);
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            JSONArray oepBalanceArray = ((JSONObject) jsonObject.getJSONArray("Result").get(0)).getJSONArray("OepBalance");

            Map<String, String> map = new HashMap<>();
            oepBalanceArray.forEach(item -> {
                JSONObject obj = (JSONObject) item;
                map.put(obj.getString("contract_address"), obj.getString("balance"));
            });

            initSDK();
            JSONArray balanceArray = sdk.getOpe8AssetBalance(address, "edf64937ca304ea8180fa92e2de36dc0a33cc712");


/*            oep4s.forEach(item -> {
                        String contractHash = item.getContractHash();
                        BigDecimal balance = new BigDecimal(map.get(contractHash));
                        if (balance.compareTo(ConstantParam.ZERO) != 0) {
                            BalanceDto balanceDto = BalanceDto.builder()
                                    .assetName(item.getSymbol())
                                    .assetType(ConstantParam.ASSET_TYPE_OEP4)
                                    .balance(balance)
                                    .build();
                            balanceList.add(balanceDto);
                        }
                    }
            );*/
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
        return balanceList;

/*        for (Map<String, String> map :
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
        }*/
    }

    /**
     * 计算待提取的ong
     *
     * @param address
     * @param ont
     * @return
     */
    private String calculateWaitingBoundOng(String address, String ont) {

        Integer txtime = txDetailMapper.selectLatestOntTransferTxTime(address);

        if (Helper.isEmptyOrNull(txtime)) {
            return "0";
        }
        long now = System.currentTimeMillis() / 1000L;
        log.info("calculateWaitingBoundOng latestOntTransferTxTime:{},now:{}", txtime, now);

        BigDecimal totalOng = new BigDecimal(now).subtract(new BigDecimal(txtime)).multiply(paramsConfig.ONG_SECOND_GENERATE);
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

        //云斗龙资产使用like查询, for ONTO
/*        if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
            assetName = assetName + "%";
            transferTxDtos = txDetailMapper.selectDragonTransferTxsByTime(address, assetName, beginTime, endTime);
        } else {
            transferTxDtos = txDetailMapper.selectTransferTxsByTime(address, assetName, beginTime, endTime);
        }*/

        List<TransferTxDto> transferTxDtos = txDetailMapper.selectTransferTxsByTime(address, assetName, beginTime, endTime);

        List<TransferTxDto> formattedTransferTxDtos = formatTransferTxDtos(transferTxDtos);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), formattedTransferTxDtos);
    }


    @Override
    public ResponseBean queryTransferTxsByTimeAndPage(String address, String assetName, Long endTime, Integer pageSize) {

        List<TransferTxDto> transferTxDtos = new ArrayList<>();
        //云斗龙资产使用like查询，for ONTO，只查询转账不包括ong手续费记录
        if (ConstantParam.HYPERDRAGONS.equals(assetName)) {
            assetName = assetName + "%";
            transferTxDtos = txDetailMapper.selectDragonTransferTxsByTimeAndPage(address, assetName, endTime, pageSize);
        } else {
            transferTxDtos = txDetailMapper.selectTransferTxsByTimeAndPage(address, assetName, endTime, pageSize);
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
