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

import static com.feilong.core.Validator.isNullOrEmpty;

import java.net.HttpURLConnection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.entity.HttpRequest;

/**
 * 封装 request header.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class RequestHeadersPacker{

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHeadersPacker.class);

    /** Don't let anyone instantiate this class. */
    private RequestHeadersPacker(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * @param httpURLConnection
     * @param headerMap
     * @since 1.10.4
     */
    public static void pack(HttpURLConnection httpURLConnection,Map<String, String> headerMap){

        //do with RequestProperty
        //设置默认的UA, 你可以使用headerMap来覆盖
        httpURLConnection.setRequestProperty("User-Agent", HttpRequest.DEFAULT_USER_AGENT);

        if (isNullOrEmpty(headerMap)){
            LOGGER.debug("headerMap is null or empty ,skip");
            return;
        }

        for (Map.Entry<String, String> entry : headerMap.entrySet()){
            httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

}
