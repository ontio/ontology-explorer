package com.github.ontio.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.core.payload.DeployCode;
import com.github.ontio.mapper.*;
import com.github.ontio.model.common.BatchBlockDto;
import com.github.ontio.model.dao.*;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.utils.ConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/8
 */
@Slf4j
@Service
public class CommonService {


    private final ParamsConfig paramsConfig;
    private final CurrentMapper currentMapper;
    private final OntidTxDetailMapper ontidTxDetailMapper;
    private final TxDetailMapper txDetailMapper;
    private final Oep4TxDetailMapper oep4TxDetailMapper;
    private final Oep5TxDetailMapper oep5TxDetailMapper;
    private final Oep8TxDetailMapper oep8TxDetailMapper;
    private final TxEventLogMapper txEventLogMapper;
    private final Oep5DragonMapper oep5DragonMapper;
    private final BlockMapper blockMapper;
    private final ContractMapper contractMapper;
    private final TxDetailDailyMapper txDetailDailyMapper;

    @Autowired
    public CommonService(TxDetailMapper txDetailMapper, ParamsConfig paramsConfig, CurrentMapper currentMapper, OntidTxDetailMapper ontidTxDetailMapper,
                         Oep4TxDetailMapper oep4TxDetailMapper, Oep5TxDetailMapper oep5TxDetailMapper, Oep8TxDetailMapper oep8TxDetailMapper, TxEventLogMapper txEventLogMapper,
                         BlockMapper blockMapper, Oep5DragonMapper oep5DragonMapper, ContractMapper contractMapper, TxDetailDailyMapper txDetailDailyMapper) {
        this.txDetailMapper = txDetailMapper;
        this.paramsConfig = paramsConfig;
        this.currentMapper = currentMapper;
        this.ontidTxDetailMapper = ontidTxDetailMapper;
        this.oep4TxDetailMapper = oep4TxDetailMapper;
        this.oep5TxDetailMapper = oep5TxDetailMapper;
        this.oep8TxDetailMapper = oep8TxDetailMapper;
        this.txEventLogMapper = txEventLogMapper;
        this.blockMapper = blockMapper;
        this.oep5DragonMapper = oep5DragonMapper;
        this.contractMapper = contractMapper;
        this.txDetailDailyMapper = txDetailDailyMapper;
    }

    /**
     * 批量插入记录
     *
     * @param tHeight
     * @param batchBlockDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchInsertDb(int tHeight, BatchBlockDto batchBlockDto) {
        //插入tbl_tx_detail表
        if (batchBlockDto.getTxDetails().size() > 0) {
            int count = batchBlockDto.getTxDetails().size();
            if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
                for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
                    List<TxDetail> list = batchBlockDto.getTxDetails().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT, (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count : (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
                    if (list.size() > 0) {
                        txDetailMapper.batchInsert(list);
                    }
                }
            } else {
                txDetailMapper.batchInsert(batchBlockDto.getTxDetails());
            }
        }
        //插入tbl_tx_detail_daily表
        if (batchBlockDto.getTxDetailDailys().size() > 0) {
            int count = batchBlockDto.getTxDetailDailys().size();
            if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
                for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
                    List<TxDetailDaily> list = batchBlockDto.getTxDetailDailys().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT, (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count : (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
                    if (list.size() > 0) {
                        txDetailDailyMapper.batchInsert(list);
                    }
                }
            } else {
                txDetailDailyMapper.batchInsert(batchBlockDto.getTxDetailDailys());
            }
        }
        //插入tbl_tx_eventlog表
        if (batchBlockDto.getTxEventLogs().size() > 0) {
            int count = batchBlockDto.getTxEventLogs().size();
            if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
                for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
                    List<TxEventLog> list = batchBlockDto.getTxEventLogs().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT, (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count : (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
                    if (list.size() > 0) {
                        txEventLogMapper.batchInsert(list);
                    }
                }
            } else {
                txEventLogMapper.batchInsert(batchBlockDto.getTxEventLogs());
            }
        }
        //插入tbl_ontid_tx_detail表
        if (batchBlockDto.getOntidTxDetails().size() > 0) {
            int count = batchBlockDto.getOntidTxDetails().size();
            if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
                for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
                    List<OntidTxDetail> list = batchBlockDto.getOntidTxDetails().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT, (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count : (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
                    if (list.size() > 0) {
                        ontidTxDetailMapper.batchInsert(list);
                    }
                }
            } else {
                ontidTxDetailMapper.batchInsert(batchBlockDto.getOntidTxDetails());
            }
        }
        //插入tbl_oep4_tx_detail表
        if (batchBlockDto.getOep4TxDetails().size() > 0) {
            int count = batchBlockDto.getOep4TxDetails().size();
            if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
                for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
                    List<Oep4TxDetail> list = batchBlockDto.getOep4TxDetails().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT, (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count : (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
                    if (list.size() > 0) {
                        oep4TxDetailMapper.batchInsert(list);
                    }
                }
            } else {
                oep4TxDetailMapper.batchInsert(batchBlockDto.getOep4TxDetails());
            }
        }
        //插入tbl_oep5_tx_detail表
        if (batchBlockDto.getOep5TxDetails().size() > 0) {
            int count = batchBlockDto.getOep5TxDetails().size();
            if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
                for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
                    List<Oep5TxDetail> list = batchBlockDto.getOep5TxDetails().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT, (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count : (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
                    if (list.size() > 0) {
                        oep5TxDetailMapper.batchInsert(list);
                    }
                }
            } else {
                oep5TxDetailMapper.batchInsert(batchBlockDto.getOep5TxDetails());
            }
        }
        //插入tbl_oep5_dragon表
        if (batchBlockDto.getOep5Dragons().size() > 0) {
            int count = batchBlockDto.getOep5Dragons().size();
            if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
                for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
                    List<Oep5Dragon> list = batchBlockDto.getOep5Dragons().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT, (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count : (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
                    if (list.size() > 0) {
                        oep5DragonMapper.batchInsert(list);
                    }
                }
            } else {
                oep5DragonMapper.batchInsert(batchBlockDto.getOep5Dragons());
            }
        }
        //插入tbl_oep8_tx_detail表
        if (batchBlockDto.getOep8TxDetails().size() > 0) {
            int count = batchBlockDto.getOep8TxDetails().size();
            if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
                for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
                    List<Oep8TxDetail> list = batchBlockDto.getOep8TxDetails().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT, (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count : (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
                    if (list.size() > 0) {
                        oep8TxDetailMapper.batchInsert(list);
                    }
                }
            } else {
                oep8TxDetailMapper.batchInsert(batchBlockDto.getOep8TxDetails());
            }
        }
        //插入tbl_contract表
        if (batchBlockDto.getContracts().size() > 0) {
            contractMapper.batchInsert(batchBlockDto.getContracts());
        }
        //插入tbl_block表
        if (batchBlockDto.getBlocks().size() > 0) {
            blockMapper.batchInsert(batchBlockDto.getBlocks());
        }
        //更新current表
        List<Current> currents = currentMapper.selectAll();
        int txCount = currents.get(0).getTxCount();
        int ontIdCount = currents.get(0).getOntidCount();
        int ontIdTxCount = currents.get(0).getOntidTxCount();
        Current current = Current.builder()
                .blockHeight(tHeight)
                .txCount(txCount + ConstantParam.BATCHBLOCK_TX_COUNT)
                .ontidCount(ontIdCount + ConstantParam.BATCHBLOCK_ONTID_COUNT)
                .ontidTxCount(ontIdTxCount + batchBlockDto.getOntidTxDetails().size())
                .build();
        currentMapper.update(current);
    }


    /**
     * switch to another node and initialize ONT_SDKSERVICE object
     * when the master node have an exception
     */
    public void switchNode() {
        ConstantParam.MASTERNODE_INDEX++;
        if (ConstantParam.MASTERNODE_INDEX >= paramsConfig.NODE_COUNT) {
            ConstantParam.MASTERNODE_INDEX = 0;
        }
        ConstantParam.MASTERNODE_RESTFULURL = ConstantParam.NODE_RESTFULURLLIST.get(ConstantParam.MASTERNODE_INDEX);
        log.warn("####switch node restfulurl to {}####", ConstantParam.MASTERNODE_RESTFULURL);

        OntSdk wm = OntSdk.getInstance();
        wm.setRestful(ConstantParam.MASTERNODE_RESTFULURL);
        ConstantParam.ONT_SDKSERVICE = wm;
    }

    /**
     * get the the blockheight of the ontology blockchain
     *
     * @return
     * @throws Exception
     */
    public int getRemoteBlockHeight() throws Exception {
        int remoteHeight = 0;
        int tryTime = 1;
        while (true) {
            try {
                remoteHeight = ConstantParam.ONT_SDKSERVICE.getConnect().getBlockHeight();
                break;
            } catch (ConnectorException ex) {
                log.error("getBlockHeight error, try again...restful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % paramsConfig.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
            } catch (IOException e) {
                log.error("get blockheight thread can't work,error {} ", e);
                throw new Exception(e);
            }
        }

        return remoteHeight;
    }


    /**
     * get the block by height
     *
     * @return
     * @throws Exception
     */
    public JSONObject getBlockJsonByHeight(int height) throws Exception {
        JSONObject blockObj = new JSONObject();
        int tryTime = 1;
        while (true) {
            try {
                blockObj = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getBlockJson(height);
                break;
            } catch (ConnectorException ex) {
                log.error("getBlockJsonByHeight error, try again...restful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % paramsConfig.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
            } catch (IOException ex) {
                log.error("getBlockJsonByHeight thread can't work,error {} ", ex);
                throw new Exception(ex);
            }
        }

        return blockObj;
    }


    /**
     * get transaction event log by height
     *
     * @return
     * @throws Exception
     */
    public JSONArray getTxEventLogsByHeight(int height) throws Exception {
        JSONArray txEventLogArray = new JSONArray();
        int tryTime = 1;
        while (true) {
            try {
                //无交易返回空字符串，有交易返回JSONArray
                Object object = ConstantParam.ONT_SDKSERVICE.getConnect().getSmartCodeEvent(height);
                if (object instanceof JSONArray) {
                    txEventLogArray = (JSONArray) object;
                }
                break;
            } catch (ConnectorException ex) {
                log.error("getTxEventLogsByHeight error, try again...restful:{},error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % paramsConfig.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
            } catch (IOException ex) {
                log.error("getTxEventLogsByHeight thread can't work,error {} ", ex);
                throw new Exception(ex);
            }
        }

        return txEventLogArray;
    }


    /**
     * get oep5 total supply
     *
     * @return
     * @throws Exception
     */
    public Long getOep5TotalSupply(String contractAddress) {

        ConstantParam.ONT_SDKSERVICE.neovm().oep5().setContractAddress(contractAddress);
        Long totalSupply = 0L;
        int tryTime = 1;
        while (true) {
            try {
                totalSupply = Long.valueOf(ConstantParam.ONT_SDKSERVICE.neovm().oep5().queryTotalSupply());
                break;
            } catch (ConnectorException ex) {
                log.error("getOep5TotalSupply error, try again...restful: {}, error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % paramsConfig.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            } catch (Exception ex) {
                log.error("getOep5TotalSupply error {} ", ex);
                break;
            }
        }
        return totalSupply;
    }


    /**
     * get the event log by txhash
     *
     * @return
     * @throws Exception
     */
    public JSONObject getEventLogByTxHash(String txHash) throws Exception {

        JSONObject eventObj = new JSONObject();
        int tryTime = 1;
        while (true) {
            try {
                eventObj = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getSmartCodeEvent(txHash);
                break;
            } catch (ConnectorException ex) {
                log.error("getEventLogByTxHash error, try again...restful: {}, error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % paramsConfig.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
            } catch (IOException ex) {
                log.error("getEventLogByTxHash thread can't work,error {} ", ex);
                throw new Exception(ex);
            }
        }

        return eventObj;
    }


    /**
     * 根据部署合约交易hash获取链上合约信息
     *
     * @param txHash
     * @return
     * @throws Exception
     */
    public JSONObject getContractInfoByTxHash(String txHash) throws Exception {

        JSONObject contractObj = new JSONObject();
        contractObj.put("Name", "");
        contractObj.put("Description", "");
        contractObj.put("ContractHash", "");
        int tryTime = 1;
        while (true) {
            try {
                DeployCode deployCodeObj = (DeployCode) ConstantParam.ONT_SDKSERVICE.getConnect().getTransaction(txHash);
                //根据code转成合约hash
                String code = Helper.toHexString(deployCodeObj.code);
                String contractHash = Address.AddressFromVmCode(code).toHexString();
                contractObj.put("ContractHash", contractHash);
                //先设置合约hash，某些合约信息在链上找不到
                try {
                    contractObj = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getContractJson(contractHash);
                    contractObj.remove("Code");
                    contractObj.put("ContractHash", contractHash);
                } catch (Exception e) {
                    log.error("getContractJson error...", e);
                    break;
                }
                break;
            } catch (ConnectorException ex) {
                log.error("getContractInfoByTxHash error, try again...restful: {}, error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % paramsConfig.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    Thread.sleep(1000);
                    continue;
                }
            } catch (Exception ex) {
                log.error("getContractInfoByTxHash thread can't work,error {} ", ex);
                break;
            }
        }
        return contractObj;
    }

    /**
     * 根据oep8的tokenId获取总量
     *
     * @param tokenId
     * @return
     */
    public Long getOep8TotalSupply(String tokenId) {

        Long totalSupply = 0L;
        int tryTime = 1;
        while (true) {
            try {
                totalSupply = Long.valueOf(ConstantParam.ONT_SDKSERVICE.neovm().oep8().queryTotalSupply(Helper.hexToBytes(tokenId)));
                break;
            } catch (ConnectorException ex) {
                log.error("getOep8TotalSupply error, try again...restful: {}, error:", ConstantParam.MASTERNODE_RESTFULURL, ex);
                if (tryTime % paramsConfig.NODE_INTERRUPTTIME_MAX == 0) {
                    switchNode();
                    tryTime++;
                    continue;
                } else {
                    tryTime++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            } catch (Exception ex) {
                log.error("getOep8TotalSupply thread can't work,error {} ", ex);
                break;
            }
        }
        return totalSupply;
    }


}
