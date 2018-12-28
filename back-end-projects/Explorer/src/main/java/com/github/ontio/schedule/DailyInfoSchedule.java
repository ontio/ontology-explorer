package com.github.ontio.schedule;

import com.alibaba.fastjson.JSON;
import com.github.ontio.common.Address;
import com.github.ontio.dao.*;
import com.github.ontio.model.ContractSummary;
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

    @Autowired
    private DailySummaryMapper dailySummaryMapper;

    @Autowired
    private TransactionDetailTmpMapper transactionDetailTmpMapper;

    @Autowired
    private ContractSummaryMapper contractSummaryMapper;

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

        try {
            Integer startTime = dailySummaryMapper.selectMaxTime();
            if (startTime == null) {
                // 创始区块的时间： 2018-06-30 00：00：00    时间戳：1530288000
                startTime = Integer.valueOf(String.valueOf(System.currentTimeMillis() / 1000));
            }

            // 将每10分钟的数据先转移到临时表，再统计其他信息
            Map param = new HashMap<>();
            param.put("startTime", startTime);
            transactionDetailTmpMapper.deleteAll();
            transactionDetailTmpMapper.InsertSelectiveByStartTime(param);

            //查询所有contract
            List<Contracts> contractList = contractsMapper.selectAllApprovedContract();
            for (Contracts contracts : contractList) {
                String type = contracts.getType();
                String contractHash = contracts.getContract();
                String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58();
                logger.info("contractHash:{}, contractAddress:{}", contractHash, contractAddress);

                ContractSummary contractSummary = contractSummaryMapper.selectContractSummary(contractHash);
                if (contractSummary == null){
                    contractSummary = new ContractSummary();
                    contractSummary.setTxncount(0);
                    contractSummary.setNewaddress(0);
                    contractSummary.setOngcount(new BigDecimal("0"));
                    contractSummary.setOntcount(new BigDecimal("0"));
                }

                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("contractHash", contractHash);
                paramMap.put("contractAddress", contractAddress);
                paramMap.put("assetname", "ont");
                BigDecimal ontCount = transactionDetailTmpMapper.selectContractAssetSum(paramMap);
                ontCount = ontCount == null ? new BigDecimal(0) : ontCount;

                paramMap.put("assetname", "ong");
                BigDecimal ongCount = transactionDetailTmpMapper.selectContractAssetSum(paramMap);
                ongCount = ongCount == null ? new BigDecimal(0) : ongCount.divide(new BigDecimal("1000000000"));

                // 依据合约hash和合约地址分别查询交易数
                int txnCount = transactionDetailTmpMapper.selectTxnAmount(paramMap);

                // 依据合约hash和合约地址分别查询地址数（去重）
                List<String> addressByContractList = transactionDetailTmpMapper.selectAllAddressByContract(contractHash);
                List<String> addressByAddressList = transactionDetailTmpMapper.selectAllAddressByAddress(contractAddress);
                addressByContractList.addAll(addressByAddressList);

                List<String> addressList = new ArrayList<>();
                for (String address: addressByContractList) {
                    if (address.length() != 34){
                        continue;
                    }

                    addressList.add(address);
                }

                // 计算合约内部转账金额
                List<Map> tokenCountMapList = null;
                //OEP4,5,8
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
                }

                contracts.setTxcount(txnCount + contractSummary.getTxncount());
                contracts.setOngcount(ongCount.add(contractSummary.getOngcount()));
                contracts.setOntcount(ontCount.add(contractSummary.getOntcount()));
                contracts.setTokencount(JSON.toJSONString(tokenCountMapList == null ? new ArrayList<>() : tokenCountMapList));
                contracts.setAddresscount(contractSummary.getNewaddress() + addressList.size());
            }

            contractsMapper.banchUpdateByPrimaryKeySelective(contractList);
        } catch (Exception e) {
            logger.error("error...", e);
        }
    }
}
