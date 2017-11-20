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
package com.feilong.net.httpclient4;

import static com.feilong.core.CharsetType.UTF8;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import com.feilong.net.HttpMethodType;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.entity.HttpRequest;
import com.feilong.net.httpclient4.builder.HttpEntityBuilder;
import com.feilong.net.httpclient4.builder.HttpUriRequestBuilder;

/**
 * 基于 HttpClient4 的工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.apache.http.client.methods.HttpUriRequest
 * @see "com.feilong.tools.net.httpclient3.HttpClientUtil"
 * @since 1.2.1
 */
public final class HttpClientUtil{

    /** 默认编码 <code>{@value}</code>. */
    private static final String DEFAULT_CHARSET = UTF8;

    //---------------------------------------------------------------

    /**
     * 发送请求,获得请求的响应内容.
     * 
     * <p>
     * 默认 {@link com.feilong.net.HttpMethodType#GET} 请求
     * </p>
     *
     * @param uri
     *            请求的uri地址
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static String getResponseBodyAsString(String uri){
        return getResponseBodyAsString(uri, HttpMethodType.GET);
    }

    /**
     * 发送请求,获得请求的响应内容..
     *
     * @param uri
     *            请求的uri地址
     * @param httpMethodType
     *            the http method type
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static String getResponseBodyAsString(String uri,HttpMethodType httpMethodType){
        return getResponseBodyAsString(uri, null, httpMethodType);
    }

    /**
     * 发送请求,获得请求的响应内容.
     *
     * @param uri
     *            请求的uri地址
     * @param requestParamMap
     *            the request param map
     * @param httpMethodType
     *            the http method type
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static String getResponseBodyAsString(String uri,Map<String, String> requestParamMap,HttpMethodType httpMethodType){
        Validate.notBlank(uri, "uri can't be blank!");

        HttpRequest httpRequest = new HttpRequest(uri, requestParamMap, httpMethodType);
        return getResponseBodyAsString(httpRequest);
    }

    /**
     * 发送请求,获得请求的响应内容.
     *
     * @param httpRequest
     *            the http request
     * @return the response body as string
     */
    public static String getResponseBodyAsString(HttpRequest httpRequest){
        Validate.notNull(httpRequest, "httpRequest can't be null!");

        HttpUriRequest httpUriRequest = HttpUriRequestBuilder.build(httpRequest);
        return getResponseBodyAsString(httpUriRequest);
    }

    //---------------------------------------------------------------

    /**
     * 获得 response body as string.
     *
     * @param httpUriRequest
     *            the http uri request
     * @return 如果 <code>httpUriRequest</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 解析有问题,抛出 {@link UncheckedHttpException}<br>
     */
    private static String getResponseBodyAsString(HttpUriRequest httpUriRequest){
        Validate.notNull(httpUriRequest, "httpUriRequest can't be null!");

        HttpEntity responseEntity = HttpEntityBuilder.execute(httpUriRequest);
        try{
            return EntityUtils.toString(responseEntity, DEFAULT_CHARSET);
        }catch (ParseException | IOException e){
            throw new UncheckedHttpException(e);
        }
    }
}
