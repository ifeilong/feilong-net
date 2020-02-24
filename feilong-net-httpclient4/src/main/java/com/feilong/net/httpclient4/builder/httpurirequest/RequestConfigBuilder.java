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
package com.feilong.net.httpclient4.builder.httpurirequest;

import static com.feilong.core.TimeInterval.MILLISECOND_PER_SECONDS;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;

import com.feilong.net.entity.ConnectionConfig;

/**
 * httpclient 超时时间 等待时间 响应时间.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
final class RequestConfigBuilder{

    /** Don't let anyone instantiate this class. */
    private RequestConfigBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the {@link RequestConfig}.
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return the request config
     * @see Builder
     * @see RequestConfig
     * @see org.apache.http.client.config.CookieSpecs
     */
    static RequestConfig build(ConnectionConfig connectionConfig){
        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        //---------------------------------------------------------------
        Builder requestConfigBuilder = RequestConfig.custom(); //RequestConfig.DEFAULT;

        //设置超时时间
        setTimeout(requestConfigBuilder, useConnectionConfig);

        //设置代理
        setProxy(requestConfigBuilder, useConnectionConfig);

        //---------------------------------------------------------------
        //since 2.0.3
        requestConfigBuilder.setCookieSpec(CookieSpecs.IGNORE_COOKIES);

        return requestConfigBuilder.build();
    }

    //---------------------------------------------------------------

    /**
     * 设置超时时间.
     *
     * @param requestConfigBuilder
     *            the builder
     * @param useConnectionConfig
     *            the use connection config
     * @see <a href="http://blog.csdn.net/senblingbling/article/details/43916851">httpclient 超时时间 等待时间 响应时间</a>
     */
    private static void setTimeout(Builder requestConfigBuilder,ConnectionConfig useConnectionConfig){
        //设置从connect Manager获取Connection 超时时间，单位毫秒
        //从连接池中获取连接的超时时间

        //    Caused by: org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
        //        at org.apache.http.impl.conn.PoolingHttpClientConnectionManager.leaseConnection(PoolingHttpClientConnectionManager.java:314)
        requestConfigBuilder.setConnectionRequestTimeout(1 * MILLISECOND_PER_SECONDS);

        //---------------------------------------------------------------
        //与服务器连接超时时间
        //httpclient会创建一个异步线程用以创建socket连接，此处设置该socket的连接超时时间
        //单位毫秒
        requestConfigBuilder.setConnectTimeout(useConnectionConfig.getConnectTimeout());

        //请求获取数据的超时时间，单位毫秒
        //socket读数据超时时间：从服务器获取响应数据的超时时间
        //访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
        requestConfigBuilder.setSocketTimeout(useConnectionConfig.getReadTimeout());
    }

    //---------------------------------------------------------------

    /**
     * 设置代理.
     *
     * @param requestConfigBuilder
     *            the builder
     * @param connectionConfig
     *            the connection config
     */
    private static void setProxy(Builder requestConfigBuilder,ConnectionConfig connectionConfig){
        String proxyAddress = connectionConfig.getProxyAddress();
        Integer proxyPort = connectionConfig.getProxyPort();

        boolean isNeedProxy = isNotNullOrEmpty(proxyAddress) && isNotNullOrEmpty(proxyPort);
        if (isNeedProxy){
            HttpHost proxy = new HttpHost(proxyAddress, proxyPort);
            requestConfigBuilder.setProxy(proxy);
        }
    }
}
