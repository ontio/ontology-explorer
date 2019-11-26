<<<<<<< Updated upstream
package com.github.ontio.service.impl;

import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/7/11
 */
@Slf4j
@Service
public class CommonService {

    private static HttpClient httpClient;

    static {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        //客户端和服务器建立连接的timeout
        requestConfigBuilder.setConnectTimeout(30000);
        //从连接池获取连接的timeout
        requestConfigBuilder.setConnectionRequestTimeout(30000);
        //连接建立后，request没有回应的timeout
        requestConfigBuilder.setSocketTimeout(30000);

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        clientBuilder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(30000).build()); //连接建立后，request没有回应的timeout
        clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(60);
        httpClient = clientBuilder.setConnectionManager(cm).build();
    }


    /**
     * send http post request
     *
     * @param url
     * @param requestBodyStr
     * @param headerMap
     * @return
     */
    public String httpPostRequest(String url, String requestBodyStr, Map<String, String> headerMap) {

        String responseStr = "";
        //make request body
        StringEntity strEntity = new StringEntity(requestBodyStr, Charset.forName("UTF-8"));
        strEntity.setContentEncoding("UTF-8");
        strEntity.setContentType("application/json");
        //send request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(strEntity);
        if (Helper.isNotEmptyAndNull(headerMap)) {
            for (Map.Entry<String, String> entry :
                    headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entitys = response.getEntity();
                responseStr = EntityUtils.toString(entitys);
                log.info("{} url:{} response 200 :{}", Helper.currentMethod(), url, responseStr);
            } else {
                log.error("{} url:{} response {} :{}", Helper.currentMethod(), url, response.getStatusLine().getStatusCode(), responseStr);
            }
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
        return responseStr;
    }

    /**
     * send http get request
     *
     * @param url
     * @param headerMap
     * @return
     */
    public String httpGetRequest(String url, Map<String, String> headerMap) {

        String responseStr = "";

        HttpGet httpGet = new HttpGet(url);
        if (Helper.isNotEmptyAndNull(headerMap)) {
            for (Map.Entry<String, String> entry :
                    headerMap.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entitys = response.getEntity();
                responseStr = EntityUtils.toString(entitys);
                log.info("{} url:{} response 200 :{}", Helper.currentMethod(), url, responseStr);
            } else {
                log.error("{} url:{} response {} :{}", Helper.currentMethod(), url, response.getStatusLine().getStatusCode(), responseStr);
            }
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
        return responseStr;
    }

}
=======
package com.github.ontio.service.impl;

import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/7/11
 */
@Slf4j
@Service
public class CommonService {

    private static HttpClient httpClient;

    static {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        //客户端和服务器建立连接的timeout
        requestConfigBuilder.setConnectTimeout(30000);
        //从连接池获取连接的timeout
        requestConfigBuilder.setConnectionRequestTimeout(30000);
        //连接建立后，request没有回应的timeout
        requestConfigBuilder.setSocketTimeout(30000);

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        clientBuilder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(30000).build()); //连接建立后，request没有回应的timeout
        clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(60);
        httpClient = clientBuilder.setConnectionManager(cm).build();
    }


    /**
     * send http post request
     *
     * @param url
     * @param requestBodyStr
     * @param headerMap
     * @return
     */
    public String httpPostRequest(String url, String requestBodyStr, Map<String, String> headerMap) {

        String responseStr = "";
        //make request body
        StringEntity strEntity = new StringEntity(requestBodyStr, Charset.forName("UTF-8"));
        strEntity.setContentEncoding("UTF-8");
        strEntity.setContentType("application/json");
        //send request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(strEntity);
        if (Helper.isNotEmptyOrNull(headerMap)) {
            for (Map.Entry<String, String> entry :
                    headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entitys = response.getEntity();
                responseStr = EntityUtils.toString(entitys);
                log.info("{} url:{} response 200 :{}", Helper.currentMethod(), url, responseStr);
            } else {
                log.error("{} url:{} response {} :{}", Helper.currentMethod(), url, response.getStatusLine().getStatusCode(), responseStr);
            }
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
        return responseStr;
    }

    /**
     * send http get request
     *
     * @param url
     * @param headerMap
     * @return
     */
    public String httpGetRequest(String url, Map<String, String> headerMap) {

        String responseStr = "";

        HttpGet httpGet = new HttpGet(url);
        if (Helper.isNotEmptyOrNull(headerMap)) {
            for (Map.Entry<String, String> entry :
                    headerMap.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entitys = response.getEntity();
                responseStr = EntityUtils.toString(entitys);
                log.info("{} url:{} response 200 :{}", Helper.currentMethod(), url, responseStr);
            } else {
                log.error("{} url:{} response {} :{}", Helper.currentMethod(), url, response.getStatusLine().getStatusCode(), responseStr);
            }
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
        }
        return responseStr;
    }

}
>>>>>>> Stashed changes
