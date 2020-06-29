package com.github.ontio.txPush.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/26
 */
@Component
public class PushConfig {


    @Value("${sendcloud.email.apiUser}")
    public String SC_EMAIL_APIUSER;

    @Value("${sendcloud.email.apiKey}")
    public String SC_EMAIL_APIKEY;

    @Value("${sendcloud.email.sender}")
    public String SC_EMAIL_SENDER;

    @Value("${sendcloud.email.senderName}")
    public String SC_EMAIL_SENDERNAME;

    @Value("${sendcloud.email.transaction.template}")
    public String SC_EMAIL_TX_TEMPLATE;

    @Value("${perEmail.perDay.upperLimit}")
    public int PEREMAIL_PERDAY_UPPERLIMIT;

    @Value("${perUPush.perDay.upperLimit}")
    public int PERUPUSH_PERDAY_UPPERLIMIT;

    @Value("${ontoservice.host}")
    public String ONTOSERVICE_HOST;

    @Value("${ontoservice.appId}")
    public String ONTOSERVICE_APPID;

    @Value("${ontoservice.appSecret}")
    public String ONTOSERVICE_APPSECRET;

}
