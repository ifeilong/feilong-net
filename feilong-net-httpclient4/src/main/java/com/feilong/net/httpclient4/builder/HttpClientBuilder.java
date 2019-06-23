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

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;

import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.ssl.SSLProtocol;

/**
 * HttpClient 构造器.
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
     */
    public static HttpClient build(ConnectionConfig connectionConfig){
        return build(connectionConfig, null);
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param connectionConfig
     *            the connection config
     * @param layeredConnectionSocketFactory
     *            the layered connection socket factory
     * @return 如果 <code>layeredConnectionSocketFactory</code> 不是null,将会设置
     *         {@link org.apache.http.impl.client.HttpClientBuilder#setSSLSocketFactory(LayeredConnectionSocketFactory)}<br>
     */
    public static HttpClient build(ConnectionConfig connectionConfig,LayeredConnectionSocketFactory layeredConnectionSocketFactory){
        org.apache.http.impl.client.HttpClientBuilder customHttpClientBuilder = HttpClients.custom();

        setSSL(layeredConnectionSocketFactory, customHttpClientBuilder);
        //customHttpClientBuilder.setConnectionManager(connManager);
        //.setDefaultCredentialsProvider(CredentialsProviderBuilder.build(AuthScope.ANY, userName, password))//

        //---------------------------------------------------------------
        return customHttpClientBuilder.build();
    }

    //---------------------------------------------------------------

    /**
     * 设置 SSL.
     *
     * @param layeredConnectionSocketFactory
     *            the layered connection socket factory
     * @param customHttpClientBuilder
     *            the custom http client builder
     * @since 1.11.4
     */
    private static void setSSL(
                    LayeredConnectionSocketFactory layeredConnectionSocketFactory,
                    org.apache.http.impl.client.HttpClientBuilder customHttpClientBuilder){
        if (null != layeredConnectionSocketFactory){
            customHttpClientBuilder.setSSLSocketFactory(layeredConnectionSocketFactory);
        }

        //---------------------------------------------------------------
        // SSLContext sslContext = buildHttpClient4SSLContext();
        //这代码比上面简洁
        SSLContext sslContext = com.feilong.net.ssl.SSLContextBuilder.build(SSLProtocol.TLSv12);
        customHttpClientBuilder.setSSLContext(sslContext);
    }
}
