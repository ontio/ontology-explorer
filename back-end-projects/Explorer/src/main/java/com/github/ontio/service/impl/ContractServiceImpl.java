package com.github.ontio.service.impl;

import com.github.ontio.common.Address;
import com.github.ontio.dao.ContractsMapper;
import com.github.ontio.dao.Oep4Mapper;
import com.github.ontio.dao.Oep4TxnDetailMapper;
import com.github.ontio.dao.TransactionDetailMapper;
import com.github.ontio.model.Contracts;
import com.github.ontio.model.Oep4;
import com.github.ontio.utils.Helper;
import com.github.ontio.paramBean.Result;
import com.github.ontio.service.IContractService;
import com.github.ontio.utils.ErrorInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @version
 * @date
 */
@Service("ContractService")
@MapperScan("com.github.ontio.dao")
public class ContractServiceImpl implements IContractService {
    private static final Logger logger = LoggerFactory.getLogger(CurrentServiceImpl.class);

    private static final String VERSION = "1.0";

    @Autowired
    private ContractsMapper contractsMapper;

    @Autowired
    private Oep4Mapper oep4Mapper;

    @Autowired
    private Oep4TxnDetailMapper oep4TxnDetailMapper;

    @Autowired
    private TransactionDetailMapper transactionDetailMapper;

    @Override
    public Result queryContract(Integer pageSize, Integer pageNumber){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
        paramMap.put("PageSize", pageSize);
        List<Map> list = contractsMapper.selectContractByPage(paramMap);

        Map<String, Object> rs = new HashMap();
        rs.put("Total", contractsMapper.selectContractCount());
        rs.put("ContractList", list);

        return Helper.result("QueryContract", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    };

    /**
     * query txn by page
     * @param contractHash   contractHash
     * @param pageSize   the amount of each page
     * @param pageNumber the start page
     * @return
     */
    @Override
    public Result queryContractByHash(String contractHash, int pageSize, int pageNumber) {
        Map<String, Object> rs = getResultMap(contractHash, "", pageSize, pageNumber);

        return Helper.result("QueryContractByHash", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    private Map<String, Object> getResultMap(String contractHash, String type, int pageSize, int pageNumber){
        Contracts contract = contractsMapper.selectContractByContractHash(contractHash);
        if (contract == null)
        {
            return null;
        }

        if (type.isEmpty()){
            type = contract.getType();
        }

        Map<String, Object> paramMap1 = new HashMap<>();
        paramMap1.put("address", Address.parse(com.github.ontio.common.Helper.reverse(contractHash)).toBase58());
        paramMap1.put("assetname", "ont");
        String ontCount = transactionDetailMapper.selectContractAssetCountByHeight(paramMap1);

        paramMap1.put("assetname", "ong");
        String ongCount = transactionDetailMapper.selectContractAssetCountByHeight(paramMap1);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("contractHash", contractHash);
        paramMap.put("Start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
        paramMap.put("PageSize", pageSize);
        List<Map> txnList = null;
        int contractTxsCount = 0;
        switch (type.toLowerCase()){
            case "oep4":
                txnList = oep4TxnDetailMapper.selectContractByHash(paramMap);
                contractTxsCount = oep4TxnDetailMapper.selectContractByHashAmount(contractHash);
                break;
            case "oep8":
                txnList = oep4TxnDetailMapper.selectContractByHash(paramMap);
                contractTxsCount = oep4TxnDetailMapper.selectContractByHashAmount(contractHash);
                break;
            default:
                txnList = transactionDetailMapper.selectContractByHash(paramMap);
                contractTxsCount = transactionDetailMapper.selectContractByHashAmount(contractHash);
                break;
        }

        if (!txnList.isEmpty()){
            for (Map map : txnList) {
                map.put("Fee", ((BigDecimal) map.get("Fee")).toPlainString());
            }
        }

        Map<String, Object> rs = new HashMap();
        rs.put("TxnList", txnList);
        rs.put("Total", contractTxsCount);
        rs.put("Creator", contract.getCreator());
        rs.put("Name", contract.getName());
        rs.put("ABI", contract.getAbi());
        rs.put("Code", contract.getCode());
        rs.put("CreateTime", contract.getCreatetime());
        rs.put("UpdateTime", contract.getUpdatetime());
        rs.put("ContactInfo", contract.getContactinfo());
        rs.put("Description", contract.getDescription());
        rs.put("Logo", contract.getLogo());
        rs.put("OntCount", ontCount == null ? 0 : ontCount);
        rs.put("OngCount", ongCount == null ? 0 : new BigDecimal(ongCount).divide(new BigDecimal(1000000000)));

        return rs;
    }

    /**
     *  依据类型查询token列表
     * @param type   type
     * @param pageSize   the amount of each page
     * @param pageNumber the start page
     * @return
     */
    @Override
    public Result queryOEPContract(String type, int pageSize, int pageNumber){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("Start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));
        paramMap.put("PageSize", pageSize);
        List<Map> contractList = null;
        int totalNum = 0;
        switch (type.toLowerCase()){
            case "oep4":
                contractList = oep4Mapper.queryOEPContracts(paramMap);
                totalNum = oep4Mapper.queryOEPContractCount();
                break;
            case "oep8":
                contractList = oep4Mapper.queryOEPContracts(paramMap);
                totalNum = oep4Mapper.queryOEPContractCount();
                break;
            default:
                break;
        }

        Map<String, Object> rs = new HashMap();
        rs.put("ContractList", contractList);
        rs.put("Total", totalNum);

        return Helper.result("QueryOEPContracts", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }

    /**
     *  依据合约hash查询Token合约
     * @param contractHash   contractHash
     * @param type   type
     * @param pageSize   the amount of each page
     * @param pageNumber the start page
     * @return
     */
    @Override
    public Result queryOEPContractByHash(String contractHash, String type, int pageSize, int pageNumber){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("contract", contractHash);
        paramMap.put("PageSize", pageSize);
        paramMap.put("Start", pageSize * (pageNumber - 1) < 0 ? 0 : pageSize * (pageNumber - 1));

        Map<String, Object> rs = getResultMap(contractHash, type, pageSize, pageNumber);
        if(rs == null){
            return Helper.result("QueryOEPContractByHash", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
        }
        switch (type.toLowerCase()){
            case "oep4":
                Oep4 oep4 = oep4Mapper.queryOEPContract(contractHash);
                rs.put("TotalSupply", oep4 == null ? 0 : oep4.getTotalsupply());
                rs.put("Decimals", oep4 == null ? 0 : oep4.getDecimals());
                rs.put("Symbol", oep4 == null ? 0 : oep4.getSymbol());
                break;
            case "oep8":
                Oep4 oep41 = oep4Mapper.queryOEPContract(contractHash);
                rs.put("TotalSupply", oep41 == null ? 0 : oep41.getTotalsupply());
                rs.put("Decimals", oep41 == null ? 0 : oep41.getDecimals());
                rs.put("Symbol", oep41 == null ? 0 : oep41.getSymbol());
                break;
            default:
                break;
        }

        return Helper.result("QueryOEPContractByHash", ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.desc(), VERSION, rs);
    }
}
