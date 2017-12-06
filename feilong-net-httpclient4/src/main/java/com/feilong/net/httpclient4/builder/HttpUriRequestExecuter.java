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

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import com.feilong.net.UncheckedHttpException;

/**
 * 专门发送请求 <code>httpUriRequest</code> .
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public final class HttpUriRequestExecuter{

    /** Don't let anyone instantiate this class. */
    private HttpUriRequestExecuter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Execute.
     *
     * @param httpUriRequest
     *            the http uri request
     * @return the http response
     */
    public static HttpResponse execute(HttpUriRequest httpUriRequest){
        try{
            HttpClient httpClient = HttpClientBuilder.build();
            return httpClient.execute(httpUriRequest);
        }catch (IOException e){
            throw new UncheckedHttpException(e);
        }
    }

}
