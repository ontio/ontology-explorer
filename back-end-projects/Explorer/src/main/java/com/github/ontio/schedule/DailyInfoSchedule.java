package com.github.ontio.schedule;

import com.github.ontio.common.Address;
import com.github.ontio.dao.*;
import com.github.ontio.model.Contracts;
import com.github.ontio.model.Daily;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
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
    private BlockMapper blockMapper;
    @Autowired
    private TransactionDetailMapper transactionDetailMapper;
    @Autowired
    private OntIdMapper ontIdMapper;
    @Autowired
    private DailyMapper dailyMapper;
    @Autowired
    private ContractsMapper contractsMapper;


    /**
     * 记录每日统计数据
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void UpdateDailyInfo() {

        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        long endTime = System.currentTimeMillis() / 1000L;
        long startTime = endTime - 24 * 60 * 60;

        int blockCount = blockMapper.selectBlockCountInOneDay(startTime, endTime);
        int txnCount = transactionDetailMapper.selectTxnCountInOneDay(startTime, endTime);
        String ontIdDes = "Register%";
        int ontidCount = ontIdMapper.selectOntIdCountInOneDay(startTime, endTime, ontIdDes);

/*
        Set<String> addressSet = new HashSet<>();

        List<Map<String, String>> recordList = transactionDetailMapper.selectAllAddress();
        for (Map<String, String> addressMap :
                recordList) {
            for (String key :
                    addressMap.keySet()) {
                addressSet.add(key);
                addressSet.add(addressMap.get(key));
            }
        }
*/

        int addrCount = transactionDetailMapper.selectAllAddressCount();

        Daily dailyDAO = new Daily();
        dailyDAO.setBlockcount(blockCount);
        dailyDAO.setTxncount(txnCount);
        dailyDAO.setOntidcount(ontidCount);
        dailyDAO.setAddresscount(addrCount);
        dailyDAO.setTime(new Date(startTime * 1000L));
        dailyMapper.insert(dailyDAO);
    }


    /**
     * 记录合约统计数据
     */
    @Scheduled(cron = "0 0/10 * * * *")
    public void UpdateContractInfo() {

        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Start", 0);
        paramMap.put("PageSize", 40);
        List<Map> contractList = contractsMapper.selectContractByPage(paramMap);

        for (Map map :
                contractList) {
            try {
                String type = (String) map.get("Type");
                String contractHash = (String) map.get("ContractHash");
                String address = Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58();
                logger.info("contractHash:{},address:{}", contractHash, address);

                Map<String, Object> paramMap1 = new HashMap<>();
                paramMap1.put("address", address);
                paramMap1.put("assetname", "ont");
                BigDecimal ontCount = transactionDetailMapper.selectContractAssetSum(paramMap1);

                paramMap1.put("assetname", "ong");
                BigDecimal ongCount = transactionDetailMapper.selectContractAssetSum(paramMap1);

                int txnCount = transactionDetailMapper.selectContractAddrAmount(contractHash);

                int addrCount = 0;
                int fromAddrCount = 0;
                int toAddrCount = 0;

                if ("oep4".equals(type) || "oep8".equals(type)) {
                    fromAddrCount = transactionDetailMapper.selectAllFromAddressCountByContract(contractHash);
                    toAddrCount = transactionDetailMapper.selectAllToAddressCountByContract(contractHash);
                } else {
                    fromAddrCount = transactionDetailMapper.selectAllFromAddressCountByAddr(address);
                    toAddrCount = transactionDetailMapper.selectAllToAddressCountByAddr(address);
                }

                addrCount = fromAddrCount + toAddrCount;

                Contracts contractsDAO = new Contracts();
                contractsDAO.setContract(contractHash);
                contractsDAO.setTxcount(txnCount);
                contractsDAO.setOngcount(ongCount == null ? new BigDecimal("0") : ongCount.divide(new BigDecimal("1000000000")));
                contractsDAO.setOntcount(ontCount == null ? new BigDecimal("0") : ontCount);
                contractsDAO.setAddresscount(addrCount);
                contractsMapper.updateByPrimaryKeySelective(contractsDAO);
            } catch (Exception e) {
                logger.error("error...", e);
            }
        }

    }


}
