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
package com.feilong.net;

import static com.feilong.net.HttpMethodType.POST;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.net.URLUtil;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 * The Class URLConnectionHelper辅助类,目前主要是buildURLConnection.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.9.6
 */
class URLConnectionBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(URLConnectionBuilder.class);

    /** Don't let anyone instantiate this class. */
    private URLConnectionBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 构建 {@link java.net.HttpURLConnection}.
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return {@link java.net.HttpURLConnection}
     */
    static URLConnection buildURLConnection(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        Validate.notNull(httpRequest, "httpRequest can't be null!");

        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, new ConnectionConfig());
        try{
            HttpURLConnection httpURLConnection = openConnection(httpRequest, useConnectionConfig);
            prepareConnection(httpURLConnection, httpRequest, useConnectionConfig);
            return httpURLConnection;
        }catch (IOException e){
            throw new UncheckedHttpException(e);
        }
    }

    /**
     * Prepare connection.
     *
     * @param httpURLConnection
     *            the http url connection
     * @param httpRequest
     *            the http request
     * @param useConnectionConfig
     *            the use connection config
     * @throws IOException
     *             the IO exception
     * @see "org.springframework.http.client.SimpleClientHttpRequestFactory#prepareConnection(HttpURLConnection, String)"
     */
    private static void prepareConnection(HttpURLConnection httpURLConnection,HttpRequest httpRequest,ConnectionConfig useConnectionConfig)
                    throws IOException{

        //如果是https  
        if (httpURLConnection instanceof HttpsURLConnection){
            //HttpsURLConnectionHelper.doWithHttps((HttpsURLConnection) httpURLConnection);
        }

        //一定要为HttpUrlConnection设置connectTimeout属性以防止连接被阻塞
        httpURLConnection.setConnectTimeout(useConnectionConfig.getConnectTimeout());
        httpURLConnection.setReadTimeout(useConnectionConfig.getReadTimeout());

        httpURLConnection.setRequestMethod(httpRequest.getHttpMethodType().getMethod().toUpperCase());//这里要大写,否则会报  java.net.ProtocolException: Invalid HTTP method: get

        //设置是否向httpUrlConnection输出,如果是post请求,参数要放在http正文内,因此需要设为true,默认是false
        httpURLConnection.setDoOutput(POST == httpRequest.getHttpMethodType());

        //**********************************************
        //设置默认的UA, 你可以使用headerMap来覆盖
        httpURLConnection.setRequestProperty("User-Agent", HttpRequest.DEFAULT_USER_AGENT);
        //**********************************************

        Map<String, String> headerMap = httpRequest.getHeaderMap();
        if (null != headerMap){
            for (Map.Entry<String, String> entry : headerMap.entrySet()){
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

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
     * @since 1.2.0
     */
    private static HttpURLConnection openConnection(HttpRequest httpRequest,ConnectionConfig connectionConfig) throws IOException{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("httpRequest:[{}],useConnectionConfig:[{}]", JsonUtil.format(httpRequest), JsonUtil.format(connectionConfig));
        }

        URL url = URLUtil.toURL(httpRequest.getUri());

        Proxy proxy = getProxy(connectionConfig.getProxyAddress(), connectionConfig.getProxyPort());

        // 此处的urlConnection对象实际上是根据URL的请求协议(此处是http)生成的URLConnection类的子类HttpURLConnection,
        // 故此处最好将其转化 为HttpURLConnection类型的对象,以便用到 HttpURLConnection更多的API.
        if (isNotNullOrEmpty(proxy)){
            LOGGER.debug("use proxy:{}", proxy.toString());
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
