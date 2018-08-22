/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.temp;

import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.HttpMethodType;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.2.1 2015年6月6日 下午11:04:42
 * @since 1.2.1
 */
public class HttpClientUtilTest2{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtilTest2.class);

    /**
     * TestHttpClientUtilTest.
     * 
     * @throws IOException
     * @throws ClientProtocolException
     */
    @Test
    public void testHttpClientUtilTest2() throws ClientProtocolException,IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://targethost/homepage");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        // The underlying HTTP connection is still held by the response object to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager. 
        try{
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body  and ensure it is fully consumed
            EntityUtils.consume(entity1);
        }finally{
            response1.close();
        }

        HttpPost httpPost = new HttpPost("http://targethost/login");
        List<NameValuePair> nvps = newArrayList();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        try{
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body and ensure it is fully consumed
            EntityUtils.consume(entity2);
        }finally{
            response2.close();
        }
    }

    //---------------------InputStream------------------------------------------

    /**
     * 获得请求的响应码.
     * 
     * <p>
     * 默认 {@link HttpMethodType#GET} 请求
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     int responseStatusCode = HttpClientUtil.getResponseStatusCode(urlString);
     *     LOGGER.debug("" + responseStatusCode);
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 200
     * </pre>
     * 
     * </blockquote>
     *
     * @param urlString
     *            the url string
     * @return 如果 <code>urlString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>urlString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static InputStream getInputStream(String urlString){
        Validate.notBlank(urlString, "urlString can't be blank!");
        return getInputStream(urlString, null);
    }

    /**
     * 获得请求的响应码.
     * 
     * <p>
     * 默认 {@link HttpMethodType#GET} 请求
     * </p>
     * 
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     int responseStatusCode = HttpClientUtil.getResponseStatusCode(urlString);
     *     LOGGER.debug("" + responseStatusCode);
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 200
     * </pre>
     * 
     * </blockquote>
     *
     *
     * @param urlString
     *            the url string
     * @param connectionConfig
     *            the connection config
     * @return 如果 <code>urlString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>urlString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static InputStream getInputStream(String urlString,ConnectionConfig connectionConfig){
        Validate.notBlank(urlString, "urlString can't be blank!");
        HttpRequest httpRequest = new HttpRequest(urlString);
        return getInputStream(httpRequest, connectionConfig);
    }

    /**
     * 获得请求的响应码.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * public void testGetResponseBodyAsString(){
     *     String urlString = "http://www.baidu.com";
     * 
     *     int responseStatusCode = HttpClientUtil.getResponseStatusCode(urlString);
     *     LOGGER.debug("" + responseStatusCode);
     * }
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 200
     * </pre>
     * 
     * </blockquote>
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return 如果 <code>httpRequest</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static InputStream getInputStream(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        Validate.notNull(httpRequest, "httpRequest can't be null!");

        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        HttpResponse httpResponse = null;
        //execute(httpRequest, useConnectionConfig);

        try{
            InputStream inputStream = httpResponse.getEntity().getContent();

            //---------------------------------------------------------------

            if (LOGGER.isTraceEnabled()){
                LOGGER.trace("httpRequest:[{}],connectionConfig:[{}] ", JsonUtil.format(httpRequest), JsonUtil.format(useConnectionConfig));
            }
            return inputStream;
        }catch (UnsupportedOperationException | IOException e){
            throw new UncheckedHttpException(e);
        }
    }
}
