package com.github.ontio.txPush;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.ontio.txPush.model.PushConfig;
import com.github.ontio.txPush.model.PushConstant;
import com.github.ontio.txPush.model.PushEmailDto;
import com.github.ontio.utils.Helper;
import com.github.ontio.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/26
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class EmailService {

    private final PushConfig pushConfig;

    private LoadingCache<String, Integer> emailCountCache;

    @Autowired
    public void setCache() {
        emailCountCache = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, Integer>() {
                    @Override
                    public long expireAfterCreate(String key, Integer time, long currentTime) {
                        Calendar endCalendar = new GregorianCalendar();
                        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
                        endCalendar.set(Calendar.MINUTE, 59);
                        endCalendar.set(Calendar.SECOND, 59);
                        long endTime = endCalendar.getTimeInMillis();
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


    public void sendTransferTxInfoEmail(PushEmailDto pushEmailDto) {
        log.info("{}...email:{},ontId:{},txDetail:{}", Helper.currentMethod(), pushEmailDto.getEmail(), pushEmailDto.getOntId(), JSONObject.toJSONString(pushEmailDto));
        int time = emailCountCache.get(pushEmailDto.getEmail());
        if (time > pushConfig.PEREMAIL_PERDAY_UPPERLIMIT) {
            log.warn("email:{} exceed one day max time:{}", pushEmailDto.getEmail(), pushConfig.PEREMAIL_PERDAY_UPPERLIMIT);
            return;
        }

        String xsmtpapi = convertVerificationCode(pushEmailDto);
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("apiUser", pushConfig.SC_EMAIL_APIUSER));
        params.add(new BasicNameValuePair("apiKey", pushConfig.SC_EMAIL_APIKEY));
        params.add(new BasicNameValuePair("xsmtpapi", xsmtpapi));
        params.add(new BasicNameValuePair("templateInvokeName", pushConfig.SC_EMAIL_TX_TEMPLATE));
        params.add(new BasicNameValuePair("from", pushConfig.SC_EMAIL_SENDER));
        params.add(new BasicNameValuePair("fromName", pushConfig.SC_EMAIL_SENDERNAME));
        try {
            HttpClientUtil.postRequest(PushConstant.SC_EMAIL_TEMPLATEMAIL_URL, params, new HashMap<>());
            time++;
            emailCountCache.put(pushEmailDto.getEmail(), time);
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
    }


    private String convertVerificationCode(PushEmailDto pushEmailDto) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+0:00"));
        JSONObject sub = new JSONObject();
        sub.put("%ontId%", Arrays.asList(pushEmailDto.getOntId()));
        sub.put("%address%", Arrays.asList(pushEmailDto.getUserAddress()));
        sub.put("%fromAddress%", Arrays.asList(pushEmailDto.getFromAddress()));
        sub.put("%hash%", Arrays.asList(pushEmailDto.getTxHash()));
        sub.put("%assetName%", Arrays.asList(pushEmailDto.getAssetName()));
        sub.put("%time%", Arrays.asList(sdf.format(new Date(pushEmailDto.getTime() * 1000L))));
        sub.put("%toAddress%", Arrays.asList(pushEmailDto.getToAddress()));
        sub.put("%amount%", Arrays.asList(pushEmailDto.getAmount()));

        JSONObject ret = new JSONObject();
        ret.put("to", Arrays.asList(pushEmailDto.getEmail()));
        ret.put("sub", sub);
        return ret.toString();
    }


}
