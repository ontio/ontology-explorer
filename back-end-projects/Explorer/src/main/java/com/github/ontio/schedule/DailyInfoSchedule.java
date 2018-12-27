package com.github.ontio.schedule;

import com.alibaba.fastjson.JSON;
import com.github.ontio.common.Address;
import com.github.ontio.dao.ContractsMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.model.Contracts;
import com.github.ontio.service.impl.SummaryServiceImpl;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/12/6
 */
@Component
@EnableScheduling
public class DailyInfoSchedule {

    private static final Logger logger = LoggerFactory.getLogger(DailyInfoSchedule.class);

    private static final String CLASS_NAME = DailyInfoSchedule.class.getSimpleName();

    @Autowired
    private SummaryServiceImpl summaryService;

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private TransactionDetailMapper transactionDetailMapper;

    /**
     * 记录每日统计数据
     */
    @Scheduled(cron = "0 10 0 * * *")
    public void UpdateDailyInfo() {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        summaryService.summaryAllInfo();
    }

    /**
     * 更新合约的地址数、交易数、交易额（ong\ont\tokenName）
     */
    @Scheduled(cron = "0 0/10 * * * *")
    public void UpdateContractInfo() {
        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        //查询所有contract
        List<Contracts> contractList = contractsMapper.selectAllApprovedContract();
        try {
            for (Contracts contracts : contractList) {
                String type = contracts.getType();
                String contractHash = contracts.getContract();
                String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58();
                logger.info("contractHash:{}, contractAddress:{}", contractHash, contractAddress);

                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("contractHash", contractHash);
                paramMap.put("contractAddress", contractAddress);
                paramMap.put("assetname", "ont");
                BigDecimal ontCount = transactionDetailMapper.selectContractAssetAllSum(paramMap);

                paramMap.put("assetname", "ong");
                BigDecimal ongCount = transactionDetailMapper.selectContractAssetAllSum(paramMap);

                // 依据合约hash和合约地址分别查询交易数
//                int txnCount = transactionDetailMapper.selectTxnAllAmount(paramMap);
                int txnCount = transactionDetailMapper.selectContractAddrAmount(contractHash);

                List<Map> tokenCountMapList = null;
                int fromAddrCount = 0;
                int toAddrCount = 0;
                //OEP4,5,8其实只用统计toaddress，即参与的所有地址
                if (type.startsWith("oep")) {
                    if(type.equalsIgnoreCase("oep5")){
                        paramMap.put("OEP5", "oep5");
                        tokenCountMapList = transactionDetailMapper.selectContractTokenAllSum(paramMap);
                        if (!tokenCountMapList.isEmpty()){
                            Map oep8Map = tokenCountMapList.get(0);
                            oep8Map.put("Assetname", ((String)oep8Map.get("Assetname")).split(":")[0]);
                        }

                    }else {
                        tokenCountMapList = transactionDetailMapper.selectContractTokenAllSum(paramMap);
                    }

                    // fromAddrCount = transactionDetailMapper.selectAllFromAddressCountByContract(contractHash);
                    toAddrCount = transactionDetailMapper.selectAllToAddressCountByContract(contractHash);
                } else {
                    fromAddrCount = transactionDetailMapper.selectAllFromAddressCountByAddr(contractAddress);
                    toAddrCount = transactionDetailMapper.selectAllToAddressCountByAddr(contractAddress);
                }

                contracts.setTxcount(txnCount);
                contracts.setOngcount(ongCount == null ? new BigDecimal("0") : ongCount.divide(new BigDecimal("1000000000")));
                contracts.setOntcount(ontCount == null ? new BigDecimal("0") : ontCount);
                contracts.setTokencount(JSON.toJSONString(tokenCountMapList == null ? new ArrayList<>() : tokenCountMapList));
                contracts.setAddresscount(fromAddrCount + toAddrCount);
            }

            contractsMapper.banchUpdateByPrimaryKeySelective(contractList);
        } catch (Exception e) {
            logger.error("error...", e);
        }
    }
}
