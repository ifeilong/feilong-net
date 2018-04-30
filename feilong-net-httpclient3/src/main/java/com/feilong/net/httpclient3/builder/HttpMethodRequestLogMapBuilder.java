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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class HttpMethodUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public final class HttpMethodRequestLogMapBuilder{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpMethodRequestLogMapBuilder.class);

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private HttpMethodRequestLogMapBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 请求信息LOGGER.
     * 
     * @param httpMethod
     *            the http method
     * @return the http method attribute map for log
     */
    static Map<String, Object> build(HttpMethod httpMethod){
        Map<String, Object> map = newLinkedHashMap();
        try{
            map.put("httpMethod.getName()", httpMethod.getName());
            map.put("httpMethod.getURI()", httpMethod.getURI().toString());
            map.put("httpMethod.getPath()", httpMethod.getPath());
            map.put("httpMethod.getQueryString()", httpMethod.getQueryString());

            map.put("httpMethod.getRequestHeaders()", httpMethod.getRequestHeaders());

            map.put("httpMethod.getDoAuthentication()", httpMethod.getDoAuthentication());
            map.put("httpMethod.getFollowRedirects()", httpMethod.getFollowRedirects());
            map.put("httpMethod.getHostAuthState()", httpMethod.getHostAuthState().toString());

            // HttpMethodParams httpMethodParams = httpMethod.getParams();
            // map.put("httpMethod.getParams()", httpMethodParams);
            map.put("httpMethod.getProxyAuthState()", httpMethod.getProxyAuthState().toString());

        }catch (Exception e){
            LOGGER.error(e.getClass().getName(), e);
        }
        return map;
    }
}
