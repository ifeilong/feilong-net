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
package com.feilong.snippet;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.UncheckedHttpException;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.ssl.SSLProtocol;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.22.0
 */
public class HttpClient4SSLContextBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient4SSLContextBuilder.class);

    //customHttpClientBuilder.setConnectionManager(buildConnectionManager(connectionConfig));
    /**
     * 
     * @return
     * @since 2.0.3
     * @see "org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor#createDefaultHttpClient()"
     * @see org.apache.http.impl.conn.PoolingHttpClientConnectionManager#PoolingHttpClientConnectionManager(org.apache.http.conn.HttpClientConnectionOperator,
     *      org.apache.http.conn.HttpConnectionFactory, long, java.util.concurrent.TimeUnit)
     */
    static HttpClientConnectionManager buildConnectionManager(ConnectionConfig connectionConfig){
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(100);
        //设置每个路由基础的连接
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(5);

        //HttpHost httpHost = new HttpHost(hostname, port);
        //        // 设置目标主机对应的路由的最大连接数，会覆盖setDefaultMaxPerRoute设置的默认值
        //poolingHttpClientConnectionManager.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);
        return poolingHttpClientConnectionManager;
    }

    static SSLContext buildHttpClient4SSLContext(){

        // 被 com.feilong.net.ssl.SSLContextBuilder.build(String) 替代 
        //参见 com.feilong.net.httpclient4.builder.HttpClientBuilder.setSSL(LayeredConnectionSocketFactory, HttpClientBuilder)
        try{
            SSLContextBuilder sslContextBuilder = SSLContexts.custom()//
                            //  WARN: setProtocol since  4.4.7
                            .setProtocol(SSLProtocol.TLSv12)//
                            //since 1.14.0
                            .loadTrustMaterial(TrustAllStrategy.INSTANCE);

            return sslContextBuilder.build();
        }catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e){
            throw new UncheckedHttpException(e);
        }catch (java.lang.NoSuchMethodError e){
            LOGGER.warn(
                            "pls update your [org.apache.httpcomponents:httpclient] version >= [4.4.7],otherwise cannot use setProtocol method",
                            e);
        }
        return null;
    }
}
