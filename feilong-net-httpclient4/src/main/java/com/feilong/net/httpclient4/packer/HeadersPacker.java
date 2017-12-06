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
package com.feilong.net.httpclient4.packer;

import static com.feilong.core.Validator.isNullOrEmpty;

import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.entity.HttpRequest;

/**
 * 专门用来封装请求头 {@link org.apache.http.HttpMessage#setHeader(String, String)}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public final class HeadersPacker{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HeadersPacker.class);

    //---------------------------------------------------------------

    /**
     * 设置 headers.
     *
     * @param httpUriRequest
     *            the http uri request
     * @param headerMap
     *            请求header map, 如果 <code>headerMap</code> 是null或者empty,什么都不做<br>
     */
    public static void setHeaders(HttpUriRequest httpUriRequest,Map<String, String> headerMap){
        if (isNullOrEmpty(headerMap)){
            LOGGER.trace("input [headerMap] is null or empty ,skip!");
            return;
        }

        //-------------set header------------------------------------------
        for (Map.Entry<String, String> entry : headerMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            httpUriRequest.setHeader(key, value);

            if (LOGGER.isTraceEnabled()){
                LOGGER.trace("httpUriRequest.setHeader({}, {})", key, value);
            }
        }
    }

    /**
     * 设置默认头.
     * 
     * @param httpUriRequest
     */
    public static void setDefaultHeader(HttpUriRequest httpUriRequest){
        httpUriRequest.setHeader(HttpHeaders.USER_AGENT, HttpRequest.DEFAULT_USER_AGENT);
        //httpUriRequest.setHeader("Connection", "keep-alive");
    }
}