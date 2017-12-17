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
package com.feilong.net.handler;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.net.URLUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;

/**
 * open HttpURLConnection.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class HttpURLConnectionOpener{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpURLConnectionOpener.class);

    /**
     * Open connection.
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return the http url connection
     * @throws IOException
     *             the IO exception
     */
    public static HttpURLConnection open(HttpRequest httpRequest,ConnectionConfig connectionConfig) throws IOException{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("httpRequest:[{}],useConnectionConfig:[{}]", JsonUtil.format(httpRequest), JsonUtil.format(connectionConfig));
        }

        //---------------------------------------------------------------
        URL url = URLUtil.toURL(httpRequest.getUri());

        Proxy proxy = getProxy(connectionConfig.getProxyAddress(), connectionConfig.getProxyPort());

        // 此处的urlConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection,
        // 故此处最好将其转化 为HttpURLConnection类型的对象,以便用到 HttpURLConnection更多的API.
        if (isNotNullOrEmpty(proxy)){
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("use proxy:{}", proxy.toString());
            }

            return (HttpURLConnection) url.openConnection(proxy);//proxy can't null,otherwise IllegalArgumentException
        }
        // 每次调用此 URL 的协议处理程序的 openConnection 方法都打开一个新的连接.
        return (HttpURLConnection) url.openConnection();
    }
    // ******************************************************************************************

    /**
     * 获得代理.
     * 
     * @param proxyAddress
     *            the proxy address
     * @param proxyPort
     *            代理端口 <br>
     *            A valid port value is between 0 ~ 65535. <br>
     *            A port number of zero will let the system pick up an ephemeral port in a bind operation.
     * @return the proxy
     * @see java.net.Proxy.Type.HTTP
     * @see java.net.InetSocketAddress#InetSocketAddress(String, int)
     */
    private static Proxy getProxy(String proxyAddress,Integer proxyPort){
        return isNotNullOrEmpty(proxyAddress) && isNotNullOrEmpty(proxyPort)
                        ? new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort)) : null;
    }
}
