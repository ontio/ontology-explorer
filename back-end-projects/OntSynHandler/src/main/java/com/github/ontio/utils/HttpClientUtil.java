package com.github.ontio.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhouq
 */
@Slf4j
public class HttpClientUtil {

    private static HttpClient httpClient;

    static {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectTimeout(30000);
        requestConfigBuilder.setConnectionRequestTimeout(30000);
        requestConfigBuilder.setSocketTimeout(60000);

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        clientBuilder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(60000).build());
        clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(30);
        httpClient = clientBuilder.setConnectionManager(cm).build();
    }

    /**
     * httpclient post
     *
     * @param reqBodyStr
     * @param url
     * @return
     * @throws Exception
     */
    public static String postRequest(String url, String reqBodyStr, Map<String, Object> headerMap) throws Exception {

        String responseStr = "";

        StringEntity stringEntity = new StringEntity(reqBodyStr, Charset.forName("UTF-8"));
        stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);
        //set header
        headerMap.forEach((key, value) -> {
            httpPost.setHeader(key, value.toString());
        });
        CloseableHttpResponse response = null;
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responseStr = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            throw e;
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            return responseStr;
        } else {
            log.error("send request body:{} to {},response status:{},{}", reqBodyStr, url, response.getStatusLine().getStatusCode(), responseStr);
            throw new Exception(response.toString());
        }
    }

    /**
     * httpclient get
     *
     * @param uri
     * @return
     * @throws Exception
     */
    public static String getRequest(String uri, Map<String, Object> paramMap, Map<String, Object> headerMap) throws Exception {
        String responseStr = "";
        CloseableHttpResponse response = null;
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(uri);
            List<NameValuePair> params = new ArrayList<>();
            paramMap.forEach((key, value) -> {
                params.add(new BasicNameValuePair(key, value.toString()));
            });
            uriBuilder.setParameters(params);

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            //set header
            headerMap.forEach((key, value) -> {
                httpGet.setHeader(key, value.toString());
            });
            response = (CloseableHttpResponse) httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            responseStr = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            throw e;
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            return responseStr;
        } else {
            log.error("send to {},response status:{},{}", uriBuilder.toString(), response.getStatusLine().getStatusCode(), responseStr);
            throw new Exception(response.toString());
        }
    }


    /**
     * httpclient post
     *
     * @param url
     * @param urlParameters
     * @param headerMap
     * @return
     * @throws Exception
     */
    public static String postRequest(String url, List<BasicNameValuePair> urlParameters, Map<String, String> headerMap) throws Exception {
        String responseStr = "";
        HttpPost httpPost = new HttpPost(url);
        //set header
        headerMap.forEach((key, value) -> {
            httpPost.setHeader(key, value);
        });
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responseStr = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            throw e;
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            return responseStr;
        } else {
            log.error("send to {},response status:{},{}", url + urlParameters.toString(), response.getStatusLine().getStatusCode(), responseStr);
            throw new Exception(response.toString());
        }
    }

}
