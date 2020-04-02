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
package com.feilong.net.httpclient4.builder;

import static com.feilong.core.util.MapUtil.newConcurrentHashMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.httpclient4.packer.SSLPacker;

/**
 * {@link HttpClient} 构造器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * 
 * @see org.apache.http.impl.client.HttpClients#createDefault()
 * @see org.apache.http.impl.client.HttpClients#custom()
 * @see org.apache.http.impl.client.HttpClientBuilder#create()
 * @since 1.10.6
 * @since 1.11.0 change class Access Modifiers
 */
public class HttpClientBuilder{

    /** The Constant log. */
    private static final Logger                      LOGGER = LoggerFactory.getLogger(HttpClientBuilder.class);

    //---------------------------------------------------------------
    /**
     * 设置缓存
     * 
     * @since 2.0.3
     */
    private static Map<ConnectionConfig, HttpClient> CACHE  = newConcurrentHashMap(10);

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private HttpClientBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 创造 {@link HttpClient}.
     *
     * @param connectionConfig
     *            the connection config
     * @return the closeable http client
     * @since 2.0.3
     */
    public static HttpClient build(ConnectionConfig connectionConfig){
        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        //---------------------------------------------------------------
        HttpClient httpClient = CACHE.get(useConnectionConfig);
        if (null != httpClient){
            LOGGER.debug(StringUtils.center("loader from cache,cache size:[" + CACHE.size() + "]", 80, "="));
            return httpClient;
        }

        //---------------------------------------------------------------
        //只有cache 中没有httpClient 对象时 才需要排队
        synchronized (HttpClientBuilder.class){
            httpClient = CACHE.get(useConnectionConfig);
            if (null == httpClient){
                httpClient = build(useConnectionConfig, null);
                CACHE.put(useConnectionConfig, httpClient);

                LOGGER.debug(StringUtils.center("build new httpClient and set to cache,cache size:[" + CACHE.size() + "]", 120, "-"));
            }
        }

        return httpClient;
    }

    //---------------------------------------------------------------

    /**
     * Builds the {@link CloseableHttpClient}.
     *
     * @param connectionConfig
     *            the connection config
     * @param layeredConnectionSocketFactory
     *            the layered connection socket factory
     * @return 如果 <code>layeredConnectionSocketFactory</code> 不是null,将会设置
     *         {@link org.apache.http.impl.client.HttpClientBuilder#setSSLSocketFactory(LayeredConnectionSocketFactory)}<br>
     * @see org.apache.http.impl.client.HttpClientBuilder#build()
     */
    public static HttpClient build(ConnectionConfig connectionConfig,LayeredConnectionSocketFactory layeredConnectionSocketFactory){
        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        //---------------------------------------------------------------
        org.apache.http.impl.client.HttpClientBuilder customHttpClientBuilder = HttpClients.custom();

        SSLPacker.pack(customHttpClientBuilder, useConnectionConfig, layeredConnectionSocketFactory);

        //---------------------------------------------------------------
        //see org.apache.http.impl.client.HttpClientBuilder#build()

        //since 2.0.3
        int maxConnPerRoute = useConnectionConfig.getMaxConnPerRoute();
        if (maxConnPerRoute > 0){
            //Assigns maximum connection per route value.
            //Please note this value can be overridden by the setConnectionManager(org.apache.http.conn.HttpClientConnectionManager) method.
            //针对一个域名同时正在使用的最多连接数 系统默认2
            customHttpClientBuilder.setMaxConnPerRoute(maxConnPerRoute);
        }

        int maxConnTotal = useConnectionConfig.getMaxConnTotal();
        if (maxConnTotal > 0){
            //Assigns maximum total connection value.
            //Please note this value can be overridden by the setConnectionManager(org.apache.http.conn.HttpClientConnectionManager) method.
            //同时间正在用的最多的连接数 系统默认20
            customHttpClientBuilder.setMaxConnTotal(maxConnTotal);
        }
        //---------------------------------------------------------------
        //since 2.0.3
        //since httpclient 4.4
        //适用于 复用连接的时候使用
        customHttpClientBuilder.evictExpiredConnections()//
                        .evictIdleConnections(30L, TimeUnit.SECONDS);

        //---------------------------------------------------------------
        RequestConfig requestConfig = RequestConfigBuilder.build(connectionConfig);
        customHttpClientBuilder.setDefaultRequestConfig(requestConfig);

        //---------------------------------------------------------------
        //CloseableHttpClient
        return customHttpClientBuilder.build();
    }

    //
    //customHttpClientBuilder.setConnectionManagerShared(true);

    //org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy.INSTANCE
    //customHttpClientBuilder.setKeepAliveStrategy(keepAliveStrategy);

    // 配置出错重试
    //org.apache.http.impl.client.DefaultHttpRequestRetryHandler.INSTANCE
    //        customHttpClientBuilder.disableAutomaticRetries();
    //        customHttpClientBuilder.setRetryHandler(new StandardHttpRequestRetryHandler(3, true));

}
