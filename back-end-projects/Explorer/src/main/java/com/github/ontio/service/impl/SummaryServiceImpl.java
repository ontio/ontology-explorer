package com.github.ontio.service.impl;

import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.OngSupply;
import com.github.ontio.model.dto.ContractDailySummaryDto;
import com.github.ontio.model.dto.CurrentDto;
import com.github.ontio.model.dto.DailySummaryDto;
import com.github.ontio.service.ISummaryService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.OntologySDKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("SummaryService")
public class SummaryServiceImpl implements ISummaryService {

    private static final Integer TIMESTAMP_20200707000000_UTC = 1594080000;
    private static final Integer TIMESTAMP_20200629000000_UTC = 1593388800;
    private static final Integer TIMESTAMP_20190630000000_UTC = 1561852800;
    private static final Integer TIMESTAMP_20180630000000_UTC = 1530316800;

    private final ParamsConfig paramsConfig;
    private final TxEventLogMapper txEventLogMapper;
    private final DailySummaryMapper dailySummaryMapper;
    private final ContractDailySummaryMapper contractDailySummaryMapper;
    private final CurrentMapper currentMapper;
    private final AddressDailySummaryMapper addressDailySummaryMapper;
    private final OntologySDKService ontologySDKService;
    private final OngSupplyMapper ongSupplyMapper;

    @Autowired
    public SummaryServiceImpl(ParamsConfig paramsConfig, TxEventLogMapper txEventLogMapper, DailySummaryMapper dailySummaryMapper, ContractDailySummaryMapper contractDailySummaryMapper, CurrentMapper currentMapper, AddressDailySummaryMapper addressDailySummaryMapper, OntologySDKService ontologySDKService
            , OngSupplyMapper ongSupplyMapper) {
        this.paramsConfig = paramsConfig;
        this.txEventLogMapper = txEventLogMapper;
        this.dailySummaryMapper = dailySummaryMapper;
        this.contractDailySummaryMapper = contractDailySummaryMapper;
        this.currentMapper = currentMapper;
        this.addressDailySummaryMapper = addressDailySummaryMapper;
        this.ontologySDKService = ontologySDKService;
        this.ongSupplyMapper = ongSupplyMapper;
    }


    @Override
    public ResponseBean getBlockChainLatestInfo() {

        CurrentDto currentDto = currentMapper.selectSummaryInfo();
        currentDto.setNodeCount(paramsConfig.BLOCKCHAIN_NODE_COUNT);

        Integer addressCount = addressDailySummaryMapper.selectAllAddressCount(ConstantParam.ADDR_DAILY_SUMMARY_ONTONG_CONTRACTHASH);
        currentDto.setAddressCount(addressCount);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), currentDto);

    }

    @Override
    public ResponseBean getBlockChainTps() {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("current_tps", calcTps());
        resultMap.put("max_tps", paramsConfig.BLOCKCHAIN_MAX_TPS);

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), resultMap);
    }

    /**
     * 计算当前tps
     *
     * @return
     */
    private String calcTps() {
        Long now = System.currentTimeMillis() / 1000;
        Integer txPerMin = txEventLogMapper.queryTxCount(now - 60, now);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((double) (txPerMin) / 60);
    }


    @Override
    public ResponseBean getBlockChainDailySummary(Long startTime, Long endTime) {

        List<DailySummaryDto> dailySummaryDtos = dailySummaryMapper.selectSummaryByTime(startTime, endTime);
        if (dailySummaryDtos.size() > 0) {
            Map<String, BigDecimal> addrAndOntidCountMap = dailySummaryMapper.selectAddrAndOntIdTotal(startTime);
            if (Helper.isEmptyOrNull(addrAndOntidCountMap)) {
                dailySummaryDtos.get(0).setOntidTotal(0);
                dailySummaryDtos.get(0).setAddressTotal(0);
            } else {
                dailySummaryDtos.get(0).setAddressTotal(addrAndOntidCountMap.get("addressTotal").intValue());
                dailySummaryDtos.get(0).setOntidTotal(addrAndOntidCountMap.get("ontidTotal").intValue());
            }

            for (int i = 1; i < dailySummaryDtos.size(); i++) {
                DailySummaryDto dailySummaryDto = dailySummaryDtos.get(i);
                dailySummaryDto.setAddressTotal(dailySummaryDtos.get(i - 1).getAddressTotal() + dailySummaryDto.getNewAddressCount());
                dailySummaryDto.setOntidTotal(dailySummaryDtos.get(i - 1).getOntidTotal() + dailySummaryDto.getNewOntidCount());
            }
        }

        PageResponseBean pageResponseBean = new PageResponseBean(dailySummaryDtos, dailySummaryDtos.size());

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }


    @Override
    public ResponseBean getContractDailySummary(String contractHash, Long startTime, Long endTime) {

        List<ContractDailySummaryDto> contractDailySummaryDtos = contractDailySummaryMapper.selectDailySummaryByContractHash(contractHash, startTime, endTime);

        PageResponseBean pageResponseBean = new PageResponseBean(contractDailySummaryDtos, contractDailySummaryDtos.size());

        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), pageResponseBean);
    }


    @Override
    public ResponseBean getNativeTotalSupply() {

        BigDecimal specialAddrOnt = new BigDecimal("0");
        for (String addr :
                ConstantParam.SPECIALADDRLIST) {
            Map<String, String> map = ontologySDKService.getNativeAssetBalance(addr);
            specialAddrOnt = specialAddrOnt.add(new BigDecimal(map.get("ont")));
        }
        BigDecimal ontTotalSupply = ConstantParam.ONT_TOTAL.subtract(specialAddrOnt);

        BigDecimal ong01 = new BigDecimal(TIMESTAMP_20190630000000_UTC).subtract(new BigDecimal(TIMESTAMP_20180630000000_UTC)).multiply(new BigDecimal(5));
        BigDecimal ong02 = new BigDecimal(TIMESTAMP_20200629000000_UTC).subtract(new BigDecimal(TIMESTAMP_20190630000000_UTC)).multiply(new BigDecimal(4));
        BigDecimal ong03 = new BigDecimal(TIMESTAMP_20200707000000_UTC).subtract(new BigDecimal(TIMESTAMP_20200629000000_UTC)).multiply(new BigDecimal(3));
        BigDecimal totalOng = ong01.add(ong02).add(ong03);
        BigDecimal ongTotalSupply = totalOng.multiply(ontTotalSupply).divide(ConstantParam.ONT_TOTAL);

        List<OngSupply> ongSupplies = ongSupplyMapper.selectAll();
        if (!CollectionUtils.isEmpty(ongSupplies)) {
            OngSupply ongSupply = ongSupplies.get(0);
            BigDecimal roundOngSupply = ongSupply.getOngSupply();
            ongTotalSupply = ongTotalSupply.add(roundOngSupply);
        }
        Map<String, BigDecimal> rsMap = new HashMap<>();
        rsMap.put("ong", ongTotalSupply);
        rsMap.put("ont", ontTotalSupply);
        return new ResponseBean(ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), rsMap);
    }

    @Override
    public BigDecimal queryNativeTotalCirculatingSupply(String token) {
        BigDecimal specialAddrOnt = new BigDecimal("0");
        for (String addr :
                ConstantParam.SPECIALADDRLIST) {
            Map<String, String> map = ontologySDKService.getNativeAssetBalance(addr);
            specialAddrOnt = specialAddrOnt.add(new BigDecimal(map.get("ont")));
        }
        BigDecimal ontTotalSupply = ConstantParam.ONT_TOTAL.subtract(specialAddrOnt);

        if ("ont".equals(token.toLowerCase())) {
            return ontTotalSupply;
        }

        if ("ong".equals(token.toLowerCase())) {
            BigDecimal ong01 = new BigDecimal(TIMESTAMP_20190630000000_UTC).subtract(new BigDecimal(TIMESTAMP_20180630000000_UTC)).multiply(new BigDecimal(5));
            BigDecimal ong02 = new BigDecimal(TIMESTAMP_20200629000000_UTC).subtract(new BigDecimal(TIMESTAMP_20190630000000_UTC)).multiply(new BigDecimal(4));
            BigDecimal ong03 = new BigDecimal(TIMESTAMP_20200707000000_UTC).subtract(new BigDecimal(TIMESTAMP_20200629000000_UTC)).multiply(new BigDecimal(3));
            BigDecimal totalOng = ong01.add(ong02).add(ong03);
            BigDecimal ongTotalSupply = totalOng.multiply(ontTotalSupply).divide(ConstantParam.ONT_TOTAL);

            List<OngSupply> ongSupplies = ongSupplyMapper.selectAll();
            if (!CollectionUtils.isEmpty(ongSupplies)) {
                OngSupply ongSupply = ongSupplies.get(0);
                BigDecimal roundOngSupply = ongSupply.getOngSupply();
                ongTotalSupply = ongTotalSupply.add(roundOngSupply);
            }
            BigDecimal billion = new BigDecimal(1000000000);
            if (ongTotalSupply.compareTo(billion) == 1) {
                ongTotalSupply = billion;
            }
            return ongTotalSupply;
        }
        return null;
    }
}
