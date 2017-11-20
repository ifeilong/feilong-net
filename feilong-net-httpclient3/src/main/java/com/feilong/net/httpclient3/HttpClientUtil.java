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
package com.feilong.net.httpclient3;

import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.HttpMethodType;
import com.feilong.net.UncheckedHttpException;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * HttpClient相关工具类.
 * 
 * <h3>关于 HttpMethod Response</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link HttpMethod#getResponseBody()}</td>
 * <td>返回的是目标的二进制的byte流;</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link HttpMethod#getResponseBodyAsString()}</td>
 * <td>返回的是String类型,值得注意的是该方法返回的String的编码是根据系统默认的编码方式,<br>
 * 所以返回的String值可能编码类型有误,在本文的"字符编码"部分中将对此做详细介绍;</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link HttpMethod#getResponseBodyAsStream()}</td>
 * <td>对于目标地址中有大量数据需要传输是最佳的.</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.apache.commons.httpclient.HttpMethod
 * @see org.apache.commons.httpclient.HttpClient
 * @since 1.0.6
 */
public final class HttpClientUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /** Don't let anyone instantiate this class. */
    private HttpClientUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得 response body as string.
     *
     * @param uri
     *            the uri
     * @param httpMethodType
     *            the http method type
     * @return the response body as string
     * @since 1.5.0
     */
    public static String getResponseBodyAsString(String uri,HttpMethodType httpMethodType){
        return getResponseBodyAsString(uri, null, httpMethodType);
    }

    /**
     * 获得 response body as string.
     *
     * @param uri
     *            the uri
     * @param requestParamMap
     *            the request param map
     * @param httpMethodType
     *            the http method type
     * @return the response body as string
     * @since 1.5.0
     */
    public static String getResponseBodyAsString(String uri,Map<String, String> requestParamMap,HttpMethodType httpMethodType){
        HttpClientConfig httpClientConfig = new HttpClientConfig(uri, requestParamMap, httpMethodType);
        return getResponseBodyAsString(httpClientConfig);
    }

    /**
     * Gets the http method response body as string.
     *
     * @param httpClientConfig
     *            the http client config
     * @return the http method response body as string<br>
     *         仅支持 {@link HttpMethodType} 类型协议,其他抛出 {@link UnsupportedOperationException}
     */
    public static String getResponseBodyAsString(HttpClientConfig httpClientConfig){
        HttpMethod httpMethod = HttpMethodUtil.buildHttpMethod(httpClientConfig);
        try{

            // 得到返回的数据
            String responseBodyAsString = httpMethod.getResponseBodyAsString();
            if (LOGGER.isDebugEnabled()){
                Map<String, Object> map = HttpMethodUtil.getHttpMethodResponseAttributeMapForLog(httpMethod, httpClientConfig);
                LOGGER.debug("getHttpMethodResponseAttributeMapForLog:{}", JsonUtil.format(map));
            }
            return responseBodyAsString;
        }catch (Exception e){
            LOGGER.error(e.getClass().getName(), e);
            throw new UncheckedHttpException(e);
        }finally{
            httpMethod.releaseConnection();// 释放连接
        }
    }
}
