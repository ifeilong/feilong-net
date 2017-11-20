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

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.HttpMethodType;
import com.feilong.net.entity.HttpRequest;
import com.feilong.net.httpclient4.packer.HeadersPacker;
import com.feilong.net.httpclient4.packer.ParametersPacker;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * 专门用来构造 {@link HttpUriRequest}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class HttpUriRequestBuilder{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUriRequestBuilder.class);

    /**
     * 基于 httpRequest 构造 {@link HttpUriRequest}.
     *
     * @param httpRequest
     *            the http request
     * @return 如果 <code>httpRequest</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>httpRequest Uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>httpRequest Uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static HttpUriRequest build(HttpRequest httpRequest){
        Validate.notNull(httpRequest, "httpRequest can't be null!");

        String uri = httpRequest.getUri();
        Validate.notBlank(uri, "uri can't be blank!");

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("httpRequest info:[{}]", JsonUtil.format(httpRequest));
        }

        HttpUriRequest httpUriRequest = HttpUriRequestBuilder.build(httpRequest, uri);

        HeadersPacker.setHeaders(httpUriRequest, httpRequest.getHeaderMap());
        ParametersPacker.setParameters(httpUriRequest, httpRequest.getParamMap());

        return httpUriRequest;
    }

    //---------------------------------------------------------------

    /**
     * 基于 httpRequest 和 uri 构造 {@link HttpUriRequest}.
     * 
     * <p>
     * 暂时只支持 get 和post 方法
     * </p>
     *
     * @param httpRequest
     *            the http request
     * @param uri
     *            the uri
     * @return the http uri request
     */
    private static HttpUriRequest build(HttpRequest httpRequest,String uri){
        RequestConfig requestConfig = build();

        HttpMethodType httpMethodType = httpRequest.getHttpMethodType();
        switch (httpMethodType) {
            case GET:
                return buildHttpGet(uri, requestConfig);
            case POST:
                return buildHttpPost(uri, requestConfig);
            default:
                throw new NotImplementedException(httpMethodType + " is not implemented!");
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the http post.
     *
     * @param uri
     *            the uri
     * @param requestConfig
     *            the request config
     * @return the http uri request
     */
    private static HttpUriRequest buildHttpPost(String uri,RequestConfig requestConfig){
        HttpPost httpPost = new HttpPost(uri);
        /// HttpEntity entity = new StringEntity(requestBody, charset);
        //httpPost.setEntity(entity);
        httpPost.setConfig(requestConfig);
        return httpPost;
    }

    /**
     * Builds the http get.
     *
     * @param uri
     *            the uri
     * @param requestConfig
     *            the request config
     * @return the http uri request
     */
    private static HttpUriRequest buildHttpGet(String uri,RequestConfig requestConfig){
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    /**
     * Builds the.
     *
     * @return the request config
     */
    private static RequestConfig build(){
        return RequestConfig.DEFAULT;
    }
}
