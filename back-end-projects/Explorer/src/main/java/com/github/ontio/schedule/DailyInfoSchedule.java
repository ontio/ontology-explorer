package com.github.ontio.schedule;

import com.github.ontio.service.impl.SummaryServiceImpl;
import com.github.ontio.utils.ConfigParam;
import com.github.ontio.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    private ConfigParam configParam;

    /**
     * 记录每日统计数据
     */
    @Scheduled(cron = "0 10 0 * * *")
    public void UpdateDailyInfo() {

        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());

        summaryService.summaryAllInfo();
    }

//    /**
//     * 记录合约统计数据
//     */
//    @Scheduled(cron = "0 0 0 * * *")
//    public void UpdateDailyInfo() {
//
//        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
//
//        summaryService.summaryAllInfo();
//    }
//
//
//    @Scheduled(cron = "0 0/10 * * * *")
//    public void UpdateContractInfo() {
//
//        logger.info("####{}.{} begin...", CLASS_NAME, Helper.currentMethod());
//        //查询所有contract
//        List<Map> contractList = contractsMapper.selectContract();
//
//        for (Map map :
//                contractList) {
//            try {
//                String type = (String) map.get("Type");
//                String contractHash = (String) map.get("ContractHash");
//                String address = Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58();
//                logger.info("contractHash:{},address:{}", contractHash, address);
//
//                Map<String, Object> paramMap1 = new HashMap<>();
//                paramMap1.put("address", address);
//                paramMap1.put("assetname", "ont");
//                BigDecimal ontCount = transactionDetailMapper.selectContractAssetSum(paramMap1);
//
//                paramMap1.put("assetname", "ong");
//                BigDecimal ongCount = transactionDetailMapper.selectContractAssetSum(paramMap1);
//
//                int txnCount = transactionDetailMapper.selectContractAddrAmount(contractHash);
//
//                int addrCount = 0;
//                int fromAddrCount = 0;
//                int toAddrCount = 0;
//                //OEP4,8其实只用统计toaddress，即参与的所有地址
//                if ("oep4".equals(type) || "oep8".equals(type)) {
//                   // fromAddrCount = transactionDetailMapper.selectAllFromAddressCountByContract(contractHash);
//                    toAddrCount = transactionDetailMapper.selectAllToAddressCountByContract(contractHash);
//                } else {
//                    fromAddrCount = transactionDetailMapper.selectAllFromAddressCountByAddr(address);
//                    toAddrCount = transactionDetailMapper.selectAllToAddressCountByAddr(address);
//                }
//                addrCount = fromAddrCount + toAddrCount;
//
//                Contracts contractsDAO = new Contracts();
//                contractsDAO.setContract(contractHash);
//                contractsDAO.setTxcount(txnCount);
//                contractsDAO.setOngcount(ongCount == null ? new BigDecimal("0") : ongCount.divide(new BigDecimal("1000000000")));
//                contractsDAO.setOntcount(ontCount == null ? new BigDecimal("0") : ontCount);
//                contractsDAO.setAddresscount(addrCount);
//                contractsMapper.updateByPrimaryKeySelective(contractsDAO);
//            } catch (Exception e) {
//                logger.error("error...", e);
//            }
//        }
//    }

}
