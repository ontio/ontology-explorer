package com.github.ontio.utils;

import com.github.ontio.common.Helper;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.sdk.exception.SDKException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


@Slf4j
@Component
public class Web3jSdkUtil {

    @Autowired
    private ParamsConfig paramsConfig;

    private Web3j web3jSingleton;

    // 单例模式
    public Web3j getWeb3jSingleton() {
        if (web3jSingleton == null) {
            synchronized (Web3jSdkUtil.class) {
                if (web3jSingleton == null) {
                    String eth_web3J_url = paramsConfig.ETH_WEB3J_URL;
                    HttpService httpService = new HttpService(eth_web3J_url);
                    web3jSingleton = Web3j.build(httpService);
                }
            }
        }
        return web3jSingleton;
    }

    public String sendPreTransaction(String contract, String name, String address, List<Type> params) throws Exception {
        Web3j web3j = getWeb3jSingleton();
        Transaction ethCallTransaction = createPreTransaction(contract, name, address, params);
        EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        web3j.shutdown();
        String result = ethCall.getResult();
        return result;
    }

    public Transaction createPreTransaction(String contract, String name, String address, List<Type> params) {
        List<TypeReference<?>> typeReferences = Arrays.asList(new TypeReference<Type<String>>() {
        });
        Function function = new Function(name, params, typeReferences);
        String transactionData = FunctionEncoder.encode(function);

        Transaction ethCallTransaction = Transaction.createEthCallTransaction(address, contract, transactionData);
        return ethCallTransaction;
    }


    private List<RlpType> asRlpValues(RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> result = new ArrayList();
        result.add(RlpString.create(rawTransaction.getNonce()));
        result.add(RlpString.create(rawTransaction.getGasPrice()));
        result.add(RlpString.create(rawTransaction.getGasLimit()));
        String to = rawTransaction.getTo();
        if (to != null && to.length() > 0) {
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(rawTransaction.getValue()));
        byte[] data = Numeric.hexStringToByteArray(rawTransaction.getData());
        result.add(RlpString.create(data));
        if (signatureData != null) {
            result.add(RlpString.create(signatureData.getV()));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }
        return result;
    }


    public BigInteger getLatestBlockNumber() throws IOException {
        Web3j web3j = getWeb3jSingleton();
        EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
        web3j.shutdown();
        return ethBlockNumber.getBlockNumber();
    }


    public TransactionReceipt getReceiptByHash(String hash) throws IOException {
        Web3j web3j = getWeb3jSingleton();
        EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(hash).send();
        web3j.shutdown();
        Optional<TransactionReceipt> transactionReceiptOptional = receipt.getTransactionReceipt();
        if (transactionReceiptOptional.isPresent()) {
            TransactionReceipt transactionReceipt = transactionReceiptOptional.get();
            return transactionReceipt;
        } else {
            return null;
        }
    }

    public BigDecimal queryEthBalance(String address) throws IOException {
        Web3j web3j = getWeb3jSingleton();
        BigInteger ethBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        web3j.shutdown();
        return new BigDecimal(ethBalance);
    }


    public String queryInputDataByTxHash(String txHash) throws IOException {
        Web3j web3j = getWeb3jSingleton();
        EthTransaction send = web3j.ethGetTransactionByHash(txHash).send();
        String input = send.getResult().getInput();
        return input;
    }

    public List<Type> sendPreTransactionAndDecode(Web3j web3j, String contract, String name, String address, List<Type> params, List<TypeReference<?>> outputParameters) throws Exception {
        try {
            Function function = new Function(name, params, outputParameters);
            String transactionData = FunctionEncoder.encode(function);
            Transaction ethCallTransaction = Transaction.createEthCallTransaction(address, contract, transactionData);
            EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
            web3j.shutdown();
            Response.Error error = ethCall.getError();
            if (error != null) {
                String errorMessage = error.getMessage();
                log.error("error when invoke contract:{},name:{},error:{}", contract, name, errorMessage);
            }
            List<Type> result = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            return result;
        } catch (Exception e) {
            web3j.shutdown();
            throw e;
        }
    }

    public BigDecimal queryOrc20Balance(String fromAddress, String contractAddress) {
        Web3j web3j = getWeb3jSingleton();
        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        outputParameters.add(ConstantParam.TYPE_REFERENCE_UINT256);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);
        EthCall ethCall;
        BigDecimal bigDecimal = BigDecimal.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());

            bigDecimal = new BigDecimal(results.get(0).getValue().toString());
        } catch (IOException e) {
            log.error("address:{} get orc20 balance error:{}", fromAddress, e);
        }
        return bigDecimal;
    }


    public static String ontAddrToEthAddr(String ontAddr) throws SDKException {
        com.github.ontio.common.Address address = com.github.ontio.common.Address.decodeBase58(ontAddr);
        String reverse = Helper.reverse(address.toHexString());
        return ConstantParam.EVM_ADDRESS_PREFIX + reverse;
    }

    public static String EthAddrToOntAddr(String ethAddr) {
        if (ethAddr.startsWith(ConstantParam.EVM_ADDRESS_PREFIX)) {
            ethAddr = ethAddr.substring(2);
        }
        com.github.ontio.common.Address parse = com.github.ontio.common.Address.parse(ethAddr);
        return parse.toBase58();
    }

    public String getCode(String address) {
        String code = ConstantParam.EMPTY;
        Web3j web3j = getWeb3jSingleton();
        try {
            EthGetCode ethGetCode = web3j.ethGetCode(address, DefaultBlockParameterName.LATEST).send();
//            Response.Error error = ethGetCode.getError();
//            if (error != null) {
//                String errorMessage = error.getMessage();
//                log.error("eth getCode error, address:{},error:{}", address, errorMessage);
//            }
            code = ethGetCode.getCode();
        } catch (IOException e) {
            log.error("getCode error....", e);
        }
        return code;
    }

    public String getOrcTokenSymbol(String contract) {
        String symbol = null;
        Web3j web3j = getWeb3jSingleton();
        try {
            List<Type> params = Collections.emptyList();
            List<TypeReference<?>> outputParameters = Collections.singletonList(ConstantParam.TYPE_REFERENCE_UTF8);
            List<Type> result = sendPreTransactionAndDecode(web3j, contract, ConstantParam.FUN_SYMBOL, Address.DEFAULT.getValue(), params, outputParameters);
            symbol = result.get(0).getValue().toString();
        } catch (Exception e) {
            log.error("getOrcTokenSymbol error....", e);
        }
        return symbol;
    }

    public Integer getOrc20Decimals(String contract) {
        Integer decimals = null;
        Web3j web3j = getWeb3jSingleton();
        try {
            List<Type> params = Collections.emptyList();
            List<TypeReference<?>> outputParameters = Collections.singletonList(ConstantParam.TYPE_REFERENCE_UINT8);
            List<Type> result = sendPreTransactionAndDecode(web3j, contract, ConstantParam.FUN_DECIMALS, Address.DEFAULT.getValue(), params, outputParameters);
            decimals = ((BigInteger) result.get(0).getValue()).intValue();
        } catch (Exception e) {
            log.error("getOrcTokenSymbol error....", e);
        }
        return decimals;
    }

}
