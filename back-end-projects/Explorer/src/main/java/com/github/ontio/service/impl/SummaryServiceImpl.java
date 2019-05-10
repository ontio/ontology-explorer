package com.github.ontio.service.impl;

import com.github.ontio.config.ConfigParam;
import com.github.ontio.mapper.ContractDailySummaryMapper;
import com.github.ontio.mapper.CurrentMapper;
import com.github.ontio.mapper.DailySummaryMapper;
import com.github.ontio.mapper.TxEventLogMapper;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dto.ContractDailySummaryDto;
import com.github.ontio.model.dto.CurrentDto;
import com.github.ontio.model.dto.DailySummaryDto;
import com.github.ontio.service.ISummaryService;
import com.github.ontio.util.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("SummaryService")
public class SummaryServiceImpl implements ISummaryService {

    private final ConfigParam configParam;
    private final TxEventLogMapper  txEventLogMapper;
    private final DailySummaryMapper dailySummaryMapper;
    private final ContractDailySummaryMapper contractDailySummaryMapper;
    private final CurrentMapper currentMapper;

    @Autowired
    public SummaryServiceImpl(ConfigParam configParam, TxEventLogMapper txEventLogMapper, DailySummaryMapper dailySummaryMapper, ContractDailySummaryMapper contractDailySummaryMapper, CurrentMapper currentMapper) {
        this.configParam = configParam;
        this.txEventLogMapper = txEventLogMapper;
        this.dailySummaryMapper = dailySummaryMapper;
        this.contractDailySummaryMapper = contractDailySummaryMapper;
        this.currentMapper = currentMapper;
    }

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
            Map<String, Integer> addrAndOntidCountMap = dailySummaryMapper.selectAddrAndOntIdCount(startTime);

            dailySummaryDtos.get(0).setAddressTotal(addrAndOntidCountMap.get("addressTotal"));
            dailySummaryDtos.get(0).setOntidTotal(addrAndOntidCountMap.get("ontidTotal"));

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
}
