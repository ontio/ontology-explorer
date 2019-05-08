package com.github.ontio.service.impl;

import com.github.ontio.config.ConfigParam;
import com.github.ontio.mapper.*;
import com.github.ontio.model.Contracts;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.CurrentDto;
import com.github.ontio.service.ISummaryService;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service("SummaryService")
@MapperScan("com.github.ontio.dao")
public class SummaryServiceImpl implements ISummaryService {

    private static final Integer NUM_7 = 7;

    private static final Integer NUM_30 = 30;

    private static final String CLASS_NAME = SummaryServiceImpl.class.getSimpleName();

    @Autowired
    private ConfigParam configParam;
    @Autowired
    private TxDetailMapper txDetailMapper;

    @Autowired
    private DappDailySummaryMapper dappDailySummaryMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractDailySummaryMapper contractDailySummaryMapper;

    @Autowired
    private CurrentMapper currentMapper;

    @Override
    public ResponseBean getBlockChainLatestInfo() {

        CurrentDto currentDto = currentMapper.selectSummaryInfo();
        currentDto.setNodeCount(configParam.SDK_NODE_COUNT);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), currentDto);

    }

    @Override
    public ResponseBean getBlockChainTps() {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("current_tps", calcTps());
        resultMap.put("max_tps", configParam.BLOCKCHAIN_MAX_TPS);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), resultMap);
    }

    /**
     * 计算当前tps
     * @return
     */
    private String calcTps() {
        int now = (int) (System.currentTimeMillis() / 1000);
        Map<String, Object> txCountParam = new HashMap<>();
        txCountParam.put("start_time", now - 60);
        txCountParam.put("end_time", now);
        Integer txPerMin = txDetailMapper.queryTransactionCount(txCountParam);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((double) (txPerMin) / 60);
    }


    private List<Map> querySummaryInfo(List<Map> dailyList, SimpleDateFormat simpleDateFormat, Integer count) {
        List<Map> SummaryInfo = new ArrayList<>();
        if (dailyList.size() < count) {
            return SummaryInfo;
        }
        int weekCount = dailyList.size() / count;
        for (int i = 0; i < weekCount; i++) {
            Map<String, Object> result = dailyList.get(i * count);
            int time = (Integer) result.get("time");
            result.put("time", simpleDateFormat.format((long) time * 1000));
            for (int j = 1; j < count; j++) {
                Map<String, Object> perMap = dailyList.get(i * count + j);
                result.put("tx_count", (Integer) result.get("tx_count") + (Integer) perMap.get("tx_count"));
                result.put("block_count", (Integer) result.get("BlockCount") + (Integer) perMap.get("BlockCount"));
                result.put("active_address_count", (Integer) result.get("active_address_count") + (Integer) perMap.get("active_address_count"));
                result.put("new_ontid_count", (Integer) result.get("new_ontid_count") + (Integer) perMap.get("new_ontid_count"));
                result.put("active_ontid_count", (Integer) result.get("active_ontid_count") + (Integer) perMap.get("active_ontid_count"));
                result.put("new_address_count", (Integer) result.get("new_address_count") + (Integer) perMap.get("new_address_count"));
                result.put("ong_sum", ((BigDecimal) result.get("ong_sum")).add((BigDecimal) perMap.get("ong_sum")));
                result.put("ont_sum", ((BigDecimal) result.get("ont_sum")).add((BigDecimal) perMap.get("ont_sum")));
            }
            result.put("ont_sum", ((BigDecimal) result.get("ont_sum")).toPlainString());
            result.put("ong_sum", ((BigDecimal) result.get("ong_sum")).toPlainString());
            SummaryInfo.add(result);
        }
        return SummaryInfo;
    }

    private List<Map> querySummaryInfo(List<Map> dailyList, SimpleDateFormat simpleDateFormat, Map<String, Object> addressAndOntIdCount) {
        int addressCount = 0;
        int ontIdCount = 0;
        if (addressAndOntIdCount != null && addressAndOntIdCount.get("address_total") != null) {
            addressCount = Integer.parseInt(addressAndOntIdCount.get("address_total").toString());
        }
        if (addressAndOntIdCount != null && addressAndOntIdCount.get("ontid_total") != null) {
            ontIdCount = Integer.parseInt(addressAndOntIdCount.get("ontid_total").toString());
        }

        for (Map map : dailyList) {
            int time = (Integer) map.get("time");
            map.put("time", simpleDateFormat.format((long) time * 1000));
            map.put("ont_sum", ((BigDecimal) map.get("ont_sum")).toPlainString());
            map.put("ong_sum", ((BigDecimal) map.get("ong_sum")).toPlainString());
            addressCount += (Integer) map.get("new_address_count");
            ontIdCount += (Integer) map.get("new_ontid_count");
            map.put("address_total", addressCount);
            map.put("ontid_total", ontIdCount);

        }

        return dailyList;
    }

    @Override
    public ResponseBean getChainSummary(String type, int startTime, int endTime) {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> dailyInfoParam = new HashMap<>();
        dailyInfoParam.put("start_time", startTime);
        dailyInfoParam.put("end_time", endTime);
        List<Map> dailyList = dappDailySummaryMapper.selectDailyInfo(dailyInfoParam);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Map<String, Object> addressAndOntIdCount = dappDailySummaryMapper.selectAddressAndOntIdCount(startTime);
        switch (type.toLowerCase()) {
            case "weekly":
                dailyList = querySummaryInfo(dailyList, simpleDateFormat, NUM_7);
                Collections.reverse(dailyList);
                break;
            case "monthly":
                dailyList = querySummaryInfo(dailyList, simpleDateFormat, NUM_30);
                Collections.reverse(dailyList);
                break;
            default:
                Collections.reverse(dailyList);
                dailyList = querySummaryInfo(dailyList, simpleDateFormat, addressAndOntIdCount);
                break;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", dailyList.size());
        result.put("records", dailyList);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), result);
    }

    private List<Map> queryContract(List<Map> dailyList, SimpleDateFormat simpleDateFormat, int count) {
        List<Map> resultMapList = new ArrayList<>();
        if (dailyList.size() < count) {
            return resultMapList;
        }

        int weekCount = dailyList.size() / count;
        for (int i = 0; i < weekCount; i++) {
            Map<String, Object> result = dailyList.get(i * count);
            int time = (Integer) result.get("time");
            result.put("time", simpleDateFormat.format((long) time * 1000));
            BigInteger tx = (BigInteger) result.get("tx_count");
            BigInteger activeAddress = (BigInteger) result.get("active_address_count");
            BigInteger newAddress = (BigInteger) result.get("new_address_count");
            BigInteger ont = (BigInteger) result.get("ont_sum");
            BigDecimal ong = (BigDecimal) result.get("ong_sum");
            for (int j = 1; j < count; j++) {
                Map perMap = dailyList.get(i * count + j);
                tx = ((BigInteger) perMap.get("tx_count")).add(tx);
                activeAddress = ((BigInteger) perMap.get("active_address_count")).add(activeAddress);
                newAddress = ((BigInteger) perMap.get("active_address_count")).add(newAddress);
                ont = ((BigInteger) perMap.get("ont_sum")).add(ont);
                ong = ((BigDecimal) perMap.get("ong_sum")).add(ong);
            }
            result.put("tx_count", tx.toString());
            result.put("active_address_count", activeAddress.toString());
            result.put("new_address_count", newAddress.toString());
            result.put("ont_sum", ont.toString());
            result.put("ong_sum", ong.toPlainString());
            resultMapList.add(result);
        }

        return resultMapList;
    }

    private List<Map> queryContract(List<Map> dailyList, SimpleDateFormat simpleDateFormat) {
        for (Map map : dailyList) {
            int time = (Integer) map.get("Time");
            map.put("time", simpleDateFormat.format((long) time * 1000));
            map.put("ont_sum", map.get("ont_sum").toString());
            map.put("ong_sum", ((BigDecimal) map.get("ong_sum")).toPlainString());
        }
        return dailyList;
    }

    private List<Map> getContractSummaryList(List<Map> dailyList, String type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        switch (type.toLowerCase()) {
            case "weekly":
                dailyList = queryContract(dailyList, simpleDateFormat, NUM_7);
                break;
            case "monthly":
                dailyList = queryContract(dailyList, simpleDateFormat, NUM_30);
                break;
            default:
                dailyList = queryContract(dailyList, simpleDateFormat);
                break;
        }
        Collections.reverse(dailyList);
        return dailyList;
    }

    @Override
    public ResponseBean getContractSummary(String contractHash, String type, int startTime, int endTime) {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
        Map<String, Object> resultMap = new HashMap<>();
        Contracts contracts = contractMapper.selectContractByContractHash(contractHash);
        if (contracts == null) {
            return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), resultMap);
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("contract_hash", contractHash);
        paramMap.put("start_time", startTime);
        paramMap.put("end_time", endTime);
        List<Map> summaryList = contractDailySummaryMapper.selectDailySummaryByContractHash(paramMap);

        summaryList = getContractSummaryList(summaryList, type);

        resultMap.put("total", summaryList.size());
        resultMap.put("records", summaryList);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), resultMap);
    }
}
