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
package com.feilong.net.httpclient3.builder;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;

import com.feilong.net.httpclient3.HttpClientConfig;

/**
 * The Class HttpMethodUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
public final class HttpMethodResponseLogMapBuilder{

    /** Don't let anyone instantiate this class. */
    private HttpMethodResponseLogMapBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 返回信息LOGGER.
     *
     * @param httpMethod
     *            the http method
     * @param httpClientConfig
     *            the http client config
     * @return the http method response attribute map for log
     */
    public static Map<String, Object> build(HttpMethod httpMethod,HttpClientConfig httpClientConfig){
        Map<String, Object> map = newLinkedHashMap();

        map.put("httpMethod.getRequestHeaders()-->map", NameValuePairUtil.toMap(httpMethod.getRequestHeaders()));

        map.put("httpMethod.getStatusCode()", buildStatusCode(httpMethod));
        map.put("httpMethod.getStatusText()", buildStatusText(httpMethod));
        map.put("httpMethod.getStatusLine()", "" + httpMethod.getStatusLine());

        map.put("httpMethod.getResponseHeaders()-->map", NameValuePairUtil.toMap(httpMethod.getResponseHeaders()));

        map.put("httpMethod.getResponseFooters()", httpMethod.getResponseFooters());
        map.put("httpClientConfig", httpClientConfig);
        return map;
    }

    //---------------------------------------------------------------

    /**
     * Builds the status text.
     *
     * @param httpMethod
     *            the http method
     * @return the string
     */
    private static String buildStatusText(HttpMethod httpMethod){
        try{
            return httpMethod.getStatusText();
        }catch (Exception e){
            return e.getClass().getName() + " " + e.getMessage();
        }
    }

    /**
     * Builds the status code.
     *
     * @param httpMethod
     *            the http method
     * @return the object
     */
    private static Object buildStatusCode(HttpMethod httpMethod){
        try{
            return httpMethod.getStatusCode();
        }catch (Exception e){
            return e.getClass().getName() + " " + e.getMessage();
        }
    }
}
