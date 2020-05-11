package com.github.ontio.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.TreeMap;

@Slf4j
public class HmacSha256Base64Util {

    private static final String ALGORITHM_HMAC_SHA256 = "HmacSHA256";

    private static final String URI_QUESTION = "?";

    private static final String CHARSET_UTF8 = "UTF-8";


    /**
     * Signing a Message.<br/>
     * <p>
     * using: Hmac SHA256 + base64
     *
     * @param timestamp   the number of seconds since Unix Epoch in UTC. Decimal values are allowed.
     *                    eg: 2018-03-08T10:59:25.789Z
     * @param method      eg: POST
     * @param requestPath eg: /orders
     * @param queryString eg: before=2&limit=30
     *                    //     * @param body        json string, eg: {"product_id":"BTC-USD-0309","order_id":"377454671037440"}
     * @param secretKey   user's secret key eg: E65791902180E9EF4510DB6A77F6EBAE
     * @return signed string   eg: TO6uwdqz+31SIPkd4I+9NiZGmVH74dXi+Fd5X0EzzSQ=
     * @throws CloneNotSupportedException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public static String sign(String timestamp, String method, String requestPath,
                              String queryString, String appKey, String secretKey, TreeMap<String, String> body)
            throws CloneNotSupportedException, InvalidKeyException, UnsupportedEncodingException {
        String preHash = preHash(timestamp, method, requestPath, queryString, appKey, body);
        log.info("origin sign data:{}", preHash);
        byte[] secretKeyBytes = secretKey.getBytes(CHARSET_UTF8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, ALGORITHM_HMAC_SHA256);
        Mac mac = (Mac) MAC.clone();
        mac.init(secretKeySpec);
        return Base64Utils.encodeToString(mac.doFinal(preHash.getBytes(CHARSET_UTF8)));
    }

    /**
     * the prehash string = timestamp + method + requestPath + body .<br/>
     *
     * @param timestamp   the number of seconds since Unix Epoch in UTC. Decimal values are allowed.
     *                    eg: 2018-03-08T10:59:25.789Z
     * @param method      eg: POST
     * @param requestPath eg: /orders
     * @param queryString eg: before=2&limit=30
     *                    //     * @param body        json string, eg: {"product_id":"BTC-USD-0309","order_id":"377454671037440"}
     * @return prehash string eg: 2018-03-08T10:59:25.789ZPOST/orders?before=2&limit=30{"product_id":"BTC-USD-0309",
     * "order_id":"377454671037440"}
     */
    public static String preHash(String timestamp, String method, String requestPath, String queryString, String appKey, TreeMap<String, String> body) throws UnsupportedEncodingException {
        StringBuilder preHash = new StringBuilder();
        preHash.append(timestamp);
        preHash.append(method.toUpperCase());
        preHash.append(appKey);
        preHash.append(requestPath);
        if (!StringUtils.isEmpty(queryString)) {
            preHash.append(URI_QUESTION).append(URLDecoder.decode(queryString, "UTF-8"));
        }
        if (!CollectionUtils.isEmpty(body)) {
            preHash.append(appendBody(body));
        }
        return preHash.toString();
    }

    public static String appendBody(TreeMap<String, String> params) {
        StringBuilder str = new StringBuilder("");
        Set<String> setKey = params.keySet();
        for (String key : setKey) {
            str.append(key).append("=").append(String.valueOf(params.get(key))).append("&");
        }
        String strBody = str.toString();
        if (!StringUtils.isEmpty(strBody)) {
            //删除最后一个拼接符
            strBody = strBody.substring(0, strBody.length() - 1);
        }
        return strBody;
    }

    public static Mac MAC;

    static {
        try {
            MAC = Mac.getInstance(ALGORITHM_HMAC_SHA256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeErrorException(new Error("Can't get Mac's instance."));
        }
    }
}
