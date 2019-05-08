package com.github.ontio.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.ontio.OntSdk;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.core.payload.DeployCode;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.utils.ConstantParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/8
 */
@Slf4j
@Service
public class CommonService {

    private final ParamsConfig paramsConfig;

    @Autowired
    public CommonService(ParamsConfig paramsConfig) {
        this.paramsConfig = paramsConfig;
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
                //返回空字符串或JSONArray
                Object object = (Object) ConstantParam.ONT_SDKSERVICE.getConnect().getSmartCodeEvent(height);
                if(object instanceof JSONArray){
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
                contractObj = (JSONObject) ConstantParam.ONT_SDKSERVICE.getConnect().getContractJson(contractHash);
                contractObj.remove("Code");
                contractObj.put("ContractHash", contractHash);
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




}
