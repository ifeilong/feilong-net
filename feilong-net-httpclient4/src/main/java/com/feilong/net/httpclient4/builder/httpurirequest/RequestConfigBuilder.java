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
     * Builds the.
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return the request config
     * @see Builder
     * @see RequestConfig
     * @see <a href="http://blog.csdn.net/senblingbling/article/details/43916851">httpclient 超时时间 等待时间 响应时间</a>
     */
    static RequestConfig build(ConnectionConfig connectionConfig){
        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);

        //---------------------------------------------------------------
        Builder builder = RequestConfig.custom(); //RequestConfig.DEFAULT;

        //设置超时时间
        setTimeout(builder, useConnectionConfig);

        //设置代理
        setProxy(builder, useConnectionConfig);

        return builder.build();
    }

    //---------------------------------------------------------------

    /**
     * 设置超时时间.
     *
     * @param builder
     *            the builder
     * @param useConnectionConfig
     *            the use connection config
     */
    private static void setTimeout(Builder builder,ConnectionConfig useConnectionConfig){
        //FIXME
        //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
        builder.setSocketTimeout(useConnectionConfig.getReadTimeout());

        //设置连接超时时间，单位毫秒。
        builder.setConnectTimeout(useConnectionConfig.getConnectTimeout());

        //设置从connect Manager获取Connection 超时时间，单位毫秒。
        //这个属性是新加的属性，因为目前版本是可以共享连接池的。
        builder.setConnectionRequestTimeout(1 * MILLISECOND_PER_SECONDS);
    }

    //---------------------------------------------------------------

    /**
     * 设置代理.
     *
     * @param builder
     *            the builder
     * @param connectionConfig
     *            the connection config
     */
    private static void setProxy(Builder builder,ConnectionConfig connectionConfig){
        String proxyAddress = connectionConfig.getProxyAddress();
        Integer proxyPort = connectionConfig.getProxyPort();

        boolean isNeedProxy = isNotNullOrEmpty(proxyAddress) && isNotNullOrEmpty(proxyPort);
        if (isNeedProxy){
            HttpHost proxy = new HttpHost(proxyAddress, proxyPort);
            builder.setProxy(proxy);
        }
    }
}
