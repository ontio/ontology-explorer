package com.github.ontio.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.mapper.CommonMapper;
import com.github.ontio.mapper.NodeInfoOnChainMapper;
import com.github.ontio.mapper.Oep4TxDetailMapper;
import com.github.ontio.mapper.TxDetailMapper;
import com.github.ontio.model.dao.NodeInfoOnChain;
import com.github.ontio.model.dto.Anniversary6thDataDto;
import com.github.ontio.model.dto.GovernanceInfoDto;
import com.github.ontio.service.IActivityDataService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.OntologySDKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class ActivityDataServiceImpl implements IActivityDataService {
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private TxDetailMapper txDetailMapper;
    @Autowired
    private Oep4TxDetailMapper oep4TxDetailMapper;
    @Autowired
    private NodeInfoOnChainMapper nodeInfoOnChainMapper;
    @Autowired
    private OntologySDKService sdk;


    private static final String OLD_STONT_NODE_CONTRACT = "6525d1b24228ef698d14c3214a5bb41f0a9112ea";

    private static final String NEW_STONT_NODE_CONTRACT = "ededc698631ef4eccb3c3b98e7cc73a629be94b8";

    private static final String STONT_CONTRACT = "e9ec04127b57c5686310f8fac28dc617f21f84cd";

    private static final String ZERO_ADDRESS = "AFmseVrdL9f9oyCzZefL9tG6UbvhPbdYzM";

    private static final int CYCLE_221_END_BLOCK = 16794299;


    @Override
    public Anniversary6thDataDto queryAddress6thAnniversaryData(String address) {
        // ONT质押数量
        List<GovernanceInfoDto> stakingInfoByAddress = commonMapper.getStakingInfoByAddress(address);
        long stakingAmount = 0;
        for (GovernanceInfoDto governanceInfoDto : stakingInfoByAddress) {
            Long consensusPos = governanceInfoDto.getConsensusPos();
            Long candidatePos = governanceInfoDto.getCandidatePos();
            stakingAmount = stakingAmount + consensusPos + candidatePos;
        }

        // 是否参与stONT质押
        boolean stakingStOnt = false;
        BigDecimal stOntBalance = new BigDecimal(sdk.getWasmvmOep4AssetBalance(address, STONT_CONTRACT)).divide(ConstantParam.NEW_ONT_DECIMAL, 9, RoundingMode.DOWN);
        List<String> list = Arrays.asList(OLD_STONT_NODE_CONTRACT, NEW_STONT_NODE_CONTRACT);
        BigDecimal mintAmount = oep4TxDetailMapper.selectTransferSumByCondition(ZERO_ADDRESS, address, list, "stONT", null, null);
        BigDecimal unstakeAmount = oep4TxDetailMapper.selectTransferSumByCondition(address, ZERO_ADDRESS, list, "stONT", null, null);
        BigDecimal mintRemainAmount = mintAmount.subtract(unstakeAmount);
        if (BigDecimal.ZERO.compareTo(mintAmount) != 0 && mintRemainAmount.compareTo(stOntBalance) >= 0) {
            stakingStOnt = true;
        }
        // 是否参与221轮 stONT质押
        BigDecimal mintAmountBeforeCycle222 = oep4TxDetailMapper.selectTransferSumByCondition(ZERO_ADDRESS, address, Collections.singletonList(OLD_STONT_NODE_CONTRACT), "stONT", null, CYCLE_221_END_BLOCK);
        boolean stakingStOntIn221 = BigDecimal.ZERO.compareTo(mintAmountBeforeCycle222) < 0;

        // 是否运行节点
        List<String> publicKeyAddressList = Optional.ofNullable(nodePublicKeyAddress.get("1")).orElse(Collections.emptyList());
        Example example = new Example(NodeInfoOnChain.class);
        example.createCriteria().andEqualTo("address", address);
        int count = nodeInfoOnChainMapper.selectCountByExample(example);
        boolean runningNode = count > 0 || publicKeyAddressList.contains(address);

        Anniversary6thDataDto anniversary6thDataDto = new Anniversary6thDataDto();
        anniversary6thDataDto.setStakingAmount(stakingAmount);
        anniversary6thDataDto.setStakingStOnt(stakingStOnt);
        anniversary6thDataDto.setStakingStOntIn221(stakingStOntIn221);
        anniversary6thDataDto.setRunningNode(runningNode);
        return anniversary6thDataDto;
    }

    private LoadingCache<String, List<String>> nodePublicKeyAddress;

    @Autowired
    public void initNodePublicKeyAddress() {
        nodePublicKeyAddress = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5))
                .build(key -> {
                    List<String> addressList = new ArrayList<>();
                    List<NodeInfoOnChain> nodeInfoOnChains = nodeInfoOnChainMapper.selectAll();
                    for (NodeInfoOnChain nodeInfoOnChain : nodeInfoOnChains) {
                        String publicKey = nodeInfoOnChain.getPublicKey();
                        String address = Address.addressFromPubKey(Helper.hexToBytes(publicKey)).toBase58();
                        addressList.add(address);
                    }
                    return addressList;
                });
    }

    @Override
    public Integer queryAddressTxCountInPeriod(String address, Integer startTime, Integer endTime) {
        return txDetailMapper.selectFromTxCountInPeriod(address, startTime, endTime);
    }

    @Override
    public String queryAddressCertainTimeBalance(String address, Integer timestamp) {
        BigDecimal ontBalance = new BigDecimal((String) sdk.getNativeAssetBalance(address).get(ConstantParam.ONT));
        BigDecimal fromAmount = txDetailMapper.selectAssetTransferAmountByAddress(ConstantParam.ONT, address, null, timestamp);
        BigDecimal toAmount = txDetailMapper.selectAssetTransferAmountByAddress(ConstantParam.ONT, null, address, timestamp);
        BigDecimal certainTimeBalance = ontBalance.add(fromAmount).subtract(toAmount);
        return certainTimeBalance.stripTrailingZeros().toPlainString();
    }
}