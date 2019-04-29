package com.github.ontio.schedule;

import com.alibaba.fastjson.JSON;
import com.github.ontio.dao.*;
import com.github.ontio.model.ContractSummary;
import com.github.ontio.model.Contracts;
import com.github.ontio.service.impl.SummaryServiceImpl;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
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
@Slf4j
@Component
@EnableScheduling
public class DailyInfoSchedule {

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
    private TransactionDetailDailyMapper transactionDetailDailyMapper;

    @Autowired
    private ContractSummaryMapper contractSummaryMapper;

    @Autowired
    private AddressSummaryMapper addressSummaryMapper;

    /**
     * 记录每日统计数据
     */
   // @Scheduled(cron = "0 5 0 * * *")
    public void UpdateDailyInfo() {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        summaryService.summaryAllInfo();
    }

    /**
     * 更新合约的地址数、交易数、交易额（ong\ont\tokenName）
     */
  //  @Scheduled(cron = "0 0/10 * * * *")
    public void UpdateContractInfo() {
        log.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        try {
            Integer startTime = dailySummaryMapper.selectMaxTime();
            if (startTime == null) {
                // 创始区块时间： UTC:2018-06-30 00：00：00    时间戳：1530316800
                startTime = 1530316800;
            }

            //每天0:10跑批时，就会将daily表前一天数据清掉。只保留当天0点之后的数据
            Map param = new HashMap<>();
            param.put("startTime", startTime + 24 * 60 * 60);
            if (transactionDetailDailyMapper.selectiveByEndTime(param) != 0) {
                transactionDetailDailyMapper.deleteByEndTime(param);
            }

            //查询所有审核过的contract
            List<Contracts> contractList = contractsMapper.selectAllApprovedContract();
            for (Contracts contracts :
                    contractList) {
                String type = contracts.getType();
                String contractHash = contracts.getContract();
                //String contractAddress = Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58();
                log.info("type:{}, contractHash:{}", type, contractHash);
                //查询合约到目前为止的统计数据
                ContractSummary contractSummary = contractSummaryMapper.selectContractSummary(contractHash);
                if (contractSummary == null) {
                    contractSummary = new ContractSummary();
                    contractSummary.setTxncount(0);
                    contractSummary.setNewaddress(0);
                    contractSummary.setOngcount(ConstantParam.ZERO);
                    contractSummary.setOntcount(ConstantParam.ZERO);
                }
                BigDecimal ontCount = transactionDetailDailyMapper.selectContractAssetAmount(contractHash, ConstantParam.ONT);
                ontCount = ontCount == null ? new BigDecimal(0) : ontCount;

                BigDecimal ongCount = transactionDetailDailyMapper.selectContractAssetAmount(contractHash, ConstantParam.ONG);
                ongCount = ongCount == null ? new BigDecimal(0) : ongCount.divide(ConstantParam.ONG_TOTAL);

                // 依据合约hash查询交易数
                int txnCount = transactionDetailDailyMapper.selectTxnCount(contractHash);
                // 依据payer+fromaddress去重查询地址数
                List<String> addressList = transactionDetailDailyMapper.selectContractAddr(contractHash);

                // 新增地址数=合约活跃地址数-address_summary所有地址
                List<String> allAddressList = addressSummaryMapper.selectDistinctAddressByContract(contractHash);
                addressList.removeAll(allAddressList);
                int newAddressCount = addressList.size();

                // 计算合约内部转账金额
                List<Map> tokenCountMapList = null;
                //OEP4,5,8
                if (type.startsWith("oep")) {
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("contractHash", contractHash);
                    if (type.equalsIgnoreCase("oep5")) {
                        paramMap.put("OEP5", "oep5");
                        tokenCountMapList = transactionDetailMapper.selectContractTokenAllSum(paramMap);
                        if (!Helper.isEmptyOrNull(tokenCountMapList) && tokenCountMapList.size() != 0) {
                            Map oep8Map = tokenCountMapList.get(0);
                            oep8Map.put("Assetname", ((String) oep8Map.get("Assetname")).split(":")[0]);
                        }

                    } else {
                        tokenCountMapList = transactionDetailMapper.selectContractTokenAllSum(paramMap);
                    }
                }

                contracts.setTxcount(txnCount + contractSummary.getTxncount());
                contracts.setOngcount(ongCount.add(contractSummary.getOngcount()));
                contracts.setOntcount(ontCount.add(contractSummary.getOntcount()));
                contracts.setTokencount(JSON.toJSONString(tokenCountMapList == null ? new ArrayList<>() : tokenCountMapList));
                contracts.setAddresscount(newAddressCount + contractSummary.getNewaddress());
            }

            contractsMapper.banchUpdateByPrimaryKeySelective(contractList);
        } catch (Exception e) {
            log.error("UpdateContractInfo error...", e);
        }
    }
}
