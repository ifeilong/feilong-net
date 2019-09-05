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

import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;

import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.ssl.SSLProtocol;

/**
 * ssl 封装器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.0.0
 */
public final class SSLPacker{

    /** Don't let anyone instantiate this class. */
    private SSLPacker(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 设置 SSL.
     * 
     * @param customHttpClientBuilder
     *            the custom http client builder
     * @param connectionConfig
     *            the connection config
     * @param layeredConnectionSocketFactory
     *            the layered connection socket factory
     *
     * @see org.apache.http.conn.ssl.NoopHostnameVerifier
     * @see javax.net.ssl.HostnameVerifier
     */
    static void pack(
                    org.apache.http.impl.client.HttpClientBuilder customHttpClientBuilder,
                    ConnectionConfig connectionConfig,
                    LayeredConnectionSocketFactory layeredConnectionSocketFactory){
        if (null != layeredConnectionSocketFactory){
            customHttpClientBuilder.setSSLSocketFactory(layeredConnectionSocketFactory);
        }
        //---------------------------------------------------------------
        // SSLContext sslContext = buildHttpClient4SSLContext();
        //这代码比上面简洁
        SSLContext sslContext = com.feilong.net.ssl.SSLContextBuilder.build(SSLProtocol.TLSv12);
        customHttpClientBuilder.setSSLContext(sslContext);

        //---------------------------------------------------------------
        //since 2.0.0
        if (connectionConfig.getTurnOffHostnameVerifier()){
            customHttpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
        }
    }
}
