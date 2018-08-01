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

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import com.feilong.net.entity.HttpRequest;

/**
 * The Class HttpPutBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.5
 */
final class HttpPutBuilder{

    /** Don't let anyone instantiate this class. */
    private HttpPutBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the http post.
     *
     * @param httpRequest
     *            the http request
     * @param requestConfig
     *            the request config
     * @return the http uri request
     */
    static HttpUriRequest build(HttpRequest httpRequest,RequestConfig requestConfig){
        HttpEntity httpEntity = HttpEntityBuilder.build(httpRequest);

        //---------------------------------------------------------------
        HttpPut httpPut = new HttpPut(httpRequest.getUri());
        httpPut.setEntity(httpEntity);
        httpPut.setConfig(requestConfig);

        return httpPut;
    }
}
