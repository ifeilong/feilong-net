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
package com.feilong.net.httpclient4.builder.httpurirequest;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;

import com.feilong.net.HttpMethodType;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class HttpUriRequestFactory{

    /** Don't let anyone instantiate this class. */
    private HttpUriRequestFactory(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
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
     * @param connectionConfig
     *            the connection config
     * @return the http uri request
     */
    public static HttpUriRequest buildHttpUriRequest(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        RequestConfig requestConfig = RequestConfigBuilder.build(connectionConfig);

        //---------------------------------------------------------------

        HttpMethodType httpMethodType = httpRequest.getHttpMethodType();
        switch (httpMethodType) {
            case GET:
                return HttpGetBuilder.build(httpRequest, requestConfig);
            case POST:
                return HttpPostBuilder.build(httpRequest, requestConfig);
            default:
                throw new NotImplementedException(httpMethodType + " is not implemented!");
        }
    }
}
