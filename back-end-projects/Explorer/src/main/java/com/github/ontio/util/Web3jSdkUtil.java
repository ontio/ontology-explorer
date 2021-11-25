package com.github.ontio.util;

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
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


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


    public String queryPayloadByTxHash(String txHash) throws IOException {
        Web3j web3j = getWeb3jSingleton();
        EthTransaction send = web3j.ethGetTransactionByHash(txHash).send();
        String input = send.getResult().getInput();
        return input;
    }

    public BigDecimal queryOrc20Balance(String fromAddress, String contractAddress) {
        Web3j web3j = getWeb3jSingleton();
        String methodName = ConstantParam.FUN_BALANCE_OF;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
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


}
