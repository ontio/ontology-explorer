package com.github.ontio.util;

import com.github.ontio.exception.ExplorerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
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
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhouq
 * @version 1.0
 */
@Slf4j
public class HttpClientUtil {

    private static HttpClient httpClient;

    static {
        //HttpClient4.5版本后的参数设置
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        //客户端和服务器建立连接的timeout
        requestConfigBuilder.setConnectTimeout(30000);
        //从连接池获取连接的timeout
        requestConfigBuilder.setConnectionRequestTimeout(30000);
        //连接建立后，request没有回应的timeout。
        requestConfigBuilder.setSocketTimeout(60000);

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        //连接建立后，request没有回应的timeout
        clientBuilder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(60000).build());
        clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(30);
        httpClient = clientBuilder.setConnectionManager(cm).build();
    }

    /**
     * httpclient post请求
     *
     * @param reqBodyStr
     * @param url
     * @return
     * @throws ExplorerException
     */
    public static String postRequest(String url, String reqBodyStr, Map<String, Object> headerMap) throws ExplorerException {

        StringEntity stringEntity = new StringEntity(reqBodyStr, Charset.forName("UTF-8"));
        stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);
        //设置请求头
        for (Map.Entry<String, Object> entry :
                headerMap.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue().toString());
        }

        CloseableHttpResponse response = null;
        String responseStr = "";
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responseStr = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            log.error("{} error...", Helper.currentMethod(), e);
            throw new ExplorerException(ErrorInfo.COMM_FAIL);
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            //log.info("send requestbody:{} to {},response 200:{}", reqBodyStr, url, responseStr);
            return responseStr;
        } else {
            log.error("send requestbody:{} to {},response {}:{}", reqBodyStr, url, response.getStatusLine().getStatusCode(), responseStr);
            throw new ExplorerException(ErrorInfo.COMM_FAIL);
        }
    }

    /**
     * httpclient get请求
     *
     * @param uri
     * @return
     * @throws ExplorerException
     */
    public static String getRequest(String uri, Map<String, Object> paramMap, Map<String, Object> headerMap) throws ExplorerException {

        String responseStr = "";
        CloseableHttpResponse response = null;
        URIBuilder uriBuilder = null;
        try {
            //拼完整的请求url
            uriBuilder = new URIBuilder(uri);
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry :
                    paramMap.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            uriBuilder.setParameters(params);

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            //设置请求头
            for (Map.Entry<String, Object> entry :
                    headerMap.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue().toString());
            }
            response = (CloseableHttpResponse) httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            responseStr = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            log.error("{} error...", Helper.currentMethod(), e);
            throw new ExplorerException(ErrorInfo.COMM_FAIL);
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            //log.info("send to {},response 200:{}", uriBuilder.toString(), responseStr);
            return responseStr;
        } else {
            log.error("send to {},response {}:{}", uriBuilder.toString(), response.getStatusLine().getStatusCode(), responseStr);
            throw new ExplorerException(ErrorInfo.COMM_FAIL);
        }
    }


    /**
     * httpclient delete请求(带请求体)
     *
     * @param reqBodyStr
     * @param url
     * @return
     * @throws ExplorerException
     */
    public static String deleteRequest(String url, String reqBodyStr, Map<String, Object> headerMap) throws ExplorerException {

        StringEntity stringEntity = new StringEntity(reqBodyStr, Charset.forName("UTF-8"));
        stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
        httpDelete.setEntity(stringEntity);
        //设置请求头
        for (Map.Entry<String, Object> entry :
                headerMap.entrySet()) {
            httpDelete.setHeader(entry.getKey(), entry.getValue().toString());
        }

        CloseableHttpResponse response = null;
        String responseStr = "";
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpDelete);
            HttpEntity httpEntity = response.getEntity();
            responseStr = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            log.error("{} error...", Helper.currentMethod(), e);
            throw new ExplorerException(ErrorInfo.COMM_FAIL);
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            return responseStr;
        } else {
            log.error("send requestbody:{} to {},response {}:{}", reqBodyStr, url, response.getStatusLine().getStatusCode(), responseStr);
            throw new ExplorerException(ErrorInfo.COMM_FAIL);
        }
    }


    static public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpDeleteWithBody() {
            super();
        }
    }


    /**
     * httpclient put请求
     *
     * @param reqBodyStr
     * @param url
     * @return
     * @throws ExplorerException
     */
    public static String putRequest(String url, String reqBodyStr, Map<String, Object> headerMap) throws ExplorerException {

        StringEntity stringEntity = new StringEntity(reqBodyStr, Charset.forName("UTF-8"));
        stringEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(stringEntity);
        //设置请求头
        for (Map.Entry<String, Object> entry :
                headerMap.entrySet()) {
            httpPut.setHeader(entry.getKey(), entry.getValue().toString());
        }

        CloseableHttpResponse response = null;
        String responseStr = "";
        try {
            response = (CloseableHttpResponse) httpClient.execute(httpPut);
            HttpEntity httpEntity = response.getEntity();
            responseStr = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            log.error("{} error...", Helper.currentMethod(), e);
            throw new ExplorerException(ErrorInfo.COMM_FAIL);
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            //log.info("send requestbody:{} to {},response 200:{}", reqBodyStr, url, responseStr);
            return responseStr;
        } else {
            log.error("send requestbody:{} to {},response {}:{}", reqBodyStr, url, response.getStatusLine().getStatusCode(), responseStr);
            throw new ExplorerException(ErrorInfo.COMM_FAIL);
        }
    }

}
