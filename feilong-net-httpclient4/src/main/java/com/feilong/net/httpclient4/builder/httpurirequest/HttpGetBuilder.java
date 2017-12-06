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

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;

import com.feilong.net.UncheckedHttpException;
import com.feilong.net.entity.HttpRequest;

/**
 * The Class HttpGetBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
final class HttpGetBuilder{

    /** Don't let anyone instantiate this class. */
    private HttpGetBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the http get.
     *
     * @param httpRequest
     *            the http request
     * @param requestConfig
     *            the request config
     * @return the http uri request
     */
    static HttpUriRequest build(HttpRequest httpRequest,RequestConfig requestConfig){
        HttpGet httpGet = new HttpGet(buildUri(httpRequest));

        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    /**
     * Builds the uri.
     *
     * @param httpRequest
     *            the http request
     * @return the uri
     */
    private static URI buildUri(HttpRequest httpRequest){
        try{
            URIBuilder uriBuilder = URIBuilderBuilder.builder(httpRequest);
            return uriBuilder.build();
        }catch (URISyntaxException e){
            throw new UncheckedHttpException(e);
        }
    }
}
