package com.github.ontio.txPush;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.ontio.mapper.UserAddressMapper;
import com.github.ontio.model.dao.TxDetail;
import com.github.ontio.txPush.model.PushEmailDto;
import com.github.ontio.txPush.model.PushStrategyEnum;
import com.github.ontio.txPush.model.PushUserAddressInfoDto;
import com.github.ontio.utils.ConstantParam;
import com.github.ontio.utils.Helper;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouq
 */
@Service
@Slf4j
public class TransferTransactionPush implements DisruptorEventPublisher, EventHandler<DisruptorEvent> {

    private static final String USERADDRCACHE_KEY = "userAddress";
    private static final String CHANNEL_EXPLORER = "explorer";
    private static final String CHANNEL_ONTO = "onto";

    private LoadingCache<String, Map<String, List<PushUserAddressInfoDto>>> userAddressCache;

    private final UserAddressMapper userAddressMapper;
    private final EmailService emailService;
    private final OntoService ontoService;

    private RingBuffer<DisruptorEvent> ringBuffer;

    @Override
    public RingBuffer<DisruptorEvent> getRingBuffer() {
        return ringBuffer;
    }

    @Autowired
    public TransferTransactionPush(UserAddressMapper userAddressMapper, EmailService emailService, OntoService ontoService) {
        Disruptor<DisruptorEvent> disruptor = createDisruptor(65536, ProducerType.SINGLE);
        disruptor.handleEventsWith(this);
        disruptor.start();
        this.ringBuffer = disruptor.getRingBuffer();
        this.userAddressMapper = userAddressMapper;
        this.emailService = emailService;
        this.ontoService = ontoService;
    }

    @Override
    public void onEvent(DisruptorEvent disruptorEvent, long sequence, boolean endOfBatch) {
        //return;
        Object event = disruptorEvent.getEvent();
        if (event instanceof TxDetail) {
            TxDetail txDetail = (TxDetail) event;
            log.info("disruptor consumer receive tx:{}", JSONObject.toJSONString(txDetail));
            String fromAddress = txDetail.getFromAddress();
            String toAddress = txDetail.getToAddress();
            if (fromAddress.equals(toAddress)) {
                return;
            }
            Map<String, List<PushUserAddressInfoDto>> cacheMap = userAddressCache.get(USERADDRCACHE_KEY);
            List<PushUserAddressInfoDto> fromAddrPushUserAddressInfoDtos = cacheMap.get(fromAddress);
            if (fromAddrPushUserAddressInfoDtos != null) {
                fromAddrPushUserAddressInfoDtos.forEach(dto -> {
                    handleWithdrawTransferTx(txDetail, dto);
                });
            }
            List<PushUserAddressInfoDto> toAddrPushUserAddressInfoDtos = cacheMap.get(toAddress);
            if (toAddrPushUserAddressInfoDtos != null) {
                toAddrPushUserAddressInfoDtos.forEach(dto -> {
                    handleDepositTransferTx(txDetail, dto);
                });
            }
        }

    }


    private void handleWithdrawTransferTx(TxDetail txDetail, PushUserAddressInfoDto dto) {
        if (dto.getStrategy() == PushStrategyEnum.DepositPush.value()) {
            return;
        }
        PushEmailDto pushEmailDto = new PushEmailDto();
        if (ConstantParam.ASSET_NAME_ONT.equals(txDetail.getAssetName())
                || ConstantParam.ASSET_NAME_ONG.equals(txDetail.getAssetName())) {
            pushEmailDto = PushEmailDto.buildDto(dto, txDetail, PushEmailDto.WITHDRAW);
        } else {
            if (dto.getIncludeOepToken()) {
                pushEmailDto = PushEmailDto.buildDto(dto, txDetail, PushEmailDto.WITHDRAW);
            } else {
                return;
            }
        }
        sendNotification(pushEmailDto);
    }


    private void handleDepositTransferTx(TxDetail txDetail, PushUserAddressInfoDto dto) {
        if (dto.getStrategy() == PushStrategyEnum.WithdrawPush.value()) {
            return;
        }
        PushEmailDto pushEmailDto = new PushEmailDto();
        if (ConstantParam.ASSET_NAME_ONT.equals(txDetail.getAssetName())
                || ConstantParam.ASSET_NAME_ONG.equals(txDetail.getAssetName())) {
            pushEmailDto = PushEmailDto.buildDto(dto, txDetail, PushEmailDto.DEPOSIT);
        } else {
            if (dto.getIncludeOepToken()) {
                pushEmailDto = PushEmailDto.buildDto(dto, txDetail, PushEmailDto.DEPOSIT);
            } else {
                return;
            }
        }
        sendNotification(pushEmailDto);
    }

    private void sendNotification(PushEmailDto pushEmailDto) {
        if (CHANNEL_EXPLORER.equals(pushEmailDto.getChannel()) && Helper.isNotEmptyOrNull(pushEmailDto.getEmail())) {
            emailService.sendTransferTxInfoEmail(pushEmailDto);
        } else if (CHANNEL_ONTO.equals(pushEmailDto.getChannel())) {
            String assetName = pushEmailDto.getAssetName();
            if (ConstantParam.ASSET_NAME_ONG.equals(assetName) || ConstantParam.ASSET_NAME_ONT.equals(assetName)) {
                if (new BigDecimal(pushEmailDto.getAmount()).compareTo(pushEmailDto.getAmountThreshold()) >= 0) {
                    ontoService.sendTransferTxInfo2OntoService(pushEmailDto);
                    if (Helper.isNotEmptyOrNull(pushEmailDto.getEmail())) {
                        emailService.sendTransferTxInfoEmail(pushEmailDto);
                    }
                }
            } else {
                ontoService.sendTransferTxInfo2OntoService(pushEmailDto);
                if (Helper.isNotEmptyOrNull(pushEmailDto.getEmail())) {
                    emailService.sendTransferTxInfoEmail(pushEmailDto);
                }
            }
        }
    }


    @Autowired
    private void setCache() {
        userAddressCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(key -> {
                    return getUserAddress();
                });
    }

    private Map<String, List<PushUserAddressInfoDto>> getUserAddress() {
        Map<String, List<PushUserAddressInfoDto>> map = new HashMap<>();
        List<PushUserAddressInfoDto> userAddressInfoDtos = userAddressMapper.selectUserAddressInfo();
        userAddressInfoDtos.forEach(item -> {
            if (map.containsKey(item.getAddress())) {
                List<PushUserAddressInfoDto> list = map.get(item.getAddress());
                list.add(item);
            } else {
                map.put(item.getAddress(), new ArrayList<PushUserAddressInfoDto>() {{
                    add(item);
                }});
            }
        });
        return map;
    }


}
