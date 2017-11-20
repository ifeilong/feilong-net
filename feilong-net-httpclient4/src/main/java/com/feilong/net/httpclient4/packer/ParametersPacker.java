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

import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 专门用来封装请求头 {@link org.apache.http.HttpMessage#setHeader(String, String)}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class ParametersPacker{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersPacker.class);

    /**
     * 设置 parameters.
     *
     * @param httpUriRequest
     *            the http uri request
     * @param paramMap
     *            请求param Map, 如果 <code>paramMap</code> 是null或者empty,什么都不做<br>
     */
    public static void setParameters(HttpUriRequest httpUriRequest,Map<String, String> paramMap){
        if (isNullOrEmpty(paramMap)){
            LOGGER.debug("input param [paramMap] is null or empty ,skip!");
            return;
        }

        //-------------set params--------------------------------------------------
        for (Map.Entry<String, String> entry : paramMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            httpUriRequest.getParams().setParameter(key, value);

            if (LOGGER.isTraceEnabled()){
                LOGGER.trace("httpUriRequest.setHeader({}, {})", key, value);
            }
        }
    }
}
