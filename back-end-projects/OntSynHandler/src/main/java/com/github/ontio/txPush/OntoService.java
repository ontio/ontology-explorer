package com.github.ontio.txPush;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.ontio.txPush.model.PushConfig;
import com.github.ontio.txPush.model.PushEmailDto;
import com.github.ontio.utils.Helper;
import com.github.ontio.utils.HmacSha256Base64Util;
import com.github.ontio.utils.HttpClientUtil;
import com.github.ontio.utils.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/5/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OntoService {

    private final PushConfig pushConfig;

    private static final String HTTPHEADER_AUTH = "Authorization";
    private static final String HMAC_ONTO_SCHEMA = "Onto";
    private static final String HMAC_SIGN_SEPARATOR = ":";
    private static final String TX_NOTIFICATION_URI = "/v2/ontoservice/monitor_txs/notifications";

    private LoadingCache<String, Integer> notificationCountCache;

    @Autowired
    public void setCache() {
        notificationCountCache = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, Integer>() {
                    @Override
                    public long expireAfterCreate(String key, Integer time, long currentTime) {
                        LocalDateTime localDateTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
                        long endTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond() * 1000L;
                        return (endTime - System.currentTimeMillis()) * 1000000L;
                    }

                    @Override
                    public long expireAfterUpdate(String key, Integer time, long currentTime,
                                                  long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(String key, Integer time, long currentTime,
                                                long currentDuration) {
                        return currentDuration;
                    }
                })
                .build(key -> {
                    return 1;
                });
    }


    /**
     * send transfer tx notification 2 ontoservice
     *
     * @param pushEmailDto
     */
    public void sendTransferTxInfo2OntoService(PushEmailDto pushEmailDto) {
        String ontId = pushEmailDto.getOntId();
        int time = notificationCountCache.get(ontId);
        if (time > pushConfig.PERUPUSH_PERDAY_UPPERLIMIT) {
            return;
        }
        String url = pushConfig.ONTOSERVICE_HOST + TX_NOTIFICATION_URI;
        try {
            TreeMap<String, Object> bodyMap = new TreeMap<>();
            bodyMap.put("ont_id", pushEmailDto.getOntId());
            bodyMap.put("user_name", pushEmailDto.getUserName());
            bodyMap.put("user_address", pushEmailDto.getUserAddress());
            bodyMap.put("tx_des", pushEmailDto.getTxDes());
            bodyMap.put("tx_hash", pushEmailDto.getTxHash());
            bodyMap.put("asset_name", pushEmailDto.getAssetName());
            bodyMap.put("amount", pushEmailDto.getAmount());
            bodyMap.put("time", pushEmailDto.getTime());
            bodyMap.put("to_address", pushEmailDto.getToAddress());
            bodyMap.put("from_address", pushEmailDto.getFromAddress());
            bodyMap.put("amount_threshold", pushEmailDto.getAmountThreshold());
            bodyMap.put("note", pushEmailDto.getNote());
            bodyMap.put("channel", pushEmailDto.getChannel());
            bodyMap.put("email", pushEmailDto.getEmail());
            Map<String, Object> headerMap = makeHmacReqHeader("POST", TX_NOTIFICATION_URI, "", bodyMap);
            String response = HttpClientUtil.postRequest(url, JacksonUtil.beanToJSonStr(pushEmailDto), headerMap);
            log.info("ontoservice response:{}", response);
            if (JSONObject.parseObject(response).getInteger("Error") == 0) {
                time++;
                notificationCountCache.put(ontId, time);
            }
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
    }


    private Map<String, Object> makeHmacReqHeader(String method, String requestPath, String requestQueryStr, TreeMap bodyMap) {
        Map<String, Object> headerMap = new HashMap<>();
        String timeStampStr = String.valueOf(System.currentTimeMillis());
        try {
            String sign = HmacSha256Base64Util.sign(timeStampStr, method, requestPath, requestQueryStr, pushConfig.ONTOSERVICE_APPID, pushConfig.ONTOSERVICE_APPSECRET, bodyMap);
            String authorizationStr = HMAC_ONTO_SCHEMA
                    + HMAC_SIGN_SEPARATOR
                    + pushConfig.ONTOSERVICE_APPID
                    + HMAC_SIGN_SEPARATOR
                    + timeStampStr
                    + HMAC_SIGN_SEPARATOR
                    + sign;
            headerMap.put(HTTPHEADER_AUTH, authorizationStr);
            log.info("authorizationStr:{}", authorizationStr);
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
        return headerMap;
    }

}
